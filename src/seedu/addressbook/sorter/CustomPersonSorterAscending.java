package seedu.addressbook.sorter;

import java.util.Comparator;

public class CustomPersonSorterAscending implements Comparator<String[]> {

	@Override
	public int compare(String[] person1, String[] person2) {
		return person1[0].compareTo(person2[0]);
	}
	
}
