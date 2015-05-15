import java.util.ArrayList;

/*
 For class SeqDict you can largely use the functionality of the underlying ArrayList class.  
 In other words, the class will add the insert() and find() methods but it will do so using 
 the functionality that is already part of the ArrayList[1].  Consequently, SeqDict will be 
 almost trivial to implement. 
 */


public class SeqDict extends ArrayList<String> implements searchTest {
	
	private ArrayList<String> list;

	public SeqDict(){
		list = new ArrayList<String>();
	}
	
	//int count=0;
	public void insert(String s) {
		list.add(s);
		//count++;
		//System.out.println(count);
	}

	public boolean find(String s) {
		for(int j=0;j<list.size();j++){
			//System.out.println("list.get(j)="+list.get(j)+"   s="+ s);
			if((list.get(j)).equals(s)){
				//System.out.println("list.get(j)="+list.get(j)+"   s="+ s);
				return true;
			}
		}
		return false;
	}

}
