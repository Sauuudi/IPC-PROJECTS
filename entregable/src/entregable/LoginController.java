/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entregable;

import DBAcess.ClubDBAccess;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.ENTER;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Member;

/**
 *
 * @author Mystubis
 */
public class LoginController implements Initializable {
    
    @FXML
    private TextField usuarioField;
    @FXML
    private PasswordField contraseñaField;
    @FXML
    private Button loginButton;
    @FXML
    private Button registroButton;
    @FXML
    private Label errorFieldd;
    @FXML
    private Button pistasButton;
    @FXML
    private AnchorPane parentt;
    
    private ClubDBAccess clubDBAcess;
    
    @FXML
    private void loginn() throws IOException{
        
        //check de los datos del login
        clubDBAcess = ClubDBAccess.getSingletonClubDBAccess();
        String usuario = usuarioField.getText().trim();
        String pass = contraseñaField.getText();
        
        Member m = clubDBAcess.getMemberByCredentials(usuario, pass);
       
        
        if(m != null){
            errorFieldd.setVisible(false);
            
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("main.fxml"));
            Parent root = miCargador.load();

            MainController controladorMain = miCargador.<MainController>getController();


            Scene scene = new Scene(root);
            Stage stage = new Stage();
            
            String css = controladorMain.getClass().getResource("main.css").toExternalForm();
            scene.getStylesheets().add(css);
            
            stage.setScene(scene);
            stage.setTitle("MAIN");
            
            stage.show();
            
            //le pasamos el memeber a main
            controladorMain.setMember(m);
            closeCurrentWindow();     
            
        }
        else{
            errorFieldd.setText("Ususario o contraseña incorrectos");
            String style =  "-fx-text-fill: #ea2020;"+ "-fx-font-size: 19px;";
            errorFieldd.setStyle(style);
            errorFieldd.setVisible(true);
        }
    }
    
    @FXML
    private void verPistas() throws IOException {
        
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("verPistas.fxml"));
            Parent root = miCargador.load();
            
            VerPistasController controladorPistas = miCargador.<VerPistasController>getController();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            
            String css = controladorPistas.getClass().getResource("verPistas.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
            stage.setTitle("PISTAS");
            
            stage.setResizable(false);
            
            stage.showAndWait();
       }
    
    @FXML
    private void register() throws IOException {
        
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("registro.fxml"));
            Parent root = miCargador.load();

            
            RegistroController controladorRegistro = miCargador.<RegistroController>getController();

            Scene scene = new Scene(root);
            Stage stage = new Stage();
            
            controladorRegistro.setStage(stage);
            String css = controladorRegistro.getClass().getResource("registro.css").toExternalForm();
            scene.getStylesheets().add(css);

            stage.setScene(scene);
            stage.setTitle("REGISTRO");
            stage.setResizable(false);
            
            stage.showAndWait();
            
            if(controladorRegistro.getRegistroCorrecto()){
                errorFieldd.setText("Registrado Correctamente");
                String style = "-fx-text-fill: #32CD32;" + "-fx-font-size: 18px;";
                errorFieldd.setStyle(style);
                errorFieldd.setVisible(true);
            }
        }
    
   
    //consigue el stage y cierra la ventana actual
    private void closeCurrentWindow(){
        Stage stage = (Stage) errorFieldd.getScene().getWindow();
        stage.close();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        // TODO
        errorFieldd.setVisible(false);
        
        //lista de listeners para la tecla ENTER
        usuarioField.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke) {
                try {
                    if(ke.getCode() == ENTER){loginn();}
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        contraseñaField.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke) {
                try {
                    if(ke.getCode() == ENTER){loginn();}
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        loginButton.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke) {
                try {
                    if(ke.getCode() == ENTER){loginn();}
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        pistasButton.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke) {
                try {
                    if(ke.getCode() == ENTER){verPistas();}
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        registroButton.setOnKeyPressed(new EventHandler<KeyEvent>(){
            @Override
            public void handle(KeyEvent ke) {
                try {
                    if(ke.getCode() == ENTER){register();}
                } catch (IOException ex) {
                    Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        
    }    
    
}
