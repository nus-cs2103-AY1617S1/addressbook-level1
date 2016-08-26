package seedu.addressbook.sorter;

import java.util.Comparator;

public class CustomPersonSorterDescending implements Comparator<String[]> {

	public int compare(String[] person1, String[] person2) {
		return person2[0].compareTo(person1[0]);
	}

}
