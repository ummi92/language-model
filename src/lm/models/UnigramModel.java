package lm.models;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class UnigramModel {

	public HashMap<String, Integer> calculateUnigramModel(String[] input) {

		int size = input.length;

		HashMap<String, Integer> output = new HashMap<String, Integer>();

		for (int count = 0; count < size; count++) {

			String temp = input[count];

			if (!output.containsKey(temp)) {
				output.put(temp, new Integer(1));
			} else {

				Integer occ = output.get(temp);
				int noc = occ.intValue() + 1;
				output.put(temp, new Integer(noc));
			}

		}

		return output;
	}

	public void writeUnigramModelToFile(HashMap<String, Integer> output) {
		BufferedWriter bufferedWriter = null;
		try {
			// FileOutputStream fstream = new FileOutputStream("unigram.txt");
			// DataOutputStream out = new DataOutputStream(fstream);
			// Set<String> keys = output.keySet();
			// Iterator<String> iter = keys.iterator();
			//	        
			// while(iter.hasNext()){
			// String s = iter.next();
			// System.out.print(s);
			// System.out.print(" ");
			// int i = output.get(s).intValue();
			// System.out.println(i);
			//	           
			// out.writeChars(s);
			// out.writeChars(" ");
			// out.writeInt(i);
			// out.writeChars("\n");
			// }
			// out.close();
			bufferedWriter = new BufferedWriter(new FileWriter("unigram.txt"));

			for (String word : output.keySet()) {
				bufferedWriter.write(word + " " + output.get(word));
				bufferedWriter.newLine();
			}

		} catch (Exception e) {
			System.err.println("Error: " + e.getMessage());
		} finally {
			if (bufferedWriter != null) {
				try {
					bufferedWriter.flush();
					bufferedWriter.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public double getTokenCount(HashMap<String, Integer> unigram) {
		double tokenCount = 0;
		
		for(String word : unigram.keySet()){
			tokenCount += unigram.get(word);
		}

		return tokenCount;

	}
}
