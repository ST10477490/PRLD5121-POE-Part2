package com.chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Login class, using the test data supplied in the brief.
 */
public class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        // Register a valid user before each test
        login = new Login("Kyle", "Smith");
        login.registerUser("kyl_1", "Ch&&sec@ke99!");
    }

    // ---------- assertEquals tests ----------

    @Test
    void usernameCorrectlyFormatted() {
        assertEquals("Welcome Kyle, Smith it is great to see you again.",
                login.returnLoginStatus(login.loginUser("kyl_1", "Ch&&sec@ke99!")));
    }

    @Test
    void usernameIncorrectlyFormatted() {
        assertEquals("Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.",
                login.usernameMessage("kyle!!!!!!!"));
    }

    @Test
    void passwordMeetsComplexity() {
        assertEquals("Password successfully captured.",
                login.passwordMessage("Ch&&sec@ke99!"));
    }

    @Test
    void passwordDoesNotMeetComplexity() {
        assertEquals("Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, "
                + "and a special character.",
                login.passwordMessage("password"));
    }

    @Test
    void cellPhoneCorrectlyFormatted() {
        assertEquals("Cell number successfully captured.",
                login.cellNumberMessage("+27838968976"));
    }

    @Test
    void cellPhoneIncorrectlyFormatted() {
        assertEquals("Cell number is incorrectly formatted or does not contain an "
                + "international code; please correct the number and try again.",
                login.cellNumberMessage("08966553"));
    }

    // ---------- assertTrue / assertFalse tests ----------

    @Test
    void loginSuccessful() {
        assertTrue(login.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    @Test
    void loginFailed() {
        assertFalse(login.loginUser("kyl_1", "wrongPassword1!"));
    }

    @Test
    void usernameCorrectlyFormattedTrue() {
        assertTrue(login.checkUserName("kyl_1"));
    }

    @Test
    void usernameIncorrectlyFormattedFalse() {
        assertFalse(login.checkUserName("kyle!!!!!!!"));
    }

    @Test
    void passwordMeetsComplexityTrue() {
        assertTrue(login.checkPasswordComplexity("Ch&&sec@ke99!"));
    }

    @Test
    void passwordDoesNotMeetComplexityFalse() {
        assertFalse(login.checkPasswordComplexity("password"));
    }

    @Test
    void cellPhoneCorrectlyFormattedTrue() {
        assertTrue(login.checkCellPhoneNumber("+27838968976"));
    }

    @Test
    void cellPhoneIncorrectlyFormattedFalse() {
        assertFalse(login.checkCellPhoneNumber("08966553"));
    }
}
