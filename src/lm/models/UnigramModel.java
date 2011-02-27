package lm.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lm.objects.Unigram;

public class UnigramModel {

	private Map<String, Unigram> unigramModel;
	private String unKnownWord = "<UNK>";

	public UnigramModel(String[] vocabulary, boolean handleUnknownWord) {
		unigramModel = new HashMap<String, Unigram>();
		if (handleUnknownWord == false)
			populateModel(vocabulary);
		else
			populateModelHandlingUnknownWord(vocabulary);
	}

	public Map<String, Unigram> getUnigramModel() {
		return unigramModel;
	}

	private void populateModel(String[] vocabulary) {

		int size = vocabulary.length;

		for (int count = 0; count < size; count++) {

			String word = vocabulary[count];

			if (unigramModel.containsKey(word)) {
				Unigram unigram = unigramModel.get(word);
				double wordCount = unigram.getCount();
				unigram.setCount(wordCount + 1);

			} else {

				Unigram unigram = new Unigram();
				unigram.setWord(word);
				unigram.setCount(1);
				unigramModel.put(word, unigram);
			}

		}

		Set<String> wordSet = unigramModel.keySet();

		for (String word : wordSet) {

			Unigram unigram = unigramModel.get(word);
			double count = unigram.getCount();
			double prob = count / size;
			unigram.setProbability(prob);

		}

	}

	private void populateModelHandlingUnknownWord(String[] vocabulary) {

		int size = vocabulary.length;

		int trainingSize = 4 * size / 5;

		for (int count = 0; count < trainingSize; count++) {

			String word = vocabulary[count];

			if (unigramModel.containsKey(word)) {
				Unigram unigram = unigramModel.get(word);
				double wordCount = unigram.getCount();
				unigram.setCount(wordCount + 1);

			} else {

				Unigram unigram = new Unigram();

				unigram.setWord(word);
				unigram.setCount(1);
				unigramModel.put(word, unigram);
			}

		}

		Unigram unigramUNK = new Unigram();
		unigramUNK.setWord(unKnownWord);
		unigramModel.put(unKnownWord, unigramUNK);

		for (int count = 4 * size / 5; count < size; count++) {

			String word = vocabulary[count];

			if (unigramModel.containsKey(word)) {
				Unigram unigram = unigramModel.get(word);
				double wordCount = unigram.getCount();
				unigram.setCount(wordCount + 1);

			} else {

				Unigram unigram = unigramModel.get(unKnownWord);
				double wordCount = unigram.getCount();
				unigram.setCount(wordCount + 1);
			}

		}

		Set<String> wordSet = unigramModel.keySet();

		for (String word : wordSet) {

			Unigram unigram = unigramModel.get(word);
			double count = unigram.getCount();
			double prob = count / size;
			unigram.setProbability(prob);

		}

	}

	public Unigram generateNextWord() {

		double randomNrForGeneration = Math.random();

		List<Unigram> words = new ArrayList<Unigram>(unigramModel.values());
		Collections.sort(words);

		double probForGeneration = 0;
		Unigram lastWord = null;
		for (Unigram word : words) {
			lastWord = word;
			double wordCount = word.getCount();
			double nrOfTokens = unigramModel.size();
			double wordProb = wordCount / nrOfTokens;
			probForGeneration += wordProb;

			if (randomNrForGeneration < probForGeneration) {
				return word;
			}
		}
		return lastWord;

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String unigram : unigramModel.keySet()) {
			sb.append(unigram + " " + unigramModel.get(unigram).getCount()
					+ " " + unigramModel.get(unigram).getProbability());
			sb.append("\n");
		}

		return sb.toString();
	}

}
