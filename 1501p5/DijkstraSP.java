import java.util.Stack;


public class DijkstraSP {
    private double[] distTo;          // distTo[v] = distance  of shortest s->v path
    private Edge[] edgeTo;    // edgeTo[v] = last edge on shortest s->v path
    private IndexMinPQ<Double> pq;    // priority queue of vertices

    /**
     * Computes a shortest paths tree from <tt>s</tt> to every other vertex in
     * the edge-weighted digraph <tt>G</tt>.
     * @param G the edge-weighted digraph
     * @param s the source vertex
     * @throws IllegalArgumentException if an edge distance is negative
     * @throws IllegalArgumentException unless 0 &le; <tt>s</tt> &le; <tt>V</tt> - 1
     */
    public DijkstraSP(EdgeWeightedGraph G, int s) {
        for (Edge e : G.edges()) {
            if (e.distance() < 0)
                throw new IllegalArgumentException("edge " + e + " has negative distance");
        }

        distTo = new double[G.V()];
        edgeTo = new Edge[G.V()];
        for (int v = 0; v < G.V(); v++)
            distTo[v] = Double.POSITIVE_INFINITY;
        distTo[s] = 0.0;

        // relax vertices in order of distance from s
        pq = new IndexMinPQ<Double>(G.V());
        pq.insert(s, distTo[s]);
        while (!pq.isEmpty()) {
            int v = pq.delMin();
            for (Edge e : G.adj(v))
                relax(e);
        }

        // check optimality conditions
        assert check(G, s);
    }

    // relax edge e and update pq if changed
    private void relax(Edge e) {
        int v = e.either(), w = e.other(v);
        if (distTo[w] > distTo[v] + e.price()) {

            distTo[w] = distTo[v] + e.price();
            edgeTo[w] = e;
            if (pq.contains(w)) {
            	pq.decreaseKey(w, distTo[w]);}
            else  {
	    		//System.out.println("1 HERE "+ v+ " "+ w);
	            pq.insert(w, distTo[w]);}
        }
        w = e.either();
        v = e.other(w);					
        if (distTo[w] > distTo[v] + e.price()) {
            distTo[w] = distTo[v] + e.price();
            edgeTo[w] = e;
            if (pq.contains(w)) {
            	pq.decreaseKey(w, distTo[w]);}
            else {
            	//System.out.println("2 HERE "+ w+ " "+ v);
            	pq.insert(w, distTo[w]);}
        }
    }

    /**
     * Returns the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     * @param v the destination vertex
     * @return the length of a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>;
     *    <tt>Double.POSITIVE_INFINITY</tt> if no such path
     */
    public double distTo(int v) {
        return distTo[v];
    }

    /**
     * Is there a path from the source vertex <tt>s</tt> to vertex <tt>v</tt>?
     * @param v the destination vertex
     * @return <tt>true</tt> if there is a path from the source vertex
     *    <tt>s</tt> to vertex <tt>v</tt>, and <tt>false</tt> otherwise
     */
    public boolean hasPathTo(int v) {
        return distTo[v] < Double.POSITIVE_INFINITY;
    }

    /**
     * Returns a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>.
     * @param v the destination vertex
     * @return a shortest path from the source vertex <tt>s</tt> to vertex <tt>v</tt>
     *    as an iterable of edges, and <tt>null</tt> if no such path
     */
    public Iterable<Edge> pathTo(int v) {
        if (!hasPathTo(v)) return null;
        Stack<Edge> path = new Stack<Edge>();
        int i=0, tried=0, tracki=0;
        Edge nexte = null;
        for (Edge e = edgeTo[v]; e != null; e = nexte) {
        	//System.out.println("\n"+i+". e="+e+ "   nexte="+nexte);     
            path.push(e);
            if (tried==0)	{	
            	nexte=edgeTo[e.either()];
            	//System.out.println(i+".   "+edgeTo[e.either()]); 
            	
            	if (tracki<i){
            		tried=1;
            		//path.pop();
            		//System.out.println(i+". tried"+e);
            	}
            	tracki=i;
            }
            if (tried==1){      
            	nexte=edgeTo[e.other(e.either())];
            	//System.out.println(i+".      "+edgeTo[e.other(e.either())]);
            	tried=0;
            	tracki=i+1;
            }
            if (i>20){
            	break;
            }
        	i++;
        }
        return path;
    }


    // check optimality conditions:
    // (i) for all edges e:            distTo[e.to()] <= distTo[e.from()] + e.distance()
    // (ii) for all edge e on the SPT: distTo[e.to()] == distTo[e.from()] + e.distance()
    private boolean check(EdgeWeightedGraph G, int s) {

        // check that edge distances are nonnegative
        for (Edge e : G.edges()) {
            if (e.distance() < 0) {
                System.err.println("negative edge distance detected");
                return false;
            }
        }

        // check that distTo[v] and edgeTo[v] are consistent
        if (distTo[s] != 0.0 || edgeTo[s] != null) {
            System.err.println("distTo[s] and edgeTo[s] inconsistent");
            return false;
        }
        for (int v = 0; v < G.V(); v++) {
            if (v == s) continue;
            if (edgeTo[v] == null && distTo[v] != Double.POSITIVE_INFINITY) {
                System.err.println("distTo[] and edgeTo[] inconsistent");
                return false;
            }
        }

        // check that all edges e = v->w satisfy distTo[w] <= distTo[v] + e.distance()
        for (int v = 0; v < G.V(); v++) {
            for (Edge e : G.adj(v)) {
                int w = e.other(v);			
                if (distTo[v] + e.distance() < distTo[w]) {
                    System.err.println("edge " + e + " not relaxed");
                    return false;
                }
            }
        }

        // check that all edges e = v->w on SPT satisfy distTo[w] == distTo[v] + e.distance()
        for (int w = 0; w < G.V(); w++) {
            if (edgeTo[w] == null) continue;
            Edge e = edgeTo[w];
            int v = e.either();					
            if (w != e.other(v)) return false;
            if (distTo[v] + e.distance() != distTo[w]) {
                System.err.println("edge " + e + " on shortest path not tight");
                return false;
            }
        }
        return true;
    }


    /**
     * Unit tests the <tt>DijkstraSP</tt> data type.
     */
    public static void main(String[] args) {
        EdgeWeightedGraph G;
        // random graph with V vertices and E edges, parallel edges allowed
        int V = 4;
        int E = 5;
        G = new EdgeWeightedGraph(V, E);
        
        System.out.println(G);
        int s = 1;
        // compute shortest paths
        DijkstraSP sp = new DijkstraSP(G, s);


        // print shortest path
        for (int t = 0; t < G.V(); t++) {
            if (sp.hasPathTo(t)) {
                System.out.printf("%d to %d (%.0f)  ", s, t, sp.distTo(t));
                if (sp.hasPathTo(t)) {
                	Edge e2=null;
                	int nexte = 0,i = 0;
                    for (Edge e : sp.pathTo(t)) {
                    	if (e!=e2){
                    		if(i==0){
                    			if (e.either()==t){
                    				System.out.print(e.either() +" "+e.price()+ " "+e.other(e.either())+" ");
                    				nexte=e.other(e.either());
                    			}
                    			else{
                    				System.out.print(e.other(e.either()) +" "+e.price()+ " "+e.either()+" ");
                    				nexte=e.either();
                    			}
	                    	}
                    		else {
                    			if (e.either()==nexte){
                    				System.out.print(e.price()+" "+ e.other(e.either())+" ");
                    				nexte=e.other(e.either());
                    			}
                    			else{
                    				System.out.print(e.price()+" "+ e.either()+" ");
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
                    //System.out.print(" "+s);
                }
                System.out.println();
            }
            else {
                System.out.printf("%d to %d         no path\n", s, t);
            }
        }
    }

}