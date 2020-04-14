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

import java.io.File;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.springframework.boot.context.properties.ConfigurationProperties;

import com.alipay.sofa.registry.common.model.constants.ValueConstants;

/**
 *
 * @author shangyu.wh
 * @version $Id: MetaServerConfigBean.java, v 0.1 2018-01-16 11:01 shangyu.wh Exp $
 */
@ConfigurationProperties(prefix = MetaServerConfigBean.PREFIX)
public class MetaServerConfigBean implements MetaServerConfig {

    public static final String PREFIX                                          = "meta.server";

    private int                sessionServerPort                               = 9610;

    private int                dataServerPort                                  = 9611;

    private int                metaServerPort                                  = 9612;

    private int                httpServerPort                                  = 9615;

    private int                schedulerHeartbeatTimeout                       = 3;

    private int                schedulerHeartbeatFirstDelay                    = 3;

    private int                schedulerHeartbeatExpBackOffBound               = 10;

    private int                schedulerGetDataChangeTimeout                   = 5;

    private int                schedulerGetDataChangeFirstDelay                = 5;

    private int                schedulerGetDataChangeExpBackOffBound           = 5;

    private int                schedulerConnectMetaServerTimeout               = 3;

    private int                schedulerConnectMetaServerFirstDelay            = 3;

    private int                schedulerConnectMetaServerExpBackOffBound       = 10;

    private int                schedulerCheckNodeListChangePushTimeout         = 3;

    private int                schedulerCheckNodeListChangePushFirstDelay      = 1;

    private int                schedulerCheckNodeListChangePushExpBackOffBound = 10;

    private int                dataNodeExchangeTimeout                         = 3000;

    private int                sessionNodeExchangeTimeout                      = 3000;

    private int                metaNodeExchangeTimeout                         = 3000;

    private int                dataCenterChangeNotifyTaskRetryTimes            = 3;

    private int                dataNodeChangePushTaskRetryTimes                = 1;

    private int                getDataCenterChangeListTaskRetryTimes           = 3;

    private int                receiveStatusConfirmNotifyTaskRetryTimes        = 3;

    private int                sessionNodeChangePushTaskRetryTimes             = 3;

    /**
     * Whether to enable metrics for node.
     */
    private boolean            enableMetrics                                   = true;

    private DecisionMode       decisionMode;

    private String             raftDataPath                                    = System
                                                                                   .getProperty("user.home")
                                                                                 + File.separator
                                                                                 + "raftData";

    private int                rockDBCacheSize                                 = 64;           //64M

    private int                heartbeatCheckExecutorMinSize                   = 3;
    private int                heartbeatCheckExecutorMaxSize                   = 10;
    private int                heartbeatCheckExecutorQueueSize                 = 1024;

    private int                checkDataChangeExecutorMinSize                  = 3;
    private int                checkDataChangeExecutorMaxSize                  = 10;
    private int                checkDataChangeExecutorQueueSize                = 1024;

    private int                getOtherDataCenterChangeExecutorMinSize         = 3;
    private int                getOtherDataCenterChangeExecutorMaxSize         = 10;
    private int                getOtherDataCenterChangeExecutorQueueSize       = 1024;

    private int                connectMetaServerExecutorMinSize                = 3;
    private int                connectMetaServerExecutorMaxSize                = 10;
    private int                connectMetaServerExecutorQueueSize              = 1024;

    private int                checkNodeListChangePushExecutorMinSize          = 3;
    private int                checkNodeListChangePushExecutorMaxSize          = 10;
    private int                checkNodeListChangePushExecutorQueueSize        = 1024;

    private int                raftClientRefreshExecutorMinSize                = 3;
    private int                raftClientRefreshExecutorMaxSize                = 10;
    private int                raftClientRefreshExecutorQueueSize              = 1024;

    private int                metaSchedulerPoolSize                           = 6;

    @Override
    public int getSessionServerPort() {
        return sessionServerPort;
    }

    /**
     * Setter method for property <tt>sessionServerPort</tt>.
     *
     * @param sessionServerPort  value to be assigned to property sessionServerPort
     */
    public void setSessionServerPort(int sessionServerPort) {
        this.sessionServerPort = sessionServerPort;
    }

    @Override
    public int getDataServerPort() {
        return dataServerPort;
    }

    /**
     * Setter method for property <tt>dataServerPort</tt>.
     *
     * @param dataServerPort  value to be assigned to property dataServerPort
     */
    public void setDataServerPort(int dataServerPort) {
        this.dataServerPort = dataServerPort;
    }

    @Override
    public int getHttpServerPort() {
        return httpServerPort;
    }

    /**
     * Setter method for property <tt>httpServerPort</tt>.
     *
     * @param httpServerPort  value to be assigned to property httpServerPort
     */
    public void setHttpServerPort(int httpServerPort) {
        this.httpServerPort = httpServerPort;
    }

    /**
     * Getter method for property <tt>schedulerHeartbeatTimeout</tt>.
     *
     * @return property value of schedulerHeartbeatTimeout
     */
    @Override
    public int getSchedulerHeartbeatTimeout() {
        return schedulerHeartbeatTimeout;
    }

    /**
     * Setter method for property <tt>schedulerHeartbeatTimeout</tt>.
     *
     * @param schedulerHeartbeatTimeout  value to be assigned to property schedulerHeartbeatTimeout
     */
    public void setSchedulerHeartbeatTimeout(int schedulerHeartbeatTimeout) {
        this.schedulerHeartbeatTimeout = schedulerHeartbeatTimeout;
    }

    /**
     * Getter method for property <tt>schedulerHeartbeatFirstDelay</tt>.
     *
     * @return property value of schedulerHeartbeatFirstDelay
     */
    @Override
    public int getSchedulerHeartbeatFirstDelay() {
        return schedulerHeartbeatFirstDelay;
    }

    /**
     * Setter method for property <tt>schedulerHeartbeatFirstDelay</tt>.
     *
     * @param schedulerHeartbeatFirstDelay  value to be assigned to property schedulerHeartbeatFirstDelay
     */
    public void setSchedulerHeartbeatFirstDelay(int schedulerHeartbeatFirstDelay) {
        this.schedulerHeartbeatFirstDelay = schedulerHeartbeatFirstDelay;
    }

    /**
     * Getter method for property <tt>schedulerHeartbeatExpBackOffBound</tt>.
     *
     * @return property value of schedulerHeartbeatExpBackOffBound
     */
    @Override
    public int getSchedulerHeartbeatExpBackOffBound() {
        return schedulerHeartbeatExpBackOffBound;
    }

    /**
     * Setter method for property <tt>schedulerHeartbeatExpBackOffBound</tt>.
     *
     * @param schedulerHeartbeatExpBackOffBound  value to be assigned to property schedulerHeartbeatExpBackOffBound
     */
    public void setSchedulerHeartbeatExpBackOffBound(int schedulerHeartbeatExpBackOffBound) {
        this.schedulerHeartbeatExpBackOffBound = schedulerHeartbeatExpBackOffBound;
    }

    @Override
    public DecisionMode getDecisionMode() {
        return decisionMode;
    }

    /**
     * Setter method for property <tt>decisionMode</tt>.
     *
     * @param decisionMode  value to be assigned to property decisionMode
     */
    public void setDecisionMode(DecisionMode decisionMode) {
        this.decisionMode = decisionMode;
    }

    /**
     * Getter method for property <tt>dataNodeExchangeTimeout</tt>.
     *
     * @return property value of dataNodeExchangeTimeout
     */
    @Override
    public int getDataNodeExchangeTimeout() {
        return dataNodeExchangeTimeout;
    }

    /**
     * Setter method for property <tt>dataNodeExchangeTimeout</tt>.
     *
     * @param dataNodeExchangeTimeout  value to be assigned to property dataNodeExchangeTimeout
     */
    public void setDataNodeExchangeTimeout(int dataNodeExchangeTimeout) {
        this.dataNodeExchangeTimeout = dataNodeExchangeTimeout;
    }

    /**
     * Getter method for property <tt>sessionNodeExchangeTimeout</tt>.
     *
     * @return property value of sessionNodeExchangeTimeout
     */
    @Override
    public int getSessionNodeExchangeTimeout() {
        return sessionNodeExchangeTimeout;
    }

    /**
     * Setter method for property <tt>sessionNodeExchangeTimeout</tt>.
     *
     * @param sessionNodeExchangeTimeout  value to be assigned to property sessionNodeExchangeTimeout
     */
    public void setSessionNodeExchangeTimeout(int sessionNodeExchangeTimeout) {
        this.sessionNodeExchangeTimeout = sessionNodeExchangeTimeout;
    }

    /**
     * Getter method for property <tt>schedulerConnectMetaServerTimeout</tt>.
     *
     * @return property value of schedulerConnectMetaServerTimeout
     */
    @Override
    public int getSchedulerConnectMetaServerTimeout() {
        return schedulerConnectMetaServerTimeout;
    }

    /**
     * Setter method for property <tt>schedulerConnectMetaServerTimeout</tt>.
     *
     * @param schedulerConnectMetaServerTimeout  value to be assigned to property schedulerConnectMetaServerTimeout
     */
    public void setSchedulerConnectMetaServerTimeout(int schedulerConnectMetaServerTimeout) {
        this.schedulerConnectMetaServerTimeout = schedulerConnectMetaServerTimeout;
    }

    /**
     * Getter method for property <tt>schedulerConnectMetaServerFirstDelay</tt>.
     *
     * @return property value of schedulerConnectMetaServerFirstDelay
     */
    @Override
    public int getSchedulerConnectMetaServerFirstDelay() {
        return schedulerConnectMetaServerFirstDelay;
    }

    /**
     * Setter method for property <tt>schedulerConnectMetaServerFirstDelay</tt>.
     *
     * @param schedulerConnectMetaServerFirstDelay  value to be assigned to property schedulerConnectMetaServerFirstDelay
     */
    public void setSchedulerConnectMetaServerFirstDelay(int schedulerConnectMetaServerFirstDelay) {
        this.schedulerConnectMetaServerFirstDelay = schedulerConnectMetaServerFirstDelay;
    }

    /**
     * Getter method for property <tt>schedulerConnectMetaServerExpBackOffBound</tt>.
     *
     * @return property value of schedulerConnectMetaServerExpBackOffBound
     */
    @Override
    public int getSchedulerConnectMetaServerExpBackOffBound() {
        return schedulerConnectMetaServerExpBackOffBound;
    }

    /**
     * Setter method for property <tt>schedulerConnectMetaServerExpBackOffBound</tt>.
     *
     * @param schedulerConnectMetaServerExpBackOffBound  value to be assigned to property schedulerConnectMetaServerExpBackOffBound
     */
    public void setSchedulerConnectMetaServerExpBackOffBound(int schedulerConnectMetaServerExpBackOffBound) {
        this.schedulerConnectMetaServerExpBackOffBound = schedulerConnectMetaServerExpBackOffBound;
    }

    /**
     * Getter method for property <tt>metaServerPort</tt>.
     *
     * @return property value of metaServerPort
     */
    @Override
    public int getMetaServerPort() {
        return metaServerPort;
    }

    /**
     * Setter method for property <tt>metaServerPort</tt>.
     *
     * @param metaServerPort  value to be assigned to property metaServerPort
     */
    public void setMetaServerPort(int metaServerPort) {
        this.metaServerPort = metaServerPort;
    }

    /**
     * Getter method for property <tt>metaNodeExchangeTimeout</tt>.
     *
     * @return property value of metaNodeExchangeTimeout
     */
    @Override
    public int getMetaNodeExchangeTimeout() {
        return metaNodeExchangeTimeout;
    }

    /**
     * Setter method for property <tt>metaNodeExchangeTimeout</tt>.
     *
     * @param metaNodeExchangeTimeout  value to be assigned to property metaNodeExchangeTimeout
     */
    public void setMetaNodeExchangeTimeout(int metaNodeExchangeTimeout) {
        this.metaNodeExchangeTimeout = metaNodeExchangeTimeout;
    }

    /**
     * Getter method for property <tt>dataCenterChangeNotifyTaskRetryTimes</tt>.
     *
     * @return property value of dataCenterChangeNotifyTaskRetryTimes
     */
    @Override
    public int getDataCenterChangeNotifyTaskRetryTimes() {
        return dataCenterChangeNotifyTaskRetryTimes;
    }

    /**
     * Setter method for property <tt>dataCenterChangeNotifyTaskRetryTimes</tt>.
     *
     * @param dataCenterChangeNotifyTaskRetryTimes  value to be assigned to property dataCenterChangeNotifyTaskRetryTimes
     */
    public void setDataCenterChangeNotifyTaskRetryTimes(int dataCenterChangeNotifyTaskRetryTimes) {
        this.dataCenterChangeNotifyTaskRetryTimes = dataCenterChangeNotifyTaskRetryTimes;
    }

    /**
     * Getter method for property <tt>dataNodeChangePushTaskRetryTimes</tt>.
     *
     * @return property value of dataNodeChangePushTaskRetryTimes
     */
    @Override
    public int getDataNodeChangePushTaskRetryTimes() {
        return dataNodeChangePushTaskRetryTimes;
    }

    /**
     * Setter method for property <tt>dataNodeChangePushTaskRetryTimes</tt>.
     *
     * @param dataNodeChangePushTaskRetryTimes  value to be assigned to property dataNodeChangePushTaskRetryTimes
     */
    public void setDataNodeChangePushTaskRetryTimes(int dataNodeChangePushTaskRetryTimes) {
        this.dataNodeChangePushTaskRetryTimes = dataNodeChangePushTaskRetryTimes;
    }

    /**
     * Getter method for property <tt>getDataCenterChangeListTaskRetryTimes</tt>.
     *
     * @return property value of getDataCenterChangeListTaskRetryTimes
     */
    @Override
    public int getGetDataCenterChangeListTaskRetryTimes() {
        return getDataCenterChangeListTaskRetryTimes;
    }

    /**
     * Setter method for property <tt>getDataCenterChangeListTaskRetryTimes</tt>.
     *
     * @param getDataCenterChangeListTaskRetryTimes  value to be assigned to property getDataCenterChangeListTaskRetryTimes
     */
    public void setGetDataCenterChangeListTaskRetryTimes(int getDataCenterChangeListTaskRetryTimes) {
        this.getDataCenterChangeListTaskRetryTimes = getDataCenterChangeListTaskRetryTimes;
    }

    /**
     * Getter method for property <tt>receiveStatusConfirmNotifyTaskRetryTimes</tt>.
     *
     * @return property value of receiveStatusConfirmNotifyTaskRetryTimes
     */
    @Override
    public int getReceiveStatusConfirmNotifyTaskRetryTimes() {
        return receiveStatusConfirmNotifyTaskRetryTimes;
    }

    /**
     * Setter method for property <tt>receiveStatusConfirmNotifyTaskRetryTimes</tt>.
     *
     * @param receiveStatusConfirmNotifyTaskRetryTimes  value to be assigned to property receiveStatusConfirmNotifyTaskRetryTimes
     */
    public void setReceiveStatusConfirmNotifyTaskRetryTimes(int receiveStatusConfirmNotifyTaskRetryTimes) {
        this.receiveStatusConfirmNotifyTaskRetryTimes = receiveStatusConfirmNotifyTaskRetryTimes;
    }

    /**
     * Getter method for property <tt>sessionNodeChangePushTaskRetryTimes</tt>.
     *
     * @return property value of sessionNodeChangePushTaskRetryTimes
     */
    @Override
    public int getSessionNodeChangePushTaskRetryTimes() {
        return sessionNodeChangePushTaskRetryTimes;
    }

    /**
     * Setter method for property <tt>sessionNodeChangePushTaskRetryTimes</tt>.
     *
     * @param sessionNodeChangePushTaskRetryTimes  value to be assigned to property sessionNodeChangePushTaskRetryTimes
     */
    public void setSessionNodeChangePushTaskRetryTimes(int sessionNodeChangePushTaskRetryTimes) {
        this.sessionNodeChangePushTaskRetryTimes = sessionNodeChangePushTaskRetryTimes;
    }

    /**
     * Getter method for property <tt>schedulerCheckNodeListChangePushTimeout</tt>.
     *
     * @return property value of schedulerCheckNodeListChangePushTimeout
     */
    @Override
    public int getSchedulerCheckNodeListChangePushTimeout() {
        return schedulerCheckNodeListChangePushTimeout;
    }

    /**
     * Setter method for property <tt>schedulerCheckNodeListChangePushTimeout</tt>.
     *
     * @param schedulerCheckNodeListChangePushTimeout  value to be assigned to property schedulerCheckNodeListChangePushTimeout
     */
    public void setSchedulerCheckNodeListChangePushTimeout(int schedulerCheckNodeListChangePushTimeout) {
        this.schedulerCheckNodeListChangePushTimeout = schedulerCheckNodeListChangePushTimeout;
    }

    /**
     * Getter method for property <tt>schedulerCheckNodeListChangePushFirstDelay</tt>.
     *
     * @return property value of schedulerCheckNodeListChangePushFirstDelay
     */
    @Override
    public int getSchedulerCheckNodeListChangePushFirstDelay() {
        return schedulerCheckNodeListChangePushFirstDelay;
    }

    /**
     * Setter method for property <tt>schedulerCheckNodeListChangePushFirstDelay</tt>.
     *
     * @param schedulerCheckNodeListChangePushFirstDelay  value to be assigned to property schedulerCheckNodeListChangePushFirstDelay
     */
    public void setSchedulerCheckNodeListChangePushFirstDelay(int schedulerCheckNodeListChangePushFirstDelay) {
        this.schedulerCheckNodeListChangePushFirstDelay = schedulerCheckNodeListChangePushFirstDelay;
    }

    /**
     * Getter method for property <tt>schedulerCheckNodeListChangePushExpBackOffBound</tt>.
     *
     * @return property value of schedulerCheckNodeListChangePushExpBackOffBound
     */
    @Override
    public int getSchedulerCheckNodeListChangePushExpBackOffBound() {
        return schedulerCheckNodeListChangePushExpBackOffBound;
    }

    /**
     * Setter method for property <tt>schedulerCheckNodeListChangePushExpBackOffBound</tt>.
     *
     * @param schedulerCheckNodeListChangePushExpBackOffBound  value to be assigned to property
     *                                                         schedulerCheckNodeListChangePushExpBackOffBound
     */
    public void setSchedulerCheckNodeListChangePushExpBackOffBound(int schedulerCheckNodeListChangePushExpBackOffBound) {
        this.schedulerCheckNodeListChangePushExpBackOffBound = schedulerCheckNodeListChangePushExpBackOffBound;
    }

    /**
     * Getter method for property <tt>schedulerGetDataChangeTimeout</tt>.
     *
     * @return property value of schedulerGetDataChangeTimeout
     */
    @Override
    public int getSchedulerGetDataChangeTimeout() {
        return schedulerGetDataChangeTimeout;
    }

    /**
     * Setter method for property <tt>schedulerGetDataChangeTimeout</tt>.
     *
     * @param schedulerGetDataChangeTimeout  value to be assigned to property schedulerGetDataChangeTimeout
     */
    public void setSchedulerGetDataChangeTimeout(int schedulerGetDataChangeTimeout) {
        this.schedulerGetDataChangeTimeout = schedulerGetDataChangeTimeout;
    }

    /**
     * Getter method for property <tt>schedulerGetDataChangeFirstDelay</tt>.
     *
     * @return property value of schedulerGetDataChangeFirstDelay
     */
    @Override
    public int getSchedulerGetDataChangeFirstDelay() {
        return schedulerGetDataChangeFirstDelay;
    }

    /**
     * Setter method for property <tt>schedulerGetDataChangeFirstDelay</tt>.
     *
     * @param schedulerGetDataChangeFirstDelay  value to be assigned to property schedulerGetDataChangeFirstDelay
     */
    public void setSchedulerGetDataChangeFirstDelay(int schedulerGetDataChangeFirstDelay) {
        this.schedulerGetDataChangeFirstDelay = schedulerGetDataChangeFirstDelay;
    }

    /**
     * Getter method for property <tt>schedulerGetDataChangeExpBackOffBound</tt>.
     *
     * @return property value of schedulerGetDataChangeExpBackOffBound
     */
    @Override
    public int getSchedulerGetDataChangeExpBackOffBound() {
        return schedulerGetDataChangeExpBackOffBound;
    }

    /**
     * Setter method for property <tt>schedulerGetDataChangeExpBackOffBound</tt>.
     *
     * @param schedulerGetDataChangeExpBackOffBound  value to be assigned to property schedulerGetDataChangeExpBackOffBound
     */
    public void setSchedulerGetDataChangeExpBackOffBound(int schedulerGetDataChangeExpBackOffBound) {
        this.schedulerGetDataChangeExpBackOffBound = schedulerGetDataChangeExpBackOffBound;
    }

    /**
     * Getter method for property <tt>raftGroup</tt>.
     *
     * @return property value of raftGroup
     */
    @Override
    public String getRaftGroup() {
        return ValueConstants.RAFT_SERVER_GROUP;
    }

    /**
     * Getter method for property <tt>raftServerPort</tt>.
     *
     * @return property value of raftServerPort
     */
    @Override
    public int getRaftServerPort() {
        return ValueConstants.RAFT_SERVER_PORT;
    }

    /**
     * Getter method for property <tt>raftDataPath</tt>.
     *
     * @return property value of raftDataPath
     */
    @Override
    public String getRaftDataPath() {
        return raftDataPath;
    }

    /**
     * Setter method for property <tt>raftDataPath</tt>.
     *
     * @param raftDataPath  value to be assigned to property raftDataPath
     */
    public void setRaftDataPath(String raftDataPath) {
        this.raftDataPath = raftDataPath;
    }

    @Override
    public boolean isEnableMetrics() {
        return enableMetrics;
    }

    /**
     * Setter method for property <tt>enableMetrics</tt>.
     *
     * @param enableMetrics  value to be assigned to property enableMetrics
     */
    public void setEnableMetrics(boolean enableMetrics) {
        this.enableMetrics = enableMetrics;
    }

    /**
     * Getter method for property <tt>RockDBCacheSize</tt>.
     *
     * @return property value of RockDBCacheSize
     */
    public int getRockDBCacheSize() {
        return rockDBCacheSize;
    }

    /**
     * Setter method for property <tt>RockDBCacheSize</tt>.
     *
     * @param rockDBCacheSize  value to be assigned to property RockDBCacheSize
     */
    public void setRockDBCacheSize(int rockDBCacheSize) {
        this.rockDBCacheSize = rockDBCacheSize;
    }

    /**
     * Getter method for property <tt>heartbeatCheckExecutorMinSize</tt>.
     *
     * @return property value of heartbeatCheckExecutorMinSize
     */
    @Override
    public int getHeartbeatCheckExecutorMinSize() {
        return heartbeatCheckExecutorMinSize;
    }

    /**
     * Getter method for property <tt>heartbeatCheckExecutorMaxSize</tt>.
     *
     * @return property value of heartbeatCheckExecutorMaxSize
     */
    @Override
    public int getHeartbeatCheckExecutorMaxSize() {
        return heartbeatCheckExecutorMaxSize;
    }

    /**
     * Getter method for property <tt>heartbeatCheckExecutorQueueSize</tt>.
     *
     * @return property value of heartbeatCheckExecutorQueueSize
     */
    @Override
    public int getHeartbeatCheckExecutorQueueSize() {
        return heartbeatCheckExecutorQueueSize;
    }

    /**
     * Getter method for property <tt>checkDataChangeExecutorMinSize</tt>.
     *
     * @return property value of checkDataChangeExecutorMinSize
     */
    @Override
    public int getCheckDataChangeExecutorMinSize() {
        return checkDataChangeExecutorMinSize;
    }

    /**
     * Getter method for property <tt>checkDataChangeExecutorMaxSize</tt>.
     *
     * @return property value of checkDataChangeExecutorMaxSize
     */
    @Override
    public int getCheckDataChangeExecutorMaxSize() {
        return checkDataChangeExecutorMaxSize;
    }

    /**
     * Getter method for property <tt>checkDataChangeExecutorQueueSize</tt>.
     *
     * @return property value of checkDataChangeExecutorQueueSize
     */
    @Override
    public int getCheckDataChangeExecutorQueueSize() {
        return checkDataChangeExecutorQueueSize;
    }

    /**
     * Getter method for property <tt>getOtherDataCenterChangeExecutorMinSize</tt>.
     *
     * @return property value of getOtherDataCenterChangeExecutorMinSize
     */
    @Override
    public int getGetOtherDataCenterChangeExecutorMinSize() {
        return getOtherDataCenterChangeExecutorMinSize;
    }

    /**
     * Getter method for property <tt>getOtherDataCenterChangeExecutorMaxSize</tt>.
     *
     * @return property value of getOtherDataCenterChangeExecutorMaxSize
     */
    @Override
    public int getGetOtherDataCenterChangeExecutorMaxSize() {
        return getOtherDataCenterChangeExecutorMaxSize;
    }

    /**
     * Getter method for property <tt>getOtherDataCenterChangeExecutorQueueSize</tt>.
     *
     * @return property value of getOtherDataCenterChangeExecutorQueueSize
     */
    @Override
    public int getGetOtherDataCenterChangeExecutorQueueSize() {
        return getOtherDataCenterChangeExecutorQueueSize;
    }

    /**
     * Getter method for property <tt>connectMetaServerExecutorMinSize</tt>.
     *
     * @return property value of connectMetaServerExecutorMinSize
     */
    @Override
    public int getConnectMetaServerExecutorMinSize() {
        return connectMetaServerExecutorMinSize;
    }

    /**
     * Getter method for property <tt>connectMetaServerExecutorMaxSize</tt>.
     *
     * @return property value of connectMetaServerExecutorMaxSize
     */
    @Override
    public int getConnectMetaServerExecutorMaxSize() {
        return connectMetaServerExecutorMaxSize;
    }

    /**
     * Getter method for property <tt>connectMetaServerExecutorQueueSize</tt>.
     *
     * @return property value of connectMetaServerExecutorQueueSize
     */
    @Override
    public int getConnectMetaServerExecutorQueueSize() {
        return connectMetaServerExecutorQueueSize;
    }

    /**
     * Getter method for property <tt>checkNodeListChangePushExecutorMinSize</tt>.
     *
     * @return property value of checkNodeListChangePushExecutorMinSize
     */
    @Override
    public int getCheckNodeListChangePushExecutorMinSize() {
        return checkNodeListChangePushExecutorMinSize;
    }

    /**
     * Getter method for property <tt>checkNodeListChangePushExecutorMaxSize</tt>.
     *
     * @return property value of checkNodeListChangePushExecutorMaxSize
     */
    @Override
    public int getCheckNodeListChangePushExecutorMaxSize() {
        return checkNodeListChangePushExecutorMaxSize;
    }

    /**
     * Getter method for property <tt>checkNodeListChangePushExecutorQueueSize</tt>.
     *
     * @return property value of checkNodeListChangePushExecutorQueueSize
     */
    @Override
    public int getCheckNodeListChangePushExecutorQueueSize() {
        return checkNodeListChangePushExecutorQueueSize;
    }

    /**
     * Getter method for property <tt>raftClientRefreshExecutorMinSize</tt>.
     *
     * @return property value of raftClientRefreshExecutorMinSize
     */
    @Override
    public int getRaftClientRefreshExecutorMinSize() {
        return raftClientRefreshExecutorMinSize;
    }

    /**
     * Getter method for property <tt>raftClientRefreshExecutorMaxSize</tt>.
     *
     * @return property value of raftClientRefreshExecutorMaxSize
     */
    @Override
    public int getRaftClientRefreshExecutorMaxSize() {
        return raftClientRefreshExecutorMaxSize;
    }

    /**
     * Getter method for property <tt>raftClientRefreshExecutorQueueSize</tt>.
     *
     * @return property value of raftClientRefreshExecutorQueueSize
     */
    @Override
    public int getRaftClientRefreshExecutorQueueSize() {
        return raftClientRefreshExecutorQueueSize;
    }

    /**
     * Setter method for property <tt>metaSchedulerPoolSize </tt>.
     *
     * @param metaSchedulerPoolSize  value to be assigned to property metaSchedulerPoolSize
     */
    public void setMetaSchedulerPoolSize(int metaSchedulerPoolSize) {
        this.metaSchedulerPoolSize = metaSchedulerPoolSize;
    }

    @Override
    public int getMetaSchedulerPoolSize() {
        return metaSchedulerPoolSize;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}