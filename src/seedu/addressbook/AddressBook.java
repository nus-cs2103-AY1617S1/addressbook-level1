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
    private static final String a = "addressbook.txt";

    /**
     * Version info of the program.
     */
    private static final String b = "AddessBook Level 1 - Version 1.0";

    /**
     * A decorative prefix added to the beginning of lines printed by AddressBook
     */
    public static final String c = "|| ";

    /**
     * A platform independent line separator.
     */
    private static final String d = System.lineSeparator() + c;

    /*
     * ==============NOTE TO STUDENTS======================================
     * These messages shown to the user are defined in one place for convenient
     * editing and proof reading. Such messages are considered part of the UI
     * and may be subjected to review by UI experts or technical writers. Note
     * that Some of the strings below include '%1$s' etc to mark the locations
     * at which java String.format(...) method can insert values.
     * ====================================================================
     */
    private static final String e = "New person added: %1$s, Phone: %2$s, Email: %3$s";
    private static final String f = "Address book has been cleared!";
    private static final String g = "%1$s: %2$s";
    private static final String h = "\tParameters: %1$s";
    private static final String i = "\tExample: %1$s";
    private static final String j = "Deleted Person: %1$s";
    private static final String k = "%1$s  Phone Number: %2$s  Email: %3$s";
    private static final String l = "%1$d. ";
    private static final String m = "Exiting Address Book... Good bye!";
    private static final String n = "Invalid command format: %1$s " + d + "%2$s";
    private static final String o = "The given file name [%1$s] is not a valid file name!";
    private static final String p = "Too many parameters! Correct program argument format:"
                                                            + d + "\tjava AddressBook"
                                                            + d + "\tjava AddressBook [custom storage file path]";
    private static final String q = "The person index provided is invalid";
    private static final String r = "Storage file has invalid content";
    private static final String s = "Person could not be found in address book";
    private static final String t = "Error: unable to create file: %1$s";
    private static final String u = "Storage file missing: %1$s";
    private static final String v = "Unexpected error: unable to read from file: %1$s";
    private static final String w = "Unexpected error: unable to write to file: %1$s";
    private static final String x = "%1$d persons found!";
    private static final String y = "Created new empty storage file: %1$s";
    private static final String z = "Welcome to your Address Book!";
    private static final String aa = "Using default storage file : " + a;

    // These are the prefix strings to define the data type of a command parameter
    private static final String ab = "p/";
    private static final String ac = "e/";

    private static final String ad = "%1$s " // name
                                                            + ab + "%2$s " // phone
                                                            + ac + "%3$s"; // email
    private static final String ae = "add";
    private static final String af = "Adds a person to the address book.";
    private static final String ag = "NAME "
                                                      + ab + "PHONE_NUMBER "
                                                      + ac + "EMAIL";
    private static final String ah = ae + " John Doe p/98765432 e/johnd@gmail.com";

    private static final String ai = "find";
    private static final String aj = "Finds all persons whose names contain any of the specified "
                                        + "keywords (case-sensitive) and displays them as a list with index numbers.";
    private static final String ak = "KEYWORD [MORE_KEYWORDS]";
    private static final String al = ai + " alice bob charlie";

    private static final String am = "list";
    private static final String an = "Displays all persons as a list with index numbers.";
    private static final String ao = am;

    private static final String ap = "delete";
    private static final String aq = "Deletes a person identified by the index number used in "
                                                    + "the last find/list call.";
    private static final String ar = "INDEX";
    private static final String as = ap + " 1";

    private static final String at = "clear";
    private static final String au = "Clears address book permanently.";
    private static final String av = at;

    private static final String aw = "help";
    private static final String ax = "Shows program usage instructions.";
    private static final String ay = aw;

    private static final String az = "exit";
    private static final String ba = "Exits the program.";
    private static final String bb = az;

    private static final String bc = "===================================================";


    /* We use a String array to store details of a single person.
     * The constants given below are the indexes for the different data elements of a person
     * used by the internal String[] storage format.
     * For example, a person's name is stored as the 0th element in the array.
     */
    private static final int bd = 0;
    private static final int be = 1;
    private static final int bf = 2;

    /**
     * The number of data elements for a single person.
     */
    private static final int bg = 3;

    /**
     * Offset required to convert between 1-indexing and 0-indexing.COMMAND_
     */
    private static final int bh = 1;



    /**
     * If the first non-whitespace character in a user's input line is this, that line will be ignored.
     */
    private static final char bi = '#';

    /*
     * This variable is declared for the whole class (instead of declaring it
     * inside the readUserCommand() method to facilitate automated testing using
     * the I/O redirection technique. If not, only the first line of the input
     * text file will be processed.
     */
    private static final Scanner bj = new Scanner(System.in);
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
    private static final ArrayList<String[]> bk = new ArrayList<>();


    /**
     * Stores the most recent list of persons shown to the user as a result of a user command.
     * This is a subset of the full list. Deleting persons in the pull list does not delete
     * those persons from this list.
     */
    private static ArrayList<String[]> bl = gap(); // initial view is of all

    /**
     * The path to the file used for storing person data.
     */
    private static String bm;

    /*
     * ==============NOTE TO STUDENTS======================================
     * Notice how this method solves the whole problem at a very high level.
     * We can understand the high-level logic of the program by reading this
     * method alone.
     * ====================================================================
     */
    public static void main(String[] bv) {
        String[] bs = { bc, bc, b, z, bc };
		for (String m : bs) {
		    System.out.println(c + m);
		}
        if (bv.length >= 2) {
		    String[] bn = { p };
			for (String m1 : bn) {
			    System.out.println(c + m1);
			}
		    bo();
		}
		
		if (bv.length == 1) {
		    String bq = bv[0];
			if (!br(bq)) {
			    String[] bt = { String.format(o, bq) };
				for (String m1 : bt) {
				    System.out.println(c + m1);
				}
			    bo();
			}
			
			bm = bq;
			bw(bq);
		}
		
		if(bv.length == 0) {
		    String[] bx = { aa };
			for (String m1 : bx) {
			    System.out.println(c + m1);
			}
			bm = a;
			bw(bm);
		}
        bk.clear();
		bk.addAll(by(bm));
        while (true) {
            String az = ca();
			String[] a = { "[Command entered:" + az + "]" };
            for (String m1 : a) {
			    System.out.println(c + m1);
			}
            String b = bz(az);
			String[] z = { b, bc };
            for (String m2 : z) {
			    System.out.println(c + m2);
			}
        }
    }

    /*
     * ==============NOTE TO STUDENTS======================================
     * The method header comment can be omitted if the method is trivial
     * and the header comment is going to be almost identical to the method
     * signature anyway.
     * ====================================================================
     */
    private static void cb() {
        String[] message = { bc, bc, b, z, bc };
		for (String m : message) {
		    System.out.println(c + m);
		}
    }

    private static void srtu(String a) {
        String[] b = { a, bc };
		for (String m : b) {
		    System.out.println(c + m);
		}
    }

    /*
     * ==============NOTE TO STUDENTS======================================
     * Parameter description can be omitted from the method header comment
     * if the parameter name is self-explanatory.
     * In the method below, '@param userInput' comment has been omitted.
     * ====================================================================
     */
    /**
     * Echoes the user input back to the user.
     */
    private static void euc(String a) {
        String[] b = { "[Command entered:" + a + "]" };
		for (String m : b) {
		    System.out.println(c + m);
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
     * Processes the program main method run arguments.
     * If a valid storage file is specified, sets up that file for storage.
     * Otherwise sets up the default file for storage.
     *
     * @param a full program arguments passed to application main method
     */
    private static void pga(String[] a) {
        if (a.length >= 2) {
            String[] b = { p };
			for (String m : b) {
			    System.out.println(c + m);
			}
            bo();
        }

        if (a.length == 1) {
            sgfs(a[0]);
        }

        if(a.length == 0) {
            sdfs();
        }
    }

    /**
     * Sets up the storage file based on the supplied file path.
     * Creates the file if it is missing.
     * Exits if the file name is not acceptable.
     */
    private static void sgfs(String a) {

        if (!br(a)) {
            String[] message = { String.format(o, a) };
			for (String m : message) {
			    System.out.println(c + m);
			}
            bo();
        }

        bm = a;
        bw(a);
    }

    /**
     * Displays the goodbye message and exits the runtime.
     */
    private static void bo() {
        String[] a = { m, bc, bc };
		for (String m : a) {
		    System.out.println(c + m);
		}
        System.exit(0);
    }

    /**
     * Sets up the storage based on the default file.
     * Creates file if missing.
     * Exits program if the file cannot be created.
     */
    private static void sdfs() {
        String[] b = { aa };
		for (String m : b) {
		    System.out.println(c + m);
		}
        bm = a;
        bw(bm);
    }

    /**
     * Returns true if the given file is acceptable.
     * The file path is acceptable if it ends in '.txt'
     * TODO: Implement a more rigorous validity checking.
     */
    private static boolean br(String a) {
        return a.endsWith(".txt");
    }

    /**
     * Initialises the in-memory data using the storage file.
     * Assumption: The file exists.
     */
    private static void ldfs() {
        iab(by(bm));
    }


    /*
     * ===========================================
     *           COMMAND LOGIC
     * ===========================================
     */

    /**
     * Executes the command as specified by the {@code userInputString}
     *
     * @param b  raw input from user
     * @return  feedback about how the command was executed
     */
    public static String bz(String b) {
        final String[] a = scwa(b);
        final String c = a[0];
        final String d = a[1];
        switch (c) {
        case ae:
            return eaa(d);
        case ai:
            return efa(d);
        case am:
            return ela();
        case ap:
            return edp(d);
        case at:
            return eca();
        case aw:
            return geuifa();
        case az:
            ecpr();
        default:
            return gmfi(c, geuifa());
        }
    }

    /**
     * Splits raw user input into command word and command arguments string
     *
     * @return  size 2 array; first element is the command type and second element is the arguments string
     */
    private static String[] scwa(String a) {
        final String[] split =  a.trim().split("\\s+", 2);
        return split.length == 2 ? split : new String[] { split[0] , "" }; // else case: no parameters
    }

    /**
     * Constructs a generic feedback message for an invalid command from user, with instructions for correct usage.
     *
     * @param b message showing the correct usage
     * @return invalid command args feedback message
     */
    private static String gmfi(String a, String b) {
        return String.format(n, a, b);
    }

    /**
     * Adds a person (specified by the command args) to the address book.
     * The entire command arguments string is treated as a string representation of the person to add.
     *
     * @param a full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String eaa(String a) {
        // try decoding a person from the raw args
        final Optional<String[]> b = dpfs(a);

        // checks if args are valid (decode result will not be present if the person is invalid)
        if (!b.isPresent()) {
            return gmfi(ae, String.format(g, ae, af) + d
			+ String.format(h, ag) + d
			+ String.format(i, ah) + d);
        }

        // add the person as specified
        final String[] pta = b.get();
        ada(pta);
        return gemfsa(pta);
    }

    /**
     * Constructs a feedback message for a successful add person command execution.
     *
     * @see #eaa(String)
     * @param a person who was successfully added
     * @return successful add person feedback message
     */
    private static String gemfsa(String[] a) {
        return String.format(e,
                gn(a), gp(a), ge(a));
    }

    /**
     * Finds and lists all persons in address book whose name contains any of the argument keywords.
     * Keyword matching is case sensitive.
     *
     * @param a full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String efa(String a) {
        final Set<String> k = ekffp(a);
        final ArrayList<String[]> personsFound = gpnck(k);
        stu(personsFound);
        return gmfpm(personsFound);
    }

    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of persons.
     *
     * @param a used to generate summary
     * @return summary message for persons displayed
     */
    private static String gmfpm(ArrayList<String[]> a) {
        return String.format(x, a.size());
    }

    /**
     * Extract keywords from the command arguments given for the find persons command.
     *
     * @param a full command args string for the find persons command
     * @return set of keywords as specified by args
     */
    private static Set<String> ekffp(String a) {
        return new HashSet<>(sbw(a.trim()));
    }

    /**
     * Retrieve all persons in the full model whose names contain some of the specified keywords.
     *
     * @param a for searching
     * @return list of persons in full model with name containing some of the keywords
     */
    private static ArrayList<String[]> gpnck(Collection<String> a) {
        final ArrayList<String[]> b = new ArrayList<>();
        for (String[] d : gap()) {
            final Set<String> c = new HashSet<>(sbw(gn(d)));
            if (!Collections.disjoint(c, a)) {
                b.add(d);
            }
        }
        return b;
    }

    /**
     * Deletes person identified using last displayed index.
     *
     * @param b full command args string from the user
     * @return feedback display message for the operation result
     */
    private static String edp(String b) {
        if (!idav(b)) {
            return gmfi(ap, guifdc());
        }
        final int a = etifdp(b);
        if (!idiv(a)) {
            return q;
        }
        final String[] c = bl.get(a - bh);
        return dpab(c) ? gmfsd(c) // success
        		: s; // not found
    }

    /**
     * Checks validity of delete person argument string's format.
     *
     * @param b raw command args string for the delete person command
     * @return whether the input args string is valid
     */
    private static boolean idav(String b) {
        try {
            final int a = Integer.parseInt(b.trim()); // use standard libraries to parse
            return a >= bh;
        } catch (NumberFormatException nfe) {
            return false;
        }
    }

    /**
     * Extracts the target's index from the raw delete person args string
     *
     * @param a raw command args string for the delete person command
     * @return extracted index
     */
    private static int etifdp(String a) {
        return Integer.parseInt(a.trim());
    }

    /**
     * Checks that the given index is within bounds and valid for the last shown person list view.
     *
     * @param a to check
     * @return whether it is valid
     */
    private static boolean idiv(int a) {
        return a >= bh && a < glfpv().size() + bh;
    }

    /**
     * Constructs a feedback message for a successful delete person command execution.
     *
     * @see #edp(String)
     * @param a successfully deleted
     * @return successful delete person feedback message
     */
    private static String gmfsd(String[] a) {
        return String.format(j, String.format(k,
		gn(a), gp(a), ge(a)));
    }

    /**
     * Clears all persons in the address book.
     *
     * @return feedback display message for the operation result
     */
    private static String eca() {
        cab();
        return f;
    }

    /**
     * Displays all persons in the address book to the user; in added order.
     *
     * @return feedback display message for the operation result
     */
    private static String ela() {
        ArrayList<String[]> a = gap();
        stu(a);
        return gmfpm(a);
    }

    /**
     * Request to terminate the program.
     *
     * @return feedback display message for the operation result
     */
    private static void ecpr() {
        bo();
    }

    /*
     * ===========================================
     *               UI LOGIC
     * ===========================================
     */

    /**
     * Prompts for the command and reads the text entered by the user.
     * Ignores lines with first non-whitespace char equal to {@link #bi} (considered comments)
     *
     * @return full line entered by the user
     */
    private static String ca() {
        System.out.print(c + "Enter command: ");
        String a = bj.nextLine();
        // silently consume all blank and comment lines
        while (a.trim().isEmpty() || a.trim().charAt(0) == bi) {
            a = bj.nextLine();
        }
        return a;
    }

   /**
     * Shows the list of persons to the user.
     * The list will be indexed, starting from 1.
     *
     */
    private static void stu(ArrayList<String[]> p) {
        String l = gds(p);
		String[] message = { l };
        for (String m : message) {
		    System.out.println(c + m);
		}
        // clone to insulate from future changes to arg list
		bl = new ArrayList<>(p);
    }

    /**
     * Returns the display string representation of the list of persons.
     */
    private static String gds(ArrayList<String[]> persons) {
        final StringBuilder ma = new StringBuilder();
        for (int i = 0; i < persons.size(); i++) {
            final String[] person = persons.get(i);
            final int a = i + bh;
            ma.append('\t')
                              .append(giplm(a, person))
                              .append(d);
        }
        return ma.toString();
    }

    /**
     * Constructs a prettified listing element message to represent a person and their data.
     *
     * @param a visible index for this listing
     * @param b to show
     * @return formatted listing message with index
     */
    private static String giplm(int a, String[] b) {
        return String.format(l, a) + String.format(k,
		gn(b), gp(b), ge(b));
    }

    /**
     * @return unmodifiable list view of the last person listing view
     */
    private static ArrayList<String[]> glfpv() {
        return bl;
    }


    /*
     * ===========================================
     *             STORAGE LOGIC
     * ===========================================
     */

    /**
     * Creates storage file if it does not exist. Shows feedback to user.
     *
     * @param a file to create if not present
     */
    private static void bw(String a) {
        final File b = new File(a);
        if (b.exists()) {
            return;
        }
		String[] me = { String.format(u, a) };

        for (String m : me) {
		    System.out.println(c + m);
		}

        try {
            b.createNewFile();
			String[] me2 = { String.format(y, a) };
            for (String m : me2) {
			    System.out.println(c + m);
			}
        } catch (IOException ioe) {
            String[] me2 = { String.format(t, a) };
			for (String m : me2) {
			    System.out.println(c + m);
			}
            bo();
        }
    }

    /**
     * Converts contents of a file into a list of persons.
     * Shows error messages and exits program if any errors in reading or decoding was encountered.
     *
     * @param filePath file to load from
     * @return the list of decoded persons
     */
    private static ArrayList<String[]> by(String filePath) {
        final Optional<ArrayList<String[]>> successfullyDecoded = dp(glif(filePath));
        if (!successfullyDecoded.isPresent()) {
            String[] message = { r };
			for (String m : message) {
			    System.out.println(c + m);
			}
            bo();
        }
        return successfullyDecoded.get();
    }

    /**
     * Gets all lines in the specified file as a list of strings. Line separators are removed.
     * Shows error messages and exits program if unable to read from file.
     */
    private static ArrayList<String> glif(String a) {
        ArrayList<String> b = null;
        try {
            b = new ArrayList(Files.readAllLines(Paths.get(a)));
        } catch (FileNotFoundException fnfe) {
            String[] message = { String.format(u, a) };
			for (String m : message) {
			    System.out.println(c + m);
			}
            bo();
        } catch (IOException ioe) {
            String[] message = { String.format(v, a) };
			for (String m : message) {
			    System.out.println(c + m);
			}
            bo();
        }
        return b;
    }

    /**
     * Saves all data to the file.
     * Exits program if there is an error saving to file.
     *
     * @param filePath file for saving
     */
    private static void sptf(ArrayList<String[]> persons, String filePath) {
        final ArrayList<String> linesToWrite = epts2(persons);
        try {
            Files.write(Paths.get(bm), linesToWrite);
        } catch (IOException ioe) {
            String[] message = { String.format(w, filePath) };
			for (String m : message) {
			    System.out.println(c + m);
			}
            bo();
        }
    }


    /*
     * ================================================================================
     *        INTERNAL ADDRESS BOOK DATA METHODS
     * ================================================================================
     */

    /**
     * Adds a person to the address book. Saves changes to storage file.
     *
     * @param person to add
     */
    private static void ada(String[] person) {
        bk.add(person);
        sptf(gap(), bm);
    }

    /**
     * Deletes a person from the address book, target is identified by it's absolute index in the full list.
     * Saves changes to storage file.
     *
     * @param index absolute index of person to delete (index within {@link #bk})
     */
    private static void dpfa(int index) {
        bk.remove(index);
        sptf(gap(), bm);
    }

    /**
     * Deletes the specified person from the addressbook if it is inside. Saves any changes to storage file.
     *
     * @param exactPerson the actual person inside the address book (exactPerson == the person to delete in the full list)
     * @return true if the given person was found and deleted in the model
     */
    private static boolean dpab(String[] exactPerson) {
        final boolean changed = bk.remove(exactPerson);
        if (changed) {
            sptf(gap(), bm);
        }
        return changed;
    }

    /**
     * @return unmodifiable list view of all persons in the address book
     */
    private static ArrayList<String[]> gap() {
        return bk;
    }

    /**
     * Clears all persons in the address book and saves changes to file.
     */
    private static void cab() {
        bk.clear();
        sptf(gap(), bm);
    }

    /**
     * Resets the internal model with the given data. Does not save to file.
     *
     * @param persons list of persons to initialise the model with
     */
    private static void iab(ArrayList<String[]> persons) {
        bk.clear();
        bk.addAll(persons);
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
    private static String gn(String[] person) {
        return person[bd];
    }

    /**
     * @param person whose phone number you want
     * @return person's phone number
     */
    private static String gp(String[] person) {
        return person[be];
    }

    /**
     * @param person whose email you want
     * @return person's email
     */
    private static String ge(String[] person) {
        return person[bf];
    }

    /**
     * Create a person for use in the internal data.
     *
     * @param name of person
     * @param phone without data prefix
     * @param email without data prefix
     * @return constructed person
     */
    private static String[] mpfd(String name, String phone, String email) {
        final String[] person = new String[bg];
        person[bd] = name;
        person[be] = phone;
        person[bf] = email;
        return person;
    }

    /**
     * Encodes a person into a decodable and readable string representation.
     *
     * @param person to be encoded
     * @return encoded string
     */
    private static String epts(String[] person) {
        return String.format(ad,
                gn(person), gp(person), ge(person));
    }

    /**
     * Encodes list of persons into list of decodable and readable string representations.
     *
     * @param persons to be encoded
     * @return encoded strings
     */
    private static ArrayList<String> epts2(ArrayList<String[]> persons) {
        final ArrayList<String> encoded = new ArrayList<>();
        for (String[] person : persons) {
            encoded.add(epts(person));
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
    private static Optional<String[]> dpfs(String encoded) {
        // check that we can extract the parts of a person from the encoded string
        if (!ipef(encoded)) {
            return Optional.empty();
        }
        final String[] decodedPerson = mpfd(
                enp(encoded),
                ep(encoded),
                eefp(encoded)
        );
        // check that the constructed person is valid
        return ipdv(decodedPerson) ? Optional.of(decodedPerson) : Optional.empty();
    }

    /**
     * Decode persons from a list of string representations.
     *
     * @param encodedPersons strings to be decoded
     * @return if cannot decode any: empty Optional
     *         else: Optional containing decoded persons
     */
    private static Optional<ArrayList<String[]>> dp(ArrayList<String> encodedPersons) {
        final ArrayList<String[]> decodedPersons = new ArrayList<>();
        for (String encodedPerson : encodedPersons) {
            final Optional<String[]> decodedPerson = dpfs(encodedPerson);
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
    private static boolean ipef(String personData) {
        final String matchAnyPersonDataPrefix = ab + '|' + ac;
        final String[] splitArgs = personData.trim().split(matchAnyPersonDataPrefix);
        return splitArgs.length == 3 // 3 arguments
                && !splitArgs[0].isEmpty() // non-empty arguments
                && !splitArgs[1].isEmpty()
                && !splitArgs[2].isEmpty();
    }

    /**
     * Extracts substring representing person name from person string representation
     *
     * @param a person string representation
     * @return name argument
     */
    private static String enp(String a) {
        final int b = a.indexOf(ab);
        final int c = a.indexOf(ac);
        // name is leading substring up to first data prefix symbol
        int d = Math.min(c, b);
        return a.substring(0, d).trim();
    }

    /**
     * Extracts substring representing phone number from person string representation
     *
     * @param encoded person string representation
     * @return phone number argument WITHOUT prefix
     */
    private static String ep(String encoded) {
        final int a = encoded.indexOf(ab);
        final int b = encoded.indexOf(ac);

        // phone is last arg, target is from prefix to end of string
        if (a > b) {
            return rps(encoded.substring(a, encoded.length()).trim(),
                    ab);

        // phone is middle arg, target is from own prefix to next prefix
        } else {
            return rps(
                    encoded.substring(a, b).trim(),
                    ab);
        }
    }

    /**
     * Extracts substring representing email from person string representation
     *
     * @param a person string representation
     * @return email argument WITHOUT prefix
     */
    private static String eefp(String a) {
        final int b = a.indexOf(ab);
        final int c = a.indexOf(ac);

        // email is last arg, target is from prefix to end of string
        if (c > b) {
            return rps(a.substring(c, a.length()).trim(),
                    ac);

        // email is middle arg, target is from own prefix to next prefix
        } else {
            return rps(
                    a.substring(c, b).trim(),
                    ac);
        }
    }

    /**
     * Validates a person's data fields
     *
     * @param person String array representing the person (used in internal data)
     * @return whether the given person has valid data
     */
    private static boolean ipdv(String[] person) {
        return ipnv(person[bd])
                && ippv(person[be])
                && ipev(person[bf]);
    }

    /*
     * ==============NOTE TO STUDENTS======================================
     * Note the use of 'regular expressions' in the method below.
     * Regular expressions can be very useful in checking if a a string
     * follows a sepcific format.
     * ====================================================================
     */
    /**
     * Validates string as a legal person name
     *
     * @param name to be validated
     * @return whether arg is a valid person name
     */
    private static boolean ipnv(String name) {
        return name.matches("(\\w|\\s)+");  // name is nonempty mixture of alphabets and whitespace
        //TODO: implement a more permissive validation
    }

    /**
     * Validates string as a legal person phone number
     *
     * @param phone to be validated
     * @return whether arg is a valid person phone number
     */
    private static boolean ippv(String phone) {
        return phone.matches("\\d+");    // phone nonempty sequence of digits
        //TODO: implement a more permissive validation
    }

    /**
     * Validates string as a legal person email
     *
     * @param email to be validated
     * @return whether arg is a valid person email
     */
    private static boolean ipev(String email) {
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
    private static String geuifa() {
        return String.format(g, ae, af) + d
		+ String.format(h, ag) + d
		+ String.format(i, ah) + d + d
                + String.format(g, ai, aj) + d
				+ String.format(h, ak) + d
				+ String.format(i, al) + d + d
                + guiv() + d
                + guifdc() + d
                + guii() + d
                + guife() + d
                + guifh();
    }

    /**
     * Builds string for showing 'delete' command usage instruction
     *
     * @return  'delete' command usage instruction
     */
    private static String guifdc() {
        return String.format(g, ap, aq) + d
                + String.format(h, ar) + d
                + String.format(i, as) + d;
    }

    /**
     * Builds string for showing 'clear' command usage instruction
     *
     * @return  'clear' command usage instruction
     */
    private static String guii() {
        return String.format(g, at, au) + d
                + String.format(i, av) + d;
    }

    /**
     * Builds string for showing 'view' command usage instruction
     *
     * @return  'view' command usage instruction
     */
    private static String guiv() {
        return String.format(g, am, an) + d
                + String.format(i, ao) + d;
    }

    /**
     * Builds string for showing 'help' command usage instruction
     *
     * @return  'help' command usage instruction
     */
    private static String guifh() {
        return String.format(g, aw, ax)
                + String.format(i, ay);
    }

    /**
     * Builds string for showing 'exit' command usage instruction
     *
     * @return  'exit' command usage instruction
     */
    private static String guife() {
        return String.format(g, az, ba)
                + String.format(i, bb);
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
    private static String rps(String s, String sign) {
        return s.replace(sign, "");
    }

    /**
     * Splits a source string into the list of substrings that were separated by whitespace.
     *
     * @param toSplit source string
     * @return split by whitespace
     */
    private static ArrayList<String> sbw(String toSplit) {
        return new ArrayList(Arrays.asList(toSplit.trim().split("\\s+")));
    }

}