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
	HashMap<Double, String> probs = new HashMap<Double, String>();
	BigramModel bigram = null;
	private Map<String, Unigram> unigramModel;
	
	public SpellChecker(String file){
		BufferedReader in = null;
		try {
		in = new BufferedReader(new FileReader(file));
		
		String[] filecontent = FileRead.fileReadLowerCase(file);
		UnigramModel unigram = new UnigramModel(filecontent, true);
		unigramModel = unigram.getUnigramModel();
		bigram = new BigramModel(unigramModel, filecontent, true);
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
	//	for(int i=0; i < word.length(); ++i) result.add(word.substring(0, i) + word.substring(i+1));
	//	for(int i=0; i < word.length()-1; ++i) result.add(word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2));
		for(int i=0; i < word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i+1));
	//	for(int i=0; i <= word.length(); ++i) for(char c='a'; c <= 'z'; ++c) result.add(word.substring(0, i) + String.valueOf(c) + word.substring(i));
		return result;
	}

	public final String check(String sent) {
		
		
		String[] words = sent.split(" ");
		
		if(words.length == 1){
		
		if(nWords.containsKey(sent.toLowerCase())) return sent;
		ArrayList<String> list = edits(sent.toLowerCase());
		for (String s : list) System.out.println(s);
		HashMap<Integer, String> candidates = new HashMap<Integer, String>();
		for(String s : list) if(nWords.containsKey(s)) candidates.put(nWords.get(s),s);
		if(candidates.size() > 0) return candidates.get(Collections.max(candidates.keySet()));
		for(String s : list) for(String w : edits(s)) if(nWords.containsKey(w)) candidates.put(nWords.get(w),w);
		return candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : sent;
		
		}
		
		String lastToken = null;
		StringBuffer sb1 = new StringBuffer();
		for(int i = 0; i<words.length; i++){
			sb1.append(words[i]);
			sb1.append(" ");
		}
		//System.out.println(calculateJointProb(words));
		//probs.put(calculateJointProb(words), sb1.toString());
		
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
    						for(Bigram s : lists) if(list.contains(s.getSecond())) {
    							candidates.put(new Double(s.getProbability()),s.getSecond());
    							addTo(words,i,lastToken,s.getSecond());
    						}
    						//if(candidates.size() > 0) words[i] = candidates.get(Collections.max(candidates.keySet()));
    						//candidates.clear();
    						for(String s : list) for(String w : edits(s)) {
    							double secProb = containSecond(lists,w);
    							if(secProb >= 0.0) {
    								candidates.put(secProb,w);
    								addTo(words,i,lastToken,w);
    							}
    							
    						}
    						words[i] = candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : token;
    						candidates.clear(); 
                    	}
    					
                        if(unigramModel.containsKey(token)){
                        	ArrayList<String> list = edits(lastToken);
    						List<Bigram> lists = bigram.getBigramsBySuffix(token);
    						HashMap<Double, String> candidates = new HashMap<Double, String>();
    						for(Bigram s : lists) if(list.contains(s.getFirst())) {
    							candidates.put(new Double(s.getProbability()),s.getFirst());
    							addTo(words,i,s.getFirst(),token);
    						}
    						//if(candidates.size() > 0) words[i] = candidates.get(Collections.max(candidates.keySet()));
    						//candidates.clear();
    						for(String s : list) for(String w : edits(s)) {
    							double firstProb = containFirst(lists,w);
    							if(firstProb >= 0.0) {
    								candidates.put(firstProb,w);
    								addTo(words,i,w,token);
    							}
    							
    						}
    						words[i-1] = candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : lastToken;
    						candidates.clear(); 
                        }
                    	
                    }
				} else {
					if(unigramModel.containsKey(lastToken)){
						ArrayList<String> list = edits(token);
						List<Bigram> lists = bigram.getBigramsByPrefix(lastToken);
						HashMap<Double, String> candidates = new HashMap<Double, String>();
						for(Bigram s : lists) if(list.contains(s.getSecond())) {
							candidates.put(new Double(s.getProbability()),s.getSecond());
							addTo(words,i,lastToken,s.getSecond());
						}
						//if(candidates.size() > 0) words[i] = candidates.get(Collections.max(candidates.keySet()));
						//candidates.clear();
						for(String s : list) for(String w : edits(s)) {
							double secProb = containSecond(lists,w);
							if(secProb >= 0.0) {
								candidates.put(secProb,w);
								addTo(words,i,lastToken,w);
							}
							
						}
						words[i] = candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : token;
						candidates.clear(); 
					}
					
					if(unigramModel.containsKey(token)){
                    	ArrayList<String> list = edits(lastToken);
						List<Bigram> lists = bigram.getBigramsBySuffix(token);
						HashMap<Double, String> candidates = new HashMap<Double, String>();
						for(Bigram s : lists) if(list.contains(s.getFirst())) {
							candidates.put(new Double(s.getProbability()),s.getFirst());
							addTo(words,i,s.getFirst(),token);
						}
						//if(candidates.size() > 0) words[i] = candidates.get(Collections.max(candidates.keySet()));
						//candidates.clear();
						for(String s : list) for(String w : edits(s)) {
							double firstProb = containFirst(lists,w);
							if(firstProb >= 0.0) {
								candidates.put(firstProb,w);
								addTo(words,i,w,token);
							}
						}
						words[i-1] = candidates.size() > 0 ? candidates.get(Collections.max(candidates.keySet())) : lastToken;
						candidates.clear(); 
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
		//System.out.println(sb.toString());
		//probs.put(calculateJointProb(words), sb.toString());
		//System.out.println(calculateJointProb(words));
		//System.out.println(Collections.max(probs.keySet()));
		//String result = probs.size() > 0 ? probs.get(Collections.max(probs.keySet())) : sb.toString();
		//probs.clear();
		return sb.toString();
	}
	
	private double containFirst(List<Bigram> lists, String word){
		double result = -1;
		
		for(Bigram bigram : lists){
			if (bigram.getFirst().toLowerCase().equals(word.toLowerCase())) result = bigram.getProbability();
		}
		
		return result;
	}
	
	private double containSecond(List<Bigram> lists, String word){
		double result = -1;
		
		for(Bigram bigram : lists){
			if (bigram.getSecond().toLowerCase().equals(word.toLowerCase())) result = bigram.getProbability();
		}
		
		return result;
	}
	
	private double calculateJointProb(String[] testset, int index, String lastToken, String token){
		
		double logProbability = 0.0;
		
		testset[index-1] = lastToken;
		testset[index] = token;
		

		return calculateJointProb(testset);
}
	
	private double calculateJointProb(String[] testset){
		double logProbability = 0.0;
		for (int i = 0; i < testset.length - 1; i++) {
			if (bigram.containsBigram(testset[i] + " " + testset[i + 1])) {
			//	System.out.println("Bigram = " + testset[i] + " "+ testset[i + 1]);
			//	System.out.println("Probability = "	+ bigramModel.getSmoothedBigramProbability(testset[i]+ " " + testset[i + 1]));
				logProbability += Math.log(bigram
						.getSmoothedBigramProbability(testset[i] + " "
								+ testset[i + 1]));
				continue;
			} else if (bigram.containsBigram(testset[i] + " " + "<UNK>")) {
			//	System.out.println("Bigram = " + testset[i] + " " + "<UNK>");
			//	System.out.println("Probability = "	+ bigramModel.getSmoothedBigramProbability(testset[i]+ " " + "<UNK>"));
				logProbability += Math.log(bigram.getSmoothedBigramProbability(testset[i] + " "+ "<UNK>"));
				continue;
			} else if (bigram.containsBigram("<UNK>" + " "
					+ testset[i + 1])) {
			//	System.out.println("Bigram = " + "<UNK>" + " " + testset[i + 1]);
			//	System.out.println("Probability = "	+ bigramModel.getSmoothedBigramProbability("<UNK>"+ " " + testset[i + 1]));
				logProbability += Math.log(bigram
						.getSmoothedBigramProbability("<UNK>" + " "
								+ testset[i + 1]));
				continue;
			} else if (bigram.containsBigram("<UNK>" + " " + "<UNK>")) {
			//	System.out.println("Bigram = " + "<UNK>" + " " + "<UNK>");
			//	System.out.println("Probability = "+ bigramModel.getSmoothedBigramProbability("<UNK>"+ " " + "<UNK>"));
				logProbability += Math.log(bigram
						.getSmoothedBigramProbability("<UNK>" + " " + "<UNK>"));
				continue;
			}
		}

		return Math.exp(logProbability);
	}
	
	private void addTo(String[] testset, int index, String lastToken, String token){
		StringBuffer sb = new StringBuffer();
		for(int i = 0; i<testset.length; i++){
			sb.append(testset[i]);
			sb.append(" ");
		}
		probs.put(this.calculateJointProb(testset, index, lastToken, token), sb.toString());
	}
}
