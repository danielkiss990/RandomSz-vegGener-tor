
package bullshit;

import java.util.Comparator;

public class ListComparatorForOfSpeeches implements Comparator<OfSpeech>{

    @Override
    public int compare(OfSpeech o1, OfSpeech o2) {
        if(o1.getCounter()==o2.getCounter()){
            return 0;
        }else if( o1.getCounter()>o2.getCounter()){
            return -1;
        }
        return 1;
    }
}
