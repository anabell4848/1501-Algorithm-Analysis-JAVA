import java.util.ArrayList;


public class EdgeWeightedGraph {
    private int V;
    private int E;
    private ArrayList<Edge>[] adj;	
    //private String[] keys;           // index  -> string
    
   /**
     * Create an empty edge-weighted graph with V vertices.
     */
    public EdgeWeightedGraph(int V) {
        if (V < 0) throw new RuntimeException("Number of vertices must be nonnegative");
        this.V = V;
        this.E = 0;
        adj = (ArrayList<Edge>[]) new ArrayList[V];
        for (int v = 0; v < V; v++) {
        	adj[v] = new ArrayList<Edge>();
        }
        //keys = new String[V];
    }
    
    public void addVertex(){
    	V++;
    	ArrayList<Edge>[] tempadj=(ArrayList<Edge>[]) new ArrayList[V];
		for (int i=0; i<adj.length; i++){
			tempadj[i]=adj[i];
			//System.out.println(i+" "+tempadj[i]);
		}
		//System.out.println();
		tempadj[V-1]=new ArrayList<Edge>();
		adj=tempadj;
		/*for (int i=0; i<adj.length; i++){
			System.out.println(i+" "+adj[i]);
		}*/
    }
   /**
     * Create a random edge-weighted graph with V vertices and E edges.
     * The expected running time is proportional to V + E.
     */
    public EdgeWeightedGraph(int V, int E) {
        this(V);
        if (E < 0) throw new RuntimeException("Number of edges must be nonnegative");
        for (int i = 0; i < E; i++) {
            int v = (int) (Math.random() * V);
            int w = (int) (Math.random() * V);
            int distance = 4;
            double price = Math.round(100 * Math.random()) / 100.0;
            Edge e = new Edge(v, w, distance, price);
            addEdge(e);
        }
    } 

   /**
     * Return the number of vertices in this graph.
     */
    public int V() {
        return V;
    }

   /**
     * Return the number of edges in this graph.
     */
    public int E() {
        return E;
    }


   /**
     * Add the edge e to this graph.
     */
    public void addEdge(Edge e) {
        int v = e.either();
        int w = e.other(v);
        int ok=1;
        for (Edge e2 : adj(v)) {
	        if (e2.other(v)==w){
		        ok=0;
		        System.out.print("Couldn't add route, route already exists. ");
	    	}
        }
        if (ok==1){
        	adj[v].add(e);
	        adj[w].add(e);
	        E++;
	        //System.out.print("Route added");
        }
    }

    public void removeEdge(int v, int w){
    	for (Edge e : adj(v)) {
    		//System.out.println(v+" "+w + "e.other="+ e.other(v));
            if (e.other(v)==w) {
                removeEdge(e);
            	//System.out.print("REMOVE success ");
                break;
            }
            else {
            	//System.out.print("REMOVE fail ");
            }
        }
    	
    }
    public void removeEdge(Edge e){
    	 int v = e.either();
         int w = e.other(v);
         adj[v].remove(e);
         adj[w].remove(e);
         E--;
    }
   /**
     * Return the edges incident to vertex v as an Iterable.
     * To iterate over the edges incident to vertex v, use foreach notation:
     * for (Edge e : graph.adj(v)).
     */
    public Iterable<Edge> adj(int v) {
        return adj[v];
    }

   /**
     * Return all edges in this graph as an Iterable.
     * To iterate over the edges, use foreach notation:
     * for (Edge e : graph.edges()).
     */
    public Iterable<Edge> edges() {
        ArrayList<Edge> list = new ArrayList<Edge>();
        for (int v = 0; v < V; v++) {
            for (Edge e : adj(v)) {
            	//System.out.print(v);
                if (e.other(v) > v) {
                    list.add(e);
                }
            }
        }
        return list;
    }



   /**
     * Return a string representation of this graph.
     */
    public String toString() {
        String NEWLINE = System.getProperty("line.separator");
        StringBuilder s = new StringBuilder();
        s.append(V + " " + E + NEWLINE);
        for (int v = 0; v < V; v++) {
            s.append(v + ": ");
            for (Edge e : adj[v]) {
                s.append(e + "  ");
            }
            s.append(NEWLINE);
        }
        return s.toString();
    }

   /**
     * Test client.
     */
    public static void main(String[] args) {
        EdgeWeightedGraph G;
        // random graph with V vertices and E edges, parallel edges allowed
        int V = Integer.parseInt("2");
        int E = Integer.parseInt("5");
        G = new EdgeWeightedGraph(V, E);
        //G.removeEdge(1, 1);
        System.out.println(G);
    } 

}