package lm.objects;


public class Unigram implements Comparable{

	private String word;
	private double count = 0;
	private double probability = 0;
	
	public void setWord(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}
	
	public double getCount() {
		return count;
	}

	public void setCount(double count) {
		this.count = count;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	public int compareTo(Object o) {
		Unigram to = (Unigram) o;
		int result=0;
		if (this.count > to.count) result = 1;
		else if(this.count < to.count) result = -1;
		return (result);
	}
	
	public String toString() {

		return word;
	}
}
