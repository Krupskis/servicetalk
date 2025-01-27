/*
 * Copyright © 2021 Apple Inc. and the ServiceTalk project authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.servicetalk.concurrent.api.internal;

import io.servicetalk.concurrent.api.AsyncContext;
import io.servicetalk.concurrent.api.AsyncContextMap;
import io.servicetalk.concurrent.api.Executor;
import io.servicetalk.concurrent.api.ExecutorExtension;
import io.servicetalk.concurrent.api.TestExecutor;
import io.servicetalk.concurrent.internal.DeliberateException;

import org.hamcrest.Matcher;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.servicetalk.concurrent.api.ExecutorExtension.withCachedExecutor;
import static io.servicetalk.concurrent.api.Executors.immediate;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.startsWith;

public abstract class AbstractOffloadingTest {

    protected static final Logger LOGGER = LoggerFactory.getLogger(AbstractOffloadingTest.class);

    protected static final DeliberateException DELIBERATE_EXCEPTION = new DeliberateException();

    protected static final String ASYNC_CONTEXT_KEY = "OFFLOAD";
    protected static final String ASYNC_CONTEXT_VALUE = "TEST";

    protected static final AsyncContextMap.Key<String> ASYNC_CONTEXT_CUSTOM_KEY =
            AsyncContextMap.Key.newKey(ASYNC_CONTEXT_KEY);

    public enum CaptureSlot {
        IN_APP,
        IN_ORIGINAL_SUBSCRIBE,
        IN_OFFLOADED_SUBSCRIBE,
        IN_ORIGINAL_ON_COMPLETE,
        IN_OFFLOADED_ON_COMPLETE,
        IN_ORIGINAL_ON_SUBSCRIBE,
        IN_OFFLOADED_ON_SUBSCRIBE,
        IN_ORIGINAL_ON_ERROR,
        IN_OFFLOADED_ON_ERROR,
        IN_ORIGINAL_REQUEST,
        IN_OFFLOADED_REQUEST,
        IN_ORIGINAL_ON_NEXT,
        IN_OFFLOADED_ON_NEXT,
        IN_ORIGINAL_CANCEL,
        IN_OFFLOADED_CANCEL
    }

    protected enum TerminalOperation {
        CANCEL,
        COMPLETE,
        ERROR
    }

    /**
     * If true then "application" is executed on a separate thread from the test.
     */
    protected static final boolean APP_ISOLATION = true;
    protected static final String APP_EXECUTOR_PREFIX = "app";
    protected static final Matcher<String> APP_EXECUTOR = APP_ISOLATION ?
            startsWith(APP_EXECUTOR_PREFIX) :
            is(Thread.currentThread().getName());

    protected static final Matcher<String> OFFLOAD_EXECUTOR = startsWith("TestExecutor");

    @RegisterExtension
    public final ExecutorExtension<Executor> app = APP_ISOLATION ?
            withCachedExecutor(APP_EXECUTOR_PREFIX) :
            ExecutorExtension.withExecutor(() -> immediate());
    @RegisterExtension
    public final ExecutorExtension<TestExecutor> testExecutor = ExecutorExtension.withTestExecutor();

    protected final CaptureReferences<CaptureSlot, String> capturedThreads;
    protected final CaptureReferences<CaptureSlot, AsyncContextMap> capturedContexts;

    protected AbstractOffloadingTest() {
        this.capturedThreads = new CaptureReferences(CaptureSlot.class, () -> Thread.currentThread().getName());
        this.capturedContexts = new CaptureReferences(CaptureSlot.class, AsyncContext::current);
    }

    protected void capture(CaptureSlot slot) {
        capturedThreads.capture(slot);
        capturedContexts.capture(slot);
    }
}
