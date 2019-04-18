package com.authenticationservice.configuration;

import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Global values used in different part of the application
 */
public class Constants {

    // Database schema on which the entities have been included
    public static final String DATABASE_SCHEMA = "eat";


    // Constants related with JWT
    public static final class JWT {

        // Algorithm used to encrypt the token
        public static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS512;

        // Key on JWT's payload used to store the role information of the user
        public static final String ROLES_KEY = "roles";
    }

}