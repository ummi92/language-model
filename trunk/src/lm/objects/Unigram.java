package lm.objects;

import java.math.BigDecimal;

public class Unigram implements Comparable {

	private String word;
	private BigDecimal count = BigDecimal.ZERO;
	private BigDecimal probability = BigDecimal.ZERO;

	public void setWord(String word) {
		this.word = word;
	}

	public String getWord() {
		return word;
	}

	public BigDecimal getCount() {
		return count;
	}

	public void setCount(BigDecimal count) {
		this.count = count;
	}

	public BigDecimal getProbability() {
		return probability;
	}

	public void setProbability(BigDecimal probability) {
		this.probability = probability;
	}

	public int compareTo(Object o) {
		Unigram to = (Unigram) o;
		return (this.probability.compareTo(to.probability));
	}

	public String toString() {

		return word;
	}
}
