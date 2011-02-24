package lm.models;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.io.*;


public class UnigramModel {

	
	public void calculateUnigramModel(String[] input){
		
		int size = input.length;
		
		HashMap<String,Integer> output = new HashMap<String,Integer>();
		
		for(int count = 0; count < size; count++){
		
			String temp = input[count];
			
			if(!output.containsKey(temp)){
				output.put(temp, new Integer(1));
			}
			else {
				
				Integer occ = output.get(temp);
				int noc = occ.intValue()+1;
				output.put(temp, new Integer(noc));
			}
			
		}
		
		try{
			FileOutputStream fstream = new FileOutputStream("unigram.txt");
			DataOutputStream  out = new DataOutputStream(fstream);
	        Set<String> keys = output.keySet();
	        Iterator<String> iter = keys.iterator();
	        
	        while(iter.hasNext()){
	           String s = iter.next();
	           System.out.print(s);
	           System.out.print(" ");
	           int i = output.get(s).intValue();
	           System.out.println(i);
	           
	           out.writeChars(s);
	           out.writeChars(" ");
	           out.writeInt(i);
	           out.writeChars("\n");
	        }
	        out.close();
				
        }catch(Exception e){
           System.err.println("Error: " + e.getMessage());
        }
	}
}

