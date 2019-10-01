/*
 * Copyright © 2019 Apple Inc. and the ServiceTalk project authors
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
package io.servicetalk.http.netty;

/**
 * Defines protocol names used in ALPN and NPN.
 *
 * @see <a href="https://tools.ietf.org/html/rfc7540#section-11.1">RFC7540 (HTTP/2)</a>
 * @see <a href="https://tools.ietf.org/html/rfc7301#section-6">RFC7301 (TLS ALPN Extension)</a>
 * @see <a href="https://tools.ietf.org/html/draft-agl-tls-nextprotoneg-04#section-7">TLS NPN Extension Draft</a>
 */
final class ApplicationProtocolNames {

    /**
     * {@code "http/1.1"}: HTTP version 1.1
     */
    static final String HTTP_1_1 = "http/1.1";

    /**
     * {@code "h2"}: HTTP version 2
     */
    static final String HTTP_2 = "h2";

    private ApplicationProtocolNames() {
        // No instances
    }
}