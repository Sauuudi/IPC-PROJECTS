/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica7;

import java.net.URL;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.concurrent.Task;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;


/**
 *
 * @author Mystubis
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Button mostrarButton;
    @FXML
    private Button pararButton;
    @FXML
    private TextField text;
    
    private getHora gh;
    
    public class getHora extends Task<LocalTime>{
        public getHora(){}
        
        @Override        
        protected LocalTime call() throws Exception {
            LocalTime h = LocalTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh : mm : ss a");
            
            while(true){
                updateValue(h);
                Platform.runLater(() -> {
                text.setText(LocalTime.now().format(formatter));
                });
                try { Thread.sleep(1000); }
                catch (InterruptedException e) { if(isCancelled()){break;} }
            }
            
            
            return h;
        }
        
    };
    @FXML
    private void mostar (ActionEvent event) {
        gh = new getHora();
        
        
        Thread backgroundThread = new Thread(gh);
        backgroundThread.setDaemon(true);
        backgroundThread.start();
        //text.textProperty().bind(Bindings.convert(gh.valueProperty()));
        text.setVisible(true);
    }
    
    @FXML
    private void parar (ActionEvent event) {
        gh.cancel();
        text.setVisible(false);
    }
    
    @Override   
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        text.setVisible(false);
        text.setText("");

        
        
    }    
    
}
