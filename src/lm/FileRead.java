package lm;

import java.io.*;
import java.util.*;

public class FileRead {

	public static String[] fileRead(String filename) 
	{
		ArrayList<String> wordList = new ArrayList<String>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));

			String word;

			while ((word = br.readLine()) != null) {
				wordList.add(word);
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException exc) {
			}
		}

		String[] words = new String[wordList.size()];
		wordList.toArray(words);

		ArrayList<String> finalw = new ArrayList<String>();

		for (int i = 0; i < words.length; i++) {
			StringTokenizer st = new StringTokenizer(words[i]);
			while (st.hasMoreTokens())
				finalw.add(st.nextToken());
		}
		String[] finalwords = new String[finalw.size()];
		finalw.toArray(finalwords);

		return finalwords;
	}
	
	
	
	public static String[] fileReadNeat(String filename) 
	{
		ArrayList<String> wordList = new ArrayList<String>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));

			String word;

			while ((word = br.readLine()) != null) {
				wordList.add(word);
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException exc) {
			}
		}

		String[] words = new String[wordList.size()];
		wordList.toArray(words);

		ArrayList<String> finalw = new ArrayList<String>();

		for (int i = 0; i < words.length; i++) {
			StringTokenizer st = new StringTokenizer(words[i]);
			while (st.hasMoreTokens())
				finalw.add(st.nextToken());
		}
		String[] finalwords = new String[finalw.size()];
		finalw.toArray(finalwords);
		
		for(int i = 0; i < finalwords.length; i ++)
		{
			if(!Character.isLetterOrDigit(finalwords[i].charAt( finalwords[i].length() - 1 )))
			{
			    if(finalwords[i].length() != 1){
				    finalwords[i] = finalwords[i].substring(0, finalwords[i].length()-1);
			    }
			}
		}
		
		return finalwords;
	}
	
	public static String[] fileReadLowerCase(String filename) 
	{
		ArrayList<String> wordList = new ArrayList<String>();

		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(filename));

			String word;

			while ((word = br.readLine()) != null) {
				wordList.add(word);
			}
		} catch (IOException exc) {
			exc.printStackTrace();
		} finally {
			try {
				br.close();
			} catch (IOException exc) {
			}
		}

		String[] words = new String[wordList.size()];
		wordList.toArray(words);

		ArrayList<String> finalw = new ArrayList<String>();

		for (int i = 0; i < words.length; i++) {
			StringTokenizer st = new StringTokenizer(words[i]);
			while (st.hasMoreTokens())
				finalw.add(st.nextToken().toLowerCase());
		}
		String[] finalwords = new String[finalw.size()];
		finalw.toArray(finalwords);
		
		for(int i = 0; i < finalwords.length; i ++)
		{
			if(!Character.isLetterOrDigit(finalwords[i].charAt( finalwords[i].length() - 1 )))
			{
			    if(finalwords[i].length() != 1){
				    finalwords[i] = finalwords[i].substring(0, finalwords[i].length()-1);
			    }
			}
		}
		
		return finalwords;
	}
}
