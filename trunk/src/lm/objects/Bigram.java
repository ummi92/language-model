package lm.objects;

import java.math.BigDecimal;

public class Bigram implements Comparable {
	private String first;
	private String second;

	private BigDecimal count = BigDecimal.ZERO;
	private BigDecimal probability = BigDecimal.ZERO;

	public String getFirst() {
		return first;
	}

	public void setFirst(String first) {
		this.first = first;
	}

	public String getSecond() {
		return second;
	}

	public void setSecond(String second) {
		this.second = second;
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
		Bigram to = (Bigram) o;
		return (this.count.compareTo(to.count));
	}

	@Override
	public String toString() {

		return first + " " + second;
	}
}
