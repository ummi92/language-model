package lm.models;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
	
	private void populateModel(String[] vocabulary){
		
		int size = vocabulary.length;
		
		
		for(int count = 0; count < size; count++){
		
			String word = vocabulary[count];
			
			if(unigramModel.containsKey(word)){
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
		
	        Set<String> wordSet = unigramModel.keySet();
	        Iterator<String> iter = wordSet.iterator();
	        
	        BigDecimal totalCount = new BigDecimal(size);
	        
	        while(iter.hasNext()){
	        	        	
	           String word = iter.next();
	           Unigram unigram = unigramModel.get(word);
	           unigram.setProbability(unigram.getCount().divide(totalCount));
	          
	        }
	        
	}
}

