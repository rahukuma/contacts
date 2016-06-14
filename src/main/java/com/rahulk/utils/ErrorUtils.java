package com.rahulk.utils;

import com.google.gson.Gson;
import com.rahulk.mockserver.contacts.message.Error.ErrorsDTO;

import java.util.Arrays;

/**
 * Created by Rahul Kumar on 13-05-16.
 */

public class ErrorUtils {
    public static String get(int code, String errorMessage) {
        ErrorsDTO errorsDTO = new ErrorsDTO();
        ErrorsDTO.Errors errors = new ErrorsDTO().new Errors();
        errors.setCode(code);
        errors.setMessage(errorMessage);

        errorsDTO.setErrors(Arrays.asList(errors));
        return new Gson().toJson(errorsDTO);
    }
}
