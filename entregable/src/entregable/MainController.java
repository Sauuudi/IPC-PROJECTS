/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entregable;


import DBAcess.ClubDBAccess;
import java.io.IOException;
import javafx.event.ActionEvent;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;
import model.Booking;
import model.Court;
import model.Member;

/**
 * FXML Controller class
 *
 * @author Mystubis
 */
public class MainController implements Initializable {

    @FXML
    private Pane reservarPane;
    @FXML
    private Pane misReservasPane;
    @FXML
    private Button reservarButton;
    @FXML
    private Button misReservasButton;
    @FXML
    private ListView<Booking> reservasPendientesList;
    @FXML
    private ListView<Booking> ultimasReservasList;
    @FXML
    private Button cancelarReservaButton;
    @FXML
    private DatePicker fecha;
    @FXML
    private ListView<LocalTime> p1List;
    @FXML
    private ListView<LocalTime> p2List;
    @FXML
    private ListView<LocalTime> p3List;
    @FXML
    private ListView<LocalTime> p4List;
    @FXML
    private Text userText;
    @FXML
    private Button reservarPistaButton;
    @FXML
    private ImageView userImage;
    @FXML
    private Button buscarButton;
    @FXML
    private TextField screen;
    @FXML
    private Text fechaText;
    @FXML
    private Text screen2;
    @FXML
    private AnchorPane leftPane;
    
    private Member m;
    private int turn = 0;
    private ClubDBAccess clubDBAcess = ClubDBAccess.getSingletonClubDBAccess();
    
    //datos finale de seleccion que nos van a ayudar a hacer la resrva, la fecha se saca de fecha(DatePicker)
    private Court court;
    private LocalTime horaReserva;
    private ArrayList<Court> courts;
    private int contador = 0;
    //el booking que vamos a cancelar
    private Booking bookingCancel;
    
    
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////
    //para los botones del menu lateral y cambiar entre mis reservas y reservar
    @FXML
    private void changePane(ActionEvent event)
    {
        if(event.getSource()== reservarPistaButton){
            fecha.setValue(LocalDate.now());
            reservarPane.toFront();
            showReservar();
            checkIfReserved();
            horaReserva = null;
            court = null;
            bookingCancel = null;
            screen.setText("");
            reservarPistaButton.setStyle("-fx-background-color: #347BBE;");
            misReservasButton.setStyle("-fx-background-color: #5582D5;");
            
            
        }
        else if(event.getSource()== misReservasButton){
            misReservasPane.toFront();
            showMisReservas();
            showPendientes();
            horaReserva = null;
            court = null;
            bookingCancel = null;
            screen2.setText("");
            misReservasButton.setStyle("-fx-background-color: #347BBE;");
            reservarPistaButton.setStyle("-fx-background-color: #5582D5;");
            
            
        }
    }
    //para pasar el member desde el login a  main
    public void setMember(Member mem)
    {
        m = mem;
        if(m.getImage() != null){userImage.imageProperty().setValue(m.getImage());}
        userText.setText(m.getLogin());
    }
    
    //actualiza main con la nueva fecha al cambiar de fecha en el date picker
    //o al darle añ botom buscar
    @FXML
    private void actualizar()
    {
        //si la fecha introducida es menor que hoy, que en principio no se puede pk asi tenemos
        //configurado el datepicker, muestra un mensaje
        if(fecha.getValue().compareTo(LocalDate.now()) < 0){
            //en principio este mensaje nunca aparece
            fechaText.setText("ilias te has olvidado de quitar el modo pruebas");
            fechaText.setStyle("font-size: 26px;");
            
            //descomentar solo pa pruebas
            //showReservar();
            //checkIfReserved();
        }
        else{
           fechaText.setText("Club PaddleExperience");
            
            showReservar();
            checkIfReserved();
            //aqui ponemos los datos de la seleccion de la reserva otra vez a null pk al cambiar de fecha no 
            //queremos que se quede guardada la seleccion anterior, y tambien hacemo clear selecciont de todo
            horaReserva = null;
            court = null;
            screen.setText("");
            p1List.getSelectionModel().clearSelection();
            p2List.getSelectionModel().clearSelection();
            p3List.getSelectionModel().clearSelection();
            p4List.getSelectionModel().clearSelection();
        }
    
    }
    
    //fin de la parte
    /////////////////////////////////////////////////////////////////////////////////////////
    //INTENTAR MEJORAR ESTA PARTE DEL SHOWRESERVAR ILIAS
    //DESDE EL PRINCIPIO HASTA AHORA HAS APRENDIDO MUCHAS COSAS
    
    //aqui solo creamos las horas para los listview
    private void showReservar()
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
    //aqui actualizamos las listas anteriores segun estan 
    //disponible o no usando bookedUpdateListCell y isCourtBookedForHour
    private void checkIfReserved() 
    {
        p1List.setCellFactory(c -> new bookedUpdateListCell());
        
        p2List.setCellFactory(c -> new bookedUpdateListCell());
        
        p3List.setCellFactory(c -> new bookedUpdateListCell());
        
        p4List.setCellFactory(c -> new bookedUpdateListCell());
        

    }
    //este metodo checkea si una determinada pista esta reservada para 
    //un determinado dia y una determiada hora, y si lo esta, nos devuelve
    //true y el login de quien la reservo
    private Pair<Boolean,String> isCourtBookedForHour(LocalDate d, LocalTime t, String court)
    {
        
        ArrayList<Booking> boo = clubDBAcess.getCourtBookings(court,  d);
        
        for(int i = 0; i < boo.size(); i++){
            if(boo.get(i).getFromTime().equals(t)){
                return new Pair(true,boo.get(i).getMember().getLogin());
            }
        }
        
        return new Pair(false,"no esta reservada");
    }
    //usamos dos pseudocalses para distinguir en css 
    //cuando esta reservada, disponible, o ya no se puede reservar
    //con condiciones y el metodo anterior comprobamos cuando 
    //cambiar el estado y cuando no
    class bookedUpdateListCell extends ListCell<LocalTime> 
    {
        ListView<LocalTime> listView = new ListView<>();
        
        PseudoClass booked = PseudoClass.getPseudoClass("booked");
        PseudoClass pasada = PseudoClass.getPseudoClass("pasada");
         @Override
        protected void updateItem(LocalTime time, boolean empty) {
            super.updateItem(time, empty);
            if (time == null || empty) {setText(null);pseudoClassStateChanged(booked, false);} 
            else {
                String pista = courts.get(turn).getName();
                boolean book = isCourtBookedForHour(fecha.getValue() ,time ,pista).getKey();
                String log = isCourtBookedForHour(fecha.getValue() ,time ,pista).getValue();

                //fechas reservadas
                if (book){
                    setText(time + " - (" + log + ")" );
                    pseudoClassStateChanged(booked, true);
                    pseudoClassStateChanged(pasada,false);
                }
                
                //las fechas que ya no se pueden reservar 
                else if(fecha.getValue().compareTo(LocalDate.now()) < 0 
                        ||(fecha.getValue().compareTo(LocalDate.now()) == 0 && time.compareTo(LocalTime.now()) < 0)){
                    setText(time + " NO DISPONIBLE");
                    pseudoClassStateChanged(pasada,true);
                    pseudoClassStateChanged(booked, false);
                
                }
                //disponobles
                else{
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
    
    //fin de la parte
    ///////////////////////////////////////////////////////////////////////7
    //para las reservas pendientes
    private void showPendientes()
    {
        ObservableList<Booking> observableBookings;
        ArrayList<Booking> aa = clubDBAcess.getUserBookings(m.getLogin());
        
        //pa quitar las que no valen
        for(int i = 0; i< aa.size();){
            Booking aux = aa.get(i);
            if(aux.getMadeForDay().compareTo(LocalDate.now()) < 0 ){aa.remove(i);}
            else if(aux.getMadeForDay().compareTo(LocalDate.now()) == 0 
                    && aux.getFromTime().compareTo(LocalTime.now()) < 0 ){aa.remove(i);}
            else{i++;}
        }
        
        observableBookings = FXCollections.observableList(aa);
        
        reservasPendientesList.setItems(observableBookings);
        reservasPendientesList.setCellFactory(c-> new misReservasPendientesListCell());
    
    }
    class misReservasPendientesListCell extends ListCell<Booking>
    {
        @Override
        protected void updateItem(Booking item, boolean empty)
            {   super.updateItem(item, empty); 
                
            if (item==null || empty){ setText(null);}
                else{
                    String msg = "NO PAGADO";
                    if(clubDBAcess.hasCreditCard(m.getLogin())){msg = "PAGADO";}
                    setText("(" + item.getMember().getLogin() + "), "+ item.getMadeForDay()+ ", " 
                            +  item.getCourt().getName() + " - " + item.getFromTime()  + " - " + msg);
               }
            }       
    }
    
    //para las reservas pasadas 
    private void showMisReservas()//aqui se veran todas las reservas del member que ha hecho con anterioridad
    {
        
        ObservableList<Booking> observableBookings;
        
        ArrayList<Booking> a = clubDBAcess.getUserBookings(m.getLogin());
        
        //pa quitar las que no valen
        for(int i = 0; i< a.size(); ){
            Booking aux = a.get(i);
            if(aux.getMadeForDay().compareTo(LocalDate.now()) > 0 ){a.remove(i);}
            else if(aux.getMadeForDay().compareTo(LocalDate.now()) == 0 
                    && aux.getFromTime().compareTo(LocalTime.now()) > 0 ){a.remove(i);}
            else{i++;}
        }

        //para quedarnos solo con las 10 ultimasLogin1  
        if(a.size() > 10){
            for(int i  = 0; i < a.size() - 11; i++){a.remove(0);}
        }
        
        observableBookings = FXCollections.observableList(a);
        
        ultimasReservasList.setItems(observableBookings);
        ultimasReservasList.setCellFactory(c-> new misReservasListCell());

    }
    class misReservasListCell extends ListCell<Booking>
    {
        @Override
        protected void updateItem(Booking item, boolean empty)
            {   super.updateItem(item, empty); 
                
            if (item==null || empty){ setText(null);}
                else 
                {  setText("(" + item.getMember().getLogin() + "), "+ item.getMadeForDay()+ ", " 
                            +  item.getCourt().getName() + " - " + item.getFromTime());
                }
            }
    }       
    
    //fin de la parte
    ///////////////////////////////////////////////////////////////////////////////
    //manejar el boton de resrvar, abre una nueva ventana si esta todo  
    //correcto y le pasa los datos de reserva para confirmar 
    @FXML
    private void reservar() throws IOException{
        if(horaReserva == null || court == null || court.equals("")){ screen.setText("SELECCIONA ANTES DE RESRVAR");}
        else if(fecha.getValue().compareTo(LocalDate.now()) < 0){screen.setText("SELECCION INVALIDA");}
        else if(fecha.getValue().compareTo(LocalDate.now()) ==  0 && horaReserva.compareTo(LocalTime.now()) < 0){screen.setText("SELECCION INVALIDA");}
        else if(isCourtBookedForHour(fecha.getValue(), horaReserva, court.getName()).getKey()){screen.setText("SELECCION INVALIDA");}
        else{
        
        
            FXMLLoader miCargador = new FXMLLoader(getClass().getResource("reserva.fxml"));
            Parent root = miCargador.load();

            ReservaController controladorReserva = miCargador.<ReservaController>getController();
            
            Scene scene = new Scene(root);
            Stage stage = new Stage();
            
            String css = controladorReserva.getClass().getResource("reserva.css").toExternalForm();
            scene.getStylesheets().add(css);
            
            stage.setScene(scene);
            stage.setTitle("CONFIRMAR RESERVA");
            stage.setResizable(false);
            
            stage.initModality(Modality.APPLICATION_MODAL);
            controladorReserva.setValues(m, court, fecha.getValue(), horaReserva );
            stage.showAndWait();
            
            showReservar();
            checkIfReserved();
            horaReserva = null;
            court = null;
            screen.setText("");
            p1List.getSelectionModel().clearSelection();
            p2List.getSelectionModel().clearSelection();
            p3List.getSelectionModel().clearSelection();
            p4List.getSelectionModel().clearSelection();
            
            
        }
    }
    //maneja el cancelarresrva, checkea que la difernecia
    //sea menor que 24 horas en segundos
    @FXML
    private void cancelarReserva(){
        LocalDateTime dh;
        if(bookingCancel != null){
            dh = LocalDateTime.of(bookingCancel.getMadeForDay(), bookingCancel.getFromTime());
            if(Math.abs(ChronoUnit.SECONDS.between(dh,LocalDateTime.now())) < 86400){
                screen2.setText("No es posible cancelar esta reserva");
            }
            else{
                
                clubDBAcess.getBookings().remove(bookingCancel); 
                //descomentar si se desea que se guarde la reserva en base de datos inmediatamente despues de confirmar
                save();
                showPendientes(); 
                showMisReservas();
                screen2.setText("RESERVA CANCELADA");
                
            }
        }
        else{screen2.setText("SELECCION INVALIDA");}
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
    public void initialize(URL url, ResourceBundle rb) 
    { // TODO
        
        screen.setText("");
        screen2.setText("");
        reservarPane.toFront();
        reservarPistaButton.setStyle("-fx-background-color: #347BBE;");
        fechaText.setText("Club PaddleExperience");
        
        fecha.setValue(LocalDate.now());
        courts = clubDBAcess.getCourts();
        
        showReservar();
        checkIfReserved();
        
        //no permitir dias anteriores a hoy datepicker,descomentar cuando termine las pruebas, no te olvides ilias
        fecha.setDayCellFactory((DatePicker picker) -> {return new DateCell() 
        {
            @Override
            public void updateItem(LocalDate date, boolean empty) 
            {
                super.updateItem(date, empty);
                LocalDate today = LocalDate.now();
                setDisable(empty || date.compareTo(today) < 0 );
            }
            }; });
        
        fecha.valueProperty().addListener((ov, oldValue, newValue) -> {
            actualizar();    
         });
        
        //como listeners con la funcion de seleccion,
        //mostrar la seleccion de las listview en screen y solo tener 1 listview seleccionado
        //uno por cada lista
        p1List.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent ke) {
                String s = p1List.getSelectionModel().selectedItemProperty().getValue().toString();
                court =  courts.get(0);
                LocalTime t = p1List.getSelectionModel().selectedItemProperty().getValue();
                horaReserva = t;
                if(fecha.getValue().compareTo(LocalDate.now()) < 0){screen.setText("SELECCION INVALIDA");}
                else if(fecha.getValue().compareTo(LocalDate.now()) == 0 && t.compareTo(LocalTime.now()) < 0){screen.setText("SELECCION INVALIDA");}
                else if(isCourtBookedForHour(fecha.getValue(), t, courts.get(0).getName()).getKey()){screen.setText("SELECCION INVALIDA");}
                else{screen.setText("Selecconada " + court.getName() + " - Para: "+ fecha.getValue().toString() + " - " + s);}
                p2List.getSelectionModel().clearSelection();
                p3List.getSelectionModel().clearSelection();
                p4List.getSelectionModel().clearSelection();
            }
        });
        p2List.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent ke) {
                String s = p2List.getSelectionModel().selectedItemProperty().getValue().toString();
                court =  courts.get(1);
                LocalTime t = p2List.getSelectionModel().selectedItemProperty().getValue();
                horaReserva = t;
                if(fecha.getValue().compareTo(LocalDate.now()) < 0){screen.setText("SELECCION INVALIDA");}
                else if(fecha.getValue().compareTo(LocalDate.now()) == 0 && t.compareTo(LocalTime.now()) < 0){screen.setText("SELECCION INVALIDA");}
                else if(isCourtBookedForHour(fecha.getValue(), t, courts.get(1).getName()).getKey()){screen.setText("SELECCION INVALIDA");}
                else{screen.setText("Selecconada " + court.getName() + " - Para: "+ fecha.getValue().toString() + " - " + s);}
                p1List.getSelectionModel().clearSelection();
                p3List.getSelectionModel().clearSelection();
                p4List.getSelectionModel().clearSelection();
            }
        });
        p3List.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent ke) {
                String s = p3List.getSelectionModel().selectedItemProperty().getValue().toString();
                court =  courts.get(2);
                LocalTime t = p3List.getSelectionModel().selectedItemProperty().getValue();
                horaReserva = t; 
                if(fecha.getValue().compareTo(LocalDate.now()) < 0){screen.setText("SELECCION INVALIDA");}
                else if(fecha.getValue().compareTo(LocalDate.now()) == 0 && t.compareTo(LocalTime.now()) < 0){screen.setText("SELECCION INVALIDA");}
                else if(isCourtBookedForHour(fecha.getValue(), t, courts.get(2).getName()).getKey()){screen.setText("SELECCION INVALIDA");}
                else{screen.setText("Selecconada " + court.getName() + " - Para: "+ fecha.getValue().toString() + " - " + s);}
                p2List.getSelectionModel().clearSelection();
                p1List.getSelectionModel().clearSelection();
                p4List.getSelectionModel().clearSelection();
            }
        });
        p4List.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent ke) {
                String s = p4List.getSelectionModel().selectedItemProperty().getValue().toString();
                court =  courts.get(3);
                LocalTime t = p4List.getSelectionModel().selectedItemProperty().getValue();
                horaReserva = t;
                if(fecha.getValue().compareTo(LocalDate.now()) < 0){screen.setText("SELECCION INVALIDA");}
                else if(fecha.getValue().compareTo(LocalDate.now()) == 0 && t.compareTo(LocalTime.now()) < 0){screen.setText("SELECCION INVALIDA");}
                else if(isCourtBookedForHour(fecha.getValue(), t, courts.get(3).getName()).getKey()){screen.setText("SELECCION INVALIDA");}
                else{screen.setText("Selecconada " + court.getName() + " - Para: "+ fecha.getValue().toString() + " - " + s);}
                p2List.getSelectionModel().clearSelection();
                p3List.getSelectionModel().clearSelection();
                p1List.getSelectionModel().clearSelection();
            }
        });
        
        //lo mismo que el anterior pero para mis reservas
        reservasPendientesList.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent ke) {
                bookingCancel = reservasPendientesList.getSelectionModel().selectedItemProperty().getValue();
                ultimasReservasList.getSelectionModel().clearSelection();
            }
        });
        ultimasReservasList.setOnMousePressed(new EventHandler<MouseEvent>()
        {
            public void handle(MouseEvent ke) {
                bookingCancel = null;
                reservasPendientesList.getSelectionModel().clearSelection();
            }
        });
        
        
        
    }    
}
