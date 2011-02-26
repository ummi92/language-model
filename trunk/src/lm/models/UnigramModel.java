package lm.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lm.objects.Unigram;

public class UnigramModel {

	private Map<String, Unigram> unigramModel;

	public UnigramModel(String[] vocabulary) {
		unigramModel = new HashMap<String, Unigram>();
		populateModel(vocabulary);
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
				BigDecimal wordCount = unigram.getCount();
				unigram.setCount(wordCount.add(BigDecimal.ONE));

			} else {

				Unigram unigram = new Unigram();

				unigram.setWord(word);
				unigram.setCount(BigDecimal.ONE);
				unigramModel.put(word, unigram);
			}

		}
		/*
		 * Set<String> wordSet = unigramModel.keySet(); BigDecimal totalCount =
		 * new BigDecimal(size);
		 * 
		 * for (String word : wordSet){
		 * 
		 * Unigram unigram = unigramModel.get(word); BigDecimal count =
		 * unigram.getCount(); BigDecimal prob = count.divide(totalCount);
		 * unigram.setProbability(prob);
		 * 
		 * }
		 */
	}

	public Unigram generateNextWord() {

		double random = Math.random();
		BigDecimal randomNrForGeneration = new BigDecimal(random);
		List<Unigram> words = new ArrayList<Unigram>(unigramModel.values());
		Collections.sort(words);

		BigDecimal probForGeneration = BigDecimal.ZERO;
		Unigram lastWord = null;
		for (Unigram word : words) {
			lastWord = word;
			BigDecimal wordCount = word.getCount();
			BigDecimal nrOfTokens = new BigDecimal(unigramModel.size());
			BigDecimal wordProb = wordCount.divide(nrOfTokens, 20,
					BigDecimal.ROUND_HALF_UP);
			probForGeneration = probForGeneration.add(wordProb);

			if (randomNrForGeneration.compareTo(probForGeneration) < 0) {
				return word;
			}
		}
		return lastWord;

	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String unigram : unigramModel.keySet()) {
			sb.append(unigram + " " + unigramModel.get(unigram).getCount());
			sb.append("\n");
		}

		return sb.toString();
	}

}
