import java.util.ArrayList;

/*
  Class HashDict must be built from scratch using double hashing, as 
  we discussed in lecture.  You may assume that your HashDict class will store only 
  String objects, so the underlying data structure for your hash table should be an array of String.  Regarding 
  the hash function itself, you should start with the predefined hashCode() method for 
  Strings.  However, be careful with the result – it returns a negative number for some 
  Strings – you'll need to convert it to a non-negative number for it to map correctly.  
  See the HashTest.java handout to see how this can be done.  Once you have a non-negative 
  value, you can then simply take it modulo the table size for your final hash index.  
  Regarding the 2nd hash function (h2(x), used for the increment), start with the non-negative 
  value that you determined for the primary hash function.  Call this value hcode.  
  Your h2(x) increment should then be
		h2(x) =  (hcode % dhval) + 1
  where dhval is the greatest prime number less than the table size.		

  You should keep your hash table at most 75% full, so you must incorporate a resizing ability 
  into your insert() method.   Specifically, if the table gets over 75% full, you must create 
  a new array that is approximately twice the size of the previous array, and rehash all of the 
  data into the new table.  Your new table size should be the smallest prime number greater than 
  twice the previous table size.  You will also need to modify your 2nd hash function when you 
  rehash, so that it remains effective.   Make it the greatest prime number less than the new 
  table size.  For example, if your previous table size was M = 83 and dhval = 79, you would need 
  to resize after inserting the 63rd item into the table.  At that time you would:
	1)    allocate a new table of size M = 167 and a new dhval = 163
	2)    hash all of the old data into the new table (be sure to take the new size and dhval into account when hashing)
	3)    assign your table variable to your new table (the old array will be garbage collected).
  To efficiently test for primality, see the isProbablePrime() method of the BigInteger class.  
  For consistency, in your default constructor, initialize your table size, M = 19 and your double 
  hash mod value, dhval = 17.
  
*/
public class HashDict implements searchTest {
	
	 String[] htable; 
	 int N = 0; 			//number of items in the table 
	 int M = 0;		//table size 
	 int dhval = 0;	//double hash mod value
	 int primescount = 0;
	 int[] primes = {37,41,79,83,163,167,331,337,673,677,1327,1361,2719,2729,5449,5471,
			 				10939,10949,21893,21911,43801,43853,87701,87719,175433,175447,350891,350899,
			 				701791,701819,1403627,1403641,2807239,2807303,5614597,5614657,11229301,11229331};
  
	public HashDict(){
		//initialize your table size, M = 19 and your double hash mod value, dhval = 17
	  	M = 19;
	  	dhval =17;
	  	htable = new String[M]; 
	}

	public void insert(String s) {
		if (LF()<0.75){			//insert items into table
	  		int hashcode = s.hashCode(); //1st hashcode for A array
	  		int hcode = hashcode & 0x7FFFFFFF;  // Make hash code positive
	  		int index = hcode % M;	// Get the value mod M
	  		int h2x =  (hcode % dhval) + 1; //second hash value
	  		
	  		while (htable[index]!=null){
	  			index+=h2x;		//add the step
	  			index%=M;		//wrap around
	  		}
	  		htable[index]=s;
			//System.out.println("htable["+index+"]="+htable[index]);	  		
	  		N++;   			//increment number of items in table

			//System.out.println("N="+N);	
		}
	  	else {				//rehash table
	  		htable=rehash();
	  		for (int y=0; y<htable.length; y++){
	  			//System.out.println("htable["+y+"]="+htable[y]);	  
	  		}
	  	}
		
	}
	
	
	public boolean find(String s) {
		int hashcode = s.hashCode(); //1st hashcode for A array
  		int hcode = hashcode & 0x7FFFFFFF;  // Make hash code positive
  		int index = hcode % M;	// Get the value mod M
  		int h2x =  (hcode % dhval) + 1; //second hash value
  		
  		while (htable[index]!=null){
  			if ((htable[index]).equals(s)){
  				return true;
  			}
  			index+=h2x;		//add the step
  			index%=M;		//wrap around
  		}
		return false;
	}
	
	public float LF() {	//at 75% full
		float loadfactor = ((float)N)/((float)M);
		return loadfactor;
		
	}
	public String[] rehash(){
		int oldM;
		oldM=M;
  		M = primes[primescount+1];
  		dhval = primes[primescount]; //greatest prime number less than the table size
  		primescount++;
		String[] temphtable = new String[M]; 
		for (int i = 0; i < oldM; i++){  //go through every item in old hash table
			if (htable[i] != null){
				String item = htable[i];
				int hashcode = item.hashCode(); //1st hashcode for A array
		  		int hcode = hashcode & 0x7FFFFFFF;  // Make hash code positive
		  		int index = hcode % M;	// Get the value mod M
		  		int h2x =  (hcode % dhval) + 1; //second hash value
		  		
		  		while (temphtable[index]!=null){
		  			index+=h2x;		//add the step
		  			index%=M;		//wrap around
		  			
		  		}
		  		temphtable[index]=item;
		  		//System.out.println("temphtable["+index+"]="+temphtable[index]);	
            }
		}
		return temphtable;
	}

}
