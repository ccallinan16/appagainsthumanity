package mocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * this class is to be used by the server to create the random cards.
 * 
 * @author egetzner
 *
 */

public class Shuffler {

	private static int maxSize = 50;
	
	private Shuffler() {

	}
	
	/**
	 * 
	 * @param numCards
	 * @return a SET of IDs (all unique)
	 */
	public static List<Integer> getRandomListOfInts(int numCards) {
		
		if (numCards == 0)
			throw new RuntimeException();
		
		Random rand = new Random();
		Set<Integer> list = new HashSet<Integer>();

		while (list.size() < numCards)
		{
			list.add(rand.nextInt(maxSize));
		}
				
		List<Integer> newList =  new ArrayList<Integer>(list);
		
		return newList;
	}

}
