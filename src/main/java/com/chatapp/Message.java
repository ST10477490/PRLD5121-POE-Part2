package com.chatapp;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Message class for QuickChat (Part 2 - Sending Messages).
 * Creates, validates, sends, discards and stores messages.
 */
public class Message {

    // Maximum allowed length of a message
    private static final int MAX_MESSAGE_LENGTH = 250;

    // Reuses the cell phone validation written in Part 1
    private static final Login CELL_VALIDATOR = new Login("", "");

    // All messages sent while the program is running
    private static final List<Message> sentMessages = new ArrayList<>();

    // All messages the user chose to store for later
    private static final List<Message> storedMessages = new ArrayList<>();

    // Counts messages as they are created (the first message is number 0)
    private static int messageCounter = 0;

    // File that stored messages are written to
    private static String storagePath = "messages.json";

    private final String messageId;
    private final int numMessagesSent;
    private final String recipient;
    private final String text;
    private final String messageHash;

    /**
     * Creates a message with a randomly generated ten-digit ID.
     *
     * @param recipient the recipient's cell phone number
     * @param text      the message text
     */
    public Message(String recipient, String text) {
        this(generateMessageId(), messageCounter++, recipient, text);
    }

    /**
     * Creates a message with a supplied ID and number (used for testing).
     *
     * @param messageId       the message ID
     * @param numMessagesSent the message number
     * @param recipient       the recipient's cell phone number
     * @param text            the message text
     */
    public Message(String messageId, int numMessagesSent, String recipient, String text) {
        this.messageId = messageId;
        this.numMessagesSent = numMessagesSent;
        this.recipient = recipient;
        this.text = text;
        this.messageHash = createMessageHash();
    }

    /**
     * Generates a random ten-digit message ID.
     *
     * @return a ten-digit number as a String
     */
    public static String generateMessageId() {
        long number = ThreadLocalRandom.current().nextLong(10_000_000_000L);
        return String.format("%010d", number);
    }

    /**
     * Checks that the message ID is not more than ten characters.
     *
     * @return true if the ID is valid
     */
    public boolean checkMessageID() {
        return messageId != null && !messageId.isEmpty() && messageId.length() <= 10;
    }

    /**
     * @return the message telling the user that an ID was generated
     */
    public String messageIdGenerated() {
        return "Message ID generated: " + messageId;
    }

    /**
     * Checks whether a recipient cell number is valid (international code and
     * no more than ten digits after it).
     *
     * @param cellNumber the number to check
     * @return true if valid
     */
    public static boolean isValidRecipient(String cellNumber) {
        return CELL_VALIDATOR.checkCellPhoneNumber(cellNumber);
    }

    /**
     * Checks the recipient cell number of this message.
     *
     * @return the success or failure message
     */
    public String checkRecipientCell() {
        if (isValidRecipient(recipient)) {
            return "Cell phone number successfully captured.";
        }
        return "Cell phone number is incorrectly formatted or does not contain an "
                + "international code. Please correct the number and try again.";
    }

    /**
     * Checks whether a message is within the 250 character limit.
     *
     * @param messageText the text to check
     * @return true if the text is 250 characters or less
     */
    public static boolean isValidLength(String messageText) {
        return messageText != null && messageText.length() <= MAX_MESSAGE_LENGTH;
    }

    /**
     * Checks the length of a message and returns the matching message.
     *
     * @param messageText the text to check
     * @return the success or failure message
     */
    public static String checkMessageLength(String messageText) {
        if (isValidLength(messageText)) {
            return "Message ready to send.";
        }
        int excess = messageText.length() - MAX_MESSAGE_LENGTH;
        return "Message exceeds 250 characters by " + excess + "; please reduce the size.";
    }

    /**
     * Creates the message hash: first two digits of the ID, a colon, the
     * message number, a colon, then the first and last words in capitals.
     * Example: 00:0:HITHANKS
     *
     * @return the message hash
     */
    public String createMessageHash() {
        String firstTwo = messageId.length() >= 2 ? messageId.substring(0, 2) : messageId;

        // Split the message into words and remove punctuation from the ends
        String[] words = text.trim().split("\\s+");
        String firstWord = words[0].replaceAll("[^A-Za-z0-9]", "");
        String lastWord = words[words.length - 1].replaceAll("[^A-Za-z0-9]", "");

        return (firstTwo + ":" + numMessagesSent + ":" + firstWord + lastWord).toUpperCase();
    }

    /**
     * Carries out the user's choice for this message.
     *
     * @param choice 1 = send, 2 = disregard, 3 = store for later
     * @return the message for the chosen option
     */
    public String sentMessage(int choice) {
        switch (choice) {
            case 1:
                sentMessages.add(this);
                return "Message successfully sent.";
            case 2:
                return "Press 0 to delete the message.";
            case 3:
                if (storeMessage()) {
                    return "Message successfully stored.";
                }
                return "Message could not be stored.";
            default:
                return "Invalid option. Please choose 1, 2 or 3.";
        }
    }

    /**
     * @return the full details of this message in the required order
     */
    public String getDetails() {
        return "Message ID: " + messageId + "\n"
                + "Message Hash: " + messageHash + "\n"
                + "Recipient: " + recipient + "\n"
                + "Message: " + text;
    }

    /**
     * @return the details of all messages sent while the program is running
     */
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages have been sent.";
        }
        StringBuilder output = new StringBuilder();
        for (Message message : sentMessages) {
            output.append(message.getDetails()).append("\n\n");
        }
        return output.toString().trim();
    }

    /**
     * @return the total number of messages sent
     */
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    /**
     * Stores this message, and every other stored message, in a JSON file.
     * The JSON is written by hand: an array of objects, one per message.
     *
     * @return true if the file was written successfully
     */
    public boolean storeMessage() {
        storedMessages.add(this);

        StringBuilder json = new StringBuilder("[\n");
        for (int i = 0; i < storedMessages.size(); i++) {
            Message m = storedMessages.get(i);
            json.append("  {\n")
                .append("    \"messageId\": \"").append(escapeJson(m.messageId)).append("\",\n")
                .append("    \"messageNumber\": ").append(m.numMessagesSent).append(",\n")
                .append("    \"messageHash\": \"").append(escapeJson(m.messageHash)).append("\",\n")
                .append("    \"recipient\": \"").append(escapeJson(m.recipient)).append("\",\n")
                .append("    \"message\": \"").append(escapeJson(m.text)).append("\"\n")
                .append("  }");
            json.append(i < storedMessages.size() - 1 ? ",\n" : "\n");
        }
        json.append("]\n");

        try {
            Path path = Paths.get(storagePath);
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            Files.write(path, json.toString().getBytes(StandardCharsets.UTF_8));
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Escapes special characters so text is valid inside a JSON string.
     */
    private static String escapeJson(String value) {
        StringBuilder escaped = new StringBuilder();
        for (char c : value.toCharArray()) {
            switch (c) {
                case '"':  escaped.append("\\\""); break;
                case '\\': escaped.append("\\\\"); break;
                case '\n': escaped.append("\\n");  break;
                case '\r': escaped.append("\\r");  break;
                case '\t': escaped.append("\\t");  break;
                default:
                    if (c < 0x20) {
                        escaped.append(String.format("\\u%04x", (int) c));
                    } else {
                        escaped.append(c);
                    }
            }
        }
        return escaped.toString();
    }

    /**
     * Clears all stored and sent messages and resets the counter.
     * Used so that each unit test starts fresh.
     */
    public static void resetAll() {
        sentMessages.clear();
        storedMessages.clear();
        messageCounter = 0;
    }

    /**
     * Changes the file used by storeMessage (used for testing).
     */
    public static void setStoragePath(String path) {
        storagePath = path;
    }

    public String getMessageId() { return messageId; }
    public int getNumMessagesSent() { return numMessagesSent; }
    public String getRecipient() { return recipient; }
    public String getText() { return text; }
    public String getMessageHash() { return messageHash; }
}
