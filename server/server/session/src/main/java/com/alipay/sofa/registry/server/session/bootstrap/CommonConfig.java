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
package com.alipay.sofa.registry.server.session.bootstrap;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.beans.factory.annotation.Value;

/**
 *
 * @author shangyu.wh
 * @version $Id: CommonConfig.java, v 0.1 2018-05-05 15:16 shangyu.wh Exp $
 */
public class CommonConfig {

    /**
     * server local data center, get from System Property
     * example: nodes.localDataCenter=DefaultDataCenter
     */
    @Value("${nodes.localDataCenter}")
    private String                                           localDataCenter;

    /**
     * server local region, get from System Property
     * example: nodes.localRegion=DEFAULT_ZONE
     */
    @Value("${nodes.localRegion}")
    private String                                           localRegion;

    /**
     * metaNodeInfo, get from System Property
     * example: nodes.metaNode=DefaultDataCenter:192.168.xxx.xxx,192.168.xxx.xxx,192.168.xxx.xxx|AnotherDataCenter:192.168.xxx.xxx,192.168.xxx.xxx,192.168.xxx.xxx
     */
    @Value("#{PropertySplitter.mapOfList('${nodes.metaNode}')}")
    private Map<String/*dataCenterId*/, Collection<String>> metaNode;

    /**
     * Getter method for property <tt>metaNode</tt>.
     *
     * @return property value of metaNode
     */
    public Map<String, Collection<String>> getMetaNode() {
        return metaNode;
    }

    /**
     * Setter method for property <tt>metaNode</tt>.
     *
     * @param metaNode  value to be assigned to property metaNode
     */
    public void setMetaNode(Map<String, Collection<String>> metaNode) {
        this.metaNode = metaNode;
    }

    /**
     * Getter method for property <tt>localDataCenter</tt>.
     *
     * @return property value of localDataCenter
     */
    public String getLocalDataCenter() {
        return localDataCenter;
    }

    /**
     * Getter method for property <tt>localRegion</tt>.
     *
     * @return property value of localRegion
     */
    public String getLocalRegion() {
        return localRegion;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}