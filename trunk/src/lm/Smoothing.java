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
        
		double totalWords = wordSet.size();
        double totalCount = 0;
		
		for(String word : wordSet){
			totalCount = totalCount+unigramModel.get(word).getCount();
		}
		
		for (String word : wordSet) {
			Unigram unigram = unigramModel.get(word);
			double wordCount = unigram.getCount()+1;
			unigram.setCount(wordCount);
			unigram.setProbability(wordCount/totalCount+totalWords);
		}

	}
	
//	public static void bigramSmoothing(BigramModel model){
//		
//		Map<String, Bigram> bigramModel = model.getBigramModel();
//		
//		for (String word : bigramModel.keySet()) {
//			
//			String prefix = word.substring(0, word.indexOf(" "));
//			List<Bigram> bigrams = model.getBigramsByPrefix(prefix);
//			double totalCount = 0;
//			double prefixCount = bigrams.size();
//			
//			for(Bigram bigram : bigrams){
//				totalCount= totalCount + bigram.getCount();
//			}
//			
//			Bigram bigram = bigramModel.get(word);
//			double wordCount = bigram.getCount()+1;
//			bigram.setCount(wordCount);
//			bigram.setProbability(wordCount/totalCount+prefixCount);
//		    
//		}
//	}
	
	
}
