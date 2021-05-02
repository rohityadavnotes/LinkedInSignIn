package com.social.sign.in.linkedin;

public class LinkedInConstants {
    public static final String LINKED_IN_ACCOUNT_DETAILS    = "linked_in_account_details";

    public static final String LINKED_IN_API_HOST           = "https://api.linkedin.com/";
    public static final String LINKED_IN_HOST               = "https://www.linkedin.com/oauth/";
    public static final String API_VERSION                  = "v2/";

    public static final String QUESTION_MARK               = "?";
    public static final String AMPERSAND                   = "&";
    public static final String EQUALS                      = "=";

    public static final String CLIENT_ID                    = "78hbv22g3q9tu0";
    public static final String CLIENT_SECRET                = "Y9X7g67Rbrgwe0sw";

    /**
     * This is any string we want to use. This will be used for avoid CSRF attacks.
     * You can generate as RandomString
     * You can generate one here: http://strongpasswordgenerator.com/
     */
    public static final String STATE                       = "E3ZYKC1T6H2yP4z";

    /**
     * This is the url that LinkedIn Auth process will redirect to. We can put whatever we want that starts with http:// or https://
     * We use a made up url that we will intercept when redirecting.
     */
    public static final String REDIRECT_URI                = "https://backend24.000webhostapp.com/callback";

    public static final String AUTHORIZATION_URL           = "authorization";
    public static final String RESPONSE_TYPE_PARAM         = "response_type";
    public static final String RESPONSE_TYPE_VALUE         = "code";
    public static final String CLIENT_ID_PARAM             = "client_id";
    public static final String REDIRECT_URI_PARAM          = "redirect_uri";
    public static final String STATE_PARAM                 = "state";
    public static final String SCOPE_PARAM                 = "scope";

    public static final String ACCESS_TOKEN_URL            = "accessToken";
    public static final String GRANT_TYPE_PARAM            = "grant_type";
    public static final String GRANT_TYPE                  = "authorization_code";
    public static final String CLIENT_SECRET_PARAM         = "client_secret";


    /**
     * URL-encoded, space-delimited list of member permissions your application
     * is requesting on behalf of the user. These must be explicitly requested. For
     * example, scope=r_liteprofile%20r_emailaddress%20w_member_social.
     * , is same as %20
     */
    /**
     * Allows to read basic information about profile, such as name
     */
    public static final String READ_BASIC_PROFILE           = "r_basicprofile";

    /**
     * Enables access to email address field
     */
    public static final String READ_EMAIL_ADDRESS           = "r_emailaddress";

    /**
     * Enables  to manage business company, retrieve analytics
     */
    public static final String MANAGE_COMPANY               = "rw_company_admin";

    /**
     * Enables ability to share content on LinkedIn
     */
    public static final String SHARING                      = "w_share";

    /**
     * Manage and delete your data including your profile, posts, invitations, and messages
     */
    public static final String COMPLIANCE                   = "w_compliance";

    public static final String SCOPE                        = "r_liteprofile%20r_emailaddress%20w_member_social";

    /**
     * Method that generates the url for get the authorization token
     * api : https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=*&scope=*&state=*&redirect_uri=*
     *
     * @return Url e.g., https://www.linkedin.com/oauth/v2/authorization?response_type=code&client_id=78oind952azzs3&scope=r_liteprofile%20r_emailaddress%20w_member_social&state=E3ZYKC1T6H2yP4z&redirect_uri=https://hellomajorproject.000webhostapp.com/auth/callback
     */
    public static String getAuthorizationUrl() {
        return LINKED_IN_HOST + API_VERSION +
                AUTHORIZATION_URL +
                QUESTION_MARK +
                RESPONSE_TYPE_PARAM + EQUALS + RESPONSE_TYPE_VALUE +
                AMPERSAND + CLIENT_ID_PARAM + EQUALS + CLIENT_ID +
                AMPERSAND + SCOPE_PARAM + EQUALS + SCOPE +
                AMPERSAND + STATE_PARAM + EQUALS + STATE +
                AMPERSAND + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI;
    }

    /**
     * Method that generates the url for get the access token
     * api : https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code&redirect_uri=*&client_id=*&client_secret=*&code=*
     *
     * @return Url e.g., https://www.linkedin.com/oauth/v2/accessToken?grant_type=authorization_code&redirect_uri=https://127.0.0.1/auth/callback&client_id=78oind952azzs3&client_secret=17BLVd0yUK4fOOLa&code=AQTJZ03GUZhslfO-RWBLicZET-1lhp2qRwZ99hszXK2WrsKiCLGm7OvRRlhvV_VSFohbD5Zo0Xezczyv3uIzNErzPPT0ocZQ_8TDqZUOuxCXlPoP2UFbRiuyOMXHMqyNMU-OcFdgSIQvSNe_dTA6gOSj_sxxaWRl0MNMXEsiTjdn7Hln_QxW7Mt8FMZwXg
     */
    public static String getAccessTokenUrl(String authorizationCode) {
        return LINKED_IN_HOST + API_VERSION +
                ACCESS_TOKEN_URL +
                QUESTION_MARK +
                GRANT_TYPE_PARAM + EQUALS + GRANT_TYPE +
                AMPERSAND + REDIRECT_URI_PARAM + EQUALS + REDIRECT_URI +
                AMPERSAND + CLIENT_ID_PARAM + EQUALS + CLIENT_ID +
                AMPERSAND + CLIENT_SECRET_PARAM + EQUALS + CLIENT_SECRET +
                AMPERSAND + RESPONSE_TYPE_VALUE + EQUALS + authorizationCode;
    }
}
