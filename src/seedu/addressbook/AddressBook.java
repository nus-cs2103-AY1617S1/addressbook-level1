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

public class AddressBook {

	private static enum PersonProperty {
		NAME, EMAIL, PHONE
	};

	private static final int PERSON_DATA_COUNT = 3;

	private static final int DISPLAYED_INDEX_OFFSET = 1;

	private static final char INPUT_COMMENT_MARKER = '#';
	
	private static final Scanner SCANNER = new Scanner(System.in);

	private static final ArrayList<HashMap<String, String>> ALL_PERSONS = new ArrayList<>();

	private static ArrayList<HashMap<String, String>> latestPersonListingView = ALL_PERSONS; // initial
																												// view
																												// is
																												// of
																												// all

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
			String filePath = args[0];
			if (!filePath.endsWith(".txt")) {
				System.out.println("|| " + String.format("The given file name [%1$s] is not a valid file name!", filePath));
				String[] output = new String[3];
				output[0] = "Exiting Address Book... Good bye!";
				output[1] = "===================================================";
				output[2] = "===================================================";
				for (String m1 : output) {
					System.out.println("|| " + m1);
				}
				System.exit(0);
			}
			
			storageFilePath = filePath;
			createFileIfMissing(filePath);
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
			String inputLine = SCANNER.nextLine();
			// silently consume all blank and comment lines
			while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == INPUT_COMMENT_MARKER) {
				inputLine = SCANNER.nextLine();
			}
			String userCommand = inputLine;
			System.out.println("|| " + "[Command entered:" + userCommand + "]");
			String feedback = executeCommand(userCommand);
			String[] output = new String[2];
			output[0] = feedback;
			output[1] = "===================================================";
			for (String m1 : output) {
				System.out.println("|| " + m1);
			}
		}
	}

	public static String executeCommand(String userInputString) {
		final String[] split = userInputString.trim().split("\\s+", 2);
		final String[] commandTypeAndParams = split.length == 2 ? split : new String[] { split[0], "" };
		final String commandType = commandTypeAndParams[0];
		final String commandArgs = commandTypeAndParams[1];
		switch (commandType) {
		case "add":
			// try decoding a person from the raw args
			final Optional<HashMap<String, String>> decodeResult = decodePersonFromString(commandArgs);
			
			if (!decodeResult.isPresent()) {
				return String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", "add",
				String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
								+ String.format("\tParameters: %1$s", "NAME " + "p/" + "PHONE_NUMBER " + "e/" + "EMAIL") + System.lineSeparator() + "|| "
								+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + System.lineSeparator() + "|| ");
			}
			
			// add the person as specified
			final HashMap<String, String> personToAdd = decodeResult.get();
			ALL_PERSONS.add(personToAdd);
			final ArrayList<String> encoded = new ArrayList<>();
			for (HashMap<String, String> person : ALL_PERSONS) {
				encoded.add(String.format("%1$s " + "p/" + "%2$s " + "e/" + "%3$s", getNameFromPerson(person), getPhoneFromPerson(person),
				getEmailFromPerson(person)));
			}
			final ArrayList<String> linesToWrite = encoded;
			try {
				Files.write(Paths.get(storageFilePath), linesToWrite);
			} catch (IOException ioe) {
				System.out.println("|| " + String.format("Unexpected error: unable to write to file: %1$s", storageFilePath));
				String[] output = new String[3];
				output[0] = "Exiting Address Book... Good bye!";
				output[1] = "===================================================";
				output[2] = "===================================================";
				for (String m : output) {
					System.out.println("|| " + m);
				}
				System.exit(0);
			}
			return String.format("New person added: %1$s, Phone: %2$s, Email: %3$s", getNameFromPerson(personToAdd),
			getPhoneFromPerson(personToAdd), getEmailFromPerson(personToAdd));
		case "find":
			final Set<String> keywords = new HashSet<>(splitByWhitespace(commandArgs.trim()));
			final ArrayList<HashMap<String, String>> matchedPersons = new ArrayList<>();
			for (HashMap<String, String> person1 : ALL_PERSONS) {
				final Set<String> wordsInName = new HashSet<>(splitByWhitespace(getNameFromPerson(person1)));
				if (!Collections.disjoint(wordsInName, keywords)) {
					matchedPersons.add(person1);
				}
			}
			final ArrayList<HashMap<String, String>> personsFound = matchedPersons;
			final StringBuilder messageAccumulator = new StringBuilder();
			for (int i = 0; i < personsFound.size(); i++) {
				final HashMap<String, String> person2 = personsFound.get(i);
				final int displayIndex = i + DISPLAYED_INDEX_OFFSET;
				messageAccumulator.append('\t').append(String.format("%1$d. ", displayIndex) + getMessageForFormattedPersonData(person2))
						.append(System.lineSeparator() + "|| ");
			}
			String listAsString = messageAccumulator.toString();
			System.out.println("|| " + listAsString);
			// clone to insulate from future changes to arg list
			latestPersonListingView = new ArrayList<>(personsFound);
			return String.format("%1$d persons found!", personsFound.size());
		case "list":
			ArrayList<HashMap<String, String>> toBeDisplayed = ALL_PERSONS;
			final StringBuilder messageAccumulator1 = new StringBuilder();
			for (int i = 0; i < toBeDisplayed.size(); i++) {
				final HashMap<String, String> person2 = toBeDisplayed.get(i);
				final int displayIndex = i + DISPLAYED_INDEX_OFFSET;
				messageAccumulator1.append('\t').append(String.format("%1$d. ", displayIndex) + getMessageForFormattedPersonData(person2))
						.append(System.lineSeparator() + "|| ");
			}
			String listAsString1 = messageAccumulator1.toString();
			System.out.println("|| " + listAsString1);
			// clone to insulate from future changes to arg list
			latestPersonListingView = new ArrayList<>(toBeDisplayed);
			return String.format("%1$d persons found!", toBeDisplayed.size());
		case "delete":
			if (!isDeletePersonArgsValid(commandArgs)) {
				return String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", "delete",
				String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in " + "the last find/list call.") + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", "INDEX") + System.lineSeparator() + "|| "
				+ String.format("\tExample: %1$s", "delete" + " 1") + System.lineSeparator() + "|| ");
			}
			final int targetVisibleIndex = Integer.parseInt(commandArgs.trim());
			if (!(targetVisibleIndex >= DISPLAYED_INDEX_OFFSET && targetVisibleIndex < latestPersonListingView.size() + DISPLAYED_INDEX_OFFSET)) {
				return "The person index provided is invalid";
			}
			final HashMap<String, String> targetInModel = latestPersonListingView.get(targetVisibleIndex - DISPLAYED_INDEX_OFFSET);
			return deletePersonFromAddressBook(targetInModel) ? String.format("Deleted Person: %1$s", getMessageForFormattedPersonData(targetInModel))
					: "Person could not be found in address book";
		case "clear":
			ALL_PERSONS.clear();
			final ArrayList<String> encoded1 = new ArrayList<>();
			for (HashMap<String, String> person2 : ALL_PERSONS) {
				encoded1.add(String.format("%1$s " + "p/" + "%2$s " + "e/" + "%3$s", getNameFromPerson(person2), getPhoneFromPerson(person2),
				getEmailFromPerson(person2)));
			}
			final ArrayList<String> linesToWrite1 = encoded1;
			try {
				Files.write(Paths.get(storageFilePath), linesToWrite1);
			} catch (IOException ioe) {
				System.out.println("|| " + String.format("Unexpected error: unable to write to file: %1$s", storageFilePath));
				String[] output = new String[3];
				output[0] = "Exiting Address Book... Good bye!";
				output[1] = "===================================================";
				output[2] = "===================================================";
				for (String m : output) {
					System.out.println("|| " + m);
				}
				System.exit(0);
			}
			return "Address book has been cleared!";
		case "help":
			return String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
			+ String.format("\tParameters: %1$s", "NAME " + "p/" + "PHONE_NUMBER " + "e/" + "EMAIL") + System.lineSeparator() + "|| "
			+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| " + String.format("%1$s: %2$s", "find", "Finds all persons whose names contain any of the specified " + "keywords (case-sensitive) and displays them as a list with index numbers.") + System.lineSeparator() + "|| "
			+ String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + System.lineSeparator() + "|| "
			+ String.format("\tExample: %1$s", "find" + " alice bob charlie") + System.lineSeparator() + "|| "
					+ System.lineSeparator() + "|| " + String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "list") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
					+ String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in " + "the last find/list call.") + System.lineSeparator() + "|| "
					+ String.format("\tParameters: %1$s", "INDEX") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "delete" + " 1") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| " + String.format("%1$s: %2$s", "clear", "Clears address book permanently.") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "clear") + System.lineSeparator() + "|| "
					+ System.lineSeparator() + "|| " + String.format("%1$s: %2$s", "exit", "Exits the program.")
					+ String.format("\tExample: %1$s", "exit") + System.lineSeparator() + "|| "
					+ String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
					+ String.format("\tExample: %1$s", "help");
		case "exit":
			String[] output = new String[3];
			output[0] = "Exiting Address Book... Good bye!";
			output[1] = "===================================================";
			output[2] = "===================================================";
			for (String m : output) {
				System.out.println("|| " + m);
			}
			System.exit(0);
		default:
			return String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", commandType,
			String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
			+ String.format("\tParameters: %1$s", "NAME " + "p/" + "PHONE_NUMBER " + "e/" + "EMAIL") + System.lineSeparator() + "|| "
			+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| " + String.format("%1$s: %2$s", "find", "Finds all persons whose names contain any of the specified " + "keywords (case-sensitive) and displays them as a list with index numbers.") + System.lineSeparator() + "|| "
			+ String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + System.lineSeparator() + "|| "
			+ String.format("\tExample: %1$s", "find" + " alice bob charlie") + System.lineSeparator() + "|| "
					+ System.lineSeparator() + "|| " + String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "list") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
					+ String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in " + "the last find/list call.") + System.lineSeparator() + "|| "
					+ String.format("\tParameters: %1$s", "INDEX") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "delete" + " 1") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| " + String.format("%1$s: %2$s", "clear", "Clears address book permanently.") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "clear") + System.lineSeparator() + "|| "
					+ System.lineSeparator() + "|| " + String.format("%1$s: %2$s", "exit", "Exits the program.")
					+ String.format("\tExample: %1$s", "exit") + System.lineSeparator() + "|| "
					+ String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
					+ String.format("\tExample: %1$s", "help"));
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
	 * =========================================== UI LOGIC
	 * ===========================================
	 */

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
			String[] output = new String[3];
			output[0] = "Exiting Address Book... Good bye!";
			output[1] = "===================================================";
			output[2] = "===================================================";
			for (String m : output) {
				System.out.println("|| " + m);
			}
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
	private static ArrayList<HashMap<String, String>> loadPersonsFromFile(String filePath) {
		final Optional<ArrayList<HashMap<String, String>>> successfullyDecoded = decodePersonsFromStrings(
				getLinesInFile(filePath));
		if (!successfullyDecoded.isPresent()) {
			System.out.println("|| " + "Storage file has invalid content");
			String[] output = new String[3];
			output[0] = "Exiting Address Book... Good bye!";
			output[1] = "===================================================";
			output[2] = "===================================================";
			for (String m : output) {
				System.out.println("|| " + m);
			}
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
			String[] output = new String[3];
			output[0] = "Exiting Address Book... Good bye!";
			output[1] = "===================================================";
			output[2] = "===================================================";
			for (String m : output) {
				System.out.println("|| " + m);
			}
			System.exit(0);
		} catch (IOException ioe) {
			System.out.println("|| " + String.format("Unexpected error: unable to read from file: %1$s", filePath));
			String[] output = new String[3];
			output[0] = "Exiting Address Book... Good bye!";
			output[1] = "===================================================";
			output[2] = "===================================================";
			for (String m : output) {
				System.out.println("|| " + m);
			}
			System.exit(0);
		}
		return lines;
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
			final ArrayList<String> encoded = new ArrayList<>();
			for (HashMap<String, String> person : ALL_PERSONS) {
				encoded.add(String.format("%1$s " + "p/" + "%2$s " + "e/" + "%3$s", getNameFromPerson(person), getPhoneFromPerson(person),
				getEmailFromPerson(person)));
			}
			final ArrayList<String> linesToWrite = encoded;
			try {
				Files.write(Paths.get(storageFilePath), linesToWrite);
			} catch (IOException ioe) {
				System.out.println("|| " + String.format("Unexpected error: unable to write to file: %1$s", storageFilePath));
				String[] output = new String[3];
				output[0] = "Exiting Address Book... Good bye!";
				output[1] = "===================================================";
				output[2] = "===================================================";
				for (String m : output) {
					System.out.println("|| " + m);
				}
				System.exit(0);
			}
		}
		return isChanged;
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