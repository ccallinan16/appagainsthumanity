package mocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MockDB {

	int size = 25;
	
	public MockDB() {

	}

	/**
	 * 
	 * @param numCards
	 * @return a SET of IDs (all unique)
	 */
	public List<Integer> assignCards(int numCards) {
		
		if (numCards == 0)
			throw new RuntimeException();
		
		Random rand = new Random();
		Set<Integer> list = new HashSet<Integer>();

		while (list.size() < numCards)
		{
			list.add(rand.nextInt(size));
		}
				
		List<Integer> newList =  new ArrayList<Integer>(list);
		
		return newList;
	}

}
