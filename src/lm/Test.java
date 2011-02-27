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

		String[] train = FileRead.fileRead("Dataset3/Train.txt");
		String[] validation = FileRead.fileRead("Dataset3/Validation.txt");
		String[] training = new String[train.length + validation.length];
		System.arraycopy(train, 0, training, 0, train.length);
		System.arraycopy(validation, 0, training, train.length, validation.length);

		// System.out.println("----------------------------------------------");
		// String text = "I am Sam";
		// String[] training = text.split(" ");

		System.out.println("\n----------------------------------------------");
		UnigramModel unigramModel = new UnigramModel(training, true);
		// System.out.println(unigramModel.toString());

		System.out.println("\n----------------------------------------------");
		BigramModel bigramModel = new BigramModel(unigramModel
				.getUnigramModel(), training, true);
		bigramModel.setProbabilities(unigramModel.getUnigramModel());
		// System.out.println(bigramModel.toString());
		System.out.println("\n----------------------------------------------");

		for (int i = 0; i < 10; i++) {
			Unigram unigram = unigramModel.generateNextWord();
			System.out.print(unigram.getWord() + ' ');
		}

		// System.out.println("\n----------------------------------------------");
		// String prefix = "I";
		// System.out.print(prefix + ' ');
		// for (int i = 0; i < 10; i++) {
		// Bigram bigram = bigramModel.generateNextBigram(prefix);
		// prefix = bigram.getSecond();
		// System.out.print(bigram.getSecond() + ' ');
		// }
		//
		// System.out.println("\n----------------------------------------------");
		// SpellChecker spellchecker = new SpellChecker("Dataset4/Test.txt");
		// System.out.println(spellchecker.correct("womed"));

		System.out.println("\n----------------------------------------------");
		String[] test = FileRead.fileRead("Dataset3/Test.txt");
		// String testText = "am I";
		// String[] test = testText.split(" ");
		double unigramPerplexity = Perplexity.computeUnigramPerplexity(
				unigramModel.getUnigramModel(), test);
		double bigramPerplexity = Perplexity.computeBigramPerplexity(
				bigramModel, test);

		System.out.println("Unigram perplexity = " + unigramPerplexity);
		System.out.println("Bigram perplexity = " + bigramPerplexity);

	}
}
