
public class Index {

	public static void main(String[] args) {
		
		Graph g= Graph.genErdosRenyi(30, 30,false,null);
		g.saveFile("erdosRenyi_30.gv");
		
		g= Graph.genErdosRenyi(100, 100,false,null);
		g.saveFile("erdosRenyi_100.gv");
		
		g= Graph.genErdosRenyi(500, 500,false,null);
		g.saveFile("erdosRenyi_500.gv");
		
		g= Graph.genGilbert(30, 0.05,false,null);
		g.saveFile("gilbert_30.gv");
		
		g= Graph.genGilbert(100, 0.01,false,null);
		g.saveFile("gilbert_100.gv");
		
		g= Graph.genGilbert(500, 0.002,false,null);
		g.saveFile("gilbert_500.gv");
		
		g= Graph.genGeografico(30, 0.2,false,null);
		g.saveFile("geografico_30.gv");
		
		g= Graph.genGeografico(100, 0.15,false,null);
		g.saveFile("geografico_100.gv");
		
		g= Graph.genGeografico(500, 0.1,false,null);
		g.saveFile("geografico_500.gv");
		
		g= Graph.genBarabasiAlbertVariantRandom(30, 5,false,null);
		g.saveFile("barabasi_30.gv");
		
		g= Graph.genBarabasiAlbertVariantRandom(100, 5,false,null);
		g.saveFile("barabasi_100.gv");
		
		g= Graph.genBarabasiAlbertVariantRandom(500, 5,false,null);
		g.saveFile("barabasi_500.gv");
		
		
		
		
		
	}

}
