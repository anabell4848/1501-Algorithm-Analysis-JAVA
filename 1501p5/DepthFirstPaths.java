import java.util.ArrayList;
import java.util.Arrays;
import java.util.Stack;


public class DepthFirstPaths {
    private boolean[] marked;    // marked[v] = is there an s-v path?
    private int[] edgeTo;        // edgeTo[v] = last edge on s-v path
    private final int s;         // source vertex
    private final static int capacity=100000;         // source vertex
    private static double[] priceTo=new double[capacity];
    private static String[] pathPrice=new String[capacity];    
    private static String[] paths=new String[capacity];
    static StringBuilder sb = new StringBuilder();
    static StringBuilder sb2 = new StringBuilder();
    static double trackprice=0;
    static int numpaths=0;

    /**
     * Computes a path between <tt>s</tt> and every other vertex in graph <tt>G</tt>.
     * @param G the graph
     * @param s the source vertex
     */
    public DepthFirstPaths(EdgeWeightedGraph G, int s, StringBuilder sb1,StringBuilder sb12, double trackprice1, double cost) {
        this.s = s;
        edgeTo = new int[G.V()];
        marked = new boolean[G.V()];  
        sb = sb1;
        sb2 = sb12;
    	trackprice = trackprice1;
        dfs(G, s, cost);
    }

    // depth first search from v
    private void dfs(EdgeWeightedGraph G, int v, double cost) {
    	//System.out.println(" v="+v);
        marked[v] = true;
        for (Edge e : G.adj(v)) {
        	int w = e.other(v);
             //System.out.print("  w="+w);
            
            if (!marked[w]) {
	           // System.out.println("marked");
                edgeTo[w] = v;
                
                sb.append(w+" ");
                String sbstring=sb+"";
                sb2.append(e.price()+" ");
                String sbstring2=sb2+"";
                //System.out.println("  "+e.price()+"+"+trackprice+" = ");
                trackprice+=e.price();
                
                //check if it's already in the paths
                
                
                if (trackprice<=cost){
                	boolean inthere= false;
	                for (int i=0;i<paths.length;i++){
	                	if (sbstring.equals(paths[i])){
	                		inthere=true;
	                		break;
	                	}
	                }
                	if (!inthere){
			            paths[numpaths]=sbstring;
			            pathPrice[numpaths]= sbstring2;
			            priceTo[numpaths]=trackprice;
			            //System.out.println(" ADDED "+numpaths+". " +sbstring +" "+priceTo[numpaths]);
			            numpaths++;
                	}
                }
                dfs(G, w, cost);
                
                String tosplit=sb+"";
                String[] splitting = tosplit.split("\\s+");
                String laste=splitting[splitting.length-1];
                //System.out.println(" WHAAAAAAAA "+sb+" "+sb.length() +"-" +laste.length()+"-1");
                sb.setLength(sb.length() -laste.length() - 1);
                
                String pricesize=e.price()+"";
                sb2.setLength(sb2.length()-pricesize.length()-1);
                //System.out.println("Backtrack  "+trackprice+"  "+e.price());
                trackprice-=e.price();
                //System.out.println("     w="+w+" HERRRRRRRRRRR " + sb);
                marked[w]=false;
            }
        }
    }

    /**
     * Is there a path between the source vertex <tt>s</tt> and vertex <tt>v</tt>?
     * @param v the vertex
     * @return <tt>true</tt> if there is a path, <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return marked[v];
    }
    
    public double getpriceTo(int v) {
        return priceTo[v];
    }
    
    public String getpaths(int v) {
        return paths[v];
    }
    
    public void output(double cost, String[] cities){
    	for (int j=0; j<numpaths;j++){
    		String[] splitpaths = paths[j].split("\\s+");
        	double price=(double)Math.round(priceTo[j] * 100) / 100;
			String[] split = pathPrice[j].split("\\s+");
			System.out.print("Cost: "+price+" Path (reversed): ");
			for (int i=0; i<splitpaths.length;i++){
				int ctr=Integer.parseInt(splitpaths[i]);
				if (i<splitpaths.length-1){
					//System.out.println(paths[j]+ "  "+pathPrice[j]);
					System.out.print(cities[ctr]+" "+split[i]+" ");
				}else {
					System.out.print(cities[ctr]);
				}
			}
			System.out.println();
			//System.out.println(paths[j]+ "  "+pathPrice[j]);
        }
    	priceTo=new double[capacity];
        pathPrice=new String[capacity];    
        paths=new String[capacity];
    	numpaths=0;
    }

    /**
     * Returns a path between the source vertex <tt>s</tt> and vertex <tt>v</tt>, or
     * <tt>null</tt> if no such path.
     * @param v the vertex
     * @return the sequence of vertices on a path between the source vertex
     *   <tt>s</tt> and vertex <tt>v</tt>, as an Iterable
     */
    public Iterable<Integer> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Integer> path = new Stack<Integer>();
        for (int x = v; x != s; x = edgeTo[x])
            path.push(x);
        path.push(s);
        return path;
    }

    /**
     * Unit tests the <tt>DepthFirstPaths</tt> data type.
     */
    public static void main(String[] args) {
    	EdgeWeightedGraph G;
        // random graph with V vertices and E edges
        int V = 11;
        int E = 11;
        G = new EdgeWeightedGraph(V, E);
        System.out.println(G);
        for (int i=0; i < G.V();i++){
            StringBuilder sb = new StringBuilder();
            sb.setLength(0);
            sb2.setLength(0);
	    	sb.append(i +" ");
	    	double trackprice = 0;
	    	double cost = 50;
	        DepthFirstPaths dfs = new DepthFirstPaths(G, i, sb, sb2, trackprice, cost);
	        //System.out.println();
	        if (i==G.V()-1){
		       // dfs.output(5);
        	}
        }
        
    }

}