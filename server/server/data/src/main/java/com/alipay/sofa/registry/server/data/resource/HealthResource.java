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
package com.alipay.sofa.registry.server.data.resource;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;

import com.alipay.sofa.registry.common.model.CommonResponse;
import com.alipay.sofa.registry.metrics.ReporterUtils;
import com.alipay.sofa.registry.server.data.bootstrap.DataServerBootstrap;
import com.alipay.sofa.registry.server.data.node.DataNodeStatus;
import com.alipay.sofa.registry.server.data.util.LocalServerStatusEnum;
import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;

/**
 *
 * @author shangyu.wh
 * @version $Id: HealthResource.java, v 0.1 2018-10-19 14:56 shangyu.wh Exp $
 */
@Path("health")
public class HealthResource {

    @Autowired
    private DataServerBootstrap dataServerBootstrap;

    @Autowired
    private DataNodeStatus      dataNodeStatus;

    @PostConstruct
    public void init() {
        MetricRegistry metrics = new MetricRegistry();
        metrics.register("healthCheck", (Gauge<CommonResponse>) () -> getHealthCheckResult());
        ReporterUtils.startSlf4jReporter(60, metrics);
    }

    @GET
    @Path("check")
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkHealth() {

        ResponseBuilder builder = Response.status(Response.Status.OK);
        CommonResponse response = getHealthCheckResult();
        builder.entity(response);
        if (!response.isSuccess()) {
            builder.status(Status.INTERNAL_SERVER_ERROR);
        }

        return builder.build();
    }

    private CommonResponse getHealthCheckResult() {
        CommonResponse response;

        StringBuilder sb = new StringBuilder("DataServerBoot ");

        boolean start = dataServerBootstrap.getServerForSessionStarted().get();
        boolean ret = start;
        sb.append("severForSession:").append(start);

        start = dataServerBootstrap.getServerForDataSyncStarted().get();
        ret = ret && start;
        sb.append(", severForDataSync:").append(start);

        start = dataServerBootstrap.getHttpServerStarted().get();
        ret = ret && start;
        sb.append(", httpServer:").append(start);

        start = dataServerBootstrap.getSchedulerStarted().get();
        ret = ret && start;
        sb.append(", schedulerStarted:").append(start);

        start = dataNodeStatus.getStatus() == LocalServerStatusEnum.WORKING;
        ret = ret && start;
        sb.append(", status:").append(dataNodeStatus.getStatus());

        if (ret) {
            response = CommonResponse.buildSuccessResponse(sb.toString());
        } else {
            response = CommonResponse.buildFailedResponse(sb.toString());
        }

        if (ret) {
            response = CommonResponse.buildSuccessResponse(sb.toString());
        } else {
            response = CommonResponse.buildFailedResponse(sb.toString());
        }
        return response;
    }
}