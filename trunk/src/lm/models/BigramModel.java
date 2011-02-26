package lm.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lm.objects.Bigram;
import lm.objects.Unigram;

public class BigramModel {

	private Map<String, Bigram> bigramModel;

	public BigramModel(Map<String, Unigram> vocabulary, String[] corpusContent) {
		bigramModel = new HashMap<String, Bigram>();
		populateModel(vocabulary, corpusContent);
	}

	private void populateModel(Map<String, Unigram> vocabulary, String[] tokens) {

		String lastToken = null;
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			// bigrams
			if (lastToken != null) {

				String bigramAsString = lastToken + ' ' + token;

				if (bigramModel.containsKey(bigramAsString)) {
					BigDecimal count = bigramModel.get(bigramAsString)
							.getCount();
					bigramModel.get(bigramAsString).setCount(
							count.add(BigDecimal.ONE));

				} else {
					Bigram bigram = new Bigram();
					bigram.setFirst(lastToken);
					bigram.setSecond(token);
					bigram.setCount(BigDecimal.ONE);
					bigramModel.put(bigramAsString, bigram);
				}

			}
			lastToken = token;
		}

		// List<String> allPossibleBigrams = new ArrayList<String>();
		// // add the zeroes
		// for (int i = 0; i < vocabulary.size(); i++) {
		// for (int j = 1; j < vocabulary.size() j++) {
		//
		// allPossibleBigrams.add(vocabulary + " " + vocabulary[j]);
		// }
		//
		// }
		//
		// for (String bigram : allPossibleBigrams) {
		// if (!bigramModel.containsKey(bigram)) {
		// Bigram bigramObj = new Bigram();
		// String[] items = bigram.split(" ");
		// bigramObj.setFirst(items[0]);
		// bigramObj.setSecond(items[1]);
		// bigramModel.put(bigram, bigramObj);
		// }
		// }

	}

	public void setProbabilities(Map<String, Unigram> unigrams) {
		for (String bigram : bigramModel.keySet()) {
			String first = bigram.split(" ")[0];
			BigDecimal probability = bigramModel.get(bigram).getCount().divide(
					unigrams.get(first).getCount(), 20,
					BigDecimal.ROUND_HALF_UP);// P(wn/wn-1)=C(wn-1wn)/C(wn-1)
			bigramModel.get(bigram).setProbability(probability);
		}
	}

	public Map<String, Bigram> getBigramModel() {
		return bigramModel;
	}

	public void setBigramModel(Map<String, Bigram> bigramModel) {
		this.bigramModel = bigramModel;
	}

	public List<Bigram> getBigramsByPrefix(String prefix) {
		List<Bigram> bigrams = new ArrayList<Bigram>();

		for (String bigram : bigramModel.keySet()) {
			if (bigram.startsWith(prefix)) {
				bigrams.add(bigramModel.get(bigram));
			}
		}
		return bigrams;

	}

	public Bigram generateNextBigram(String prefix) {

		List<Bigram> bigrams = getBigramsByPrefix(prefix);
		Collections.sort(bigrams);

		double random = Math.random();
		BigDecimal randomNrForGeneration = new BigDecimal(random);
		BigDecimal probForGeneration = BigDecimal.ZERO;
		Bigram lastBigram = null;
		for (Bigram bigram : bigrams) {
			lastBigram = bigram;

			BigDecimal bigramProb = bigram.getProbability();
			probForGeneration = probForGeneration.add(bigramProb);

			if (randomNrForGeneration.compareTo(probForGeneration) < 0) {
				return bigram;
			}
		}

		return lastBigram;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (String bigram : bigramModel.keySet()) {
			sb.append(bigram + " " + bigramModel.get(bigram).getCount());
			sb.append("\n");
		}

		return sb.toString();
	}
}
