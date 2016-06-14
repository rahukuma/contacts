package com.rahulk.utils;

import java.util.UUID;

/**
 * Created by Rahul Kumar on 13-05-16.
 *
 */

public final class StringUtils {
    public static String getRandom() {
        return  UUID.randomUUID().toString();
    }

}
