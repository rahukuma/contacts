package com.rahulk.mockserver.contacts.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rahulk.mockserver.contacts.message.ContactDTO;

/**
 * Created by rahulk on 14-06-2016.
 */
public class JsonUtils {
    static Gson gson = new GsonBuilder().serializeNulls().create();

    public static boolean compare(String json1, String json2) {
        ContactDTO contactDTO1 = gson.fromJson(json1, ContactDTO.class);
        ContactDTO contactDTO2 = gson.fromJson(json2, ContactDTO.class);
        boolean result = false;
        if (contactDTO1.getEmail() != null && contactDTO2.getEmail() != null) {
            if (contactDTO1.getEmail().equals(contactDTO2.getEmail())) {
                result = true;
            }
            if (contactDTO1.getName() != null && contactDTO2.getName() != null) {
                if (contactDTO1.getName().equals(contactDTO2.getName())) {
                    result = true;
                } else {
                    result = false;
                }
            }
            if (contactDTO1.getAddress() != null && contactDTO2.getAddress() != null) {
                if (contactDTO1.getAddress().equals(contactDTO2.getAddress())) {
                    result = true;
                } else {
                    result = false;
                }
            }
            if (contactDTO1.getPhone() != null && contactDTO2.getPhone() != null) {
                if (contactDTO1.getPhone().equals(contactDTO2.getPhone())) {
                    result = true;
                } else {
                    result = false;
                }
            }
        }
        return result;
    }
}
