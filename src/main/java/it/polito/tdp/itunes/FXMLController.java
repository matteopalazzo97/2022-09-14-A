/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Album;
import it.polito.tdp.itunes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnComponente"
    private Button btnComponente; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnSet"
    private Button btnSet; // Value injected by FXMLLoader

    @FXML // fx:id="cmbA1"
    private ComboBox<Album> cmbA1; // Value injected by FXMLLoader

    @FXML // fx:id="txtDurata"
    private TextField txtDurata; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML // fx:id="txtX"
    private TextField txtX; // Value injected by FXMLLoader

    @FXML
    void doComponente(ActionEvent event) {
    	
    	Album album;
    	
    	if(this.cmbA1.getValue() == null) {
    		this.txtResult.appendText("\nSelezionare un valore dal menù a tendina");
    		return;
    	}
    	
    	album = this.cmbA1.getValue();
    	
    	this.txtResult.appendText("\nDimensione della componente connessa a cui appartiene " + 
    								album.toString() + ": " + this.model.dimConnessa(album)); 
    	
    	this.txtResult.appendText("\nDurata componente connessa: " + this.model.minConnessa(album) +
    								" min.");		
    	
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	this.txtResult.clear();
    	
    	int durata;
    	
    	if(this.txtDurata.getText() == null) {
    		this.txtResult.setText("Inserire un valore di durata.");
    		return;
    	}
    	try {
    		durata = Integer.parseInt(this.txtDurata.getText());
    	} catch(NumberFormatException e) {
    		e.printStackTrace();
    		this.txtResult.setText("Inserire un valore numerico per la durata.");
    		return;
    	}
    	
    	this.model.creaGrafo(durata);
    	
    	this.txtResult.appendText("Grafo creato.");
    	this.txtResult.appendText("\nVertici: " + this.model.getNumVertici());
    	this.txtResult.appendText("\nArchi: " + this.model.getNumArchi());

    	
    	this.cmbA1.getItems().addAll(this.model.getTendina(durata));
    	
    	
    }

    @FXML
    void doEstraiSet(ActionEvent event) {

    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnComponente != null : "fx:id=\"btnComponente\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSet != null : "fx:id=\"btnSet\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbA1 != null : "fx:id=\"cmbA1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDurata != null : "fx:id=\"txtDurata\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX != null : "fx:id=\"txtX\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    }

}
