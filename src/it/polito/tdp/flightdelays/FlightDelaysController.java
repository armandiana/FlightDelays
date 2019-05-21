

/**
 * Sample Skeleton for 'ExtFlightDelays.fxml' Controller Class
 */

package it.polito.tdp.flightdelays;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.extflightdelays.model.Airport;
import it.polito.tdp.extflightdelays.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
/**
 * Sample Skeleton for 'FlightDelays.fxml' Controller Class
 */



public class FlightDelaysController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="distanzaMinima"
    private TextField distanzaMinima; // Value injected by FXMLLoader

    @FXML // fx:id="btnAnalizza"
    private Button btnAnalizza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoPartenza"
    private ComboBox<String> cmbBoxAeroportoPartenza; // Value injected by FXMLLoader

    @FXML // fx:id="cmbBoxAeroportoArrivo"
    private ComboBox<String> cmbBoxAeroportoArrivo; // Value injected by FXMLLoader

    @FXML // fx:id="btnAeroportiConnessi"
    private Button btnAeroportiConnessi; // Value injected by FXMLLoader

    @FXML
    void doAnalizzaAeroporti(ActionEvent event) {
    	//crea un grafo che contiene tutti i voli che collegano almeno due areoporti che hanno distanza minima come quella inserita.
    	this.txtResult.clear();
    	
    	try {
        	Integer distanza=Integer.parseInt(this.distanzaMinima.getText());

    		this.model.creaGrafo(distanza);
    		this.txtResult.appendText("Ho creato il grafo con distanza minima: "+distanza+" miglia \n"+
    		                            "Formato da "+this.model.getGrafo().vertexSet().size()+" vertici e "+
    				                     this.model.getGrafo().edgeSet().size()+" archi. \n "+this.model.getGrafo().toString());
    		
    	}catch(NumberFormatException e) {
    		
    		e.printStackTrace();
    		this.txtResult.appendText("Avete inserito un formato non valido. Riprova! \n");
    	}

    }

    @FXML
    void doTestConnessione(ActionEvent event) {
    	this.txtResult.appendText("*** Testiamo la connessone tra i due areoporti *** \n");
    	Boolean esisteConnessione=null;
    	try {
    		Integer a1=Integer.parseInt(this.cmbBoxAeroportoPartenza.getValue());
    		Integer a2=Integer.parseInt(this.cmbBoxAeroportoArrivo.getValue());
    		
    		esisteConnessione=this.model.testConnessione(a1, a2);
    		List<Airport>percorso=this.model.trovaPercorso(a1, a2);
    		
    		if(esisteConnessione) {
    			this.txtResult.appendText("E' possibile raggiungere l'areoporto "+this.cmbBoxAeroportoArrivo.getValue()+
    					" dall'areoporto "+this.cmbBoxAeroportoPartenza.getValue()+"\n");
    			if(percorso!=null)
    			this.txtResult.appendText("Un possibile percorso tra i due areoporti potrebbe essere: "+percorso.toString()+"\n");
    			
    		}else if(!esisteConnessione){
    			this.txtResult.appendText("I due areoporti non sono collegati \n");
    		}
    		
    	}catch(NumberFormatException e) {
    		e.printStackTrace();
    		this.txtResult.appendText("Avete inserito un formato non valido. Riprova! \n");
    		throw new RuntimeException("Errore durante l'esecuzione del programma! \n");
    		
    	}

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert distanzaMinima != null : "fx:id=\"distanzaMinima\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert btnAnalizza != null : "fx:id=\"btnAnalizza\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxAeroportoPartenza != null : "fx:id=\"cmbBoxAeroportoPartenza\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert cmbBoxAeroportoArrivo != null : "fx:id=\"cmbBoxAeroportoArrivo\" was not injected: check your FXML file 'FlightDelays.fxml'.";
        assert btnAeroportiConnessi != null : "fx:id=\"btnAeroportiConnessi\" was not injected: check your FXML file 'FlightDelays.fxml'.";

    }
    
    public void setModel(Model model) {
		this.model = model;
	
		for(Airport a: this.model.loadAllAirports()) {
			this.cmbBoxAeroportoPartenza.getItems().add(String.valueOf(a.getId()));
			this.cmbBoxAeroportoArrivo.getItems().add(String.valueOf(a.getId()));
		}
	}
}




