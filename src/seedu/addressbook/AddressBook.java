package seedu.addressbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class AddressBook {

    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ArrayList<String[]> ALL_PERSONS = new ArrayList<>();
    private static ArrayList<String[]> latestPersonListingView = ALL_PERSONS;

    private static String storageFilePath;

    public static void main(String[] args) {
        String[] message = { "===================================================",
                "===================================================", "AddessBook Level 1 - Version 1.0",
                "Welcome to your Address Book!", "===================================================" };
        for (String m : message) {
            System.out.println("|| " + m);
        }
        if (args.length >= 2) {
            String[] message1 = { "Too many parameters! Correct program argument format:" + System.lineSeparator()
                    + "|| " + "\tjava AddressBook" + System.lineSeparator() + "|| "
                    + "\tjava AddressBook [custom storage file path]" };
            for (String m : message1) {
                System.out.println("|| " + m);
            }
            String[] message2 = { "Exiting Address Book... Good bye!",
                    "===================================================",
                    "===================================================" };
            for (String m1 : message2) {
                System.out.println("|| " + m1);
            }
            System.exit(0);
        }

        if (args.length == 1) {
            String filePath = args[0];
            if (!filePath.endsWith(".txt")) {
                String[] message1 = { String.format("The given file name [%1$s] is not a valid file name!", filePath) };
                for (String m1 : message1) {
                    System.out.println("|| " + m1);
                }
                String[] message2 = { "Exiting Address Book... Good bye!",
                        "===================================================",
                        "===================================================" };
                for (String m11 : message2) {
                    System.out.println("|| " + m11);
                }
                System.exit(0);
            }

            storageFilePath = filePath;
            final File storageFile = new File(filePath);
            if (!storageFile.exists()) {
                String[] message1 = { String.format("Storage file missing: %1$s", filePath) };

                for (String m1 : message1) {
                    System.out.println("|| " + m1);
                }

                try {
                    storageFile.createNewFile();
                    String[] message2 = { String.format("Created new empty storage file: %1$s", filePath) };
                    for (String m3 : message2) {
                        System.out.println("|| " + m3);
                    }
                } catch (IOException ioe) {
                    String[] message3 = { String.format("Error: unable to create file: %1$s", filePath) };
                    for (String m2 : message3) {
                        System.out.println("|| " + m2);
                    }
                    String[] message4 = { "Exiting Address Book... Good bye!",
                            "===================================================",
                            "===================================================" };
                    for (String m11 : message4) {
                        System.out.println("|| " + m11);
                    }
                    System.exit(0);
                }
            }
        }

        if (args.length == 0) {
            String[] message1 = { "Using default storage file : " + "addressbook.txt" };
            for (String m1 : message1) {
                System.out.println("|| " + m1);
            }
            storageFilePath = "addressbook.txt";
            final File storageFile = new File(storageFilePath);
            if (!storageFile.exists()) {
                String[] message2 = { String.format("Storage file missing: %1$s", storageFilePath) };

                for (String m3 : message2) {
                    System.out.println("|| " + m3);
                }

                try {
                    storageFile.createNewFile();
                    String[] message3 = { String.format("Created new empty storage file: %1$s", storageFilePath) };
                    for (String m2 : message3) {
                        System.out.println("|| " + m2);
                    }
                } catch (IOException ioe) {
                    String[] message4 = { String.format("Error: unable to create file: %1$s", storageFilePath) };
                    for (String m4 : message4) {
                        System.out.println("|| " + m4);
                    }
                    String[] message5 = { "Exiting Address Book... Good bye!",
                            "===================================================",
                            "===================================================" };
                    for (String m11 : message5) {
                        System.out.println("|| " + m11);
                    }
                    System.exit(0);
                }
            }
        }
        ArrayList<String[]> persons = null;
        ArrayList<String> lines1 = null;
        try {
            lines1 = new ArrayList<String>(Files.readAllLines(Paths.get(storageFilePath)));
        } catch (FileNotFoundException fnfe) {
            String[] message9 = { String.format("Storage file missing: %1$s", storageFilePath) };
            for (String m7 : message9) {
                System.out.println("|| " + m7);
            }
            String[] message12 = { "Exiting Address Book... Good bye!",
                    "===================================================",
                    "===================================================" };
            for (String m12 : message12) {
                System.out.println("|| " + m12);
            }
            System.exit(0);
        } catch (IOException ioe) {
            String[] message8 = { String.format("Unexpected error: unable to read from file: %1$s", storageFilePath) };
            for (String m6 : message8) {
                System.out.println("|| " + m6);
            }
            String[] message11 = { "Exiting Address Book... Good bye!",
                    "===================================================",
                    "===================================================" };
            for (String m11 : message11) {
                System.out.println("|| " + m11);
            }
            System.exit(0);
        }
        ArrayList<String> lines = lines1;
        final ArrayList<String[]> decodedPersons = new ArrayList<>();
        Optional<ArrayList<String[]>> myOutput = null;
        for (String encodedPerson : lines) {
            Optional<String[]> output1 = null;
            final String matchAnyPersonDataPrefix = "p/" + '|' + "e/";
            final String[] splitArgs = encodedPerson.trim().split(matchAnyPersonDataPrefix);
            final boolean isExtractable = splitArgs.length == 3 && !splitArgs[0].isEmpty() && !splitArgs[1].isEmpty()
                    && !splitArgs[2].isEmpty();
            if (!isExtractable) {
                output1 = Optional.empty();
            } else {
                final String[] person3 = new String[3];
                final int indexOfPhonePrefix2 = encodedPerson.indexOf("p/");
                final int indexOfEmailPrefix2 = encodedPerson.indexOf("e/");
                int indexOfFirstPrefix = Math.min(indexOfEmailPrefix2, indexOfPhonePrefix2);
                person3[0] = encodedPerson.substring(0, indexOfFirstPrefix).trim();
                final int indexOfPhonePrefix = encodedPerson.indexOf("p/");
                final int indexOfEmailPrefix = encodedPerson.indexOf("e/");
                String output = null;
                if (indexOfPhonePrefix > indexOfEmailPrefix) {
                    output = encodedPerson.substring(indexOfPhonePrefix, encodedPerson.length()).trim().replace("p/",
                            "");
                } else {
                    output = encodedPerson.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim().replace("p/", "");
                }
                person3[1] = output;
                final int indexOfPhonePrefix1 = encodedPerson.indexOf("p/");
                final int indexOfEmailPrefix1 = encodedPerson.indexOf("e/");
                String output2 = null;
                if (indexOfEmailPrefix1 > indexOfPhonePrefix1) {
                    output2 = encodedPerson.substring(indexOfEmailPrefix1, encodedPerson.length()).trim().replace("e/",
                            "");
                } else {
                    output2 = encodedPerson.substring(indexOfEmailPrefix1, indexOfPhonePrefix1).trim().replace("e/",
                            "");
                }
                person3[2] = output2;
                final String[] decodedPerson11 = person3;
                output1 = decodedPerson11[0].matches("(\\w|\\s)+") && decodedPerson11[1].matches("\\d+")
                        && decodedPerson11[2].matches("\\S+@\\S+\\.\\S+") ? Optional.of(decodedPerson11)
                                : Optional.empty();
            }
            final Optional<String[]> decodedPerson1 = output1;
            if (!decodedPerson1.isPresent()) {
                myOutput = Optional.empty();
                break;
            }
            decodedPersons.add(decodedPerson1.get());
        }
        if (myOutput == null) {
            myOutput = Optional.of(decodedPersons);
        }
        final Optional<ArrayList<String[]>> successfullyDecoded = myOutput;
        if (!successfullyDecoded.isPresent()) {
            String[] message6 = { "Storage file has invalid content" };
            for (String m : message6) {
                System.out.println("|| " + m);
            }
            String[] message7 = { "Exiting Address Book... Good bye!",
                    "===================================================",
                    "===================================================" };
            for (String m1 : message7) {
                System.out.println("|| " + m1);
            }
            System.exit(0);
        }
        persons = successfullyDecoded.get();
        ALL_PERSONS.clear();
        ALL_PERSONS.addAll(persons);
        while (true) {
            System.out.print("|| " + "Enter command: ");
            String inputLine = SCANNER.nextLine();
            while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == '#') {
                inputLine = SCANNER.nextLine();
            }
            String userCommand = inputLine;
            String[] message1 = { "[Command entered:" + userCommand + "]" };
            for (String m1 : message1) {
                System.out.println("|| " + m1);
            }
            String feedback = null;
            final String[] split = userCommand.trim().split("\\s+", 2);
            final String[] commandTypeAndParams = split.length == 2 ? split : new String[] { split[0], "" };
            final String commandType = commandTypeAndParams[0];
            final String commandArgs = commandTypeAndParams[1];
            switch (commandType) {
            case "add":
                Optional<String[]> output = null;
                final String matchAnyPersonDataPrefix = "p/" + '|' + "e/";
                final String[] splitArgs = commandArgs.trim().split(matchAnyPersonDataPrefix);
                final boolean isExtractable = splitArgs.length == 3 && !splitArgs[0].isEmpty()
                        && !splitArgs[1].isEmpty() && !splitArgs[2].isEmpty();
                if (!isExtractable) {
                    output = Optional.empty();
                } else {
                    final String[] person2 = new String[3];
                    final int indexOfPhonePrefix2 = commandArgs.indexOf("p/");
                    final int indexOfEmailPrefix2 = commandArgs.indexOf("e/");
                    int indexOfFirstPrefix = Math.min(indexOfEmailPrefix2, indexOfPhonePrefix2);
                    person2[0] = commandArgs.substring(0, indexOfFirstPrefix).trim();
                    final int indexOfPhonePrefix = commandArgs.indexOf("p/");
                    final int indexOfEmailPrefix = commandArgs.indexOf("e/");
                    String output1 = null;
                    if (indexOfPhonePrefix > indexOfEmailPrefix) {
                        output1 = commandArgs.substring(indexOfPhonePrefix, commandArgs.length()).trim().replace("p/",
                                "");
                    } else {
                        output1 = commandArgs.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim().replace("p/",
                                "");
                    }
                    person2[1] = output1;
                    final int indexOfPhonePrefix1 = commandArgs.indexOf("p/");
                    final int indexOfEmailPrefix1 = commandArgs.indexOf("e/");
                    String output2 = null;
                    if (indexOfEmailPrefix1 > indexOfPhonePrefix1) {
                        output2 = commandArgs.substring(indexOfEmailPrefix1, commandArgs.length()).trim().replace("e/",
                                "");
                    } else {
                        output2 = commandArgs.substring(indexOfEmailPrefix1, indexOfPhonePrefix1).trim().replace("e/",
                                "");
                    }
                    person2[2] = output2;
                    final String[] decodedPerson = person2;
                    output = decodedPerson[0].matches("(\\w|\\s)+") && decodedPerson[1].matches("\\d+")
                            && decodedPerson[2].matches("\\S+@\\S+\\.\\S+") ? Optional.of(decodedPerson)
                                    : Optional.empty();
                }
                final Optional<String[]> decodeResult = output;
                if (!decodeResult.isPresent()) {
                    feedback = String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s",
                            "add",
                            String.format("%1$s: %2$s", "add", "Adds a person to the address book.")
                                    + System.lineSeparator() + "|| "
                                    + String.format("\tParameters: %1$s",
                                            "NAME " + "p/" + "PHONE_NUMBER " + "e/" + "EMAIL")
                                    + System.lineSeparator() + "|| "
                                    + String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com")
                                    + System.lineSeparator() + "|| ");
                } else {
                    final String[] personToAdd = decodeResult.get();
                    ALL_PERSONS.add(personToAdd);
                    final ArrayList<String> encoded = new ArrayList<>();
                    for (String[] person1 : ALL_PERSONS) {
                        encoded.add(String.format("%1$s " + "p/" + "%2$s " + "e/" + "%3$s", person1[0], person1[1],
                                person1[2]));
                    }
                    final ArrayList<String> linesToWrite = encoded;
                    try {
                        Files.write(Paths.get(storageFilePath), linesToWrite);
                    } catch (IOException ioe) {
                        String[] message6 = {
                                String.format("Unexpected error: unable to write to file: %1$s", storageFilePath) };
                        for (String m6 : message6) {
                            System.out.println("|| " + m6);
                        }
                        String[] message11 = { "Exiting Address Book... Good bye!",
                                "===================================================",
                                "===================================================" };
                        for (String m11 : message11) {
                            System.out.println("|| " + m11);
                        }
                        System.exit(0);
                    }
                    feedback = String.format("New person added: %1$s, Phone: %2$s, Email: %3$s", personToAdd[0],
                            personToAdd[1], personToAdd[2]);
                }
                break;
            case "find":
                final Set<String> keywords = new HashSet<>(
                        new ArrayList<String>(Arrays.asList(commandArgs.trim().trim().split("\\s+"))));
                final ArrayList<String[]> matchedPersons = new ArrayList<>();
                for (String[] person : ALL_PERSONS) {
                    final Set<String> wordsInName = new HashSet<>(
                            new ArrayList<String>(Arrays.asList(person[0].trim().split("\\s+"))));
                    if (!Collections.disjoint(wordsInName, keywords)) {
                        matchedPersons.add(person);
                    }
                }
                final ArrayList<String[]> personsFound = matchedPersons;
                final StringBuilder messageAccumulator = new StringBuilder();
                for (int i = 0; i < personsFound.size(); i++) {
                    final String[] person1 = personsFound.get(i);
                    final int displayIndex = i + 1;
                    messageAccumulator.append('\t')
                            .append(String.format("%1$d. ", displayIndex) + String.format(
                                    "%1$s  Phone Number: %2$s  Email: %3$s", person1[0], person1[1], person1[2]))
                            .append(System.lineSeparator() + "|| ");
                }
                String listAsString1 = messageAccumulator.toString();
                String[] message5 = { listAsString1 };
                for (String m5 : message5) {
                    System.out.println("|| " + m5);
                }
                latestPersonListingView = new ArrayList<>(personsFound);
                feedback = String.format("%1$d persons found!", personsFound.size());
                break;
            case "list":
                ArrayList<String[]> toBeDisplayed = ALL_PERSONS;
                final StringBuilder messageAccumulator1 = new StringBuilder();
                for (int i = 0; i < toBeDisplayed.size(); i++) {
                    final String[] person1 = toBeDisplayed.get(i);
                    final int displayIndex = i + 1;
                    messageAccumulator1.append('\t')
                            .append(String.format("%1$d. ", displayIndex) + String.format(
                                    "%1$s  Phone Number: %2$s  Email: %3$s", person1[0], person1[1], person1[2]))
                            .append(System.lineSeparator() + "|| ");
                }
                String listAsString = messageAccumulator1.toString();
                String[] message4 = { listAsString };
                for (String m4 : message4) {
                    System.out.println("|| " + m4);
                }
                latestPersonListingView = new ArrayList<>(toBeDisplayed);
                feedback = String.format("%1$d persons found!", toBeDisplayed.size());
                break;
            case "delete":
                boolean isValid = true;
                try {
                    final int extractedIndex = Integer.parseInt(commandArgs.trim());
                    isValid = extractedIndex >= 1;
                } catch (NumberFormatException nfe) {
                    isValid = false;
                }
                if (!isValid) {
                    feedback = String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s",
                            "delete",
                            String.format("%1$s: %2$s", "delete",
                                    "Deletes a person identified by the index number used in "
                                            + "the last find/list call.")
                                    + System.lineSeparator() + "|| " + String.format("\tParameters: %1$s", "INDEX")
                                    + System.lineSeparator() + "|| " + String.format("\tExample: %1$s", "delete" + " 1")
                                    + System.lineSeparator() + "|| ");
                } else {
                    final int targetVisibleIndex = Integer.parseInt(commandArgs.trim());
                    if (!(targetVisibleIndex >= 1 && targetVisibleIndex < latestPersonListingView.size() + 1)) {
                        feedback = "The person index provided is invalid";
                    } else {
                        final String[] targetInModel = latestPersonListingView.get(targetVisibleIndex - 1);
                        final boolean changed = ALL_PERSONS.remove(targetInModel);
                        if (changed) {
                            final ArrayList<String> encoded = new ArrayList<>();
                            for (String[] person1 : ALL_PERSONS) {
                                encoded.add(String.format("%1$s " + "p/" + "%2$s " + "e/" + "%3$s", person1[0],
                                        person1[1], person1[2]));
                            }
                            final ArrayList<String> linesToWrite = encoded;
                            try {
                                Files.write(Paths.get(storageFilePath), linesToWrite);
                            } catch (IOException ioe) {
                                String[] message6 = { String.format("Unexpected error: unable to write to file: %1$s",
                                        storageFilePath) };
                                for (String m6 : message6) {
                                    System.out.println("|| " + m6);
                                }
                                String[] message11 = { "Exiting Address Book... Good bye!",
                                        "===================================================",
                                        "===================================================" };
                                for (String m11 : message11) {
                                    System.out.println("|| " + m11);
                                }
                                System.exit(0);
                            }
                        }
                        feedback = changed
                                ? String.format("Deleted Person: %1$s",
                                        String.format("%1$s  Phone Number: %2$s  Email: %3$s", targetInModel[0],
                                                targetInModel[1], targetInModel[2]))
                                : "Person could not be found in address book";
                    }
                }
                break;
            case "clear":
                ALL_PERSONS.clear();
                final ArrayList<String> encoded = new ArrayList<>();
                for (String[] person1 : ALL_PERSONS) {
                    encoded.add(String.format("%1$s " + "p/" + "%2$s " + "e/" + "%3$s", person1[0], person1[1],
                            person1[2]));
                }
                final ArrayList<String> linesToWrite = encoded;
                try {
                    Files.write(Paths.get(storageFilePath), linesToWrite);
                } catch (IOException ioe) {
                    String[] message6 = {
                            String.format("Unexpected error: unable to write to file: %1$s", storageFilePath) };
                    for (String m6 : message6) {
                        System.out.println("|| " + m6);
                    }
                    String[] message11 = { "Exiting Address Book... Good bye!",
                            "===================================================",
                            "===================================================" };
                    for (String m11 : message11) {
                        System.out.println("|| " + m11);
                    }
                    System.exit(0);
                }
                feedback = "Address book has been cleared!";
                break;
            case "help":
                feedback = String.format("%1$s: %2$s", "add", "Adds a person to the address book.")
                        + System.lineSeparator() + "|| "
                        + String.format("\tParameters: %1$s", "NAME " + "p/" + "PHONE_NUMBER " + "e/" + "EMAIL")
                        + System.lineSeparator() + "|| "
                        + String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com")
                        + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
                        + String.format("%1$s: %2$s", "find",
                                "Finds all persons whose names contain any of the specified "
                                        + "keywords (case-sensitive) and displays them as a list with index numbers.")
                        + System.lineSeparator() + "|| "
                        + String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + System.lineSeparator()
                        + "|| " + String.format("\tExample: %1$s", "find" + " alice bob charlie")
                        + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
                        + String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.")
                        + System.lineSeparator() + "|| " + String.format("\tExample: %1$s", "list")
                        + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
                        + String.format("%1$s: %2$s", "delete",
                                "Deletes a person identified by the index number used in " + "the last find/list call.")
                        + System.lineSeparator() + "|| " + String.format("\tParameters: %1$s", "INDEX")
                        + System.lineSeparator() + "|| " + String.format("\tExample: %1$s", "delete" + " 1")
                        + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
                        + String.format("%1$s: %2$s", "clear", "Clears address book permanently.")
                        + System.lineSeparator() + "|| " + String.format("\tExample: %1$s", "clear")
                        + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
                        + String.format("%1$s: %2$s", "exit", "Exits the program.")
                        + String.format("\tExample: %1$s", "exit") + System.lineSeparator() + "|| "
                        + String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
                        + String.format("\tExample: %1$s", "help");
                break;
            case "exit":
                String[] message3 = { "Exiting Address Book... Good bye!",
                        "===================================================",
                        "===================================================" };
                for (String m3 : message3) {
                    System.out.println("|| " + m3);
                }
                System.exit(0);
            default:
                feedback = String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s",
                        commandType,
                        String.format("%1$s: %2$s", "add", "Adds a person to the address book.")
                                + System.lineSeparator() + "|| "
                                + String.format("\tParameters: %1$s", "NAME " + "p/" + "PHONE_NUMBER " + "e/" + "EMAIL")
                                + System.lineSeparator() + "|| "
                                + String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com")
                                + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
                                + String.format("%1$s: %2$s", "find",
                                        "Finds all persons whose names contain any of the specified "
                                                + "keywords (case-sensitive) and displays them as a list with index numbers.")
                                + System.lineSeparator() + "|| "
                                + String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]")
                                + System.lineSeparator() + "|| "
                                + String.format("\tExample: %1$s", "find" + " alice bob charlie")
                                + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
                                + String.format("%1$s: %2$s", "list",
                                        "Displays all persons as a list with index numbers.")
                                + System.lineSeparator() + "|| " + String.format("\tExample: %1$s", "list")
                                + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
                                + String.format("%1$s: %2$s", "delete",
                                        "Deletes a person identified by the index number used in "
                                                + "the last find/list call.")
                                + System.lineSeparator() + "|| " + String.format("\tParameters: %1$s", "INDEX")
                                + System.lineSeparator() + "|| " + String.format("\tExample: %1$s", "delete" + " 1")
                                + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
                                + String.format("%1$s: %2$s", "clear", "Clears address book permanently.")
                                + System.lineSeparator() + "|| " + String.format("\tExample: %1$s", "clear")
                                + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
                                + String.format("%1$s: %2$s", "exit", "Exits the program.")
                                + String.format("\tExample: %1$s", "exit") + System.lineSeparator() + "|| "
                                + String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
                                + String.format("\tExample: %1$s", "help"));
            }
            String[] message2 = { feedback, "===================================================" };
            for (String m2 : message2) {
                System.out.println("|| " + m2);
            }
        }
    }
}