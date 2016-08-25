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
import java.util.Collections;
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
 * This class is used to maintain a list of HUMAN data which are saved
 * in a text file.
 **/
public class AddressBookT4 {

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
    private static final String LINE_SEPARATOR = System.lineSeparator() + LINE_PREFIX;

    /*
     * ==============NOTE TO STUDENTS======================================
     * These messages shown to the user are defined in one place for convenient
     * editing and proof reading. Such messages are considered part of the UI
     * and may be subjected to review by UI experts or technical writers. Note
     * that Some of the strings below include '%1$s' etc to mark the locations
     * at which java String.format(...) method can insert values.
     * ====================================================================
     */
    private static final String MESSAGE_ADDED = "New HUMAN added: %1$s, Phone: %2$s, Email: %3$s";
    private static final String MESSAGE_ADDRESSBOOK_CLEARED = "Address book has been cleared!";
    private static final String MESSAGE_COMMAND_HELP = "%1$s: %2$s";
    private static final String MESSAGE_COMMAND_HELP_PARAMETERS = "\tParameters: %1$s";
    private static final String MESSAGE_COMMAND_HELP_EXAMPLE = "\tExample: %1$s";
    private static final String MESSAGE_DELETE_HUMAN_SUCCESS = "Deleted HUMAN: %1$s";
    private static final String MESSAGE_DISPLAY_HUMAN_DATA = "%1$s  Phone Number: %2$s  Email: %3$s";
    private static final String MESSAGE_DISPLAY_LIST_ELEMENT_INDEX = "%1$d. ";
    private static final String MESSAGE_GOODBYE = "Exiting Address Book... Good bye!";
    private static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format: %1$s " + LINE_SEPARATOR + "%2$s";
    private static final String MESSAGE_INVALID_FILE = "The given file name [%1$s] is not a valid file name!";
    private static final String MESSAGE_INVALID_PROGRAM_ARGS = "Too many parameters! Correct program argument format:"
                                                            + LINE_SEPARATOR + "\tjava AddressBook"
                                                            + LINE_SEPARATOR + "\tjava AddressBook [custom storage file path]";
    private static final String MESSAGE_INVALID_HUMAN_DISPLAYED_INDEX = "The HUMAN index provided is invalid";
    private static final String MESSAGE_INVALID_STORAGE_FILE_CONTENT = "Storage file has invalid content";
    private static final String MESSAGE_HUMAN_NOT_IN_ADDRESSBOOK = "HUMAN could not be found in address book";
    private static final String MESSAGE_ERROR_CREATING_STORAGE_FILE = "Error: unable to create file: %1$s";
    private static final String MESSAGE_ERROR_MISSING_STORAGE_FILE = "Storage file missing: %1$s";
    private static final String MESSAGE_ERROR_READING_FROM_FILE = "Unexpected error: unable to read from file: %1$s";
    private static final String MESSAGE_ERROR_WRITING_TO_FILE = "Unexpected error: unable to write to file: %1$s";
    private static final String MESSAGE_HUMANS_FOUND_OVERVIEW = "%1$d HUMANs found!";
    private static final String message_storageFILeCreatEd = "Created new empty storage file: %1$s";
    private static final String MESSAGE_WELCOME = "Welcome to your Address Book!";
    private static final String MESSAGE_USING_DEFAULT_FILE = "Using default storage file : " + DEFAULT_STORAGE_FILEPATH;

    
    // These are the prefix strings to define the data type of a command parameter
    private static final String human_data_prefix_phone = "p/";
    private static final String HUMAN_DATA_PREFIX_EMAIL = "e/";

    private static final String HUMAN_STRING_REPRESENTATION = "%1$s " // name
                                                            + human_data_prefix_phone + "%2$s " // phone
                                                            + HUMAN_DATA_PREFIX_EMAIL + "%3$s"; // email
    private static final String COMMAND_ADD_WORD = "add";
    private static final String COMMAND_ADD_DESC = "Adds a HUMAN to the address book.";
    private static final String COMMAND_ADD_PARAMETERS = "NAME "
                                                      + human_data_prefix_phone + "PHONE_NUMBER "
                                                      + HUMAN_DATA_PREFIX_EMAIL + "EMAIL";
    private static final String COMMAND_ADD_EXAMPLE = COMMAND_ADD_WORD + " John Doe p/98765432 e/johnd@gmail.com";

    private static final String COMMAND_FIND_WORD = "find";
    private static final String COMMAND_FIND_DESC = "Finds all HUMANs whose names contain any of the specified "
                                        + "keywords (case-sensitive) and displays them as a list with index numbers.";
    private static final String COMMAND_FIND_PARAMETERS = "KEYWORD [MORE_KEYWORDS]";
    private static final String COMMAND_FIND_EXAMPLE = COMMAND_FIND_WORD + " alice bob charlie";

    private static final String COMMAND_LIST_WORD = "list";
    private static final String COMMAND_LIST_DESC = "Displays all HUMANs as a list with index numbers.";
    private static final String COMMAND_LIST_EXAMPLE = COMMAND_LIST_WORD;

    private static final String COMMAND_DELETE_WORD = "delete";
    private static final String COMMAND_DELETE_DESC = "Deletes a HUMAN identified by the index number used in "
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


    /* We use a String array to store details of a single HUMAN.
     * The constants given below are the indexes for the different data elements of a HUMAN
     * used by the internal String[] storage format.
     * For example, a HUMAN's name is stored as the 0th element in the array.
     */
    private static final int HUMAN_DATA_INDEX_NAME = 0;
    private static final int HUMAN_DATA_INDEX_PHONE = 1;
    private static final int HUMAN_data_index_email = 2;

    /**
     * The number of data elements for a single HUMAN.
     */
    private static final int HUMAN_DATA_COUNT = 3;

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
     *    private static final List<String[]> ALL_HUMANS = new ArrayList<>()
     * That is because List is an interface implemented by the ArrayList class.
     * In this code we use ArrayList instead because we wanted to to stay away from advanced concepts
     * such as interface inheritance.
     * ====================================================================================================
     */
    /**
     * List of all HUMANs in the address book.
     */
    private static final ArrayList<String[]> ALL_HUMANS = new ArrayList<String[]>();


    /**
     * Stores the most recent list of HUMANs shown to the user as a result of a user command.
     * This is a subset of the full list. Deleting HUMANs in the pull list does not delete
     * those HUMANs from this list.
     */
    private static ArrayList<String[]> latestHUMANListingView = getAllHUMANsInAddressBook(); // initial view is of all

    /**
     * The path to the file used for storing HUMAN data.
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
        String[] message = { DIVIDER, DIVIDER, VERSION, MESSAGE_WELCOME, DIVIDER };
		for (String m : message) {
		    System.out.println(LINE_PREFIX + m);
		}
        if (args.length >= 2) {
		    String[] message3 = { MESSAGE_INVALID_PROGRAM_ARGS };
			for (String m1 : message3) {
			    System.out.println(LINE_PREFIX + m1);
			}
		    String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m2 : message1) {
			    System.out.println(LINE_PREFIX + m2);
			}
			System.exit(0);
		}
		
		if (args.length == 1) {
		    String filePath = args[0];
			if (!isValidFilePath(filePath)) {
			    String[] message1 = { String.format(MESSAGE_INVALID_FILE, filePath) };
				for (String m1 : message1) {
				    System.out.println(LINE_PREFIX + m1);
				}
			    String[] message11 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
				for (String m1 : message11) {
				    System.out.println(LINE_PREFIX + m1);
				}
				System.exit(0);
			}
			
			storageFilePath = filePath;
			createFileIfMissing(filePath);
		}
		
		if(args.length == 0) {
		    setupDefaultFileForStorage();
		}
        ALL_HUMANS.clear();
		ALL_HUMANS.addAll(loadHUMANsFromFile(storageFilePath));
        while (true) {
            System.out.print(LINE_PREFIX + "Enter command: ");
			String inputLine = SCANNER.nextLine();
			// silently consume all blank and comment lines
			while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == INPUT_COMMENT_MARKER) {
			    inputLine = SCANNER.nextLine();
			}
			String userCommand = inputLine;
			String[] message1 = { "[Command entered:" + userCommand + "]" };
            for (String m : message1) {
			    System.out.println(LINE_PREFIX + m);
			}
            String feedback = executeCommand(userCommand);
			String[] message2 = { feedback, DIVIDER };
            for (String m : message2) {
			    System.out.println(LINE_PREFIX + m);
			}
        }
    }

    

    /*
     * ==============NOTE TO STUDENTS==========================================
     * If the reader wants a deeper understanding of the solution, she can go
     * to the next level of abstraction by reading the methods (given below)
     * that is referenced by the method above.
     * ====================================================================
     */

    /**
     * Sets up the storage based on the default file.
     * Creates file if missing.
     * Exits program if the file cannot be created.
     */
    private static void setupDefaultFileForStorage() {
        String[] message = { MESSAGE_USING_DEFAULT_FILE };
		for (String m : message) {
		    System.out.println(LINE_PREFIX + m);
		}
        storageFilePath = DEFAULT_STORAGE_FILEPATH;
        createFileIfMissing(storageFilePath);
    }

    /**
     * Returns true if the given file is acceptable.
     * The file path is acceptable if it ends in '.txt'
     * TODO: Implement a more rigorous validity checking.
     */
    private static boolean isValidFilePath(String filePath) {
        return filePath.endsWith(".txt");
    }

    


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
        final String[] commandTypeAndParams = splitCommandWordAndArgs(userInputString);
        final String commandType = commandTypeAndParams[0];
        final String commandArgs = commandTypeAndParams[1];
        switch (commandType) {
        case COMMAND_ADD_WORD:
            return executeAddHUMAN(commandArgs);
        case COMMAND_FIND_WORD:
            return executeFindHUMANs(commandArgs);
        case COMMAND_LIST_WORD:
            return executeListAllHUMANsInAddressBook();
        case COMMAND_DELETE_WORD:
            return executeDeleteHUMAN(commandArgs);
        case COMMAND_CLEAR_WORD:
            return executeClearAddressBook();
        case COMMAND_HELP_WORD:
            return getUsageInfoForAllCommands();
        case COMMAND_EXIT_WORD:
            ExecuteEXITPROGRAM();
        default:
            return String.format(MESSAGE_INVALID_COMMAND_FORMAT, commandType, getUsageInfoForAllCommands());
        }
    }

    /**
     * Splits raw user input into command word and command arguments string
     *
     * @return  size 2 array; first element is the command type and second element is the arguments string
     */
    private static String[] splitCommandWordAndArgs(String rawUserInput) {
        final String[] split =  rawUserInput.trim().split("\\s+", 2);
        return split.length == 2 ? split : new String[] { split[0] , "" }; // else case: no parameters
    }

    /**
     * Adds a HUMAN (specified by the command args) to the address book.
     * The entire command arguments string is treated as a string representation of the HUMAN to add.
     *
     * @param commandArgs full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String executeAddHUMAN(String commandArgs) {
        // try decoding a HUMAN from the raw args
        final Optional<String[]> decodeResult = decodeHUMANFromString(commandArgs);

        // checks if args are valid (decode result will not be present if the HUMAN is invalid)
        if (!decodeResult.isPresent()) {
            return String.format(MESSAGE_INVALID_COMMAND_FORMAT, COMMAND_ADD_WORD, getUsageInfoForADDCOMMAND());
        }

        // add the HUMAN as specified
        final String[] HUMANToAdd = decodeResult.get();
        addHUMANToAddressBook(HUMANToAdd);
        return getMessageForSuccessfulAddHUMAN(HUMANToAdd);
    }

    /**
     * Constructs a feedback message for a successful add HUMAN command execution.
     *
     * @see #executeAddHUMAN(String)
     * @param addedHUMAN HUMAN who was successfully added
     * @return successful add HUMAN feedback message
     */
    private static String getMessageForSuccessfulAddHUMAN(String[] addedHUMAN) {
        return String.format(MESSAGE_ADDED,
                getNameFromHUMAN(addedHUMAN), getPhoneFromHUMAN(addedHUMAN), getEmailFromHUMAN(addedHUMAN));
    }

    /**
     * Finds and lists all HUMANs in address book whose name contains any of the argument keywords.
     * Keyword matching is case sensitive.
     *
     * @param commandArgs full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String executeFindHUMANs(String commandArgs) {
        final Set<String> keywords = new HashSet<>(splitByWhitespace(commandArgs.trim()));
		final ArrayList<String[]> matchedHUMANs = new ArrayList<>();
		for(String keyword: keywords){
			keyword = keyword.toLowerCase();
		}
		for (String[] HUMAN : getAllHUMANsInAddressBook()) {
		    final Set<String> wordsInName = new HashSet<>(splitByWhitespace(getNameFromHUMAN(HUMAN)));
		    if (!Collections.disjoint(wordsInName, keywords)) {
		        matchedHUMANs.add(HUMAN);
		    }
		}
        final ArrayList<String[]> HUMANsFound = matchedHUMANs;
        showToUser(HUMANsFound);
        return String.format(MESSAGE_HUMANS_FOUND_OVERVIEW, HUMANsFound.size());
    }

    /**
     * Deletes HUMAN identified using last displayed index.
     *
     * @param commandArgs full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String executeDeleteHUMAN(String commandArgs) {
        if (!isDeleteHUMANArgsValid(commandArgs)) {
            return String.format(MESSAGE_INVALID_COMMAND_FORMAT, COMMAND_DELETE_WORD, getUsageInfoForDeleteCommand());
        }
        final int targetVisibleIndex = extractTargetIndexFromDeleteHUMANArgs(commandArgs);
        if (!isDisplayIndexValidForLastHUMANListingView(targetVisibleIndex)) {
            return MESSAGE_INVALID_HUMAN_DISPLAYED_INDEX;
        }
        final String[] targetInModel = getHUMANByLastVisibleIndex(targetVisibleIndex);
        return deleteHUMANFromAddressBook(targetInModel) ? getMessageForSuccessfulDelete(targetInModel) // success
                                                          : MESSAGE_HUMAN_NOT_IN_ADDRESSBOOK; // not found
    }

    /**
     * Checks validity of delete HUMAN argument string's format.
     *
     * @param rawArgs raw command args string for the delete HUMAN command
     * @return whether the input args string is valid
     */
    private static boolean isDeleteHUMANArgsValid(String rawArgs) {
        try {
            final int extractedIndex = Integer.parseInt(rawArgs.trim()); // use standard libraries to parse
            return extractedIndex >= DISPLAYED_INDEX_OFFSET;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Extracts the target's index from the raw delete HUMAN args string
     *
     * @param rawArgs raw command args string for the delete HUMAN command
     * @return extracted index
     */
    private static int extractTargetIndexFromDeleteHUMANArgs(String rawArgs) {
        return Integer.parseInt(rawArgs.trim());
    }

    /**
     * Checks that the given index is within bounds and valid for the last shown HUMAN list view.
     *
     * @param index to check
     * @return whether it is valid
     */
    private static boolean isDisplayIndexValidForLastHUMANListingView(int index) {
        return index >= DISPLAYED_INDEX_OFFSET && index < getLatestHUMANListingView().size() + DISPLAYED_INDEX_OFFSET;
    }

    /**
     * Constructs a feedback message for a successful delete HUMAN command execution.
     *
     * @see #executeDeleteHUMAN(String)
     * @param deletedHUMAN successfully deleted
     * @return successful delete HUMAN feedback message
     */
    private static String getMessageForSuccessfulDelete(String[] deletedHUMAN) {
        return String.format(MESSAGE_DELETE_HUMAN_SUCCESS, getMessageForFormattedHUMANData(deletedHUMAN));
    }

    /**
     * Clears all HUMANs in the address book
     *
     * @return feedback display message for the operation result
     */
    private static String executeClearAddressBook() {
        clearAddressBook();
        return MESSAGE_ADDRESSBOOK_CLEARED;
    }

    /**
     * Displays all HUMANs in the address book to the user; in added order.
     *
     * @return feedback display message for the operation result
     */
    private static String executeListAllHUMANsInAddressBook() {
        ArrayList<String[]> toBeDisplayed = getAllHUMANsInAddressBook();
        showToUser(toBeDisplayed);
        return String.format(MESSAGE_HUMANS_FOUND_OVERVIEW, toBeDisplayed.size());
    }

    /**
     * Request to terminate the program.
     *
     * @return feedback display message for the operation result
     */
    private static void ExecuteEXITPROGRAM() {
        String[] message = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
		for (String m : message) {
		    System.out.println(LINE_PREFIX + m);
		}
		System.exit(0);
    }

    /*
     * ===========================================
     *               UI LOGIC
     * ===========================================
     */

    /**
     * Shows the list of HUMANs to the user.
     * The list will be indexed, starting from 1.
     *
     */
    private static void showToUser(ArrayList<String[]> HUMANs) {
        String listAsString = getDisplayString(HUMANs);
		String[] message = { listAsString };
        for (String m : message) {
		    System.out.println(LINE_PREFIX + m);
		}
        updateLatestViewedHUMANListing(HUMANs);
    }

    /**
     * Returns the display string representation of the list of HUMANs.
     */
    private static String getDisplayString(ArrayList<String[]> HUMANs) {
        final StringBuilder messageAccumulator = new StringBuilder();
        for (int i = 0; i < HUMANs.size(); i++) {
            final String[] HUMAN = HUMANs.get(i);
            final int displayIndex = i + DISPLAYED_INDEX_OFFSET;
            messageAccumulator.append('\t')
                              .append(getIndexedHUMANListElementMessage(displayIndex, HUMAN))
                              .append(LINE_SEPARATOR);
        }
        return messageAccumulator.toString();
    }

    /**
     * Constructs a prettified listing element message to represent a HUMAN and their data.
     *
     * @param visibleIndex visible index for this listing
     * @param HUMAN to show
     * @return formatted listing message with index
     */
    private static String getIndexedHUMANListElementMessage(int visibleIndex, String[] HUMAN) {
        return String.format(MESSAGE_DISPLAY_LIST_ELEMENT_INDEX, visibleIndex) + getMessageForFormattedHUMANData(HUMAN);
    }

    /**
     * Constructs a prettified string to show the user a HUMAN's data.
     *
     * @param HUMAN to show
     * @return formatted message showing internal state
     */
    private static String getMessageForFormattedHUMANData(String[] HUMAN) {
        return String.format(MESSAGE_DISPLAY_HUMAN_DATA,
                getNameFromHUMAN(HUMAN), getPhoneFromHUMAN(HUMAN), getEmailFromHUMAN(HUMAN));
    }

    /**
     * Updates the latest HUMAN listing view the user has seen.
     *
     * @param newListing the new listing of HUMANs
     */
    private static void updateLatestViewedHUMANListing(ArrayList<String[]> newListing) {
        // clone to insulate from future changes to arg list
        latestHUMANListingView = new ArrayList<>(newListing);
    }

    /**
     * Retrieves the HUMAN identified by the displayed index from the last shown listing of HUMANs.
     *
     * @param lastVisibleIndex displayed index from last shown HUMAN listing
     * @return the actual HUMAN object in the last shown HUMAN listing
     */
    private static String[] getHUMANByLastVisibleIndex(int lastVisibleIndex) {
       return latestHUMANListingView.get(lastVisibleIndex - DISPLAYED_INDEX_OFFSET);
    }

    /**
     * @return unmodifiable list view of the last HUMAN listing view
     */
    private static ArrayList<String[]> getLatestHUMANListingView() {
        return latestHUMANListingView;
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
		String[] message = { String.format(MESSAGE_ERROR_MISSING_STORAGE_FILE, filePath) };

        for (String m : message) {
		    System.out.println(LINE_PREFIX + m);
		}

        try {
            storageFile.createNewFile();
			String[] message1 = { String.format(message_storageFILeCreatEd, filePath) };
            for (String m : message1) {
			    System.out.println(LINE_PREFIX + m);
			}
        } catch (IOException ioe) {
            String[] message1 = { String.format(MESSAGE_ERROR_CREATING_STORAGE_FILE, filePath) };
			for (String m : message1) {
			    System.out.println(LINE_PREFIX + m);
			}
            String[] message2 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message2) {
			    System.out.println(LINE_PREFIX + m1);
			}
			System.exit(0);
        }
    }

    /**
     * Converts contents of a file into a list of HUMANs.
     * Shows error messages and exits program if any errors in reading or decoding was encountered.
     *
     * @param filePath file to load from
     * @return the list of decoded HUMANs
     */
    private static ArrayList<String[]> loadHUMANsFromFile(String filePath) {
        final Optional<ArrayList<String[]>> successFullyDecoded = decodeHUMANsFromStrings(getLinesInFile(filePath));
        if (!successFullyDecoded.isPresent()) {
            String[] message = { MESSAGE_INVALID_STORAGE_FILE_CONTENT };
			for (String m : message) {
			    System.out.println(LINE_PREFIX + m);
			}
            String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message1) {
			    System.out.println(LINE_PREFIX + m1);
			}
			System.exit(0);
        }
        return successFullyDecoded.get();
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
            String[] message = { String.format(MESSAGE_ERROR_MISSING_STORAGE_FILE, filePath) };
			for (String m : message) {
			    System.out.println(LINE_PREFIX + m);
			}
            String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message1) {
			    System.out.println(LINE_PREFIX + m1);
			}
			System.exit(0);
        } catch (IOException ioe) {
            String[] message = { String.format(MESSAGE_ERROR_READING_FROM_FILE, filePath) };
			for (String m : message) {
			    System.out.println(LINE_PREFIX + m);
			}
            String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message1) {
			    System.out.println(LINE_PREFIX + m1);
			}
			System.exit(0);
        }
        return lines;
    }

    


    /*
     * ================================================================================
     *        INTERNAL ADDRESS BOOK DATA METHODS
     * ================================================================================
     */

    /**
     * Adds a HUMAN to the address book. Saves changes to storage file.
     *
     * @param HUMAN to add
     */
    private static void addHUMANToAddressBook(String[] HUMAN) {
        ALL_HUMANS.add(HUMAN);
		final ArrayList<String> encoded = new ArrayList<>();
		for (String[] HUMAN1 : getAllHUMANsInAddressBook()) {
		    encoded.add(encodeHUMANToString(HUMAN1));
		}
        final ArrayList<String> linesToWrite = encoded;
		try {
		    Files.write(Paths.get(storageFilePath), linesToWrite);
		} catch (IOException ioe) {
		    String[] message = { String.format(MESSAGE_ERROR_WRITING_TO_FILE, storageFilePath) };
			for (String m : message) {
			    System.out.println(LINE_PREFIX + m);
			}
		    String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message1) {
			    System.out.println(LINE_PREFIX + m1);
			}
			System.exit(0);
		}
    }

    /**
     * Deletes the specified HUMAN from the addressbook if it is inside. Saves any changes to storage file.
     *
     * @param exactHUMAN the actual HUMAN inside the address book (exactHUMAN == the HUMAN to delete in the full list)
     * @return true if the given HUMAN was found and deleted in the model
     */
    private static boolean deleteHUMANFromAddressBook(String[] exactHUMAN) {
        final boolean changed = ALL_HUMANS.remove(exactHUMAN);
        if (changed) {
            final ArrayList<String> encoded = new ArrayList<>();
			for (String[] HUMAN : getAllHUMANsInAddressBook()) {
			    encoded.add(encodeHUMANToString(HUMAN));
			}
			final ArrayList<String> linesToWrite = encoded;
			try {
			    Files.write(Paths.get(storageFilePath), linesToWrite);
			} catch (IOException ioe) {
			    String[] message = { String.format(MESSAGE_ERROR_WRITING_TO_FILE, storageFilePath) };
				for (String m : message) {
				    System.out.println(LINE_PREFIX + m);
				}
			    String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
				for (String m1 : message1) {
				    System.out.println(LINE_PREFIX + m1);
				}
				System.exit(0);
			}
        }
        return changed;
    }

    /**
     * @return unmodifiable list view of all HUMANs in the address book
     */
    private static ArrayList<String[]> getAllHUMANsInAddressBook() {
        return ALL_HUMANS;
    }

    /**
     * Clears all HUMANs in the address book and saves changes to file.
     */
    private static void clearAddressBook() {
        ALL_HUMANS.clear();
		final ArrayList<String> encoded = new ArrayList<>();
		for (String[] HUMAN : getAllHUMANsInAddressBook()) {
		    encoded.add(encodeHUMANToString(HUMAN));
		}
        final ArrayList<String> linesToWrite = encoded;
		try {
		    Files.write(Paths.get(storageFilePath), linesToWrite);
		} catch (IOException ioe) {
		    String[] message = { String.format(MESSAGE_ERROR_WRITING_TO_FILE, storageFilePath) };
			for (String m : message) {
			    System.out.println(LINE_PREFIX + m);
			}
		    String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message1) {
			    System.out.println(LINE_PREFIX + m1);
			}
			System.exit(0);
		}
    }

    


    /*
     * ===========================================
     *             HUMAN METHODS
     * ===========================================
     */

    /**
     * @param HUMAN whose name you want
     * @return HUMAN's name
     */
    private static String getNameFromHUMAN(String[] HUMAN) {
        return HUMAN[HUMAN_DATA_INDEX_NAME].toLowerCase();
    }

    /**
     * @param HUMAN whose phone number you want
     * @return HUMAN's phone number
     */
    private static String getPhoneFromHUMAN(String[] HUMAN) {
        return HUMAN[HUMAN_DATA_INDEX_PHONE];
    }

    /**
     * @param HUMAN whose email you want
     * @return HUMAN's email
     */
    private static String getEmailFromHUMAN(String[] HUMAN) {
        return HUMAN[HUMAN_data_index_email];
    }

    /**
     * Create a HUMAN for use in the internal data.
     *
     * @param name of HUMAN
     * @param phone without data prefix
     * @param email without data prefix
     * @return constructed HUMAN
     */
    private static String[] makeHUMANFromData(String name, String phone, String email) {
        final String[] HUMAN = new String[HUMAN_DATA_COUNT];
        HUMAN[HUMAN_DATA_INDEX_NAME] = name;
        HUMAN[HUMAN_DATA_INDEX_PHONE] = phone;
        HUMAN[HUMAN_data_index_email] = email;
        return HUMAN;
    }

    /**
     * Encodes a HUMAN into a decodable and readable string representation.
     *
     * @param HUMAN to be encoded
     * @return encoded string
     */
    private static String encodeHUMANToString(String[] HUMAN) {
        return String.format(HUMAN_STRING_REPRESENTATION,
                getNameFromHUMAN(HUMAN), getPhoneFromHUMAN(HUMAN), getEmailFromHUMAN(HUMAN));
    }

    /*
     * ==============NOTE TO STUDENTS======================================
     * Note the use of Java's new 'Optional' feature to indicate that
     * the return value may not always be present.
     * ====================================================================
     */
    /**
     * Decodes a HUMAN from it's supposed string representation.
     *
     * @param encoded string to be decoded
     * @return if cannot decode: empty Optional
     *         else: Optional containing decoded HUMAN
     */
    private static Optional<String[]> decodeHUMANFromString(String encoded) {
        // check that we can extract the parts of a HUMAN from the encoded string
        if (!isHUMANDataExtractableFrom(encoded)) {
            return Optional.empty();
        }
        final String[] decodedHUMAN = makeHUMANFromData(
                extractNameFromHUMANString(encoded),
                extractPhoneFromHUMANString(encoded),
                extractEmailFromHUMANString(encoded)
        );
        // check that the constructed HUMAN is valid
        return isHUMANDataValid(decodedHUMAN) ? Optional.of(decodedHUMAN) : Optional.empty();
    }

    /**
     * Decode HUMANs from a list of string representations.
     *
     * @param encodedHUMANs strings to be decoded
     * @return if cannot decode any: empty Optional
     *         else: Optional containing decoded HUMANs
     */
    private static Optional<ArrayList<String[]>> decodeHUMANsFromStrings(ArrayList<String> encodedHUMANs) {
        final ArrayList<String[]> decodedHUMANs = new ArrayList<>(); //Creates a new arraylist
        for (String encodedHUMAN : encodedHUMANs) {
            final Optional<String[]> decodedHUMAN = decodeHUMANFromString(encodedHUMAN);
            if (!decodedHUMAN.isPresent()) {
                return Optional.empty();
            }
            decodedHUMANs.add(decodedHUMAN.get());
        }
        return Optional.of(decodedHUMANs);
    }

    /**
     * Checks whether HUMAN data (email, name, phone etc) can be extracted from the argument string.
     * Format is [name] p/[phone] e/[email], phone and email positions can be swapped.
     *
     * @param HUMANData HUMAN string representation
     * @return whether format of add command arguments allows parsing into individual arguments
     */
    private static boolean isHUMANDataExtractableFrom(String HUMANData) {
        final String matchAnyHUMANDataPrefix = human_data_prefix_phone + '|' + HUMAN_DATA_PREFIX_EMAIL;
        final String[] splitArgs = HUMANData.trim().split(matchAnyHUMANDataPrefix);
        return splitArgs.length == 3 // 3 arguments
                && !splitArgs[0].isEmpty() // non-empty arguments
                && !splitArgs[1].isEmpty()
                && !splitArgs[2].isEmpty();
    }

    /**
     * Extracts substring representing HUMAN name from HUMAN string representation
     *
     * @param encoded HUMAN string representation
     * @return name argument
     */
    private static String extractNameFromHUMANString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf(human_data_prefix_phone);
        final int indexOfEmailPrefix = encoded.indexOf(HUMAN_DATA_PREFIX_EMAIL);
        // name is leading substring up to first data prefix symbol
        int indexOfFirstPrefix = Math.min(indexOfEmailPrefix, indexOfPhonePrefix);
        return encoded.substring(0, indexOfFirstPrefix).trim();
    }

    /**
     * Extracts substring representing phone number from HUMAN string representation
     *
     * @param encoded HUMAN string representation
     * @return phone number argument WITHOUT prefix
     */
    private static String extractPhoneFromHUMANString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf(human_data_prefix_phone);
        final int indexOfEmailPrefix = encoded.indexOf(HUMAN_DATA_PREFIX_EMAIL);

        // phone is last arg, target is from prefix to end of string
        if (indexOfPhonePrefix > indexOfEmailPrefix) {
            return removePrefixSign(encoded.substring(indexOfPhonePrefix, encoded.length()).trim(),
                    human_data_prefix_phone);

        // phone is middle arg, target is from own prefix to next prefix
        } else {
            return removePrefixSign(
                    encoded.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim(),
                    human_data_prefix_phone);
        }
    }

    /**
     * Extracts substring representing email from HUMAN string representation
     *
     * @param encoded HUMAN string representation
     * @return email argument WITHOUT prefix
     */
    private static String extractEmailFromHUMANString(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf(human_data_prefix_phone);
        final int indexOfEmailPrefix = encoded.indexOf(HUMAN_DATA_PREFIX_EMAIL);

        // email is last arg, target is from prefix to end of string
        if (indexOfEmailPrefix > indexOfPhonePrefix) {
            return removePrefixSign(encoded.substring(indexOfEmailPrefix, encoded.length()).trim(),
                    HUMAN_DATA_PREFIX_EMAIL);

        // email is middle arg, target is from own prefix to next prefix
        } else {
            return removePrefixSign(
                    encoded.substring(indexOfEmailPrefix, indexOfPhonePrefix).trim(),
                    HUMAN_DATA_PREFIX_EMAIL);
        }
    }

    /**
     * Validates a HUMAN's data fields
     *
     * @param HUMAN String array representing the HUMAN (used in internal data)
     * @return whether the given HUMAN has valid data
     */
    private static boolean isHUMANDataValid(String[] HUMAN) {
        return isHUMANNameValid(HUMAN[HUMAN_DATA_INDEX_NAME])
                && isHUMANPhoneValid(HUMAN[HUMAN_DATA_INDEX_PHONE])
                && ISHUMANEMAILVALID(HUMAN[HUMAN_data_index_email]);
    }

    /*
     * ==============NOTE TO STUDENTS======================================
     * Note the use of 'regular expressions' in the method below.
     * Regular expressions can be very useful in checking if a a string
     * follows a specific format.
     * ====================================================================
     */
    /**
     * Validates string as a legal HUMAN name
     *
     * @param name to be validated
     * @return whether arg is a valid HUMAN name
     */
    private static boolean isHUMANNameValid(String name) {
        return name.matches("(\\w|\\s)+");  // name is nonempty mixture of alphabets and whitespace
        //TODO: implement a more permissive validation
    }

    /**
     * Validates string as a legal HUMAN phone number
     *
     * @param phone to be validated
     * @return whether arg is a valid HUMAN phone number
     */
    private static boolean isHUMANPhoneValid(String phone) {
        return phone.matches("\\d+");    // phone nonempty sequence of digits
        //TODO: implement a more permissive validation
    }

    /**
     * Validates string as a legal HUMAN email
     *
     * @param email to be validated
     * @return whether arg is a valid HUMAN email
     */
    private static boolean ISHUMANEMAILVALID(String email) {
        return email.matches("\\S+@\\S+\\.\\S+"); // email is [non-whitespace]@[non-whitespace].[non-whitespace]
        //TODO: implement a more permissive validation
    }


    /*
     * ===============================================
     *         COMMAND HELP INFO FOR USERS
     * ===============================================
     */

    /**
     * @return  Usage info for all commands
     */
    private static String getUsageInfoForAllCommands() {
        return getUsageInfoForADDCOMMAND() + LINE_SEPARATOR
                + getUsageINFOForFindCommand() + LINE_SEPARATOR
                + GETUsageInfoForViewCommand() + LINE_SEPARATOR
                + getUsageInfoForDeleteCommand() + LINE_SEPARATOR
                + GETUSAGEInfoForClearCommand() + LINE_SEPARATOR
                + getUsageInfoForExitCommand() + LINE_SEPARATOR
                + GetUsageInfoForHelpCommand();
    }

    /**
     * Builds string for showing 'add' command usage instruction
     *
     * @return  'add' command usage instruction
     */
    private static String getUsageInfoForADDCOMMAND() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_ADD_WORD, COMMAND_ADD_DESC) + LINE_SEPARATOR
                + String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_ADD_PARAMETERS) + LINE_SEPARATOR
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_ADD_EXAMPLE) + LINE_SEPARATOR;
    }

    /**
     * Builds string for showing 'find' command usage instruction
     *
     * @return  'find' command usage instruction
     */
    private static String getUsageINFOForFindCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_FIND_WORD, COMMAND_FIND_DESC) + LINE_SEPARATOR
                + String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_FIND_PARAMETERS) + LINE_SEPARATOR
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_FIND_EXAMPLE) + LINE_SEPARATOR;
    }

    /**
     * Builds string for showing 'delete' command usage instruction
     *
     * @return  'delete' command usage instruction
     */
    private static String getUsageInfoForDeleteCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_DELETE_WORD, COMMAND_DELETE_DESC) + LINE_SEPARATOR
                + String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_DELETE_PARAMETER) + LINE_SEPARATOR
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_DELETE_EXAMPLE) + LINE_SEPARATOR;
    }

    /**
     * Builds string for showing 'clear' command usage instruction
     *
     * @return  'clear' command usage instruction
     */
    private static String GETUSAGEInfoForClearCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_CLEAR_WORD, COMMAND_CLEAR_DESC) + LINE_SEPARATOR
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_CLEAR_EXAMPLE) + LINE_SEPARATOR;
    }

    /**
     * Builds string for showing 'view' command usage instruction
     *
     * @return  'view' command usage instruction
     */
    private static String GETUsageInfoForViewCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_LIST_WORD, COMMAND_LIST_DESC) + LINE_SEPARATOR
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_LIST_EXAMPLE) + LINE_SEPARATOR;
    }

    /**
     * Builds string for showing 'help' command usage instruction
     *
     * @return  'help' command usage instruction
     */
    private static String GetUsageInfoForHelpCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_HELP_WORD, COMMAND_HELP_DESC)
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_HELP_EXAMPLE);
    }

    /**
     * Builds string for showing 'exit' command usage instruction
     *
     * @return  'exit' command usage instruction
     */
    private static String getUsageInfoForExitCommand() {
        return String.format(MESSAGE_COMMAND_HELP, COMMAND_EXIT_WORD, COMMAND_EXIT_DESC)
                + String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_EXIT_EXAMPLE);
    }


    /*
     * ============================
     *         UTILITY METHODS
     * ============================
     */

    /**
     * Removes sign(p/, d/, etc) from parameter string
     *
     * @param s  Parameter as a string
     * @param sign  Parameter sign to be removed
     *
     * @return  Priority string without p/
     */
    private static String removePrefixSign(String s, String sign) {
        return s.replace(sign, "");
    }

    /**
     * Splits a source string into the list of substrings that were separated by whitespace.
     *
     * @param toSplit source string
     * @return split by whitespace
     */
    private static ArrayList<String> splitByWhitespace(String toSplit) {
        return new ArrayList<String>(Arrays.asList(toSplit.trim().split("\\s+")));
    }

}