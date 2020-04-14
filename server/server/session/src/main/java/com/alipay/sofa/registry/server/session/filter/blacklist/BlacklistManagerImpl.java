/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.alipay.sofa.registry.server.session.filter.blacklist;

import com.alipay.sofa.registry.common.model.constants.ValueConstants;
import com.alipay.sofa.registry.common.model.metaserver.FetchProvideDataRequest;
import com.alipay.sofa.registry.common.model.metaserver.ProvideData;
import com.alipay.sofa.registry.common.model.store.URL;
import com.alipay.sofa.registry.log.Logger;
import com.alipay.sofa.registry.log.LoggerFactory;
import com.alipay.sofa.registry.remoting.exchange.NodeExchanger;
import com.alipay.sofa.registry.remoting.exchange.message.Request;
import com.alipay.sofa.registry.remoting.exchange.message.Response;
import com.alipay.sofa.registry.server.session.bootstrap.SessionServerConfig;
import com.alipay.sofa.registry.server.session.node.RaftClientManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 *
 * @author shangyu.wh
 * @version 1.0: BlacklistManagerImpl.java, v 0.1 2019-06-19 18:30 shangyu.wh Exp $
 */
public class BlacklistManagerImpl implements BlacklistManager {

    private static final Logger   LOGGER              = LoggerFactory
                                                          .getLogger(BlacklistManagerImpl.class);

    private static final Logger   EXCHANGE_LOGGER     = LoggerFactory.getLogger("SESSION-EXCHANGE");

    @Autowired
    private SessionServerConfig   sessionServerConfig;

    @Autowired
    protected NodeExchanger       metaNodeExchanger;

    @Autowired
    protected RaftClientManager   raftClientManager;

    private List<BlacklistConfig> blacklistConfigList = new ArrayList();

    @Override
    public void load() {
        fetchStopPushSwitch();
    }

    @Override
    public List<BlacklistConfig> getBlacklistConfigList() {
        return blacklistConfigList;
    }

    @Override
    public void setBlacklistConfigList(List<BlacklistConfig> blacklistConfigList) {
        this.blacklistConfigList = blacklistConfigList;
    }

    private void fetchStopPushSwitch() {

        Object ret = sendMetaRequest();
        if (ret instanceof ProvideData) {
            ProvideData provideData = (ProvideData) ret;
            if (provideData.getProvideData() == null
                || provideData.getProvideData().getObject() == null) {
                LOGGER.info("Fetch session blacklist no data existed,current config not change!");
                return;
            }
            String data = (String) provideData.getProvideData().getObject();
            if (data != null) {
                convertBlacklistConfig(data);
                EXCHANGE_LOGGER.info("Fetch session blacklist data switch {} success!", data);
            } else {
                LOGGER.info("Fetch session blacklist data null,current config not change!");
            }
        } else {
            LOGGER.info("Fetch session blacklist data null,config not change!");
        }
    }

    private Object sendMetaRequest() {
        try {
            Request<FetchProvideDataRequest> request = new Request<FetchProvideDataRequest>() {
                @Override
                public FetchProvideDataRequest getRequestBody() {
                    return new FetchProvideDataRequest(ValueConstants.BLACK_LIST_DATA_ID);
                }

                @Override
                public URL getRequestUrl() {
                    return new URL(raftClientManager.getLeader().getIp(),
                        sessionServerConfig.getMetaServerPort());
                }
            };

            Response<FetchProvideDataRequest> response = metaNodeExchanger.request(request);

            if (response != null && response.getResult() != null) {
                EXCHANGE_LOGGER.info("Update blacklist info success!");
                return response.getResult();
            } else {
                LOGGER.error("Get blacklist info error!No response receive!");
                throw new RuntimeException("Get blacklist info error!No response receive!");
            }
        } catch (Exception e) {
            LOGGER.error("Get blacklist info error!", e);
            throw new RuntimeException("Get blacklist info error! ", e);
        }
    }

    /**
     * @see BlacklistManager#convertBlacklistConfig(String)
     */
    public Map<String, Map<String, Set<String>>> convertBlacklistConfig(String config) {

        TypeReference<HashMap<String, HashMap<String, HashSet<String>>>> typeReference = new TypeReference<HashMap<String, HashMap<String, HashSet<String>>>>() {
        };

        ObjectMapper mapper = new ObjectMapper();

        Map<String, Map<String, Set<String>>> blacklistConfigMap;
        try {
            blacklistConfigMap = mapper.readValue(config, typeReference);
        } catch (IOException e) {
            LOGGER.error("Parser config json error!", e);
            return null;
        }
        if (null == blacklistConfigMap) {
            LOGGER.info("[cmd] setBlacklistConfig fail, params is null");
            return null;
        }
        try {
            List<BlacklistConfig> blacklistConfigs = new ArrayList();
            for (Entry<String, Map<String, Set<String>>> configEntry : blacklistConfigMap
                .entrySet()) {
                BlacklistConfig blacklistConfig = new BlacklistConfig();
                blacklistConfig.setType(configEntry.getKey());

                List<MatchType> matchTypeList = new ArrayList();

                Map<String, Set<String>> matchTypeMap = configEntry.getValue();
                for (Entry<String, Set<String>> typeEntry : matchTypeMap.entrySet()) {
                    String type = typeEntry.getKey();

                    MatchType<String> ipFullMatchType = new MatchType();
                    ipFullMatchType.setType(type);
                    ipFullMatchType.setPatternSet(typeEntry.getValue());
                    matchTypeList.add(ipFullMatchType);
                }
                blacklistConfig.setMatchTypes(matchTypeList);
                blacklistConfigs.add(blacklistConfig);
            }

            setBlacklistConfigList(blacklistConfigs);
            return blacklistConfigMap;
        } catch (Exception e) {
            LOGGER.error("[cmd] setBlacklistConfig error", e);
            return null;
        }
    }
}