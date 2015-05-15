/*
Hanyu Xiong
CS 1501
Project 5

2) If the route graph is not connected, query should identify and show each connected subtrees of graph. 

All routes bidirectional, undirected graph.Adjacency list. The cities have string name and any other information. The edges will have 
multiple values (distance, price), can be implemented as a single list of edges with two values 
each, or as two separate lists of edges.  
 	- MST: Prim’s or Kruskal’s algorithm. 
	- Shortest distance and shortest price paths: Dijkstra’s algorithm. 
	- Shortest hops path: breadth-first search.
	- DFS
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;


public class GraphTester {
	public static void main(String[] args) throws IOException{
		
		//read a user inputed file 
		Scanner input = new Scanner(System.in);
		System.out.print("INPUT FILE: ");
		String fileName = input.nextLine();
		//BufferedReader file = new BufferedReader( new FileReader("C:\\Users\\anabe_000\\OneDrive\\4 Senior Yr\\1. CS 1501\\1501p5\\a5data1.txt") );
		BufferedReader file = new BufferedReader( new FileReader(fileName));
		
		//write file
		PrintWriter writer = new PrintWriter("outputFile.txt", "UTF-8");
		
		//declare variables
		String[] cities = null; 
		int cityctr=0;
		EdgeWeightedGraph G = null;
		
		int filelinectr=0;
		while (file.ready())  {
			String line = file.readLine(); 
			//System.out.println(line + "     "+ filelinectr + " "+ cityctr);
			if (filelinectr==0){	//first line has an int that tells how many cities
				cityctr=Integer.parseInt(line);
				cities=new String[cityctr];		//create cities array of size cityctr
				G = new EdgeWeightedGraph(cityctr);	//create graph with cityctr vertices
				writer.println(line);
			}
			else if(filelinectr>0 && filelinectr<=cityctr){	//add all the cities into an array
				cities[filelinectr-1]=line;
				writer.println(line);
			}
			else {	//read in the distances and prices between the cities
				//System.out.println(line + "     "+ filelinectr + " "+ cityctr);
				String theline[] = line.split(" ");
				int v = Integer.parseInt(theline[0]);
				v=v-1;
				int w = Integer.parseInt(theline[1]);
				w=w-1;
				int distance = Integer.parseInt(theline[2]);
				Double price = Double.parseDouble(theline[3]);
	            Edge e = new Edge(v, w, distance, price);		
	            G.addEdge(e); 							//add edges to graph
				
			}
			filelinectr++;
		}
		file.close();
		//System.out.println("-----------------------");
        
		//Menu of options for user to choose
		int userChoice, quit=0;
		do {
			System.out.print(
				"\nOptions (pick a number): \n"
				+ "1: Entire list of direct routes, distances and prices\n"
				+ "2: Miminum Spanning Tree based on distances\n"
				+ "3: Three shortest path searches\n"
				+ "4: All paths costing a certain price\n"
				+ "5: Add a new route to the schedule\n"
				+ "6: Remove a route from the schedule\n"
				+ "7: Shortest route from a source to a destination through a third city\n"
				+ "8: Cheapest route from a source to a destination through a third city\n"
				+ "9: Remove a city/vertex (removes assosiated edges) \n"
				+ "10: Add a city/vertex (does not add any edges) \n"
				+ "   Type anything else to quit the program\n"
				+ "Your choice is: ");
			userChoice = input.nextInt();
			System.out.println("");
			
			if (userChoice==1){			//output data
				for (Edge e : G.edges()){
					System.out.println(cities[e.either()] + " "+ cities[e.other(e.either())]+ " "+e.distance()+" "+e.price());
				}
			}
			else if (userChoice==2){	//MST: Prim’s or Kruskal’s algorithm. 
				System.out.println("MINIMUM SPANNING TREE\n------------------------\nThe edges in the MST based on distance follow:");
				//if (G.V() <= 10) System.out.println(G);
				CC cc = new CC(G);
				//System.out.println("Number of connected components = " + cc.count());
		        String Connected[] = new String[cc.count()];
		        for (int w = 0; w < cc.count(); w++) {
		        	Connected[w]="";
		        }
		        for (int v = 0; v < G.V(); v++) {
			        for (int w = 0; w < cc.count(); w++) {
			        	if(w==cc.id(v)){
				            //System.out.println("Connected["+w+"]"+"="+Connected[w] + " += " + v);
				        	Connected[w]=Connected[w]+""+v;
			        	}
			        }
		        }
		        int tempcount=0, temptempcount=0;
				LazyPrimMST mst = new LazyPrimMST(G);
		        for (Edge e : mst.edges()) {
		        	//System.out.println(e);
		        	System.out.println(cities[e.either()] + ","+ cities[e.other(e.either())]+ " : "+e.distance());
		        	tempcount++;
			        if (tempcount==(Connected[temptempcount].length()-1)){
			        	temptempcount++;
			        	tempcount=0;
			        	System.out.println();
			        	//System.out.println(Connected[temptempcount].length());
			        }	
		        }
				/*for (int w = 0; w < cc.count(); w++) {
		        	//System.out.println(Connected[w]);
		        	String[] ConnectedC = Connected[w].split("");
		        	for (int i = 1; i < ConnectedC.length; i++){
		        		int temp=Integer.parseInt(ConnectedC[i])+1;
		        		System.out.println(ConnectedC.length+".   "+cities[Integer.parseInt(ConnectedC[i])]);
		        	}
		        	System.out.println();
		        }*/
			}
			else if (userChoice==3){	//shortest cost, shortest distance, fewest hops
				String idk = input.nextLine();
				System.out.print("");
				
				System.out.print("Source city(case sensitive): ");
				String v1str=input.nextLine();
				int v1 = Arrays.asList(cities).indexOf(v1str);
				//System.out.print(v1);
				System.out.print("Destination city(case sensitive): ");
				String v2str=input.nextLine();
				int v2= Arrays.asList(cities).indexOf(v2str);
				//System.out.print(v2);
				System.out.println("");
				
				if (v1==-1 || v2==-1){
					System.out.println("You've entered invalid city names");
				}
				else{
					//Shortest price paths: Dijkstra’s algorithm
					System.out.println("SHORTEST COST PATH from " + v1str + " to "+ v2str +"\n-----------------------");
					DijkstraSP sp = new DijkstraSP(G, v1);
					if (sp.hasPathTo(v2)) {
	                	System.out.println("Shortest cost from " + v1str + " to "+ v2str + " is " +sp.distTo(v2)
            					+ "\nPath with edges (in reverse order):");
	                	Edge e2=null;
	                	int nexte = 0, i = 0;
	                    for (Edge e : sp.pathTo(v2)) {
	                    	if (e!=e2){
	                    		if(i==0){
	                    			if (e.either()==v2){
	                    				System.out.print(cities[e.either()] +" "+e.price()+ " "+cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				System.out.print(cities[e.other(e.either())] +" "+e.price()+ " "+cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
		                    	}
	                    		else {
	                    			if (e.either()==nexte){
	                    				//if (e.other(e.either())==v1) {System.out.print("HERE1");break;}
	                    				System.out.print(e.price()+" "+ cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				//if (e.either()==v1) {System.out.print("HERE2");break;}
	                    				System.out.print(e.price()+" "+ cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
	                    		}
		                    	e2=e;
	                    	}
	                    	else{
	                    		//System.out.print("  deleted*"+ e+"* ");
	                    	}
	                    	i=1;
	                    }
		            }
		            else {
		                System.out.println(v1str + " to "+ v2str  +" not connected");
		            }
		            System.out.println("\n");
					
					//Shortest distance paths: Dijkstra’s algorithm
					System.out.println("SHORTEST DISTANCE PATH from " + v1str + " to "+ v2str 
									+ "\n---------------------------------------");
					DijkstraSP2 sp2 = new DijkstraSP2(G, v1);
					if (sp2.hasPathTo(v2)) {
	                	int dist=(int)sp2.distTo(v2);
	                	System.out.println("Shortest distance from " + v1str + " to "+ v2str + " is " +dist
            					+ "\nPath with edges (in reverse order):");
	                	Edge e2=null;
	                	int nexte = 0, i = 0;
	                    for (Edge e : sp2.pathTo(v2)) {
	                    	if (e!=e2){
	                    		if(i==0){
	                    			if (e.either()==v2){
	                    				System.out.print(cities[e.either()] +" "+e.distance()+ " "+cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				System.out.print(cities[e.other(e.either())] +" "+e.distance()+ " "+cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
		                    	}
	                    		else {
	                    			if (e.either()==nexte){
	                    				//if (e.other(e.either())==v1) {System.out.print("HERE1");break;}
	                    				System.out.print(e.distance()+" "+ cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				//if (e.either()==v1) {System.out.print("HERE2");break;}
	                    				System.out.print(e.distance()+" "+ cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
	                    		}
		                    	e2=e;
	                    	}
	                    	else{
	                    		//System.out.print("  deleted*"+ e+"* ");
	                    	}
	                    	i=1;
	                    }
		            }
		            System.out.println("\n");
					
					//Shortest hops path: breadth-first search
			        System.out.println("FEWEST HOPS from " + v1str + " to "+ v2str 
			        					+"\n-----------------------");
			        
		        	BreadthFirstPaths bfs = new BreadthFirstPaths(G, v1);
		            if (bfs.hasPathTo(v2)) {
		            	System.out.println("Fewest hops from " + v1str + " to "+ v2str + " is " +bfs.distTo(v2)
		            					+ "\nPath (in reverse order):");
		                for (int x : bfs.pathTo(v2)) {
		                    if (x != v1) 
		                    	System.out.print(cities[x]+ " ");
		                }
		                System.out.print(v1str);
		            }
		            else {
		            	System.out.println(v1str + " to "+ v2str  +" not connected");
		            }
		            System.out.println("");
				}
		    }
			else if (userChoice==4){	//cost less than
				System.out.print("Maximum price: ");
				double cost = input.nextDouble();
				System.out.println("ALL PATHS OF COST " + cost + " OR LESS"
						+ "\nNote that paths are duplicated, once from each end city's point of view"
						+ "\n-----------------------------------------------------------------------"
						+ "\nList of paths at most " +cost +" in length:");
				for (int i=0; i < G.V();i++){
		            StringBuilder sb = new StringBuilder();
		            sb.setLength(0);
			    	sb.append(i+" ");
		            StringBuilder sb2 = new StringBuilder();
		            sb2.setLength(0);
			    	double trackprice = 0;
			        DepthFirstPaths dfs = new DepthFirstPaths(G, i, sb, sb2, trackprice, cost);
			        //System.out.println();
			        if (i==G.V()-1){
				        dfs.output(cost, cities);
		        	}
		        }
				
			}
			else if (userChoice==5){	//add or remove route
				String idk = input.nextLine();
				System.out.print("");
				
				System.out.print("Source city(case sensitive): ");
				String v1str=input.nextLine();
				int v1 = Arrays.asList(cities).indexOf(v1str);
				//System.out.print(v1);
				System.out.print("Destination city(case sensitive): ");
				String v2str=input.nextLine();
				int v2= Arrays.asList(cities).indexOf(v2str);
				
				System.out.print("Distance: ");
				int distance = input.nextInt();
				System.out.print("Price: ");
				double price = input.nextDouble();
				System.out.println("");
				
				if (v1==-1 || v2==-1){
					System.out.println("You've entered invalid city names. ");
				}
				else{
		           	//System.out.println(v1 + " " + v2 + " " + distance + " " +price);
		            Edge e = new Edge(v1, v2, distance, price);		
		            G.addEdge(e); 							//add edges to graph
		            System.out.println("Done. ");
				}
			}
			else if (userChoice==6){ 	//remove route
				String idk = input.nextLine();
				System.out.print("");
				
				System.out.print("Source city(case sensitive): ");
				String v1str=input.nextLine();
				int v1 = Arrays.asList(cities).indexOf(v1str);
				//System.out.print(v1);
				System.out.print("Destination city(case sensitive): ");
				String v2str=input.nextLine();
				int v2= Arrays.asList(cities).indexOf(v2str);
				//System.out.print(v2);
				System.out.println("");
				
				if (v1==-1 || v2==-1){
					System.out.println("You've entered invalid city names. ");
				}
				else{
					G.removeEdge(v1, v2);
					System.out.println("Done. ");
				}
			}
			else if (userChoice==7){ 
				String idk = input.nextLine();
				System.out.print("");
				
				System.out.print("Source city(case sensitive): ");
				String v1str=input.nextLine();
				int v1 = Arrays.asList(cities).indexOf(v1str);
				//System.out.print(v1);
				System.out.print("Middle city(case sensitive): ");
				String v3str=input.nextLine();
				int v3 = Arrays.asList(cities).indexOf(v3str);
				//System.out.print(v3);
				System.out.print("Destination city(case sensitive): ");
				String v2str=input.nextLine();
				int v2= Arrays.asList(cities).indexOf(v2str);
				//System.out.print(v2);
				System.out.println("");
				
				if (v1==-1 || v2==-1 || v3==-1){
					System.out.println("You've entered invalid city names");
				}
				else{
					//Shortest price paths: Dijkstra’s algorithm
					System.out.println("SHORTEST DISTANCE PATH from " + v1str + " to "+ v2str +" through "+v3str+" (in reverse order)\n------------------------------");
					DijkstraSP2 sp = new DijkstraSP2(G, v3);
					int dist1=0;
					if (sp.hasPathTo(v2)) {
	                	dist1=(int)sp.distTo(v2);
	                	Edge e2=null;
	                	int nexte = 0, i = 0;
	                    for (Edge e : sp.pathTo(v2)) {
	                    	if (e!=e2){
	                    		if(i==0){
	                    			if (e.either()==v2){
	                    				System.out.print(cities[e.either()] +" "+e.distance()+ " "+cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				System.out.print(cities[e.other(e.either())] +" "+e.distance()+ " "+cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
		                    	}
	                    		else {
	                    			if (e.either()==nexte){
	                    				//if (e.other(e.either())==v1) {System.out.print("HERE1");break;}
	                    				System.out.print(e.distance()+" "+ cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				//if (e.either()==v1) {System.out.print("HERE2");break;}
	                    				System.out.print(e.distance()+" "+ cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
	                    		}
		                    	e2=e;
	                    	}
	                    	else{
	                    		//System.out.print("  deleted*"+ e+"* ");
	                    	}
	                    	i=1;
	                    }
		            }
					DijkstraSP2 sp2 = new DijkstraSP2(G, v1);
					int dist2=0;
					if (sp2.hasPathTo(v3)) {
	                	dist2=(int)sp2.distTo(v3);
	                	Edge e2=null;
	                	int nexte = 0, i = 0;
	                    for (Edge e : sp2.pathTo(v3)) {
	                    	if (e!=e2){
	                    		if(i==0){
	                    			if (e.either()==v3){
	                    				System.out.print(e.distance()+ " "+cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				System.out.print(e.distance()+ " "+cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
		                    	}
	                    		else {
	                    			if (e.either()==nexte){
	                    				//if (e.other(e.either())==v1) {System.out.print("HERE1");break;}
	                    				System.out.print(e.distance()+" "+ cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				//if (e.either()==v1) {System.out.print("HERE2");break;}
	                    				System.out.print(e.distance()+" "+ cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
	                    		}
		                    	e2=e;
	                    	}
	                    	else{
	                    		//System.out.print("  deleted*"+ e+"* ");
	                    	}
	                    	i=1;
	                    }
	                    System.out.println();
	                	System.out.println("Shortest distance from " + v1str + " to "+ v3str + " is " + (dist1+dist2));
		            }
				}
			}
			else if (userChoice==8){ 
				String idk = input.nextLine();
				System.out.print("");
				
				System.out.print("Source city(case sensitive): ");
				String v1str=input.nextLine();
				int v1 = Arrays.asList(cities).indexOf(v1str);
				//System.out.print(v1);
				System.out.print("Middle city(case sensitive): ");
				String v3str=input.nextLine();
				int v3 = Arrays.asList(cities).indexOf(v3str);
				//System.out.print(v3);
				System.out.print("Destination city(case sensitive): ");
				String v2str=input.nextLine();
				int v2= Arrays.asList(cities).indexOf(v2str);
				//System.out.print(v2);
				System.out.println("");
				
				if (v1==-1 || v2==-1 || v3==-1){
					System.out.println("You've entered invalid city names");
				}
				else{
					//Shortest price paths: Dijkstra’s algorithm
					System.out.println("SHORTEST COST PATH from " + v1str + " to "+ v2str +" through "+v3str+" (in reverse order)\n------------------------------");
                    DijkstraSP sp2 = new DijkstraSP(G, v3);
					if (sp2.hasPathTo(v2)) {
	                	Edge e2=null;
	                	int nexte = 0, i = 0;
	                    for (Edge e : sp2.pathTo(v2)) {
	                    	if (e!=e2){
	                    		if(i==0){
	                    			if (e.either()==v2){
	                    				System.out.print(cities[e.either()] +" "+e.price()+ " "+cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				System.out.print(cities[e.other(e.either())] +" "+e.price()+ " "+cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
		                    	}
	                    		else {
	                    			if (e.either()==nexte){
	                    				//if (e.other(e.either())==v1) {System.out.print("HERE1");break;}
	                    				System.out.print(e.price()+" "+ cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				//if (e.either()==v1) {System.out.print("HERE2");break;}
	                    				System.out.print(e.price()+" "+ cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
	                    		}
		                    	e2=e;
	                    	}
	                    	else{
	                    		//System.out.print("  deleted*"+ e+"* ");
	                    	}
	                    	i=1;
	                    }
		            }
					DijkstraSP sp = new DijkstraSP(G, v1);
					if (sp.hasPathTo(v3)) {
	                	Edge e2=null;
	                	int nexte = 0, i = 0;
	                    for (Edge e : sp.pathTo(v3)) {
	                    	if (e!=e2){
	                    		if(i==0){
	                    			if (e.either()==v3){
	                    				System.out.print(e.price()+ " "+cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				System.out.print(e.price()+ " "+cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
		                    	}
	                    		else {
	                    			if (e.either()==nexte){
	                    				//if (e.other(e.either())==v1) {System.out.print("HERE1");break;}
	                    				System.out.print(e.price()+" "+ cities[e.other(e.either())]+" ");
	                    				nexte=e.other(e.either());
	                    			}
	                    			else{
	                    				//if (e.either()==v1) {System.out.print("HERE2");break;}
	                    				System.out.print(e.price()+" "+ cities[e.either()]+" ");
	                    				nexte=e.either();
	                    			}
	                    		}
		                    	e2=e;
	                    	}
	                    	else{
	                    		//System.out.print("  deleted*"+ e+"* ");
	                    	}
	                    	i=1;
	                    }
					}
					System.out.println();
                	System.out.println("Shortest cost from " + v1str + " to "+ v2str + " though "+ v3str+" is " +(sp.distTo(v3)+sp2.distTo(v2)));
			    }
			}
			else if (userChoice==9){ 
				String idk = input.nextLine();
				System.out.print("");
				
				System.out.print("City/vertex to remove (case sensitive): ");
				String v1str=input.nextLine();
				int rmVertex = Arrays.asList(cities).indexOf(v1str);
				
				//System.out.print(v1);
				System.out.println("");
				
				if (rmVertex==-1){
					System.out.println("You've entered an invalid city name");
				}
				else{
					for (Edge e : G.edges()){
						if (e.either()==rmVertex || e.other(e.either())==rmVertex){
							G.removeEdge(e);
						}
					}
				}
				System.out.println("Done.");
			}
			else if (userChoice==10){
				String idk = input.nextLine();
				//System.out.print("");
				
				System.out.print("City/vertex to remove (case sensitive): ");
				String v1str=input.nextLine();
				String[] tempcities=new String[cityctr+1];
				for (int i=0; i<cities.length; i++){
					tempcities[i]=cities[i];
					//System.out.println(tempcities[i]);
				}
				//System.out.println();
				tempcities[cityctr]=v1str;
				cities=tempcities;
				/*for (int i=0; i<cities.length; i++){
					System.out.println(cities[i]);
				}*/
				G.addVertex();
				System.out.println();
				System.out.println("Done.");
			}
			else {	//user wants to quit, save data
				quit=1;
				System.out.println("Your data is saved in a file called Output.txt\nGood Bye");
				for (Edge e : G.edges()){
					writer.println((e.either()+1) + " "+ (e.other(e.either())+1)+ " "+e.distance()+" "+e.price());
				}
				writer.close();
			}
		} while (quit!=1);
		
	}
}
