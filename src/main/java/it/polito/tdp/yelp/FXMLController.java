/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
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

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Business> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Business> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	String citta = this.cmbCitta.getValue();
    	
    	model.creaGrafo(citta);
    	
    	this.txtResult.appendText("Numero vertici: "+model.getNumVertici());
    	this.txtResult.appendText("\nNumero archi : "+model.getNumEdges());

    	
    	this.cmbB1.getItems().addAll(model.getAllLocali()); 
    	this.cmbB2.getItems().addAll(model.getAllLocali()); 
    	
    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
    	Business locale = this.cmbB1.getValue();
    	
    	this.txtResult.appendText("\nIl locale piu distante : "+model.getLocaleDistante(locale)+" - Peso: "+model.getPesoVerticeDistante() );

    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	Business locale = this.cmbB1.getValue();
    	Business locale2 = this.cmbB2.getValue();
    	double soglia= Double.parseDouble(txtX2.getText()); 
    	
    	model.tourGastronomico(locale, locale2, soglia); 
    	this.txtResult.appendText("\nricorsione finita, risultato: "); 
    	this.txtResult.appendText("\n"+model.tourGastronomico(locale, locale2, soglia) );
    	this.txtResult.appendText(locale2+"\n");

    	this.txtResult.appendText("\nkm percorsi: "+model.getKmPercorsiFinali()); 



    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	setCmbox(); 
    }
    
    public void setCmbox() {
    	this.cmbCitta.getItems().addAll(model.allCities()); 
    }
}











