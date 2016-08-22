package seedu.addressbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class AddressBook {
    private static final Scanner a = new Scanner(System.in);
    private static final ArrayList<String[]> b = new ArrayList<>();
    private static ArrayList<String[]> c = b;
    private static String d;
    public static void main(String[] e) {
        zz("===================================================", "===================================================", "AddessBook Level 1 - Version 1.0", "Welcome to your Address Book!", "===================================================");
        if (e.length >= 2) {
		    zz("Too many parameters! Correct program argument format:"
			                                                        + System.lineSeparator() + "|| " + "\tjava AddressBook"
			                                                        + System.lineSeparator() + "|| " + "\tjava AddressBook [custom storage file path]");
		    zz("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
		}
		
		if (e.length == 1) {
		    if (!e[0].endsWith(".txt")) {
			    zz(String.format("The given file name [%1$s] is not a valid file name!", e[0]));
			    zz("Exiting Address Book... Good bye!", "===================================================", "===================================================");
				System.exit(0);
			}
			
			d = e[0];
			u(e[0]);
		}
		
		if(e.length == 0) {
		    zz("Using default storage file : " + "addressbook.txt");
			d = "addressbook.txt";
			u(d);
		}
        b.clear();
		b.addAll(uio(d));
        while (true) {
            System.out.print("|| " + "Enter command: ");
			String g = a.nextLine();
			while (g.trim().isEmpty() || g.trim().charAt(0) == '#') {
			    g = a.nextLine();
			}
			String w = g;
            zz("[Command entered:" + w + "]");
            String qwe = dfkgmldf(w);
            zz(qwe, "===================================================");
        }
    }
    public static String dfkgmldf(String userInputString) {
        final String[] j = userInputString.trim().split("\\s+", 2).length == 2 ? userInputString.trim().split("\\s+", 2) : new String[] { userInputString.trim().split("\\s+", 2)[0] , "" };
        switch (j[0]) {
        case "add":
            final Optional<String[]> s = d(j[1]);
			if (!s.isPresent()) {
			    return String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", "add", String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", "NAME "
				                                                  + "p/" + "PHONE_NUMBER "
				                                                  + "e/" + "EMAIL") + System.lineSeparator() + "|| "
				+ String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + System.lineSeparator() + "|| ");
			}
			
			final String[] p = s.get();
			b.add(p);
			final ArrayList<String> encoded = new ArrayList<>();
			for (String[] person1 : b) {
			    encoded.add(String.format("%1$s "
				                                                + "p/" + "%2$s " 
				                                                + "e/" + "%3$s",
				person1[0], person1[1], person1[2]));
			}
			final ArrayList<String> linesToWrite = encoded;
			try {
			    Files.write(Paths.get(d), linesToWrite);
			} catch (IOException ioe) {
			    zz(String.format("Unexpected error: unable to write to file: %1$s", d));
			    zz("Exiting Address Book... Good bye!", "===================================================", "===================================================");
				System.exit(0);
			}
			return String.format("New person added: %1$s, Phone: %2$s, Email: %3$s",
			p[0], p[1], p[2]);
        case "find":
            final Set<String> keywords = new HashSet<>((ArrayList<String>) new ArrayList(Arrays.asList(j[1].trim().trim().split("\\s+"))));
			final ArrayList<String[]> matchedPersons = new ArrayList<>();
			for (String[] person : b) {
			    final Set<String> wordsInName = new HashSet<>((ArrayList<String>) new ArrayList(Arrays.asList(person[0].trim().split("\\s+"))));
			    if (!Collections.disjoint(wordsInName, keywords)) {
			        matchedPersons.add(person);
			    }
			}
			final ArrayList<String[]> personsFound = matchedPersons;
			zz(personsFound);
			return String.format("%1$d persons found!", personsFound.size());
        case "list":
            ArrayList<String[]> toBeDisplayed = b;
			zz(toBeDisplayed);
			return String.format("%1$d persons found!", toBeDisplayed.size());
        case "delete":
            if (!isDeletePersonArgsValid(j[1])) {
			    return String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", "delete", String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in "
				                                                + "the last find/list call.") + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", "INDEX") + System.lineSeparator() + "|| "
				+ String.format("\tExample: %1$s", "delete" + " 1") + System.lineSeparator() + "|| ");
			}
			final int targetVisibleIndex = Integer.parseInt(j[1].trim());
			if (!(targetVisibleIndex >= 1 && targetVisibleIndex < c.size() + 1)) {
			    return "The person index provided is invalid";
			}
			final String[] targetInModel = c.get(targetVisibleIndex - 1);
			final boolean changed = b.remove(targetInModel);
	        if (changed) {
	            final ArrayList<String> encoded2 = new ArrayList<>();
				for (String[] person : b) {
				    encoded2.add(String.format("%1$s " 
					                                                + "p/" + "%2$s " 
					                                                + "e/" + "%3$s",
					person[0], person[1], person[2]));
				}
				final ArrayList<String> linesToWrite2 = encoded2;
				try {
				    Files.write(Paths.get(d), linesToWrite2);
				} catch (IOException ioe) {
				    zz(String.format("Unexpected error: unable to write to file: %1$s", d));
				    zz("Exiting Address Book... Good bye!", "===================================================", "===================================================");
					System.exit(0);
				}
	        }
			return changed ? String.format("Deleted Person: %1$s", String.format("%1$s  Phone Number: %2$s  Email: %3$s",
			targetInModel[0], targetInModel[1], targetInModel[2]))
			                                                  : "Person could not be found in address book";
        case "clear":
            b.clear();
			final ArrayList<String> encoded1 = new ArrayList<>();
			for (String[] person2 : b) {
			    encoded1.add(String.format("%1$s "
				                                                + "p/" + "%2$s " 
				                                                + "e/" + "%3$s",
				person2[0], person2[1], person2[2]));
			}
			final ArrayList<String> linesToWrite1 = encoded1;
			try {
			    Files.write(Paths.get(d), linesToWrite1);
			} catch (IOException ioe) {
			    zz(String.format("Unexpected error: unable to write to file: %1$s", d));
			    zz("Exiting Address Book... Good bye!", "===================================================", "===================================================");
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
            zz("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
        default:
            return String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", j[0], String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
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
    private static void zz(String... message) {
        for (String m : message) {
            System.out.println("|| " + m);
        }
    }
    private static void zz(ArrayList<String[]> persons) {
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
        zz(listAsString);
        c = new ArrayList<>(persons);
    }
    private static void u(String filePath) {
        if (new File(filePath).exists()) return;
        zz(String.format("Storage file missing: %1$s", filePath));
        try {
            new File(filePath).createNewFile();
            zz(String.format("Created new empty storage file: %1$s", filePath));
        } catch (IOException ioe) {
            zz(String.format("Error: unable to create file: %1$s", filePath));
            zz("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
        }
    }
    private static ArrayList<String[]> uio(String ji) {
    	ArrayList<String> t = null;
        try {
            t = new ArrayList(Files.readAllLines(Paths.get(ji)));
        } catch (FileNotFoundException fnfe) {
            zz(String.format("Storage file missing: %1$s", ji));
            zz("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
        } catch (IOException ioe) {
            zz(String.format("Unexpected error: unable to read from file: %1$s", ji));
            zz("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
        }
        final Optional<ArrayList<String[]>> successfullyDecoded = ws(t);
        if (!successfullyDecoded.isPresent()) {
            zz("Storage file has invalid content");
            zz("Exiting Address Book... Good bye!", "===================================================", "===================================================");
			System.exit(0);
        }
        return successfullyDecoded.get();
    }
    private static Optional<String[]> d(String ef) {
        if (!as(ef)) {
            return Optional.empty();
        }
		final String[] person = new String[3];
		person[0] = te(ef);
		person[1] = py(ef);
		person[2] = ft(ef);
        final String[] decodedPerson = person;
        return decodedPerson[0].matches("(\\w|\\s)+")
		&& decodedPerson[1].matches("\\d+")
		&& decodedPerson[2].matches("\\S+@\\S+\\.\\S+") ? Optional.of(decodedPerson) : Optional.empty();
    }
    private static Optional<ArrayList<String[]>> ws(ArrayList<String> gh) {
        final ArrayList<String[]> decodedPersons = new ArrayList<>();
        for (String wq : gh) {
            final Optional<String[]> po = d(wq);
            if (!po.isPresent()) {
                return Optional.empty();
            }
            decodedPersons.add(po.get());
        }
        return Optional.of(decodedPersons);
    }
    private static boolean as(String y) {
        final String[] x = y.trim().split("p/" + '|' + "e/");
        return x.length == 3
                && !x[0].isEmpty() 
                && !x[1].isEmpty()
                && !x[2].isEmpty();
    }
    private static String te(String encoded) {
        int indexOfFirstPrefix = Math.min(encoded.indexOf("e/"), encoded.indexOf("p/"));
        return encoded.substring(0, indexOfFirstPrefix).trim();
    }
    private static String py(String encoded) {
        if (encoded.indexOf("p/") > encoded.indexOf("e/")) {
            return encoded.substring(encoded.indexOf("p/"), encoded.length()).trim().replace("p/", "");
        } else {
            return encoded.substring(encoded.indexOf("p/"), encoded.indexOf("e/")).trim().replace("p/", "");
        }
    }
    private static String ft(String encoded) {
        if (encoded.indexOf("e/") > encoded.indexOf("p/")) {
            return encoded.substring(encoded.indexOf("e/"), encoded.length()).trim().replace("e/", "");
        } else {
            return encoded.substring(encoded.indexOf("e/"), encoded.indexOf("p/")).trim().replace("e/", "");
        }
    }

}