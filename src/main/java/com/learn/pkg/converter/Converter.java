package com.learn.pkg.converter;

public interface Converter<T, U> {
  public U convert(T object);
}
