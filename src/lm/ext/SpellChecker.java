package lm.ext;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import lm.FileRead;
import lm.models.BigramModel;
import lm.models.UnigramModel;
import lm.objects.Bigram;
import lm.objects.Unigram;

public class SpellChecker {

	private final HashMap<String, Integer> nWords = new HashMap<String, Integer>();
	BigramModel bigram = null;
	private Map<String, Unigram> unigramModel;
	
	public SpellChecker(String file){
		BufferedReader in = null;
		try {
		in = new BufferedReader(new FileReader(file));
		
		String[] filecontent = FileRead.fileReadLowerCase(file);
		UnigramModel unigram = new UnigramModel(filecontent, false);
		unigramModel = unigram.getUnigramModel();
		bigram = new BigramModel(unigramModel, filecontent, false);
		//bigram.setProbabilities(unigramModel);
		//bigramModel = bigram.getBigramModel();
		
		Pattern p = Pattern.compile("\\w+");
		for(String temp = ""; temp != null; temp = in.readLine()){
			Matcher m = p.matcher(temp.toLowerCase());
			while(m.find()) nWords.put((temp = m.group()), nWords.containsKey(temp) ? nWords.get(temp) + 1 : 1);
		}
		} catch (IOException exc) {
			exc.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException exc) {
				exc.printStackTrace();
			}
		}
	}


	private final ArrayList<String> edits(String word) {
		ArrayList<String> result = new ArrayList<String>();
		for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));
		for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
		for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
		for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
		return result;
	}

	public final String check(String sent) {
		
		
		String[] words = sent.split(" ");
		
		if(words.length == 1){
		
		if(nWords.containsKey(sent.toLowerCase())) return sent;
		ArrayList<String> list = edits(sent.toLowerCase());
		HashMap<Integer, String> candidates = new HashMap<Integer, String>();
		for(String s : list) if(nWords.containsKey(s)) candidates.put(nWords.get(s),s);
		if(candidates.size() > 0) return candidates.get(Collections.max(candidates.keySet()));
		for(String s : list) for(String w : edits(s)) if(nWords.containsKey(w)) candidates.put(nWords.get(w),w);
		return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : sent;
		
		}
		
		String lastToken = null;
		for (int i = 0; i < words.length; i++) {
			String token = words[i].toLowerCase();
			
			if (lastToken != null) {

				String bigramAsString = lastToken + " " + token;
System.out.println("original string = "+bigramAsString);
				if (bigram.containsBigram(bigramAsString)) {
					double prob = bigram.getBigramProbability(bigramAsString);
                    if(prob == 0.0){
                    	if(unigramModel.containsKey(lastToken)){
                    		ArrayList<String> list = edits(token);
    						List<Bigram> lists = bigram.getBigramsByPrefix(lastToken);
    						HashMap<Double, String> candidates = new HashMap<Double, String>();
    						for(Bigram s : lists) if(list.contains(s.getSecond())) candidates.put(new Double(s.getProbability()),s.getSecond());
    						if(candidates.size() > 0) words[i] = candidates.get(Collections.max(candidates.keySet()));
    						candidates.clear();
    						for(String s : list) for(String w : edits(s)) {
    							double secProb = containSecond(lists,w);
    							if(secProb >= 0.0) candidates.put(secProb,w);
    						}
    						words[i] = candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : token;
                    	 }
    					
                        if(unigramModel.containsKey(token)){
                        	ArrayList<String> list = edits(lastToken);
    						List<Bigram> lists = bigram.getBigramsBySuffix(token);
    						HashMap<Double, String> candidates = new HashMap<Double, String>();
    						for(Bigram s : lists) if(list.contains(s.getFirst())) candidates.put(new Double(s.getProbability()),s.getFirst());
    						if(candidates.size() > 0) words[i] = candidates.get(Collections.max(candidates.keySet()));
    						candidates.clear();
    						for(String s : list) for(String w : edits(s)) {
    							double firstProb = containFirst(lists,w);
    							if(firstProb >= 0.0) candidates.put(firstProb,w);
    						}
    						words[i-1] = candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : lastToken;
                    	 }
                    	
                    }
				} else {
					if(unigramModel.containsKey(lastToken)){
						ArrayList<String> list = edits(token);
						List<Bigram> lists = bigram.getBigramsByPrefix(lastToken);
						HashMap<Double, String> candidates = new HashMap<Double, String>();
						for(Bigram s : lists) if(list.contains(s.getSecond())) candidates.put(new Double(s.getProbability()),s.getSecond());
						if(candidates.size() > 0) words[i] = candidates.get(Collections.max(candidates.keySet()));
						candidates.clear();
						for(String s : list) for(String w : edits(s)) {
							double secProb = containSecond(lists,w);
							if(secProb >= 0.0) candidates.put(secProb,w);
						}
						words[i] = candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : token;
                	 }
					
					if(unigramModel.containsKey(token)){
                    	ArrayList<String> list = edits(lastToken);
						List<Bigram> lists = bigram.getBigramsBySuffix(token);
						HashMap<Double, String> candidates = new HashMap<Double, String>();
						for(Bigram s : lists) if(list.contains(s.getFirst())) candidates.put(new Double(s.getProbability()),s.getFirst());
						if(candidates.size() > 0) words[i] = candidates.get(Collections.max(candidates.keySet()));
						candidates.clear();
						for(String s : list) for(String w : edits(s)) {
							double firstProb = containFirst(lists,w);
							if(firstProb >= 0.0) candidates.put(firstProb,w);
						}
						words[i-1] = candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : lastToken;
                	 }
				}

			}
			if(i>0)System.out.println("new string = "+words[i-1]+" "+words[i]);
			lastToken = words[i].toLowerCase();
		}
		
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i<words.length; i++){
			sb.append(words[i]);
			sb.append(" ");
		}
		return sb.toString();
	}
	
	private double containFirst(List<Bigram> lists, String word){
		double result = -1;
		
		for(Bigram bigram : lists){
			if (bigram.getFirst().equals(word)) result = bigram.getProbability();
		}
		
		return result;
	}
	
	private double containSecond(List<Bigram> lists, String word){
		double result = -1;
		
		for(Bigram bigram : lists){
			if (bigram.getSecond().equals(word)) result = bigram.getProbability();
		}
		
		return result;
	}

}
