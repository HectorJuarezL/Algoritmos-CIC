import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class Graph {
	
	boolean dirigido;
	private HashMap<Integer, Set<Edge>> nodes = new HashMap<>();
	private HashMap<Integer, String> labels = new HashMap<>();

	
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
		for ( int node : nodes.keySet() ) {
			g.addNode(node,nodes.get(node));
		}
		return g;
	}
	
	
	/**
	 * Función para obtener todas las aristas del grafo. 
	 * Por defecto, las retorna de manera ascendente con respecto a su peso.
	 * @return ArrayList de aristas
	 */
	public ArrayList<Edge> getEdges(){
		return getEdges(true);
	}
	
	/**
	 * Función para obtener todas las aristas del grafo. 
	 * @param asc valor para ordenar las aristas ya sea ascendente (true) o
	 * 			descendente (false)
	 * @return ArrayList de aristas
	 */
	public ArrayList<Edge> getEdges(boolean asc) {
		Set<Edge> edges_set = new HashSet<Edge>();
		for (Map.Entry<Integer,Set<Edge>> n : nodes.entrySet()) {
			for (Edge value : n.getValue()) {
				edges_set.add(value);
			}
	    }
		ArrayList<Edge> edges= new ArrayList<Edge>();
		for (Edge e : edges_set) {
			edges.add(e);
		}
		if(asc) {
			Collections.sort(edges, new Comparator<Edge>() {
			    @Override
			    public int compare(Edge o1, Edge o2) {
			    	return o1.w < o2.w ? -1
							: o1.w > o2.w ? 1
							: 0;
			    }
			});
		}else {
			Collections.sort(edges, new Comparator<Edge>() {
			    @Override
			    public int compare(Edge o1, Edge o2) {
			    	return o1.w > o2.w ? -1
							: o1.w < o2.w ? 1
							: 0;
			    }
			});
		}
		
		
		return edges;
	}
	

	/**
	 * Funcion para obtener la suma de los pesos de las aristas de un grafo
	 * @return peso del grafo
	 */
	public long getGraphWeight() {
		long sum=0;
		for (Edge e:getEdges()) {
			sum+=e.w;
		}
		if(!dirigido) {
			sum/=2;
		}
		return sum;
	}
	
	/**
	 * Funcion para obtener el peso de una arista dada por sus nodos
	 * @param a nodo a
	 * @param b nodo b
	 * @return peso de la arista
	 */
	public int getW(int a,int b) {
		if (existsNode(a) && existsNode(b)) {
			for (Edge value : nodes.get(a)) {
				if (value.b==b) {
					return value.w;
				}
			}
			return Integer.MAX_VALUE;
		}else {
			return Integer.MAX_VALUE;
		}
	}
	
	/**
	 * Función para añadir un nodo
	 * @param id	recibe el identificador del nodo
	 */
	public void addNode(int id) {
		if (!nodes.containsKey(id)) {
			nodes.put(id,new HashSet<Edge>());
			labels.put(id, ""+id);
		}
	}
	
	/**
	 * Funcion para añadir un nodo y establecer una etiqueta personalizada a ese nodo
	 * @param id identificador del nodo
	 * @param label etiqueta del nodo
	 */
	public void addNode(int id, String label) {
		if (!nodes.containsKey(id)) {
			nodes.put(id,new HashSet<Edge>());
			labels.put(id, label);
		}
	}
	
	/**
	 * Función para añadir un nodo con un vecindario
	 * @param id	Identificador del nodo a añadir
	 * @param neighbors	Conjunto con los dentificadores de sus nodos vecinos
	 */
	public void addNode(int id,Set<Edge> neighbors) {
		nodes.put(id,neighbors);
		labels.put(id,""+id);
		if(!dirigido) {
			for(Edge n:neighbors) {
				linkNodes(n);
			}
		}
		
	}
	
	
	/**
	 * Funcion para eliminar una arista del grafo
	 * @param e recibe la arista a eliminar
	 */
	public void dropEdge(Edge e) {
		nodes.get(e.a).remove(e);
		if (!dirigido) {
			nodes.get(e.b).remove(e.inverse());
		}
	}
	
	/**
	 * Funcion para obtener el numero de nodos del grafo
	 * @return número de nodos del grafo
	 */
	public int getSize() {
		return nodes.size();
	}
	
	/**
	 * Función para obtener los nodos vecinos de un determinado nodo
	 * @param id	Identificador del nodo del cual se desean conocer sus vecinos
	 * @return
	 */
	public Set<Edge> getNeighbors(int id) {
		return nodes.get(id);
	}
	
	/**
	 * Funcion para comprobar si existe un nodo
	 * @param id identificador del nodo a comprobar
	 * @return retorna true si si existe el nodo, de lo contrario retorna false
	 */
	public boolean existsNode(int id) {
		if(nodes.get(id) ==null) {
			return false;
		}else {
			return true;
		}
	}
	
	
	/**
	 * Función para comprobar si existe la conexión de a->b
	 * @param a Nodo a
	 * @param b Nodo b
	 * @return retorna true si existe la conexión
	 */
	public boolean isLinked(int a,int b) {
		for (Edge e : nodes.get(a)) {
			if (e.b==b) {
				return true;
			}
		}
		return false;
	}
	
	
	/**
	 * Función para enlazar dos nodos
	 * @param a	identificador del primer nodo
	 * @param b	identificador del segundo nodo
	 * @return	retorna true si los nodos no estaban unidos previamente, en caso de que ya estuvieran unidos retorna falso
	 */
	public boolean linkNodes(int a, int b) {
		return linkNodes(new Edge(a,b));
	}

	/**
	 * Función para enlazar dos nodos
	 * @param a	identificador del primer nodo
	 * @param b	identificador del segundo nodo
	 * @param w valor del peso del enlace
	 * @return	retorna true si los nodos no estaban unidos previamente, en caso de que ya estuvieran unidos retorna falso
	 */
	public boolean linkNodes(int a, int b, int w) {
		return linkNodes(new Edge(a,b,w));
		
	}
	
	
	/**
	 * Función para enlazar dos nodos
	 * @param e	arista del enlace, esta contiene la informacion de los nodos a unir y el peso del enlace
	 * @return	retorna true si los nodos no estaban unidos previamente, en caso de que ya estuvieran unidos retorna falso
	 */
	public boolean linkNodes(Edge e) {
		if (!nodes.containsKey(e.a)  ) {
			this.addNode(e.a);
		}
		if (!nodes.containsKey(e.b)) {
			this.addNode(e.b);
		}
		if(dirigido) {
			if(this.isLinked(e.a, e.b)) {
				return false;
			}else {
				nodes.get(e.a).add(e);
				return true;
			}
		}else {
			if (this.isLinked(e.a, e.b)) {
				return false;
			}else {
				Edge f=new Edge(e.b,e.a,e.w);
				nodes.get(e.a).add(e);
				nodes.get(e.b).add(f);
				return true;
			}
			
		}
		
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
	
	public static void debug(String s) {
		System.out.println(s);
	}
	
	
	/**
	 * Funcion para cargar un grafo desde un archivo
	 * @param filename Nombre del archivo
	 * @return instancia tipo Graph
	 */
	public static Graph loadFile(String filename) {
		Graph g=null;
		boolean dirigido=false;
		try{	
			File file=new File(filename);
			FileReader fr=new FileReader(file);  
			BufferedReader br=new BufferedReader(fr);
			String line;
			String[] n,e;
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
					line=line.replace(";", "").trim();
					if(line.contains("-")) {
						if(dirigido) {
							n=line.split("->");
							e=n[1].trim().split(" ");
							e[1]=e[1].replace("[label=\"", "");
							e[1]=e[1].replace("\"]", "");
							//Graph.debug("n1="+n[1] + "e0= "+e[0]+" e1="+e[1]);
							g.linkNodes(Integer.parseInt(n[0].trim()), Integer.parseInt(e[0].trim()),Integer.parseInt(e[1].trim()));
						}else {
							n=line.split("--");
							e=n[1].trim().split(" ");
							e[1]=e[1].replace("[label=\"", "");
							e[1]=e[1].replace("\"]", "");
							g.linkNodes(Integer.parseInt(n[0].trim()), Integer.parseInt(e[0].trim()),Integer.parseInt(e[1].trim()));
						}
					}else {
						e=line.split(" ");
						e[1]=e[1].replace("[label=\"", "");
						e[1]=e[1].replace("\"]", "");
						g.addNode(Integer.parseInt(e[0]),e[1]);
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
		
		ArrayList<Integer> queue;
		Graph G = new Graph();
		int i;
		HashMap<Integer, Boolean> explorados = new HashMap<>();
		for ( int node : S.nodes.keySet() ) {
			explorados.put(node,false);
		}
		queue = new ArrayList<Integer>();
		queue.add(n);
		explorados.put(n, true);
		G.addNode(n);
		
		while(queue.size() != 0) {
			i = queue.get(0);
			queue.remove(0);
			Set<Edge>neighbors=S.getNeighbors(i);
			for (Edge value : neighbors) {
				if(!explorados.get(value.b)) {
					G.linkNodes(i,value.b,value.w);
					explorados.put(value.b, true);
					queue.add(value.b);
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
		HashMap<Integer, Boolean> explorados = new HashMap<>();
		for ( int node : S.nodes.keySet() ) {
			explorados.put(node,false);
		}
		
		ArrayList<Integer> visitados,por_visitar;
		por_visitar = new ArrayList<Integer>();
		visitados = new ArrayList<Integer>();
		Set<Edge>neighbors;
		
		boolean backwards=false;
		Graph G = new Graph();
		
		por_visitar.add(n);
		G.addNode(n);
		
		while(por_visitar.size() != 0) {
			j = por_visitar.size()-1;
			i = por_visitar.get(j);
			por_visitar.remove(j);
			if(explorados.get(i)) {
				continue;
			}
			explorados.put(i, true);
			visitados.add(i);
			backwards=true;
			
			neighbors=S.getNeighbors(i);
			for (Edge value : neighbors) {
				if(!explorados.get(value.b)) {
					backwards=false;
					por_visitar.add(value.b);
				}
			}
			while( (visitados.size() > 1) && backwards) {
				i = visitados.size()-1;
				j= visitados.get(i-1);
				G.linkNodes(visitados.get(i), j);
				visitados.remove(i);
				neighbors=S.getNeighbors(j);
				for (Edge value : neighbors) {
					if(!explorados.get(value.b)) {
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
		HashMap<Integer, Boolean> explorados = new HashMap<>();
		for ( int node : S.nodes.keySet() ) {
			explorados.put(node,false);
		}
		explorados.put(n, true);
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
	private static void getDFSr(int n,Graph S, Graph G, HashMap<Integer,Boolean> explorados) {
		Set<Edge>neighbors=S.getNeighbors(n);
		for (Edge value : neighbors) {
			if(!explorados.get(value.b)) {
				explorados.put(value.b,true);
				G.linkNodes(n,value.b,value.w);
				getDFSr(value.b,S,G,explorados);
			}
		}
	}
	
	/**
	 * Función para obtener el arbol de distancia mínima dado un nodo fuente.
	 * Está basado en el algoritmo BFS para la exploración de nodos.
	 * @param n identificador del nodo fuente
	 * @return Instancia tipo Graph cuyas etiquetas de los nodos estan acompañadas de la distancia minima al nodo fuente.
	 */
	public Graph getDijkstra(int n) {
		return Graph.getDijkstra(this, n);
	}
	public static Graph getDijkstra(Graph S,int n) {
		Graph G = new Graph(S.dirigido);
		if(!S.existsNode(n)) {
			System.err.println("El nodo raiz no existe");
			return null;
		}
		
		HashMap<Integer, Integer> distancia = new HashMap<>();
		for ( int node : S.nodes.keySet() ) {
			distancia.put(node,Integer.MAX_VALUE);
		}
		
		ArrayList<Integer> queue;
		
		int i,dist;
		HashMap<Integer, Boolean> explorados = new HashMap<>();
		for ( int node : S.nodes.keySet() ) {
			explorados.put(node,false);
		}
		queue = new ArrayList<Integer>();
		queue.add(n);
		explorados.put(n, true);
		distancia.put(n, 0);
		//G.addNode(n);
		
		while(queue.size() != 0) {
			i = queue.get(0);
			queue.remove(0);
			Set<Edge>neighbors=S.getNeighbors(i);
			for (Edge value : neighbors) {
				G.linkNodes(i, value.b,value.w);
				dist=distancia.get(i);
				if ((dist+S.getW(i,value.b)) < distancia.get(value.b)) {
					distancia.put(value.b, dist+S.getW(i,value.b));
				}
				if(!explorados.get(value.b)) {
					explorados.put(value.b, true);
					queue.add(value.b);
				}
			}
		}


		for ( int node : G.nodes.keySet() ) {
			G.labels.put(node,node+"_"+distancia.get(node));
		}
		
		return G;
	}
	
	/**
	 * Obtiene el componente conectado de un grafo a partir de un nodo fuente.
	 * @param n identificador del nodo fuente
	 * @return Instancia tipo Graph
	 */
	public Graph getConnectedGraph(int n){
		return Graph.getConnectedGraph(this, n);
	}
	
	/**
	 * Obtiene el componente conectado de un grafo a partir de un nodo fuente.
	 * @param S Grafo del cual se obtendrá el componente conectado
	 * @param n identificador del nodo fuente
	 * @return Instancia tipo Graph
	 */
	public static Graph getConnectedGraph(Graph S, int n){
		if(!S.existsNode(n)) {
			System.err.println("El nodo raiz no existe");
			return null;
		}
		
		ArrayList<Integer> queue;
		Graph G = new Graph(S.dirigido);
		int i;
		HashMap<Integer, Boolean> explorados = new HashMap<>();
		for ( int node : S.nodes.keySet() ) {
			explorados.put(node,false);
		}
		queue = new ArrayList<Integer>();
		queue.add(n);
		explorados.put(n, true);
		G.addNode(n);
		
		while(queue.size() != 0) {
			i = queue.get(0);
			queue.remove(0);
			Set<Edge>neighbors=S.getNeighbors(i);
			for (Edge value : neighbors) {
				G.linkNodes(i,value.b,value.w);
				if(!explorados.get(value.b)) {
					explorados.put(value.b, true);
					queue.add(value.b);
				}
			}
		}
		return G;
	}
	
	/**
	 * Funcion para unir los conjuntos de dados dos nodos. Para representar un conjunto
	 * se utilizó una tabla hash donde la key es el identificador del nodo
	 * y el value el valor del conjunto al cual pertenece.
	 * @param conj tabla hash con la información del conjunto
	 * @param a identificador del nodo a
	 * @param b identificador del nodo b
	 */
	private static void Union(HashMap<Integer,Integer> conj,int a,int b){
		a=conj.get(a);
		b=conj.get(b);
		for (int c:conj.keySet()) {
			if(conj.get(c)==b) {
				conj.put(c, a);
			}
		}
	}
	
	/**
	 * Funcion para obtener el grafo arbol MST usando el algoritmo Kruskal
	 * @param n Identificador del nodo para obtener su componente conectado antes de aplicar el algoritmo
	 * @return Instancia tipo grafo con el arbol MST
	 */
	public Graph getKruskal(int n) {
		return Graph.getKruskal(this,n);
	}
	
	/**
	 * Funcion para obtener el grafo arbol MST usando el algoritmo Kruskal
	 * @param S arbol al cual se aplica el algoritmo
	 * @param n Identificador del nodo para obtener su componente conectado antes de aplicar el algoritmo1
	 * @return Instancia tipo grafo con el arbol MST
	 */
	public static Graph getKruskal(Graph S,int n) {
		S=S.getConnectedGraph(n);
		Graph G = new Graph(S.dirigido);
		ArrayList<Edge> edges = S.getEdges();
		
		HashMap<Integer, Integer> conjunto = new HashMap<>();
		for ( int node : S.nodes.keySet() ) {
			conjunto.put(node,node);
		}
		for (Edge edge: edges) {
			if( (conjunto.get(edge.a)-conjunto.get(edge.b))!=0) {
				G.linkNodes(edge);
				Union(conjunto,edge.a,edge.b);
			}
		}
		return G;
	}
	
	
	/**
	 * Funcion para comprobar si estan conectados dos nodos
	 * @param a identificador del nodo a
	 * @param b identificador del nodo b
	 * @return retorna true si estan conectados o false de lo contrario.
	 */
	public boolean isConnected(int a,int b) {
		return Graph.isConnected(this, a, b);
	}
	
	/**
	 * Funcion para comprobar si estan conectados dos nodos.
	 * Está basado en el algoritmo BFS para exploración de grafos
	 * @param S Grafo desde el cual se hace el recorrido.
	 * @param a identificador del nodo a
	 * @param b identificador del nodo b
	 * @return retorna true si estan conectados o false de lo contrario.
	 */
	public static boolean isConnected(Graph S,int a,int b) {
		if(!S.existsNode(a) || !S.existsNode(b) ) {
			System.err.println("El nodo raiz no existe");
			return false;
		}
		
		HashMap<Integer, Boolean> explorados = new HashMap<>();
		for ( int node : S.nodes.keySet() ) {
			explorados.put(node,false);
		}
		
		ArrayList<Integer> queue;
		int i;
		queue = new ArrayList<Integer>();
		queue.add(a);
		explorados.put(a, true);
		
		while(queue.size() != 0) {
			i = queue.get(0);
			queue.remove(0);
			Set<Edge>neighbors=S.getNeighbors(i);
			for (Edge value : neighbors) {
				if(!explorados.get(value.b)) {
					if(b==value.b) {
						return true;
					}
					explorados.put(value.b, true);
					queue.add(value.b);
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Funcion para obtener el arbol MST de un grafo usando el
	 * algoritmo de kruskal inverso
	 * @param n identificador del nodo fuene para obtener primero el
	 * 	componente conectado del grafo.
	 * @return Instancia tipo Graph con el arbol MST
	 */
	public Graph getKruskal_i(int n) {
		return Graph.getKruskal_i(this,n);
	}
	
	/**
	 * Funcion para obtener el arbol MST de un grafo usando el
	 * algoritmo de kruskal inverso
	 * @param S grafo al cual obtener su MST
	 * @param n identificador del nodo fuene para obtener primero el
	 * 	componente conectado del grafo.
	 * @return Instancia tipo Graph con el arbol MST
	 */
	public static Graph getKruskal_i(Graph S,int n) {
		S=S.getConnectedGraph(n);
		Graph G = S.clone();
		ArrayList<Edge> edges = G.getEdges(false);
		
		
		for (Edge edge: edges) {
			G.dropEdge(edge);
			if( !G.isConnected(edge.a,edge.b) ) {
				G.linkNodes(edge);
			}
		}
		return G;
	}
	

	/**
	 * Funcion para obtener la arista con el peso minimo dado un 
	 * ArrayList de aristas
	 * @param edges ArrayList con aristas
	 * @return Arista con el peso minimo
	 */
	private static Edge min_edge(ArrayList<Edge> edges) {
		Edge min=edges.get(0);
		for (Edge e:edges) {
			if(e.w<min.w) {
				min=e;
			}
		}
		return min;
	}
	
	
	/**
	 * Funcion para obtener el arbol de expansion mínima usando el algoritmo de Prim.
	 * @param S 
	 * @param n
	 * @return
	 */
	public Graph getPrim(int n){
		return getPrim_variante(this,n);
	}
	
	
	/**
	 * Funcion para obtener el arbol de expansion mínima usando el algoritmo de Prim.
	 * Lo nombré variante porque no es igual al algoritmo del video
	 * pero si es igual a la prueba de escritorio que realizó. 
	 * @param S Grafo del cual obtener su MST
	 * @param n identificador del nodo con el que comenzará el algoritmo
	 * @return instancias tipo Graph con el MST
	 */
	public static Graph getPrim_variante(Graph S,int n) {
		Graph G = new Graph(S.dirigido);
		
		if(!S.existsNode(n) ) {
			System.err.println("El nodo raiz no existe");
			return null;
		}
		
		HashMap<Integer, Boolean> explorados = new HashMap<>();
		for ( int node : S.nodes.keySet() ) {
			explorados.put(node,false);
		}
		ArrayList<Edge> queue= new ArrayList<Edge>();
		explorados.put(n, true);
		
		Set<Edge>neighbors=S.getNeighbors(n);
		for (Edge edge : neighbors) {
			queue.add(edge);
		}
		Edge i;
		
		while(queue.size() > 0) {
			i = min_edge(queue);
			queue.remove(i);
			G.linkNodes(i);
			explorados.put(i.b, true);
			neighbors=S.getNeighbors(i.b);
			for (Edge edge : neighbors) {
				if(!explorados.get(edge.b)) {
					queue.add(edge);
				}
			}
			for (Edge edge : queue) {
				if (edge.b==i.b) {
					queue.remove(edge);
				}
			}
		}
		
		return G;
	}
	
	
	/**
	 * funcion que retorna un String con el formato GraphViz 
	 * @return String con formato de graph viz
	 */
	public String toGrahpViz() {
		String res="";
		String arista;
		
		for ( int node : nodes.keySet() ) {
			res+="  "+node+" [label=\""+labels.get(node)+"\"];\n";
		}
		if(dirigido) {
			arista=" -> ";
			res="digraph {\n"+res;
			for (Map.Entry<Integer,Set<Edge>> n : nodes.entrySet()) {
				int node=n.getKey();
				for (Edge value : n.getValue()) {
					res+="  "+node+arista+value.b+" [label="+value.w+"];\n";
				}
		    }
		}else {
			arista=" -- ";
			res="graph {\n"+res;
			for (Map.Entry<Integer,Set<Edge>> n : nodes.entrySet()) {
				int node=n.getKey();
				for (Edge value : n.getValue()) {
					if (node<value.b) {
						res+="  "+node+arista+value.b+" [label=\""+value.w+"\"];\n";
					}
				}
		    }
		}
		
		res+="}";
		return res;
	}
}