package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import models.EstadisticasModel;
import persistence.EstadisticasDB;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;


public class SampleController implements Initializable {
    
    @FXML private TextField rangoMin;
    @FXML private TextField rangoMax;
    @FXML private ChoiceBox choiceFun;
    @FXML private Button button;
    @FXML private DatePicker dateDesde;
    @FXML private DatePicker dateHasta;
    
    // Declaramos el "LineChart" donde pintaremos la funcion
    @FXML private NumberAxis ejeXDia;
    CategoryAxis xAxis = new CategoryAxis();
    @FXML private NumberAxis ejeYTotal = new NumberAxis(0, 100, 5);
    @FXML private LineChart<String, Number> graph = new LineChart(xAxis, ejeYTotal);
    
    
    /**
     * Método que se ejecuta al pulsar el boton "Dibujar Funcion"
     * @param event 
     */
    @FXML private void AccionPintar(ActionEvent event) {
        //int grado = getGradoFuncion(choiceFun.getValue().toString());
        pintarGrafica();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Inicializamos el rango sobre el que pintaremos la funcion.
//        rangoMin.setText("-10");
//        rangoMax.setText("10");
          //choiceFun.setValue("x");
//        ejeDia.setLowerBound(0);
//        ejeDia.setUpperBound(10);
//        ejeDia.setTickUnit(1);
//        ejeYTotal.setLowerBound(0);
//        ejeTotal.setUpperBound(10);
//        ejeTotal.setTickUnit(5);
          CategoryAxis xAxis = new CategoryAxis();
    }
    
    /**
     * Método que devuelve el grado de la función a dibujar
     * @param funcion
     * @return grado de la funcion
     */
    private static int getGradoFuncion(String funcion){
        if (funcion.equals("x"))
            return 1;
        else if(funcion.equals("x^2"))
            return 2;
        else if (funcion.equals("x^3"))
            return 3;
        else if (funcion.equals("x^4"))
            return 4;
        else
            return 5;
    }
    

    private void pintarGrafica (){
        EstadisticasDB estadisticasDB = new EstadisticasDB();
        List<EstadisticasModel> lista;
        dateDesde.getValue();
        dateHasta.getValue();
        int top = 0;
        lista = estadisticasDB.consultarVisitasDiariasPorRango(dateDesde.getValue().toString(), dateHasta.getValue().toString());
        if(!lista.isEmpty()){
            top = lista.get(0).getTotal();
        }
        for(EstadisticasModel elemento : lista){
            if(top <= elemento.getTotal()){
                top = elemento.getTotal();
            }
        }
        for (EstadisticasModel elemento : lista) {
            //seriesList.add(new XYChart.Data(elemento.getFechas(), elemento.getTotal()));
        }
    }
}