package com.softwareengineering.utils;

import io.javalin.http.Context;
import com.softwareengineering.models.enums.UserTypeEnum;
import java.util.Map;

/**
 * Utility class for handling authentication and authorization
 */
public class AuthUtils {

    /**
     * Validates user session and checks if user has the required user type
     *
     * @param context          The Javalin context containing session information
     * @param requiredUserType The required user type (DOCTOR or PATIENT)
     * @return The user ID if authorized
     * @throws UnauthorizedException if user is not authorized or session is invalid
     */
    public static int validateUserAndGetId(Context context, UserTypeEnum requiredUserType)
            throws UnauthorizedException {
        // Check if user ID exists in session
        Integer id = context.sessionAttribute("id");
        if (id == null || id == 0) {
            throw new UnauthorizedException("No valid session found");
        }

        // Check if user type matches required type
        UserTypeEnum userType = getUserTypeFromSession(context);
        if (!userType.equals(requiredUserType)) {
            throw new UnauthorizedException("Insufficient permissions for this action");
        }

        return id;
    }

    /**
     * Validates that user is a doctor and returns doctor ID
     *
     * @param context The Javalin context
     * @return The doctor ID
     * @throws UnauthorizedException if user is not a doctor or not authorized
     */
    public static int validateDoctorAndGetId(Context context) throws UnauthorizedException {
        return validateUserAndGetId(context, UserTypeEnum.DOCTOR);
    }

    /**
     * Validates that user is a patient and returns patient ID
     *
     * @param context The Javalin context
     * @return The patient ID
     * @throws UnauthorizedException if user is not a patient or not authorized
     */
    public static int validatePatientAndGetId(Context context) throws UnauthorizedException {
        return validateUserAndGetId(context, UserTypeEnum.PATIENT);
    }

    /**
     * Handles unauthorized access by setting appropriate HTTP response
     *
     * @param context The Javalin context
     * @param e       The UnauthorizedException containing the error message
     */
    public static void handleUnauthorized(Context context, UnauthorizedException e) {
        context.status(401).json(Map.of("message", e.getMessage()));
    }

    /**
     * Retrieves the user type from the session
     *
     * @param context The Javalin context
     * @return The UserTypeEnum corresponding to the user type in session
     * @throws UnauthorizedException if user type is not found or invalid
     */
    public static UserTypeEnum getUserTypeFromSession(Context context) throws UnauthorizedException {
        String userTypeStr = context.sessionAttribute("userType");
        if (userTypeStr == null) {
            throw new UnauthorizedException("No user type in session");
        }
        try {
            return UserTypeEnum.valueOf(userTypeStr);
        } catch (IllegalArgumentException e) {
            throw new UnauthorizedException("Invalid user type in session");
        }
    }

    /**
     * Custom exception for unauthorized access
     */
    public static class UnauthorizedException extends Exception {
        public UnauthorizedException(String message) {
            super(message);
        }
    }
}
