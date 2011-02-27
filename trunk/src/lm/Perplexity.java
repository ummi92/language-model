package lm;

import java.util.Map;
import lm.models.*;
import lm.objects.*;

public class Perplexity {

	static double computeUnigramPerplexity(Map<String, Unigram> unigramModel,
			String[] testset) {
		double logProbability = 0.0;
		double totalcount = testset.length;
		double templog, uniperplexity;

		for (int i = 0; i < testset.length; i++) {

			if (unigramModel.containsKey(testset[i])) {
				logProbability += Math.log(unigramModel.get(testset[i])
						.getProbability());
			} else {
				logProbability += Math.log(unigramModel.get("<UNK>")
						.getProbability());
			}
		}

		templog = -1.0 * logProbability;
		templog = Math.log(templog) - Math.log(totalcount);
		uniperplexity = Math.exp(Math.exp(templog));
		return uniperplexity;

	}

	static double computeBigramPerplexity(BigramModel bigramModel,
			String[] testset) {
		double logProbability = 0.0;
		double totalcount = testset.length;
		double templog, biperplexity;

		for (int i = 0; i < testset.length - 1; i++) {
			if (bigramModel.containsBigram(testset[i] + " " + testset[i + 1])) {
				System.out.println("Bigram = " + testset[i] + " "
						+ testset[i + 1]);
				System.out.println("Probability = "
						+ bigramModel.getSmoothedBigramProbability(testset[i]
								+ " " + testset[i + 1]));
				logProbability += Math.log(bigramModel
						.getSmoothedBigramProbability(testset[i] + " "
								+ testset[i + 1]));
				continue;
			} else if (bigramModel.containsBigram(testset[i] + " " + "<UNK>")) {
				System.out.println("Bigram = " + testset[i] + " " + "<UNK>");
				System.out.println("Probability = "
						+ bigramModel.getSmoothedBigramProbability(testset[i]
								+ " " + "<UNK>"));
				logProbability += Math.log(bigramModel
						.getSmoothedBigramProbability(testset[i] + " "
								+ "<UNK>"));
				continue;
			} else if (bigramModel.containsBigram("<UNK>" + " "
					+ testset[i + 1])) {
				System.out
						.println("Bigram = " + "<UNK>" + " " + testset[i + 1]);
				System.out.println("Probability = "
						+ bigramModel.getSmoothedBigramProbability("<UNK>"
								+ " " + testset[i + 1]));
				logProbability += Math.log(bigramModel
						.getSmoothedBigramProbability("<UNK>" + " "
								+ testset[i + 1]));
				continue;
			} else if (bigramModel.containsBigram("<UNK>" + " " + "<UNK>")) {
				System.out.println("Bigram = " + "<UNK>" + " " + "<UNK>");
				System.out.println("Probability = "
						+ bigramModel.getSmoothedBigramProbability("<UNK>"
								+ " " + "<UNK>"));
				logProbability += Math.log(bigramModel
						.getSmoothedBigramProbability("<UNK>" + " " + "<UNK>"));
				continue;
			}
		}

		templog = -1.0 * logProbability;
		templog = templog / totalcount;
		biperplexity = Math.exp(templog);
		return biperplexity;

	}

	/*
	 * static double computeBigramPerplexity(Map<String, Bigram> bigramModel,
	 * String[] testset) { double logProbability = 0.0; double totalcount =
	 * testset.length; double templog, biperplexity;
	 * 
	 * for(int i = 0; i < testset.length - 1; i++) { if(
	 * bigramModel.containsKey(testset[i] + " " + testset[i+1]) ) {
	 * logProbability += Math.log( bigramModel.get(testset[i] + " " +
	 * testset[i+1]).getProbability() ); break; } else if(
	 * bigramModel.containsKey(testset[i] + " " + "<UNK>") ) { logProbability +=
	 * Math.log( bigramModel.get(testset[i] + " " + "<UNK>").getProbability() );
	 * break; } else if( bigramModel.containsKey("<UNK>" + " " + testset[i+1]) )
	 * { logProbability += Math.log( bigramModel.get("<UNK>" + " " +
	 * testset[i+1]).getProbability() ); break; } else if(
	 * bigramModel.containsKey("<UNK>" + " " + "<UNK>") ) { logProbability +=
	 * Math.log( bigramModel.get("<UNK>" + " " + "<UNK>").getProbability() );
	 * break; }
	 * 
	 * }
	 * 
	 * templog = -1.0 * logProbability; templog = Math.log(templog) -
	 * Math.log(totalcount); biperplexity = Math.exp(Math.exp(templog)); return
	 * biperplexity; }
	 */

	/*
	 * static double computeTrigramPerplexity(Map<String, Trigram> trigramModel,
	 * String[] testset) { double logProbability = 0.0; double totalcount =
	 * testset.length; double templog, triperplexity;
	 * 
	 * for(int i = 0; i < testset.length - 2; i++) {
	 * if(trigramModel.containsKey(testset[i] + " " + testset[i+1] + " " +
	 * testset[i+2])) { logProbability += Math.log(trigramModel.get(testset[i] +
	 * " " + testset[i+1] + " " + testset[i+2]).getProbability()); break; } else
	 * if(trigramModel.containsKey(testset[i] + " " + testset[i+1] + " " +
	 * "<UNK>")) { logProbability += Math.log(trigramModel.get(testset[i] + " "
	 * + testset[i+1] + " " + "<UNK>").getProbability()); break; } else
	 * if(trigramModel.containsKey(testset[i] + " " + "<UNK>" + " " +
	 * testset[i+2])) { logProbability += Math.log(trigramModel.get(testset[i] +
	 * " " + "<UNK>" + " " + testset[i+2]).getProbability()); break; } else
	 * if(trigramModel.containsKey("<UNK>" + " " + testset[i+1] + " " +
	 * testset[i+2])) { logProbability += Math.log(trigramModel.get("<UNK>" +
	 * " " + testset[i+1] + " " + testset[i+2]).getProbability()); break; } else
	 * if(trigramModel.containsKey("<UNK>" + " " + "<UNK>" + " " +
	 * testset[i+2])) { logProbability += Math.log(trigramModel.get("<UNK>" +
	 * " " + "<UNK>" + " " + testset[i+2]).getProbability()); break; } else
	 * if(trigramModel.containsKey("<UNK>" + " " + testset[i+1] + " " +
	 * "<UNK>")) { logProbability += Math.log(trigramModel.get("<UNK>" + " " +
	 * testset[i+1] + " " + "<UNK>").getProbability()); break; } else
	 * if(trigramModel.containsKey(testset[i] + " " + "<UNK>" + " " + "<UNK>"))
	 * { logProbability += Math.log(trigramModel.get(testset[i] + " " + "<UNK>"
	 * + " " + "<UNK>").getProbability()); break; } else
	 * if(trigramModel.containsKey("<UNK>" + " " + "<UNK>" + " " + "<UNK>")) {
	 * logProbability += Math.log(trigramModel.get("<UNK>" + " " + "<UNK>" + " "
	 * + "<UNK>").getProbability()); break; }
	 * 
	 * }
	 * 
	 * templog = -1.0 * logProbability; templog = Math.log(templog) -
	 * Math.log(totalcount); triperplexity = Math.exp(Math.exp(templog)); return
	 * triperplexity; }
	 */

}