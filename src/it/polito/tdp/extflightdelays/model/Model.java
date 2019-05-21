package it.polito.tdp.extflightdelays.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.extflightdelays.db.ExtFlightDelaysDAO;

public class Model {

	private SimpleWeightedGraph<Airport, DefaultWeightedEdge>grafo;
	private Map<Integer, Airport>aIdMap;
	private Map<Airport, Airport>visita;
	private List<Airport>tuttiAreoporti;
	private ExtFlightDelaysDAO dao=new ExtFlightDelaysDAO();


	
	public Model() {
		grafo= new SimpleWeightedGraph<Airport, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		aIdMap=new HashMap<Integer, Airport>();
		visita= new HashMap<Airport, Airport>(); //relazioni padre e figlio.
	}
	
	
	public void creaGrafo(Integer distanzaMedia) {
	    dao=new ExtFlightDelaysDAO();
		dao.loadAllAirports(aIdMap);
		
		//aggiungere i vertici
		Graphs.addAllVertices(grafo, aIdMap.values());
		
		for(Rotta rotta:dao.getRotte(aIdMap, distanzaMedia)) {
			//controlllo se esite gia un arco
			//se esiste, aggiorno il peso
			DefaultWeightedEdge edge=grafo.getEdge(rotta.getPartenza(), rotta.getDestinazione());
			if(edge==null) {
			Graphs.addEdge(grafo, rotta.getPartenza(), rotta.getDestinazione(), rotta.getDistanzaMedia());// MA QUESTO NON MODELLA CORRETTAMENTE L'ARCO PERCHè COLLEGA SOLO L'AREOPORTO A a B E NON DA B a A.
			}else {
				double peso=grafo.getEdgeWeight(edge);
				double newPeso=(peso+rotta.getDistanzaMedia())/2;
				System.out.println("Aggiornare peso! Peso vecchio: "+peso+" Peso nuovo: "+newPeso);

				grafo.setEdgeWeight(edge, newPeso);
			}
		}
		System.out.println("Grafo creato!");
		System.out.println("Vertici: "+grafo.vertexSet().size());
		System.out.println("Archi: "+grafo.edgeSet().size());
	}
	
	public Boolean testConnessione(Integer a1, Integer a2) {//di solito il per punto d non sarà necessario scrivere molte righe di codice, quindi se stiamo scrivendo molte righe, stiamo sbagliando qualcosa.
		Set<Airport>visitati=new HashSet<>();
		
		Airport partenza=aIdMap.get(a1);
		Airport destinazione=aIdMap.get(a2);
		
		System.out.println("Testo la connessione tra "+partenza+" e "+destinazione);
		
		BreadthFirstIterator<Airport, DefaultWeightedEdge>it= new BreadthFirstIterator<Airport, DefaultWeightedEdge>(grafo,partenza);
		//con la visita in ampiezza si trova il percorso con il minimo degli archi.
		
		while(it.hasNext()) {
			visitati.add(it.next());
		}
		if(visitati.contains(destinazione)) {
			return true;
		}else {
			return false;
		}
		
	}
	
	public List<Airport>trovaPercorso(Integer a1, Integer a2){
		List<Airport>percorso=new ArrayList<>();
		
		 Airport partenza=aIdMap.get(a1);
		 Airport destinazione=aIdMap.get(a2);
		 
		 BreadthFirstIterator<Airport,DefaultWeightedEdge>it= new BreadthFirstIterator<Airport, DefaultWeightedEdge>(this.grafo, partenza);
		 
		 visita.put(partenza, null);
		 it.addTraversalListener(new TraversalListener<Airport, DefaultWeightedEdge>() {
			
			@Override
			public void vertexTraversed(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void vertexFinished(VertexTraversalEvent<Airport> arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void edgeTraversed(EdgeTraversalEvent<DefaultWeightedEdge> e) {
			
				Airport sorgente=grafo.getEdgeSource(e.getEdge());
				Airport destinazione=grafo.getEdgeTarget(e.getEdge());
				
				if(!visita.containsKey(destinazione)&& visita.containsKey(partenza)) {
					visita.put(destinazione, sorgente);
				}else if(!visita.containsKey(sorgente)&& visita.containsKey(destinazione)) {
					visita.put(sorgente, destinazione);
				}
			}
			
			@Override
			public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		 
		 while(it.hasNext()) {
			 it.next();
		 }//non mi serve salvare nulla perche il salvataggio lo faccio già nel metodo.
		 
		 if(!visita.containsKey(partenza)||!visita.containsKey(destinazione)) {
			 return null; //gli areoporti non sono collegati
		 }
		 
		 Airport step=destinazione;
		 while(!step.equals(partenza)) {
			 percorso.add(step);
			 step=visita.get(step);
		 }
		 
		 return percorso;
	}
	
	public List<Airport> loadAllAirports() {
		tuttiAreoporti=dao.loadAllAirports();
		return tuttiAreoporti;
	}


	public SimpleWeightedGraph<Airport, DefaultWeightedEdge> getGrafo() {
		return grafo;
	}
	
	
	
	

}
