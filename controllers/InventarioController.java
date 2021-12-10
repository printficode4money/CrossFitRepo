package controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

public class InventarioController implements Initializable {

    @FXML
    private TableView tablaInventario;

    @FXML
    private TableColumn columnaNombre;

    @FXML
    private TableColumn columnaDescripcion;

    @FXML
    private TableColumn columnaPrecio;

    @FXML
    private TableColumn columnaExistencias;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtDescripcion;

    @FXML
    private TextField txtPrecio;

    @FXML
    private TextField txtExistencias;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }
}
