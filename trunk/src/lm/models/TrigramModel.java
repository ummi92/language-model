package lm.models;

import java.util.HashMap;
import java.util.Map;

import lm.objects.Bigram;
import lm.objects.Trigram;
import lm.objects.Unigram;

public class TrigramModel {
	private Map<String, Trigram> trigramModel;
	private BigramModel bigramModel;
	private Map<String, Unigram> unigramModel;

	private String unKnownWord = "<UNK>";

	public TrigramModel(Map<String, Unigram> unigrams, BigramModel bigramModel,
			String[] corpusContent, boolean handlingUnknown) {
		unigramModel = unigrams;
		this.bigramModel = bigramModel;
		trigramModel = new HashMap<String, Trigram>();

		if (!handlingUnknown) {
			populateModel(unigrams, bigramModel.getBigramModel(), corpusContent);
		} else {
			populateModelWithUnknown(unigrams, bigramModel.getBigramModel(),
					corpusContent);
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

	public double getTrigramCount(String trigram) {

		double result = -1;

		if (trigramModel.containsKey(trigram)) {
			return trigramModel.get(trigram).getCount();
		} else {
			String[] items = trigram.split(" ");
			String first = items[0];
			String second = items[1];
			String third = items[2];

			if (unigramModel.keySet().contains(first)
					&& unigramModel.keySet().contains(second)
					&& unigramModel.keySet().contains(third)) {
				return 0;
			}
		}

		if (result == -1) {
			System.out.println(trigram);
			throw new RuntimeException(
					"Trigram Model: getTrigramCount -1 should not happen");

		}

		return result;
	}

	public double getSmoothedTrigramProbability(String trigram) {

		String prefix = trigram.substring(0, trigram.lastIndexOf(" "));

		double prefixCount = bigramModel.getBigramCount(prefix);

		return (getTrigramCount(trigram) + 1)
				/ (prefixCount + unigramModel.size());
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
