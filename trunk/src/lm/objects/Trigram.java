package lm.objects;

public class Trigram implements Comparable {

	private String bigram;
	private String third;

	private double count = 0;
	private double probability = 0;

	public String getBigram() {
		return bigram;
	}

	public void setBigram(String bigram) {
		this.bigram = bigram;
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

	public String getThird() {
		return third;
	}

	public void setThird(String third) {
		this.third = third;
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

		return bigram + " " + third;
	}

}
