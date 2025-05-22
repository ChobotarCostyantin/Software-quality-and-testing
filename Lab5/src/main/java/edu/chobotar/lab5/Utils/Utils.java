package edu.chobotar.lab5.Utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/*
  @author User
  @project Lab5
  @class Utils
  @version 1.0.0
  @since 22.05.2025 - 17.12
*/
public class Utils {
    public static String toJson(Object object) throws JsonProcessingException {
        return new ObjectMapper().writeValueAsString(object);
    }
}
