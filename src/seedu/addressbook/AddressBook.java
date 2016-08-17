package seedu.addressbook;


import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;




public class AddressBook {


private static final String DEFAULT_STORAGE_FILEPATH = "addressbook.txt";


private static final String VERSION = "AddessBook Level 1 - Version 1.0";


public static final String LINE_PREFIX = "|| ";


private static final String LS = System.lineSeparator() + LINE_PREFIX;


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



private static final int PERSON_DATA_INDEX_NAME = 0;
private static final int PERSON_DATA_INDEX_PHONE = 1;
private static final int PERSON_DATA_INDEX_EMAIL = 2;


private static final int PERSON_DATA_COUNT = 3;


private static final int DISPLAYED_INDEX_OFFSET = 1;




private static final char INPUT_COMMENT_MARKER = '#';


private static final Scanner SCANNER = new Scanner(System.in);


private static final ArrayList<String[]> ALL_PERSONS = new ArrayList<>();



private static ArrayList<String[]> latestPersonListingView = getAllPersonsInAddressBook(); // initial view is of all


private static String storageFilePath;


public static void main(String[] args) {
showWelcomeMessage();
processProgramArgs(args);
loadDataFromStorage();
while (true) {
String userCommand = getUserInput();
echoUserCommand(userCommand);
String feedback = executeCommand(userCommand);
showResultToUser(feedback);
}
}


private static void showWelcomeMessage() {
showToUser(DIVIDER, DIVIDER, VERSION, MESSAGE_WELCOME, DIVIDER);
}

private static void showResultToUser(String result) {
showToUser(result, DIVIDER);
}



private static void echoUserCommand(String userCommand) {
showToUser("[Command entered:" + userCommand + "]");
}




private static void processProgramArgs(String[] args) {
if (args.length >= 2) {
showToUser(MESSAGE_INVALID_PROGRAM_ARGS);
exitProgram();
}

if (args.length == 1) {
setupGivenFileForStorage(args[0]);
}

if(args.length == 0) {
setupDefaultFileForStorage();
}
}


private static void setupGivenFileForStorage(String filePath) {

if (!isValidFilePath(filePath)) {
showToUser(String.format(MESSAGE_INVALID_FILE, filePath));
exitProgram();
}

storageFilePath = filePath;
createFileIfMissing(filePath);
}


private static void exitProgram() {
showToUser(MESSAGE_GOODBYE, DIVIDER, DIVIDER);
System.exit(0);
}


private static void setupDefaultFileForStorage() {
showToUser(MESSAGE_USING_DEFAULT_FILE);
storageFilePath = DEFAULT_STORAGE_FILEPATH;
createFileIfMissing(storageFilePath);
}


private static boolean isValidFilePath(String filePath) {
return filePath.endsWith(".txt");
}


private static void loadDataFromStorage() {
initialiseAddressBookModel(loadPersonsFromFile(storageFilePath));
}





public static String executeCommand(String userInputString) {
final String[] commandTypeAndParams = splitCommandWordAndArgs(userInputString);
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
return executeClearAddressBook();
case COMMAND_HELP_WORD:
return getUsageInfoForAllCommands();
case COMMAND_EXIT_WORD:
executeExitProgramRequest();
default:
return getMessageForInvalidCommandInput(commandType, getUsageInfoForAllCommands());
}
}


private static String[] splitCommandWordAndArgs(String rawUserInput) {
final String[] split =  rawUserInput.trim().split("\\s+", 2);
return split.length == 2 ? split : new String[] { split[0] , "" }; // else case: no parameters
}


private static String getMessageForInvalidCommandInput(String userCommand, String correctUsageInfo) {
return String.format(MESSAGE_INVALID_COMMAND_FORMAT, userCommand, correctUsageInfo);
}


private static String executeAddPerson(String commandArgs) {
// try decoding a person from the raw args
final Optional<String[]> decodeResult = decodePersonFromString(commandArgs);

// checks if args are valid (decode result will not be present if the person is invalid)
if (!decodeResult.isPresent()) {
return getMessageForInvalidCommandInput(COMMAND_ADD_WORD, getUsageInfoForAddCommand());
}

// add the person as specified
final String[] personToAdd = decodeResult.get();
addPersonToAddressBook(personToAdd);
return getMessageForSuccessfulAddPerson(personToAdd);
}


private static String getMessageForSuccessfulAddPerson(String[] addedPerson) {
return String.format(MESSAGE_ADDED,
getNameFromPerson(addedPerson), getPhoneFromPerson(addedPerson), getEmailFromPerson(addedPerson));
}


private static String executeFindPersons(String commandArgs) {
final Set<String> keywords = extractKeywordsFromFindPersonArgs(commandArgs);
final ArrayList<String[]> personsFound = getPersonsWithNameContainingAnyKeyword(keywords);
showToUser(personsFound);
return getMessageForPersonsDisplayedSummary(personsFound);
}


private static String getMessageForPersonsDisplayedSummary(ArrayList<String[]> personsDisplayed) {
return String.format(MESSAGE_PERSONS_FOUND_OVERVIEW, personsDisplayed.size());
}


private static Set<String> extractKeywordsFromFindPersonArgs(String findPersonCommandArgs) {
return new HashSet<>(splitByWhitespace(findPersonCommandArgs.trim()));
}


private static ArrayList<String[]> getPersonsWithNameContainingAnyKeyword(Collection<String> keywords) {
final ArrayList<String[]> matchedPersons = new ArrayList<>();
for (String[] person : getAllPersonsInAddressBook()) {
final Set<String> wordsInName = new HashSet<>(splitByWhitespace(getNameFromPerson(person)));
if (!Collections.disjoint(wordsInName, keywords)) {
matchedPersons.add(person);
}
}
return matchedPersons;
}


private static String executeDeletePerson(String commandArgs) {
if (!isDeletePersonArgsValid(commandArgs)) {
return getMessageForInvalidCommandInput(COMMAND_DELETE_WORD, getUsageInfoForDeleteCommand());
}
final int targetVisibleIndex = extractTargetIndexFromDeletePersonArgs(commandArgs);
if (!isDisplayIndexValidForLastPersonListingView(targetVisibleIndex)) {
return MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
}
final String[] targetInModel = getPersonByLastVisibleIndex(targetVisibleIndex);
return deletePersonFromAddressBook(targetInModel) ? getMessageForSuccessfulDelete(targetInModel) // success
  : MESSAGE_PERSON_NOT_IN_ADDRESSBOOK; // not found
}


private static boolean isDeletePersonArgsValid(String rawArgs) {
try {
final int extractedIndex = Integer.parseInt(rawArgs.trim()); // use standard libraries to parse
return extractedIndex >= DISPLAYED_INDEX_OFFSET;
} catch (NumberFormatException nfe) {
return false;
}
}


private static int extractTargetIndexFromDeletePersonArgs(String rawArgs) {
return Integer.parseInt(rawArgs.trim());
}


private static boolean isDisplayIndexValidForLastPersonListingView(int index) {
return index >= DISPLAYED_INDEX_OFFSET && index < getLatestPersonListingView().size() + DISPLAYED_INDEX_OFFSET;
}


private static String getMessageForSuccessfulDelete(String[] deletedPerson) {
return String.format(MESSAGE_DELETE_PERSON_SUCCESS, getMessageForFormattedPersonData(deletedPerson));
}


private static String executeClearAddressBook() {
clearAddressBook();
return MESSAGE_ADDRESSBOOK_CLEARED;
}


private static String executeListAllPersonsInAddressBook() {
ArrayList<String[]> toBeDisplayed = getAllPersonsInAddressBook();
showToUser(toBeDisplayed);
return getMessageForPersonsDisplayedSummary(toBeDisplayed);
}


private static void executeExitProgramRequest() {
exitProgram();
}




private static String getUserInput() {
System.out.print(LINE_PREFIX + "Enter command: ");
String inputLine = SCANNER.nextLine();
// silently consume all blank and comment lines
while (inputLine.trim().isEmpty() || inputLine.trim().charAt(0) == INPUT_COMMENT_MARKER) {
inputLine = SCANNER.nextLine();
}
return inputLine;
}

   

private static void showToUser(String... message) {
for (String m : message) {
System.out.println(LINE_PREFIX + m);
}
}


private static void showToUser(ArrayList<String[]> persons) {
String listAsString = getDisplayString(persons);
showToUser(listAsString);
updateLatestViewedPersonListing(persons);
}


private static String getDisplayString(ArrayList<String[]> persons) {
final StringBuilder messageAccumulator = new StringBuilder();
for (int i = 0; i < persons.size(); i++) {
final String[] person = persons.get(i);
final int displayIndex = i + DISPLAYED_INDEX_OFFSET;
messageAccumulator.append('\t')
  .append(getIndexedPersonListElementMessage(displayIndex, person))
  .append(LS);
}
return messageAccumulator.toString();
}


private static String getIndexedPersonListElementMessage(int visibleIndex, String[] person) {
return String.format(MESSAGE_DISPLAY_LIST_ELEMENT_INDEX, visibleIndex) + getMessageForFormattedPersonData(person);
}


private static String getMessageForFormattedPersonData(String[] person) {
return String.format(MESSAGE_DISPLAY_PERSON_DATA,
getNameFromPerson(person), getPhoneFromPerson(person), getEmailFromPerson(person));
}


private static void updateLatestViewedPersonListing(ArrayList<String[]> newListing) {
// clone to insulate from future changes to arg list
latestPersonListingView = new ArrayList<>(newListing);
}


private static String[] getPersonByLastVisibleIndex(int lastVisibleIndex) {
   return latestPersonListingView.get(lastVisibleIndex - DISPLAYED_INDEX_OFFSET);
}


private static ArrayList<String[]> getLatestPersonListingView() {
return latestPersonListingView;
}





private static void createFileIfMissing(String filePath) {
final File storageFile = new File(filePath);
if (storageFile.exists()) {
return;
}

showToUser(String.format(MESSAGE_ERROR_MISSING_STORAGE_FILE, filePath));

try {
storageFile.createNewFile();
showToUser(String.format(MESSAGE_STORAGE_FILE_CREATED, filePath));
} catch (IOException ioe) {
showToUser(String.format(MESSAGE_ERROR_CREATING_STORAGE_FILE, filePath));
exitProgram();
}
}


private static ArrayList<String[]> loadPersonsFromFile(String filePath) {
final Optional<ArrayList<String[]>> successfullyDecoded = decodePersonsFromStrings(getLinesInFile(filePath));
if (!successfullyDecoded.isPresent()) {
showToUser(MESSAGE_INVALID_STORAGE_FILE_CONTENT);
exitProgram();
}
return successfullyDecoded.get();
}


private static ArrayList<String> getLinesInFile(String filePath) {
ArrayList<String> lines = null;
try {
lines = new ArrayList(Files.readAllLines(Paths.get(filePath)));
} catch (FileNotFoundException fnfe) {
showToUser(String.format(MESSAGE_ERROR_MISSING_STORAGE_FILE, filePath));
exitProgram();
} catch (IOException ioe) {
showToUser(String.format(MESSAGE_ERROR_READING_FROM_FILE, filePath));
exitProgram();
}
return lines;
}


private static void savePersonsToFile(ArrayList<String[]> persons, String filePath) {
final ArrayList<String> linesToWrite = encodePersonsToStrings(persons);
try {
Files.write(Paths.get(storageFilePath), linesToWrite);
} catch (IOException ioe) {
showToUser(String.format(MESSAGE_ERROR_WRITING_TO_FILE, filePath));
exitProgram();
}
}





private static void addPersonToAddressBook(String[] person) {
ALL_PERSONS.add(person);
savePersonsToFile(getAllPersonsInAddressBook(), storageFilePath);
}


private static void deletePersonFromAddressBook(int index) {
ALL_PERSONS.remove(index);
savePersonsToFile(getAllPersonsInAddressBook(), storageFilePath);
}


private static boolean deletePersonFromAddressBook(String[] exactPerson) {
final boolean changed = ALL_PERSONS.remove(exactPerson);
if (changed) {
savePersonsToFile(getAllPersonsInAddressBook(), storageFilePath);
}
return changed;
}


private static ArrayList<String[]> getAllPersonsInAddressBook() {
return ALL_PERSONS;
}


private static void clearAddressBook() {
ALL_PERSONS.clear();
savePersonsToFile(getAllPersonsInAddressBook(), storageFilePath);
}


private static void initialiseAddressBookModel(ArrayList<String[]> persons) {
ALL_PERSONS.clear();
ALL_PERSONS.addAll(persons);
}





private static String getNameFromPerson(String[] person) {
return person[PERSON_DATA_INDEX_NAME];
}


private static String getPhoneFromPerson(String[] person) {
return person[PERSON_DATA_INDEX_PHONE];
}


private static String getEmailFromPerson(String[] person) {
return person[PERSON_DATA_INDEX_EMAIL];
}


private static String[] makePersonFromData(String name, String phone, String email) {
final String[] person = new String[PERSON_DATA_COUNT];
person[PERSON_DATA_INDEX_NAME] = name;
person[PERSON_DATA_INDEX_PHONE] = phone;
person[PERSON_DATA_INDEX_EMAIL] = email;
return person;
}


private static String encodePersonToString(String[] person) {
return String.format(PERSON_STRING_REPRESENTATION,
getNameFromPerson(person), getPhoneFromPerson(person), getEmailFromPerson(person));
}


private static ArrayList<String> encodePersonsToStrings(ArrayList<String[]> persons) {
final ArrayList<String> encoded = new ArrayList<>();
for (String[] person : persons) {
encoded.add(encodePersonToString(person));
}
return encoded;
}



private static Optional<String[]> decodePersonFromString(String encoded) {
// check that we can extract the parts of a person from the encoded string
if (!isPersonDataExtractableFrom(encoded)) {
return Optional.empty();
}
final String[] decodedPerson = makePersonFromData(
extractNameFromPersonString(encoded),
extractPhoneFromPersonString(encoded),
extractEmailFromPersonString(encoded)
);
// check that the constructed person is valid
return isPersonDataValid(decodedPerson) ? Optional.of(decodedPerson) : Optional.empty();
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
final String matchAnyPersonDataPrefix = PERSON_DATA_PREFIX_PHONE + '|' + PERSON_DATA_PREFIX_EMAIL;
final String[] splitArgs = personData.trim().split(matchAnyPersonDataPrefix);
return splitArgs.length == 3 // 3 arguments
&& !splitArgs[0].isEmpty() // non-empty arguments
&& !splitArgs[1].isEmpty()
&& !splitArgs[2].isEmpty();
}


private static String extractNameFromPersonString(String encoded) {
final int indexOfPhonePrefix = encoded.indexOf(PERSON_DATA_PREFIX_PHONE);
final int indexOfEmailPrefix = encoded.indexOf(PERSON_DATA_PREFIX_EMAIL);
// name is leading substring up to first data prefix symbol
int indexOfFirstPrefix = Math.min(indexOfEmailPrefix, indexOfPhonePrefix);
return encoded.substring(0, indexOfFirstPrefix).trim();
}


private static String extractPhoneFromPersonString(String encoded) {
final int indexOfPhonePrefix = encoded.indexOf(PERSON_DATA_PREFIX_PHONE);
final int indexOfEmailPrefix = encoded.indexOf(PERSON_DATA_PREFIX_EMAIL);

// phone is last arg, target is from prefix to end of string
if (indexOfPhonePrefix > indexOfEmailPrefix) {
return removePrefixSign(encoded.substring(indexOfPhonePrefix, encoded.length()).trim(),
PERSON_DATA_PREFIX_PHONE);

// phone is middle arg, target is from own prefix to next prefix
} else {
return removePrefixSign(
encoded.substring(indexOfPhonePrefix, indexOfEmailPrefix).trim(),
PERSON_DATA_PREFIX_PHONE);
}
}


private static String extractEmailFromPersonString(String encoded) {
final int indexOfPhonePrefix = encoded.indexOf(PERSON_DATA_PREFIX_PHONE);
final int indexOfEmailPrefix = encoded.indexOf(PERSON_DATA_PREFIX_EMAIL);

// email is last arg, target is from prefix to end of string
if (indexOfEmailPrefix > indexOfPhonePrefix) {
return removePrefixSign(encoded.substring(indexOfEmailPrefix, encoded.length()).trim(),
PERSON_DATA_PREFIX_EMAIL);

// email is middle arg, target is from own prefix to next prefix
} else {
return removePrefixSign(
encoded.substring(indexOfEmailPrefix, indexOfPhonePrefix).trim(),
PERSON_DATA_PREFIX_EMAIL);
}
}


private static boolean isPersonDataValid(String[] person) {
return isPersonNameValid(person[PERSON_DATA_INDEX_NAME])
&& isPersonPhoneValid(person[PERSON_DATA_INDEX_PHONE])
&& isPersonEmailValid(person[PERSON_DATA_INDEX_EMAIL]);
}



private static boolean isPersonNameValid(String name) {
return name.matches("(\\w|\\s)+");  // name is nonempty mixture of alphabets and whitespace
//TODO: implement a more permissive validation
}


private static boolean isPersonPhoneValid(String phone) {
return phone.matches("\\d+");// phone nonempty sequence of digits
//TODO: implement a more permissive validation
}


private static boolean isPersonEmailValid(String email) {
return email.matches("\\S+@\\S+\\.\\S+"); // email is [non-whitespace]@[non-whitespace].[non-whitespace]
//TODO: implement a more permissive validation
}





private static String getUsageInfoForAllCommands() {
return getUsageInfoForAddCommand() + LS
+ getUsageInfoForFindCommand() + LS
+ getUsageInfoForViewCommand() + LS
+ getUsageInfoForDeleteCommand() + LS
+ getUsageInfoForClearCommand() + LS
+ getUsageInfoForExitCommand() + LS
+ getUsageInfoForHelpCommand();
}


private static String getUsageInfoForAddCommand() {
return String.format(MESSAGE_COMMAND_HELP, COMMAND_ADD_WORD, COMMAND_ADD_DESC) + LS
+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_ADD_PARAMETERS) + LS
+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_ADD_EXAMPLE) + LS;
}


private static String getUsageInfoForFindCommand() {
return String.format(MESSAGE_COMMAND_HELP, COMMAND_FIND_WORD, COMMAND_FIND_DESC) + LS
+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_FIND_PARAMETERS) + LS
+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_FIND_EXAMPLE) + LS;
}


private static String getUsageInfoForDeleteCommand() {
return String.format(MESSAGE_COMMAND_HELP, COMMAND_DELETE_WORD, COMMAND_DELETE_DESC) + LS
+ String.format(MESSAGE_COMMAND_HELP_PARAMETERS, COMMAND_DELETE_PARAMETER) + LS
+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_DELETE_EXAMPLE) + LS;
}


private static String getUsageInfoForClearCommand() {
return String.format(MESSAGE_COMMAND_HELP, COMMAND_CLEAR_WORD, COMMAND_CLEAR_DESC) + LS
+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_CLEAR_EXAMPLE) + LS;
}


private static String getUsageInfoForViewCommand() {
return String.format(MESSAGE_COMMAND_HELP, COMMAND_LIST_WORD, COMMAND_LIST_DESC) + LS
+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_LIST_EXAMPLE) + LS;
}


private static String getUsageInfoForHelpCommand() {
return String.format(MESSAGE_COMMAND_HELP, COMMAND_HELP_WORD, COMMAND_HELP_DESC)
+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_HELP_EXAMPLE);
}


private static String getUsageInfoForExitCommand() {
return String.format(MESSAGE_COMMAND_HELP, COMMAND_EXIT_WORD, COMMAND_EXIT_DESC)
+ String.format(MESSAGE_COMMAND_HELP_EXAMPLE, COMMAND_EXIT_EXAMPLE);
}





private static String removePrefixSign(String s, String sign) {
return s.replace(sign, "");
}


private static ArrayList<String> splitByWhitespace(String toSplit) {
return new ArrayList(Arrays.asList(toSplit.trim().split("\\s+")));
}

}