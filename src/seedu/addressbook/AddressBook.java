package seedu.addressbook;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Optional;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Scanner;
import java.util.Set;

public class AddressBook {

    private static final String v = "AddessBook Level 1 - Version 1.0";

    private static final String Ls = System.lineSeparator() + "|| ";

    private static final String Ld = "===================================================";

    private static final Scanner _PRINTER = new Scanner(System.in);

    private static final ArrayList<String[]> LIST = new ArrayList<>();

    private static ArrayList<String[]> vIEW = LIST; 

    private static String ROAd;

    public static void main(String[] args) {
        String[] message = { Ld, Ld, v, "Welcome to your Address Book!", Ld };
		for (String m : message) {
		    System.out.println("|| " + m);
		}
        
        if (args.length >= 2) {
		    String[] message1 = { "Too many parameters! Correct program argument format:"
			                                                        + Ls + "\tjava AddressBook"
			                                                        + Ls + "\tjava AddressBook [custom storage file path]" };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
		    String[] message2 = { "Exiting Address Book... Good bye!", Ld, Ld };
			for (String m2 : message2) {
			    System.out.println("|| " + m2);
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
			    String[] message2 = { "Exiting Address Book... Good bye!", Ld, Ld };
				for (String m2 : message2) {
				    System.out.println("|| " + m2);
				}
				System.exit(0);
			}
			
			ROAd = filePath;
			GiveMeFile(filePath);
		}
		
		if(args.length == 0) {
		    String[] message1 = { "Using default storage file : " + "addressbook.txt" };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
			ROAd = "addressbook.txt";
			GiveMeFile(ROAd);
		}
		
		LIST.clear();
		LIST.addAll(Load(ROAd));
        while (true) {
            System.out.print("|| " + "Enter command: ");
			String inputLine = _PRINTER.nextLine();
			// silently consume all blank and comment lines
			while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == '#') {
			    inputLine = _PRINTER.nextLine();
			}
			String userCommand = inputLine;
            String[] message1 = { "[Command entered:" + userCommand + "]" };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
            String feedback = Run(userCommand);
            String[] message2 = { feedback, Ld };
			for (String m2 : message2) {
			    System.out.println("|| " + m2);
			}
        }
    }

    private static void WElcome() {
        String[] message = { Ld, Ld, v, "Welcome to your Address Book!", Ld };
		for (String m : message) {
		    System.out.println("|| " + m);
		}
    }

    private static void UserSee(String result) {
        String[] message = { result, Ld };
		for (String m : message) {
		    System.out.println("|| " + m);
		}
    }

    private static void processStuff(String[] args) {
        if (args.length >= 2) {
            String[] message = { "Too many parameters! Correct program argument format:"
			                                                        + Ls + "\tjava AddressBook"
			                                                        + Ls + "\tjava AddressBook [custom storage file path]" };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
            String[] message1 = { "Exiting Address Book... Good bye!", Ld, Ld };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
        }

        if (args.length == 1) {
            String filePath = args[0];
			if (!filePath.endsWith(".txt")) {
			    String[] message = { String.format("The given file name [%1$s] is not a valid file name!", filePath) };
				for (String m : message) {
				    System.out.println("|| " + m);
				}
			    String[] message1 = { "Exiting Address Book... Good bye!", Ld, Ld };
				for (String m1 : message1) {
				    System.out.println("|| " + m1);
				}
				System.exit(0);
			}
			
			ROAd = filePath;
			GiveMeFile(filePath);
        }

        if(args.length == 0) {
            String[] message = { "Using default storage file : " + "addressbook.txt" };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
			ROAd = "addressbook.txt";
			GiveMeFile(ROAd);
        }
    }

    public static String Run(String userInputString) {
        switch (Split(userInputString)[0]) {
        case "add":
            return Add(Split(userInputString)[1]);
        case "find":
            return Find(Split(userInputString)[1]);
        case "list":
            return ListAll();
        case "delete":
            return Delete(Split(userInputString)[1]);
        case "clear":
            return Clear();
        case "help":
            return String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + Ls
			+ String.format("\tParameters: %1$s", "NAME "
			                                                  + "p/" + "PHONE_NUMBER "
			                                                  + "e/" + "EMAIL") + Ls
			+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + Ls + Ls
			+ String.format("%1$s: %2$s", "find", "Finds all persons whose names contain any of the specified "
			                                    + "keywords (case-sensitive) and displays them as a list with index numbers.") + Ls
			+ String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + Ls
			+ String.format("\tExample: %1$s", "find" + " alice bob charlie") + Ls + Ls
			+ String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + Ls
			+ String.format("\tExample: %1$s", "list") + Ls + Ls
			+ String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in "
			                                                + "the last find/list call.") + Ls
			+ String.format("\tParameters: %1$s", "INDEX") + Ls
			+ String.format("\tExample: %1$s", "delete" + " 1") + Ls + Ls
			+ String.format("%1$s: %2$s", "clear", "Clears address book permanently.") + Ls
			+ String.format("\tExample: %1$s", "clear") + Ls + Ls
			+ String.format("%1$s: %2$s", "exit", "Exits the program.")
			+ String.format("\tExample: %1$s", "exit") + Ls
			+ String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
			+ String.format("\tExample: %1$s", "help");
        case "exit":
            String[] message = { "Exiting Address Book... Good bye!", Ld, Ld };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
			System.exit(0);
        default:
            return String.format("Invalid command format: %1$s " + Ls + "%2$s", Split(userInputString)[0], String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + Ls
			+ String.format("\tParameters: %1$s", "NAME "
			                                                  + "p/" + "PHONE_NUMBER "
			                                                  + "e/" + "EMAIL") + Ls
			+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + Ls + Ls
			+ String.format("%1$s: %2$s", "find", "Finds all persons whose names contain any of the specified "
			                                    + "keywords (case-sensitive) and displays them as a list with index numbers.") + Ls
			+ String.format("\tParameters: %1$s", "KEYWORD [MORE_KEYWORDS]") + Ls
			+ String.format("\tExample: %1$s", "find" + " alice bob charlie") + Ls + Ls
			+ String.format("%1$s: %2$s", "list", "Displays all persons as a list with index numbers.") + Ls
			+ String.format("\tExample: %1$s", "list") + Ls + Ls
			+ String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in "
			                                                + "the last find/list call.") + Ls
			+ String.format("\tParameters: %1$s", "INDEX") + Ls
			+ String.format("\tExample: %1$s", "delete" + " 1") + Ls + Ls
			+ String.format("%1$s: %2$s", "clear", "Clears address book permanently.") + Ls
			+ String.format("\tExample: %1$s", "clear") + Ls + Ls
			+ String.format("%1$s: %2$s", "exit", "Exits the program.")
			+ String.format("\tExample: %1$s", "exit") + Ls
			+ String.format("%1$s: %2$s", "help", "Shows program usage instructions.")
			+ String.format("\tExample: %1$s", "help"));
        }
    }

    private static String[] Split(String rawUserInput) {
        final String[] parts =  rawUserInput.trim().split("\\s+", 2);
        return parts.length == 2 ? parts : new String[] { parts[0] , "" }; // else case: no parameters
    }

    private static String Add(String commandArgs) {
        // try decoding a person from the raw args
        final Optional<String[]> decodedPerson = Decode(commandArgs);

        // checks if args are valid (decode result will not be present if the person is invalid)
        if (!decodedPerson.isPresent()) {
            return String.format("Invalid command format: %1$s " + Ls + "%2$s", "add", String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + Ls
			+ String.format("\tParameters: %1$s", "NAME "
			                                                  + "p/" + "PHONE_NUMBER "
			                                                  + "e/" + "EMAIL") + Ls
			+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + Ls);
        }

        // add the person as specified
        final String[] personToAdd = decodedPerson.get();
        LIST.add(personToAdd);
		SaveToFile(LIST, ROAd);
        return String.format("New person added: %1$s, Phone: %2$s, Email: %3$s",
		personToAdd[0], personToAdd[1], personToAdd[2]);
    }

    private static String Find(String commandArgs) {
        final Set<String> keywords = new HashSet<>((ArrayList<String>) new ArrayList(Arrays.asList(commandArgs.trim().trim().split("\\s+"))));
        final ArrayList<String[]> personsFound = doesTheseGuysOrGirlsNameHaveTheseKeywordsOrnot(keywords);
        UserSee(personsFound);
        return String.format("%1$d persons found!", personsFound.size());
    }

    private static ArrayList<String[]> doesTheseGuysOrGirlsNameHaveTheseKeywordsOrnot(Collection<String> keywords) {
        final ArrayList<String[]> matchedPersons = new ArrayList<>();
        for (String[] person : LIST) {
            final Set<String> wordsInName = new HashSet<>((ArrayList<String>) new ArrayList(Arrays.asList(person[0].trim().split("\\s+"))));
            if (!Collections.disjoint(wordsInName, keywords)) {
                matchedPersons.add(person);
            }
        }
        return matchedPersons;
    }

    private static String Delete(String commandArgs) {
        if (!validOrNot(commandArgs)) {
            return String.format("Invalid command format: %1$s " + Ls + "%2$s", "delete", String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in "
			                                                + "the last find/list call.") + Ls
			+ String.format("\tParameters: %1$s", "INDEX") + Ls
			+ String.format("\tExample: %1$s", "delete" + " 1") + Ls);
        }
        final int targetVisibleIndex = Integer.parseInt(commandArgs.trim());
        if (!(targetVisibleIndex >= 1 && targetVisibleIndex < vIEW.size() + 1)) {
            return "The person index provided is invalid";
        }
        final String[] targetPersonInModel = vIEW.get(targetVisibleIndex - 1);
        return Delete2(targetPersonInModel) ? String.format("Deleted Person: %1$s", String.format("%1$s  Phone Number: %2$s  Email: %3$s",
		targetPersonInModel[0], targetPersonInModel[1], targetPersonInModel[2])) // success
                                                          : "Person could not be found in address book"; // not found
    }

    private static boolean validOrNot(String rawArgs) {
        try {
            final int extractedIndex = Integer.parseInt(rawArgs.trim()); // use standard libraries to parse
            return extractedIndex >= 1;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    private static String Clear() {
        LIST.clear();
		final ArrayList<String> linesToWrite = Encode(LIST);
		try {
		    Files.write(Paths.get(ROAd), linesToWrite);
		} catch (IOException ioe) {
		    String[] message = { String.format("Unexpected error: unable to write to file: %1$s", ROAd) };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
		    String[] message1 = { "Exiting Address Book... Good bye!", Ld, Ld };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
		}
        return "Address book has been cleared!";
    }

    private static String ListAll() {
        ArrayList<String[]> personsForDisplay = LIST;
        String listAsString = Display(personsForDisplay);
		String[] message = { listAsString };
		for (String m : message) {
		    System.out.println("|| " + m);
		}
		// clone to insulate from future changes to arg list
		vIEW = new ArrayList<>(personsForDisplay);
        return String.format("%1$d persons found!", personsForDisplay.size());
    }

    private static void UserSee(ArrayList<String[]> persons) {
        String listAsString = Display(persons);
		String[] message = { listAsString };
        for (String m : message) {
		    System.out.println("|| " + m);
		}
        // clone to insulate from future changes to arg list
		vIEW = new ArrayList<>(persons);
    }

    private static String Display(ArrayList<String[]> persons) {
        final StringBuilder messageAccumulator = new StringBuilder();
        for (int i = 0; i < persons.size(); i++) {
            final String[] person = persons.get(i);
            final int displayIndex = i + 1;
            messageAccumulator.append('\t')
                              .append(String.format("%1$d. ", displayIndex) + String.format("%1$s  Phone Number: %2$s  Email: %3$s",
							person[0], person[1], person[2]))
                              .append(Ls);
        }
        return messageAccumulator.toString();
    }

    private static void GiveMeFile(String filePath) {
        final File storageFile = new File(filePath);
        if (storageFile.exists()) {
            return;
        }
		String[] message = { String.format("Storage file missing: %1$s", filePath) };

        for (String m : message) {
		    System.out.println("|| " + m);
		}

        try {
            storageFile.createNewFile();
			String[] message1 = { String.format("Created new empty storage file: %1$s", filePath) };
            for (String m : message1) {
			    System.out.println("|| " + m);
			}
        } catch (IOException ioe) {
            String[] message1 = { String.format("Error: unable to create file: %1$s", filePath) };
			for (String m : message1) {
			    System.out.println("|| " + m);
			}
            String[] message2 = { "Exiting Address Book... Good bye!", Ld, Ld };
			for (String m1 : message2) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
        }
    }

    private static ArrayList<String[]> Load(String filePath) {
        final Optional<ArrayList<String[]>> decodedPersons = Decode2(LinesInFile(filePath));
        if (!decodedPersons.isPresent()) {
            String[] message = { "Storage file has invalid content" };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
            String[] message1 = { "Exiting Address Book... Good bye!", Ld, Ld };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
        }
        return decodedPersons.get();
    }

    private static ArrayList<String> LinesInFile(String filePath) {
        ArrayList<String> lines = null;
        try {
            lines = new ArrayList(Files.readAllLines(Paths.get(filePath)));
        } catch (FileNotFoundException fnfe) {
            String[] message = { String.format("Storage file missing: %1$s", filePath) };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
            String[] message1 = { "Exiting Address Book... Good bye!", Ld, Ld };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
        } catch (IOException ioe) {
            String[] message = { String.format("Unexpected error: unable to read from file: %1$s", filePath) };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
            String[] message1 = { "Exiting Address Book... Good bye!", Ld, Ld };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
        }
        return lines;
    }

    private static void SaveToFile(ArrayList<String[]> persons, String filePath) {
        final ArrayList<String> linesToWrite = Encode(persons);
        try {
            Files.write(Paths.get(ROAd), linesToWrite);
        } catch (IOException ioe) {
            String[] message = { String.format("Unexpected error: unable to write to file: %1$s", filePath) };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
            String[] message1 = { "Exiting Address Book... Good bye!", Ld, Ld };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
        }
    }
    
    private static boolean Delete2(String[] exactPerson) {
        final boolean isChanged = LIST.remove(exactPerson);
        if (isChanged) {
            SaveToFile(LIST, ROAd);
        }
        return isChanged;
    }

    private static String[] giveMePerson(String name, String phone, String email) {
        final String[] person = new String[3];
        person[0] = name;
        person[1] = phone;
        person[2] = email;
        return person;
    }

    private static ArrayList<String> Encode(ArrayList<String[]> persons) {
        final ArrayList<String> encodedStrings = new ArrayList<>();
        for (String[] person : persons) {
            encodedStrings.add(String.format("%1$s " // name
			                                                + "p/" + "%2$s " // phone
			                                                + "e/" + "%3$s",
			person[0], person[1], person[2]));
        }
        return encodedStrings;
    }

    private static Optional<String[]> Decode(String encodedString) {
        // check that we can extract the parts of a person from the encoded string
        if (!ExtractableOrNot(encodedString)) {
            return Optional.empty();
        }
        final String[] decodedPerson = giveMePerson(
        		Extract(encodedString),
                Extract2(encodedString),
                Extract3(encodedString)
        );
        // check that the constructed person is valid
        return decodedPerson[0].matches("(\\w|\\s)+")
		&& decodedPerson[1].matches("\\d+")
		&& decodedPerson[2].matches("\\S+@\\S+\\.\\S+") ? Optional.of(decodedPerson) : Optional.empty();
    }

    private static Optional<ArrayList<String[]>> Decode2(ArrayList<String> encodedPersons) {
        final ArrayList<String[]> decodedPersons = new ArrayList<>();
        for (String encodedPerson : encodedPersons) {
            final Optional<String[]> decodedPerson = Decode(encodedPerson);
            if (!decodedPerson.isPresent()) {
                return Optional.empty();
            }
            decodedPersons.add(decodedPerson.get());
        }
        return Optional.of(decodedPersons);
    }

    private static boolean ExtractableOrNot(String personData) {
        final String matchAnyPersonDataPrefix = "p/" + '|' + "e/";
        final String[] splitArgs = personData.trim().split(matchAnyPersonDataPrefix);
        return splitArgs.length == 3 // 3 arguments
                && !splitArgs[0].isEmpty() // non-empty arguments
                && !splitArgs[1].isEmpty()
                && !splitArgs[2].isEmpty();
    }

    private static String Extract(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf("p/");
        final int indexOfEmailPrefix = encoded.indexOf("e/");
        // name is leading substring up to first data prefix symbol
        int indexOfFirstPrefix = Math.min(indexOfEmailPrefix, indexOfPhonePrefix);
        return encoded.substring(0, indexOfFirstPrefix).trim();
    }

    private static String Extract2(String encoded) {
        final int indexOfPhonePrefix = encoded.indexOf("p/");
        final int indexOfEmailPrefix = encoded.indexOf("e/");

        // phone is last arg, target is from prefix to end of string
        if (indexOfPhonePrefix > indexOfEmailPrefix) {
            return encoded.substring(indexOfPhonePrefix, encoded.length()).trim().replace("p/", "");

        // phone is middle arg, target is from own prefix to next prefix
        } else {
            return encoded.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim().replace("p/", "");
        }
    }

    private static String Extract3(String encodedString) {
        final int indexOfPhonePrefix = encodedString.indexOf("p/");
        final int indexOfEmailPrefix = encodedString.indexOf("e/");

        // email is last arg, target is from prefix to end of string
        if (indexOfEmailPrefix > indexOfPhonePrefix) {
            return encodedString.substring(indexOfEmailPrefix, encodedString.length()).trim().replace("e/", "");

        // email is middle arg, target is from own prefix to next prefix
        } else {
            return encodedString.substring(indexOfEmailPrefix, indexOfPhonePrefix).trim().replace("e/", "");
        }
    }

}