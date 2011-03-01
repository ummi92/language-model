package lm;

import java.util.Map;
import java.util.Set;

import lm.objects.Unigram;

public class Smoothing {

	public static void unigramSmoothing(Map<String, Unigram> unigramModel) {

		Set<String> wordSet = unigramModel.keySet();

		double totalWords = wordSet.size();
		double totalCount = 0;

		for (String word : wordSet) {
			totalCount = totalCount + unigramModel.get(word).getCount();
		}

		for (String word : wordSet) {
			Unigram unigram = unigramModel.get(word);
			double wordCount = unigram.getCount() + 1;
			unigram.setCount(wordCount);
			unigram.setProbability(wordCount / (totalCount + totalWords));
		}

	}

}
