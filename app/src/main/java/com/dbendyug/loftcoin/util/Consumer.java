package com.dbendyug.loftcoin.util;

@FunctionalInterface
public interface Consumer<T> {

    void apply(T value);
}
