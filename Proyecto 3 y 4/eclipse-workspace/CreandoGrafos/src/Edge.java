
import java.util.Random;

public class Edge{
	private static Random r = new Random();
	int a;
	int b;
	int w;
	
	public static void setRanrom(Random r) {
		Edge.r=r;
	}
	
	public Edge(int a,int b,int w) {
		this.a=a;
		this.b=b;
		this.w=w;
	}
	
	public Edge(int a,int b) {
		this.a=a;
		this.b=b;
		this.w=r.nextInt(100)+1;
	}
	
	public int intValue() {
		return b;
	}
	
	public Edge inverse() {
		return new Edge(this.b,this.a,this.w);
	}
	
	@Override
	public boolean equals(Object o) {
        if (o == this) { 
            return true; 
        } 
        if (!(o instanceof Edge)) { 
            return false; 
        } 
          
        // typecast o to Complex so that we can compare data members  
        Edge c = (Edge) o; 
          
        // Compare the data members and return accordingly  
        return a==c.a && b==c.b && w==c.w;
	}
	
    @Override
	public String toString() {
		return a+"--"+b+" ["+w+"]";
	}
}
