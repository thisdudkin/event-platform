package com.modsen.profile.utils;

/**
 * @author Alexander Dudkin
 */
public final class JdbcUtils {

    private JdbcUtils() {
        throw new AssertionError();
    }

    public static final String GENERATED_KEY_COLUMN = "id";

    public static final String PROFILES_TABLE_NAME = "profiles";
    public static final String PROFILE_EMAILS_TABLE_NAME = "profile_emails";

}
