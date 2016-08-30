package seedu.addressbook;
/* ==============NOTE TO STUDENTS======================================
 * This class is written in a procedural fashion (i.e. not Object-Oriented)
 * Yes, it is possible to write non-OO code using an OO language.
 * ====================================================================
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

/* ==============NOTE TO STUDENTS======================================
 * This class header comment below is brief because details of how to
 * use this class are documented elsewhere.
 * ====================================================================
 */

/**
 * This class is used to maintain a list of person data which are saved in a
 * text file.
 **/
public class AddressBookT2A4 {

    private enum PersonProperty {
        NAME, PHONE, EMAIL
    }

    /**
     * A platform independent line separator.
     */
    // private static final String LS" = System.lineSeparator() + "|| ";

    /*
     * ==============NOTE TO STUDENTS======================================
     * These messages shown to the user are defined in one place for convenient
     * editing and proof reading. Such messages are considered part of the UI
     * and may be subjected to review by UI experts or technical writers. Note
     * that Some of the strings below include '%1$s' etc to mark the locations
     * at which java String.format(...) method can insert values.
     * ====================================================================
     */

    /*
     * We use a String array to store details of a single person. The constants
     * given below are the indexes for the different data elements of a person
     * used by the internal HashMap<PersonProperty, String> storage format. For
     * example, a person's name is stored as the 0th element in the array.
     */
    /*
     * private static final int PERSON_DATA_INDEX_NAME = 0; private static final
     * int PERSON_DATA_INDEX_PHONE = 1; private static final int
     * PERSON_DATA_INDEX_EMAIL = 2;
     */

    /**
     * The number of data elements for a single person.
     */

    /**
     * Offset required to convert between 1-indexing and 0-indexing.COMMAND_
     */
    private static final int DISPLAYED_INDEX_OFFSET = 1;

    /**
     * If the first non-whitespace character in a user's input line is this,
     * that line will be ignored.
     */
    private static final char INPUT_COMMENT_MARKER = '#';

    /*
     * This variable is declared for the whole class (instead of declaring it
     * inside the readUserCommand() method to facilitate automated testing using
     * the I/O redirection technique. If not, only the first line of the input
     * text file will be processed.
     */

    /*
     * ==============NOTE TO
     * STUDENTS=================================================================
     * ===== Note that the type of the variable below can also be declared as
     * List<HashMap<PersonProperty, String>>, as follows: private static final
     * List<HashMap<PersonProperty, String>> ALL_PERSONS = new ArrayList<>()
     * That is because List is an interface implemented by the ArrayList class.
     * In this code we use ArrayList instead because we wanted to to stay away
     * from advanced concepts such as interface inheritance.
     * =========================================================================
     * ===========================
     */
    /**
     * List of all persons in the address book.
     */
    private static final ArrayList<HashMap<PersonProperty, String>> ALL_PERSONS = new ArrayList<>();

    /**
     * Stores the most recent list of persons shown to the user as a result of a
     * user command. This is a subset of the full list. Deleting persons in the
     * pull list does not delete those persons from this list.
     */
    private static ArrayList<HashMap<PersonProperty, String>> latestPersonListingView = ALL_PERSONS; // initial
                                                                                                     // view
                                                                                                     // is
                                                                                                     // of
                                                                                                     // all

    /**
     * The path to the file used for storing person data.
     */
    private static String storageFilePath;

    /*
     * ==============NOTE TO STUDENTS======================================
     * Notice how this method solves the whole problem at a very high level. We
     * can understand the high-level logic of the program by reading this method
     * alone.
     * ====================================================================
     */
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("|| " + "===================================================");
        System.out.println("|| " + "===================================================");
        System.out.println("|| " + "AddessBook Level 1 - Version 1.0");
        System.out.println("|| " + "Welcome to your Address Book!");
        System.out.println("|| " + "===================================================");

        if (args.length >= 2) {
            System.out.println("|| " + "Too many parameters! Correct program argument format:" + "\r\n|| " + "\tjava AddressBook" + "\r\n|| " + "\tjava AddressBook [custom storage file path]");
            System.out.println("|| " + "Exiting Address Book... Good bye!");
            System.out.println("|| " + "===================================================");
            System.out.println("|| " + "===================================================");
            System.exit(0);
        }

        if (args.length == 1) {
            if (!args[0].endsWith(".txt")) {
                System.out.println("|| " + String.format("The given file name [%1$s] is not a valid file name!", args[0]));
                System.out.println("|| " + "Exiting Address Book... Good bye!");
                System.out.println("|| " + "===================================================");
                System.out.println("|| " + "===================================================");
                System.exit(0);
            }

            storageFilePath = args[0];
            createFileIfMissing(args[0]);
            ;
        }

        if (args.length == 0) {
            System.out.println("|| " + "Using default storage file : " + "addressbook.txt");
            storageFilePath = "addressbook.txt";
            createFileIfMissing(storageFilePath);
        }

        ALL_PERSONS.clear();
        ALL_PERSONS.addAll(loadPersonsFromFile(storageFilePath));

        while (true) {
            System.out.print("|| " + "Enter command: ");
            String userCommand = scanner.nextLine();
            // silently consume all blank and comment lines
            while (userCommand.trim().isEmpty() || userCommand.trim().charAt(0) == INPUT_COMMENT_MARKER) {
                userCommand = scanner.nextLine();
            }

            System.out.println("|| " + "[Command entered:" + userCommand + "]");

            String feedback = "";
            final String[] split = userCommand.trim().split("\\s+", 2);

            final String[] commandTypeAndParams = split.length == 2 ? split : new String[] { split[0], "" };
            final String commandType = commandTypeAndParams[0];
            final String commandArgs = commandTypeAndParams[1];

            if (commandType.equals("add")) {
                // try decoding a person from the raw args
                final Optional<HashMap<PersonProperty, String>> decodeResult = decodePersonFromString(commandArgs);

                // checks if args are valid (decode result will not be present
                // if the person is invalid)
                if (!decodeResult.isPresent()) {
                    feedback = String.format("Invalid command format: %1$s " + "\r\n|| " + "%2$s", "add",
                            String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + "\r\n|| "
                                    + String.format("\tParameters: %1$s", "NAME " + "p/" + "PHONE_NUMBER " + "e/" + "EMAIL") + "\r\n|| "
                                    + String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + "\r\n|| ");
                } else {
                    // add the person as specified
                    final HashMap<PersonProperty, String> personToAdd = decodeResult.get();
                    ALL_PERSONS.add(personToAdd);
                    final ArrayList<String> linesToWrite = encodePersonsToStrings(ALL_PERSONS);
                    try {
                        Files.write(Paths.get(storageFilePath), linesToWrite);
                    } catch (IOException ioe) {
                        System.out.println("|| " + String.format("Unexpected error: unable to write to file: %1$s", storageFilePath));
                        System.out.println("|| " + "Exiting Address Book... Good bye!");
                        System.out.println("|| " + "===================================================");
                        System.out.println("|| " + "===================================================");
                        System.exit(0);
                    }
                    feedback = String.format("New person added: %1$s, Phone: %2$s, Email: %3$s", personToAdd.get(PersonProperty.NAME),
                            personToAdd.get(PersonProperty.PHONE), personToAdd.get(PersonProperty.EMAIL));
                }
            } else if (commandType.equals("find")) {
                final Set<String> keywords = new HashSet<>(
                        (ArrayList<String>) new ArrayList(Arrays.asList(commandArgs.trim().trim().split("\\s+"))));
                final ArrayList<HashMap<PersonProperty, String>> matchedPersons = new ArrayList<>();
                for (HashMap<PersonProperty, String> person : ALL_PERSONS) {
                    final Set<String> wordsInName = new HashSet<>((ArrayList<String>) new ArrayList(
                            Arrays.asList(person.get(PersonProperty.NAME).trim().split("\\s+"))));
                    if (!Collections.disjoint(wordsInName, keywords)) {
                        matchedPersons.add(person);
                    }
                }
                final ArrayList<HashMap<PersonProperty, String>> personsFound = matchedPersons;
                final StringBuilder messageAccumulator = new StringBuilder();
                for (int i = 0; i < personsFound.size(); i++) {
                    final HashMap<PersonProperty, String> person1 = personsFound.get(i);
                    final int displayIndex = i + DISPLAYED_INDEX_OFFSET;
                    messageAccumulator.append('\t')
                            .append(String.format("%1$d. ", displayIndex)
                                    + String.format("%1$s  Phone Number: %2$s  Email: %3$s", person1.get(PersonProperty.NAME),
                                            person1.get(PersonProperty.PHONE), person1.get(PersonProperty.EMAIL)))
                            .append("\r\n|| ");
                }
                String listAsString = messageAccumulator.toString();
                System.out.println("|| " + listAsString);
                // clone to insulate from future changes to arg list
                latestPersonListingView = new ArrayList<>(personsFound);
                feedback = String.format("%1$d persons found!", personsFound.size());
            } else if (commandType.equals("list")) {
                ArrayList<HashMap<PersonProperty, String>> toBeDisplayed = ALL_PERSONS;
                final StringBuilder messageAccumulator = new StringBuilder();
                for (int i = 0; i < toBeDisplayed.size(); i++) {
                    final HashMap<PersonProperty, String> person = toBeDisplayed.get(i);
                    final int displayIndex = i + DISPLAYED_INDEX_OFFSET;
                    messageAccumulator.append('\t')
                            .append(String.format("%1$d. ", displayIndex)
                                    + String.format("%1$s  Phone Number: %2$s  Email: %3$s", person.get(PersonProperty.NAME),
                                            person.get(PersonProperty.PHONE), person.get(PersonProperty.EMAIL)))
                            .append("\r\n|| ");
                }
                String listAsString = messageAccumulator.toString();
                System.out.println("|| " + listAsString);
                // clone to insulate from future changes to arg list
                latestPersonListingView = new ArrayList<>(toBeDisplayed);
                feedback = String.format("%1$d persons found!", toBeDisplayed.size());
            } else if (commandType.equals("delete")) {
                feedback = executeDeletePerson(commandArgs);
            } else if (commandType.equals("clear")) {
                ALL_PERSONS.clear();
                final ArrayList<String> linesToWrite = encodePersonsToStrings(ALL_PERSONS);
                try {
                    Files.write(Paths.get(storageFilePath), linesToWrite);
                } catch (IOException ioe) {
                    System.out.println("|| " + String.format("Unexpected error: unable to write to file: %1$s", storageFilePath));
                    System.out.println("|| " + "Exiting Address Book... Good bye!");
                    System.out.println("|| " + "===================================================");
                    System.out.println("|| " + "===================================================");
                    System.exit(0);
                }
                feedback = "Address book has been cleared!";
            } else if (commandType.equals("help")) {
                feedback = String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + "\r\n|| "
                        + String.format("\tParameters: %1$s", "NAME " + "p/" + "PHONE_NUMBER " + "e/" + "EMAIL") + "\r\n|| "
                        + String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + "\r\n|| " + "\r\n|| "
                        + String.format("%1$s: %2$s", "find", "Finds all persons whose names contain any of the specified " + "keywords (case-sensitive) and displays them as a list with index numbers.") + "\r\n|| "
                        + String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + "\r\n|| "
                        + String.format("\tExample: %1$s", "find" + " alice bob charlie") + "\r\n|| " + "\r\n|| "
                        + String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + "\r\n|| "
                        + String.format("\tExample: %1$s", "list") + "\r\n|| " + "\r\n|| "
                        + String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in " + "the last find/list call.") + "\r\n|| "
                        + String.format("\tParameters: %1$s", "INDEX") + "\r\n|| "
                        + String.format("\tExample: %1$s", "delete" + " 1") + "\r\n|| " + "\r\n|| "
                        + String.format("%1$s: %2$s", "clear", "Clears address book permanently.") + "\r\n|| "
                        + String.format("\tExample: %1$s", "clear") + "\r\n|| " + "\r\n|| "
                        + String.format("%1$s: %2$s", "exit", "Exits the program.")
                        + String.format("\tExample: %1$s", "exit") + "\r\n|| "
                        + String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
                        + String.format("\tExample: %1$s", "help");
            } else if (commandType.equals("exit")) {
                System.out.println("|| " + "Exiting Address Book... Good bye!");
                System.out.println("|| " + "===================================================");
                System.out.println("|| " + "===================================================");
                System.exit(0);
            } else {
                feedback = String.format("Invalid command format: %1$s " + "\r\n|| " + "%2$s", commandType,
                        String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + "\r\n|| "
                                + String.format("\tParameters: %1$s", "NAME " + "p/" + "PHONE_NUMBER " + "e/" + "EMAIL") + "\r\n|| "
                                + String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + "\r\n|| " + "\r\n|| "
                                + String.format("%1$s: %2$s", "find", "Finds all persons whose names contain any of the specified " + "keywords (case-sensitive) and displays them as a list with index numbers.") + "\r\n|| "
                                + String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + "\r\n|| "
                                + String.format("\tExample: %1$s", "find" + " alice bob charlie") + "\r\n|| " + "\r\n|| "
                                + String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + "\r\n|| "
                                + String.format("\tExample: %1$s", "list") + "\r\n|| " + "\r\n|| "
                                + String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in " + "the last find/list call.") + "\r\n|| "
                                + String.format("\tParameters: %1$s", "INDEX") + "\r\n|| "
                                + String.format("\tExample: %1$s", "delete" + " 1") + "\r\n|| " + "\r\n|| "
                                + String.format("%1$s: %2$s", "clear", "Clears address book permanently.") + "\r\n|| "
                                + String.format("\tExample: %1$s", "clear") + "\r\n|| " + "\r\n|| "
                                + String.format("%1$s: %2$s", "exit", "Exits the program.")
                                + String.format("\tExample: %1$s", "exit") + "\r\n|| "
                                + String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
                                + String.format("\tExample: %1$s", "help"));
            }

            System.out.println("|| " + feedback);
            System.out.println("|| " + "===================================================");
        }
    }

    /*
     * ==============NOTE TO STUDENTS====================================== The
     * method header comment can be omitted if the method is trivial and the
     * header comment is going to be almost identical to the method signature
     * anyway.
     * ====================================================================
     */

    /*
     * ==============NOTE TO STUDENTS======================================
     * Parameter description can be omitted from the method header comment if
     * the parameter name is self-explanatory. In the method below, '@param
     * userInput' comment has been omitted.
     * ====================================================================
     */

    /*
     * ==============NOTE TO STUDENTS==========================================
     * If the reader wants a deeper understanding of the solution, she can go to
     * the next level of abstraction by reading the methods (given below) that
     * is referenced by the method above.
     * ====================================================================
     */

    /**
     * Processes the program main method run arguments. If a valid storage file
     * is specified, sets up that file for storage. Otherwise sets up the
     * default file for storage.
     *
     * @param args
     *            full program arguments passed to application main method
     */

    /*
     * =========================================== COMMAND LOGIC
     * ===========================================
     */

    /**
     * Executes the command as specified by the {@code userInputString}
     *
     * @param userInputString
     *            raw input from user
     * @return feedback about how the command was executed
     */

    /**
     * Adds a person (specified by the command args) to the address book. The
     * entire command arguments string is treated as a string representation of
     * the person to add.
     *
     * @param commandArgs
     *            full command args string from the user
     * @return feedback display message for the operation result
     */

    /**
     * Deletes person identified using last displayed index.
     *
     * @param commandArgs
     *            full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String executeDeletePerson(String commandArgs) {
        if (!isDeletePersonArgsValid(commandArgs)) {
            return String.format("Invalid command format: %1$s " + "\r\n|| " + "%2$s", "delete",
                    String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in " + "the last find/list call.") + "\r\n|| "
                            + String.format("\tParameters: %1$s", "INDEX") + "\r\n|| "
                            + String.format("\tExample: %1$s", "delete" + " 1") + "\r\n|| ");
        }
        final int targetVisibleIndex = Integer.parseInt(commandArgs.trim());
        if (!(targetVisibleIndex >= DISPLAYED_INDEX_OFFSET
                && targetVisibleIndex < latestPersonListingView.size() + DISPLAYED_INDEX_OFFSET)) {
            return "The person index provided is invalid";
        }
        final HashMap<PersonProperty, String> targetInModel = latestPersonListingView
                .get(targetVisibleIndex - DISPLAYED_INDEX_OFFSET);
        if (deletePersonFromAddressBook(targetInModel)) {
            return String.format("Deleted Person: %1$s",
                    String.format("%1$s  Phone Number: %2$s  Email: %3$s", targetInModel.get(PersonProperty.NAME),
                            targetInModel.get(PersonProperty.PHONE), targetInModel.get(PersonProperty.EMAIL))); // success
        } else {
            return "Person could not be found in address book"; // not found
        }
    }

    /**
     * Checks validity of delete person argument string's format.
     *
     * @param rawArgs
     *            raw command args string for the delete person command
     * @return whether the input args string is valid
     */
    private static boolean isDeletePersonArgsValid(String rawArgs) {
        try {
            final int extractedIndex = Integer.parseInt(rawArgs.trim()); // use
                                                                         // standard
                                                                         // libraries
                                                                         // to
                                                                         // parse
            return extractedIndex >= DISPLAYED_INDEX_OFFSET;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /*
     * =========================================== STORAGE LOGIC
     * ===========================================
     */

    /**
     * Creates storage file if it does not exist. Shows feedback to user.
     *
     * @param filePath
     *            file to create if not present
     */
    private static void createFileIfMissing(String filePath) {
        final File storageFile = new File(filePath);
        if (storageFile.exists()) {
            return;
        }

        System.out.println("|| " + String.format("Storage file missing: %1$s", filePath));

        try {
            storageFile.createNewFile();
            System.out.println("|| " + String.format("Created new empty storage file: %1$s", filePath));
        } catch (IOException ioe) {
            System.out.println("|| " + String.format("Error: unable to create file: %1$s", filePath));
            System.out.println("|| " + "Exiting Address Book... Good bye!");
            System.out.println("|| " + "===================================================");
            System.out.println("|| " + "===================================================");
            System.exit(0);
        }
    }

    /**
     * Converts contents of a file into a list of persons. Shows error messages
     * and exits program if any errors in reading or decoding was encountered.
     *
     * @param filePath
     *            file to load from
     * @return the list of decoded persons
     */
    private static ArrayList<HashMap<PersonProperty, String>> loadPersonsFromFile(String filePath) {
        final Optional<ArrayList<HashMap<PersonProperty, String>>> successfullyDecoded = decodePersonsFromStrings(
                getLinesInFile(filePath));
        if (!successfullyDecoded.isPresent()) {
            System.out.println("|| " + "Storage file has invalid content");
            System.out.println("|| " + "Exiting Address Book... Good bye!");
            System.out.println("|| " + "===================================================");
            System.out.println("|| " + "===================================================");
            System.exit(0);
        }
        return successfullyDecoded.get();
    }

    /**
     * Gets all lines in the specified file as a list of strings. Line
     * separators are removed. Shows error messages and exits program if unable
     * to read from file.
     */
    private static ArrayList<String> getLinesInFile(String filePath) {
        ArrayList<String> lines = null;
        try {
            lines = new ArrayList(Files.readAllLines(Paths.get(filePath)));
        } catch (FileNotFoundException fnfe) {
            System.out.println("|| " + String.format("Storage file missing: %1$s", filePath));
            System.out.println("|| " + "Exiting Address Book... Good bye!");
            System.out.println("|| " + "===================================================");
            System.out.println("|| " + "===================================================");
            System.exit(0);
        } catch (IOException ioe) {
            System.out.println("|| " + String.format("Unexpected error: unable to read from file: %1$s", filePath));
            System.out.println("|| " + "Exiting Address Book... Good bye!");
            System.out.println("|| " + "===================================================");
            System.out.println("|| " + "===================================================");
            System.exit(0);
        }
        return lines;
    }

    /**
     * Deletes a person from the address book, target is identified by it's
     * absolute index in the full list. Saves changes to storage file.
     *
     * @param index
     *            absolute index of person to delete (index within
     *            {@link #ALL_PERSONS})
     */
    private static void deletePersonFromAddressBook(int index) {
        ALL_PERSONS.remove(index);
        final ArrayList<String> linesToWrite = encodePersonsToStrings(ALL_PERSONS);
        try {
            Files.write(Paths.get(storageFilePath), linesToWrite);
        } catch (IOException ioe) {
            System.out.println("|| " + String.format("Unexpected error: unable to write to file: %1$s", storageFilePath));
            System.out.println("|| " + "Exiting Address Book... Good bye!");
            System.out.println("|| " + "===================================================");
            System.out.println("|| " + "===================================================");
            System.exit(0);
        }
    }

    /**
     * Deletes the specified person from the addressbook if it is inside. Saves
     * any changes to storage file.
     *
     * @param exactPerson
     *            the actual person inside the address book (exactPerson == the
     *            person to delete in the full list)
     * @return true if the given person was found and deleted in the model
     */
    private static boolean deletePersonFromAddressBook(HashMap<PersonProperty, String> exactPerson) {
        final boolean changed = ALL_PERSONS.remove(exactPerson);
        if (changed) {
            final ArrayList<String> linesToWrite = encodePersonsToStrings(ALL_PERSONS);
            try {
                Files.write(Paths.get(storageFilePath), linesToWrite);
            } catch (IOException ioe) {
                System.out.println("|| " + String.format("Unexpected error: unable to write to file: %1$s", storageFilePath));
                System.out.println("|| " + "Exiting Address Book... Good bye!");
                System.out.println("|| " + "===================================================");
                System.out.println("|| " + "===================================================");
                System.exit(0);
            }
        }
        return changed;
    }

    /*
     * =========================================== PERSON METHODS
     * ===========================================
     */

    /**
     * Encodes list of persons into list of decodable and readable string
     * representations.
     *
     * @param persons
     *            to be encoded
     * @return encoded strings
     */
    private static ArrayList<String> encodePersonsToStrings(ArrayList<HashMap<PersonProperty, String>> persons) {
        final ArrayList<String> encoded = new ArrayList<>();
        for (HashMap<PersonProperty, String> person : persons) {
            encoded.add(String.format("%1$s " + "p/" + "%2$s " + "e/" + "%3$s", person.get(PersonProperty.NAME),
                    person.get(PersonProperty.PHONE), person.get(PersonProperty.EMAIL)));
        }
        return encoded;
    }

    /*
     * ==============NOTE TO STUDENTS====================================== Note
     * the use of Java's new 'Optional' feature to indicate that the return
     * value may not always be present.
     * ====================================================================
     */
    /**
     * Decodes a person from it's supposed string representation.
     *
     * @param encoded
     *            string to be decoded
     * @return if cannot decode: empty Optional else: Optional containing
     *         decoded person
     */
    private static Optional<HashMap<PersonProperty, String>> decodePersonFromString(String encoded) {
        // check that we can extract the parts of a person from the encoded
        // string
        if (!isPersonDataExtractableFrom(encoded)) {
            return Optional.empty();
        }
        final HashMap<PersonProperty, String> person = new HashMap<>();
        person.put(PersonProperty.NAME, extractNameFromPersonString(encoded));
        person.put(PersonProperty.PHONE, extractPhoneFromPersonString(encoded));
        person.put(PersonProperty.EMAIL, extractEmailFromPersonString(encoded));
        final HashMap<PersonProperty, String> decodedPerson = person;
        // check that the constructed person is valid
        return decodedPerson.get(PersonProperty.NAME).matches("(\\w|\\s)+")
                && decodedPerson.get(PersonProperty.PHONE).matches("\\d+")
                && decodedPerson.get(PersonProperty.EMAIL).matches("\\S+@\\S+\\.\\S+") ? Optional.of(decodedPerson)
                        : Optional.empty();
    }

    /**
     * Decode persons from a list of string representations.
     *
     * @param encodedPersons
     *            strings to be decoded
     * @return if cannot decode any: empty Optional else: Optional containing
     *         decoded persons
     */
    private static Optional<ArrayList<HashMap<PersonProperty, String>>> decodePersonsFromStrings(
            ArrayList<String> encodedPersons) {
        final ArrayList<HashMap<PersonProperty, String>> decodedPersons = new ArrayList<>();
        for (String encodedPerson : encodedPersons) {
            final Optional<HashMap<PersonProperty, String>> decodedPerson = decodePersonFromString(encodedPerson);
            if (!decodedPerson.isPresent()) {
                return Optional.empty();
            }
            decodedPersons.add(decodedPerson.get());
        }
        return Optional.of(decodedPersons);
    }

    /**
     * Checks whether person data (email, name, phone etc) can be extracted from
     * the argument string. Format is [name] p/[phone] e/[email], phone and
     * email positions can be swapped.
     *
     * @param personData
     *            person string representation
     * @return whether format of add command arguments allows parsing into
     *         individual arguments
     */
    private static boolean isPersonDataExtractableFrom(String personData) {
        final String matchAnyPersonDataPrefix = "p/" + '|' + "e/";
        final String[] splitArgs = personData.trim().split(matchAnyPersonDataPrefix);
        return splitArgs.length == 3 // 3 arguments
                && !splitArgs[0].isEmpty() // non-empty arguments
                && !splitArgs[1].isEmpty() && !splitArgs[2].isEmpty();
    }

    /**
     * Extracts substring representing person name from person string
     * representation
     *
     * @param encoded
     *            person string representation
     * @return name argument
     */
    private static String extractNameFromPersonString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf("p/");
        final int indexOfEmailPrefix = encoded.indexOf("e/");
        // name is leading substring up to first data prefix symbol
        int indexOfFirstPrefix = Math.min(indexOfEmailPrefix, indexOfPhonePrefix);
        return encoded.substring(0, indexOfFirstPrefix).trim();
    }

    /**
     * Extracts substring representing phone number from person string
     * representation
     *
     * @param encoded
     *            person string representation
     * @return phone number argument WITHOUT prefix
     */
    private static String extractPhoneFromPersonString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf("p/");
        final int indexOfEmailPrefix = encoded.indexOf("e/");

        // phone is last arg, target is from prefix to end of string
        if (indexOfPhonePrefix > indexOfEmailPrefix) {
            return encoded.substring(indexOfPhonePrefix, encoded.length()).trim().replace("p/", "");

            // phone is middle arg, target is from own prefix to next prefix
        } else {
            return encoded.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim().replace("p/",
                    "");
        }
    }

    /**
     * Extracts substring representing email from person string representation
     *
     * @param encoded
     *            person string representation
     * @return email argument WITHOUT prefix
     */
    private static String extractEmailFromPersonString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf("p/");
        final int indexOfEmailPrefix = encoded.indexOf("e/");

        // email is last arg, target is from prefix to end of string
        if (indexOfEmailPrefix > indexOfPhonePrefix) {
            return encoded.substring(indexOfEmailPrefix, encoded.length()).trim().replace("e/", "");

            // email is middle arg, target is from own prefix to next prefix
        } else {
            return encoded.substring(indexOfEmailPrefix, indexOfPhonePrefix).trim().replace("e/",
                    "");
        }
    }

}