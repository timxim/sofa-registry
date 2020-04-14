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
package com.alipay.sofa.registry.server.data.event.handler;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.alipay.sofa.registry.log.Logger;
import com.alipay.sofa.registry.log.LoggerFactory;
import com.alipay.sofa.registry.server.data.event.EventCenter;

/**
 *
 * @author qian.lqlq
 * @version $Id: AbstractEventHandler.java, v 0.1 2018-03-13 15:34 qian.lqlq Exp $
 */
public abstract class AbstractEventHandler<Event> implements InitializingBean {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEventHandler.class);

    @Autowired
    private EventCenter         eventCenter;

    @Override
    public void afterPropertiesSet() throws Exception {
        eventCenter.register(this);
    }

    /**
     * event handle func
     * @param event
     */
    public void handle(Event event) {
        try {
            doHandle(event);
        } catch (Exception e) {
            LOGGER.error("[{}] handle event error", this.getClass().getSimpleName(), e);
        }
    }

    public abstract List<Class<? extends Event>> interest();

    public abstract void doHandle(Event event);
}