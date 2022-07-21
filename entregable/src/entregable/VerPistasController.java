/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entregable;

import DBAcess.ClubDBAccess;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import javafx.util.Pair;
import model.Booking;
import model.Court;

/**
 * FXML Controller class
 *
 * @author Mystubis
 */
public class VerPistasController implements Initializable {

    @FXML
    private ListView<LocalTime> p1List;
    @FXML
    private ListView<LocalTime> p2List;
    @FXML
    private ListView<LocalTime> p3List;
    @FXML
    private ListView<LocalTime> p4List;
    
     private ClubDBAccess clubDBAcess = ClubDBAccess.getSingletonClubDBAccess();
     private int turn = 0;
     private ArrayList<Court> courts = clubDBAcess.getCourts();
     private int contador = 0;
    @FXML
    private Text titulo;
     
    //////////////////////////////////////////////////////////////////////
    //esta parte es igual que en main, solo que la usamos para mostar lo que esta reservado y que no 
     private void showTimes()
    {
        LocalTime inicio =  LocalTime.of(9,0);
        ArrayList<LocalTime> times = new ArrayList();
        
        for(int i = 0; i< 9; i++){
            times.add(inicio.plus(Duration.ofMinutes(90*i)));
        }
        
        ObservableList<LocalTime> observableBookings;
        observableBookings = FXCollections.observableList(times);
        
        p1List.setItems(observableBookings);
        p2List.setItems(observableBookings);
        p3List.setItems(observableBookings);
        p4List.setItems(observableBookings);
    }
     private void checkIfReserved()
    {
        p1List.setCellFactory(c -> new VerPistasController.BookingListCell());
        
        p2List.setCellFactory(c -> new VerPistasController.BookingListCell());
        
        p3List.setCellFactory(c -> new VerPistasController.BookingListCell());
        
        p4List.setCellFactory(c -> new VerPistasController.BookingListCell());
        

    }
     private Pair<Boolean,String> isCourtBookedForHour(LocalDate d, LocalTime t, String court)
    {
        ArrayList<Booking> boo = clubDBAcess.getCourtBookings(court,  d);
        
        for(int i = 0; i < boo.size(); i++){
            
            if(boo.get(i).getFromTime().equals(t)){
                
                return new Pair(true,boo.get(i).getMember().getLogin());
            }
        }
        
        return new Pair(false,"");
    }
     class BookingListCell extends ListCell<LocalTime>
    {
        ListView<LocalTime> listView = new ListView<>();
        
        PseudoClass booked = PseudoClass.getPseudoClass("booked");
        PseudoClass pasada = PseudoClass.getPseudoClass("pasada");
        
        
        @Override
        protected void updateItem(LocalTime time, boolean empty) {
            super.updateItem(time, empty);
            if (time == null || empty) {setText(null); pseudoClassStateChanged(booked, false);} 
            else {
                String p = courts.get(turn).getName();
                boolean book = isCourtBookedForHour(LocalDate.now() ,time ,p).getKey();
                String log = isCourtBookedForHour(LocalDate.now() ,time ,p).getValue();

                //fechas reservadas
                if (book) {
                    setText(time + " - (" + log + ")" );
                    pseudoClassStateChanged(booked, true);
                    pseudoClassStateChanged(pasada,false);
                }
                //las fechas ya pasadas 
                else if(  time.compareTo(LocalTime.now()) < 0){
                    setText(time + " NO DISPONIBLE");
                    pseudoClassStateChanged(pasada,true);
                    pseudoClassStateChanged(booked, false);
                }
                //disponobles
                else {
                    setText(time + " - DISPONIBLE");
                    pseudoClassStateChanged(pasada,false);
                    pseudoClassStateChanged(booked, false);
                }
                //usamos el turnup para ir cambiando de pista por cada actualizacion
                //tras nueve tiempos,(localtimes), actualizamos pista(turnUP)
                if ( contador == 9 ){contador = 0; turnUp();}else{contador++;}
            }
        }
    }
    private void turnUp()
    {
         turn = (turn + 1) % 4;
     }
     ////////////////////////////////////////////////////////////////////////////////////
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        showTimes();
        checkIfReserved();
        
        //para que solo este seleccionado 1 listview
        //igual que en main pero menos
        p1List.setOnMouseClicked(new EventHandler<MouseEvent>() 
        {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                p2List.getSelectionModel().clearSelection();
                p3List.getSelectionModel().clearSelection();
                p4List.getSelectionModel().clearSelection();
            }
        });
        p2List.setOnMouseClicked(new EventHandler<MouseEvent>() 
        {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                p1List.getSelectionModel().clearSelection();
                p3List.getSelectionModel().clearSelection();
                p4List.getSelectionModel().clearSelection();
            }
        });
        p3List.setOnMouseClicked(new EventHandler<MouseEvent>() 
        {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                p2List.getSelectionModel().clearSelection();
                p1List.getSelectionModel().clearSelection();
                p4List.getSelectionModel().clearSelection();
            }
        });
        p4List.setOnMouseClicked(new EventHandler<MouseEvent>() 
        {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                p2List.getSelectionModel().clearSelection();
                p3List.getSelectionModel().clearSelection();
                p1List.getSelectionModel().clearSelection();
            }
        });
        
     }    
    
}
