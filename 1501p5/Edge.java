
public class Edge { 

    private int v;
    private int w;
    private int distance;
    private double price;

   /**
     * Create an edge between v and w with given weight.
     */
    public Edge(int v, int w, int distance, double price) {
        this.v = v;
        this.w = w;
        this.distance = distance;
        this.price = price;
    }

   /**
     * Return the weights of this edge.
     */
    public int distance() {
        return distance;
    }

    public double price() {
        return price;
    }

   /**
     * Return either endpoint of this edge.
     */
    public int either() {
        return v;
    }

   /**
     * Return the endpoint of this edge that is different from the given vertex
     * (unless a self-loop).
     */
    public int other(int vertex) {
        if      (vertex == v) return w;
        else if (vertex == w) return v;
        else throw new RuntimeException("Illegal endpoint");
    }

   /**
     * Compare edges by distance.
     */
    public int compareDistance(Edge that) {
        if      (this.distance() < that.distance()) return -1;
        else if (this.distance() > that.distance()) return +1;
        else                                    return  0;
    }
    /**
     * Compare edges by price.
     */
    public int comparePrice(Edge that) {
        if      (this.price() < that.price()) return -1;
        else if (this.price() > that.price()) return +1;
        else                                    return  0;
    }
   /**
     * Return a string representation of this edge.
     */
    public String toString() {
        return String.format("%d-%d %d %.2f", v, w, distance, price);
    }


   /**
     * Test client.
     */
    public static void main(String[] args) {
        Edge e = new Edge(12, 23, 3, 20.5);
        System.out.println(e+ "\n"+e.either() + " "+e.other(e.either()));
    }
}