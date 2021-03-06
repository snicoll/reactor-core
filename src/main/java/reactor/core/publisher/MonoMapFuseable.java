/*
 * Copyright (c) 2011-2016 Pivotal Software Inc, All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package reactor.core.publisher;

import java.util.Objects;
import java.util.function.Function;

import org.reactivestreams.*;

import reactor.core.Fuseable;

/**
 * Maps the values of the source publisher one-on-one via a mapper function.
 * <p>
 * This variant allows composing fuseable stages.
 * 
 * @param <T> the source value type
 * @param <R> the result value type
 */

/**
 * @see <a href="https://github.com/reactor/reactive-streams-commons">Reactive-Streams-Commons</a>
 */
final class MonoMapFuseable<T, R> extends MonoSource<T, R>
		implements Fuseable {

	final Function<? super T, ? extends R> mapper;

	/**
	 * Constructs a StreamMap instance with the given source and mapper.
	 *
	 * @param source the source Publisher instance
	 * @param mapper the mapper function
	 * @throws NullPointerException if either {@code source} or {@code mapper} is null.
	 */
	public MonoMapFuseable(Publisher<? extends T> source, Function<? super T, ? extends R> mapper) {
		super(source);
		this.mapper = Objects.requireNonNull(mapper, "mapper");
	}

	public Function<? super T, ? extends R> mapper() {
		return mapper;
	}

	@Override
	public void subscribe(Subscriber<? super R> s) {
		if (s instanceof ConditionalSubscriber) {
			
			ConditionalSubscriber<? super R> cs = (ConditionalSubscriber<? super R>) s;
			source.subscribe(new FluxMapFuseable.MapFuseableConditionalSubscriber<>(cs, mapper));
			return;
		}
		source.subscribe(new FluxMapFuseable.MapFuseableSubscriber<>(s, mapper));
	}

}
