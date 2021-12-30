package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.EstadisticasModel;
import persistence.EstadisticasDB;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;


public class EstadisticasController implements Initializable {

    @FXML private Button button;
    @FXML private DatePicker dateDesde;
    @FXML private DatePicker dateHasta;
    @FXML private TableView tablaVisitasDiariasGeneral;
    @FXML private TableColumn columnaFecha;
    @FXML private TableColumn columnaTotalVisitas;
    @FXML private TableView tablaEnero;
    @FXML private TableColumn colFechaEnero;
    @FXML private TableColumn colVisEnero;
    @FXML private TableView tablaFebrero;
    @FXML private TableColumn colFechaFeb;
    @FXML private TableColumn colVisFebrero;
    @FXML private TableView tablaMarzo;
    @FXML private TableColumn colFechaMarzo;
    @FXML private TableColumn colVisMarzo;
    @FXML private TableView tablaAbril;
    @FXML private TableColumn colFechaAbril;
    @FXML private TableColumn colVisAbril;
    @FXML private TableView tablaMayo;
    @FXML private TableColumn colFechaMayo;
    @FXML private TableColumn colVisMayo;
    @FXML private TableView tablaJunio;
    @FXML private TableColumn colFechaJunio;
    @FXML private TableColumn colVisJunio;
    @FXML private TableView tablaJulio;
    @FXML private TableColumn colFechaJulio;
    @FXML private TableColumn colVisJulio;
    @FXML private TableView tablaAgosto;
    @FXML private TableColumn colFechaAgosto;
    @FXML private TableColumn colVisAgosto;
    @FXML private TableView tablaSeptiembre;
    @FXML private TableColumn colFechaSep;
    @FXML private TableColumn colVisSep;
    @FXML private TableView tablaOctubre;
    @FXML private TableColumn colFechaOct;
    @FXML private TableColumn colVisOct;
    @FXML private TableView tablaNoviembre;
    @FXML private TableColumn colFechaNov;
    @FXML private TableColumn colVisNov;
    @FXML private TableView tablaDiciembre;
    @FXML private TableColumn colFechaDic;
    @FXML private TableColumn colVisDic;
    @FXML private TextField txtMayor;
    @FXML private TextField txtMenor;

    @FXML private LineChart<String, Integer> graph;
    private List<EstadisticasModel> lista;
    private int anio = Calendar.getInstance().get(Calendar.YEAR);

    @FXML private void AccionPintar(ActionEvent event) {
        pintarGrafica();
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        columnaFecha.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        columnaTotalVisitas.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaVisitasDiariasGeneral.setEditable(true);
        tablaVisitasDiariasGeneral.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaVisitasDiariasGeneral.getSelectionModel().setCellSelectionEnabled(false);
        preparaTablasHistoricas();
    }

    private void pintarGrafica (){
        ObservableList<EstadisticasModel> data;
        ObservableList<XYChart.Series<String, Integer>> lineChartData = FXCollections.observableArrayList();
        LineChart.Series<String, Integer> series = new LineChart.Series<String, Integer>();
        EstadisticasDB estadisticasDB = new EstadisticasDB();
        dateDesde.getValue();
        dateHasta.getValue();
        int top = 0;
        int menor = 0;
        String fechaTop = null;
        lista = estadisticasDB.consultarVisitasDiariasPorRangoGrafica(dateDesde.getValue().toString(), dateHasta.getValue().toString());
        data = estadisticasDB.consultarVisitasDiariasPorRangoTabla(dateDesde.getValue().toString(), dateHasta.getValue().toString());
        tablaVisitasDiariasGeneral.setItems(data);
        if(!lista.isEmpty()){
            top = lista.get(0).getTotal();
            fechaTop = lista.get(0).getFechas();
        }
        for(EstadisticasModel elemento : lista){
            if(top <= elemento.getTotal()){
                top = elemento.getTotal();
                fechaTop = elemento.getFechas();
            }
        }
        txtMayor.setText(String.valueOf(top) + " " + fechaTop);
        txtMenor.setText(String.valueOf(menor));
        for (EstadisticasModel elemento : lista) {
            series.getData().add(new XYChart.Data<String, Integer>(elemento.getFechas(), elemento.getTotal()));
        }
        lineChartData.add(series);
        graph.createSymbolsProperty();
        graph.setCreateSymbols(true);
        graph.setData(lineChartData);
    }

    private void preparaTablasHistoricas(){
        EstadisticasDB estadisticasDB = new EstadisticasDB();
        ObservableList<EstadisticasModel> data;
        colFechaEnero.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisEnero.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaEnero.setEditable(true);
        tablaEnero.setPlaceholder(new Label("No hay datos del mes"));
        tablaEnero.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaEnero.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(1, anio);
        tablaEnero.setItems(data);

        colFechaFeb.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisFebrero.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaFebrero.setEditable(true);
        tablaFebrero.setPlaceholder(new Label("No hay datos del mes"));
        tablaFebrero.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaFebrero.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(2, anio);
        tablaFebrero.setItems(data);

        colFechaMarzo.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisMarzo.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaMarzo.setEditable(true);
        tablaMarzo.setPlaceholder(new Label("No hay datos del mes"));
        tablaMarzo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaMarzo.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(3, anio);
        tablaMarzo.setItems(data);

        colFechaAbril.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisAbril.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaAbril.setEditable(true);
        tablaAbril.setPlaceholder(new Label("No hay datos del mes"));
        tablaAbril.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaAbril.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(4, anio);
        tablaAbril.setItems(data);

        colFechaMayo.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisMayo.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaMayo.setEditable(true);
        tablaMayo.setPlaceholder(new Label("No hay datos del mes"));
        tablaMayo.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaMayo.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(5, anio);
        tablaMayo.setItems(data);

        colFechaJunio.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisJunio.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaJunio.setEditable(true);
        tablaJunio.setPlaceholder(new Label("No hay datos del mes"));
        tablaJunio.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaJunio.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(6, anio);
        tablaJunio.setItems(data);

        colFechaJulio.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisJulio.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaJulio.setEditable(true);
        tablaJulio.setPlaceholder(new Label("No hay datos del mes"));
        tablaJulio.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaJulio.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(7, anio);
        tablaJulio.setItems(data);

        colFechaAgosto.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisAgosto.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaAgosto.setEditable(true);
        tablaAgosto.setPlaceholder(new Label("No hay datos del mes"));
        tablaAgosto.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaAgosto.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(8, anio);
        tablaAgosto.setItems(data);

        colFechaSep.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisSep.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaSeptiembre.setEditable(true);
        tablaSeptiembre.setPlaceholder(new Label("No hay datos del mes"));
        tablaSeptiembre.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaSeptiembre.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(9, anio);
        tablaSeptiembre.setItems(data);

        colFechaOct.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisOct.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaOctubre.setEditable(true);
        tablaOctubre.setPlaceholder(new Label("No hay datos del mes"));
        tablaOctubre.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaOctubre.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(10, anio);
        tablaOctubre.setItems(data);

        colFechaNov.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisNov.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaNoviembre.setEditable(true);
        tablaNoviembre.setPlaceholder(new Label("No hay datos del mes"));
        tablaNoviembre.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaNoviembre.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(11, anio);
        tablaNoviembre.setItems(data);

        colFechaDic.setCellValueFactory(new PropertyValueFactory<>("fechas"));
        colVisDic.setCellValueFactory(new PropertyValueFactory<>("total"));
        tablaDiciembre.setEditable(true);
        tablaDiciembre.setPlaceholder(new Label("No hay datos del mes"));
        tablaDiciembre.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaDiciembre.getSelectionModel().setCellSelectionEnabled(false);
        data = estadisticasDB.consultarVisitasDiariasPorMesTabla(12, anio);
        tablaDiciembre.setItems(data);
    }


}