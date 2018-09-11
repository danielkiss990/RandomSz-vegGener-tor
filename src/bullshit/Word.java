
package bullshit;

public class Word {
    
    private String word;
    private int counter=1;

    public String getWord() {
        
        return word;
    }

    public int getCounter() {
        return counter;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setCounter() {
        this.counter += 1;
    }

  
    
}