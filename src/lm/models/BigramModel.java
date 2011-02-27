package lm.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lm.objects.Bigram;
import lm.objects.Unigram;

public class BigramModel {

	private Map<String, Bigram> bigramModel;
	private Map<String, Unigram> unigramModel;
	private int N; // number of tokens
	private int V; // vocabulary size
	private String unKnownWord = "<UNK>";

	/**
	 * Creates a new BigramModel. If we want to handle unknown then the input
	 * vocabulary has to handle unknown as well.
	 * 
	 * @param vocabulary
	 * @param corpusContent
	 * @param handlingUnknown
	 */
	public BigramModel(Map<String, Unigram> vocabulary, String[] corpusContent,
			boolean handlingUnknown) {
		bigramModel = new HashMap<String, Bigram>();
		this.unigramModel = vocabulary;
		if (handlingUnknown) {
			this.N = corpusContent.length + 1;

			populateModelHandlingUnknownWord(vocabulary, corpusContent);
		} else {
			this.N = corpusContent.length;
			populateModel(vocabulary, corpusContent);
		}
		this.V = vocabulary.size();
	}

	private void populateModel(Map<String, Unigram> unigrams, String[] tokens) {

		String lastToken = null;
		for (int i = 0; i < tokens.length; i++) {
			String token = tokens[i];
			// bigrams
			if (lastToken != null) {

				String bigramAsString = lastToken + " " + token;

				if (bigramModel.containsKey(bigramAsString)) {
					double count = bigramModel.get(bigramAsString).getCount();
					bigramModel.get(bigramAsString).setCount(count + 1);

				} else {
					Bigram bigram = new Bigram();
					bigram.setFirst(lastToken);
					bigram.setSecond(token);
					bigram.setCount(1);
					bigramModel.put(bigramAsString, bigram);
				}

			}
			lastToken = token;
		}
		setProbabilities(unigrams);

	}

	private void populateModelHandlingUnknownWord(
			Map<String, Unigram> unigramsWithUnknown, String[] tokens) {

		int size = tokens.length;

		int trainingSize = 4 * size / 5;

		String lastToken = null;
		for (int i = 0; i < trainingSize; i++) {
			String token = tokens[i];
			// bigrams
			if (lastToken != null) {

				String bigramAsString = lastToken + " " + token;

				if (bigramModel.containsKey(bigramAsString)) {
					double count = bigramModel.get(bigramAsString).getCount();
					bigramModel.get(bigramAsString).setCount(count + 1);

				} else {
					Bigram bigram = new Bigram();
					bigram.setFirst(lastToken);
					bigram.setSecond(token);
					bigram.setCount(1);
					bigramModel.put(bigramAsString, bigram);
				}

			}
			lastToken = token;
		}

		for (int i = 4 * size / 5; i < size; i++) {

			String word = tokens[i];

			if (!unigramsWithUnknown.containsKey(word)) {
				word = unKnownWord;
			}
			String bigramAsString = lastToken + " " + word;

			if (bigramModel.containsKey(bigramAsString)) {
				double count = bigramModel.get(bigramAsString).getCount();
				bigramModel.get(bigramAsString).setCount(count + 1);

			} else {
				Bigram bigram = new Bigram();
				bigram.setFirst(lastToken);
				bigram.setSecond(word);
				bigram.setCount(1);
				bigramModel.put(bigramAsString, bigram);
			}
			lastToken = word;

		}

		// set bigram probabilities
		setProbabilities(unigramsWithUnknown);

	}

	public void setProbabilities(Map<String, Unigram> unigrams) {
		for (String bigram : bigramModel.keySet()) {
			String first = bigram.split(" ")[0];
			double probability = bigramModel.get(bigram).getCount()
					/ unigrams.get(first).getCount();// P(wn/wn-1)=C(wn-1wn)/C(wn-1)
			bigramModel.get(bigram).setProbability(probability);
		}
	}

	private List<Bigram> getBigramsByPrefix(String prefix) {
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

		double probForGeneration = 0;
		Bigram lastBigram = null;
		for (Bigram bigram : bigrams) {
			lastBigram = bigram;

			double bigramProb = bigram.getProbability();
			probForGeneration = probForGeneration + bigramProb;

			if (random < probForGeneration) {
				return bigram;
			}
		}

		return lastBigram;
	}

	/**
	 * Returns the bigram count. As we dont's store bigrams with count 0 this
	 * method will check if bigram is in model and if yes will return the stored
	 * count, if not in model it will check first and second word in bigram in
	 * the vocabulary: if they both exist it returns 0 otherwise it will return
	 * -1.
	 * 
	 * @param bigram
	 *            a String representing a bigram
	 * 
	 * @return if bigram is in model and if yes will return the stored count, if
	 *         not in model it will check first and second word in bigram in the
	 *         vocabulary: if they both exist it returns 0 otherwise it will
	 *         return -1.
	 */
	public double getBigramCount(String bigram) {
		double result = -1;
		if (bigramModel.containsKey(bigram)) {
			return bigramModel.get(bigram).getCount();
		} else {
			String[] items = bigram.split(" ");
			String first = items[0];
			String second = items[1];

			if (unigramModel.keySet().contains(first)
					&& unigramModel.keySet().contains(second)) {
				return 0;
			}
		}
		return result;
	}

	/**
	 * Returns the bigram count. As we dont's store bigrams with count 0 this
	 * method will check if bigram is in model and if yes will return the stored
	 * count, if not in model it will check first and second word in bigram in
	 * the vocabulary: if they both exist it returns 0 otherwise it will return
	 * -1.
	 * 
	 * @param bigram
	 *            a String representing a bigram
	 * 
	 * @return if bigram is in model and if yes will return the stored count, if
	 *         not in model it will check first and second word in bigram in the
	 *         vocabulary: if they both exist it returns 0 otherwise it will
	 *         return -1.
	 */
	public double getSmoothedBigramCount(String bigram) {
		double result = -1;
		if (bigramModel.containsKey(bigram)) {
			return bigramModel.get(bigram).getCount() + 1;
		} else {
			String[] items = bigram.split(" ");
			String first = items[0];
			String second = items[1];

			if (unigramModel.keySet().contains(first)
					&& unigramModel.keySet().contains(second)) {
				return 1;
			}
		}
		return result;
	}

	public double getBigramProbability(String bigram) {
		double result = -1;
		if (bigramModel.containsKey(bigram)) {
			return bigramModel.get(bigram).getProbability();
		} else {
			String[] items = bigram.split(" ");
			String first = items[0];
			String second = items[1];

			if (unigramModel.keySet().contains(first)
					&& unigramModel.keySet().contains(second)) {
				return 0;
			}
		}
		return result;
	}

	public double getSmoothedBigramProbability(String bigram) {
		double result = -1;
		if (bigramModel.containsKey(bigram)) {
			return (bigramModel.get(bigram).getCount() + 1) / (N + V);
		} else {
			String[] items = bigram.split(" ");
			String first = items[0];
			String second = items[1];

			if (unigramModel.keySet().contains(first)
					&& unigramModel.keySet().contains(second)) {
				return 1 / (N + V);
			}
		}
		return result;
	}

	public boolean containsBigram(String bigram) {
		String[] items = bigram.split(" ");
		String first = items[0];
		String second = items[1];

		if (bigramModel.containsKey(bigram)) {
			return true;
		} else if (unigramModel.containsKey(first)
				&& unigramModel.containsKey(second)) {
			return true;
		} else
			return false;
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
