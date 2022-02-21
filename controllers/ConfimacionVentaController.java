package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.InventarioDTM;
import persistence.VentasDB;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfimacionVentaController implements Initializable {

    @FXML private ComboBox comboFormaPago;
    @FXML private TextField txtRecibido;
    @FXML private TextField txtCambio;
    @FXML private TextField txtTotalCobro;
    private Stage stageThis;
    private double totalCobro;
    private ObservableList<InventarioDTM> carrito = FXCollections.observableArrayList();

    public void initialize(URL url, ResourceBundle rb) {
        comboFormaPago.getItems().add("Efectivo");
        comboFormaPago.getItems().add("Transferencia");
        comboFormaPago.getItems().add("Tarjeta");
        txtTotalCobro.setEditable(false);
        txtCambio.setEditable(false);
        txtCambio.setText("0.0");
    }

    public void cancelar(){
        stageThis.close();
    }

    public void cobrarVenta(){
        String resultado = null;
        VentasDB ventasDB = new VentasDB();
        double recibido = Double.parseDouble(txtRecibido.getText());
        double cambio = Double.parseDouble(txtCambio.getText());
        resultado = ventasDB.guardarVenta(totalCobro, (String) comboFormaPago.getValue(), recibido, cambio, carrito);
        if(resultado.contains("éxito")){
            ventasDB.actualizaInventario(carrito);
        }
        JOptionPane.showMessageDialog(null, resultado, "Mensaje de Ventas", JOptionPane.INFORMATION_MESSAGE);
        stageThis.close();
    }

    public void receiveData(Stage stage, Double total, ObservableList<InventarioDTM> carrito) {
        this.carrito = carrito;
        totalCobro = total;
        txtTotalCobro.setText(String.valueOf(totalCobro));
        stageThis = stage;
    }
}