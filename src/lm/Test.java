package lm;

import lm.models.BigramModel;
import lm.models.UnigramModel;
// That looks good
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		String[] filecontent = FileRead.fileRead("Test.txt");
		for(int i=0; i< filecontent.length; i++)
			System.out.println(filecontent[i]);
		
		System.out.println("----------------------------------------------");
		String text = "I am Sam Sam I am I do not like green eggs and ham";
		String[] testCorpus = text.split(" ");
		String[] vocabulary = "I am Sam do not like green eggs and ham"
				.split(" ");

		BigramModel bigramModel = new BigramModel(vocabulary, testCorpus);
		System.out.println(bigramModel.toString());
		
		System.out.println("----------------------------------------------");
		UnigramModel unigramModel = new UnigramModel(filecontent);
		System.out.println(unigramModel.toString());
		
		
		

	}
}
