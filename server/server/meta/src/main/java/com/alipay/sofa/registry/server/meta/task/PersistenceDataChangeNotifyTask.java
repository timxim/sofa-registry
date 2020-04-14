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
package com.alipay.sofa.registry.server.meta.task;

import java.util.Set;

import com.alipay.sofa.registry.common.model.Node.NodeType;
import com.alipay.sofa.registry.common.model.metaserver.NotifyProvideDataChange;
import com.alipay.sofa.registry.server.meta.bootstrap.MetaServerConfig;
import com.alipay.sofa.registry.server.meta.bootstrap.ServiceFactory;
import com.alipay.sofa.registry.server.meta.node.DataNodeService;
import com.alipay.sofa.registry.server.meta.node.SessionNodeService;
import com.alipay.sofa.registry.task.listener.TaskEvent;

/**
 *
 * @author shangyu.wh
 * @version $Id: SessionNodeChangePushTask.java, v 0.1 2018-01-15 16:12 shangyu.wh Exp $
 */
public class PersistenceDataChangeNotifyTask extends AbstractMetaServerTask {

    private final SessionNodeService sessionNodeService;

    private final DataNodeService    dataNodeService;

    final private MetaServerConfig   metaServerConfig;

    private NotifyProvideDataChange  notifyProvideDataChange;

    public PersistenceDataChangeNotifyTask(MetaServerConfig metaServerConfig) {
        this.metaServerConfig = metaServerConfig;
        this.sessionNodeService = (SessionNodeService) ServiceFactory
            .getNodeService(NodeType.SESSION);

        this.dataNodeService = (DataNodeService) ServiceFactory.getNodeService(NodeType.DATA);
    }

    @Override
    public void execute() {
        Set<NodeType> nodeTypes = notifyProvideDataChange.getNodeTypes();
        if (nodeTypes.contains(NodeType.DATA)) {
            dataNodeService.notifyProvideDataChange(notifyProvideDataChange);
        }
        if (nodeTypes.contains(NodeType.SESSION)) {
            sessionNodeService.notifyProvideDataChange(notifyProvideDataChange);
        }
    }

    @Override
    public void setTaskEvent(TaskEvent taskEvent) {
        Object obj = taskEvent.getEventObj();
        if (obj instanceof NotifyProvideDataChange) {
            this.notifyProvideDataChange = (NotifyProvideDataChange) obj;
        } else {
            throw new IllegalArgumentException("Input task event object error!");
        }
    }

    @Override
    public String toString() {
        return "PERSISTENCE_DATA_CHANGE_NOTIFY_TASK{" + "taskId='" + taskId + '\''
               + ", notifyProvideDataChange=" + notifyProvideDataChange + '}';
    }

    @Override
    public boolean checkRetryTimes() {
        return checkRetryTimes(metaServerConfig.getSessionNodeChangePushTaskRetryTimes());
    }
}