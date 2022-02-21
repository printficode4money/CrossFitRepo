package controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import eu.mihosoft.scaledfx.ScalableContentPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.InventarioDTM;
import persistence.InventarioVentasDB;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class VentasController implements Initializable{

        @FXML private TableView tablaInventario;
        @FXML private TableColumn columnaNombre;
        @FXML private TableColumn columnaDescripcion;
        @FXML private TableColumn columnaPrecio;
        @FXML private TableColumn columnaExistencias;
        @FXML private TableColumn columnaClave;
        @FXML private TextField txtNombre;
        @FXML private TextField txtDescripcion;
        @FXML private TextField txtPrecio;
        @FXML private TextField txtExistencias;
        @FXML private Button btnAgregarMenu;
        @FXML private Button btnBuscar;
        @FXML private Button btnEliminar;
        @FXML private Button btnBuscarInv;
        @FXML private Button btnAgregarFila;
        @FXML private Label lblBuscar;
        @FXML private Label lblAgregar;
        @FXML private ComboBox cmbBuscar;
        @FXML private TextField txtBusqueda;
        @FXML private Button btnRestablecer;
        @FXML private Button btnEliminarFila;
        @FXML private Button btnEditar;
        @FXML private JFXDrawer drawer;
        @FXML private JFXHamburger hamburger;
        @FXML private VBox box;
        @FXML private Button btnVenta;
        @FXML private Button btnAgregaAlCarrito;
        private ObservableList<InventarioDTM> seleccionados = FXCollections.observableArrayList();
        private ObservableList<InventarioDTM> carrito = FXCollections.observableArrayList();
        Stage stageThis;

        @Override
        public void initialize(URL url, ResourceBundle rb) {
            drawer.setSidePane(box);
            HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(hamburger);
            transition.setRate(-1);
            hamburger.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
                transition.setRate(transition.getRate() * -1);
                transition.play();

                if (drawer.isOpened()) {
                    drawer.close();
                } else {
                    drawer.open();
                }
            });

            cmbBuscar.getItems().add("Nombre");
            cmbBuscar.getItems().add("Descripción");
            InventarioVentasDB inventarioVentasDB = new InventarioVentasDB();
            columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            //columnaNombre.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            //columnaDescripcion.setCellFactory(TextFieldTableCell.forTableColumn());
            columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            //columnaPrecio.setCellFactory(new PropertyValueFactory<>("precio"));
            columnaExistencias.setCellValueFactory(new PropertyValueFactory<>("existencias"));
            //columnaExistencias.setCellValueFactory(TextFieldTableCell.forTableColumn(new IntegerStringConverter()));
            columnaClave.setVisible(false);
            tablaInventario.setEditable(true);
            tablaInventario.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            tablaInventario.getSelectionModel().setCellSelectionEnabled(false);
            data = inventarioVentasDB.consultaInventario();
            tablaInventario.setItems(data);
            cmbBuscar.setVisible(false);
            txtBusqueda.setVisible(false);
            btnBuscarInv.setVisible(false);
            btnRestablecer.setVisible(false);
            btnEliminarFila.setVisible(false);
            btnEditar.setVisible(false);
            lblBuscar.setVisible(false);

            btnAgregaAlCarrito.setOnAction(new EventHandler<ActionEvent>() {
                @Override public void handle(ActionEvent e) {
                    seleccionados = tablaInventario.getSelectionModel().getSelectedItems();
                    boolean existeEnCarrito = false;
                    for(InventarioDTM selecc : seleccionados){
                        selecc.setCantidadVenta(1); //default venta 1
                        for(InventarioDTM artCarrito : carrito){
                            if(selecc.getIdInventario() == artCarrito.getIdInventario()){
                                existeEnCarrito = true;
                                break;
                            }
                        }
                        if(!existeEnCarrito){
                            carrito.add(selecc);
                        }
                        existeEnCarrito = false;
                    }
                }
            });
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
            btnRestablecer.setVisible(false);
            btnEliminarFila.setVisible(false);
            btnEditar.setVisible(false);
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
            btnRestablecer.setVisible(true);
            btnEliminarFila.setVisible(true);
            btnEditar.setVisible(true);
            lblBuscar.setVisible(true);
        }

        public void guardarArticuloInventario() {
            String resultado;
            InventarioVentasDB inventarioVentasDB = new InventarioVentasDB();
            try {
                if (!txtNombre.getText().isEmpty() && !txtDescripcion.getText().isEmpty() && !txtPrecio.getText().isEmpty() && !txtExistencias.getText().isEmpty()) {
                    InventarioDTM inventarioDTM = new InventarioDTM();
                    inventarioDTM.setNombre(txtNombre.getText());
                    inventarioDTM.setDescripcion(txtDescripcion.getText());
                    inventarioDTM.setPrecio(txtPrecio.getText());
                    inventarioDTM.setExistencias(Integer.parseInt(txtExistencias.getText()));
                    resultado = inventarioVentasDB.guardarArticuloInventario(inventarioDTM);
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
            InventarioVentasDB inventarioVentasDB = new InventarioVentasDB();
            tablaInventario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
            columnaNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            columnaDescripcion.setCellValueFactory(new PropertyValueFactory<>("descripcion"));
            columnaPrecio.setCellValueFactory(new PropertyValueFactory<>("precio"));
            columnaExistencias.setCellValueFactory(new PropertyValueFactory<>("existencias"));
            tablaInventario.setEditable(true);
            tablaInventario.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            tablaInventario.getSelectionModel().setCellSelectionEnabled(false);
            data = inventarioVentasDB.consultaInventario();
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
            tablaInventario.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
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

        public void eliminarFila(){
            String resultado;
            InventarioDTM inventarioObj = (InventarioDTM) tablaInventario.getSelectionModel().getSelectedItem();
            InventarioVentasDB inventarioVentasDB = new InventarioVentasDB();
            resultado = inventarioVentasDB.eliminarFilaInventario(inventarioObj);
            JOptionPane.showMessageDialog(null, resultado, "Mensaje Informativo de Inventario", JOptionPane.INFORMATION_MESSAGE);
            recargaTablaInventario();
        }

        public void editarFila(){
            try {
                InventarioDTM inventarioObj = (InventarioDTM) tablaInventario.getSelectionModel().getSelectedItem();
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModificaInventarioModal.fxml"));
                Parent root = loader.load();
                ModificaArticuloInventario scene2Controller = loader.getController();
                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.setTitle("Editar Inventario");
                scene2Controller.receiveData(inventarioObj, stage);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.showAndWait();
                recargaTablaInventario();
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        public void regresarMenuPrincipal(MouseEvent event){
            try {
                Stage este = (Stage)((Node) event.getSource()).getScene().getWindow();
                este.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Hub.fxml"));
                Parent root = fxmlLoader.load();
                ScalableContentPane scp = new ScalableContentPane (root);
                Stage stage = new Stage();
                stage.setMaximized(true);
                stage.setScene(new Scene(scp));
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }

    public void nuevaVenta(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/NvaVentaModal.fxml"));
            Parent root = loader.load();
            NuevaVentaController scene2Controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Nueva Venta");
            scene2Controller.receiveData(carrito, stage);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void cancelar(){
        stageThis.close();
    }

}