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
package io.servicetalk.client.api.partition;

import static java.util.Objects.requireNonNull;

/**
 * Thrown when a request is issued but there is no associated partition to send the request to.
 */
public final class UnknownPartitionException extends IllegalStateException {
    private static final long serialVersionUID = 2870933612596267788L;

    private final PartitionAttributes partitionSelector;

    /**
     * Create a new instance.
     * @param partitionSelector The {@link PartitionAttributes} that failed to select a partition.
     * @param msg The descriptive message.
     */
    public UnknownPartitionException(PartitionAttributes partitionSelector, String msg) {
        super(msg);
        this.partitionSelector = requireNonNull(partitionSelector);
    }

    /**
     * Get the {@link PartitionAttributes} that was used to select the partition.
     * @return the {@link PartitionAttributes} that was used to select the partition.
     */
    public PartitionAttributes getPartitionSelector() {
        return partitionSelector;
    }
}