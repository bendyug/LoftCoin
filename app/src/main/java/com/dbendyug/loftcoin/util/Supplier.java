package com.dbendyug.loftcoin.util;

@FunctionalInterface
public interface Supplier<T> {
    T get();
}
