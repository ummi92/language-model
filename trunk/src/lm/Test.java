package lm;

import lm.models.BigramModel;
import lm.models.UnigramModel;
import lm.objects.Bigram;
import lm.objects.Unigram;
import lm.ext.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] filecontent = FileRead.fileRead("Dataset3/Test.txt");
		for (int i = 0; i < filecontent.length; i++)
			System.out.println(filecontent[i]);

		// System.out.println("----------------------------------------------");
		// String text = "I am Sam Sam I am I do not like green eggs and ham";
		// String[] testCorpus = text.split(" ");
		// String[] vocabulary = "I am Sam do not like green eggs and ham"
		// .split(" ");

		System.out.println("\n----------------------------------------------");
		UnigramModel unigramModel = new UnigramModel(filecontent);
		System.out.println(unigramModel.toString());

		System.out.println("\n----------------------------------------------");
		BigramModel bigramModel = new BigramModel(unigramModel
				.getUnigramModel(), filecontent);
		bigramModel.setProbabilities(unigramModel.getUnigramModel());
		System.out.println(bigramModel.toString());
		System.out.println("\n----------------------------------------------");

		for (int i = 0; i < 10; i++) {
			Unigram unigram = unigramModel.generateNextWord();
			System.out.print(unigram.getWord() + ' ');
		}

		System.out.println("\n----------------------------------------------");
		String prefix = "When";
		System.out.print(prefix + ' ');
		for (int i = 0; i < 10; i++) {
			Bigram bigram = bigramModel.generateNextBigram(prefix);
			prefix = bigram.getSecond();
			System.out.print(bigram.getSecond() + ' ');
		}

		System.out.println("\n----------------------------------------------");
		SpellChecker spellchecker = new SpellChecker("Dataset3/Test.txt");
		System.out.println(spellchecker.correct("womed"));

	}
}
