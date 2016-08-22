package seedu.addressbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AddressBook {
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final ArrayList<String[]> ALL_PERSONS = new ArrayList<>();
    private static ArrayList<String[]> latestPersonListingView = ALL_PERSONS;
    private static String storageFilePath;
    public static void main(String[] args) {
        showToUser("===================================================", "===================================================", "AddessBook Level 1 - Version 1.0", "Welcome to your Address Book!", "===================================================");
        if (args.length >= 2) {
		    showToUser("Too many parameters! Correct program argument format:"
			                                                        + System.lineSeparator() + "|| " + "\tjava AddressBook"
			                                                        + System.lineSeparator() + "|| " + "\tjava AddressBook [custom storage file path]");
		    showToUser("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
		}
		
		if (args.length == 1) {
		    String filePath = args[0];
			if (!filePath.endsWith(".txt")) {
			    showToUser(String.format("The given file name [%1$s] is not a valid file name!", filePath));
			    showToUser("Exiting Address Book... Good bye!", "===================================================", "===================================================");
				System.exit(0);
			}
			
			storageFilePath = filePath;
			createFileIfMissing(filePath);
		}
		
		if(args.length == 0) {
		    showToUser("Using default storage file : " + "addressbook.txt");
			storageFilePath = "addressbook.txt";
			createFileIfMissing(storageFilePath);
		}
        ALL_PERSONS.clear();
		ALL_PERSONS.addAll(loadPersonsFromFile(storageFilePath));
        while (true) {
            System.out.print("|| " + "Enter command: ");
			String inputLine = SCANNER.nextLine();
			while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == '#') {
			    inputLine = SCANNER.nextLine();
			}
			String userCommand = inputLine;
            showToUser("[Command entered:" + userCommand + "]");
            String feedback = executeCommand(userCommand);
            showToUser(feedback, "===================================================");
        }
    }
    public static String executeCommand(String userInputString) {
        final String[] split =  userInputString.trim().split("\\s+", 2);
		final String[] commandTypeAndParams = split.length == 2 ? split : new String[] { split[0] , "" };
        final String commandType = commandTypeAndParams[0];
        final String commandArgs = commandTypeAndParams[1];
        switch (commandType) {
        case "add":
            final Optional<String[]> decodeResult = decodePersonFromString(commandArgs);
			if (!decodeResult.isPresent()) {
			    return String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", "add", String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", "NAME "
				                                                  + "p/" + "PHONE_NUMBER "
				                                                  + "e/" + "EMAIL") + System.lineSeparator() + "|| "
				+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + System.lineSeparator() + "|| ");
			}
			
			final String[] personToAdd = decodeResult.get();
			ALL_PERSONS.add(personToAdd);
			final ArrayList<String> encoded = new ArrayList<>();
			for (String[] person1 : ALL_PERSONS) {
			    encoded.add(String.format("%1$s "
				                                                + "p/" + "%2$s " 
				                                                + "e/" + "%3$s",
				person1[0], person1[1], person1[2]));
			}
			final ArrayList<String> linesToWrite = encoded;
			try {
			    Files.write(Paths.get(storageFilePath), linesToWrite);
			} catch (IOException ioe) {
			    showToUser(String.format("Unexpected error: unable to write to file: %1$s", storageFilePath));
			    showToUser("Exiting Address Book... Good bye!", "===================================================", "===================================================");
				System.exit(0);
			}
			return String.format("New person added: %1$s, Phone: %2$s, Email: %3$s",
			personToAdd[0], personToAdd[1], personToAdd[2]);
        case "find":
            final Set<String> keywords = new HashSet<>((ArrayList<String>) new ArrayList(Arrays.asList(commandArgs.trim().trim().split("\\s+"))));
			final ArrayList<String[]> matchedPersons = new ArrayList<>();
			for (String[] person : ALL_PERSONS) {
			    final Set<String> wordsInName = new HashSet<>((ArrayList<String>) new ArrayList(Arrays.asList(person[0].trim().split("\\s+"))));
			    if (!Collections.disjoint(wordsInName, keywords)) {
			        matchedPersons.add(person);
			    }
			}
			final ArrayList<String[]> personsFound = matchedPersons;
			showToUser(personsFound);
			return String.format("%1$d persons found!", personsFound.size());
        case "list":
            ArrayList<String[]> toBeDisplayed = ALL_PERSONS;
			showToUser(toBeDisplayed);
			return String.format("%1$d persons found!", toBeDisplayed.size());
        case "delete":
            if (!isDeletePersonArgsValid(commandArgs)) {
			    return String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", "delete", String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in "
				                                                + "the last find/list call.") + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", "INDEX") + System.lineSeparator() + "|| "
				+ String.format("\tExample: %1$s", "delete" + " 1") + System.lineSeparator() + "|| ");
			}
			final int targetVisibleIndex = Integer.parseInt(commandArgs.trim());
			if (!(targetVisibleIndex >= 1 && targetVisibleIndex < latestPersonListingView.size() + 1)) {
			    return "The person index provided is invalid";
			}
			final String[] targetInModel = latestPersonListingView.get(targetVisibleIndex - 1);
			return deletePersonFromAddressBook(targetInModel) ? String.format("Deleted Person: %1$s", String.format("%1$s  Phone Number: %2$s  Email: %3$s",
			targetInModel[0], targetInModel[1], targetInModel[2]))
			                                                  : "Person could not be found in address book";
        case "clear":
            ALL_PERSONS.clear();
			final ArrayList<String> encoded1 = new ArrayList<>();
			for (String[] person2 : ALL_PERSONS) {
			    encoded1.add(String.format("%1$s "
				                                                + "p/" + "%2$s " 
				                                                + "e/" + "%3$s",
				person2[0], person2[1], person2[2]));
			}
			final ArrayList<String> linesToWrite1 = encoded1;
			try {
			    Files.write(Paths.get(storageFilePath), linesToWrite1);
			} catch (IOException ioe) {
			    showToUser(String.format("Unexpected error: unable to write to file: %1$s", storageFilePath));
			    showToUser("Exiting Address Book... Good bye!", "===================================================", "===================================================");
				System.exit(0);
			}
			return "Address book has been cleared!";
        case "help":
            return String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
			+ String.format("\tParameters: %1$s", "NAME "
			                                                  + "p/" + "PHONE_NUMBER "
			                                                  + "e/" + "EMAIL") + System.lineSeparator() + "|| "
			+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "find", "Finds all persons whose names contain any of the specified "
					                                    + "keywords (case-sensitive) and displays them as a list with index numbers.") + System.lineSeparator() + "|| "
					+ String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "find" + " alice bob charlie") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "list") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in "
					                                                + "the last find/list call.") + System.lineSeparator() + "|| "
					+ String.format("\tParameters: %1$s", "INDEX") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "delete" + " 1") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "clear", "Clears address book permanently.") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "clear") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "exit", "Exits the program.")
					+ String.format("\tExample: %1$s", "exit") + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
					+ String.format("\tExample: %1$s", "help");
        case "exit":
            showToUser("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
        default:
            return String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", commandType, String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
			+ String.format("\tParameters: %1$s", "NAME "
			                                                  + "p/" + "PHONE_NUMBER "
			                                                  + "e/" + "EMAIL") + System.lineSeparator() + "|| "
			+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "find", "Finds all persons whose names contain any of the specified "
					                                    + "keywords (case-sensitive) and displays them as a list with index numbers.") + System.lineSeparator() + "|| "
					+ String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "find" + " alice bob charlie") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "list") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in "
					                                                + "the last find/list call.") + System.lineSeparator() + "|| "
					+ String.format("\tParameters: %1$s", "INDEX") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "delete" + " 1") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "clear", "Clears address book permanently.") + System.lineSeparator() + "|| "
					+ String.format("\tExample: %1$s", "clear") + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "exit", "Exits the program.")
					+ String.format("\tExample: %1$s", "exit") + System.lineSeparator() + "|| "
			        + String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
					+ String.format("\tExample: %1$s", "help"));
        }
    }
    private static boolean isDeletePersonArgsValid(String rawArgs) {
        try {
            return Integer.parseInt(rawArgs.trim()) >= 1;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
    private static void showToUser(String... message) {
        for (String m : message) {
            System.out.println("|| " + m);
        }
    }
    private static void showToUser(ArrayList<String[]> persons) {
        final StringBuilder messageAccumulator = new StringBuilder();
		for (int i = 0; i < persons.size(); i++) {
		    final String[] person = persons.get(i);
		    final int displayIndex = i + 1;
		    messageAccumulator.append('\t')
		                      .append(String.format("%1$d. ", displayIndex) + String.format("%1$s  Phone Number: %2$s  Email: %3$s",
							person[0], person[1], person[2]))
		                      .append(System.lineSeparator() + "|| ");
		}
		String listAsString = messageAccumulator.toString();
        showToUser(listAsString);
        latestPersonListingView = new ArrayList<>(persons);
    }
    private static void createFileIfMissing(String filePath) {
        if (new File(filePath).exists()) {
            return;
        }
        showToUser(String.format("Storage file missing: %1$s", filePath));
        try {
            new File(filePath).createNewFile();
            showToUser(String.format("Created new empty storage file: %1$s", filePath));
        } catch (IOException ioe) {
            showToUser(String.format("Error: unable to create file: %1$s", filePath));
            showToUser("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
        }
    }
    private static ArrayList<String[]> loadPersonsFromFile(String filePath) {
        final Optional<ArrayList<String[]>> successfullyDecoded = decodePersonsFromStrings(getLinesInFile(filePath));
        if (!successfullyDecoded.isPresent()) {
            showToUser("Storage file has invalid content");
            showToUser("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
        }
        return successfullyDecoded.get();
    }
    private static ArrayList<String> getLinesInFile(String filePath) {
        ArrayList<String> lines = null;
        try {
            lines = new ArrayList(Files.readAllLines(Paths.get(filePath)));
        } catch (FileNotFoundException fnfe) {
            showToUser(String.format("Storage file missing: %1$s", filePath));
            showToUser("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
        } catch (IOException ioe) {
            showToUser(String.format("Unexpected error: unable to read from file: %1$s", filePath));
            showToUser("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
        }
        return lines;
    }

    private static boolean deletePersonFromAddressBook(String[] exactPerson) {
        final boolean changed = ALL_PERSONS.remove(exactPerson);
        if (changed) {
            final ArrayList<String> encoded = new ArrayList<>();
			for (String[] person : ALL_PERSONS) {
			    encoded.add(String.format("%1$s " 
				                                                + "p/" + "%2$s " 
				                                                + "e/" + "%3$s",
				person[0], person[1], person[2]));
			}
			final ArrayList<String> linesToWrite = encoded;
			try {
			    Files.write(Paths.get(storageFilePath), linesToWrite);
			} catch (IOException ioe) {
			    showToUser(String.format("Unexpected error: unable to write to file: %1$s", storageFilePath));
			    showToUser("Exiting Address Book... Good bye!", "===================================================", "===================================================");
				System.exit(0);
			}
        }
        return changed;
    }
    private static Optional<String[]> decodePersonFromString(String encoded) {
        if (!isPersonDataExtractableFrom(encoded)) {
            return Optional.empty();
        }
		final String[] person = new String[3];
		person[0] = extractNameFromPersonString(encoded);
		person[1] = extractPhoneFromPersonString(encoded);
		person[2] = extractEmailFromPersonString(encoded);
        final String[] decodedPerson = person;
        return decodedPerson[0].matches("(\\w|\\s)+")
		&& decodedPerson[1].matches("\\d+")
		&& decodedPerson[2].matches("\\S+@\\S+\\.\\S+") ? Optional.of(decodedPerson) : Optional.empty();
    }
    private static Optional<ArrayList<String[]>> decodePersonsFromStrings(ArrayList<String> encodedPersons) {
        final ArrayList<String[]> decodedPersons = new ArrayList<>();
        for (String encodedPerson : encodedPersons) {
            final Optional<String[]> decodedPerson = decodePersonFromString(encodedPerson);
            if (!decodedPerson.isPresent()) {
                return Optional.empty();
            }
            decodedPersons.add(decodedPerson.get());
        }
        return Optional.of(decodedPersons);
    }
    private static boolean isPersonDataExtractableFrom(String personData) {
        final String[] splitArgs = personData.trim().split("p/" + '|' + "e/");
        return splitArgs.length == 3
                && !splitArgs[0].isEmpty() 
                && !splitArgs[1].isEmpty()
                && !splitArgs[2].isEmpty();
    }
    private static String extractNameFromPersonString(String encoded) {
        int indexOfFirstPrefix = Math.min(encoded.indexOf("e/"), encoded.indexOf("p/"));
        return encoded.substring(0, indexOfFirstPrefix).trim();
    }
    private static String extractPhoneFromPersonString(String encoded) {
        if (encoded.indexOf("p/") > encoded.indexOf("e/")) {
            return encoded.substring(encoded.indexOf("p/"), encoded.length()).trim().replace("p/", "");
        } else {
            return encoded.substring(encoded.indexOf("p/"), encoded.indexOf("e/")).trim().replace("p/", "");
        }
    }
    private static String extractEmailFromPersonString(String encoded) {
        if (encoded.indexOf("e/") > encoded.indexOf("p/")) {
            return encoded.substring(encoded.indexOf("e/"), encoded.length()).trim().replace("e/", "");
        } else {
            return encoded.substring(encoded.indexOf("e/"), encoded.indexOf("p/")).trim().replace("e/", "");
        }
    }

}