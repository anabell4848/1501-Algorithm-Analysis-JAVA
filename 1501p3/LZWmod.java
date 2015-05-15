/*************************************************************************
 *  Hanyu Xiong
 *  CS 1501
 *  Project 3
 *
 *  Compilation:  javac LZW.java
 *  Execution:    java LZW - < input.txt   (compress)
 *  Execution:    java LZW + < input.txt   (expand)
 *  Dependencies: BinaryIn.java BinaryOut.java
 *************************************************************************/

public class LZWmod{
    private static final int R = 256;        // number of input chars
	static char option;
	static int maxbits=16;
	static double filesize;
	static double outputsize;		
    static double newratio;			//compressionratio=filesize/outputsize;
    static double oldratio;	
    
    
    public static void compress() { 
        int W = 9;         					// codeword width
        int L = (int) Math.pow(2, W);       // number of codewords = 2^W
        int checkm=0;
        
        String input = BinaryStdIn.readString();	//read entire file into a giant string
        TST<Integer> st = new TST<Integer>();
        
        //initialize TST trie symbol table with all 1-character strings
        for (int i = 0; i < R; i++){
            st.put("" + (char) i, i);
        }
        int code = R+1;  // R is codeword for EOF
      
        //write option as 2 bytes at beginning of dictionary
        String optionstr=""+option;
        BinaryStdOut.write(st.get(optionstr), W);
        outputsize=W;
        
        while (input.length() > 0) {
			String s = st.longestPrefixOf(input);  // Find max prefix match s.
			filesize+=(s.length()*2);
			BinaryStdOut.write(st.get(s), W);      // Print s's encoding.
			outputsize+=W;
			newratio=filesize/outputsize;
			//System.out.println("newratio="+filesize+"/"+outputsize);
			//System.out.println("filesize="+filesize+"\toutputsize="+outputsize+"\ts="+s+"\tcode="+code+"\tW="+W+"\tL="+L);
			int t = s.length();
			if (t < input.length() && code < L) {   // Add s to symbol table.
				st.put(input.substring(0, t + 1), code++);
				if (code==L){				//if there's no more codewords
					//System.out.println("REACHED L NEED BIGGER BITS");
					if (W<maxbits){			//will count up to maxbits
						W++;						//increment codeword width
						L = (int) Math.pow(2, W);	//increase number of codewords accordingly
					}
					else{
						if (option=='r'){
							//System.out.println("REACHED OPTION R");
							W=9;
							L = (int) Math.pow(2, W);
							st = new TST<Integer>();
							for (int i = 0; i < R; i++){
								st.put("" + (char) i, i);
							}
							code = R+1;  // R is codeword for EOF
						}
						else if (option=='m'){
							checkm=1;
							//System.out.println("REACHED OPTION M, oldratio=" +oldratio+"\tnewratio="+newratio);
							if ((oldratio/newratio)>1.1){
								W=9;
								L = (int) Math.pow(2, W);
								st = new TST<Integer>();
								for (int i = 0; i < R; i++){
									st.put("" + (char) i, i);
								}
								code = R+1;  // R is codeword for EOF
							}
							oldratio=newratio;
						}
						else {			//option = 'n'
							//System.out.println("Option should equal n...= "+ option);
						}
					}
				}
			}
			else if (checkm==1){
				//System.out.println("REACHED OPTION checkM, oldratio=" +oldratio+"\tnewratio="+newratio);
				if ((oldratio/newratio)>1.1){
					W=9;
					L = (int) Math.pow(2, W);
					st = new TST<Integer>();
					for (int i = 0; i < R; i++){
						st.put("" + (char) i, i);
					}
					code = R+1;  // R is codeword for EOF
				}
				oldratio=newratio;
			}
			input = input.substring(t);            // Scan past s in input.
        }
        BinaryStdOut.write(R, W);
        BinaryStdOut.close();
    } 
    
    public static void expand() {
        int W = 9;         					// codeword width
        int L = (int) Math.pow(2, W);       // number of codewords = 2^W
        int maxL=(int) Math.pow(2, maxbits);
        String[] st = new String[maxL];
		int mcheck=0;
        int i; 								// next available codeword value
        
        // initialize matching array with all 1-character strings
        for (i = 0; i < R; i++)
            st[i] = "" + (char) i;
        st[i++] = "";                        // (unused) lookahead for EOF
        
        //read first two bytes as option
        char option = BinaryStdIn.readChar(W);
		outputsize+=W;
        //System.out.println("OPTION = "+ option);
        
        //read in first 9 bytes of the code
        int codeword = BinaryStdIn.readInt(W);
        String val = st[codeword];
		outputsize+=W;
		//System.out.println("filesize="+filesize+"\toutputsize="+outputsize+"\tcodeword="+codeword+"\ts="+st[codeword]+"\ti="+ i+"\tW="+W+"\tL="+L+"\tval="+ val);
		
        while (true) {
            BinaryStdOut.write(val);
    		filesize+=(val.length()*2);
			newratio=filesize/outputsize;
			//System.out.println("newratio="+filesize+"/"+outputsize);
			//System.out.println("filesize="+filesize+"\toutputsize="+outputsize +"\tcodeword="+codeword+"\ts="+st[codeword]+"\ti="+ i+"\tW="+W+"\tL="+L+"\tval="+ val);
            codeword = BinaryStdIn.readInt(W);
    		outputsize+=W;
			
            if (codeword == R) 
            	break;
            String s = st[codeword];
            if (i == codeword) 
            	s = val + val.charAt(0);   // special case hack
            if (i < (L-2)) {
            	st[i++] = val + s.charAt(0);
            }
            else{
            	if (W<maxbits){		//will count up to 16max
	            	W++;						//increment codeword width
					L = (int) Math.pow(2, W);	//increase number of codewords accordingly
					st[i++] = val + s.charAt(0);
            	}
            	else{	 
					if (option=='r'){
						//System.out.println("REACHED OPTION R");
						W=9;
						L = (int) Math.pow(2, W);
						st = new String[maxL];
						for (i = 0; i < R; i++)
							st[i] = "" + (char) i;
						st[i++] = "";
						
						//read in first 9 bytes of the code
						codeword = BinaryStdIn.readInt(W);
						val = st[codeword];
						outputsize+=W;
		
						BinaryStdOut.write(val);
						filesize+=(val.length()*2);
						codeword = BinaryStdIn.readInt(W);
						outputsize+=W;
						//System.out.println("filesize="+filesize+"\toutputsize="+outputsize+"\tcodeword="+codeword+"\ts="+st[codeword]+"\ti="+ i+"\tW="+W+"\tL="+L+"\tval="+ val);
						st[i++] = val + s.charAt(0);
					}
					else if (option=='m'){
						//System.out.println("ratio="+oldratio/newratio+"\toldratio=" +oldratio+"\tnewratio="+newratio);
						if ((oldratio/newratio)>1.1){
							W=9;
							L = (int) Math.pow(2, W);
							st = new String[maxL];
							for (i = 0; i < R; i++)
								st[i] = "" + (char) i;
							st[i++] = "";
							
							//read in first 9 bytes of the code
							codeword = BinaryStdIn.readInt(W);
							val = st[codeword];
							outputsize+=W;
			
							BinaryStdOut.write(val);
							filesize+=(val.length()*2);
							
							codeword = BinaryStdIn.readInt(W);
							outputsize+=W;
							//System.out.println("filesize="+filesize+"\toutputsize="+outputsize+"\tcodeword="+codeword+"\ts="+st[codeword]+"\ti="+ i+"\tW="+W+"\tL="+L+"\tval="+ val);
							newratio=filesize/outputsize;
							st[i++] = val + s.charAt(0);
						}
						else{
							if (i!=L){
								st[i++] = val + s.charAt(0);
							}
						}
						if (mcheck!=0)
							oldratio=newratio;
						else
							//System.out.println("FIRST TIME, KEEP OLDRATIO AT 0");
						mcheck=1;
					}
					else {			//option = 'n'
						//System.out.println("Option should equal n...= "+ option);
						if (i!=L){
							st[i++] = val + s.charAt(0);
						}
					}
            	}
            }
            val = s;
        } 
        BinaryStdOut.close();
    }



    public static void main(String[] args) {  
        if      (args[0].equals("-")) {
        	if (args[1].equals("n")){
        		option='n';			//do nothing, compress normally					
        	}
        	else if (args[1].equals("r")){    
        		option='r';			//reset 
        	}
        	else if (args[1].equals("m")){
        		option='m';			//monitor
        	}
        	else{
        		throw new RuntimeException("Illegal command line argument");
        	}
        	compress();
        }
        else if (args[0].equals("+")) {
        	expand();
    	}
        else { 
        	throw new RuntimeException("Illegal command line argument");
        }
    }

}
