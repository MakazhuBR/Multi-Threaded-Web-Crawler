/**
 * Counts the number of times a word has occured.
 */
public class WordCount implements Comparable<WordCount>{
	private int count;
	private String word;
	
	/**
	 * @param word
	 */
	public WordCount(String word) {
		this.count = 0;
		this.word = word.toLowerCase();
	}	
	
	/**
	 * Intializes both the word and the count
	 * @param word
	 * @param count
	 */
	public WordCount(String word, int count) {
		this.count = count;
		this.word = word.toLowerCase();
	}	

	/**
	 * @return the numOccurences
	 */
	public int getCount() {
		return count;
	}
	
	/**
	 * @return the word
	 */
	public String getWord() {
		return word;
	}
	
	/**
	 * Increments the internal counter for the number of occurences;
	 */
	public void increment() {
		count++;
	}
	
	/*
	 * @param WordCount
	 */

	public int compareTo(WordCount o) {
		if (count != o.getCount()) {
			return count - o.count;
		} else {
			return word.compareTo(o.word);
		}
	}
}
