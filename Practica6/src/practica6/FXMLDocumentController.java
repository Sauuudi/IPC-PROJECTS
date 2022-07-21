/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package practica6;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;

/**
 *
 * @author Mystubis
 */
public class FXMLDocumentController implements Initializable {

    
    @FXML
    private Button suspensoButton;
    @FXML
    private Button aprobadoButton;
    @FXML
    private Button bienButton;
    @FXML
    private Button notableButton;
    @FXML
    private Button sobreButton;
    @FXML
    private HBox piePane;
    @FXML
    private StackPane stackPane;
    @FXML
    private Button barButton;
    @FXML
    private Button lineButton;
    @FXML
    private Button colorButton;
    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private LineChart<String, Number> lineChart;
    @FXML
    private PieChart pieChart;
    @FXML
    private NumberAxis lineNAxis;
    @FXML
    private CategoryAxis lineCAxis;
    @FXML
    private NumberAxis barNAxis;
    @FXML
    private CategoryAxis barCAxis;
    
    
    private int suspenso = 0;
    private int aprobado = 0;
    private int bien = 0;
    private int notable = 0;
    private int sobresaliente = 0;
    
    private String[] state ;
    
    
    private ObservableList<PieChart.Data> pieChartData;
    private ObservableList<XYChart.Data <String, Number> > lineData;
    private ObservableList<XYChart.Data <String, Number> > barData;
    
    private XYChart.Series seriesBar;
    private XYChart.Series seriesLine;
    
    private boolean dark;
    
     
    private void createData(){
         state = new String[]{"Suspensos","Aprobados","Bien","Notables","Sobresalientes"};
         pieChartData = FXCollections.observableArrayList(
                new PieChart.Data(state[0], suspenso),
                new PieChart.Data(state[1], aprobado),
                new PieChart.Data(state[2], bien),
                new PieChart.Data(state[3], notable),
                new PieChart.Data(state[4], sobresaliente)
            );
     
        lineData = FXCollections.observableArrayList(
                new XYChart.Data(state[0], suspenso),
                new XYChart.Data(state[1], aprobado),
                new XYChart.Data(state[2], bien),
                new XYChart.Data(state[3], notable),
                new XYChart.Data(state[4], sobresaliente)
            );
        
         barData = FXCollections.observableArrayList(
                new XYChart.Data(state[0], suspenso),
                new XYChart.Data(state[1], aprobado),
                new XYChart.Data(state[2], bien),
                new XYChart.Data(state[3], notable),
                new XYChart.Data(state[4], sobresaliente)
            );
         
        seriesBar = new XYChart.Series(lineData);
        seriesBar.setName("Alumnos con cada nota");
        seriesLine = new XYChart.Series(barData);
        seriesLine.setName("Alumnos con cada nota");
    }
     
    
   
    private void setAll(){
        pieChart.setData(pieChartData);
        pieChart.setTitle("Notas IPC");
        
        lineCAxis.setLabel("Alumnos");
        lineNAxis.setLabel("Numeros");
        
        barCAxis.setLabel("Alumnos");
        barNAxis.setLabel("Numeros");
        
                
        barChart.getData().addAll(seriesBar);
        lineChart.getData().addAll(seriesLine);
        
     }
    
    @FXML
    private void sumValue(ActionEvent event){
        int c = -1;
        if(event.getSource() == suspensoButton){suspenso++; c = 0; }
        else if(event.getSource() == aprobadoButton){aprobado++; c=1;  }
        else if(event.getSource() == bienButton){bien++; c=2; }
        else if(event.getSource() == notableButton){notable++; c=3; }
        else if(event.getSource() == sobreButton){sobresaliente++; c=4; }
        
        PieChart.Data d = pieChartData.get(c);
        int valorActual = (int) d.getPieValue();
        pieChartData.get(c).setPieValue(valorActual + 1);
        
        barData.get(c).setYValue(valorActual + 1);
        lineData.get(c).setYValue(valorActual + 1);
    }
    @FXML
    private void changePane(ActionEvent event){
         if(event.getSource() == barButton){
             barChart.setVisible(true);
             barChart.toFront();
             lineChart.setVisible(false);
         }
        else if(event.getSource() == lineButton){ 
            barChart.setVisible(false);
            lineChart.setVisible(true);
            lineChart.toFront();
        }
     }
    
    @FXML
    private void changeColor(ActionEvent event){
        dark = !dark;
        if(dark){
            colorButton.getScene().getStylesheets().clear();
            colorButton.getScene().getStylesheets().add(this.getClass().getResource("estilosDark.css").toExternalForm()); }
        else{
            colorButton.getScene().getStylesheets().clear();
            colorButton.getScene().getStylesheets().add(this.getClass().getResource("estilosWhite.css").toExternalForm());}
    }
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        createData();
        setAll();
        
        dark = false;
        lineChart.setVisible(false);
        barChart.setVisible(true);
        barButton.setDisable(true);
        
        
        lineButton.setOnMouseClicked(new EventHandler<MouseEvent>() 
        {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                lineButton.setDisable(true);
                barButton.setDisable(false);
            }
        });
        barButton.setOnMouseClicked(new EventHandler<MouseEvent>() 
        {
            @Override
            public void handle(final MouseEvent mouseEvent) {
                barButton.setDisable(true);
                lineButton.setDisable(false);

            }
        });
        
        
        
        
    }    
    
}
