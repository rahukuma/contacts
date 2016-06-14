package com.rahulk.mockserver.contacts.message.Error;

/**
 * Created by Rahul Kumar on 13-05-16.
 */

public final class ErrorMessage {
    public static final String EMAIL_MISSING = "Missing required parameter: email.";
    public static final String EMAIL_EXISTING = "Given email %s exists in contacts";
    public static final String EMAIL_NOT_EXISTING = "Given email %s doesn't exist in contacts";
    public static final String NAME_EMAIL_EXISTING = "Either email or name is mandatory for search";
    public static final String SEARCH_CRITERIA_LENGTH_INSUFFICIENT = "Entered search param shouldn't be less than 3 characters";
}
