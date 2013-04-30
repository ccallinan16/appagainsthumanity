package at.tugraz.iicm.ma.appagainsthumanity.xml;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CreateCardXML {

	public static void main(String[] args) throws Exception {
	
		String[] black = readArrayFromFile("cards_black.txt");
		String[] white = readArrayFromFile("cards_white.txt");
		
		XMLCreator.createXMLFromString("allCards.xml", white, black);
		
	}

	public static String[] readArrayFromFile(String fname) {
		
		ArrayList<String> strings = new ArrayList<String>();
		
		try {
			FileInputStream fstream = new FileInputStream(fname);
			  // Get the object of DataInputStream
			  DataInputStream in = new DataInputStream(fstream);
			  BufferedReader br = new BufferedReader(new InputStreamReader(in));
			  String strLine;
			  //Read File Line By Line
			  while ((strLine = br.readLine()) != null)   {
			  // Print the content on the console
				  strings.add(strLine);
			  }
			  //Close the input stream
			  in.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String[] array = new String[strings.size()];
		return strings.toArray(array);
	}
	
	
	
}
