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
		unigramModel = new HashMap<String, Unigram>();
		bigramModel = new HashMap<String, Bigram>();
		trigramModel = new HashMap<String, Trigram>();

		if (!handlingUnknown) {
			populateModel(unigrams, bigrams, corpusContent);
		} else {
			populateModelWithUnknown(unigrams, bigrams, corpusContent);
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
				trigramModel.put(trigramAsString, trigram);
			}

			first = second;
			second = tokens[i];
		}

		setProbabilities(bigrams);

	}

	private void populateModelWithUnknown(Map<String, Unigram> unigrams,
			Map<String, Bigram> bigrams, String[] tokens) {

		int size = tokens.length;

		int trainingSize = 4 * size / 5;

		String first = tokens[0];
		String second = tokens[1];
		for (int i = 2; i < trainingSize; i++) {

			String trigramAsString = first + " " + second + " " + tokens[i];

			if (trigramModel.containsKey(trigramAsString)) {
				double count = trigramModel.get(trigramAsString).getCount();
				trigramModel.get(trigramAsString).setCount(count + 1);
			} else {
				Trigram trigram = new Trigram();
				trigram.setBigram(first + " " + second);
				trigram.setThird(tokens[i]);
				trigram.setCount(1);
				trigramModel.put(trigramAsString, trigram);
			}

			first = second;
			second = tokens[i];
		}

		System.out.println("Trigram last token:" + second);

		for (int i = trainingSize; i < size; i++) {

			String word = tokens[i];

			if (!unigrams.containsKey(word)) {
				word = unKnownWord;
			}
			String trigramAsString = first + " " + second + " " + word;

			if (trigramModel.containsKey(trigramAsString)) {
				double count = trigramModel.get(trigramAsString).getCount();
				trigramModel.get(trigramAsString).setCount(count + 1);
			} else {
				Trigram trigram = new Trigram();
				trigram.setBigram(first + " " + second);
				trigram.setThird(word);
				trigram.setCount(1);
				trigramModel.put(trigramAsString, trigram);
			}

			first = second;
			second = word;

		}

		setProbabilities(bigrams);

	}

	private void setProbabilities(Map<String, Bigram> bigrams) {
		for (String trigram : trigramModel.keySet()) {
			String[] items = trigram.split(" ");
			String bigram = items[0] + " " + items[1];
			double probability = trigramModel.get(trigram).getCount()
					/ bigrams.get(bigram).getCount();// P(wn/wn-1wn-2)=C(wn-2wn-1wn)/C(wn-2wn-1)
			trigramModel.get(trigram).setProbability(probability);
		}
	}

	public String toString() {

		StringBuilder sb = new StringBuilder();
		for (String trigram : trigramModel.keySet()) {

			sb.append(trigram + " " + trigramModel.get(trigram).getCount());
			sb.append("\n");

		}

		return sb.toString();
	}

}
