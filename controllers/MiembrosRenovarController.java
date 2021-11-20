package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.Adeudo;
import models.AdeudoDataTableModel;
import models.MiembrosModel;
import persistence.MiembrosDB;

import javax.swing.*;
import java.net.URL;
import java.sql.SQLException;
import java.util.*;

public class MiembrosRenovarController implements Initializable {

    @FXML
    private ComboBox comboMembresias;

    @FXML
    private TextField txtMontoMem;

    @FXML
    private CheckBox chkDescuentoEspecial;

    @FXML
    private TextField txtPorcenDescuento;

    @FXML
    private Button btnTotalMembresia;

    @FXML
    private Button btnCobrar;

    @FXML
    TableView tablaAdeudos;

    @FXML
    TableColumn IDADEUDO;

    @FXML
    TableColumn IDMIEMBRO;

    @FXML
    TableColumn CONCEPTO;

    @FXML
    TableColumn CANTIDADADEUDO;

    @FXML
    TableColumn FECHAADEUDO;

    @FXML
    AnchorPane anchorPaneRaiz;

    private MiembrosModel miembrosModel = new MiembrosModel();
    private double costoMembresia;
    private String idMiembro;
    private ObservableList<AdeudoDataTableModel> data;
    static double adeudoTotal = 0.0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        comboMembresias.getItems().add("Mensual");
        comboMembresias.getItems().add("Por día");
        comboMembresias.getItems().add("Cortesía");
        comboMembresias.getItems().add("Semanal");

        CONCEPTO.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        CANTIDADADEUDO.setCellValueFactory(new PropertyValueFactory<>("cantidadAdeudo"));
        FECHAADEUDO.setCellValueFactory(new PropertyValueFactory<>("fechaAdeudo"));
    }

    @FXML
    private void comboMembresiasOnChange(ActionEvent event) {
        MiembrosDB miembrosDB = new MiembrosDB();
        costoMembresia = miembrosDB.obtenerCostoMembresia(comboMembresias.getSelectionModel().getSelectedItem().toString());
        //adeudoTotal = costoMembresia + adeudoTotal;
        double importeTotal = costoMembresia + adeudoTotal;
        txtMontoMem.setText(String.valueOf(importeTotal));
        txtMontoMem.setStyle("-fx-font-weight: bold");
    }

    @FXML
    private void calculaPorcentaje(MouseEvent event){
        Double precioMembresia;
        Double porcentajeDescuento;
        precioMembresia = Double.valueOf(txtMontoMem.getText().toString());
        porcentajeDescuento = Double.valueOf(txtPorcenDescuento.getText().toString());
        Double totalMembresia = (precioMembresia*porcentajeDescuento)/100;
        totalMembresia = precioMembresia - totalMembresia;
        txtMontoMem.setText(String.valueOf(totalMembresia));
        txtMontoMem.setStyle("-fx-font-weight: bold");
    }

    @FXML
    private void cobrarMembresia() throws SQLException {
        MiembrosDB miembrosDB = new MiembrosDB();
        List<Adeudo> listaAdeudo = new ArrayList<>();
        String resultado = null;
        String tipoSuscripcion = comboMembresias.getSelectionModel().getSelectedItem().toString();
        Double cantidad_Pago = Double.valueOf(txtMontoMem.getText());
        java.util.Date fechaVencimiento = new java.util.Date();
        fechaVencimiento = validaFecha_Vencimiento(fechaVencimiento, tipoSuscripcion);
        //listaAdeudo = miembrosDB.consultarAdeudosMultas(miembrosModel.getIdMiembro());

        resultado = miembrosDB.cobrarMembresia(miembrosModel, tipoSuscripcion, cantidad_Pago, fechaVencimiento);
        if(resultado.contains("éxito")) {
            JOptionPane.showMessageDialog(null, resultado, "Resultado Cobro", JOptionPane.INFORMATION_MESSAGE);
        }else if(resultado.contains("error")){
            JOptionPane.showMessageDialog(null, resultado + " ," + " HDTRPM!", "Resultado Cobro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    public Date validaFecha_Vencimiento(Date utilDate, String tipoSuscripcion){
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT-7"));
        calendar.setTime(utilDate);
        if(tipoSuscripcion.equals("Mensual")){
            calendar.add(Calendar.DATE, 30);
        }else if(tipoSuscripcion.equals("Por Día")){
            calendar.add(Calendar.DATE, 1);
        }else if(tipoSuscripcion.equals("Cortesia")){
            calendar.add(Calendar.DATE, 0);
        }else if(tipoSuscripcion.equals("Semanal")) {
            calendar.add(Calendar.DATE, 7);
        }
        return utilDate = calendar.getTime();
    }

    public void receiveData(int idMiembro) {
        MiembrosDB miembrosDB = new MiembrosDB();
        //double adeudoTotal = costoMembresia;
        miembrosModel = miembrosDB.consultarMiembroPorIdMiembro(String.valueOf(idMiembro));
        tablaAdeudos.setEditable(false);
        tablaAdeudos.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaAdeudos.getSelectionModel().setCellSelectionEnabled(false);
        tablaAdeudos.setVisible(true);
        tablaAdeudos.setPlaceholder(new Label("No hay adeudos"));
        data = miembrosDB.consultarAdeudosMultasObs(miembrosModel.getIdMiembro());
        if(data.size() > 0) {
            tablaAdeudos.setItems(data);
        }
        for(AdeudoDataTableModel adeudo : data ){
            adeudoTotal =  adeudoTotal + adeudo.getCantidadAdeudo();
        }
        txtMontoMem.setText(String.valueOf(adeudoTotal));
        txtMontoMem.setStyle("-fx-font-weight: bold");

    }

}
