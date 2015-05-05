/**
 *
 * Copyright (c) 2006-2015, Speedment, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); You may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.speedment.util.stream.builder.pipeline;

import com.speedment.util.stream.builder.action.Action;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.BaseStream;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

/**
 *
 * @author pemi
 * @param <E> The type element that the last BaseStream holds
 *
 */
public class BasePipeline<E> implements Pipeline, ReferencePipeline<E>, IntPipeline, LongPipeline, DoublePipeline {

    private final LinkedList<Action<?, ?>> list;
    private Supplier<BaseStream<?, ?>> initialSupplier;

//    public BasePipeline(BasePipeline<E> parent) {
//        list = parent.list;
//        initialSupplier = parent.initialSupplier;
//    }
    public BasePipeline(Supplier<BaseStream<?, ?>> initialSupplier) {
        this.initialSupplier = Objects.requireNonNull(initialSupplier);
        this.list = new LinkedList<>();
    }

    public Class<? extends BaseStream> getLastStreamClass() {
        return getLast().resultStreamClass();
    }

    public <E, S extends BaseStream<E, S>> BaseStream<E, S> getAsBaseStream() {
        return (BaseStream<E, S>) getStream();
    }

    @Override
    public Stream<E> getAsReferenceStream() {
        return (Stream<E>) getStream();
    }

    @Override
    public IntStream getAsIntStream() {
        return (IntStream) getStream();
    }

    @Override
    public LongStream getAsLongStream() {
        return (LongStream) getStream();
    }

    @Override
    public DoubleStream getAsDoubleStream() {
        return (DoubleStream) getStream();
    }

    private BaseStream<E, ?> getStream() {
        BaseStream<?, ?> result = getInitialSupplier().get();
        //System.out.println("Applying " + toString());
        for (Action<?, ?> action : this) {
            result = cast(result, action);
        }

        return (BaseStream<E, ?>) result;
    }

    private <In extends BaseStream<?, ?>, Out extends BaseStream<?, Out>> Out cast(In in, Action<?, ?> action) {
        final Function<In, Out> mapper = (Function<In, Out>) action.get();
        return mapper.apply(in);
    }

    // Delegators
    @Override
    public Action<?, ?> getFirst() {
        return list.getFirst();
    }

    @Override
    public Action<?, ?> getLast() {
        return list.getLast();
    }

    @Override
    public Action<?, ?> removeFirst() {
        return list.removeFirst();
    }

    @Override
    public Action<?, ?> removeLast() {
        return list.removeLast();
    }

    @Override
    public void addFirst(Action<?, ?> e) {
        list.addFirst(e);
    }

    @Override
    public void addLast(Action<?, ?> e) {
        list.addLast(e);
    }

    @Override
    public int size() {
        return list.size();
    }

    @Override
    public boolean add(Action<?, ?> e) {
        return list.add(e);
    }

    @Override
    public void clear() {
        list.clear();
    }

    @Override
    public Action<?, ?> get(int index) {
        return list.get(index);
    }

    @Override
    public void add(int index, Action<?, ?> element) {
        list.add(index, element);
    }

    @Override
    public Action<?, ?> remove(int index) {
        return list.remove(index);
    }

    @Override
    public Iterator<Action<?, ?>> iterator() {
        return list.iterator();
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }

    @Override
    public String toString() {
        return list.toString();
    }

    @Override
    public Stream<Action<?, ?>> stream() {
        return list.stream();
    }

    @Override
    public Supplier<BaseStream<?, ?>> getInitialSupplier() {
        return initialSupplier;
    }

    @Override
    public void setInitialSupplier(Supplier<BaseStream<?, ?>> initialSupplier) {
        this.initialSupplier = Objects.requireNonNull(initialSupplier);
    }

}