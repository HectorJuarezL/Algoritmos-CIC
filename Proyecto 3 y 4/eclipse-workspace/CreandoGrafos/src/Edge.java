
import java.util.Random;

/**
 * Clase arista. Esta clase tiene como atributo el 
 * nodo a (origen), el nodo b (destino) y el peso w
 * @author Daniel
 *
 */
public class Edge{
	private static Random r = new Random();
	int a;
	int b;
	int w;
	
	/**
	 * Funcion para establecer una semilla para los numeros aleatorios.
	 * @param r
	 */
	public static void setRanrom(Random r) {
		Edge.r=r;
	}
	
	/**
	 * Constructor que recibe los nodos de origen, destino y el peso de la arista
	 * @param a nodo origen
	 * @param b nodo destino
	 * @param w peso del enlace
	 */
	public Edge(int a,int b,int w) {
		this.a=a;
		this.b=b;
		this.w=w;
	}
	
	/**
	 * Constructor que recibe los nodos de origen y destino. El peso se asigna de
	 * forma aleatoria.
	 * @param a nodo a
	 * @param b nodo b
	 */
	public Edge(int a,int b) {
		this.a=a;
		this.b=b;
		this.w=r.nextInt(100)+1;
	}
	
	/**
	 * Funcion para obtener la arista con direccion invertida, 
	 * es decir b->a
	 * @return
	 */
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
          
        // typecast o a tipo Edge para poder comparar sus atributos
        Edge c = (Edge) o; 
          
        // retorna el resultado de la comparacion de sus atributos.
        return a==c.a && b==c.b && w==c.w;
	}
	
    @Override
	public String toString() {
		return a+"--"+b+" ["+w+"]";
	}
}
