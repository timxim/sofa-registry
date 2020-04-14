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
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import com.alipay.sofa.registry.common.model.DatumSnapshotRequest;
import com.alipay.sofa.registry.common.model.constants.ValueConstants;
import com.alipay.sofa.registry.log.Logger;
import com.alipay.sofa.registry.log.LoggerFactory;
import com.alipay.sofa.registry.server.session.acceptor.WriteDataRequest.WriteDataRequestType;
import com.alipay.sofa.registry.server.session.bootstrap.SessionServerConfig;
import com.alipay.sofa.registry.server.session.renew.RenewService;
import com.alipay.sofa.registry.task.listener.TaskEvent;
import com.alipay.sofa.registry.task.listener.TaskEvent.TaskType;
import com.alipay.sofa.registry.task.listener.TaskListenerManager;
import com.google.common.collect.Lists;

/**
 *
 * @author kezhu.wukz
 * @author shangyu.wh
 * @version 1.0: WriteDataProcessor.java, v 0.1 2019-06-06 12:50 shangyu.wh Exp $
 */
public class WriteDataProcessor {

    private static final Logger                     LOGGER                 = LoggerFactory
                                                                               .getLogger(WriteDataProcessor.class);

    private static final Logger                     RENEW_LOGGER           = LoggerFactory
                                                                               .getLogger(
                                                                                   ValueConstants.LOGGER_NAME_RENEW,
                                                                                   "[WriteDataProcessor]");

    private static final Logger                     taskLogger             = LoggerFactory
                                                                               .getLogger(
                                                                                   WriteDataProcessor.class,
                                                                                   "[Task]");

    private final TaskListenerManager               taskListenerManager;

    private final SessionServerConfig               sessionServerConfig;

    private final RenewService                      renewService;

    private final String                            connectId;

    private Map<String, AtomicLong>                 lastUpdateTimestampMap = new ConcurrentHashMap<>();

    private AtomicBoolean                           writeDataLock          = new AtomicBoolean(
                                                                               false);

    private ConcurrentLinkedQueue<WriteDataRequest> acceptorQueue          = new ConcurrentLinkedQueue();

    private AtomicInteger                           acceptorQueueSize      = new AtomicInteger(0);

    public WriteDataProcessor(String connectId, TaskListenerManager taskListenerManager,
                              SessionServerConfig sessionServerConfig, RenewService renewService) {
        this.connectId = connectId;
        this.taskListenerManager = taskListenerManager;
        this.sessionServerConfig = sessionServerConfig;
        this.renewService = renewService;
    }

    private boolean halt() {
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER.debug("halt: connectId={}", connectId);
        }
        return writeDataLock.compareAndSet(false, true);
    }

    public void resume() {
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER.debug("resume: connectId={}", connectId);
        }
        flushQueue();
        writeDataLock.compareAndSet(true, false);
        flushQueue();
    }

    public void process(WriteDataRequest request) {
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER.debug("process: connectId={}, requestType={}, requestBody={}", connectId,
                request.getRequestType(), request.getRequestBody());
        }

        // record the last update time by pub/unpub
        if (isWriteRequest(request)) {
            refreshUpdateTime(request.getDataServerIP());
        }

        if (request.getRequestType() == WriteDataRequestType.DATUM_SNAPSHOT) {
            // snapshot has high priority, so handle directly
            doHandle(request);
        } else {
            // If locked, insert the queue;
            // otherwise, try emptying the queue (to avoid residue) before processing the request.
            if (writeDataLock.get()) {
                addQueue(request);
            } else {
                flushQueue();
                doHandle(request);
            }
        }

    }

    private void addQueue(WriteDataRequest request) {
        if (acceptorQueueSize.incrementAndGet() <= sessionServerConfig
            .getWriteDataAcceptorQueueSize()) {
            acceptorQueue.add(request);
        } else {
            RENEW_LOGGER
                .error(
                    "acceptorQueueSize({}) reached the limit : connectId={}, requestType={}, requestBody={}",
                    acceptorQueue.size(), connectId, request.getRequestType(),
                    request.getRequestBody());
        }
    }

    /**
     *
     * @param request
     * @return
     */
    private boolean isWriteRequest(WriteDataRequest request) {
        return request.getRequestType() == WriteDataRequestType.PUBLISHER
               || request.getRequestType() == WriteDataRequestType.UN_PUBLISHER;
    }

    /**
     * Ensure that the queue data is sent out
     */
    private void flushQueue() {
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER.debug("flushQueue: connectId={}", connectId);
        }

        while (!acceptorQueue.isEmpty()) {
            WriteDataRequest writeDataRequest = acceptorQueue.poll();
            if (writeDataRequest == null) {
                break;
            }
            acceptorQueueSize.decrementAndGet();
            doHandle(writeDataRequest);
        }
    }

    private void doHandle(WriteDataRequest request) {
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER.debug("doHandle: connectId={}, requestType={}, requestBody={}", connectId,
                request.getRequestType(), request.getRequestBody());
        }

        switch (request.getRequestType()) {
            case PUBLISHER: {
                doPublishAsync(request);
            }
                break;
            case UN_PUBLISHER: {
                doUnPublishAsync(request);
            }
                break;
            case CLIENT_OFF: {
                doClientOffAsync(request);
            }
                break;
            case RENEW_DATUM: {
                if (renewAndSnapshotInSilence(request.getDataServerIP())) {
                    return;
                }
                doRenewAsync(request);
            }
                break;
            case DATUM_SNAPSHOT: {
                if (renewAndSnapshotInSilenceAndRefreshUpdateTime(request.getDataServerIP())) {
                    return;
                }
                halt();
                try {
                    doSnapshotAsync(request);
                } finally {
                    resume();
                }
            }
                break;
            default:
                LOGGER.warn("Unknown request type, requestType={}, requestBody={}", connectId,
                    request.getRequestType(), request.getRequestBody());

        }
    }

    private void doRenewAsync(WriteDataRequest request) {
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER.debug("doRenewAsync: connectId={}, requestType={}, requestBody={}",
                connectId, request.getRequestType(), request.getRequestBody());
        }

        sendEvent(request.getRequestBody(), TaskType.RENEW_DATUM_TASK);
    }

    private void doClientOffAsync(WriteDataRequest request) {
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER.debug("doClientOffAsync: connectId={}, requestType={}, requestBody={}",
                connectId, request.getRequestType(), request.getRequestBody());
        }

        String connectId = request.getConnectId();
        sendEvent(Lists.newArrayList(connectId), TaskType.CANCEL_DATA_TASK);
    }

    private void doUnPublishAsync(WriteDataRequest request) {
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER.debug("doUnPublishAsync: connectId={}, requestType={}, requestBody={}",
                connectId, request.getRequestType(), request.getRequestBody());
        }

        sendEvent(request.getRequestBody(), TaskType.UN_PUBLISH_DATA_TASK);
    }

    private void doPublishAsync(WriteDataRequest request) {
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER.debug("doPublishAsync: connectId={}, requestType={}, requestBody={}",
                connectId, request.getRequestType(), request.getRequestBody());
        }

        sendEvent(request.getRequestBody(), TaskType.PUBLISH_DATA_TASK);
    }

    private void sendEvent(Object eventObj, TaskType taskType) {
        TaskEvent taskEvent = new TaskEvent(eventObj, taskType);

        //print but ignore if from renew module, cause renew request is too much
        if (taskType != TaskType.RENEW_DATUM_TASK) {
            taskLogger.info("send " + taskType + " taskEvent:{}", taskEvent);
        }

        taskListenerManager.sendTaskEvent(taskEvent);
    }

    private void doSnapshotAsync(WriteDataRequest request) {
        RENEW_LOGGER.info(
            "doSnapshotAsync: connectId={}, dataServerIP={}, requestType={}, requestBody={}",
            connectId, request.getDataServerIP(), request.getRequestType(),
            request.getRequestBody());

        String connectId = (String) request.getRequestBody();
        DatumSnapshotRequest datumSnapshotRequest = renewService.getDatumSnapshotRequest(connectId,
            request.getDataServerIP());
        if (datumSnapshotRequest != null) {
            TaskEvent taskEvent = new TaskEvent(datumSnapshotRequest, TaskType.DATUM_SNAPSHOT_TASK);
            taskListenerManager.sendTaskEvent(taskEvent);
        } else {
            RENEW_LOGGER
                .info(
                    "datumSnapshotRequest is null when doSnapshotAsync: connectId={}, dataServerIP={}, requestType={}",
                    connectId, request.getDataServerIP(), request.getRequestType());
        }

    }

    /**
     * In silence, do not renew and snapshot
     */
    private boolean renewAndSnapshotInSilence(String dataServerIP) {
        boolean renewAndSnapshotInSilence = System.currentTimeMillis()
                                            - getLastUpdateTime(dataServerIP).get() < this.sessionServerConfig
            .getRenewAndSnapshotSilentPeriodSec() * 1000L;
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER.debug(
                "renewAndSnapshotInSilence: connectId={}, renewAndSnapshotInSilence={}", connectId,
                renewAndSnapshotInSilence);
        }
        return renewAndSnapshotInSilence;
    }

    /**
     * In silence, do not renew and snapshot
     */
    private boolean renewAndSnapshotInSilenceAndRefreshUpdateTime(String dataServerIP) {
        boolean renewAndSnapshotInSilence = System.currentTimeMillis()
                                            - refreshUpdateTime(dataServerIP) < this.sessionServerConfig
            .getRenewAndSnapshotSilentPeriodSec() * 1000L;
        if (RENEW_LOGGER.isDebugEnabled()) {
            RENEW_LOGGER
                .debug(
                    "renewAndSnapshotInSilenceAndRefreshUpdateTime: connectId={}, renewAndSnapshotInSilence={}",
                    connectId, renewAndSnapshotInSilence);
        }
        return renewAndSnapshotInSilence;
    }

    private long refreshUpdateTime(String dataServerIP) {
        AtomicLong lastUpdateTimestamp = getLastUpdateTime(dataServerIP);
        return lastUpdateTimestamp.getAndSet(System.currentTimeMillis());
    }

    private AtomicLong getLastUpdateTime(String dataServerIP) {
        AtomicLong lastUpdateTimestamp = lastUpdateTimestampMap.get(dataServerIP);
        if (lastUpdateTimestamp == null) {
            lastUpdateTimestamp = new AtomicLong(0);
            AtomicLong _lastUpdateTimestamp = lastUpdateTimestampMap.putIfAbsent(dataServerIP,
                lastUpdateTimestamp);
            if (_lastUpdateTimestamp != null) {
                lastUpdateTimestamp = _lastUpdateTimestamp;
            }
        }
        return lastUpdateTimestamp;
    }
}