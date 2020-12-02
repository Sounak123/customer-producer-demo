package com.learn.pkg.converter;

public interface Converter<T> {
  public T convert(T object);
}
