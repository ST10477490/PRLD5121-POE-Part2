package com.chatapp;

import java.util.Scanner;

/**
 * Console application: registration and login (Part 1), then QuickChat
 * sending messages (Part 2).
 */
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);

        // ===== PART 1: REGISTRATION =====
        System.out.println("===== REGISTRATION =====");
        System.out.print("Enter your first name: ");
        String firstName = input.nextLine();
        System.out.print("Enter your last name: ");
        String lastName = input.nextLine();

        Login login = new Login(firstName, lastName);

        String username;
        do {
            System.out.print("Enter a username: ");
            username = input.nextLine();
            System.out.println(login.usernameMessage(username));
        } while (!login.checkUserName(username));

        String password;
        do {
            System.out.print("Enter a password: ");
            password = input.nextLine();
            System.out.println(login.passwordMessage(password));
        } while (!login.checkPasswordComplexity(password));

        String cellNumber;
        do {
            System.out.print("Enter your cell phone number (e.g. +27838968976): ");
            cellNumber = input.nextLine();
            System.out.println(login.cellNumberMessage(cellNumber));
        } while (!login.registerCellNumber(cellNumber));

        System.out.println(login.registerUser(username, password));

        // ===== PART 1: LOGIN =====
        System.out.println();
        System.out.println("===== LOGIN =====");
        boolean loggedIn;
        do {
            System.out.print("Enter your username: ");
            String enteredUsername = input.nextLine();
            System.out.print("Enter your password: ");
            String enteredPassword = input.nextLine();

            loggedIn = login.loginUser(enteredUsername, enteredPassword);
            System.out.println(login.returnLoginStatus(loggedIn));
        } while (!loggedIn);

        // ===== PART 2: QUICKCHAT (only reached after a successful login) =====
        runQuickChat(input);
        input.close();
    }

    /**
     * Runs the QuickChat menu until the user chooses to quit.
     */
    private static void runQuickChat(Scanner input) {
        System.out.println();
        System.out.println("Welcome to QuickChat.");

        int messageLimit = readPositiveInt(input, "How many messages do you wish to enter? ");
        int messagesEntered = 0;
        boolean running = true;

        while (running) {
            System.out.println();
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Quit");
            int choice = readInt(input, "Choose an option: ");

            switch (choice) {
                case 1:
                    if (messagesEntered >= messageLimit) {
                        System.out.println("You have entered all " + messageLimit + " messages.");
                    } else {
                        messagesEntered++;
                        sendOneMessage(input);
                    }
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    running = false;
                    break;
                default:
                    System.out.println("Invalid option. Please choose 1, 2 or 3.");
            }
        }

        System.out.println("Total number of messages sent: " + Message.returnTotalMessages());
    }

    /**
     * Collects one message from the user and sends, discards or stores it.
     */
    private static void sendOneMessage(Scanner input) {
        // Recipient
        String recipient;
        do {
            System.out.print("Enter the recipient cell number: ");
            recipient = input.nextLine();
            if (!Message.isValidRecipient(recipient)) {
                System.out.println("Cell phone number is incorrectly formatted or does not contain "
                        + "an international code. Please correct the number and try again.");
            }
        } while (!Message.isValidRecipient(recipient));

        // Message text (250 characters maximum)
        String text;
        do {
            System.out.print("Enter your message: ");
            text = input.nextLine();
            System.out.println(Message.checkMessageLength(text));
        } while (!Message.isValidLength(text) || text.trim().isEmpty());

        Message message = new Message(recipient, text);
        System.out.println(message.messageIdGenerated());

        // Send, disregard or store
        System.out.println("1) Send Message");
        System.out.println("2) Disregard Message");
        System.out.println("3) Store Message to send later");
        int option = readInt(input, "Choose an option: ");
        System.out.println(message.sentMessage(option));

        if (option == 1) {
            System.out.println();
            System.out.println(message.getDetails());
        } else if (option == 2) {
            System.out.print("Press 0 to delete the message: ");
            if (input.nextLine().trim().equals("0")) {
                System.out.println("Message deleted.");
            }
        }
    }

    /** Reads a whole number, asking again until the input is valid. */
    private static int readInt(Scanner input, String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Integer.parseInt(input.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a whole number.");
            }
        }
    }

    /** Reads a whole number greater than zero. */
    private static int readPositiveInt(Scanner input, String prompt) {
        int value;
        do {
            value = readInt(input, prompt);
            if (value < 1) {
                System.out.println("Please enter a number greater than 0.");
            }
        } while (value < 1);
        return value;
    }
}
