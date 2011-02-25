package lm;

import java.util.HashMap;

import lm.models.BigramModel;
import lm.models.UnigramModel;

// That looks good
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] filecontent = FileRead.fileRead("Dataset3/Test.txt");
		for (int i = 0; i < filecontent.length; i++)
			System.out.println(filecontent[i]);

		UnigramModel unigramModel = new UnigramModel();
		HashMap<String, Integer> unigram = unigramModel
				.calculateUnigramModel(filecontent);
		
		

		String text = "I am Sam Sam I am I do not like green eggs and ham";
		String[] testCorpus = text.split(" ");
		String[] vocabulary = "I am Sam do not like green eggs and ham"
				.split(" ");

		BigramModel bigramModel = new BigramModel(vocabulary, testCorpus);
		System.out.println(bigramModel.toString());

	}
}
