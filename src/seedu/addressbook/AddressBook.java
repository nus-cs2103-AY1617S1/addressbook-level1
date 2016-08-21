package seedu.addressbook;

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

public class AddressBook {
    
    public static void main(String[] args) {
	final Scanner SCANNER = new Scanner(System.in);
	final ArrayList<String[]> ALL_PERSONS = new ArrayList<>();
	ArrayList<String[]> latestPersonListingView = ALL_PERSONS; 
	String storageFilePath = "";
	    
	// print welcome message
        String[] message = { "===================================================", "===================================================", "AddessBook Level 1 - Version 1.0", "Welcome to your Address Book!", "===================================================" };
	for (String m : message) {
	    System.out.println("|| " + m);
	}
	
	// ensure that there are no more than 2 arguments
        if (args.length >= 2) {
	    String[] message1 = { "Too many parameters! Correct program argument format:"
	                                                            + System.lineSeparator() + "|| " + "\tjava AddressBook"
	                                                            + System.lineSeparator() + "|| " + "\tjava AddressBook [custom storage file path]" };
	    for (String m : message1) {
	        System.out.println("|| " + m);
	    }
	    String[] message2 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
	    for (String m1 : message2) {
	        System.out.println("|| " + m1);
	    }
	    System.exit(0);
	}
	
        // try to load storage file if user has given it
	if (args.length == 1) {
	    String filePath = args[0];
	    // check that path is not invalid
	    if (!filePath.endsWith(".txt")) {
	        String[] message1 = { String.format("The given file name [%1$s] is not a valid file name!", filePath) };
	        for (String m1 : message1) {
	            System.out.println("|| " + m1);
	        }
	        String[] message11 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
	        for (String m1 : message11) {
	            System.out.println("|| " + m1);
	        }
	        System.exit(0);
	    }
	    
	    storageFilePath = filePath;

	    // create storage file if it does not exist
	    final File storageFile = new File(filePath);
	    if (!storageFile.exists()) {
	    String[] message1 = { String.format("Storage file missing: %1$s", filePath) };
	    
	        for (String m1 : message1) {
	        System.out.println("|| " + m1);
	    }
	    
	        try {
	            storageFile.createNewFile();
	        String[] message12 = { String.format("Created new empty storage file: %1$s", filePath) };
	            for (String m2 : message12) {
	            System.out.println("|| " + m2);
	        }
	        } catch (IOException ioe) {
	            String[] message11 = { String.format("Error: unable to create file: %1$s", filePath) };
	        for (String m3 : message11) {
	            System.out.println("|| " + m3);
	        }
	            String[] message2 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
	        for (String m1 : message2) {
	            System.out.println("|| " + m1);
	        }
	        System.exit(0);
	        }
	    }
	}
	
	// load default if no storage given
	if(args.length == 0) {
	    String[] message1 = { "Using default storage file : " + "addressbook.txt" };
	    for (String m1 : message1) {
	        System.out.println("|| " + m1);
	    }
	    storageFilePath = "addressbook.txt";
	    
	    // create storage file if it does not exist
	    final File storageFile = new File(storageFilePath);
	    if (!storageFile.exists()) {
	    String[] message2 = { String.format("Storage file missing: %1$s", storageFilePath) };
	    
	        for (String m2 : message2) {
	        System.out.println("|| " + m2);
	    }
	    
	        try {
	            storageFile.createNewFile();
	        String[] message11 = { String.format("Created new empty storage file: %1$s", storageFilePath) };
	            for (String m3 : message11) {
	            System.out.println("|| " + m3);
	        }
	        } catch (IOException ioe) {
	            String[] message12 = { String.format("Error: unable to create file: %1$s", storageFilePath) };
	        for (String m4 : message12) {
	            System.out.println("|| " + m4);
	        }
	            String[] message21 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
	        for (String m11 : message21) {
	            System.out.println("|| " + m11);
	        }
	        System.exit(0);
	        }
	    }
	}
	
	// read all lines in file
        ALL_PERSONS.clear();
	ArrayList<String> linesInFiles = null;
	try {
	    linesInFiles = new ArrayList(Files.readAllLines(Paths.get(storageFilePath)));
	} catch (FileNotFoundException fnfe) {
	    String[] message2 = { String.format("Storage file missing: %1$s", storageFilePath) };
	    for (String m3 : message2) {
	        System.out.println("|| " + m3);
	    }
	    String[] message12 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
	    for (String m12 : message12) {
	        System.out.println("|| " + m12);
	    }
	    System.exit(0);
	} catch (IOException ioe) {
	    String[] message3 = { String.format("Unexpected error: unable to read from file: %1$s", storageFilePath) };
	    for (String m2 : message3) {
	        System.out.println("|| " + m2);
	    }
	    String[] message13 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
	    for (String m13 : message13) {
	        System.out.println("|| " + m13);
	    }
	    System.exit(0);
	}

	// check integrity of storage file
	Optional<ArrayList<String[]>> integrityResult = null;
        final ArrayList<String[]> decodedPersons = new ArrayList<>();
        for (String encodedPerson : linesInFiles) {
            Optional<String[]> decodeResult = null;
	    
	    final String matchAnyPersonDataPrefix = "p/" + '|' + "e/";
	    final String[] splitArgs = encodedPerson.trim().split(matchAnyPersonDataPrefix);
	    boolean isPersonDataExtractable = splitArgs.length == 3 
	    && !splitArgs[0].isEmpty() 
	    && !splitArgs[1].isEmpty()
	    && !splitArgs[2].isEmpty();
	    
	    if (!isPersonDataExtractable) {
	        decodeResult = Optional.empty();
	    }
	    else
	    {
	    String name = "";
	    // extract name from person string
	    {
	    final int indexOfPhonePrefix = encodedPerson.indexOf("p/");
	    final int indexOfEmailPrefix = encodedPerson.indexOf("e/");
	    
	    int indexOfFirstPrefix = Math.min(indexOfEmailPrefix, indexOfPhonePrefix);
	        name =  encodedPerson.substring(0, indexOfFirstPrefix).trim();
	    }
	    
	    // extract phone from person string
	    String phone = "";
	    {
	        final int indexOfPhonePrefix = encodedPerson.indexOf("p/");
	        final int indexOfEmailPrefix = encodedPerson.indexOf("e/");
	    
	        
	        if (indexOfPhonePrefix > indexOfEmailPrefix) {
	            // remove prefix
	    	phone = encodedPerson.substring(indexOfPhonePrefix, encodedPerson.length()).trim().replace("p/", "");
	    
	        
	        } else {
	            // remove prefix
	    	phone = encodedPerson.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim().replace("p/", "");
	        }
	    }
	    
	    // extract email from person string
	    String email = "";
	    {
	        final int indexOfPhonePrefix = encodedPerson.indexOf("p/");
	        final int indexOfEmailPrefix = encodedPerson.indexOf("e/");
	    
	        
	        if (indexOfEmailPrefix > indexOfPhonePrefix) {
	            // remove prefix
	            email = encodedPerson.substring(indexOfEmailPrefix, encodedPerson.length()).trim().replace("e/", "");
	    
	        
	        } else {
	            // remove prefix
	            email = encodedPerson.substring(indexOfEmailPrefix, indexOfPhonePrefix).trim().replace("e/", "");
	        }
	    }
	    
	    final String[] person = new String[3];
	    person[0] = name;
	    person[1] = phone;
	    person[2] = email;
	    final String[] decodedPerson1 = person;
	    
	    // is person data valid?
	    decodeResult = decodedPerson1[0].matches("(\\w|\\s)+")
	    && decodedPerson1[1].matches("\\d+")
	    && decodedPerson1[2].matches("\\S+@\\S+\\.\\S+") ? Optional.of(decodedPerson1) : Optional.empty();
	    }
	    final Optional<String[]> decodedPerson = decodeResult;
            if (!decodedPerson.isPresent()) {
        	integrityResult = Optional.empty();
        	break;
            }
            decodedPersons.add(decodedPerson.get());
        }
        if (integrityResult == null)
            integrityResult = Optional.of(decodedPersons);
        
	final Optional<ArrayList<String[]>> successfullyDecoded = integrityResult;
	if (!successfullyDecoded.isPresent()) {
	    String[] message1 = { "Storage file has invalid content" };
	    for (String m1 : message1) {
	        System.out.println("|| " + m1);
	    }
	    String[] message11 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
	    for (String m11 : message11) {
	        System.out.println("|| " + m11);
	    }
	    System.exit(0);
	}
	
	// add all to database
        ArrayList<String[]> personsFromFile = successfullyDecoded.get();
	ALL_PERSONS.addAll(personsFromFile);
	
        while (true) {
            // get user input
            System.out.print("|| " + "Enter command: ");
	    String inputLine = SCANNER.nextLine();
	    
	    while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == '#') {
	        inputLine = SCANNER.nextLine();
	    }
	    String userCommand = inputLine;
            
            // echo commands entered
            String[] message2 = { "[Command entered:" + userCommand + "]" };
	    for (String m2 : message2) {
	        System.out.println("|| " + m2);
	    }
            
	    // execute command
            String feedback = "";
            
	    final String[] split =  userCommand.trim().split("\\s+", 2);
            final String[] commandTypeAndParams = split.length == 2 ? split : new String[] { split[0] , "" };
            final String commandType = commandTypeAndParams[0];
            final String commandArgs = commandTypeAndParams[1];
            switch (commandType) {
            case "add":
		Optional<String[]> decodeResult1 = null;
		
		final String matchAnyPersonDataPrefix = "p/" + '|' + "e/";
		final String[] splitArgs = commandArgs.trim().split(matchAnyPersonDataPrefix);
		boolean isPersonDataExtractable = splitArgs.length == 3 
		&& !splitArgs[0].isEmpty() 
		&& !splitArgs[1].isEmpty()
		&& !splitArgs[2].isEmpty();
		
		if (!isPersonDataExtractable) {
		    decodeResult1 = Optional.empty();
		}
		else
		{
		String name = "";
		// extract name from person string
		{
		final int indexOfPhonePrefix = commandArgs.indexOf("p/");
		final int indexOfEmailPrefix = commandArgs.indexOf("e/");
		
		int indexOfFirstPrefix = Math.min(indexOfEmailPrefix, indexOfPhonePrefix);
		    name =  commandArgs.substring(0, indexOfFirstPrefix).trim();
		}
		
		// extract phone from person string
		String phone = "";
		{
		    final int indexOfPhonePrefix = commandArgs.indexOf("p/");
		    final int indexOfEmailPrefix = commandArgs.indexOf("e/");
		
		    
		    if (indexOfPhonePrefix > indexOfEmailPrefix) {
		        // remove prefix
			phone = commandArgs.substring(indexOfPhonePrefix, commandArgs.length()).trim().replace("p/", "");
		
		    
		    } else {
		        // remove prefix
			phone = commandArgs.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim().replace("p/", "");
		    }
		}
		
		// extract email from person string
		String email = "";
		{
		    final int indexOfPhonePrefix = commandArgs.indexOf("p/");
		    final int indexOfEmailPrefix = commandArgs.indexOf("e/");
		
		    
		    if (indexOfEmailPrefix > indexOfPhonePrefix) {
		        // remove prefix
		        email = commandArgs.substring(indexOfEmailPrefix, commandArgs.length()).trim().replace("e/", "");
		
		    
		    } else {
		        // remove prefix
		        email = commandArgs.substring(indexOfEmailPrefix, indexOfPhonePrefix).trim().replace("e/", "");
		    }
		}
		
		final String[] person4 = new String[3];
		person4[0] = name;
		person4[1] = phone;
		person4[2] = email;
		final String[] decodedPerson = person4;
		
		// is person data valid?
		decodeResult1 = decodedPerson[0].matches("(\\w|\\s)+")
		&& decodedPerson[1].matches("\\d+")
		&& decodedPerson[2].matches("\\S+@\\S+\\.\\S+") ? Optional.of(decodedPerson) : Optional.empty();
		}                
                final Optional<String[]> decodeResult = decodeResult1;
                
                if (!decodeResult.isPresent()) {
                    feedback =  String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", "add", String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
        	    + String.format("\tParameters: %1$s", "NAME "
		                                                      + "p/" + "PHONE_NUMBER "
		                                                      + "e/" + "EMAIL") + System.lineSeparator() + "|| "
        	    + String.format("\tExample: %1$s", "add" + " John Doe p/98765432 e/johnd@gmail.com") + System.lineSeparator() + "|| ");
                }
                else
                {                    
                    final String[] personToAdd = decodeResult.get();
                    ALL_PERSONS.add(personToAdd);
		    final ArrayList<String> encoded1 = new ArrayList<>();
		    for (String[] person1 : ALL_PERSONS) {
		        encoded1.add(String.format("%1$s " 
			                                                        + "p/" + "%2$s " 
			                                                        + "e/" + "%3$s",
		        person1[0], person1[1], person1[2]));
		    }
		    final ArrayList<String> linesToWrite1 = encoded1;
		    try {
		        Files.write(Paths.get(storageFilePath), linesToWrite1);
		    } catch (IOException ioe) {
		        String[] message4 = { String.format("Unexpected error: unable to write to file: %1$s", storageFilePath) };
		        for (String m4 : message4) {
		            System.out.println("|| " + m4);
		        }
		        String[] message11 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
		        for (String m11 : message11) {
		            System.out.println("|| " + m11);
		        }
		        System.exit(0);
		    }
                    feedback =  String.format("New person added: %1$s, Phone: %2$s, Email: %3$s",
		    personToAdd[0], personToAdd[1], personToAdd[2]);
                }
        	break;
            case "find":
		final Set<String> keywords = new HashSet<>((ArrayList<String>) new ArrayList(Arrays.asList(commandArgs.trim().trim().split("\\s+"))));
		final ArrayList<String[]> matchedPersons = new ArrayList<>();
		for (String[] person1 : ALL_PERSONS) {
		    final Set<String> wordsInName = new HashSet<>((ArrayList<String>) new ArrayList(Arrays.asList(person1[0].trim().split("\\s+"))));
		    if (!Collections.disjoint(wordsInName, keywords)) {
		        matchedPersons.add(person1);
		    }
		}
		final ArrayList<String[]> personsFound = matchedPersons;
		final StringBuilder messageAccumulator = new StringBuilder();
		for (int i = 0; i < personsFound.size(); i++) {
		    final String[] person2 = personsFound.get(i);
		    final int displayIndex = i + 1;
		    messageAccumulator.append('\t')
		                      .append(String.format("%1$d. ", displayIndex) + String.format("%1$s  Phone Number: %2$s  Email: %3$s",
				    person2[0], person2[1], person2[2]))
		                      .append(System.lineSeparator() + "|| ");
		}
		String listAsString = messageAccumulator.toString();
		String[] message5 = { listAsString };
		for (String m5 : message5) {
		    System.out.println("|| " + m5);
		}
		latestPersonListingView = new ArrayList<>(personsFound);
        	feedback =  String.format("%1$d persons found!", personsFound.size());        	
        	break;
            case "list":
		ArrayList<String[]> toBeDisplayed = ALL_PERSONS;
		final StringBuilder messageAccumulator2 = new StringBuilder();
		for (int i = 0; i < toBeDisplayed.size(); i++) {
		    final String[] person3 = toBeDisplayed.get(i);
		    final int displayIndex = i + 1;
		    messageAccumulator2.append('\t')
		                      .append(String.format("%1$d. ", displayIndex) + String.format("%1$s  Phone Number: %2$s  Email: %3$s",
				    person3[0], person3[1], person3[2]))
		                      .append(System.lineSeparator() + "|| ");
		}
		String listAsString1 = messageAccumulator2.toString();
		String[] message6 = { listAsString1 };
		for (String m6 : message6) {
		    System.out.println("|| " + m6);
		}
		latestPersonListingView = new ArrayList<>(toBeDisplayed);
        	feedback =  String.format("%1$d persons found!", toBeDisplayed.size());
        	break;
            case "delete":
        	boolean deletePersonHasValidArgs = false;

                try {
                    final int extractedIndex = Integer.parseInt(commandArgs.trim()); 
                    deletePersonHasValidArgs = extractedIndex >= 1;
                } catch (NumberFormatException nfe) {
                    deletePersonHasValidArgs = false;
                }
                
                if (!deletePersonHasValidArgs) {
                    feedback = String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", "delete", String.format("%1$s: %2$s", "delete", "Deletes a person identified by the index number used in "
		                                                    + "the last find/list call.") + System.lineSeparator() + "|| "
        	    + String.format("\tParameters: %1$s", "INDEX") + System.lineSeparator() + "|| "
        	    + String.format("\tExample: %1$s", "delete" + " 1") + System.lineSeparator() + "|| ");
                }
                else 
                {
                    final int targetVisibleIndex = Integer.parseInt(commandArgs.trim());
                    if (!(targetVisibleIndex >= 1 && targetVisibleIndex < latestPersonListingView.size() + 1)) {
                        feedback =  "The person index provided is invalid";
                    }
                    else {
                        final String[] targetInModel = latestPersonListingView.get(targetVisibleIndex - 1);
			final boolean changed = ALL_PERSONS.remove(targetInModel);
			if (changed) {
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
			        String[] message4 = { String.format("Unexpected error: unable to write to file: %1$s", storageFilePath) };
			        for (String m4 : message4) {
			            System.out.println("|| " + m4);
			        }
			        String[] message11 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
			        for (String m11 : message11) {
			            System.out.println("|| " + m11);
			        }
			        System.exit(0);
			    }
			}
                        boolean isDeletePersonFromAddressBook = changed;
                        feedback = isDeletePersonFromAddressBook ? String.format("Deleted Person: %1$s", String.format("%1$s  Phone Number: %2$s  Email: %3$s",
			targetInModel[0], targetInModel[1], targetInModel[2])) 
                                                                      : "Person could not be found in address book"; 
                    }
                }
        	break;
            case "clear":
		ALL_PERSONS.clear();
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
		    String[] message4 = { String.format("Unexpected error: unable to write to file: %1$s", storageFilePath) };
		    for (String m4 : message4) {
		        System.out.println("|| " + m4);
		    }
		    String[] message11 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
		    for (String m11 : message11) {
		        System.out.println("|| " + m11);
		    }
		    System.exit(0);
		}
        	feedback =  "Address book has been cleared!";
        	break;
            case "help":
        	feedback =  String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
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
        	break;
            case "exit":
                String[] message3 = { "Exiting Address Book... Good bye!", "===================================================", "===================================================" };
		for (String m3 : message3) {
		    System.out.println("|| " + m3);
		}
		System.exit(0);
		break;
            default:
        	feedback = String.format("Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s", commandType, String.format("%1$s: %2$s", "add", "Adds a person to the address book.") + System.lineSeparator() + "|| "
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
        	break;
            }
            
            // echo feedback received
            String[] message1 = { feedback, "===================================================" };
	    for (String m1 : message1) {
	        System.out.println("|| " + m1);
	    }
        }
    }
}