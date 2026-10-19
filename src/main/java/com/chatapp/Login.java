package com.chatapp;

import java.util.regex.Pattern;

/**
 * Login class for the Chat App (Part 1 - Registration and Login).
 * Handles validation of the username, password and cell phone number,
 * registration of a user, and verification of login details.
 */
public class Login {

    // Details stored when the user registers
    private String username;
    private String password;
    private String cellNumber;
    private final String firstName;
    private final String lastName;

    /**
     * Regular expression for the cell phone number: an international code
     * (a "+" followed by 1-3 digits, e.g. +27) followed by no more than
     * ten digits.
     *
     * Regular expression syntax adapted from (Oracle, n.d.).
     * Full reference at the bottom of this file.
     */
    private static final Pattern CELL_PATTERN = Pattern.compile("^\\+\\d{1,3}\\d{1,10}$");

    /**
     * Creates a Login object for a user.
     *
     * @param firstName the user's first name
     * @param lastName  the user's last name
     */
    public Login(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Checks that the username contains an underscore and is no more than
     * five characters long.
     *
     * @param username the username to check
     * @return true if the username is correctly formatted
     */
    public boolean checkUserName(String username) {
        return username != null && username.contains("_") && username.length() <= 5;
    }

    /**
     * Checks password complexity: at least eight characters, a capital
     * letter, a number and a special character.
     *
     * @param password the password to check
     * @return true if the password meets all complexity rules
     */
    public boolean checkPasswordComplexity(String password) {
        if (password == null || password.length() < 8) {
            return false;
        }

        boolean hasCapital = false;
        boolean hasNumber = false;
        boolean hasSpecial = false;

        // Loop through every character and note which rules are met
        for (int i = 0; i < password.length(); i++) {
            char character = password.charAt(i);
            if (Character.isUpperCase(character)) {
                hasCapital = true;
            } else if (Character.isDigit(character)) {
                hasNumber = true;
            } else if (!Character.isLetterOrDigit(character)) {
                hasSpecial = true;
            }
        }
        return hasCapital && hasNumber && hasSpecial;
    }

    /**
     * Checks that the cell phone number contains an international code and
     * is the correct length.
     *
     * @param cellNumber the cell phone number to check
     * @return true if the number is correctly formatted
     */
    public boolean checkCellPhoneNumber(String cellNumber) {
        return cellNumber != null && CELL_PATTERN.matcher(cellNumber).matches();
    }

    /**
     * @param username the username to check
     * @return the message describing the username check result
     */
    public String usernameMessage(String username) {
        if (checkUserName(username)) {
            return "Username successfully captured.";
        }
        return "Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.";
    }

    /**
     * @param password the password to check
     * @return the message describing the password check result
     */
    public String passwordMessage(String password) {
        if (checkPasswordComplexity(password)) {
            return "Password successfully captured.";
        }
        return "Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, "
                + "and a special character.";
    }

    /**
     * @param cellNumber the cell phone number to check
     * @return the message describing the cell phone number check result
     */
    public String cellNumberMessage(String cellNumber) {
        if (checkCellPhoneNumber(cellNumber)) {
            return "Cell number successfully captured.";
        }
        return "Cell number is incorrectly formatted or does not contain an "
                + "international code; please correct the number and try again.";
    }

    /**
     * Registers the user. The details are only stored if both the username
     * and password are valid.
     *
     * @param username the chosen username
     * @param password the chosen password
     * @return the registration message
     */
    public String registerUser(String username, String password) {
        if (!checkUserName(username)) {
            return usernameMessage(username);
        }
        if (!checkPasswordComplexity(password)) {
            return passwordMessage(password);
        }

        // Both conditions met - store the details for login later
        this.username = username;
        this.password = password;
        return "User has been registered successfully.";
    }

    /**
     * Stores the cell phone number if it is valid.
     *
     * @param cellNumber the cell phone number entered
     * @return true if the number was valid and stored
     */
    public boolean registerCellNumber(String cellNumber) {
        if (checkCellPhoneNumber(cellNumber)) {
            this.cellNumber = cellNumber;
            return true;
        }
        return false;
    }

    /**
     * Verifies that the login details match those stored at registration.
     *
     * @param enteredUsername the username entered at login
     * @param enteredPassword the password entered at login
     * @return true if both match the registered details
     */
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return username != null
                && password != null
                && username.equals(enteredUsername)
                && password.equals(enteredPassword);
    }

    /**
     * Returns the message for a successful or failed login.
     *
     * @param loginSuccessful the result returned by loginUser
     * @return the login status message
     */
    public String returnLoginStatus(boolean loginSuccessful) {
        if (loginSuccessful) {
            return "Welcome " + firstName + ", " + lastName + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
}

/*
 * Reference List
 *
 * Oracle (n.d.) Pattern (Java SE 21 & JDK 21). Available at:
 * https://docs.oracle.com/en/java/javase/21/docs/api/java.base/java/util/regex/Pattern.html
 * (Accessed: 28 September 2026).
 */

