package com.chatapp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for the Message class, using the test data supplied in the brief.
 */
public class MessageTest {

    // Test data 1 and 2 from the brief
    private static final String RECIPIENT_1 = "+27718693002";
    private static final String TEXT_1 = "Hi Mike, can you join us for dinner tonight?";
    private static final String RECIPIENT_2 = "08575975889";
    private static final String TEXT_2 = "Hi Keegan, did you receive the payment?";

    private Message message1;
    private Message message2;

    @BeforeEach
    void setUp() {
        Message.resetAll();
        Message.setStoragePath("target/test-messages.json");
        message1 = new Message("0012345678", 0, RECIPIENT_1, TEXT_1);
        message2 = new Message("0098765432", 1, RECIPIENT_2, TEXT_2);
    }

    // ---------- assertEquals tests ----------

    @Test
    void messageNotMoreThan250Characters() {
        assertEquals("Message ready to send.", Message.checkMessageLength(TEXT_1));
    }

    @Test
    void messageMoreThan250Characters() {
        String longMessage = "a".repeat(260);
        assertEquals("Message exceeds 250 characters by 10; please reduce the size.",
                Message.checkMessageLength(longMessage));
    }

    @Test
    void recipientNumberCorrectlyFormatted() {
        assertEquals("Cell phone number successfully captured.", message1.checkRecipientCell());
    }

    @Test
    void recipientNumberIncorrectlyFormatted() {
        assertEquals("Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.",
                message2.checkRecipientCell());
    }

    @Test
    void messageHashIsCorrectForTestCase1() {
        assertEquals("00:0:HITONIGHT", message1.createMessageHash());
    }

    @Test
    void messageHashesAreCorrectInALoop() {
        Message[] messages = { message1, message2 };
        String[] expectedHashes = { "00:0:HITONIGHT", "00:1:HIPAYMENT" };

        for (int i = 0; i < messages.length; i++) {
            assertEquals(expectedHashes[i], messages[i].createMessageHash());
        }
    }

    @Test
    void messageIdIsCreated() {
        Message generated = new Message(RECIPIENT_1, TEXT_1);
        assertEquals("Message ID generated: " + generated.getMessageId(),
                generated.messageIdGenerated());
        assertEquals(10, generated.getMessageId().length());
    }

    @Test
    void messageSentOptionSend() {
        assertEquals("Message successfully sent.", message1.sentMessage(1));
    }

    @Test
    void messageSentOptionDisregard() {
        assertEquals("Press 0 to delete the message.", message2.sentMessage(2));
    }

    @Test
    void messageSentOptionStore() {
        assertEquals("Message successfully stored.", message1.sentMessage(3));
    }

    @Test
    void totalMessagesSentIsCorrect() {
        message1.sentMessage(1);   // sent
        message2.sentMessage(2);   // disregarded - not counted
        assertEquals(1, Message.returnTotalMessages());
    }

    // ---------- assertTrue / assertFalse tests ----------

    @Test
    void messageIdIsValidLength() {
        assertTrue(message1.checkMessageID());
    }

    @Test
    void messageIdTooLongIsInvalid() {
        Message badId = new Message("12345678901", 0, RECIPIENT_1, TEXT_1);
        assertFalse(badId.checkMessageID());
    }

    @Test
    void recipientCorrectlyFormattedTrue() {
        assertTrue(Message.isValidRecipient(RECIPIENT_1));
    }

    @Test
    void recipientIncorrectlyFormattedFalse() {
        assertFalse(Message.isValidRecipient(RECIPIENT_2));
    }

    @Test
    void messageLengthTrueAndFalse() {
        assertTrue(Message.isValidLength(TEXT_1));
        assertFalse(Message.isValidLength("a".repeat(251)));
    }
}
