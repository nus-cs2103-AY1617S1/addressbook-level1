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
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.HashMap;

/* ==============NOTE TO STUDENTS======================================
 * This class header comment below is brief because details of how to
 * use this class are documented elsewhere.
 * ====================================================================
 */

/**
 * This class is used to maintain a list of person data which are saved in a
 * text file.
 **/
public class AddressBook {
	/*
	 * We use a String array to store details of a single person. The constants
	 * given below are the indexes for the different data elements of a person
	 * used by the internal String[] storage format. For example, a person's
	 * name is stored as the 0th element in the array.
	 */
	private static enum PersonProperty {
		NAME, EMAIL, PHONE
	};

	/**
	 * The number of data elements for a single person.
	 */
	private static final int PERSON_DATA_COUNT = 3;

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
	private static final Scanner SCANNER = new Scanner(System.in);
	/*
	 * ==============NOTE TO
	 * STUDENTS=================================================================
	 * ===== Note that the type of the variable below can also be declared as
	 * List<String[]>, as follows: private static final List<String[]>
	 * ALL_PERSONS = new ArrayList<>() That is because List is an interface
	 * implemented by the ArrayList class. In this code we use ArrayList instead
	 * because we wanted to to stay away from advanced concepts such as
	 * interface inheritance.
	 * =========================================================================
	 * ===========================
	 */
	/**
	 * List of all persons in the address book.
	 */
	private static final ArrayList<HashMap<String, String>> ALL_PERSONS = new ArrayList<>();

	/**
	 * Stores the most recent list of persons shown to the user as a result of a
	 * user command. This is a subset of the full list. Deleting persons in the
	 * pull list does not delete those persons from this list.
	 */
	private static ArrayList<HashMap<String, String>> latestPersonListingView = getAllPersonsInAddressBook(); // initial
																												// view
																												// is
																												// of
																												// all

	/**
	 * The path to the file used for storing person data.
	 */
	private static String storageFilePath;

	public static void main(String[] args) {
		String[] s1 = new String[5];
		s1[0] = "===================================================";
		s1[1] = "===================================================";
		s1[2] = "AddessBook Level 1 - Version 1.0";
		s1[3] = "Welcome to your Address Book!";
		s1[4] = "===================================================";
		for (String m : s1) {
			System.out.println("|| " + m);
		}
		if (args.length >= 2) {
			System.out.println("|| " + "Too many parameters! Correct program argument format:" + System.lineSeparator()
					+ "|| " + "\tjava AddressBook" + System.lineSeparator() + "|| "
					+ "\tjava AddressBook [custom storage file path]");
			String[] s2 = new String[3];
			s2[0] = "Exiting Address Book... Good bye!";
			s2[1] = "===================================================";
			s2[2] = "===================================================";
			for (String m : s2) {
				System.out.println("|| " + m);
			}
			System.exit(0);
		}
		if (args.length == 1) {
			setupGivenFileForStorage(args[0]);
		}

		if (args.length == 0) {
			setupDefaultFileForStorage();
		}

		loadDataFromStorage();
		while (true) {
			String userCommand = getUserInput();
			echoUserCommand(userCommand);
			String feedback = executeCommand(userCommand);
			showResultToUser(feedback);
		}
	}

	private static void showResultToUser(String result) {
		String[] output = new String[2];
		output[0] = result;
		output[1] = "===================================================";
		showToUser(output);
	}

	/*
	 * ==============NOTE TO STUDENTS======================================
	 * Parameter description can be omitted from the method header comment if
	 * the parameter name is self-explanatory. In the method below, '@param
	 * userInput' comment has been omitted.
	 * ====================================================================
	 */
	/**
	 * Echoes the user input back to the user.
	 */
	private static void echoUserCommand(String userCommand) {
		showToUser("[Command entered:" + userCommand + "]");
	}

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
	private static void processProgramArgs(String[] args) {
		if (args.length >= 2) {
			showToUser("Too many parameters! Correct program argument format:" + System.lineSeparator() + "|| "
					+ "\tjava AddressBook" + System.lineSeparator() + "|| "
					+ "\tjava AddressBook [custom storage file path]");
			exitProgram();
		}

		if (args.length == 1) {
			setupGivenFileForStorage(args[0]);
		}

		if (args.length == 0) {
			setupDefaultFileForStorage();
		}
	}

	/**
	 * Sets up the storage file based on the supplied file path. Creates the
	 * file if it is missing. Exits if the file name is not acceptable.
	 */
	private static void setupGivenFileForStorage(String filePath) {

		if (!isValidFilePath(filePath)) {
			showToUser(String.format("The given file name [%1$s] is not a valid file name!", filePath));
			exitProgram();
		}

		storageFilePath = filePath;
		createFileIfMissing(filePath);
	}

	/**
	 * Displays the goodbye message and exits the runtime.
	 */
	private static void exitProgram() {
		String[] output = new String[3];
		output[0] = "Exiting Address Book... Good bye!";
		output[1] = "===================================================";
		output[2] = "===================================================";
		showToUser(output);
		System.exit(0);
	}

	/**
	 * Sets up the storage based on the default file. Creates file if missing.
	 * Exits program if the file cannot be created.
	 */
	private static void setupDefaultFileForStorage() {
		showToUser("Using default storage file : " + "addressbook.txt");
		storageFilePath = "addressbook.txt";
		createFileIfMissing(storageFilePath);
	}

	/**
	 * Returns true if the given file is acceptable. The file path is acceptable
	 * if it ends in '.txt' TODO: Implement a more rigorous validity checking.
	 */
	private static boolean isValidFilePath(String filePath) {
		return filePath.endsWith(".txt");
	}

	/**
	 * Initialises the in-memory data using the storage file. Assumption: The
	 * file exists.
	 */
	private static void loadDataFromStorage() {
		initialiseAddressBookModel(loadPersonsFromFile(storageFilePath));
	}

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
	public static String executeCommand(String userInputString) {
		final String[] commandTypeAndParams = splitCommandWordAndArgs(userInputString);
		final String commandType = commandTypeAndParams[0];
		final String commandArgs = commandTypeAndParams[1];
		switch (commandType) {
		case "add":
			return executeAddPerson(commandArgs);
		case "find":
			return executeFindPersons(commandArgs);
		case "list":
			return executeListAllPersonsInAddressBook();
		case "delete":
			return executeDeletePerson(commandArgs);
		case "clear":
			return executeClearAddressBook();
		case "help":
			return getUsageInfoForAllCommands();
		case "exit":
			executeExitProgramRequest();
		default:
			return getMessageForInvalidCommandInput(commandType, getUsageInfoForAllCommands());
		}
	}

	/**
	 * Splits raw user input into command word and command arguments string
	 *
	 * @return size 2 array; first element is the command type and second
	 *         element is the arguments string
	 */
	private static String[] splitCommandWordAndArgs(String rawUserInput) {
		final String[] split = rawUserInput.trim().split("\\s+", 2);
		return split.length == 2 ? split : new String[] { split[0], "" }; // else
																			// case:
																			// no
																			// parameters
	}

	/**
	 * Constructs a generic feedback message for an invalid command from user,
	 * with instructions for correct usage.
	 *
	 * @param correctUsageInfo
	 *            message showing the correct usage
	 * @return invalid command args feedback message
	 */
	private static String getMessageForInvalidCommandInput(String userCommand, String correctUsageInfo) {
		return String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", userCommand,
				correctUsageInfo);
	}

	/**
	 * Adds a person (specified by the command args) to the address book. The
	 * entire command arguments string is treated as a string representation of
	 * the person to add.
	 *
	 * @param commandArgs
	 *            full command args string from the user
	 * @return feedback display message for the operation result
	 */
	private static String executeAddPerson(String commandArgs) {
		// try decoding a person from the raw args
		final Optional<HashMap<String, String>> decodeResult = decodePersonFromString(commandArgs);

		// checks if args are valid (decode result will not be present if the
		// person is invalid)
		if (!decodeResult.isPresent()) {
			return getMessageForInvalidCommandInput("add", getUsageInfoForAddCommand());
		}

		// add the person as specified
		final HashMap<String, String> personToAdd = decodeResult.get();
		addPersonToAddressBook(personToAdd);
		return getMessageForSuccessfulAddPerson(personToAdd);
	}

	/**
	 * Constructs a feedback message for a successful add person command
	 * execution.
	 *
	 * @see #executeAddPerson(String)
	 * @param addedPerson
	 *            person who was successfully added
	 * @return successful add person feedback message
	 */
	private static String getMessageForSuccessfulAddPerson(HashMap<String, String> addedPerson) {
		return String.format("New person added: %1$s, Phone: %2$s, Email: %3$s", getNameFromPerson(addedPerson),
				getPhoneFromPerson(addedPerson), getEmailFromPerson(addedPerson));
	}

	/**
	 * Finds and lists all persons in address book whose name contains any of
	 * the argument keywords. Keyword matching is case sensitive.
	 *
	 * @param commandArgs
	 *            full command args string from the user
	 * @return feedback display message for the operation result
	 */
	private static String executeFindPersons(String commandArgs) {
		final Set<String> keywords = extractKeywordsFromFindPersonArgs(commandArgs);
		final ArrayList<HashMap<String, String>> personsFound = getPersonsWithNameContainingAnyKeyword(keywords);
		showToUser(personsFound);
		return getMessageForPersonsDisplayedSummary(personsFound);
	}

	/**
	 * Constructs a feedback message to summarise an operation that displayed a
	 * listing of persons.
	 *
	 * @param personsDisplayed
	 *            used to generate summary
	 * @return summary message for persons displayed
	 */
	private static String getMessageForPersonsDisplayedSummary(ArrayList<HashMap<String, String>> personsDisplayed) {
		return String.format("%1$d persons found!", personsDisplayed.size());
	}

	/**
	 * Extract keywords from the command arguments given for the find persons
	 * command.
	 *
	 * @param findPersonCommandArgs
	 *            full command args string for the find persons command
	 * @return set of keywords as specified by args
	 */
	private static Set<String> extractKeywordsFromFindPersonArgs(String findPersonCommandArgs) {
		return new HashSet<>(splitByWhitespace(findPersonCommandArgs.trim()));
	}

	/**
	 * Retrieve all persons in the full model whose names contain some of the
	 * specified keywords.
	 *
	 * @param keywords
	 *            for searching
	 * @return list of persons in full model with name containing some of the
	 *         keywords
	 */
	private static ArrayList<HashMap<String, String>> getPersonsWithNameContainingAnyKeyword(
			Collection<String> keywords) {
		final ArrayList<HashMap<String, String>> matchedPersons = new ArrayList<>();
		for (HashMap<String, String> person : getAllPersonsInAddressBook()) {
			final Set<String> wordsInName = new HashSet<>(splitByWhitespace(getNameFromPerson(person)));
			if (!Collections.disjoint(wordsInName, keywords)) {
				matchedPersons.add(person);
			}
		}
		return matchedPersons;
	}

	/**
	 * Deletes person identified using last displayed index.
	 *
	 * @param commandArgs
	 *            full command args string from the user
	 * @return feedback display message for the operation result
	 */
	private static String executeDeletePerson(String commandArgs) {
		if (!isDeletePersonArgsValid(commandArgs)) {
			return getMessageForInvalidCommandInput("delete", getUsageInfoForDeleteCommand());
		}
		final int targetVisibleIndex = extractTargetIndexFromDeletePersonArgs(commandArgs);
		if (!isDisplayIndexValidForLastPersonListingView(targetVisibleIndex)) {
			return "The person index provided is invalid";
		}
		final HashMap<String, String> targetInModel = getPersonByLastVisibleIndex(targetVisibleIndex);
		return deletePersonFromAddressBook(targetInModel) ? getMessageForSuccessfulDelete(targetInModel)
				: "Person could not be found in address book";
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

	/**
	 * Extracts the target's index from the raw delete person args string
	 *
	 * @param rawArgs
	 *            raw command args string for the delete person command
	 * @return extracted index
	 */
	private static int extractTargetIndexFromDeletePersonArgs(String rawArgs) {
		return Integer.parseInt(rawArgs.trim());
	}

	/**
	 * Checks that the given index is within bounds and valid for the last shown
	 * person list view.
	 *
	 * @param index
	 *            to check
	 * @return whether it is valid
	 */
	private static boolean isDisplayIndexValidForLastPersonListingView(int index) {
		return index >= DISPLAYED_INDEX_OFFSET && index < getLatestPersonListingView().size() + DISPLAYED_INDEX_OFFSET;
	}

	/**
	 * Constructs a feedback message for a successful delete person command
	 * execution.
	 *
	 * @see #executeDeletePerson(String)
	 * @param deletedPerson
	 *            successfully deleted
	 * @return successful delete person feedback message
	 */
	private static String getMessageForSuccessfulDelete(HashMap<String, String> deletedPerson) {
		return String.format("Deleted Person: %1$s", getMessageForFormattedPersonData(deletedPerson));
	}

	/**
	 * Clears all persons in the address book.
	 *
	 * @return feedback display message for the operation result
	 */
	private static String executeClearAddressBook() {
		clearAddressBook();
		return "Address book has been cleared!";
	}

	/**
	 * Displays all persons in the address book to the user; in added order.
	 *
	 * @return feedback display message for the operation result
	 */
	private static String executeListAllPersonsInAddressBook() {
		ArrayList<HashMap<String, String>> toBeDisplayed = getAllPersonsInAddressBook();
		showToUser(toBeDisplayed);
		return getMessageForPersonsDisplayedSummary(toBeDisplayed);
	}

	/**
	 * Request to terminate the program.
	 *
	 * @return feedback display message for the operation result
	 */
	private static void executeExitProgramRequest() {
		exitProgram();
	}

	/*
	 * =========================================== UI LOGIC
	 * ===========================================
	 */

	/**
	 * Prompts for the command and reads the text entered by the user. Ignores
	 * lines with first non-whitespace char equal to
	 * {@link #INPUT_COMMENT_MARKER} (considered comments)
	 *
	 * @return full line entered by the user
	 */
	private static String getUserInput() {
		System.out.print("|| " + "Enter command: ");
		String inputLine = SCANNER.nextLine();
		// silently consume all blank and comment lines
		while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == INPUT_COMMENT_MARKER) {
			inputLine = SCANNER.nextLine();
		}
		return inputLine;
	}

	/*
	 * ==============NOTE TO STUDENTS====================================== Note
	 * how the method below uses Java 'Varargs' feature so that the method can
	 * accept a varying number of message parameters.
	 * ====================================================================
	 */
	/**
	 * Shows a message to the user
	 */
	private static void showToUser(String[] message) {
		for (String m : message) {
			System.out.println("|| " + m);
		}
	}

	/**
	 * Shows a message to the user.
	 */
	private static void showToUser(String message) {
		System.out.println("|| " + message);
	}

	/**
	 * Shows the list of persons to the user. The list will be indexed, starting
	 * from 1.
	 *
	 */
	private static void showToUser(ArrayList<HashMap<String, String>> persons) {
		String listAsString = getDisplayString(persons);
		showToUser(listAsString);
		updateLatestViewedPersonListing(persons);
	}

	/**
	 * Returns the display string representation of the list of persons.
	 */
	private static String getDisplayString(ArrayList<HashMap<String, String>> persons) {
		final StringBuilder messageAccumulator = new StringBuilder();
		for (int i = 0; i < persons.size(); i++) {
			final HashMap<String, String> person = persons.get(i);
			final int displayIndex = i + DISPLAYED_INDEX_OFFSET;
			messageAccumulator.append('\t').append(getIndexedPersonListElementMessage(displayIndex, person))
					.append(System.lineSeparator() + "|| ");
		}
		return messageAccumulator.toString();
	}

	/**
	 * Constructs a prettified listing element message to represent a person and
	 * their data.
	 *
	 * @param visibleIndex
	 *            visible index for this listing
	 * @param person
	 *            to show
	 * @return formatted listing message with index
	 */
	private static String getIndexedPersonListElementMessage(int visibleIndex, HashMap<String, String> person) {
		return String.format("%1$d. ", visibleIndex) + getMessageForFormattedPersonData(person);
	}

	/**
	 * Constructs a prettified string to show the user a person's data.
	 *
	 * @param person
	 *            to show
	 * @return formatted message showing internal state
	 */
	private static String getMessageForFormattedPersonData(HashMap<String, String> person) {
		return String.format("%1$s  Phone Number: %2$s  Email: %3$s", getNameFromPerson(person),
				getPhoneFromPerson(person), getEmailFromPerson(person));
	}

	/**
	 * Updates the latest person listing view the user has seen.
	 *
	 * @param newListing
	 *            the new listing of persons
	 */
	private static void updateLatestViewedPersonListing(ArrayList<HashMap<String, String>> newListing) {
		// clone to insulate from future changes to arg list
		latestPersonListingView = new ArrayList<>(newListing);
	}

	/**
	 * Retrieves the person identified by the displayed index from the last
	 * shown listing of persons.
	 *
	 * @param lastVisibleIndex
	 *            displayed index from last shown person listing
	 * @return the actual person object in the last shown person listing
	 */
	private static HashMap<String, String> getPersonByLastVisibleIndex(int lastVisibleIndex) {
		return latestPersonListingView.get(lastVisibleIndex - DISPLAYED_INDEX_OFFSET);
	}

	/**
	 * @return unmodifiable list view of the last person listing view
	 */
	private static ArrayList<HashMap<String, String>> getLatestPersonListingView() {
		return latestPersonListingView;
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

		showToUser(String.format("Storage file missing: %1$s", filePath));

		try {
			storageFile.createNewFile();
			showToUser(String.format("Created new empty storage file: %1$s", filePath));
		} catch (IOException ioe) {
			showToUser(String.format("Error: unable to create file: %1$s", filePath));
			exitProgram();
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
	private static ArrayList<HashMap<String, String>> loadPersonsFromFile(String filePath) {
		final Optional<ArrayList<HashMap<String, String>>> successfullyDecoded = decodePersonsFromStrings(
				getLinesInFile(filePath));
		if (!successfullyDecoded.isPresent()) {
			showToUser("Storage file has invalid content");
			exitProgram();
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
			showToUser(String.format("Storage file missing: %1$s", filePath));
			exitProgram();
		} catch (IOException ioe) {
			showToUser(String.format("Unexpected error: unable to read from file: %1$s", filePath));
			exitProgram();
		}
		return lines;
	}

	/**
	 * Saves all data to the file. Exits program if there is an error saving to
	 * file.
	 *
	 * @param filePath
	 *            file for saving
	 */
	private static void savePersonsToFile(ArrayList<HashMap<String, String>> persons, String filePath) {
		final ArrayList<String> linesToWrite = encodePersonsToStrings(persons);
		try {
			Files.write(Paths.get(storageFilePath), linesToWrite);
		} catch (IOException ioe) {
			showToUser(String.format("Unexpected error: unable to write to file: %1$s", filePath));
			exitProgram();
		}
	}

	/*
	 * =========================================================================
	 * ======= INTERNAL ADDRESS BOOK DATA METHODS
	 * =========================================================================
	 * =======
	 */

	/**
	 * Adds a person to the address book. Saves changes to storage file.
	 *
	 * @param person
	 *            to add
	 */
	private static void addPersonToAddressBook(HashMap<String, String> person) {
		ALL_PERSONS.add(person);
		savePersonsToFile(getAllPersonsInAddressBook(), storageFilePath);
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
		delete(index);
		savePersonsToFile(getAllPersonsInAddressBook(), storageFilePath);
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
	private static boolean deletePersonFromAddressBook(HashMap<String, String> exactPerson) {
		final boolean isChanged = ALL_PERSONS.remove(exactPerson);
		if (isChanged) {
			savePersonsToFile(getAllPersonsInAddressBook(), storageFilePath);
		}
		return isChanged;
	}

	/**
	 * @return unmodifiable list view of all persons in the address book
	 */
	private static ArrayList<HashMap<String, String>> getAllPersonsInAddressBook() {
		return ALL_PERSONS;
	}

	/**
	 * Clears all persons in the address book and saves changes to file.
	 */
	private static void clearAddressBook() {
		delete();
		savePersonsToFile(getAllPersonsInAddressBook(), storageFilePath);
	}

	/**
	 * Resets the internal model with the given data. Does not save to file.
	 *
	 * @param persons
	 *            list of persons to initialise the model with
	 */
	private static void initialiseAddressBookModel(ArrayList<HashMap<String, String>> persons) {
		delete();
		ALL_PERSONS.addAll(persons);
	}

	private static void delete() {
		ALL_PERSONS.clear();
	}

	private static void delete(int index) {
		ALL_PERSONS.remove(index);
	}

	/*
	 * =========================================== PERSON METHODS
	 * ===========================================
	 */

	/**
	 * @param person
	 *            whose name you want
	 * @return person's name
	 */
	private static String getNameFromPerson(HashMap<String, String> person) {
		return person.get(PersonProperty.NAME.toString());
	}

	/**
	 * @param person
	 *            whose phone number you want
	 * @return person's phone number
	 */
	private static String getPhoneFromPerson(HashMap<String, String> person) {
		return person.get(PersonProperty.PHONE.toString());
	}

	/**
	 * @param person
	 *            whose email you want
	 * @return person's email
	 */
	private static String getEmailFromPerson(HashMap<String, String> person) {
		return person.get(PersonProperty.EMAIL.toString());
	}

	/**
	 * Create a person for use in the internal data.
	 *
	 * @param name
	 *            of person
	 * @param phone
	 *            without data prefix
	 * @param email
	 *            without data prefix
	 * @return constructed person
	 */
	private static HashMap<String, String> makePersonFromData(String name, String phone, String email) {
		final HashMap<String, String> person = new HashMap<String, String>(PERSON_DATA_COUNT);
		person.put(PersonProperty.NAME.toString(), name);
		person.put(PersonProperty.PHONE.toString(), phone);
		person.put(PersonProperty.EMAIL.toString(), email);
		return person;
	}

	/**
	 * Encodes a person into a decodable and readable string representation.
	 *
	 * @param person
	 *            to be encoded
	 * @return encoded string
	 */
	private static String encodePersonToString(HashMap<String, String> person) {
		return String.format("%1$s " + "p/" + "%2$s " + "e/" + "%3$s", getNameFromPerson(person), getPhoneFromPerson(person),
				getEmailFromPerson(person));
	}

	/**
	 * Encodes list of persons into list of decodable and readable string
	 * representations.
	 *
	 * @param persons
	 *            to be encoded
	 * @return encoded strings
	 */
	private static ArrayList<String> encodePersonsToStrings(ArrayList<HashMap<String, String>> persons) {
		final ArrayList<String> encoded = new ArrayList<>();
		for (HashMap<String, String> person : persons) {
			encoded.add(encodePersonToString(person));
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
	private static Optional<HashMap<String, String>> decodePersonFromString(String encoded) {
		// check that we can extract the parts of a person from the encoded
		// string
		if (!isPersonDataExtractableFrom(encoded)) {
			return Optional.empty();
		}
		final HashMap<String, String> decodedPerson = makePersonFromData(extractNameFromPersonString(encoded),
				extractPhoneFromPersonString(encoded), extractEmailFromPersonString(encoded));
		// check that the constructed person is valid
		return isPersonDataValid(decodedPerson) ? Optional.of(decodedPerson) : Optional.empty();
	}

	/**
	 * Decode persons from a list of string representations.
	 *
	 * @param encodedPersons
	 *            strings to be decoded
	 * @return if cannot decode any: empty Optional else: Optional containing
	 *         decoded persons
	 */
	private static Optional<ArrayList<HashMap<String, String>>> decodePersonsFromStrings(
			ArrayList<String> encodedPersons) {
		final ArrayList<HashMap<String, String>> decodedPersons = new ArrayList<>();
		for (String encodedPerson : encodedPersons) {
			final Optional<HashMap<String, String>> decodedPerson = decodePersonFromString(encodedPerson);
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
			return removePrefixSign(encoded.substring(indexOfPhonePrefix, encoded.length()).trim(),
					"p/");

			// phone is middle arg, target is from own prefix to next prefix
		} else {
			return removePrefixSign(encoded.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim(),
					"p/");
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
			return removePrefixSign(encoded.substring(indexOfEmailPrefix, encoded.length()).trim(),
					"e/");

			// email is middle arg, target is from own prefix to next prefix
		} else {
			return removePrefixSign(encoded.substring(indexOfEmailPrefix, indexOfPhonePrefix).trim(),
					"e/");
		}
	}

	/**
	 * Validates a person's data fields
	 *
	 * @param person
	 *            String array representing the person (used in internal data)
	 * @return whether the given person has valid data
	 */
	private static boolean isPersonDataValid(HashMap<String, String> person) {
		return isPersonNameValid(person.get(PersonProperty.NAME.toString()))
				&& isPersonPhoneValid(person.get(PersonProperty.PHONE.toString()))
				&& isPersonEmailValid(person.get(PersonProperty.EMAIL.toString()));
	}

	/*
	 * ==============NOTE TO STUDENTS====================================== Note
	 * the use of 'regular expressions' in the method below. Regular expressions
	 * can be very useful in checking if a a string follows a sepcific format.
	 * ====================================================================
	 */
	/**
	 * Validates string as a legal person name
	 *
	 * @param name
	 *            to be validated
	 * @return whether arg is a valid person name
	 */
	private static boolean isPersonNameValid(String name) {
		return name.matches("(\\w|\\s)+"); // name is nonempty mixture of
											// alphabets and whitespace
		// TODO: implement a more permissive validation
	}

	/**
	 * Validates string as a legal person phone number
	 *
	 * @param phone
	 *            to be validated
	 * @return whether arg is a valid person phone number
	 */
	private static boolean isPersonPhoneValid(String phone) {
		return phone.matches("\\d+"); // phone nonempty sequence of digits
		// TODO: implement a more permissive validation
	}

	/**
	 * Validates string as a legal person email
	 *
	 * @param email
	 *            to be validated
	 * @return whether arg is a valid person email
	 */
	private static boolean isPersonEmailValid(String email) {
		return email.matches("\\S+@\\S+\\.\\S+"); // email is
													// [non-whitespace]@[non-whitespace].[non-whitespace]
		// TODO: implement a more permissive validation
	}

	/*
	 * =============================================== COMMAND HELP INFO FOR
	 * USERS ===============================================
	 */

	/**
	 * @return Usage info for all commands
	 */
	private static String getUsageInfoForAllCommands() {
		return getUsageInfoForAddCommand() + System.lineSeparator() + "|| " + getUsageInfoForFindCommand()
				+ System.lineSeparator() + "|| " + getUsageInfoForViewCommand() + System.lineSeparator() + "|| "
				+ getUsageInfoForDeleteCommand() + System.lineSeparator() + "|| " + getUsageInfoForClearCommand()
				+ System.lineSeparator() + "|| " + getUsageInfoForExitCommand() + System.lineSeparator() + "|| "
				+ getUsageInfoForHelpCommand();
	}

	/**
	 * Builds string for showing 'add' command usage instruction
	 *
	 * @return 'add' command usage instruction
	 */
	private static String getUsageInfoForAddCommand() {
		return String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", "NAME " + "p/" + "PHONE_NUMBER " + "e/" + "EMAIL") + System.lineSeparator() + "|| "
				+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + System.lineSeparator() + "|| ";
	}

	/**
	 * Builds string for showing 'find' command usage instruction
	 *
	 * @return 'find' command usage instruction
	 */
	private static String getUsageInfoForFindCommand() {
		return String.format("%1$s: %2$s", "find", "Finds all persons whose names contain any of the specified " + "keywords (case-sensitive) and displays them as a list with index numbers.") + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + System.lineSeparator() + "|| "
				+ String.format("\tExample: %1$s", "find" + " alice bob charlie") + System.lineSeparator() + "|| ";
	}

	/**
	 * Builds string for showing 'delete' command usage instruction
	 *
	 * @return 'delete' command usage instruction
	 */
	private static String getUsageInfoForDeleteCommand() {
		return String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in " + "the last find/list call.") + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", "INDEX") + System.lineSeparator() + "|| "
				+ String.format("\tExample: %1$s", "delete" + " 1") + System.lineSeparator() + "|| ";
	}

	/**
	 * Builds string for showing 'clear' command usage instruction
	 *
	 * @return 'clear' command usage instruction
	 */
	private static String getUsageInfoForClearCommand() {
		return String.format("%1$s: %2$s", "clear", "Clears address book permanently.") + System.lineSeparator() + "|| "
				+ String.format("\tExample: %1$s", "clear") + System.lineSeparator() + "|| ";
	}

	/**
	 * Builds string for showing 'view' command usage instruction
	 *
	 * @return 'view' command usage instruction
	 */
	private static String getUsageInfoForViewCommand() {
		return String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + System.lineSeparator() + "|| "
				+ String.format("\tExample: %1$s", "list") + System.lineSeparator() + "|| ";
	}

	/**
	 * Builds string for showing 'help' command usage instruction
	 *
	 * @return 'help' command usage instruction
	 */
	private static String getUsageInfoForHelpCommand() {
		return String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
				+ String.format("\tExample: %1$s", "help");
	}

	/**
	 * Builds string for showing 'exit' command usage instruction
	 *
	 * @return 'exit' command usage instruction
	 */
	private static String getUsageInfoForExitCommand() {
		return String.format("%1$s: %2$s", "exit", "Exits the program.")
				+ String.format("\tExample: %1$s", "exit");
	}

	/*
	 * ============================ UTILITY METHODS ============================
	 */

	/**
	 * Removes sign(p/, d/, etc) from parameter string
	 *
	 * @param s
	 *            Parameter as a string
	 * @param sign
	 *            Parameter sign to be removed
	 *
	 * @return Priority string without p/
	 */
	private static String removePrefixSign(String s, String sign) {
		return s.replace(sign, "");
	}

	/**
	 * Splits a source string into the list of substrings that were separated by
	 * whitespace.
	 *
	 * @param toSplit
	 *            source string
	 * @return split by whitespace
	 */
	private static ArrayList<String> splitByWhitespace(String toSplit) {
		return new ArrayList(Arrays.asList(toSplit.trim().split("\\s+")));
	}

}