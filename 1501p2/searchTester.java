import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.StringTokenizer;


public class searchTester {
	
	public static void main(String[] args) throws IOException
	{
		//Create an array of searchTest of size three
		searchTest testArray[] = new searchTest[4];
		testArray[0] = new SeqDict();
		testArray[1] = new SortDict();
		testArray[2] = new HashDict();
		testArray[3] = new DLBsearch();
		
		
		//BufferedReader Dfile = new BufferedReader( new FileReader("C:\\Users\\anabe_000\\Desktop\\1501p2\\words1.txt") ); 
		//BufferedReader Tfile = new BufferedReader( new FileReader("C:\\Users\\anabe_000\\Desktop\\1501p2\\large.txt") );
		//BufferedReader Tfile2 = new BufferedReader( new FileReader("C:\\Users\\anabe_000\\Desktop\\1501p2\\large.txt") );
		//BufferedReader Tfile3 = new BufferedReader( new FileReader("C:\\Users\\anabe_000\\Desktop\\1501p2\\large.txt") );
		//BufferedReader Tfile4 = new BufferedReader( new FileReader("C:\\Users\\anabe_000\\Desktop\\1501p2\\large.txt") );
		BufferedReader Dfile = new BufferedReader( new FileReader(args[0]) ); 
		BufferedReader Tfile = new BufferedReader( new FileReader(args[1]) );  
		BufferedReader Tfile2 = new BufferedReader( new FileReader(args[1]) );  
		BufferedReader Tfile3 = new BufferedReader( new FileReader(args[1]) );  
		BufferedReader Tfile4 = new BufferedReader( new FileReader(args[1]) );  
		BufferedReader Tfile5 = new BufferedReader( new FileReader(args[1]) );  
		
		//insert dictionary words into 3 types
		while ( Dfile.ready() )  {
			String line = Dfile.readLine();
			testArray[0].insert(line);
			testArray[1].insert(line);
			testArray[2].insert(line);
			testArray[3].insert(line);
		}
		Dfile.close();
		
		int correct0=0,correct1=0,correct2=0, correct3=0, wrong0=0, wrong1=0, wrong2=0, wrong3=0, total=0;
		
		//read the file and parse to get filetime
		long startTime = System.nanoTime();  
		while ( Tfile.ready() )  {
			String line = Tfile.readLine();
			StringTokenizer st = new StringTokenizer(line," \t\n\r\f.,!?\";:()[]/+");
		}
		Tfile.close();
		long endTime = System.nanoTime();  
		long fileTime=endTime-startTime;
		//System.out.println("FIRST");
		//read file and time for SeqDict
		long startTime2 = System.nanoTime();  
		while ( Tfile2.ready() )  {
			String line = Tfile2.readLine();
			StringTokenizer st = new StringTokenizer(line," \t\n\r\f.,!?\";:()[]/+");
	        while(st.hasMoreTokens()){
	            String word = st.nextToken();
	        	//System.out.println(word);
	        	if(testArray[0].find(word)==true){
	        		correct0++;
				}
	        	else{
	        		wrong0++;
	        	}
	        	total++;
	        } 
		}
		Tfile2.close();
		long endTime2 = System.nanoTime(); 
		//System.out.println("SECOND");
		//read file and time for SortDict
		long startTime3 = System.nanoTime();  
		while ( Tfile3.ready() )  {
			String line = Tfile3.readLine();
			StringTokenizer st = new StringTokenizer(line," \t\n\r\f.,!?\";:()[]/+");
	        while(st.hasMoreTokens()){
	            String word = st.nextToken();
	        	//System.out.println(word);
	        	
	        	if(testArray[1].find(word)==true){
	        		correct1++;
				}
	        	else{
	        		wrong1++;
	        	}
	        } 
		}
		Tfile3.close();
		long endTime3 = System.nanoTime(); 
		//System.out.println("THIRD");
		//read file and time for HashDict
		long startTime4 = System.nanoTime();  
		while ( Tfile4.ready() )  {
			String line = Tfile4.readLine();
			StringTokenizer st = new StringTokenizer(line," \t\n\r\f.,!?\";:()[]/+");
	        while(st.hasMoreTokens()){
	            String word = st.nextToken();
	        	//System.out.println(word);
	        	
	        	if(testArray[2].find(word)==true){
	        		correct2++;
				}
	        	else{
	        		wrong2++;
	        	}
	        } 
		}
		Tfile4.close();
		long endTime4 = System.nanoTime(); 
		//System.out.println("FOURTH");
		//read file and time for HashDict
		long startTime5 = System.nanoTime();  
		while ( Tfile5.ready() )  {
			String line = Tfile5.readLine();
			StringTokenizer st = new StringTokenizer(line," \t\n\r\f.,!?\";:()[]/+");
	        while(st.hasMoreTokens()){
	            String word = st.nextToken();
	        	//System.out.println(word);
	        	
	        	if(testArray[3].find(word)==true){
	        		correct3++;
				}
	        	else{
	        		wrong3++;
	        	}
	        } 
		}
		Tfile5.close();
		long endTime5 = System.nanoTime(); 
		//System.out.println("FIFTH");
		//calculate searchTime for each Dictionary
		long searchTime1 = endTime2-startTime2-fileTime; 
		long searchTime2 = endTime3-startTime3-fileTime; 
		long searchTime3 = endTime4-startTime4-fileTime; 
		long searchTime4 = endTime5-startTime5-fileTime; 
		
		//convert to seconds
		long secs1 = searchTime1/1000;
		double avgTime1= secs1/((double)total);
		long secs2 = searchTime2/1000;
		double avgTime2= secs2/((double)total);
		long secs3 = searchTime3/1000;
		double avgTime3= secs3/((double)total);
		long secs4 = searchTime4/1000;
		double avgTime4= secs4/((double)total);
		//System.out.println(fileTime+"    "+searchTime1+"   "+secs1+ "    "+ avgTime1);
		//System.out.println(fileTime+"    "+searchTime2+"   "+secs2+ "    "+ avgTime2);
		//System.out.println(fileTime+"    "+searchTime3+"   "+secs3+ "    "+ avgTime3);
		
		//output to text file
		PrintWriter writer = new PrintWriter("1501p2output.txt");
		
		writer.println("Dictionary:" +args[0]+" and Test file: "+args[1]);
		
		writer.println("Dictionary 0 (unsorted array)");
		writer.println("\tTotal words checked: "+total);
		writer.println("\tNumber of words found: "+correct0);
		writer.println("\tNumber of words not found: "+wrong0);
		writer.println("\tTotal time required for the searches: "+ secs1 + " microseconds");
		writer.println("\tAverage time required per search: "+avgTime1+" microseconds");

		writer.println("Dictionary 1 (sorted array)");
		writer.println("\tTotal words checked: "+total);
		writer.println("\tNumber of words found: "+correct1);
		writer.println("\tNumber of words not found: "+wrong1);
		writer.println("\tTotal time required for the searches: "+ secs2 + " microseconds");
		writer.println("\tAverage time required per search: "+avgTime2+" microseconds");

		writer.println("Dictionary 2 (hash table)");
		writer.println("\tTotal words checked: "+total);
		writer.println("\tNumber of words found: "+correct2);
		writer.println("\tNumber of words not found: "+wrong2);
		writer.println("\tTotal time required for the searches: "+ secs3 + " microseconds");
		writer.println("\tAverage time required per search: "+avgTime3+" microseconds"); 
		
		writer.println("Dictionary 3 (DLB)");
		writer.println("\tTotal words checked: "+total);
		writer.println("\tNumber of words found: "+correct3);
		writer.println("\tNumber of words not found: "+wrong3);
		writer.println("\tTotal time required for the searches: "+ secs4 + " microseconds");
		writer.println("\tAverage time required per search: "+avgTime4+" microseconds"); 
		writer.close();
	}

}
