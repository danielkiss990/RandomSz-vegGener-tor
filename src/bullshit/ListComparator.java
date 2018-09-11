
package bullshit;

import java.util.Comparator;

public class ListComparator implements Comparator<Word>{

    @Override
    public int compare(Word o1, Word o2) {
        if(o1.getCounter()==o2.getCounter()){
            return 0;
        }else if( o1.getCounter()>o2.getCounter()){
            return -1;
        }
        return 1;
    }
}
