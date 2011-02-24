package lm.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lm.objects.Bigram;

public class BigramModel {

	private Map<String, Bigram> bigramModel;

	public BigramModel(String[] vocabulary, String[] corpusContent) {
		bigramModel = new HashMap<String, Bigram>();
		populateModel(vocabulary, corpusContent);
	}

	private void populateModel(String[] vocabulary, String[] tokens) {

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

		List<String> allPossibleBigrams = new ArrayList<String>();
		// add the zeroes
		for (int i = 0; i < vocabulary.length; i++) {
			for (int j = 1; j < vocabulary.length; j++) {

				allPossibleBigrams.add(vocabulary[i] + " " + vocabulary[j]);
			}

		}

		for (String bigram : allPossibleBigrams) {
			if (!bigramModel.containsKey(bigram)) {
				Bigram bigramObj = new Bigram();
				String[] items = bigram.split(" ");
				bigramObj.setFirst(items[0]);
				bigramObj.setSecond(items[1]);
				bigramModel.put(bigram, bigramObj);
			}
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
