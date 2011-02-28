package lm.models;

import java.util.HashMap;
import java.util.Map;

import lm.objects.Bigram;
import lm.objects.Trigram;
import lm.objects.Unigram;

public class TrigramModel {
	private Map<String, Trigram> trigramModel;
	private Map<String, Bigram> bigramModel;
	private Map<String, Unigram> unigramModel;

	private String unKnownWord = "<UNK>";

	public TrigramModel(Map<String, Unigram> unigrams,
			Map<String, Bigram> bigrams, String[] corpusContent,
			boolean handlingUnknown) {
		bigramModel = new HashMap<String, Bigram>();

		if (!handlingUnknown) {
			populateModel(unigrams, bigrams, corpusContent);
		}

	}

	private void populateModel(Map<String, Unigram> unigrams,
			Map<String, Bigram> bigrams, String[] tokens) {

		String first = tokens[0];
		String second = tokens[1];
		for (int i = 2; i < tokens.length; i++) {

			String trigramAsString = first + " " + second + " " + tokens[i];

			if (trigramModel.containsKey(trigramAsString)) {
				double count = trigramModel.get(trigramAsString).getCount();
				trigramModel.get(trigramAsString).setCount(count + 1);
			} else {
				Trigram trigram = new Trigram();
				trigram.setBigram(first + " " + second);
				trigram.setThird(tokens[i]);
				trigram.setCount(1);
			}

			first = second;
			second = tokens[i];
		}

	}

}
