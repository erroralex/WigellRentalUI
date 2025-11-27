package com.nilsson.camping.app;

/**
 * A static class to hold application-wide user session data.
 * This ensures the logged-in user information is accessible across different
 * views and services after authentication.
 */
public class UserSession {

    private static String currentUsername = "Guest";
    private static boolean isLoggedIn = false;

    // Sets the session data upon successful login.
    public static void login(String username) {
        currentUsername = username;
        isLoggedIn = true;
        System.out.println("User " + username + " logged in.");
    }

    // Clears the session data upon logout.
    public static void logout() {
        currentUsername = "Guest";
        isLoggedIn = false;
        System.out.println("User logged out.");
    }

    public static String getCurrentUsername() {
        return currentUsername;
    }

    public static boolean isLoggedIn() {
        return isLoggedIn;
    }
}