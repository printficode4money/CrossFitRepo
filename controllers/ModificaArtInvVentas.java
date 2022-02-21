package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.InventarioDTM;
import persistence.InventarioVentasDB;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class ModificaArtInvVentas implements Initializable {

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtExistencias;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnGuardar;

    InventarioDTM inventarioObj = new InventarioDTM();
    private Stage stageThis;
    private int idInventario;

    public void initialize(URL url, ResourceBundle rb) {

    }

    public void receiveData(InventarioDTM inventarioObj, Stage stage) {
        txtNombre.setText(inventarioObj.getNombre());
        txtDescripcion.setText(inventarioObj.getDescripcion());
        txtPrecio.setText(String.valueOf(inventarioObj.getPrecio()));
        txtExistencias.setText(String.valueOf(inventarioObj.getExistencias()));
        idInventario = inventarioObj.getIdInventario();
        stageThis = stage;
    }

    public void actualizarFilaInventario(){
        String resultado;
        InventarioVentasDB inventarioDB = new InventarioVentasDB();
        inventarioObj.setIdInventario(idInventario);
        inventarioObj.setNombre(txtNombre.getText());
        inventarioObj.setDescripcion(txtDescripcion.getText());
        //inventarioObj.setPrecio(Double.parseDouble(txtPrecio.getText()));
        inventarioObj.setPrecio(txtPrecio.getText());
        inventarioObj.setExistencias(Integer.parseInt(txtExistencias.getText()));
        resultado = inventarioDB.actualizaFilaInventario(inventarioObj);
        JOptionPane.showMessageDialog(null, resultado, "Actualización de Inventario", JOptionPane.INFORMATION_MESSAGE);
        stageThis.close();
//        int input = JOptionPane.showOptionDialog(null, resultado, "Actualización de Inventario", JOptionPane.INFORMATION_MESSAGE, JOptionPane.INFORMATION_MESSAGE, null, null, null);
//
//        if(input == JOptionPane.OK_OPTION)
//        {
//            stageThis.close();
//        }
    }

    public void cancelarActualizar(){
        stageThis.close();
    }

}

