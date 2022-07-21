    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entregable;

import DBAcess.ClubDBAccess;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;
import model.Member;

/**
 * FXML Controller class
 *
 * @author Mystubis
 */
public class RegistroController implements Initializable {

    @FXML
    private TextField nombreField;
    @FXML
    private TextField usuarioField;
    @FXML
    private TextField telefonoField;
    @FXML
    private TextField apellidosField;
    @FXML
    private PasswordField contraseñaField;
    @FXML
    private TextField creditoField;
    @FXML
    private TextField svcField;
    @FXML
    private Button continuarButton;
    @FXML
    private Text usuarioError;
    @FXML
    private Text telefonoError;
    @FXML
    private Text creditoError;
    @FXML
    private Text contraseñaError;
    @FXML
    private Text nombreError;
    @FXML
    private Text apellidoError;
    @FXML
    private TextField imageField;
    @FXML
    private Button fcButton;
    @FXML
    private AnchorPane fondo;
    
    private boolean check;
    private ClubDBAccess clubDBAcess;
    private Image i = null;
    private boolean registroCorrecto = false;
    
    private Stage currentStage;
    @FXML
    private Text titulo;
    
    
    //una serie de checks comprueban que los datos etan 
    //correcatmente introducidos con un "true && check && check..."
    @FXML
    private void register(){
        check = true;
        //para informar a login de que se ha registrado correctamente
        registroCorrecto = false;
        
        String nombre = nombreField.getText().trim();
        String apellidos = apellidosField.getText().trim();
        String usuario = usuarioField.getText().trim();
        String pass = contraseñaField.getText();
        String tel = telefonoField.getText().trim();
        String card = creditoField.getText().trim();
        String svc = svcField.getText().trim();
        
       
        clubDBAcess = ClubDBAccess.getSingletonClubDBAccess();
        
        //////////////7//check nombre /////////////////////7
        if(nombre == null || nombre.equals("") || !nombre.matches("^[a-zA-ZñÑ_]+( [a-zA-ZñÑ_]+)*$")  ){
            nombreError.setVisible(true);
            check = false;
        }
        else{check = check && true;  nombreError.setVisible(false);}
        
        ///////////////////7/check apellidos///////////////////////
        if(apellidos == null || apellidos.equals("") || !apellidos.matches("^[a-zA-ZñÑ_]+( [a-zA-ZñÑ_]+)*$")  ){
            apellidoError.setVisible(true);
            check = false;
        }
        else{check = check && true;  apellidoError.setVisible(false);}
        
        /////////////7777/check usuario//////////////////////////
        if(usuario == null || usuario.equals("") ||usuario.contains(" ") ){
            usuarioError.setVisible(true);
            check = false;
        }
        else{
            //aqui hay que comprobar que el nombre de usuario no este cogido
            boolean usado = clubDBAcess.existsLogin(usuario);
            
            if(!usado){
            usuarioError.setVisible(false);
            check = check && true;
            }
            else{
            usuarioError.setVisible(true);
            check = false;
            }
        }
        
        ////////////////7777//check pass//////////////////////////
        
        if(pass != null  ||  !"".equals(pass) ){
            if(pass.length() >= 6  && pass.matches("^[a-zA-Z0-9ñÑ]+$"))//".*[\\w].*"
                {
                    check = check && true; 
                    contraseñaError.setVisible(false);
                }
                else{contraseñaError.setVisible(true);check = false;}
         }
        else{contraseñaError.setVisible(true);check = false;}
        
       ////////////////////check tel ///////////////////////////////////
       //usamos el parseint como metodo de comprobacion
       //si trata de hacer un parse int al telefono y hay un caracter 
       //salta la exception y el catch pone el check = false
       if( tel.length() == 9  ){
           try{
               int i = Integer.parseInt(tel);
               check = check && true;
               telefonoError.setVisible(false);
           }
           catch(NumberFormatException e){ telefonoError.setVisible(true);check = false;}
        }
        else{telefonoError.setVisible(true);check = false;}
        
        
        /////check credito, lo mismo comprobamos de
        //la misma manera que el tel
        if( card.length() != 0){
            if(card.length() == 16){
                try{
                        int j = Integer.parseInt(card);
                        check = check && true; 
                        creditoError.setVisible(false);
                }
                catch(NumberFormatException e){ creditoError.setVisible(true); check = false;}
            }
            else{ creditoError.setVisible(true);check = false;}
        }
        else{check = check && true;  creditoError.setVisible(false);}
        
        
        //check svc
        if(svc.length() != 0 ){
            if(svc.length() == 3){
                try{
                        int j = Integer.parseInt(svc);
                        check = check && true; 
                        creditoError.setVisible(false);
                }
                catch(NumberFormatException e){ creditoError.setVisible(true); check = false;}
            }
            else{ creditoError.setVisible(true);check = false;}
            
        }
        else{check = check && true;  creditoError.setVisible(false);}
        
        //si todo esta correcto el cehck debe estar a true y registra al nuevo miembro
        if(check){      
            clubDBAcess.getMembers().add(new Member(nombre,apellidos,tel,usuario,pass,card,svc,i));
            //y pone esta variable a tru que es para informar a login de que se ha registrado correctamente
            registroCorrecto = true;
            save();
            closeCurrentWindow();
            
        } 
        
    }
    //para informar a login de que se ha registrado correctamente
    public boolean getRegistroCorrecto(){
        return registroCorrecto;
    }
    
    private void save(){
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle(clubDBAcess.getClubName());
            alert.setHeaderText("Saving data in DB");
            alert.setContentText("The application is saving the changes into the database. This action can expend some minutes.");
            alert.show();
            clubDBAcess.saveDB();
     }
    
    
    private void closeCurrentWindow(){
        currentStage.close();
    }
    
    @FXML
    private void selectImage() throws FileNotFoundException{
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.png", "*.jpg"));
        Stage stage = (Stage) telefonoField.getScene().getWindow();
        File f = fc.showOpenDialog(stage);
        
        if(f != null){
            String url = f.getAbsolutePath();
            imageField.setText(url);
            imageField.setStyle("-fx-font-size: 14px;");
            i = new Image(new FileInputStream(url));
        }
    }
    //set stage desde login para poder cerrrlo luego
    //podriamos habrlo obtenido desde aqui directamente sin 
    //pasarselo desde login pero meh
    public void setStage(Stage s){
        currentStage = s;
    }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        //los mensajes de error a false
        nombreError.setVisible(false);
        apellidoError.setVisible(false);
        usuarioError.setVisible(false);
        telefonoError.setVisible(false);
        creditoError.setVisible(false);
        contraseñaError.setVisible(false);
        }
    }    
    

