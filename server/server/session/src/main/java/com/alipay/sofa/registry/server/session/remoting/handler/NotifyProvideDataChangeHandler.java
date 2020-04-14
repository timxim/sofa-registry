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
package com.alipay.sofa.registry.server.session.remoting.handler;

import com.alipay.sofa.registry.common.model.Node.NodeType;
import com.alipay.sofa.registry.common.model.constants.ValueConstants;
import com.alipay.sofa.registry.common.model.metaserver.NotifyProvideDataChange;
import com.alipay.sofa.registry.log.Logger;
import com.alipay.sofa.registry.log.LoggerFactory;
import com.alipay.sofa.registry.remoting.Channel;
import com.alipay.sofa.registry.server.session.store.Watchers;
import com.alipay.sofa.registry.task.listener.TaskEvent;
import com.alipay.sofa.registry.task.listener.TaskEvent.TaskType;
import com.alipay.sofa.registry.task.listener.TaskListenerManager;
import org.springframework.beans.factory.annotation.Autowired;

/**
 *
 * @author shangyu.wh
 * @version $Id: DataChangeRequestHandler.java, v 0.1 2017-12-12 15:09 shangyu.wh Exp $
 */
public class NotifyProvideDataChangeHandler extends AbstractClientHandler {

    private static final Logger LOGGER      = LoggerFactory
                                                .getLogger(NotifyProvideDataChangeHandler.class);

    private static final Logger TASK_LOGGER = LoggerFactory.getLogger(
                                                NotifyProvideDataChangeHandler.class, "[Task]");
    /**
     * store watchers
     */
    @Autowired
    private Watchers            sessionWatchers;

    /**
     * trigger task com.alipay.sofa.registry.server.meta.listener process
     */
    @Autowired
    private TaskListenerManager taskListenerManager;

    @Override
    public HandlerType getType() {
        return HandlerType.PROCESSER;
    }

    @Override
    protected NodeType getConnectNodeType() {
        return NodeType.DATA;
    }

    @Override
    public Object reply(Channel channel, Object message) {

        NotifyProvideDataChange notifyProvideDataChange = (NotifyProvideDataChange) message;

        if (!ValueConstants.STOP_PUSH_DATA_SWITCH_DATA_ID.equals(notifyProvideDataChange
            .getDataInfoId())
            && !ValueConstants.BLACK_LIST_DATA_ID.equals(notifyProvideDataChange.getDataInfoId())
            && !ValueConstants.ENABLE_DATA_RENEW_SNAPSHOT.equals(notifyProvideDataChange
                .getDataInfoId())) {
            boolean result = sessionWatchers.checkWatcherVersions(
                notifyProvideDataChange.getDataInfoId(), notifyProvideDataChange.getVersion());
            if (!result) {
                LOGGER
                    .info(
                        "Request message dataInfo {}, version {} not be interested or lower than current version!",
                        notifyProvideDataChange.getDataInfoId(),
                        notifyProvideDataChange.getVersion());
                return null;
            }

        }
        fireDataChangeFetchTask(notifyProvideDataChange);
        return null;
    }

    private void fireDataChangeFetchTask(NotifyProvideDataChange notifyProvideDataChange) {

        TaskEvent taskEvent = new TaskEvent(notifyProvideDataChange,
            TaskType.PROVIDE_DATA_CHANGE_FETCH_TASK);
        TASK_LOGGER.info("send " + taskEvent.getTaskType() + " taskEvent:{}", taskEvent);
        taskListenerManager.sendTaskEvent(taskEvent);
    }

    @Override
    public Class interest() {
        return NotifyProvideDataChange.class;
    }
}