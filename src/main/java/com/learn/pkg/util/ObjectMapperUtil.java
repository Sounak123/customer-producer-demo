package com.learn.pkg.util;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.learn.pkg.exception.ServiceException;

public class ObjectMapperUtil {

  public static String getJsonFromObj(Object obj) throws ServiceException {
    ObjectMapper objMapper = new ObjectMapper();
    try {
      return objMapper.writeValueAsString(obj);
    } catch (IOException e) {
      throw new ServiceException(
          "Exception encountered while converting to JSON string:" + e.getMessage());
    }
  }
}
