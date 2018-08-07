/*
 * Copyright © 2018 Apple Inc. and the ServiceTalk project authors
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
package io.servicetalk.examples.http.service.composition.backends;

import io.servicetalk.concurrent.api.Single;
import io.servicetalk.examples.http.service.composition.pojo.User;
import io.servicetalk.http.api.AggregatedHttpRequest;
import io.servicetalk.http.api.AggregatedHttpResponse;
import io.servicetalk.http.api.AggregatedHttpService;
import io.servicetalk.http.api.HttpPayloadChunk;
import io.servicetalk.http.api.HttpSerializer;
import io.servicetalk.http.router.predicate.HttpPredicateRouterBuilder;
import io.servicetalk.transport.api.ConnectionContext;

import java.util.concurrent.ThreadLocalRandom;

import static io.servicetalk.concurrent.api.Single.success;
import static io.servicetalk.http.api.AggregatedHttpResponses.newResponse;
import static io.servicetalk.http.api.HttpResponseStatuses.BAD_REQUEST;
import static io.servicetalk.http.api.HttpResponseStatuses.OK;
import static java.util.concurrent.ThreadLocalRandom.current;

/**
 * A service that returns {@link User} for an entity.
 */
final class UserBackend extends AggregatedHttpService {

    private static final String USER_ID_QP_NAME = "userId";
    private final HttpSerializer serializer;

    private UserBackend(HttpSerializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public Single<AggregatedHttpResponse<HttpPayloadChunk>> handle(final ConnectionContext ctx,
                                                                   final AggregatedHttpRequest<HttpPayloadChunk> request) {
        final String userId = request.parseQuery().get(USER_ID_QP_NAME);
        if (userId == null) {
            return success(newResponse(BAD_REQUEST));
        }

        // Create a random rating
        User user = new User(userId, createRandomString(5), createRandomString(3));
        return success(serializer.serialize(newResponse(OK, user), ctx.getExecutionContext().getBufferAllocator()));
    }

    static AggregatedHttpService newUserService(HttpSerializer serializer) {
        HttpPredicateRouterBuilder routerBuilder = new HttpPredicateRouterBuilder();
        return routerBuilder.whenPathStartsWith("/user")
                .thenRouteTo(new UserBackend(serializer))
                .buildAggregated();
    }

    private String createRandomString(int size) {
        final ThreadLocalRandom random = current();
        char[] randomChars = new char[size];
        for (int i = 0; i < size; i++) {
            randomChars[i] = (char) random.nextInt(97, 122);
        }
        return new String(randomChars);
    }
}