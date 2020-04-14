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
package com.alipay.sofa.registry.server.data.remoting.dataserver.task;

import com.alipay.sofa.registry.server.data.event.StartTaskTypeEnum;
import com.alipay.sofa.registry.server.data.remoting.metaserver.IMetaServerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.TimeUnit;

/**
 *
 * @author shangyu.wh
 * @version $Id: RenewNodeTask.java, v 0.1 2018-04-02 20:56 shangyu.wh Exp $
 */
public class RenewNodeTask extends AbstractTask {

    @Autowired
    private IMetaServerService metaServerService;

    @Override
    public void handle() {
        metaServerService.renewNodeTask();
    }

    @Override
    public int getDelay() {
        return 3;
    }

    @Override
    public int getInitialDelay() {
        return 0;
    }

    @Override
    public TimeUnit getTimeUnit() {
        return TimeUnit.SECONDS;
    }

    @Override
    public StartTaskTypeEnum getStartTaskTypeEnum() {
        return StartTaskTypeEnum.RENEW;
    }
}