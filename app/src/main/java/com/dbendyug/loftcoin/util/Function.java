package com.dbendyug.loftcoin.util;

@FunctionalInterface
public interface Function<T, R> {
    R apply(T value, String s);
}
