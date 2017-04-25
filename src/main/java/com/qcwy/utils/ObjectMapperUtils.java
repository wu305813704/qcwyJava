package com.qcwy.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by KouKi on 2017/3/20.
 */
public class ObjectMapperUtils {
    private static ObjectMapper objectMapper;

    public synchronized static ObjectMapper getInstence() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        return objectMapper;
    }
}
