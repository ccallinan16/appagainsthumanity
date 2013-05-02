package mocks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class MockDB {

	int size = 50;
	
	public MockDB() {

	}

	/**
	 * 
	 * @param numCards
	 * @return a SET of IDs (all unique)
	 */
	public List<Integer> assignCards(int numCards) {
		Random rand = new Random();
		Set<Integer> list = new HashSet<Integer>();

		while (list.size() < numCards)
		{
			list.add(rand.nextInt(size));
		}
		return new ArrayList<Integer>(list);
	}

}
