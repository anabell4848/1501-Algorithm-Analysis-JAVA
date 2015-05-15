import java.util.ArrayList;
import java.util.Collections;

// CS 1501
// HAX12
//Project 2


public class DLBsearch implements searchTest  {
	
	private DLBnode root;

    private class DLBnode {
    	private char val;
    	private DLBnode rightSib;
    	private DLBnode child;
    	
    	private DLBnode(){ }
    		
    	private DLBnode(char value){
			val = value;
			rightSib=null;
			child = null;
		} 
    }
    
 	public DLBsearch()
 	{
 		root = null;
 	}

 	
 	//add all the characters to the dictionary trie
 	public void insert(String s) throws NullPointerException
 	{
 		DLBnode tracker=new DLBnode();
		tracker = root; //keep track of the node we are in for the word
		boolean rooted=false;
		for(int i = 0; i < s.length(); i++)
		{
			//System.out.println(s.charAt(i));
			DLBnode letter = new DLBnode(s.charAt(i));
			if(root == null){  				//write root
				//System.out.println("root= "+letter.val);
				root=letter;
				tracker = root;
				rooted = true;
			}
			else if (rooted){ 		//write new letters,tracker.child == null
					tracker.child = letter; //make child at tracker
					tracker = tracker.child;   
					//System.out.println("1tracker=null"+" child = "+letter.val);
			}
			else { 			//if tracker exists
				if (tracker.val ==s.charAt(i)){
					//System.out.println("2trace down from " + tracker.val+ " to tracker.child "+tracker.child.val);
					tracker = tracker.child;		//trace down
				}
				else {  	//if tracker != the letter
					if (tracker.val=='/'){       //tracker = /
						if (tracker.child==null){       //"/" child DNE, make child
							tracker.child=letter;
							//System.out.println("3tracker=/ child = " +tracker.child.val);
							tracker=tracker.child;
							rooted=true;
						}
						else if (tracker.child.val ==s.charAt(i)){    //"/" child =letter, trace
							//System.out.println("4trace down down from /"+ " to tracker.child.child "+tracker.child.child.val);
							tracker=tracker.child.child;
						}
						else if (tracker.child.rightSib ==null){
							tracker.child.rightSib=letter;
							//System.out.println("5tracker=/ sib = " +tracker.child.rightSib.val);
							tracker=tracker.child.rightSib;
							rooted=true;
						}
						else if (searchRight(tracker.child, s.charAt(i)).val==s.charAt(i)){   //"/"child !=letter, match sibs, trace to matching sib
							tracker = searchRight(tracker.child, s.charAt(i));
							//System.out.println("6trace down right down from /"+ " to tracker.child "+tracker.child.val);
							tracker=tracker.child;
						}
						else {     //no sib match, make new sib
							//System.out.println(tracker.child.val+" "+ s.charAt(i)+ " "+ searchRight(tracker.child, s.charAt(i)).val);
							tracker = searchRight(tracker.child, s.charAt(i));
							tracker.rightSib=letter;
							//System.out.println("7tracker=/ to  searchright= "+ tracker.val +"tracker.rightSib "+tracker.rightSib.val);
							tracker=tracker.rightSib;
							rooted=true;
						}
						
					}
					else if(tracker.rightSib==null){   	//tracker !=letter, make right sib
						tracker.rightSib = letter;
						//System.out.println("8tracker!=letter"+" sib = "+tracker.rightSib.val);
						tracker = tracker.rightSib;
						rooted=true;
					}
					else {     //if (tracker.rightSib!=null) 
						if (searchRight(tracker, s.charAt(i)).val==s.charAt(i)){  //tracker!=letter, find right sib match\
							tracker = searchRight(tracker, s.charAt(i));
							//System.out.println("9trace down right down from /"+ " to tracker.child "+tracker.child.val);
							tracker=tracker.child;								
						} 
						else {   //no rightsib match, make another rightsib 
							tracker = searchRight(tracker, s.charAt(i));
							tracker.rightSib=letter;
							//System.out.println("10tracker=/ to  rs= "+ letter +"tracker.rightSib "+tracker.rightSib.val);
							tracker=tracker.rightSib;
							rooted=true;
						}
					}
				}
			}
		}
		DLBnode endWordNode = new DLBnode();
		endWordNode.val = '/';
		if (rooted){
			tracker.child = endWordNode;
			//System.out.println(".child Added /");
		}
		else {
			tracker = endWordNode;
			//System.out.println("tracker Added /");
		}
		
		//output debugging
		/*System.out.println("");
		System.out.print(root.val+" ");
		DLBnode newnode = root;
		while(newnode.child !=null || newnode.rightSib !=null){
			if (newnode.rightSib !=null){
				newnode=newnode.rightSib;
				System.out.print("RS"+newnode.val+" "); }
			else {
				newnode=newnode.child;
				System.out.print("Ch"+newnode.val+" "); }
		}
		System.out.println(""); */
 	}
 	public DLBnode searchRight(DLBnode tracker, char s)
 	{
 		while (tracker.val!=s){
 			if (tracker.rightSib!=null){
 				tracker=tracker.rightSib;
 			}
 			else {
 				break; //returns tracker to add on new rs
 			}
 		}
		return tracker; //returns tracker = s
 	}

 	public boolean find(String s)
 	{
		boolean prefix=false;
		boolean word=false;
 		DLBnode tracker=new DLBnode();
 		if (root==null){
			return false;
		}
 		tracker = root;
 		//System.out.println("");
		//System.out.println(s);
 		for (int j = 0; j < s.length(); j++)
		{
			DLBnode letter = new DLBnode(s.charAt(j));  
			//System.out.println("j="+ j+" tracker="+tracker.val + "  s.charAt(j)=" + s.charAt(j));
			if (tracker.val==s.charAt(j)) { //trace down
 		 		tracker = tracker.child;
 				//System.out.println("tracker=letter, trace down to "+ tracker.val);
 				
 				if (j==s.length()-1){
 					if (tracker.val == '/'){
						word = true;
						//System.out.println("word1");
					}
					prefix = true;
					//System.out.println("prefix1");
		 		}
 		 	}
 		 	else {         //if letter !=tracker
 		 		if (tracker.val == '/') {   //word or prefix
 		 			//System.out.println(j+ " " + (s.length()-1)+ " "+ tracker.child.val + " "+ s.charAt(j)+ " ");
 		 			if ((j==s.length()-1)){
 		 				//System.out.println("IN HERE");
 		 				if (tracker.child!=null){
 		 					//System.out.println("tracker.child= "+ tracker.child.val);
	 		 				if (tracker.child.val== s.charAt(j)){
		 		 				prefix = true;
		 		 				//System.out.println("prefix solution");
	 		 				}
 		 				}
 			 		}
 		 			if (tracker.child != null){
 		 				if (tracker.child == letter){
 		 					tracker=tracker.child.child;   //trace down skipping /
 		 					//System.out.println("tracker.child =letter, trace down down to "+ tracker.child.child.val);
 		 				}
 		 				else {        //trace right until found
 		 					if (searchRight(tracker.child, s.charAt(j)).val==letter.val){
 			 		 			tracker = searchRight(tracker.child, s.charAt(j));
 			 		 		//	System.out.println("1tracker!=/ .someRS =letter, trace right to "+ tracker.val+ "pointer to child"+ tracker.child.val);
 			 		 			tracker = tracker.child;
 			 		 			if (j==s.length()-1){
 			 		 				if (tracker.val == '/'){
 			 							word = true;
 			 						//	System.out.println("word2");
 			 						}
 			 		 				prefix = true;
 			 		 				//System.out.println("prefix2");
 			 		 			}
 		 		 			}
 		 					else {
 		 						break;
 		 					}
 		 				}
 		 			}
 		 		}
 		 		else {	//trace right until found
 		 			boolean truf = searchRight(tracker, s.charAt(j)).val==letter.val;
 		 			//System.out.println("tracker!=/ "+ truf+ " that searchright="+ searchRight(tracker, s.charAt(j)).val + " letter= "+ letter.val);
 		 			if (searchRight(tracker, s.charAt(j)).val==letter.val){
	 		 			tracker = searchRight(tracker, s.charAt(j));
	 		 			//System.out.println("2tracker!=/ .someRS =letter, trace right to "+ tracker.val+ " pointer to .child "+ tracker.child.val);
	 		 			tracker = tracker.child;
	 		 			if (j==s.length()-1){
	 	 					if (tracker.val == '/'){
	 							word = true;
	 							//System.out.println("word3");
	 						}
	 						prefix = true;
	 						//System.out.println("prefix3");
	 			 		}
 		 			}
 		 			else {
 		 				break;
 		 			}
 		 		}
 		 	}
 		 }
 		if (prefix && word) {
 			//System.out.println("prefix&word 3"); 
 			return true;
 		}
		else if (word) {
			//System.out.println("word 2"); 
			return true; 
		}
		else if (prefix) {
			//System.out.println("prefix 1"); 
			return false;
		}
		else{
			//System.out.println("nada 0"); 
			return false;
		}
 	}

 	public int searchPrefix(StringBuilder s, int start, int end)
 	{
 		return 0;
 	}

}
