package lm;

import lm.models.BigramModel;
import lm.models.TrigramModel;
import lm.models.UnigramModel;
import lm.objects.Bigram;
import lm.objects.Unigram;
import lm.ext.*;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String[] training = FileRead.newFileRead("ourtest/train.txt");
		// String[] validation = FileRead.fileRead("Dataset3/Test.txt");
		// String[] training = new String[train.length + validation.length];
		// System.arraycopy(train, 0, training, 0, train.length);
		// System.arraycopy(validation, 0, training, train.length,
		// validation.length);

		// System.out.println("----------------------------------------------");
		// String text =
		// "I am Sam Sam I am I do not like green eggs and. ham Ha Ha ha perplexity needs to go down";
		// String[] training = text.split(" ");

		System.out.println("\n----------------------------------------------");
		UnigramModel unigramModel = new UnigramModel(training, true);
		Smoothing.unigramSmoothing(unigramModel.getUnigramModel());

		// System.out.println(unigramModel.toString());

		System.out.println("\n----------------------------------------------");
		UnigramModel unigramModelForBigram = new UnigramModel(training, true);
		BigramModel bigramModel = new BigramModel(unigramModelForBigram
				.getUnigramModel(), training, true);
		BigramModel bigramModelForTrigram = new BigramModel(
				unigramModelForBigram.getUnigramModel(), training, true);

		TrigramModel trigramModel = new TrigramModel(unigramModelForBigram
				.getUnigramModel(), bigramModelForTrigram.getBigramModel(),
				training, true);
		System.out.println("Trigram:" + trigramModel.toString());
		// System.out.println(bigramModel.toString());
		System.out.println("\n----------------------------------------------");

		// for (int i = 0; i < 10; i++) {
		// Unigram unigram = unigramModel.generateNextWord();
		// System.out.print(unigram.getWord() + ' ');
		// }

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
		String[] test = FileRead.newFileRead("ourtest/test.txt");
		// String testText = "I am Sam I do not like perplexity at all";
		// String[] test = testText.split(" ");
		double unigramPerplexity = Perplexity.computeUnigramPerplexity(
				unigramModel.getUnigramModel(), test);

		double bigramPerplexity = Perplexity.computeBigramPerplexity(
				bigramModel, test);
		System.out.println("Unigram perplexity = " + unigramPerplexity);
		System.out.println("Bigram perplexity = " + bigramPerplexity);

	}
}
