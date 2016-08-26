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
import java.util.HashSet;
import java.util.HashMap;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

/* ==============NOTE TO STUDENTS======================================
 * This class header comment below is brief because details of how to
 * use this class are documented elsewhere.
 * ====================================================================
 */

/**
 * This class is used to maintain a list of person data which are saved
 * in a text file.
 **/
public class AddressBook {

    /**
     * Default file path used if the user doesn't provide the file name.
     */
    private static final String DEFAULT_STORAGE_FILEPATH = "addressbook.txt";

    /**
     * Version info of the program.
     */
    private static final String VERSION = "AddessBook Level 1 - Version 1.0";

    /**
     * A decorative prefix added to the beginning of lines printed by AddressBook
     */
    public static final String LINE_PREFIX = "|| ";

    /**
     * A platform independent line separator.
     */
    private static final String LS = System.lineSeparator() + LINE_PREFIX;

    /*
     * ==============NOTE TO STUDENTS======================================
     * These messages shown to the user are defined in one place for convenient
     * editing and proof reading. Such messages are considered part of the UI
     * and may be subjected to review by UI experts or technical writers. Note
     * that Some of the strings below include '%1$s' etc to mark the locations
     * at which java String.format(...) method can insert values.
     * ====================================================================
     */
    private static final String MESSAGE_ADDED = "New person added: %1$s, Phone: %2$s, Email: %3$s";
    private static final String MESSAGE_ADDRESSBOOK_CLEARED = "Address book has been cleared!";
    private static final String MESSAGE_COMMAND_HELP = "%1$s: %2$s";
    private static final String MESSAGE_COMMAND_HELP_PARAMETERS = "\tParameters: %1$s";
    private static final String MESSAGE_COMMAND_HELP_EXAMPLE = "\tExample: %1$s";
    private static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    private static final String MESSAGE_DISPLAY_PERSON_DATA = "%1$s  Phone Number: %2$s  Email: %3$s";
    private static final String MESSAGE_DISPLAY_LIST_ELEMENT_INDEX = "%1$d. ";
    private static final String MESSAGE_GOODBYE = "Exiting Address Book... Good bye!";
    private static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format: %1$s " + LS + "%2$s";
    private static final String MESSAGE_INVALID_FILE = "The given file name [%1$s] is not a valid file name!";
    private static final String MESSAGE_INVALID_PROGRAM_ARGS = "Too many parameters! Correct program argument format:"
                                                            + LS + "\tjava AddressBook"
                                                            + LS + "\tjava AddressBook [custom storage file path]";
    private static final String MESSAGE_INVALID_PERSON_DISPLAYED_INDEX = "The person index provided is invalid";
    private static final String MESSAGE_INVALID_STORAGE_FILE_CONTENT = "Storage file has invalid content";
    private static final String MESSAGE_PERSON_NOT_IN_ADDRESSBOOK = "Person could not be found in address book";
    private static final String MESSAGE_ERROR_CREATING_STORAGE_FILE = "Error: unable to create file: %1$s";
    private static final String MESSAGE_ERROR_MISSING_STORAGE_FILE = "Storage file missing: %1$s";
    private static final String MESSAGE_ERROR_READING_FROM_FILE = "Unexpected error: unable to read from file: %1$s";
    private static final String MESSAGE_ERROR_WRITING_TO_FILE = "Unexpected error: unable to write to file: %1$s";
    private static final String MESSAGE_PERSONS_FOUND_OVERVIEW = "%1$d persons found!";
    private static final String MESSAGE_STORAGE_FILE_CREATED = "Created new empty storage file: %1$s";
    private static final String MESSAGE_WELCOME = "Welcome to your Address Book!";
    private static final String MESSAGE_USING_DEFAULT_FILE = "Using default storage file : " + DEFAULT_STORAGE_FILEPATH;

    // These are the prefix strings to define the data type of a command parameter
    private static final String PERSON_DATA_PREFIX_PHONE = "p/";
    private static final String PERSON_DATA_PREFIX_EMAIL = "e/";

    private static final String PERSON_STRING_REPRESENTATION = "%1$s " // name
                                                            + PERSON_DATA_PREFIX_PHONE + "%2$s " // phone
                                                            + PERSON_DATA_PREFIX_EMAIL + "%3$s"; // email
    private static final String COMMAND_ADD_WORD = "add";
    private static final String COMMAND_ADD_DESC = "Adds a person to the address book.";
    private static final String COMMAND_ADD_PARAMETERS = "NAME "
                                                      + PERSON_DATA_PREFIX_PHONE + "PHONE_NUMBER "
                                                      + PERSON_DATA_PREFIX_EMAIL + "EMAIL";
    private static final String COMMAND_ADD_EXAMPLE = COMMAND_ADD_WORD + " John Doe p/98765432 e/johnd@gmail.com";

    private static final String COMMAND_FIND_WORD = "find";
    private static final String COMMAND_FIND_DESC = "Finds all persons whose names contain any of the specified "
                                        + "keywords (case-sensitive) and displays them as a list with index numbers.";
    private static final String COMMAND_FIND_PARAMETERS = "KEYWORD [MORE_KEYWORDS]";
    private static final String COMMAND_FIND_EXAMPLE = COMMAND_FIND_WORD + " alice bob charlie";

    private static final String COMMAND_LIST_WORD = "list";
    private static final String COMMAND_LIST_DESC = "Displays all persons as a list with index numbers.";
    private static final String COMMAND_LIST_EXAMPLE = COMMAND_LIST_WORD;

    private static final String COMMAND_DELETE_WORD = "delete";
    private static final String COMMAND_DELETE_DESC = "Deletes a person identified by the index number used in "
                                                    + "the last find/list call.";
    private static final String COMMAND_DELETE_PARAMETER = "INDEX";
    private static final String COMMAND_DELETE_EXAMPLE = COMMAND_DELETE_WORD + " 1";

    private static final String COMMAND_CLEAR_WORD = "clear";
    private static final String COMMAND_CLEAR_DESC = "Clears address book permanently.";
    private static final String COMMAND_CLEAR_EXAMPLE = COMMAND_CLEAR_WORD;

    private static final String COMMAND_HELP_WORD = "help";
    private static final String COMMAND_HELP_DESC = "Shows program usage instructions.";
    private static final String COMMAND_HELP_EXAMPLE = COMMAND_HELP_WORD;

    private static final String COMMAND_EXIT_WORD = "exit";
    private static final String COMMAND_EXIT_DESC = "Exits the program.";
    private static final String COMMAND_EXIT_EXAMPLE = COMMAND_EXIT_WORD;

    private static final String DIVIDER = "===================================================";

    /**
     * Define PersonProperty
     * <li>{@link #NAME} Name of person</li>
     * <li>{@link #EMAIL} Email of person</li>
     * <li>{@link #PHONE} Phone of person</li>
     */
    private enum PersonProperty {NAME,EMAIL,PHONE};
    
    /**
     * Offset required to convert between 1-indexing and 0-indexing.COMMAND_
     */
    private static final int DISPLAYED_INDEX_OFFSET = 1;



    /**
     * If the first non-whitespace character in a user's input line is this, that line will be ignored.
     */
    private static final char INPUT_COMMENT_MARKER = '#';

    /*
     * This variable is declared for the whole class (instead of declaring it
     * inside the readUserCommand() method to facilitate automated testing using
     * the I/O redirection technique. If not, only the first line of the input
     * text file will be processed.
     */
    private static final Scanner SCANNER = new Scanner(System.in);
    /*
     * ==============NOTE TO STUDENTS======================================================================
     * Note that the type of the variable below can also be declared as List<String[]>, as follows:
     *    private static final List<String[]> ALL_PERSONS = new ArrayList<>()
     * That is because List is an interface implemented by the ArrayList class.
     * In this code we use ArrayList instead because we wanted to to stay away from advanced concepts
     * such as interface inheritance.
     * ====================================================================================================
     */
    /**
     * List of all persons in the address book.
     */
    private static final ArrayList<HashMap<PersonProperty,String>> ALL_PERSONS = new ArrayList<>();


    /**
     * Stores the most recent list of persons shown to the user as a result of a user command.
     * This is a subset of the full list. Deleting persons in the pull list does not delete
     * those persons from this list.
     */
    private static ArrayList<HashMap<PersonProperty,String>> latestPersonListingView = ALL_PERSONS; // initial view is of all

    /**
     * The path to the file used for storing person data.
     */
    private static String storageFilePath;

    /*
     * ==============NOTE TO STUDENTS======================================
     * Notice how this method solves the whole problem at a very high level.
     * We can understand the high-level logic of the program by reading this
     * method alone.
     * ====================================================================
     */
    public static void main(String[] args) {
        System.out.println(LINE_PREFIX + DIVIDER);
		System.out.println(LINE_PREFIX + DIVIDER);
		System.out.println(LINE_PREFIX + VERSION);
		System.out.println(LINE_PREFIX + MESSAGE_WELCOME);
		System.out.println(LINE_PREFIX + DIVIDER);
        if (args.length >= 2) {
		    System.out.println(LINE_PREFIX + MESSAGE_INVALID_PROGRAM_ARGS);
		    System.out.println(LINE_PREFIX + MESSAGE_GOODBYE);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.exit(0);
		}
		
		if (args.length == 1) {
		    String filePath = args[0];
			if (!filePath.endsWith(".txt")) {
			    System.out.println(LINE_PREFIX + String.format(MESSAGE_INVALID_FILE, filePath));
			    System.out.println(LINE_PREFIX + MESSAGE_GOODBYE);
				System.out.println(LINE_PREFIX + DIVIDER);
				System.out.println(LINE_PREFIX + DIVIDER);
				System.exit(0);
			}
			
			storageFilePath = filePath;
			createFileIfMissing(filePath);
		}
		
		if(args.length == 0) {
		    System.out.println(LINE_PREFIX + MESSAGE_USING_DEFAULT_FILE);
			storageFilePath = DEFAULT_STORAGE_FILEPATH;
			createFileIfMissing(storageFilePath);
		}
        ALL_PERSONS.clear();
		ALL_PERSONS.addAll(loadPersonsFromFile(storageFilePath));
        while (true) {
            System.out.print(LINE_PREFIX + "Enter command: ");
			String inputLine = SCANNER.nextLine();
			// silently consume all blank and comment lines
			while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == INPUT_COMMENT_MARKER) {
			    inputLine = SCANNER.nextLine();
			}
			String userCommand = inputLine;
            System.out.println(LINE_PREFIX + "[Command entered:" + userCommand + "]");
            String feedback = executeCommand(userCommand);
            System.out.println(LINE_PREFIX + feedback);
			System.out.println(LINE_PREFIX + DIVIDER);
        }
    }

    

    /*
     * ==============NOTE TO STUDENTS==========================================
     * If the reader wants a deeper understanding of the solution, she can go
     * to the next level of abstraction by reading the methods (given below)
     * that is referenced by the method above.
     * ====================================================================
     */

    

    

    


    /*
     * ===========================================
     *           COMMAND LOGIC
     * ===========================================
     */

    /**
     * Executes the command as specified by the {@code userInputString}
     *
     * @param userInputString  raw input from user
     * @return  feedback about how the command was executed
     */
    public static String executeCommand(String userInputString) {
        final String[] split =  userInputString.trim().split("\\s+", 2);
		final String[] commandTypeAndParams = split.length == 2 ? split : new String[] { split[0] , "" };
        final String commandType = commandTypeAndParams[0];
        final String commandArgs = commandTypeAndParams[1];
        switch (commandType) {
        case COMMAND_ADD_WORD:
            return executeAddPerson(commandArgs);
        case COMMAND_FIND_WORD:
            return executeFindPersons(commandArgs);
        case COMMAND_LIST_WORD:
            return executeListAllPersonsInAddressBook();
        case COMMAND_DELETE_WORD:
            return executeDeletePerson(commandArgs);
        case COMMAND_CLEAR_WORD:
            ALL_PERSONS.clear();
			final ArrayList<String> linesToWrite = encodePersonsToStrings(ALL_PERSONS);
			try {
			    Files.write(Paths.get(storageFilePath), linesToWrite);
			} catch (IOException ioe) {
			    System.out.println(LINE_PREFIX + String.format(MESSAGE_ERROR_WRITING_TO_FILE, storageFilePath));
			    System.out.println(LINE_PREFIX + MESSAGE_GOODBYE);
				System.out.println(LINE_PREFIX + DIVIDER);
				System.out.println(LINE_PREFIX + DIVIDER);
				System.exit(0);
			}
			return MESSAGE_ADDRESSBOOK_CLEARED;
        case COMMAND_HELP_WORD:
            return String.format(MESSAGE_COMMAND_HELP, COMMAND_ADD_WORD, COMMAND_ADD_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_ADD_PARAMETERS) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_ADD_EXAMPLE) + LS + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_FIND_WORD, COMMAND_FIND_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_FIND_PARAMETERS) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_FIND_EXAMPLE) + LS + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_LIST_WORD, COMMAND_LIST_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_LIST_EXAMPLE) + LS + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_DELETE_WORD, COMMAND_DELETE_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_DELETE_PARAMETER) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_DELETE_EXAMPLE) + LS + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_CLEAR_WORD, COMMAND_CLEAR_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_CLEAR_EXAMPLE) + LS + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_EXIT_WORD, COMMAND_EXIT_DESC)
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_EXIT_EXAMPLE) + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_HELP_WORD, COMMAND_HELP_DESC)
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_HELP_EXAMPLE);
        case COMMAND_EXIT_WORD:
            System.out.println(LINE_PREFIX + MESSAGE_GOODBYE);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.exit(0);
        default:
            return String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandType, String.format(MESSAGE_COMMAND_HELP, COMMAND_ADD_WORD, COMMAND_ADD_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_ADD_PARAMETERS) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_ADD_EXAMPLE) + LS + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_FIND_WORD, COMMAND_FIND_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_FIND_PARAMETERS) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_FIND_EXAMPLE) + LS + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_LIST_WORD, COMMAND_LIST_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_LIST_EXAMPLE) + LS + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_DELETE_WORD, COMMAND_DELETE_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_DELETE_PARAMETER) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_DELETE_EXAMPLE) + LS + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_CLEAR_WORD, COMMAND_CLEAR_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_CLEAR_EXAMPLE) + LS + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_EXIT_WORD, COMMAND_EXIT_DESC)
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_EXIT_EXAMPLE) + LS
			+ String.format(MESSAGE_COMMAND_HELP, COMMAND_HELP_WORD, COMMAND_HELP_DESC)
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_HELP_EXAMPLE));
        }
    }

    /**
     * Adds a person (specified by the command args) to the address book.
     * The entire command arguments string is treated as a string representation of the person to add.
     *
     * @param commandArgs full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String executeAddPerson(String commandArgs) {
        // try decoding a person from the raw args
        final Optional<HashMap<PersonProperty,String>> decodeResult = decodePersonFromString(commandArgs);

        // checks if args are valid (decode result will not be present if the person is invalid)
        if (!decodeResult.isPresent()) {
            return String.format(MESSAGE_INVALID_COMMAND_FORMAT, COMMAND_ADD_WORD, String.format(MESSAGE_COMMAND_HELP, COMMAND_ADD_WORD, COMMAND_ADD_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_ADD_PARAMETERS) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_ADD_EXAMPLE) + LS);
        }

        // add the person as specified
        final HashMap<PersonProperty,String> personToAdd = decodeResult.get();
        ALL_PERSONS.add(personToAdd);
		final ArrayList<String> linesToWrite = encodePersonsToStrings(ALL_PERSONS);
		try {
		    Files.write(Paths.get(storageFilePath), linesToWrite);
		} catch (IOException ioe) {
		    System.out.println(LINE_PREFIX + String.format(MESSAGE_ERROR_WRITING_TO_FILE, storageFilePath));
		    System.out.println(LINE_PREFIX + MESSAGE_GOODBYE);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.exit(0);
		}
        return String.format(MESSAGE_ADDED,
		getNameFromPerson(personToAdd), getPhoneFromPerson(personToAdd), getEmailFromPerson(personToAdd));
    }

    /**
     * Finds and lists all persons in address book whose name contains any of the argument keywords.
     * Keyword matching is case sensitive.
     *
     * @param commandArgs full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String executeFindPersons(String commandArgs) {
        final Set<String> keywords = new HashSet<>(new ArrayList<String>(Arrays.asList(commandArgs.trim().trim().split("\\s+"))));
        final ArrayList<HashMap<PersonProperty,String>> personsFound = getPersonsWithNameContainingAnyKeyword(keywords);
        String listAsString = getDisplayString(personsFound);
		System.out.println(LINE_PREFIX + listAsString);
		// clone to insulate from future changes to arg list
		latestPersonListingView = new ArrayList<>(personsFound);
        return String.format(MESSAGE_PERSONS_FOUND_OVERVIEW, personsFound.size());
    }

    /**
     * Retrieve all persons in the full model whose names contain some of the specified keywords.
     *
     * @param keywords for searching
     * @return list of persons in full model with name containing some of the keywords
     */
    private static ArrayList<HashMap<PersonProperty,String>> getPersonsWithNameContainingAnyKeyword(Collection<String> keywords) {
        final ArrayList<HashMap<PersonProperty,String>> matchedPersons = new ArrayList<>();
        for (HashMap<PersonProperty,String> person : ALL_PERSONS) {
            final Set<String> wordsInName = new HashSet<>(new ArrayList<String>(Arrays.asList(getNameFromPerson(person).trim().split("\\s+"))));
            if (!Collections.disjoint(wordsInName, keywords)) {
                matchedPersons.add(person);
            }
        }
        return matchedPersons;
    }

    /**
     * Deletes person identified using last displayed index.
     *
     * @param commandArgs full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String executeDeletePerson(String commandArgs) {
        if (!isDeletePersonArgsValid(commandArgs)) {
            return String.format(MESSAGE_INVALID_COMMAND_FORMAT, COMMAND_DELETE_WORD, String.format(MESSAGE_COMMAND_HELP, COMMAND_DELETE_WORD, COMMAND_DELETE_DESC) + LS
			+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_DELETE_PARAMETER) + LS
			+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_DELETE_EXAMPLE) + LS);
        }
        final int targetVisibleIndex = Integer.parseInt(commandArgs.trim());
        if (!(targetVisibleIndex >= DISPLAYED_INDEX_OFFSET && targetVisibleIndex < latestPersonListingView.size() + DISPLAYED_INDEX_OFFSET)) {
            return MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
        }
        final HashMap<PersonProperty,String> targetInModel = latestPersonListingView.get(targetVisibleIndex - DISPLAYED_INDEX_OFFSET);
        return deletePersonFromAddressBook(targetInModel) ? String.format(MESSAGE_DELETE_PERSON_SUCCESS, String.format(MESSAGE_DISPLAY_PERSON_DATA,
		getNameFromPerson(targetInModel), getPhoneFromPerson(targetInModel), getEmailFromPerson(targetInModel))) // success
                                                          : MESSAGE_PERSON_NOT_IN_ADDRESSBOOK; // not found
    }

    /**
     * Checks validity of delete person argument string's format.
     *
     * @param rawArgs raw command args string for the delete person command
     * @return whether the input args string is valid
     */
    private static boolean isDeletePersonArgsValid(String rawArgs) {
        try {
            final int extractedIndex = Integer.parseInt(rawArgs.trim()); // use standard libraries to parse
            return extractedIndex >= DISPLAYED_INDEX_OFFSET;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Displays all persons in the address book to the user; in added order.
     *
     * @return feedback display message for the operation result
     */
    private static String executeListAllPersonsInAddressBook() {
        ArrayList<HashMap<PersonProperty,String>> toBeDisplayed = ALL_PERSONS;
        String listAsString = getDisplayString(toBeDisplayed);
		System.out.println(LINE_PREFIX + listAsString);
		// clone to insulate from future changes to arg list
		latestPersonListingView = new ArrayList<>(toBeDisplayed);
        return String.format(MESSAGE_PERSONS_FOUND_OVERVIEW, toBeDisplayed.size());
    }

    /**
     * Returns the display string representation of the list of persons.
     */
    private static String getDisplayString(ArrayList<HashMap<PersonProperty,String>> persons) {
        final StringBuilder messageAccumulator = new StringBuilder();
        for (int i = 0; i < persons.size(); i++) {
            final HashMap<PersonProperty,String> person = persons.get(i);
            final int displayIndex = i + DISPLAYED_INDEX_OFFSET;
            messageAccumulator.append('\t')
                              .append(String.format(MESSAGE_DISPLAY_LIST_ELEMENT_INDEX, displayIndex) + String.format(MESSAGE_DISPLAY_PERSON_DATA,
							getNameFromPerson(person), getPhoneFromPerson(person), getEmailFromPerson(person)))
                              .append(LS);
        }
        return messageAccumulator.toString();
    }

    

    

    


    /*
     * ===========================================
     *             STORAGE LOGIC
     * ===========================================
     */

    /**
     * Creates storage file if it does not exist. Shows feedback to user.
     *
     * @param filePath file to create if not present
     */
    private static void createFileIfMissing(String filePath) {
        final File storageFile = new File(filePath);
        if (storageFile.exists()) {
            return;
        }

        System.out.println(LINE_PREFIX + String.format(MESSAGE_ERROR_MISSING_STORAGE_FILE, filePath));

        try {
            storageFile.createNewFile();
            System.out.println(LINE_PREFIX + String.format(MESSAGE_STORAGE_FILE_CREATED, filePath));
        } catch (IOException ioe) {
            System.out.println(LINE_PREFIX + String.format(MESSAGE_ERROR_CREATING_STORAGE_FILE, filePath));
            System.out.println(LINE_PREFIX + MESSAGE_GOODBYE);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.exit(0);
        }
    }

    /**
     * Converts contents of a file into a list of persons.
     * Shows error messages and exits program if any errors in reading or decoding was encountered.
     *
     * @param filePath file to load from
     * @return the list of decoded persons
     */
    private static ArrayList<HashMap<PersonProperty,String>> loadPersonsFromFile(String filePath) {
        final Optional<ArrayList<HashMap<PersonProperty,String>>> successfullyDecoded = decodePersonsFromStrings(getLinesInFile(filePath));
        if (!successfullyDecoded.isPresent()) {
            System.out.println(LINE_PREFIX + MESSAGE_INVALID_STORAGE_FILE_CONTENT);
            System.out.println(LINE_PREFIX + MESSAGE_GOODBYE);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.exit(0);
        }
        return successfullyDecoded.get();
    }

    /**
     * Gets all lines in the specified file as a list of strings. Line separators are removed.
     * Shows error messages and exits program if unable to read from file.
     */
    private static ArrayList<String> getLinesInFile(String filePath) {
        ArrayList<String> lines = null;
        try {
            lines = new ArrayList<String>(Files.readAllLines(Paths.get(filePath)));
        } catch (FileNotFoundException fnfe) {
            System.out.println(LINE_PREFIX + String.format(MESSAGE_ERROR_MISSING_STORAGE_FILE, filePath));
            System.out.println(LINE_PREFIX + MESSAGE_GOODBYE);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.exit(0);
        } catch (IOException ioe) {
            System.out.println(LINE_PREFIX + String.format(MESSAGE_ERROR_READING_FROM_FILE, filePath));
            System.out.println(LINE_PREFIX + MESSAGE_GOODBYE);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.out.println(LINE_PREFIX + DIVIDER);
			System.exit(0);
        }
        return lines;
    }

    


    /**
     * Deletes the specified person from the addressbook if it is inside. Saves any changes to storage file.
     *
     * @param exactPerson the actual person inside the address book (exactPerson == the person to delete in the full list)
     * @return true if the given person was found and deleted in the model
     */
    private static boolean deletePersonFromAddressBook(HashMap<PersonProperty,String> exactPerson) {
        final boolean changed = ALL_PERSONS.remove(exactPerson);
        if (changed) {
            final ArrayList<String> linesToWrite = encodePersonsToStrings(ALL_PERSONS);
			try {
			    Files.write(Paths.get(storageFilePath), linesToWrite);
			} catch (IOException ioe) {
			    System.out.println(LINE_PREFIX + String.format(MESSAGE_ERROR_WRITING_TO_FILE, storageFilePath));
			    System.out.println(LINE_PREFIX + MESSAGE_GOODBYE);
				System.out.println(LINE_PREFIX + DIVIDER);
				System.out.println(LINE_PREFIX + DIVIDER);
				System.exit(0);
			}
        }
        return changed;
    }

    /*
     * ===========================================
     *             PERSON METHODS
     * ===========================================
     */

    /**
     * @param person whose name you want
     * @return person's name
     */
    private static String getNameFromPerson(HashMap<PersonProperty,String> person) {
        return person.get(PersonProperty.NAME);
    }

    /**
     * @param person whose phone number you want
     * @return person's phone number
     */
    private static String getPhoneFromPerson(HashMap<PersonProperty,String> person) {
        return person.get(PersonProperty.PHONE);
    }

    /**
     * @param person whose email you want
     * @return person's email
     */
    private static String getEmailFromPerson(HashMap<PersonProperty,String> person) {
        return person.get(PersonProperty.EMAIL);
    }

    /**
     * Create a person for use in the internal data.
     *
     * @param name of person
     * @param phone without data prefix
     * @param email without data prefix
     * @return constructed person
     */
    private static HashMap<PersonProperty,String> makePersonFromData(String name, String phone, String email) {
        final HashMap<PersonProperty,String> person = new HashMap<PersonProperty,String>();
        person.put(PersonProperty.NAME,name);
        person.put(PersonProperty.PHONE,phone);
        person.put(PersonProperty.EMAIL,email);
        return person;
    }

    /**
     * Encodes list of persons into list of decodable and readable string representations.
     *
     * @param persons to be encoded
     * @return encoded strings
     */
    private static ArrayList<String> encodePersonsToStrings(ArrayList<HashMap<PersonProperty,String>> persons) {
        final ArrayList<String> encoded = new ArrayList<>();
        for (HashMap<PersonProperty,String> person : persons) {
            encoded.add(String.format(PERSON_STRING_REPRESENTATION,
			getNameFromPerson(person), getPhoneFromPerson(person), getEmailFromPerson(person)));
        }
        return encoded;
    }

    /*
     * ==============NOTE TO STUDENTS======================================
     * Note the use of Java's new 'Optional' feature to indicate that
     * the return value may not always be present.
     * ====================================================================
     */
    /**
     * Decodes a person from it's supposed string representation.
     *
     * @param encoded string to be decoded
     * @return if cannot decode: empty Optional
     *         else: Optional containing decoded person
     */
    private static Optional<HashMap<PersonProperty,String>> decodePersonFromString(String encoded) {
        // check that we can extract the parts of a person from the encoded string
        if (!isPersonDataExtractableFrom(encoded)) {
            return Optional.empty();
        }
        final HashMap<PersonProperty,String> decodedPerson = makePersonFromData(
                extractNameFromPersonString(encoded),
                extractPhoneFromPersonString(encoded),
                extractEmailFromPersonString(encoded)
        );
        // check that the constructed person is valid
        return isPersonDataValid(decodedPerson) ? Optional.of(decodedPerson) : Optional.empty();
    }

    /**
     * Decode persons from a list of string representations.
     *
     * @param encodedPersons strings to be decoded
     * @return if cannot decode any: empty Optional
     *         else: Optional containing decoded persons
     */
    private static Optional<ArrayList<HashMap<PersonProperty,String>>> decodePersonsFromStrings(ArrayList<String> encodedPersons) {
        final ArrayList<HashMap<PersonProperty,String>> decodedPersons = new ArrayList<>();
        for (String encodedPerson : encodedPersons) {
            final Optional<HashMap<PersonProperty,String>> decodedPerson = decodePersonFromString(encodedPerson);
            if (!decodedPerson.isPresent()) {
                return Optional.empty();
            }
            decodedPersons.add(decodedPerson.get());
        }
        return Optional.of(decodedPersons);
    }

    /**
     * Checks whether person data (email, name, phone etc) can be extracted from the argument string.
     * Format is [name] p/[phone] e/[email], phone and email positions can be swapped.
     *
     * @param personData person string representation
     * @return whether format of add command arguments allows parsing into individual arguments
     */
    private static boolean isPersonDataExtractableFrom(String personData) {
        final String matchAnyPersonDataPrefix = PERSON_DATA_PREFIX_PHONE + '|' + PERSON_DATA_PREFIX_EMAIL;
        final String[] splitArgs = personData.trim().split(matchAnyPersonDataPrefix);
        return splitArgs.length == 3 // 3 arguments
                && !splitArgs[0].isEmpty() // non-empty arguments
                && !splitArgs[1].isEmpty()
                && !splitArgs[2].isEmpty();
    }

    /**
     * Extracts substring representing person name from person string representation
     *
     * @param encoded person string representation
     * @return name argument
     */
    private static String extractNameFromPersonString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf(PERSON_DATA_PREFIX_PHONE);
        final int indexOfEmailPrefix = encoded.indexOf(PERSON_DATA_PREFIX_EMAIL);
        // name is leading substring up to first data prefix symbol
        int indexOfFirstPrefix = Math.min(indexOfEmailPrefix, indexOfPhonePrefix);
        return encoded.substring(0, indexOfFirstPrefix).trim();
    }

    /**
     * Extracts substring representing phone number from person string representation
     *
     * @param encoded person string representation
     * @return phone number argument WITHOUT prefix
     */
    private static String extractPhoneFromPersonString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf(PERSON_DATA_PREFIX_PHONE);
        final int indexOfEmailPrefix = encoded.indexOf(PERSON_DATA_PREFIX_EMAIL);

        // phone is last arg, target is from prefix to end of string
        if (indexOfPhonePrefix > indexOfEmailPrefix) {
            return encoded.substring(indexOfPhonePrefix, encoded.length()).trim().replace(PERSON_DATA_PREFIX_PHONE, "");

        // phone is middle arg, target is from own prefix to next prefix
        } else {
            return encoded.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim().replace(PERSON_DATA_PREFIX_PHONE, "");
        }
    }

    /**
     * Extracts substring representing email from person string representation
     *
     * @param encoded person string representation
     * @return email argument WITHOUT prefix
     */
    private static String extractEmailFromPersonString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf(PERSON_DATA_PREFIX_PHONE);
        final int indexOfEmailPrefix = encoded.indexOf(PERSON_DATA_PREFIX_EMAIL);

        // email is last arg, target is from prefix to end of string
        if (indexOfEmailPrefix > indexOfPhonePrefix) {
            return encoded.substring(indexOfEmailPrefix, encoded.length()).trim().replace(PERSON_DATA_PREFIX_EMAIL, "");

        // email is middle arg, target is from own prefix to next prefix
        } else {
            return encoded.substring(indexOfEmailPrefix, indexOfPhonePrefix).trim().replace(PERSON_DATA_PREFIX_EMAIL, "");
        }
    }

    /**
     * Validates a person's data fields
     *
     * @param person String array representing the person (used in internal data)
     * @return whether the given person has valid data
     */
    private static boolean isPersonDataValid(HashMap<PersonProperty,String> person) {
        return person.get(PersonProperty.NAME).matches("(\\w|\\s)+")
                && person.get(PersonProperty.PHONE).matches("\\d+")
                && person.get(PersonProperty.EMAIL).matches("\\S+@\\S+\\.\\S+");
    }

}