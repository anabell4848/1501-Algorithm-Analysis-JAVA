// CS 1501
// HAX12
//Project 1 Boggle game

import java.io.IOException;
import java.util.Scanner;


public class BoggleTester {

	public static void main(String[] args) throws IOException
	{		
	
		Boggle boggle = new Boggle();
		int boardCount = 0;
		String continueGuess="1";
		int nextBoard=1;
		while (nextBoard==1){
			//solveBoggle starting with board 1
			int size = 4; 
			if (boardCount<2){
				size =5;
				boggle.readData("data5.txt", size);
				System.out.println("5x5 board:");
			}
			else if (boardCount<4){
				size =6;
				boggle.readData("data6.txt", size);
				System.out.println("6x6 board:");
			}
			else if (boardCount<6){
				boggle.readData("data1.txt", size);
				System.out.println("4x4 board:");
			}	
			else if (boardCount<8){
				boggle.readData("data2.txt", size);
				System.out.println("4x4 board:");
			}
			else if (boardCount<10){
				boggle.readData("data3.txt", size);
				System.out.println("4x4 board:");
			}
			for (int i=0;i<size;i++){
				for (int j=0;j<size;j++){
					if (boardCount%2==0){
						boggle.solveBoggle(i, j, size); 
						//System.out.println(""); 
						boggle.clearUsed();
					}
					else if(boardCount%2==1){
						boggle.solveBoggle2(i, j, size); 
						//System.out.println(""); 
						boggle.clearUsed();
					}
				}
			}
			boardCount++;
			while(!continueGuess.equals("0")){
				System.out.print("Enter your guess (enter 0 to end the game): ");
				Scanner input = new Scanner(System.in);
				continueGuess = input.nextLine();
				
				if (continueGuess.equals("0")){
					boggle.displayGuesses();
					boggle.displayWords();
					boggle.percentageCorrect();
				}
				else{
					boggle.addToGuess(continueGuess);
				}
				
			}
			continueGuess="1";
			do {
				System.out.print("Do you want to play next game? (1 for yes, 0 for no): ");
				Scanner in = new Scanner(System.in);
				nextBoard = in.nextInt();
			} while (nextBoard!=1 && nextBoard!=0 );
			if (boardCount==10){
				nextBoard =0;
			}
		}
		
		System.out.println("Done!" );
	
	}
}
	