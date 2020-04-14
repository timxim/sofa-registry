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
package com.alipay.sofa.registry.server.meta.bootstrap;

/**
 *
 * @author shangyu.wh
 * @version $Id: MetaServerConfig.java, v 0.1 2018-01-16 10:58 shangyu.wh Exp $
 */
public interface MetaServerConfig {
    int getSessionServerPort();

    int getDataServerPort();

    int getMetaServerPort();

    int getHttpServerPort();

    int getRaftServerPort();

    int getSchedulerHeartbeatTimeout();

    int getSchedulerHeartbeatFirstDelay();

    int getSchedulerHeartbeatExpBackOffBound();

    int getSchedulerGetDataChangeTimeout();

    int getSchedulerGetDataChangeFirstDelay();

    int getSchedulerGetDataChangeExpBackOffBound();

    int getSchedulerConnectMetaServerTimeout();

    int getSchedulerConnectMetaServerFirstDelay();

    int getSchedulerConnectMetaServerExpBackOffBound();

    int getSchedulerCheckNodeListChangePushTimeout();

    int getSchedulerCheckNodeListChangePushFirstDelay();

    int getSchedulerCheckNodeListChangePushExpBackOffBound();

    int getDataNodeExchangeTimeout();

    int getSessionNodeExchangeTimeout();

    int getMetaNodeExchangeTimeout();

    DecisionMode getDecisionMode();

    int getDataCenterChangeNotifyTaskRetryTimes();

    int getDataNodeChangePushTaskRetryTimes();

    int getGetDataCenterChangeListTaskRetryTimes();

    int getReceiveStatusConfirmNotifyTaskRetryTimes();

    int getSessionNodeChangePushTaskRetryTimes();

    String getRaftGroup();

    String getRaftDataPath();

    boolean isEnableMetrics();

    int getRockDBCacheSize();

    int getHeartbeatCheckExecutorMinSize();

    int getHeartbeatCheckExecutorMaxSize();

    int getHeartbeatCheckExecutorQueueSize();

    int getCheckDataChangeExecutorMinSize();

    int getCheckDataChangeExecutorMaxSize();

    int getCheckDataChangeExecutorQueueSize();

    int getGetOtherDataCenterChangeExecutorMinSize();

    int getGetOtherDataCenterChangeExecutorMaxSize();

    int getGetOtherDataCenterChangeExecutorQueueSize();

    int getConnectMetaServerExecutorMinSize();

    int getConnectMetaServerExecutorMaxSize();

    int getConnectMetaServerExecutorQueueSize();

    int getCheckNodeListChangePushExecutorMinSize();

    int getCheckNodeListChangePushExecutorMaxSize();

    int getCheckNodeListChangePushExecutorQueueSize();

    int getRaftClientRefreshExecutorMinSize();

    int getRaftClientRefreshExecutorMaxSize();

    int getRaftClientRefreshExecutorQueueSize();

    int getMetaSchedulerPoolSize();

    /**
     * decision mode enum
     */
    enum DecisionMode {
        RUNTIME, OFF
    }

}