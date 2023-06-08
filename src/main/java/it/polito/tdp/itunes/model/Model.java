package it.polito.tdp.itunes.model;

import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;


import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private Graph<Album, DefaultEdge> grafo;
	private Map<Integer, Album> mappaAlbum;

	public Model() {
		super();
		this.dao = new ItunesDAO();
		this.mappaAlbum = this.dao.getMappaAlbum();
	} 
	
	public void creaGrafo(int durata) {
		
		this.grafo = new SimpleGraph<Album, DefaultEdge>(DefaultEdge.class);
		
		List<Album> vertici = this.dao.getVertici(durata*60000);
		
		Graphs.addAllVertices(grafo, vertici);
		System.out.println(grafo.vertexSet().size());
		
		List<Edge> archi = this.dao.getEdge(durata*60000, mappaAlbum);
		
		for(Edge e: archi) {
			this.grafo.addEdge(e.getA1(), e.getA2());
		}
		
		System.out.println(grafo.edgeSet().size());
		
		
	}
	
	public List<Album> getTendina(int durata){
		return this.dao.getVertici(durata*60000);
	}
	
	public int dimConnessa(Album a) {
		ConnectivityInspector<Album, DefaultEdge> ci = 
				new ConnectivityInspector<Album, DefaultEdge>(this.grafo);
		
		return ci.connectedSetOf(a).size();
	}

	public int getNumVertici() {
		
		return this.grafo.vertexSet().size();
	}

	public int getNumArchi() {
		
		return this.grafo.edgeSet().size();
	}
	
	public int minConnessa (Album a) {
		ConnectivityInspector<Album, DefaultEdge> ci = 
				new ConnectivityInspector<Album, DefaultEdge>(this.grafo);
		
		double tot = 0;
		
		for(Album aa: ci.connectedSetOf(a)) {
			tot += aa.getDurata();
		}
		
		return (int)(tot/60000);
	}
	
	
}
