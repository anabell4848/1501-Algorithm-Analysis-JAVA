import java.util.ArrayList;
/*
 SortDict will also use the underlying ArrayList to a large 
 extent, but it will require some additional work: the insert() method must make sure the 
 data is sorted (don't rely on the dictionary file being sorted) and the find() method must 
 utilize binary search.  Think about how you will implement these and test them thoroughly 
 before timing them – refer to your CS 0445 textbook for help with binary search, interfaces 
 and object-oriented programming.  You MAY NOT use any Java predefined binary search method 
 in your SortDict class (i.e. you must code binary search yourself). 
 
 */
import java.util.Collections;

public class SortDict extends ArrayList<String> implements searchTest{

	private ArrayList<String> list;

	public SortDict(){
		list = new ArrayList<String>();
	}
	//int count=0;
	public void insert(String s) {
		list.add(s);
		//count++;
		//System.out.println(count);
		if (list.size() > 1){
			String prevLast = list.get(list.size()-2);
			if (s.compareTo(prevLast) < 0)
				Collections.sort(list);
		} 
	}

	public boolean find(String s) {
		int low = 0;
        int high = list.size() - 1;
        //System.out.println(low+" "+ high);
        while(low <= high){
            //mid = (low + high) / 2;
            int mid = (low + high) >>> 1;

            //System.out.println("list.get(mid).compareTo(s)="+list.get(mid).compareTo(s)+"string="+s);
            if( list.get(mid).compareTo(s) < 0 ){
                low = mid + 1;
            }
            else if( list.get(mid).compareTo(s) > 0 ){
                high = mid - 1;
            }
            else{
                return true;
            }
        }
		
		return false;
	}

}
