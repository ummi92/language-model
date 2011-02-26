package lm;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import lm.models.BigramModel;
import lm.objects.Bigram;
import lm.objects.Unigram;

public class Smoothing {

	public static void unigramSmoothing(HashMap<String, Unigram> unigramModel){
		
		Set<String> wordSet = unigramModel.keySet();
        
        BigDecimal totalWords = new BigDecimal(wordSet.size());
		BigDecimal totalCount = BigDecimal.ZERO;
		
		for(String word : wordSet){
			totalCount.add(unigramModel.get(word).getCount());
		}
		
		for (String word : wordSet) {
			Unigram unigram = unigramModel.get(word);
			BigDecimal wordCount = unigram.getCount();
			unigram.setCount(wordCount.add(BigDecimal.ONE));
			// calculate new probability
		}

	}
	
	public static void bigramSmoothing(BigramModel model){
		
		Map<String, Bigram> bigramModel = model.getBigramModel();
		
		for (String word : bigramModel.keySet()) {
			
			String prefix = word.substring(0, word.indexOf(" "));
			List<Bigram> bigrams = model.getBigramsByPrefix(prefix);
			BigDecimal totalCount = BigDecimal.ZERO;
			BigDecimal prefixCount = new BigDecimal(bigrams.size());
			
			for(Bigram bigram : bigrams){
				totalCount.add(bigram.getCount());
			}
			
			Bigram bigram = bigramModel.get(word);
			BigDecimal wordCount = bigram.getCount();
			wordCount.add(BigDecimal.ONE);
			bigram.setCount(wordCount);
			
			// calculate new probability
		}
	}
	
	
}
