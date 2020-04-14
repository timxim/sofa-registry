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
package com.alipay.sofa.registry.server.meta.node.impl;

import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alipay.sofa.registry.common.model.Node.NodeType;
import com.alipay.sofa.registry.common.model.metaserver.NodeChangeResult;
import com.alipay.sofa.registry.common.model.metaserver.NotifyProvideDataChange;
import com.alipay.sofa.registry.common.model.metaserver.SessionNode;
import com.alipay.sofa.registry.common.model.store.URL;
import com.alipay.sofa.registry.log.Logger;
import com.alipay.sofa.registry.log.LoggerFactory;
import com.alipay.sofa.registry.remoting.exchange.NodeExchanger;
import com.alipay.sofa.registry.remoting.exchange.RequestException;
import com.alipay.sofa.registry.remoting.exchange.message.Request;
import com.alipay.sofa.registry.server.meta.bootstrap.MetaServerConfig;
import com.alipay.sofa.registry.server.meta.bootstrap.ServiceFactory;
import com.alipay.sofa.registry.server.meta.node.SessionNodeService;
import com.alipay.sofa.registry.server.meta.remoting.connection.NodeConnectManager;
import com.alipay.sofa.registry.server.meta.remoting.handler.AbstractServerHandler;
import com.alipay.sofa.registry.server.meta.store.StoreService;

/**
 *
 * @author shangyu.wh
 * @version $Id: SessionNodeServiceImpl.java, v 0.1 2018-01-15 17:18 shangyu.wh Exp $
 */
public class SessionNodeServiceImpl implements SessionNodeService {

    private static final Logger   LOGGER = LoggerFactory.getLogger(SessionNodeServiceImpl.class);

    @Autowired
    private NodeExchanger         sessionNodeExchanger;

    @Autowired
    private StoreService          sessionStoreService;

    @Autowired
    private MetaServerConfig      metaServerConfig;

    @Autowired
    private AbstractServerHandler sessionConnectionHandler;

    @Override
    public NodeType getNodeType() {
        return NodeType.SESSION;
    }

    @Override
    public void pushSessions(NodeChangeResult nodeChangeResult,
                             Map<String, SessionNode> sessionNodes, String confirmNodeIp) {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SessionNodeServiceImpl pushSessions sessionNodes:{}", nodeChangeResult);
        }
        NodeConnectManager nodeConnectManager = getNodeConnectManager();
        Collection<InetSocketAddress> connections = nodeConnectManager.getConnections(null);

        if (connections.size() == 0) {
            LOGGER.warn("there are no client connected on session server port:{}",
                metaServerConfig.getSessionServerPort());
        }

        if (sessionNodes == null || sessionNodes.isEmpty()) {
            LOGGER.error("Push sessionNode list error! Input sessionNodes can't be null!");
            throw new RuntimeException(
                "Push sessionNode list error! Input sessionNodes can't be null!");
        }

        for (InetSocketAddress connection : connections) {

            if (!sessionNodes.keySet().contains(connection.getAddress().getHostAddress())) {
                continue;
            }

            try {
                Request<NodeChangeResult> nodeChangeRequest = new Request<NodeChangeResult>() {

                    @Override
                    public NodeChangeResult getRequestBody() {
                        return nodeChangeResult;
                    }

                    @Override
                    //all connect session
                    public URL getRequestUrl() {
                        return new URL(connection);
                    }
                };

                sessionNodeExchanger.request(nodeChangeRequest);

                //no error confirm receive
                sessionStoreService.confirmNodeStatus(connection.getAddress().getHostAddress(),
                    confirmNodeIp);

            } catch (RequestException e) {
                throw new RuntimeException("Push sessionNode list error: " + e.getMessage(), e);
            }
        }

    }

    @Override
    public void pushDataNodes(NodeChangeResult nodeChangeResult) {

        NodeConnectManager nodeConnectManager = getNodeConnectManager();
        Collection<InetSocketAddress> connections = nodeConnectManager.getConnections(null);

        if (connections == null || connections.isEmpty()) {
            LOGGER.error("Push sessionNode list error! No session node connected!");
            throw new RuntimeException("Push sessionNode list error! No session node connected!");
        }

        // add register confirm
        StoreService storeService = ServiceFactory.getStoreService(NodeType.SESSION);
        Map<String, SessionNode> sessionNodes = storeService.getNodes();

        if (sessionNodes == null || sessionNodes.isEmpty()) {
            LOGGER.error("Push sessionNode list error! No session node registered!");
            throw new RuntimeException("Push sessionNode list error! No session node registered!");
        }

        for (InetSocketAddress connection : connections) {

            if (!sessionNodes.keySet().contains(connection.getAddress().getHostAddress())) {
                continue;
            }

            try {
                Request<NodeChangeResult> nodeChangeRequestRequest = new Request<NodeChangeResult>() {

                    @Override
                    public NodeChangeResult getRequestBody() {
                        return nodeChangeResult;
                    }

                    @Override
                    public URL getRequestUrl() {
                        return new URL(connection);
                    }
                };

                sessionNodeExchanger.request(nodeChangeRequestRequest);

            } catch (RequestException e) {
                throw new RuntimeException("Push sessionNode list error: " + e.getMessage(), e);
            }
        }
    }

    @Override
    public void notifyProvideDataChange(NotifyProvideDataChange notifyProvideDataChange) {

        NodeConnectManager nodeConnectManager = getNodeConnectManager();
        Collection<InetSocketAddress> connections = nodeConnectManager.getConnections(null);

        if (connections == null || connections.isEmpty()) {
            LOGGER.error("Push sessionNode list error! No session node connected!");
            throw new RuntimeException("Push sessionNode list error! No session node connected!");
        }

        // add register confirm
        StoreService storeService = ServiceFactory.getStoreService(NodeType.SESSION);
        Map<String, SessionNode> sessionNodes = storeService.getNodes();

        if (sessionNodes == null || sessionNodes.isEmpty()) {
            LOGGER.error("Push sessionNode list error! No session node registered!");
            throw new RuntimeException("Push sessionNode list error! No session node registered!");
        }

        for (InetSocketAddress connection : connections) {

            if (!sessionNodes.keySet().contains(connection.getAddress().getHostAddress())) {
                continue;
            }

            try {
                Request<NotifyProvideDataChange> request = new Request<NotifyProvideDataChange>() {

                    @Override
                    public NotifyProvideDataChange getRequestBody() {
                        return notifyProvideDataChange;
                    }

                    @Override
                    public URL getRequestUrl() {
                        return new URL(connection);
                    }
                };

                sessionNodeExchanger.request(request);

            } catch (RequestException e) {
                throw new RuntimeException("Notify provide data change error: " + e.getMessage(), e);
            }
        }
    }

    private NodeConnectManager getNodeConnectManager() {
        if (!(sessionConnectionHandler instanceof NodeConnectManager)) {
            LOGGER.error("sessionConnectionHandler inject is not NodeConnectManager instance!");
            throw new RuntimeException(
                "sessionConnectionHandler inject is not NodeConnectManager instance!");
        }

        return (NodeConnectManager) sessionConnectionHandler;
    }
}