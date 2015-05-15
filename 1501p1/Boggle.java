// CS 1501
// HAX12
//Project 1 Boggle game


import java.io.*;
import java.util.*;
public class Boggle
{
	//Declarations
	DictInterface D;
	DictInterface boardDict;
	ArrayList<String> wordsInBoard = new ArrayList<String>(); 
	ArrayList<Integer> usedLetters = new ArrayList<Integer>();
	ArrayList<String> guessArray = new ArrayList<String>();
	String[] wildCard = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", 
			"k", "l", "m", "n", "o", "p", "q", "r", 
			"s", "t", "u", "v", "w", "x", "y", "z"};	

	int matchCounter=0;
		String AorB;
	
	public Boggle() throws IOException
	{ 
		do {
			System.out.print("Run it with MyDictionary (enter a) or DLB(enter b)?: ");
			Scanner inp = new Scanner(System.in);
			AorB = inp.nextLine();
		} while (!AorB.equals("a") && !AorB.equals("b"));
		
		if (AorB.equals("a")){
			D = new MyDictionary();
		}
		else{
			D = new DLB();
		}
		//	scan in the dictionary as D
		Scanner fileScan = new Scanner(new FileInputStream("dictionary.txt"));
		String st;
		while (fileScan.hasNext())
		{
			st = fileScan.nextLine();
			if (st.length()>2)
				D.add(st);
		}		
	}
	char[][] board;
	char[][] board2;
	public void readData(String fileName, int size) throws IOException
	{
		if (AorB.equals("a")){
			boardDict = new MyDictionary();
		}
		else{
			boardDict = new DLB();
		}
		board = new char[size][size];
		board2 = new char[size][size]; 
		//load data into 2 boggle boards 
		BufferedReader data = new BufferedReader( new FileReader(fileName));
		int i = -1, j = 0, inputCount =0 ;
		while (data.ready())  
		{
			String read = data.readLine(); 
			String line = read.toLowerCase();
			for (int k = 0; k < line.length(); k++){
			    if (inputCount % size == 0){
					i++;
					j=0;
				}
			    if (i<size) {
			    	board[i][j]=line.charAt(k);
			    }	
				else{
					board2[i-size][j]=line.charAt(k);
				}
				j++;
				inputCount++; 
			}
		}
	}
	public void solveBoggle(int x, int y, int size)
	{
		if (x==0 && y==0){
			for (int i=0;i<size;i++){
				for (int j=0;j<size;j++){
					System.out.print(board[i][j] + " "); }
				System.out.println(""); }
		}
		StringBuilder sb = new StringBuilder();	// creates empty builder, capacity 16
		solveBoggle(x, y, board, sb, size);
	}

	public void solveBoggle2(int x, int y, int size)
	{
		if (x==0 && y==0){
			for (int i=0;i<size;i++){
				for (int j=0;j<size;j++){
					System.out.print(board2[i][j]+ " "); }
				System.out.println(""); }
		}
		StringBuilder sb = new StringBuilder();	// creates empty builder, capacity 16
		solveBoggle(x, y, board2, sb, size);
	}
	
	public void solveBoggle(int x, int y, char[][] board, StringBuilder sb, int size)
	{
		if (x<0 || y<0 || x> (size-1) || y>(size-1)) { //if (at edge) return;
			//System.out.println("out of bounds");
			return; 
		}
		if (beenHere(x, y, size)) { //if(beenThere) return;
			//System.out.println("been there  ");
			return; 
		}
		usedLetters.add(x*size+y);//add position to usedLetters
		/*for (int i=0;i<usedLetters.size();i++){
			System.out.println(usedLetters.get(i));
		}*/
		
		if (board[x][y]=='*')
		{
			//System.out.println("*******");
			for (int i = 0; i < wildCard.length; i++)
			{
				/*for (int r=0;r<usedLetters.size();r++){
					System.out.println(usedLetters.get(r));
				}*/
				sb.append(wildCard[i]);
				int ans = D.searchPrefix(sb);   //searchPrefix(stringbuilder)
				//System.out.println(sb);
				if (ans==0){	//dead end
					//System.out.println("not found");
					sb.deleteCharAt(sb.length() - 1); //remove last letter of stringbuilder
					if (wildCard[i]=="z"){
						usedLetters.remove(usedLetters.size() - 1);
					}
				}
				else if (ans==1 || ans==3){	//1 prefix, continue search, 3 a word and prefix, add to arraylist of words Found
					if (ans==3){
						//System.out.println("a word and prefix");
						addToFound(sb);
					}
					if (ans==1){
						//System.out.println("a prefix");
					}
					solveBoggle(x, (y+1), board, sb, size);
					solveBoggle((x+1), (y+1), board, sb, size);
					solveBoggle((x+1), y, board, sb, size);
					solveBoggle((x+1), (y-1), board, sb, size);
					solveBoggle(x, (y-1), board, sb, size);
					solveBoggle((x-1), (y-1), board, sb, size);
					solveBoggle((x-1), y, board, sb, size);
					solveBoggle((x-1), (y+1), board, sb, size);
					sb.deleteCharAt(sb.length() - 1);
					if (wildCard[i]=="z"){
						usedLetters.remove(usedLetters.size() - 1);
					}
				}
				else {	//ans==2 a word, add to arraylist of words Found
					//System.out.println("a word");
					addToFound(sb);
					sb.deleteCharAt(sb.length() - 1);  //remove last letter of stringbuilder
				}	
			}
		}
		else {
			sb.append(board[x][y]); // adds character to string
			int ans = D.searchPrefix(sb);   //searchPrefix(stringbuilder)
			//System.out.println(sb);
			if (ans==0){	//dead end
				//System.out.println("not found");
				sb.deleteCharAt(sb.length() - 1); //remove last letter of stringbuilder
				usedLetters.remove(usedLetters.size() - 1);
				return;
			}
			else if (ans==1 || ans==3){	//1 prefix, continue search, 3 a word and prefix, add to arraylist of words Found
				if (ans==3){
					//System.out.println("a word and prefix");
					addToFound(sb);
				}
				if (ans==1){
					//System.out.println("a prefix");
				}
				solveBoggle(x, (y+1), board, sb, size);
				solveBoggle((x+1), (y+1), board, sb, size);
				solveBoggle((x+1), y, board, sb, size);
				solveBoggle((x+1), (y-1), board, sb, size);
				solveBoggle(x, (y-1), board, sb, size);
				solveBoggle((x-1), (y-1), board, sb, size);
				solveBoggle((x-1), y, board, sb, size);
				solveBoggle((x-1), (y+1), board, sb, size);
				sb.deleteCharAt(sb.length() - 1);
				
				usedLetters.remove(usedLetters.size() - 1);
			}
			else {	//ans==2 a word, add to arraylist of words Found
				//System.out.println("a word");
				addToFound(sb);
				sb.deleteCharAt(sb.length() - 1);  //remove last letter of stringbuilder
				
				usedLetters.remove(usedLetters.size() - 1);  //delete last usedLetters entry
				return;
			}	
		}
	}
	
	public boolean beenHere(int x, int y, int size)
	{
		int target = x*size+y;
		for (int i=0;i<usedLetters.size();i++){
			//System.out.println(usedLetters.get(i) + " "+ target);
			if (usedLetters.get(i)==target) return true;
		}
		return false;
	}
	
	public void addToFound(StringBuilder sb)
	{
		int ans = boardDict.searchPrefix(sb); 
		if (ans == 0) { //if not in boardDict and word more than 3 letters
			String found = sb+"";
			boardDict.add(found); //add stringbuilder to boardDict
			wordsInBoard.add(found); //add stringbuilder to arraylist for user display
		}
	}

	public void addToGuess(String guess)
	{
		if (!guessArray.contains(guess)){
			guessArray.add(guess);
			StringBuilder sbb = new StringBuilder(guess);
			int ans = boardDict.searchPrefix(sbb);   //searchPrefix(stringbuilder)
			if (ans==2 || ans==3){	//dead end
				matchCounter++;
				System.out.println("valid guess");
			}
			else {
				System.out.println("invalid guess");
			}
		}
		else {
			System.out.println("you already guessed this");
		}
	}
	int tot;
	public void percentageCorrect()
	{
		double ans=100*((double)matchCounter)/tot;
		System.out.println("You guessed "+ matchCounter +" correctly, and " + ans + "% correct!");
		matchCounter=0;
	}
	public void displayGuesses()
	{
		Collections.sort(guessArray);
		int i=1;
		System.out.println( "All the words you guessed: ");
		for (String temp : guessArray) {
			System.out.println(i+ ". " + temp );
			i++;
		}
		guessArray.clear();
	}
	public void displayWords()
	{
		Collections.sort(wordsInBoard);
		int i=1;
		System.out.println( "All the words on the board: ");
		for (String temp : wordsInBoard) {
			System.out.println(i+ ". " + temp );
			i++;
		}
		tot=wordsInBoard.size();
		wordsInBoard.clear();
	}
	public void clearUsed()
	{
		usedLetters.clear();
	}
	public void clearwordsInBoard()
	{
		wordsInBoard.clear();
	}
}


