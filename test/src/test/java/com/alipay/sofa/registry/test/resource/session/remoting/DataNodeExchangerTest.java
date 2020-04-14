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
package com.alipay.sofa.registry.test.resource.session.remoting;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

import com.alipay.sofa.registry.common.model.dataserver.GetDataVersionRequest;
import com.alipay.sofa.registry.common.model.store.URL;
import com.alipay.sofa.registry.remoting.exchange.RequestException;
import com.alipay.sofa.registry.remoting.exchange.message.Request;
import com.alipay.sofa.registry.server.session.remoting.DataNodeExchanger;
import com.alipay.sofa.registry.test.BaseIntegrationTest;

/**
 * @author kezhu.wukz
 * @since 2020/03/03
 */
@RunWith(SpringRunner.class)
public class DataNodeExchangerTest extends BaseIntegrationTest {

    @Test
    public void getTaskClosureTest() {
        Request<GetDataVersionRequest> request = new Request<GetDataVersionRequest>() {
            @Override
            public GetDataVersionRequest getRequestBody() {
                GetDataVersionRequest getDataVersionRequest = new GetDataVersionRequest();
                return getDataVersionRequest;
            }

            @Override
            public URL getRequestUrl() {
                return new URL("127.0.0.1", 54321);
            }
        };
        DataNodeExchanger dataNodeExchanger = (DataNodeExchanger) sessionApplicationContext
            .getBean("dataNodeExchanger");

        try {
            dataNodeExchanger.request(request);
            assert false;
        } catch (RequestException e) {
        }

    }
}
