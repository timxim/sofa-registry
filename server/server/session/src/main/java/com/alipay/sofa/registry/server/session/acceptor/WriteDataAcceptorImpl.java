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
package com.alipay.sofa.registry.server.session.acceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;

import com.alipay.sofa.registry.server.session.bootstrap.SessionServerConfig;
import com.alipay.sofa.registry.server.session.renew.RenewService;
import com.alipay.sofa.registry.task.listener.TaskListenerManager;

/**
 *
 * @author kezhu.wukz
 * @author shangyu.wh
 * @version 1.0: WriteDataAcceptor.java, v 0.1 2019-06-06 12:45 shangyu.wh Exp $
 */
public class WriteDataAcceptorImpl implements WriteDataAcceptor {

    @Autowired
    private TaskListenerManager             taskListenerManager;

    @Autowired
    private SessionServerConfig             sessionServerConfig;

    @Autowired
    private RenewService                    renewService;

    /**
     * acceptor for all write data request
     * key:connectId
     * value:writeRequest processor
     *
     */
    private Map<String, WriteDataProcessor> writeDataProcessors = new ConcurrentHashMap();

    public void accept(WriteDataRequest request) {
        String connectId = request.getConnectId();
        WriteDataProcessor writeDataProcessor = writeDataProcessors.computeIfAbsent(connectId,
                key -> new WriteDataProcessor(connectId, taskListenerManager, sessionServerConfig, renewService));

        writeDataProcessor.process(request);
    }

    public void remove(String connectId) {
        writeDataProcessors.remove(connectId);
    }
}