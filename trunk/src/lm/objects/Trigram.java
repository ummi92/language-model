package lm.objects;

public class Trigram {

	private String bigram;
	private String second;

	private double count = 0;
	private double probability = 0;

	public String getBigram() {
		return bigram;
	}

	public void setBigram(String bigram) {
		this.bigram = bigram;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
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
		Trigram to = (Trigram) o;
		int result = 0;
		if (this.count > to.count)
			result = 1;
		else if (this.count < to.count)
			result = -1;
		return (result);
	}

	@Override
	public String toString() {

		return bigram + " " + second;
	}

}
