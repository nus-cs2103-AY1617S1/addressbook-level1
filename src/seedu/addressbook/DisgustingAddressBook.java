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

import seedu.addressbook.DisgustingAddressBook.PersonProperty;

/* ==============NOTE TO STUDENTS======================================
 * This class header comment below is brief because details of how to
 * use this class are documented elsewhere.
 * ====================================================================
 */

/**
 * This class is used to maintain a list of person data which are saved
 * in a text file.
 **/
public class DisgustingAddressBook {
    
    /**
     * Provides an enumeration of person property
     * */
    public static enum PersonProperty {
        NAME, EMAIL, PHONE
    };
    

    private static final String MESSAGE_COMMAND_HELP_EXAMPLE = "\tExample: %1$s";
    private static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    private static final String MESSAGE_DISPLAY_PERSON_DATA = "%1$s  Phone Number: %2$s  Email: %3$s";
    private static final String MESSAGE_DISPLAY_LIST_ELEMENT_INDEX = "%1$d. ";
    private static final String MESSAGE_GOODBYE = "Exiting Address Book... Good bye!";
    private static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format: %1$s " + System.lineSeparator() + "|| " + "%2$s";
    private static final String MESSAGE_INVALID_FILE = "The given file name [%1$s] is not a valid file name!";
    private static final String MESSAGE_INVALID_PROGRAM_ARGS = "Too many parameters! Correct program argument format:"
                                                            + System.lineSeparator() + "|| " + "\tjava AddressBook"
                                                            + System.lineSeparator() + "|| " + "\tjava AddressBook [custom storage file path]";
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
    private static final String MESSAGE_USING_DEFAULT_FILE = "Using default storage file : " + "addressbook.txt";

    private static final String COMMAND_ADD_WORD = "add";
    private static final String COMMAND_ADD_DESC = "Adds a person to the address book.";
    private static final String COMMAND_ADD_PARAMETERS = "NAME "
                                                      + "p/" + "PHONE_NUMBER "
                                                      + "e/" + "EMAIL";
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
     * The number of data elements for a single person.
     */
    private static final int PERSON_DATA_COUNT = 3;

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
    private static final ArrayList<HashMap<PersonProperty, String>> ALL_PERSONS = new ArrayList<>();


    /**
     * Stores the most recent list of persons shown to the user as a result of a user command.
     * This is a subset of the full list. Deleting persons in the pull list does not delete
     * those persons from this list.
     */
    private static ArrayList<HashMap<PersonProperty, String>> latestPersonListingView = ALL_PERSONS; // initial view is of all

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
    	
        String[] message = { DIVIDER, DIVIDER, "AddessBook Level 1 - Version 1.0", MESSAGE_WELCOME, DIVIDER }; //Displays a standard welcome message to user
		for (String m : message) {
		    System.out.println("|| " + m);
		} 
        final int HAS_ONE_ARG = 1;
		final int HAS_NO_ARG = 0;
		
		switch (args.length){
			case (HAS_ONE_ARG) :
				String storageFilePath1 = args[0];
					if (!storageFilePath1.endsWith(".txt")) {
				    String[] message1 = { String.format(MESSAGE_INVALID_FILE, storageFilePath1) };
					for (String m : message1) {
					    System.out.println("|| " + m);
					}
				    String[] message2 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
					for (String m1 : message2) {
					    System.out.println("|| " + m1);
					}
					System.exit(0);
				}
				
				createFileIfMissing(storageFilePath1);
				break;
				
			case (HAS_NO_ARG) :
				String[] message3 = { MESSAGE_USING_DEFAULT_FILE };
				for (String m2 : message3) {
				    System.out.println("|| " + m2);
				}
				storageFilePath = "addressbook.txt";
				createFileIfMissing(storageFilePath);
				break;
			
			default:
			String[] message1 = { MESSAGE_INVALID_PROGRAM_ARGS };
				for (String m : message1) {
			    System.out.println("|| " + m);
			}
			String[] message2 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message2) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
		}
		final Optional<ArrayList<String[]>> successfullyDecodedObject = decodePersonsFromStrings(getLinesInFile(storageFilePath));
		
		if (!successfullyDecodedObject.isPresent()) {
		    for (String m3 : new String[]{MESSAGE_INVALID_STORAGE_FILE_CONTENT}) {
			    System.out.println("|| " + m3);
			}
		    String[] message4 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m11 : message4) {
			    System.out.println("|| " + m11);
			}
			System.exit(0);
		}
		
		ArrayList<String[]> people = successfullyDecodedObject.get();
		
		ArrayList<HashMap<PersonProperty, String>> peopleMap = new ArrayList<>();
		
		for (String[] person : people){
			
		    HashMap<PersonProperty, String> personMap = new HashMap<>();
		    personMap.put(PersonProperty.NAME, person[0]);
		    personMap.put(PersonProperty.PHONE, person[1]);
		    personMap.put(PersonProperty.EMAIL, person[2]);
		    
		    peopleMap.add(personMap);
		}
		
        ArrayList<HashMap<PersonProperty, String>> personsListFromFile = peopleMap; 
		ALL_PERSONS.clear();
		ALL_PERSONS.addAll(personsListFromFile);
		
        while (true) {
		    System.out.print("|| " + "Enter command: ");
			
			String inputLine = SCANNER.nextLine();
			
			// silently consume all blank and comment lines
			while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == INPUT_COMMENT_MARKER) {
			    inputLine = SCANNER.nextLine();
			}
			String userCommand = inputLine;
			
		    String[] message4 = { "[Command entered:" + userCommand + "]" };
			for (String m3 : message4) {
			    System.out.println("|| " + m3);
			}
			
		    String feedbackToPrint = executeCommand(userCommand);
		    
		    String[] message5 = { feedbackToPrint, DIVIDER };
			for (String m4 : message5) {
			    System.out.println("|| " + m4);
			}
		}
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
        
    	switch ((userInputString.trim().split("\\s+", 2).length == 2 ? userInputString.trim().split("\\s+", 2) : new String[] { userInputString.trim().split("\\s+", 2)[0] , "" })[0]) {
	        case COMMAND_ADD_WORD:
				// try decoding a person from the raw args
				final Optional<String[]> decodedResult = decodePersonFromString((userInputString.trim().split("\\s+", 2).length == 2 ? userInputString.trim().split("\\s+", 2) : new String[] { userInputString.trim().split("\\s+", 2)[0] , "" })[1]);
				
				// checks if args are valid (decode result will not be present if the person is invalid)
				if (!decodedResult.isPresent()) {
				    return String.format(MESSAGE_INVALID_COMMAND_FORMAT, COMMAND_ADD_WORD, String.format("%1$s: %2$s", COMMAND_ADD_WORD, COMMAND_ADD_DESC) + System.lineSeparator() + "|| "
					+ String.format("\tParameters: %1$s", COMMAND_ADD_PARAMETERS) + System.lineSeparator() + "|| "
					+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_ADD_EXAMPLE) + System.lineSeparator() + "|| ");
				}
				
				// add the person as specified
			HashMap<PersonProperty, String> personMapObject = new HashMap<>();
				personMapObject.put(PersonProperty.NAME, decodedResult.get()[0]);
				personMapObject.put(PersonProperty.PHONE, decodedResult.get()[1]);
				personMapObject.put(PersonProperty.EMAIL, decodedResult.get()[2]);
				
				ALL_PERSONS.add(personMapObject);
				for (HashMap<PersonProperty, String> person : ALL_PERSONS) {
				    new ArrayList<>().add(String.format("%1$s " // name
					            + "p/" + "%2$s " // phone
					            + "e/" + "%3$s",
					person.get(PersonProperty.NAME), person.get(PersonProperty.PHONE), person.get(PersonProperty.EMAIL)));
				}
				final ArrayList<String> linesToWrite = new ArrayList<>();
				
				try {
				    Files.write(Paths.get(storageFilePath), linesToWrite);
				    
				} catch (IOException ioe) {
				    String[] message3 = { String.format(MESSAGE_ERROR_WRITING_TO_FILE, storageFilePath) };
					for (String m3 : message3) {
					    System.out.println("|| " + m3);
					}
				    String[] message11 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
					for (String m11 : message11) {
					    System.out.println("|| " + m11);
					}
					System.exit(0);
				    
				}
				return String.format("New person added: %1$s, Phone: %2$s, Email: %3$s",
						personMapObject.get(PersonProperty.NAME), personMapObject.get(PersonProperty.PHONE), personMapObject.get(PersonProperty.EMAIL));
				
	        case COMMAND_FIND_WORD:
				String findPersonCommandArgsTrimmed = (userInputString.trim().split("\\s+", 2).length == 2 ? userInputString.trim().split("\\s+", 2) : new String[] { userInputString.trim().split("\\s+", 2)[0] , "" })[1].trim();
				
				ArrayList<String> keywords1 = new ArrayList<String>(Arrays.asList(findPersonCommandArgsTrimmed.trim().split("\\s+")));
				final Set<String> keywords = new HashSet<>(keywords1);
				final ArrayList<HashMap<PersonProperty, String>> matchedPersons = new ArrayList<>();
				
				for (HashMap<PersonProperty, String> person1 : ALL_PERSONS) {
					        	
				    if (!Collections.disjoint(new HashSet<>(new ArrayList<String>(Arrays.asList(person1.get(PersonProperty.NAME).trim().split("\\s+")))), keywords)) {
				        matchedPersons.add(person1);
				    }
				}
				final ArrayList<HashMap<PersonProperty, String>> personsFound = matchedPersons;
				System.out.println("|| " +  getDisplayString(personsFound));
				
				// clone to insulate from future changes to arg list
				latestPersonListingView = new ArrayList<>(personsFound);
				return String.format(MESSAGE_PERSONS_FOUND_OVERVIEW, personsFound.size());
				
	        case COMMAND_LIST_WORD:
	        	System.out.println("|| " + getDisplayString(ALL_PERSONS));
				
			// clone to insulate from future changes to arg list
				latestPersonListingView = new ArrayList<>(ALL_PERSONS);
				return String.format(MESSAGE_PERSONS_FOUND_OVERVIEW, ALL_PERSONS.size());
	        case COMMAND_DELETE_WORD:
				if (!isDeletePersonArgsValid((userInputString.trim().split("\\s+", 2).length == 2 ? userInputString.trim().split("\\s+", 2) : new String[] { userInputString.trim().split("\\s+", 2)[0] , "" })[1])) {
				    return String.format(MESSAGE_INVALID_COMMAND_FORMAT, COMMAND_DELETE_WORD, String.format("%1$s: %2$s", COMMAND_DELETE_WORD, COMMAND_DELETE_DESC) + System.lineSeparator() + "|| "
					+ String.format("\tParameters: %1$s", COMMAND_DELETE_PARAMETER) + System.lineSeparator() + "|| "
					+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_DELETE_EXAMPLE) + System.lineSeparator() + "|| ");
				}
				
				final int targetVisibleIndex = Integer.parseInt((userInputString.trim().split("\\s+", 2).length == 2 ? userInputString.trim().split("\\s+", 2) : new String[] { userInputString.trim().split("\\s+", 2)[0] , "" })[1].trim());
				
				if (!(targetVisibleIndex >= DISPLAYED_INDEX_OFFSET && targetVisibleIndex < getLatestPersonListingView().size() + DISPLAYED_INDEX_OFFSET)) {
				    return MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
				}
				
				final HashMap<PersonProperty, String> targetInModel = latestPersonListingView.get(targetVisibleIndex - DISPLAYED_INDEX_OFFSET);
				
				return deletePersonFromAddressBook(targetInModel) ? String.format(MESSAGE_DELETE_PERSON_SUCCESS, getMessageForFormattedPersonData(targetInModel)) // success
				                                                  : MESSAGE_PERSON_NOT_IN_ADDRESSBOOK; // not found
	        case COMMAND_CLEAR_WORD:
				ALL_PERSONS.clear();
				for (HashMap<PersonProperty, String> person2 : ALL_PERSONS) {
				    new ArrayList<>().add(String.format("%1$s " // name
					            + "p/" + "%2$s " // phone
					            + "e/" + "%3$s",
					person2.get(PersonProperty.NAME), person2.get(PersonProperty.PHONE), person2.get(PersonProperty.EMAIL)));
				}
				final ArrayList<String> linesToWrite1 = new ArrayList<>();
				
				try {
				    Files.write(Paths.get(storageFilePath), linesToWrite1);
				    
				} catch (IOException ioe) {
				    for (String m1 : new String[] { String.format(MESSAGE_ERROR_WRITING_TO_FILE, storageFilePath) }) {
					    System.out.println("|| " + m1);
					}
				    for (String m1 : new String[] { MESSAGE_GOODBYE, DIVIDER, DIVIDER }) {
					    System.out.println("|| " + m1);
					}
					System.exit(0);
				    
				}
				return "Address book has been cleared!";
	        case COMMAND_HELP_WORD:
				return String.format("%1$s: %2$s", COMMAND_ADD_WORD, COMMAND_ADD_DESC) + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", COMMAND_ADD_PARAMETERS) + System.lineSeparator() + "|| "
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_ADD_EXAMPLE) + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_FIND_WORD, COMMAND_FIND_DESC) + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", COMMAND_FIND_PARAMETERS) + System.lineSeparator() + "|| "
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_FIND_EXAMPLE) + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_LIST_WORD, COMMAND_LIST_DESC) + System.lineSeparator() + "|| "
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_LIST_EXAMPLE) + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_DELETE_WORD, COMMAND_DELETE_DESC) + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", COMMAND_DELETE_PARAMETER) + System.lineSeparator() + "|| "
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_DELETE_EXAMPLE) + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_CLEAR_WORD, COMMAND_CLEAR_DESC) + System.lineSeparator() + "|| "
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_CLEAR_EXAMPLE) + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_EXIT_WORD, COMMAND_EXIT_DESC)
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_EXIT_EXAMPLE) + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_HELP_WORD, COMMAND_HELP_DESC)
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_HELP_EXAMPLE);
	        case COMMAND_EXIT_WORD:
	        	for (String m : new String[] { MESSAGE_GOODBYE, DIVIDER, DIVIDER }) {
				    System.out.println("|| " + m);
				}
				System.exit(0);
	        default:
				return String.format(MESSAGE_INVALID_COMMAND_FORMAT, (userInputString.trim().split("\\s+", 2).length == 2 ? userInputString.trim().split("\\s+", 2) : new String[] { userInputString.trim().split("\\s+", 2)[0] , "" })[0], String.format("%1$s: %2$s", COMMAND_ADD_WORD, COMMAND_ADD_DESC) + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", COMMAND_ADD_PARAMETERS) + System.lineSeparator() + "|| "
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_ADD_EXAMPLE) + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_FIND_WORD, COMMAND_FIND_DESC) + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", COMMAND_FIND_PARAMETERS) + System.lineSeparator() + "|| "
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_FIND_EXAMPLE) + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_LIST_WORD, COMMAND_LIST_DESC) + System.lineSeparator() + "|| "
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_LIST_EXAMPLE) + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_DELETE_WORD, COMMAND_DELETE_DESC) + System.lineSeparator() + "|| "
				+ String.format("\tParameters: %1$s", COMMAND_DELETE_PARAMETER) + System.lineSeparator() + "|| "
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_DELETE_EXAMPLE) + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_CLEAR_WORD, COMMAND_CLEAR_DESC) + System.lineSeparator() + "|| "
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_CLEAR_EXAMPLE) + System.lineSeparator() + "|| " + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_EXIT_WORD, COMMAND_EXIT_DESC)
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_EXIT_EXAMPLE) + System.lineSeparator() + "|| "
				+ String.format("%1$s: %2$s", COMMAND_HELP_WORD, COMMAND_HELP_DESC)
				+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_HELP_EXAMPLE));
        }
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
            
            boolean isValid = extractedIndex >= DISPLAYED_INDEX_OFFSET;
            return isValid;
            
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Returns the display string representation of the list of persons.
     */
    private static String getDisplayString(ArrayList<HashMap<PersonProperty, String>> persons) {
        
    	final StringBuilder messageAccumulator = new StringBuilder();
        
    	for (int i = 0; i < persons.size(); i++) {
            final HashMap<PersonProperty, String> person = persons.get(i);
            final int displayIndex = i + DISPLAYED_INDEX_OFFSET;
            messageAccumulator.append('\t')
                              .append(getIndexedPersonListElementMessage(displayIndex, person))
                              .append(System.lineSeparator() + "|| ");
        }
        
    	return messageAccumulator.toString();
    }

    /**
     * Constructs a prettified listing element message to represent a person and their data.
     *
     * @param visibleIndex visible index for this listing
     * @param person to show
     * @return formatted listing message with index
     */
    private static String getIndexedPersonListElementMessage(int visibleIndex, HashMap<PersonProperty, String> person) {
        return String.format(MESSAGE_DISPLAY_LIST_ELEMENT_INDEX, visibleIndex) 
        		+ getMessageForFormattedPersonData(person);
    }

    /**
     * Constructs a prettified string to show the user a person's data.
     *
     * @param person to show
     * @return formatted message showing internal state
     */
    private static String getMessageForFormattedPersonData(HashMap<PersonProperty, String> person) {
        return String.format(MESSAGE_DISPLAY_PERSON_DATA,
                person.get(PersonProperty.NAME), person.get(PersonProperty.PHONE), person.get(PersonProperty.EMAIL));
    }

    /**
     * @return unmodifiable list view of the last person listing view
     */
    private static ArrayList<HashMap<PersonProperty, String>> getLatestPersonListingView() {
        return latestPersonListingView;
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
		    System.out.println("|| " + m);
		}

        try {
            storageFile.createNewFile();
			String[] message1 = { String.format(MESSAGE_STORAGE_FILE_CREATED, filePath) };
            for (String m : message1) {
			    System.out.println("|| " + m);
			}
            
        } catch (IOException ioe) {
            String[] message1 = { String.format(MESSAGE_ERROR_CREATING_STORAGE_FILE, filePath) };
			for (String m : message1) {
			    System.out.println("|| " + m);
			}
            String[] message2 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message2) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
        }
    }

    
    /**
     * Gets all lines in the specified file as a list of strings. Line separators are removed.
     * Shows error messages and exits program if unable to read from file.
     */
    private static ArrayList<String> getLinesInFile(String filePath) {
    	
        ArrayList<String> linesInFile = null;
        
        try {
            linesInFile = new ArrayList<String>(Files.readAllLines(Paths.get(filePath)));
            
        } catch (FileNotFoundException fnfe) {
            String[] message = { String.format(MESSAGE_ERROR_MISSING_STORAGE_FILE, filePath) };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
            String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
            
        } catch (IOException ioe) {
            String[] message = { String.format(MESSAGE_ERROR_READING_FROM_FILE, filePath) };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
            String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
            
        }
        
        return linesInFile;
    }

    /*
     * ================================================================================
     *        INTERNAL ADDRESS BOOK DATA METHODS
     * ================================================================================
     */

    /**
     * Deletes a person from the address book, target is identified by it's absolute index in the full list.
     * Saves changes to storage file.
     *
     * @param index absolute index of person to delete (index within {@link #ALL_PERSONS})
     */
    private static void deletePersonFromAddressBook(int index) {
        ALL_PERSONS.remove(index);
        for (HashMap<PersonProperty, String> person : ALL_PERSONS) {
		    new ArrayList<>().add(String.format("%1$s " // name
			            + "p/" + "%2$s " // phone
			            + "e/" + "%3$s",
			person.get(PersonProperty.NAME), person.get(PersonProperty.PHONE), person.get(PersonProperty.EMAIL)));
		}
		final ArrayList<String> linesToWrite = new ArrayList<>();
		
		try {
		    Files.write(Paths.get(storageFilePath), linesToWrite);
		    
		} catch (IOException ioe) {
		    String[] message = { String.format(MESSAGE_ERROR_WRITING_TO_FILE, storageFilePath) };
			for (String m : message) {
			    System.out.println("|| " + m);
			}
		    String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
			for (String m1 : message1) {
			    System.out.println("|| " + m1);
			}
			System.exit(0);
		    
		}
    }

    /**
     * Deletes the specified person from the addressbook if it is inside. Saves any changes to storage file.
     *
     * @param exactPerson the actual person inside the address book (exactPerson == the person to delete in the full list)
     * @return true if the given person was found and deleted in the model
     */
    private static boolean deletePersonFromAddressBook(HashMap<PersonProperty, String> exactPerson) {
        final boolean hasDataChanged = ALL_PERSONS.remove(exactPerson);
        
        if (hasDataChanged) {
            for (HashMap<PersonProperty, String> person : ALL_PERSONS) {
			    new ArrayList<>().add(String.format("%1$s " // name
				            + "p/" + "%2$s " // phone
				            + "e/" + "%3$s",
				person.get(PersonProperty.NAME), person.get(PersonProperty.PHONE), person.get(PersonProperty.EMAIL)));
			}
			final ArrayList<String> linesToWrite = new ArrayList<>();
			
			try {
			    Files.write(Paths.get(storageFilePath), linesToWrite);
			    
			} catch (IOException ioe) {
			    String[] message = { String.format(MESSAGE_ERROR_WRITING_TO_FILE, storageFilePath) };
				for (String m : message) {
				    System.out.println("|| " + m);
				}
			    String[] message1 = { MESSAGE_GOODBYE, DIVIDER, DIVIDER };
				for (String m1 : message1) {
				    System.out.println("|| " + m1);
				}
				System.exit(0);
			    
			}
        }
        
        return hasDataChanged;
    }

    /**
     * Create a person for use in the internal data.
     *
     * @param name of person
     * @param phone without data prefix
     * @param email without data prefix
     * @return constructed person
     */
    private static String[] makePersonFromData(String name, String phone, String email) {
        final String[] person = new String[PERSON_DATA_COUNT];
        person[0] = name;
        person[1] = phone;
        person[2] = email;
        return person;
    }

    
    /**
     * Decodes a person from it's supposed string representation.
     *
     * @param encoded string to be decoded
     * @return if cannot decode: empty Optional
     *         else: Optional containing decoded person
     */
    private static Optional<String[]> decodePersonFromString(String encoded) {
        // check that we can extract the parts of a person from the encoded string
        if (!(encoded.trim().split("p/" + '|' + "e/").length == 3 // 3 arguments
		&& !encoded.trim().split("p/" + '|' + "e/")[0].isEmpty() // non-empty arguments
		&& !encoded.trim().split("p/" + '|' + "e/")[1].isEmpty()
		&& !encoded.trim().split("p/" + '|' + "e/")[2].isEmpty())) {
            return Optional.empty();
        }
        // check that the constructed person is valid
        return makePersonFromData(
                encoded.substring(0, Math.min(encoded.indexOf("e/"), encoded.indexOf("p/"))).trim(),
                (encoded.indexOf("p/") > encoded.indexOf("e/")) ? encoded.substring(encoded.indexOf("p/"), encoded.length()).trim().replace("p/", "")
				: encoded.substring(encoded.indexOf("p/"), encoded.indexOf("e/")).trim().replace("p/", ""),
                (encoded.indexOf("e/") > encoded.indexOf("p/")) ?  encoded.substring(encoded.indexOf("e/"), encoded.length()).trim().replace("e/", "")
				:encoded.substring(encoded.indexOf("e/"), encoded.indexOf("p/")).trim().replace("e/", "")
        )[0].matches("(\\w|\\s)+")
		&& makePersonFromData(
                encoded.substring(0, Math.min(encoded.indexOf("e/"), encoded.indexOf("p/"))).trim(),
                (encoded.indexOf("p/") > encoded.indexOf("e/")) ? encoded.substring(encoded.indexOf("p/"), encoded.length()).trim().replace("p/", "")
				: encoded.substring(encoded.indexOf("p/"), encoded.indexOf("e/")).trim().replace("p/", ""),
                (encoded.indexOf("e/") > encoded.indexOf("p/")) ?  encoded.substring(encoded.indexOf("e/"), encoded.length()).trim().replace("e/", "")
				:encoded.substring(encoded.indexOf("e/"), encoded.indexOf("p/")).trim().replace("e/", "")
        )[1].matches("\\d+")
		&& makePersonFromData(
                encoded.substring(0, Math.min(encoded.indexOf("e/"), encoded.indexOf("p/"))).trim(),
                (encoded.indexOf("p/") > encoded.indexOf("e/")) ? encoded.substring(encoded.indexOf("p/"), encoded.length()).trim().replace("p/", "")
				: encoded.substring(encoded.indexOf("p/"), encoded.indexOf("e/")).trim().replace("p/", ""),
                (encoded.indexOf("e/") > encoded.indexOf("p/")) ?  encoded.substring(encoded.indexOf("e/"), encoded.length()).trim().replace("e/", "")
				:encoded.substring(encoded.indexOf("e/"), encoded.indexOf("p/")).trim().replace("e/", "")
        )[2].matches("\\S+@\\S+\\.\\S+") ? Optional.of(makePersonFromData(
                encoded.substring(0, Math.min(encoded.indexOf("e/"), encoded.indexOf("p/"))).trim(),
                (encoded.indexOf("p/") > encoded.indexOf("e/")) ? encoded.substring(encoded.indexOf("p/"), encoded.length()).trim().replace("p/", "")
				: encoded.substring(encoded.indexOf("p/"), encoded.indexOf("e/")).trim().replace("p/", ""),
                (encoded.indexOf("e/") > encoded.indexOf("p/")) ?  encoded.substring(encoded.indexOf("e/"), encoded.length()).trim().replace("e/", "")
				:encoded.substring(encoded.indexOf("e/"), encoded.indexOf("p/")).trim().replace("e/", "")
        )) : Optional.empty();
    }

    /**
     * Decode persons from a list of string representations.
     *
     * @param encodedPersons strings to be decoded
     * @return if cannot decode any: empty Optional
     *         else: Optional containing decoded persons
     */
    private static Optional<ArrayList<String[]>> decodePersonsFromStrings(ArrayList<String> encodedPersons) {
        for (String encodedPerson : encodedPersons) {
            if (!decodePersonFromString(encodedPerson).isPresent()) {
                return Optional.empty();
            }
            new ArrayList<>().add(decodePersonFromString(encodedPerson).get());
        }
        return Optional.of(new ArrayList<>());
    }

}