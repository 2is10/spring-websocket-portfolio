/*
 * Copyright 2002-2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.portfolio.web.jetty;

import org.eclipse.jetty.util.DecoratedObjectFactory;
import org.eclipse.jetty.websocket.server.WebSocketServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.samples.portfolio.config.WebSocketConfig;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.DefaultTestContext;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy;
import org.testng.annotations.Test;

import javax.servlet.ServletContext;

@WebAppConfiguration
@ContextConfiguration(classes = {WebSocketConfig.class})
public class JettyIntegrationTest extends AbstractTestNGSpringContextTests {

    /**
     * Some extra configuration that works around an {@link IllegalStateException} thrown from
     * {@link WebSocketServerFactory@start()} via {@link JettyRequestUpgradeStrategy#start()} via
     * {@link DefaultTestContext#getApplicationContext()}.
     */
    @Configuration
    static class JettyIntegrationTestConfig {
        @Bean
        static DecoratedObjectFactory decoratedObjectFactory(ServletContext servletContext) {
            DecoratedObjectFactory factory = new DecoratedObjectFactory();
            servletContext.setAttribute(DecoratedObjectFactory.ATTR, factory);
            return factory;
        }
    }

    @Test
    public void test() {
        // TODO: Avoid exception (trace below) so that any code here can run.
        // java.lang.IllegalStateException: Failed to load ApplicationContext
        //   at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:125)
        //   at org.springframework.test.context.support.DefaultTestContext.getApplicationContext(DefaultTestContext.java:107)
        //   at org.springframework.test.context.web.ServletTestExecutionListener.setUpRequestContextIfNecessary(ServletTestExecutionListener.java:190)
        //   at org.springframework.test.context.web.ServletTestExecutionListener.beforeTestMethod(ServletTestExecutionListener.java:145)
        //   at org.springframework.test.context.TestContextManager.beforeTestMethod(TestContextManager.java:287)
        //   at org.springframework.test.context.testng.AbstractTestNGSpringContextTests.springTestContextBeforeTestMethod(AbstractTestNGSpringContextTests.java:157)
        //   ...
        //   at org.testng.TestNG.run(TestNG.java:1064)
        //   at org.testng.IDEARemoteTestNG.run(IDEARemoteTestNG.java:72)
        //   at org.testng.RemoteTestNGStarter.main(RemoteTestNGStarter.java:123)
        // Caused by: org.springframework.context.ApplicationContextException: Failed to start bean 'stompWebSocketHandlerMapping'
        //   at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:186)
        //   at org.springframework.context.support.DefaultLifecycleProcessor.access$200(DefaultLifecycleProcessor.java:52)
        //   at org.springframework.context.support.DefaultLifecycleProcessor$LifecycleGroup.start(DefaultLifecycleProcessor.java:358)
        //   at org.springframework.context.support.DefaultLifecycleProcessor.startBeans(DefaultLifecycleProcessor.java:159)
        //   at org.springframework.context.support.DefaultLifecycleProcessor.onRefresh(DefaultLifecycleProcessor.java:123)
        //   at org.springframework.context.support.AbstractApplicationContext.finishRefresh(AbstractApplicationContext.java:884)
        //   at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:552)
        //   at org.springframework.test.context.web.AbstractGenericWebContextLoader.loadContext(AbstractGenericWebContextLoader.java:132)
        //   at org.springframework.test.context.web.AbstractGenericWebContextLoader.loadContext(AbstractGenericWebContextLoader.java:61)
        //   at org.springframework.test.context.support.AbstractDelegatingSmartContextLoader.delegateLoading(AbstractDelegatingSmartContextLoader.java:109)
        //   at org.springframework.test.context.support.AbstractDelegatingSmartContextLoader.loadContext(AbstractDelegatingSmartContextLoader.java:246)
        //   at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContextInternal(DefaultCacheAwareContextLoaderDelegate.java:99)
        //   at org.springframework.test.context.cache.DefaultCacheAwareContextLoaderDelegate.loadContext(DefaultCacheAwareContextLoaderDelegate.java:117)
        //   ... 30 more
        // Caused by: java.lang.IllegalStateException: Unable to start Jetty WebSocketServerFactory
        //   at org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy.start(JettyRequestUpgradeStrategy.java:136)
        //   at org.springframework.web.socket.server.support.AbstractHandshakeHandler.doStart(AbstractHandshakeHandler.java:203)
        //   at org.springframework.web.socket.server.support.AbstractHandshakeHandler.start(AbstractHandshakeHandler.java:197)
        //   at org.springframework.web.socket.sockjs.transport.handler.WebSocketTransportHandler.start(WebSocketTransportHandler.java:91)
        //   at org.springframework.web.socket.sockjs.transport.TransportHandlingSockJsService.start(TransportHandlingSockJsService.java:166)
        //   at org.springframework.web.socket.sockjs.support.SockJsHttpRequestHandler.start(SockJsHttpRequestHandler.java:104)
        //   at org.springframework.web.socket.server.support.WebSocketHandlerMapping.start(WebSocketHandlerMapping.java:65)
        //   at org.springframework.context.support.DefaultLifecycleProcessor.doStart(DefaultLifecycleProcessor.java:183)
        //   ... 42 more
        // Caused by: java.lang.NullPointerException
        //   at org.eclipse.jetty.websocket.server.WebSocketServerFactory.doStart(WebSocketServerFactory.java:318)
        //   at org.eclipse.jetty.util.component.AbstractLifeCycle.start(AbstractLifeCycle.java:68)
        //   at org.springframework.web.socket.server.jetty.JettyRequestUpgradeStrategy.start(JettyRequestUpgradeStrategy.java:133)
    }

}
