package it.polito.tdp.yelp.model;

import java.util.*;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	YelpDao dao; 
    private SimpleWeightedGraph<Business, DefaultWeightedEdge> graph;  // SEMPLICE, PESATO, NON ORIENTATO
    private List<Business> allNegozi ; 
    double pesoVerticeDistante = 0.0; 
	double kmPercorsiFinali= 0; 


	
	
	public Model() {
		this.dao= new YelpDao();  
    	this.graph= new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    	this.allNegozi= new ArrayList<>(); 
    	
		
	}
	
	public List<String> allCities() {
		return dao.getAlCities(); 
	}
	
	private void loadAllNodes(String citta) {
		this.allNegozi= dao.getAllBusinessCitta(citta);

		
	}	
	
    public void creaGrafo(String citta) {
		 
		 
		 loadAllNodes(citta); 

		 /** VERTICI */
	    	Graphs.addAllVertices(this.graph, allNegozi);
	 		System.out.println("NUMERO vertici GRAFO: " +this.graph.vertexSet().size());
	 	
	 		
	 	/*
	il peso è pari alla distanza tra i due locali, espressa in km. 
	Per calcolare tale distanza si faccia riferimento alle colonne l
	atitude e longitude della tabella Business, sfruttando la libreria 
	simplelatlng1 già inclusa nel progetto. 	
	 	 */
	 		
	 		
	 	
	 	/** ARCHI */
	 		for(Business b1: allNegozi) {
	 			for(Business b2: allNegozi) {
	 				
	 				if(b1.getBusinessId().compareTo(b2.getBusinessId())!=0) {
	 					double peso= calcolaPeso(b1,b2); 
	 						Graphs.addEdgeWithVertices(this.graph, b1, b2, peso);

	 				}
	 			}
	 		}
	 		
 	 		System.out.println("\nnumero ARCHI: "+ this.graph.edgeSet().size());
		
    }
    
    public List<Business> getAllLocali() {
    	return allNegozi; 
    }
    
    public Business getLocaleDistante(Business b) { // trova tra i vertici adiacenti quello con peso maggiore
    	
    	// List<Business> listAdiacenti= Graphs.neighborListOf(this.graph, b); // vertici (NO GOOD)
    	Set<DefaultWeightedEdge> listAdiacenti= graph.edgesOf(b); //set di EDGES che toccano b
    
    	Business result= null;
    	double pesoMax=0;
    	DefaultWeightedEdge edgeMAx= null; 
    	
    	for(DefaultWeightedEdge ee: listAdiacenti) {
    		
    		if(graph.getEdgeWeight(ee) >pesoMax) {
    			pesoMax= graph.getEdgeWeight(ee);
    			edgeMAx= ee; 
    		}
    	}
    	
    	result= Graphs.getOppositeVertex(this.graph, edgeMAx, b); 
    	pesoVerticeDistante= this.graph.getEdgeWeight(edgeMAx) ;
    	this.setPesoVerticeDistante(pesoMax);
    	
    	return result; 
    }

	private double calcolaPeso(Business b1, Business b2) {
		
		double distanza=0; 
		
		LatLng lng1= dao.getLatLang(b1);
		LatLng lng2= dao.getLatLang(b2);
		
		if(lng1!= null && lng2!= null)
	        distanza = LatLngTool.distance(lng1,lng2,LengthUnit.KILOMETER) ;
	       
		return distanza;
	}

	
	private List<Business> parziale; 
	private List<Business> percorsoOttimale; 
	//private int dimParzialeMax =0; 
	 Map <String,Double> mapRecensRistoranti; 
	
	double kmPercorsi= 0; 
	//int totLocalita; 
	
	
	public List<Business> tourGastronomico(Business bStart, Business bStop, double soglia) { // b1, b2
	
	 //List<Business> ristoranti= new ArrayList<>(this.allNegozi);  
	 
	 parziale= new ArrayList<Business>() ; // qui inseriro' tutti i vari vertici scelti
	 parziale.add(bStart); 
		
	 percorsoOttimale= new ArrayList<Business>(); 
	// percorsoOttimale.add(bStart); 
	 
	 mapRecensRistoranti = dao.getAllAvgStar();

	 
	  ricorsione( parziale,kmPercorsi ,soglia,bStop); 
	  //this.percorsoOttimale.add(bStop); 

		
		return percorsoOttimale;
		
	}

	private void ricorsione(List<Business> parziale, double kmPercorsi,  double soglia, Business bStop) {
		
		Business current = parziale.get(parziale.size()-1);

		/** ***  condizione di uscita  ***/
		 if(current.getBusinessId().compareTo(bStop.getBusinessId())==0) {  	
			
			 kmPercorsiFinali += graph.getEdgeWeight(graph.getEdge(current, bStop)); 
			 this.setKmPercorsiFinali(kmPercorsiFinali);
			//  percorsoOttimale.add(bStop);
			 
			 return; 
		  }
		
		
		  /** 2. qui trovo la SOLUZIONE MIGLIORE **/ 
		if(parziale.size()> percorsoOttimale.size()) {
			
			this.percorsoOttimale = new ArrayList<>(parziale); 
			kmPercorsiFinali= kmPercorsi;
			//this.setKmPercorsiFinali(kmPercorsiFinali);
			
		}
		
		
		List<Business> successori= Graphs.successorListOf(graph, current);
		List<Business> newSuccessori= new ArrayList<>();  
	
		
		for(Business ii: successori) {
		    if(!parziale.contains(ii)) {
		    	newSuccessori.add(ii);  // QUI METTO SOLO I VERTICI CHE NON SONO GIA' STATI USATI
		    }
	  }

		
		for(Business bb: newSuccessori) {
			
		  double temp= mapRecensRistoranti.get(bb.getBusinessId());// qui prendo l'avg Star
		
			if(temp > soglia  && temp!= 0.0) {
				
				kmPercorsi += graph.getEdgeWeight(graph.getEdge(current, bb)); 
				parziale.add(bb);
				
				ricorsione(parziale, kmPercorsi,soglia ,bStop); 
				
				parziale.remove(bb);
				kmPercorsi -= graph.getEdgeWeight(graph.getEdge(current, bb)); 
				
			
		 }
		}
	}

	/* 
	 
	- passi solo per locali commerciali per cui la media di recensioni sia maggiore di x.   (esclusi b1 e b2),
      con media di recensioni su Yelp (colonna stars, tabella Business) maggiore di una determinata soglia x
        
    - visiti il maggior numero possibile di locali commerciali. 
     
    - STAMPA i  km percorsi in totale 
	  
	 */
	
	
	public int getNumVertici() {
		
		return this.graph.vertexSet().size();
	}

	public int getNumEdges() {
		return this.graph.edgeSet().size();
	}

	public double getPesoVerticeDistante() {
		return pesoVerticeDistante;
	}

	public void setPesoVerticeDistante(double pesoVerticeDistante) {
		this.pesoVerticeDistante = pesoVerticeDistante;
	}

	public double getKmPercorsiFinali() {
		return kmPercorsiFinali;
	}

	public void setKmPercorsiFinali(double kmPercorsiFinali) {
		this.kmPercorsiFinali = kmPercorsiFinali;
	}
	

	
	
	
	
}
