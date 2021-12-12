package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import models.InventarioDTM;

import java.net.URL;
import java.util.ResourceBundle;

public class ModificaArticuloInventario implements Initializable {

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

    public void initialize(URL url, ResourceBundle rb) {

    }

    public void receiveData(InventarioDTM inventarioObj) {
    txtNombre.setText(inventarioObj.getNombre());
    txtDescripcion.setText(inventarioObj.getDescripcion());
    txtPrecio.setText(String.valueOf(inventarioObj.getPrecio()));
    txtExistencias.setText(String.valueOf(inventarioObj.getExistencias()));
    }

    public void actualizarFilaInventario(InventarioDTM inventarioObj){

    }
}
