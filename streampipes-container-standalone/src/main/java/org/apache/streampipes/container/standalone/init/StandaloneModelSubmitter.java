/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.streampipes.container.standalone.init;


import org.apache.streampipes.container.init.DeclarersSingleton;
import org.apache.streampipes.container.model.PeConfig;
import org.apache.streampipes.container.model.SpServiceDefinition;
import org.apache.streampipes.service.extensions.base.StreamPipesExtensionsServiceBase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.net.UnknownHostException;
import java.util.List;

@Configuration
@EnableAutoConfiguration
@Import({ PipelineElementContainerResourceConfig.class })
public abstract class StandaloneModelSubmitter extends StreamPipesExtensionsServiceBase {

    private static final Logger LOG =
            LoggerFactory.getLogger(StandaloneModelSubmitter.class.getCanonicalName());

    @Deprecated
    public void init(PeConfig peConfig) {
        DeclarersSingleton.getInstance()
                .setHostName(peConfig.getHost());
        DeclarersSingleton.getInstance()
                .setPort(peConfig.getPort());

        SpServiceDefinition serviceDef = DeclarersSingleton.getInstance().toServiceDefinition(peConfig.getId());

        try {
            startExtensionsService(this.getClass(), serviceDef);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onExit() {
        new PipelineElementServiceShutdownHandler().onShutdown();
    }

    @Override
    protected List<String> getServiceTags() {
        return new PipelineElementServiceTagProvider().extractServiceTags();
    }

    @Override
    public void afterServiceRegistered(SpServiceDefinition serviceDef) {

    }

}
