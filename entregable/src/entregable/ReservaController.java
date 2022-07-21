/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entregable;

import DBAcess.ClubDBAccess;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Booking;
import model.Court;
import model.Member;

/**
 * FXML Controller class
 *
 * @author Mystubis
 */
public class ReservaController implements Initializable {

  
    @FXML
    private Text screen1;
    @FXML
    private Text screen2;
    @FXML
    private Text screen3;
    @FXML
    private Text screen4;
    @FXML
    private AnchorPane parentt;
    @FXML
    private Button cancelarButton;
    @FXML
    private Button confirmarButton;
    
    private ClubDBAccess clubDBAcess = ClubDBAccess.getSingletonClubDBAccess();
    
    private Member mem;
    private Court court;
    private LocalDate fechaReserva;
    private LocalTime horaReserva;
    boolean tarjeta;
    @FXML
    private Text titulo;
    //confirmar y completar del todo la reserva
    @FXML
    private void confirmar(){
        
        Booking reserva = new Booking(LocalDateTime.now(), fechaReserva, horaReserva, tarjeta, court, mem );
        clubDBAcess.getBookings().add(reserva);
        //descomentar si se desea que se guarde la reserva en base de datos inmediatamente despues de confirmar
        save();
        closeCurrentWindow();
    }
    
    @FXML
    private void cancelar(){
        closeCurrentWindow();
    }
    
    //metodo para pasar la info desde main a este y ponerla
    public void setValues(Member m, Court c, LocalDate f, LocalTime t){
        this.mem = m;
        this.court = c;
        this.fechaReserva = f;
        this.horaReserva = t;
        tarjeta = clubDBAcess.hasCreditCard(mem.getLogin());
        
        screen1.setText("Seguro que quieres reservar: ");
        screen2.setText("Court: " + c.getName() );
        screen3.setText("Fecha: " +  f + " - " + t );
        String msg = "NO";
        if(tarjeta){msg = "SI";}
        screen4.setText("Login: "+ m.getLogin()+ "   Tarjeta: " + msg);
        
    }
    
    private void closeCurrentWindow(){
        Stage stage = (Stage) parentt.getScene().getWindow();
        stage.close();
    }
    
    private void save(){
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(clubDBAcess.getClubName());
            alert.setHeaderText("Saving data in DB");
            alert.setContentText("The application is saving the changes into the database. This action can expend some minutes.");
            alert.show();
            clubDBAcess.saveDB();
     }
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        cancelarButton.setFocusTraversable(false);
        confirmarButton.setFocusTraversable(false);
        
        screen1.setText("");
        screen2.setText("");
        screen3.setText("");
        screen4.setText("");
    }    
    
}
