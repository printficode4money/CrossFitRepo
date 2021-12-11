package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import models.InventarioDTM;
import persistence.InventarioDB;

import javax.swing.*;
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

    @FXML
    private Button btnAgregarMenu;

    @FXML
    private Button btnBuscar;

    @FXML
    private Button btnEliminar;

//    @FXML
//    private TextField txtBuscarNombre;

    @FXML
    private Button btnBuscarInv;

//    @FXML
//    private TextField txtBuscarDescrip;
//
//    @FXML
//    private TextField txtBuscarPrecio;
//
//    @FXML
//    private TextField txtBuscarExist;

    @FXML
    private Button btnAgregarFila;

    @FXML
    private Label lblBuscar;

    @FXML
    private Label lblAgregar;

    @FXML
    private ComboBox cmbBuscar;

    @FXML
    private TextField txtBusqueda;

    @FXML
    private Button btnRestablecer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbBuscar.getItems().add("Nombre");
        cmbBuscar.getItems().add("Descripción");
        InventarioDB inventarioDB = new InventarioDB();
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaExistencias.setCellValueFactory(new PropertyValueFactory<>("existencias"));
        tablaInventario.setEditable(true);
        tablaInventario.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaInventario.getSelectionModel().setCellSelectionEnabled(true);
        data = inventarioDB.consultaInventario();
        tablaInventario.setItems(data);
        cmbBuscar.setVisible(false);
        txtBusqueda.setVisible(false);
        btnBuscarInv.setVisible(false);
        lblBuscar.setVisible(false);

//        tablaInventario.setOnMouseClicked(new EventHandler<MouseEvent>() {
//            @Override
//            public void handle(MouseEvent mouseEvent) {
//                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
//                    if(mouseEvent.getClickCount() == 2){
////                        try {
//                            InventarioDTM filaInventario = (InventarioDTM) tablaInventario.getSelectionModel().getSelectedItem();
//
//
//
////                        } catch (IOException e) {
////                            e.printStackTrace();
////                        }
//                    }
//                }
//            }
//        });

    }

    private ObservableList<InventarioDTM> data;

    public void opcionesAgregarMenu() {
        txtNombre.setVisible(true);
        txtDescripcion.setVisible(true);
        txtPrecio.setVisible(true);
        txtExistencias.setVisible(true);
        btnAgregarFila.setVisible(true);
        lblAgregar.setVisible(true);
        cmbBuscar.setVisible(false);
        txtBusqueda.setVisible(false);
        btnBuscarInv.setVisible(false);
        lblBuscar.setVisible(false);
    }

    public void opcionesBuscarMenu() {
        txtNombre.setVisible(false);
        txtDescripcion.setVisible(false);
        txtPrecio.setVisible(false);
        txtExistencias.setVisible(false);
        btnAgregarFila.setVisible(false);
        lblAgregar.setVisible(false);
        cmbBuscar.setVisible(true);
        txtBusqueda.setVisible(true);
        btnBuscarInv.setVisible(true);
        lblBuscar.setVisible(true);
    }

    public void eliminarFila() {

    }

    public void guardarArticuloInventario() {
        String resultado;
        InventarioDB inventarioDB = new InventarioDB();
        try {
            if (!txtNombre.getText().isEmpty() && !txtDescripcion.getText().isEmpty() && !txtPrecio.getText().isEmpty() && !txtExistencias.getText().isEmpty()) {
                InventarioDTM inventarioDTM = new InventarioDTM();
                inventarioDTM.setNombre(txtNombre.getText());
                inventarioDTM.setDescripcion(txtDescripcion.getText());
                inventarioDTM.setPrecio(Double.parseDouble(txtPrecio.getText()));
                inventarioDTM.setExistencias(Integer.parseInt(txtExistencias.getText()));
                resultado = inventarioDB.guardarArticuloInventario(inventarioDTM);
                JOptionPane.showMessageDialog(null, resultado, "Mensaje Informativo de Inventario", JOptionPane.INFORMATION_MESSAGE);
                txtNombre.clear();
                txtDescripcion.clear();
                txtPrecio.clear();
                txtExistencias.clear();
                recargaTablaInventario();
            } else {
                JOptionPane.showMessageDialog(null, "Todos los campos deben llenarse correctamente.", "Mensaje Informativo de Inventario", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void recargaTablaInventario() {
        InventarioDB inventarioDB = new InventarioDB();
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaExistencias.setCellValueFactory(new PropertyValueFactory<>("existencias"));
        tablaInventario.setEditable(true);
        tablaInventario.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaInventario.getSelectionModel().setCellSelectionEnabled(true);
        data = inventarioDB.consultaInventario();
        tablaInventario.setItems(data);
    }

    public void recargaTablaInventarioConBusqueda(ObservableList listaBusqueda) {
        columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
        columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
        columnaExistencias.setCellValueFactory(new PropertyValueFactory<>("existencias"));
        tablaInventario.setEditable(true);
        tablaInventario.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaInventario.getSelectionModel().setCellSelectionEnabled(true);
        tablaInventario.setItems(listaBusqueda);
    }

    public void buscarArticuloInventario() {
        Object selectedItem = cmbBuscar.getSelectionModel().getSelectedItem();
        if ("Nombre".equals(selectedItem)) {
            buscarArticuloNombre(txtBusqueda.getText());
        } else if ("Descripción".equals(selectedItem)) {
            buscarArticuloDescripcion(txtBusqueda.getText());
        } else {
            JOptionPane.showMessageDialog(null, "Opción inválida.", "Mensaje Informativo de Inventario", JOptionPane.INFORMATION_MESSAGE);
        }
        txtNombre.clear();
    }

    public void buscarArticuloNombre(String nombre) {
        ObservableList<InventarioDTM> listaBusqueda;
        listaBusqueda = FXCollections.observableArrayList();
        for (InventarioDTM articulo : data) {
            if(articulo.getNombre().equals(nombre) || articulo.getNombre().contains(nombre)) {
                listaBusqueda.add(articulo);
            }
        }
        if(!listaBusqueda.isEmpty()){
            recargaTablaInventarioConBusqueda(listaBusqueda);
        }
    }

    public void buscarArticuloDescripcion(String descripcion) {
        ObservableList<InventarioDTM> listaBusqueda;
        listaBusqueda = FXCollections.observableArrayList();
        for (InventarioDTM articulo : data) {
            if(articulo.getDescripcion().equals(descripcion) || articulo.getDescripcion().contains(descripcion)) {
                listaBusqueda.add(articulo);
            }
        }
        if(!listaBusqueda.isEmpty()){
            recargaTablaInventarioConBusqueda(listaBusqueda);
        }
    }



}
