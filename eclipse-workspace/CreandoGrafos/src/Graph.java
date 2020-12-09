import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Graph {
	
	boolean dirigido;
	private HashMap<Integer, Set<Integer>> nodes = new HashMap<>();

	
	/**
	 * Constructor sin parámetros, por default es un grafo no dirigido.
	 */
	public Graph() {
		dirigido=false;
	}
	
	/**
	 * Constructor que define si el grafo es dirigido o no
	 * @param dirigido	si el valor es true, será un grafo dirigido
	 */
	public Graph(boolean dirigido) {
		this.dirigido=dirigido;
	}
	
	/**
	 * Función para glonar un grafo
	 * @return Copia del grafo
	 */
	public Graph clone() {
		Graph g = new Graph(this.dirigido);
		for ( Integer node : nodes.keySet() ) {
			g.addNode(node,nodes.get(node));
		}
		return g;
	}
	/**
	 * Función para añadir un nodo
	 * @param id	recibe el identificador del nodo
	 */
	public void addNode(int id) {
		if (!nodes.containsKey(id)) {
			nodes.put(id,new HashSet<Integer>());
		}
	}
	
	
	/**
	 * Función para añadir un nodo con un vecindario
	 * @param id	Identificador del nodo a añadir
	 * @param neighbors	Conjunto con los dentificadores de sus nodos vecinos
	 */
	public void addNode(int id,Set<Integer> neighbors) {
		nodes.put(id,neighbors);
		for(Integer n:neighbors) {
			linkNodes(id, n);
		}
	}
	
	/**
	 * Función para eliminar un nodo
	 * @param id	Ientificador del nodo a eliminar
	 */
	public void dropNode(int id) {
		nodes.remove(id);
	}
	
	public int getSize() {
		return nodes.size();
	}
	
	/**
	 * Función para obtener los nodos vecinos de un determinado nodo
	 * @param id	Identificador del nodo del cual se desean conocer sus vecinos
	 * @return
	 */
	public Set<Integer> getNeighbors(int id) {
		return nodes.get(id);
	}
	
	public boolean existsNode(int id) {
		if(nodes.get(id) ==null) {
			return false;
		}else {
			return true;
		}
	}
	
	
	
	/**
	 * Función para enlazar dos nodos
	 * @param a	identificador del primer nodo
	 * @param b	identificador del segundo nodo
	 * @return	retorna true si los nodos no estaban unidos previamente, en caso de que ya estuvieran unidos retorna falso
	 */
	public boolean linkNodes(int a, int b) {
		if (!nodes.containsKey(a)  ) {
			this.addNode(a);
		}
		if (!nodes.containsKey(b)) {
			this.addNode(b);
		}
		if(dirigido) {
			return nodes.get(a).add(b);
		}else {
			nodes.get(a).add(b);
			return nodes.get(b).add(a);
		}
		
	}
	
	/**
	 * Función para comprobar si existe la conexión de a->b
	 * @param a Nodo a
	 * @param b Nodo b
	 * @return retorna true si existe la conexión
	 */
	public boolean isLinked(int a,int b) {
		return nodes.get(a).contains(b);
	}
	

	/**
	 * Función para guardar el nodo en un archivo
	 * @param filename	Nombre del archivo en el que se guardará (incluye la extensión)
	 */
	public void saveFile(String filename) {
		try {
		      FileWriter myWriter = new FileWriter(filename);
		      myWriter.write(this.toGrahpViz());
		      myWriter.close();
		      System.out.println("Grafo guardado correctamente como "+filename);
		    } catch (IOException e) {
		      System.out.println("Ha ocurrido un error al guardar el grafo: "+filename);
		      e.printStackTrace();
		    }
	}
	
	/**
	 * Carga un grafo a partir de un archivo .gv.
	 * Nota: solo se ha probado con los mismos archivos que genera esta biblioteca.
	 * @param filename Archivo a cargar con extension .gv
	 * @return Grafo
	 */
	public static Graph loadFile(String filename) {
		Graph g=null;
		boolean dirigido=false;
		try{	
			File file=new File(filename);
			FileReader fr=new FileReader(file);  
			BufferedReader br=new BufferedReader(fr);
			String line;
			String[] n;
			line=br.readLine();
			if (line.contains("digraph")) {
				dirigido=true;
			}else{
				dirigido=false;
			}
			g=new Graph(dirigido);
			while((line=br.readLine())!=null){
				line=line.trim();
				if(line.contains(";")) {
					line=line.replace(";", "");
					if(line.contains("-")) {
						if(dirigido) {
							n=line.split("->");
							g.linkNodes(Integer.parseInt(n[0].trim()), Integer.parseInt(n[1].trim()));
						}else {
							n=line.split("--");
							g.linkNodes(Integer.parseInt(n[0].trim()), Integer.parseInt(n[1].trim()));
						}
					}else {
						g.addNode(Integer.parseInt(line));
					}
				}
				
			}  
			fr.close();
		}  
		catch(IOException e){  
			e.printStackTrace();  
		}
		return g;
		
	}
	
	
	/**
	 * Función para generar un grafo usando el modelo Erdos Renyi
	 * @param n	Número de nodos del grafo
	 * @param m	Nmero de aristas del grafo
	 * @param dirigido	si es true crea un grafo dirigido, de lo contrario es no dirigido
	 * @param r	Objeto random, en caso de que se desee especificar con una semilla.
	 * @return	Grafo
	 */
	public static Graph genErdosRenyi(int n,int m,boolean dirigido,Random r) {
		if (r==null) r=new Random();
		Graph g=new Graph(dirigido);
		for(int i=0;i<n;i++) { //for para crear los n nodos
			g.addNode(i);
		}
		int i=0; //variable que lleva el conteo de las aristas
		int j=0; //variable auxiliar para seleccionar un nodo al azar
		int k=0; //variable auxiliar para seleccionar un nodo al azar
		while(i<m) { //bucle para obtener las m aristas
			j=r.nextInt(n);
			k=r.nextInt(n);
			while(j==k) { //verifica que sean nodos diferentes j y k
				k=r.nextInt(n);
			}
			if(g.linkNodes(j,k)) {//comprueba si los nodos ya estaban unidos
				i++; //en caso de que sea nueva la union agrega 1 al conteo de aristas
			}
			
		}
		return g;
	}
	
	/**
	 * Función para generar un grafo usando el modelo Gilbert
	 * @param n	Número de nodos del grafo
	 * @param p	Probabilidad de union entre nodos
	 * @param dirigido	si es true crea un grafo dirigido, de lo contrario es no dirigido
	 * @param r	Objeto random, en caso de que se desee especificar con una semilla.
	 * @return	Grafo
	 */
	public static Graph genGilbert(int n,double p,boolean dirigido,Random r) {
		if (r==null) r=new Random();
		Graph g=new Graph(dirigido);
		for(int i=0;i<n;i++) {
			g.addNode(i);
		}
		double k=0;
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				if(i!=j) {
					k=r.nextDouble();
					if(k<p) {
						g.linkNodes(i,j);
					}
				}
			}
		}
		return g;
	}
	
	/**
	 * Función para generar un grafo usando el modelo geográfico simple
	 * @param n	Numero de nodos del gráfo
	 * @param d	distancia mínima para union de nodos
	 * @param dirigido	si es true crea un grafo dirigido, de lo contrario es no dirigido
	 * @param r	Objeto random, en caso de que se desee especificar con una semilla.
	 * @return	Grafo
	 */
	public static Graph genGeografico(int n,double d,boolean dirigido,Random r) {
		if (r==null) r=new Random();
		Graph g=new Graph(dirigido);
		Point2D[] p= new Point2D[n];
		for(int i=0;i<n;i++) {
			g.addNode(i);
			p[i]= new Point2D.Double(r.nextDouble(),r.nextDouble());
		}
		for(int i=0;i<n;i++) {
			for(int j=0;j<n;j++) {
				if(i!=j) {
					if(p[i].distance(p[j])<d) {
						g.linkNodes(i,j);
					}
				}
			}
		}
		return g;
	}
	
	
	/**
	 * Función para generar un grafo usando el modelo Barabasi.
	 * Nota: En mis intentos por generar la variante, realicé este sin querer
	 * por lo que decidí mantenerlo en el código.
	 * @param n	Numero de nodos
	 * @param d	Grado máximo de los nodos
	 * @param dirigido	si es true crea un grafo dirigido, de lo contrario es no dirigido
	 * @param r	Objeto random, en caso de que se desee especificar con una semilla.
	 * @return	Grafo
	 */
	public static Graph genBarabasiAlbert(int n,double d,boolean dirigido,Random r) {
		if (r==null) r=new Random();
		Graph g=new Graph(dirigido);
		List<Integer> intList = new ArrayList<Integer>();
		
		int i,j,k,l;
		for(i=0;i<n;i++) {
			g.addNode(i);
			
		}
		intList.add(0);
		for(i=1;i<d;i++) {
			intList.add(i);
			g.linkNodes(i,i-1);
		}
		
		for(;i<n;i++) {
			for(j=0;j<intList.size();j++) {
				l=intList.get(j);
				k=g.nodes.get(l).size();
				if(r.nextDouble()<k/d) {
					if(g.linkNodes(i,l)) {
						if( k==d-1) {
							intList.remove(j);
							j--;
						}
						break;
					}
				}
				
			}
			intList.add(i);
			if(g.nodes.get(i).size()==0) { //en caso de que el nodo no se haya conectado, se fuerza a que tenga almenos un enlace
				BarabasiVariantEnforceLink(intList,d,g,r);
			}
			
		}
		return g;
	}
	
	/**
	 * Función para generar un grafo usando el modelo Barabasi.
	 * Nota: a diferencia de la función anterior, se seleccionan los nodos de manera aleatoria
	 * @param n	Numero de nodos
	 * @param d	Grado máximo de los nodos
	 * @param dirigido	si es true crea un grafo dirigido, de lo contrario es no dirigido
	 * @param r	Objeto random, en caso de que se desee especificar con una semilla.
	 * @return	Grafo
	 */
	public static Graph genBarabasiAlbertRandom(int n,double d,boolean dirigido,Random r) {
		if (r==null) r=new Random();
		Graph g=new Graph(dirigido);
		List<Integer> intList = new ArrayList<Integer>();
		
		int i,j,k,l;
		for(i=0;i<n;i++) {
			g.addNode(i);
			
		}
		intList.add(0);
		for(i=1;i<d;i++) {
			intList.add(i);
			j=i-1;
			g.linkNodes(i,j);
		}
		
		for(;i<n;i++) {
			Collections.shuffle(intList);
			for(j=0;j<intList.size();j++) {
				l=intList.get(j);
				k=g.nodes.get(l).size();
				if(r.nextDouble()<k/d) {
					if(g.linkNodes(i,l)) {
						if( k==d-1) {
							intList.remove(j);
							j--;
						}
						break;
					}
				}
				
			}
			intList.add(i);
			if(g.nodes.get(i).size()==0) { //en caso de que el nodo no se haya conectado, se fuerza a que tenga almenos un enlace
				BarabasiVariantEnforceLink(intList,d,g,r);
			}
			
		}
		return g;
	}
	
	/**
	 * Función para generar un grafo usando la variante del modelo Barabasi
	 * @param n	Numero de nodos
	 * @param d	Grado máximo de los nodos
	 * @param dirigido	si es true crea un grafo dirigido, de lo contrario es no dirigido
	 * @param r	Objeto random, en caso de que se desee especificar con una semilla.
	 * @return	Grafo
	 */
	public static Graph genBarabasiAlbertVariant(int n,double d,boolean dirigido,Random r) {
		if (r==null) r=new Random();
		Graph g=new Graph(dirigido);
		List<Integer> intList = new ArrayList<Integer>();
		int i,j,k,l;
		for(i=0;i<n;i++) {
			g.addNode(i);
		}
		intList.add(0);
		for(i=1;i<d;i++) {
			intList.add(i);
			j=i-1;
			g.linkNodes(i,j);
			
		}
		
		for(;i<n;i++) {
			for(j=0;j<intList.size();j++) {
				l=intList.get(j);
				k=g.nodes.get(l).size();
				if( k<d) {
					if(r.nextDouble()<k/d) {
						if(g.linkNodes(i,l)) {
							if(g.nodes.get(l).size()==d) {
								intList.remove(j);
								j--;
							}
							if(g.nodes.get(i).size()==(d-1)) { //Con esta condicion se asegura que el siguiente nodo tendrá un espacio libre con quien conectarse
								break;
							}
						}
					}
				}
			}
			intList.add(i);
			if(g.nodes.get(i).size()==0) { //en caso de que el nodo no se haya conectado, se fuerza a que tenga almenos un enlace
				BarabasiVariantEnforceLink(intList,d,g,r);
			}
			
		}
		return g;
	}
	
	
	/**
	 * Función para generar un grafo usando la variante del modelo Barabasi, escogiendo los nodos de manera aleatoria
	 * @param n	Numero de nodos
	 * @param d	Grado máximo de los nodos
	 * @param dirigido	si es true crea un grafo dirigido, de lo contrario es no dirigido
	 * @param r	Objeto random, en caso de que se desee especificar con una semilla.
	 * @return	Grafo
	 */
	public static Graph genBarabasiAlbertVariantRandom(int n,double d,boolean dirigido, Random r) {
		if (r==null) r=new Random();
		Graph g=new Graph(dirigido);
		List<Integer> intList = new ArrayList<Integer>();
		int i,j,k,l;
		for(i=0;i<n;i++) {
			g.addNode(i);
		}
		intList.add(0);
		for(i=1;i<d;i++) {
			intList.add(i);
			for(j=0;j<i;j++) {
				g.linkNodes(i,j);
			}
		}
		
		for(;i<n;i++) {
			Collections.shuffle(intList);
			for(j=0;j<intList.size();j++) {
				l=intList.get(j);
				k=g.nodes.get(l).size();
				if(r.nextDouble()<k/d) {
					if(g.linkNodes(i,l)) {
						if(k==(d-1)) {
							intList.remove(j);
							j--;
						}
						if(g.nodes.get(i).size()==(d-1)) { //Con esta condicion se asegura que el siguiente nodo tendrá un espacio libre con quien conectarse
							break;
						}
					}
				}
			}
			intList.add(i);
			if(g.nodes.get(i).size()==0) { //en caso de que el nodo no se haya conectado, se fuerza a que tenga almenos un enlace
				BarabasiVariantEnforceLink(intList,d,g,r);
			}
			
		}
		return g;
	}
	
	/**
	 * Función auxiliar del modelo barabasi para forzar un enlace
	 * @param intList	Lista con los nodos de grado menor a d
	 * @param d	Grado máximo de los nodos
	 * @param g Grafo en el cual se unen los nodos
	 * @param r	Objeto Random para conservar la semilla usada en la función que lo llama
	 */
	private static void BarabasiVariantEnforceLink(List<Integer> intList,double d,Graph g, Random r) {
		int i,j,k,l;
		i=intList.size()-1;
		while(true) {
			for(j=0;j<i;j++) {
				l=intList.get(j);
				k=g.nodes.get(l).size();
				if(r.nextDouble()<k/d) {
					if(g.linkNodes(intList.get(i),l)) {
						if(k==(d-1)) {
							intList.remove(j);
						}
						return;
					}
				}
				
			}
		}
	}
	
	/**
	 * Función que retorna el arbol BFS de la instancia Graph que manda a llamar el método
	 * @param n índice del nodo que será tomado como nodo raiz
	 * @return Arbol BFS
	 */
	public Graph getBFS(int n) {
		return getBFSFromGraph(this,n);
	}
	
	/**
	 * Función que retorna el arbol BFS de la instancia Graph que se manda como parámetro
	 * @param S grafo del cual se desea obtener su arbol BFS
	 * @param n índice del nodo que será tomado como nodo raiz
	 * @return Arbol BFS
	 */
	public static Graph getBFSFromGraph(Graph S,int n) {
		if(!S.existsNode(n)) {
			System.err.println("El nodo raiz no existe");
			return null;
		}
		
		boolean[] explorados;
		ArrayList<Integer> queue;
		Graph G = new Graph();
		int i;
		explorados = new boolean[S.getSize()];
		for(i=0;i<explorados.length;i++) explorados[i]=false;
		queue = new ArrayList<Integer>();
		queue.add(n);
		explorados[n]=true;
		G.addNode(n);
		
		while(queue.size() != 0) {
			i = queue.get(0);
			queue.remove(0);
			Set<Integer>neighbors=S.getNeighbors(i);
			for (Integer value : neighbors) {
				if(!explorados[value]) {
					G.linkNodes(i, value);
					explorados[value]=true;
					queue.add(value);
				}
			}
		}
		return G;
	}
	
	/**
	 * Función que retorna el arbol DFS iterativo de la instancia Graph que manda a llamar el método
	 * @param n índice del nodo que será tomado como nodo raiz
	 * @return Arbol DFSi
	 */
	public Graph getDFSi(int n) {
		return getDFSi_FromGraph(this,n);
	}
	
	/**
	 * Función que retorna el arbol DFS iterativo de la instancia Graph que se manda como parámetro
	 * @param S grafo del cual se desea obtener su arbol DFSi
	 * @param n índice del nodo que será tomado como nodo raiz
	 * @return Arbol DFSi
	 */
	public static Graph getDFSi_FromGraph(Graph S,int n) {
		if(!S.existsNode(n)) {
			System.err.println("El nodo raiz no existe");
			return null;
		}
		
		int i;
		int j;
		boolean[] explorados = new boolean[S.getSize()];
		for(i=0;i<explorados.length;i++) explorados[i]=false;
		
		ArrayList<Integer> visitados,por_visitar;
		por_visitar = new ArrayList<Integer>();
		visitados = new ArrayList<Integer>();
		Set<Integer>neighbors;
		
		boolean backwards=false;
		Graph G = new Graph();
		
		por_visitar.add(n);
		G.addNode(n);
		
		while(por_visitar.size() != 0) {
			j = por_visitar.size()-1;
			i = por_visitar.get(j);
			por_visitar.remove(j);
			if(explorados[i]) {
				continue;
			}
			explorados[i]=true;
			visitados.add(i);
			backwards=true;
			
			neighbors=S.getNeighbors(i);
			for (Integer value : neighbors) {
				if(!explorados[value]) {
					backwards=false;
					por_visitar.add(value);
				}
			}
			while( (visitados.size() > 1) && backwards) {
				i = visitados.size()-1;
				j= visitados.get(i-1);
				G.linkNodes(visitados.get(i), j);
				visitados.remove(i);
				neighbors=S.getNeighbors(j);
				for (Integer value : neighbors) {
					if(!explorados[value]) {
						backwards=false;
					}
				}
				
			}
		}
		return G;
	}
	
	/**
	 * Función que retorna el arbol DFS recursivo de la instancia Graph que manda a llamar el método
	 * @param n índice del nodo que será tomado como nodo raiz
	 * @return Arbol DFSr
	 */
	public Graph getDFSr(int n) {
		return getDFSFromGraph(this,n);
	}
	
	/**
	 * Función para obtener el arbol DFS recursivo de la instancia Graph que se manda como parámetro
	 * Esta función manda a llamar una función recursiva, la diferencia entre esta y la recursiva es que en esta
	 * se inicializan las variables G y explorados.
	 * @param S grafo del cual se desea obtener su arbol DFSr
	 * @param n índice del nodo que será tomado como nodo raiz
	 * @return Arbol BFS
	 */
	public static Graph getDFSFromGraph(Graph S,int n) {
		if(!S.existsNode(n)) {
			System.err.println("El nodo raiz no existe");
			return null;
		}
		Graph G = new Graph();
		boolean [] explorados = new boolean[S.getSize()];
		for(int i=0;i<explorados.length;i++) explorados[i]=false;
		explorados[n]=true;
		G.addNode(n);
		getDFSr(n,S,G,explorados);
		return G;
	}
	
	/**
	 * Función recursiva para obtener el arbol DFS. Esta función no necesita retornar nada ya que
	 * al recibir un objeto tipo Grafo, este es un parametro por referencia y al modificarlo dentro
	 * del método tambien se modifica en el método que lo mandó a llamar
	 * @param n nodo raiz
	 * @param S Grafo fuente
	 * @param G Arbol BFS
	 * @param explorados variable para saber los nodos explorados
	 */
	private static void getDFSr(int n,Graph S, Graph G, boolean[] explorados) {
		Set<Integer>neighbors=S.getNeighbors(n);
		for (Integer value : neighbors) {
			if(!explorados[value]) {
				explorados[value]=true;
				G.linkNodes(n, value);
				getDFSr(value,S,G,explorados);
			}
		}
	}
	
	
	/**
	 * funcion que retorna un String con el formato GraphViz 
	 * @return String con formato de graph viz
	 */
	public String toGrahpViz() {
		String res="";
		String arista;
		for ( Integer node : nodes.keySet() ) {
			res+="  "+node+";\n";
		}
		if(dirigido) {
			arista=" -> ";
			res="digraph {\n"+res;
			for (Map.Entry<Integer,Set<Integer>> n : nodes.entrySet()) {
				Integer node=n.getKey();
				for (Integer value : n.getValue()) {
					res+="  "+node+arista+value+";\n";
				}
		    }
		}else {
			arista=" -- ";
			res="graph {\n"+res;
			for (Map.Entry<Integer,Set<Integer>> n : nodes.entrySet()) {
				Integer node=n.getKey();
				for (Integer value : n.getValue()) {
					if (node<value) {
						res+="  "+node+arista+value+";\n";
					}
				}
		    }
		}
		
		res+="}";
		return res;
	}
}