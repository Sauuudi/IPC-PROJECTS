/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ipc_ilesdeb;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;

/**
 *
 * @author Mystubis
 */
public class FXML_ilesdebController implements Initializable {

    @FXML
    private MenuItem libraEss;
    @FXML
    private MenuItem dolarEss;
    @FXML
    private MenuItem rupiaEss;
    @FXML
    private MenuItem yuanEss;
    @FXML
    private TextField inEss;
    @FXML
    private TextField outEss;
    @FXML
    private ToggleGroup menuEss;
    @FXML
    private MenuItem salirEss;
    
    //no hacer caso a esto fue la primera solucion
    //private int selected = -1;
    
    
    
       
    private void calculo(){
        if(!inEss.getText().matches("^[0-9,]+$")){outEss.setText("Error en el formato de numero");}
        
        boolean check = true;
        double d = -1;
        String s;
        
        try{
            s = inEss.getText().replace(",", ".");
            d = Double.parseDouble(s);
        }catch(NumberFormatException e){check = false;}
       
        
        
        if(check){  
            String select = menuEss.getSelectedToggle().toString().split(",")[0].replace("RadioMenuItem[id=","").trim();
            System.out.println(select);
            if(select.equals("libraEss")){                
                outEss.setText(String.valueOf(d * 0.891456));
            }
            else if(select.equals("dolarEss")){
                outEss.setText(String.valueOf(d * 1.0805));
            }
            else if(select.equals("rupiaEss")){
               outEss.setText(String.valueOf(d * 82.146));
            }
            else if(select.equals("yuanEss")){
                outEss.setText(String.valueOf(d * 7.6904));
            }
            else{outEss.setText("selecciona a que moneda quieres cambiar");}
        }
        
       
    
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        inEss.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke) {
                if(ke.getCode() == ENTER){calculo();}
            }
        });
        
         //no hacer caso a esto, fue la primera solucion
        /*
        libraEss.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(final ActionEvent event) {
                selected = 0;
            }
        });
        dolarEss.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(final ActionEvent event) {
                selected = 1;
            }
        });
        rupiaEss.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(final ActionEvent event) {
                selected = 2;
            }
        });
        yuanEss.setOnAction(new EventHandler<ActionEvent>() 
        {
            @Override
            public void handle(final ActionEvent event) {
                selected = 3;
            }
        });*/
        
        
        
    }    
    
}
