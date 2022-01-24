package controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import eu.mihosoft.scaledfx.ScalableContentPane;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Catalogo_Ejercicio_DTM;
import models.Clasificacion_WOD_DTM;
import persistence.WODDB;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class PlanificadorController implements Initializable {

    @FXML private JFXDrawer drawer;
    @FXML private JFXHamburger hamburger;
    @FXML private VBox box;
    @FXML private TableView tablaWOD;
    @FXML private ComboBox cmbGpoMusc;
    @FXML private TableColumn nombreCol;
    @FXML private TableColumn complejidadCol;

    private ObservableList<Catalogo_Ejercicio_DTM> data;
    ObservableList<Catalogo_Ejercicio_DTM> listaFiltrada = FXCollections.observableArrayList();;
    List<Clasificacion_WOD_DTM> listaGposMus = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WODDB woddb =  new WODDB();
        listaGposMus.clear();
        cmbGpoMusc.getItems().add("Todos");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombreEjercicio"));
        complejidadCol.setCellValueFactory(new PropertyValueFactory<>("complejidad"));
        tablaWOD.setEditable(true);
        tablaWOD.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaWOD.getSelectionModel().setCellSelectionEnabled(false);
        data = woddb.consultaEjercicios("Todos");
        tablaWOD.setItems(data);
        listaGposMus = woddb.consultaGruposMusculares();
        for(Clasificacion_WOD_DTM grupo : listaGposMus){
            cmbGpoMusc.getItems().add(grupo.getGrupoMuscular());
        }

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

        cmbGpoMusc.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                listaFiltrada.clear();
                if(newValue != null && newValue.equals("Todos")) {
                    tablaWOD.setItems(data);
                }else if(newValue != null && newValue.equals(cmbGpoMusc.getValue()) && !cmbGpoMusc.getValue().equals("Todos")){
                        for(Catalogo_Ejercicio_DTM ejercicio : data) {
                            if (ejercicio.getGrupoMuscular().equals(cmbGpoMusc.getValue())) {
                                listaFiltrada.add(ejercicio);
                            }
                        }
                        tablaWOD.setItems(listaFiltrada);
                }
            }
        });

        tablaWOD.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        try {
                            Catalogo_Ejercicio_DTM objEjercicio = (Catalogo_Ejercicio_DTM) tablaWOD.getSelectionModel().getSelectedItem();
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModalWOD.fxml"));
                            Parent root = loader.load();
                            CreadorEjerciciosController scene2Controller = loader.getController();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setTitle("Editar Ejercicio");
                            scene2Controller.receiveData(objEjercicio, stage, listaGposMus);
                            stage.initModality(Modality.APPLICATION_MODAL);
                            stage.showAndWait();
                            recargaTablaEjercicios();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public void regresarMenuPrincipal(MouseEvent event) {
        try {
            Stage este = (Stage) ((Node) event.getSource()).getScene().getWindow();
            este.close();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Hub.fxml"));
            Parent root = fxmlLoader.load();
            ScalableContentPane scp = new ScalableContentPane(root);
            Stage stage = new Stage();
            stage.setMaximized(true);
            stage.setScene(new Scene(scp));
            stage.show();

        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    public void crearNuevoEjercicio(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ModalWOD.fxml"));
            Parent root = loader.load();
            CreadorEjerciciosController scene2Controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Agregar Ejercicio");
            scene2Controller.receiveData(null, stage, listaGposMus);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            recargaTablaEjercicios();
            recargaGposMusc();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public void recargaTablaEjercicios() {
        WODDB woddb = new WODDB();
        tablaWOD.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombreEjercicio"));
        complejidadCol.setCellValueFactory(new PropertyValueFactory<>("complejidad"));

        tablaWOD.setEditable(true);
        tablaWOD.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaWOD.getSelectionModel().setCellSelectionEnabled(false);
        data = woddb.consultaEjercicios("Todos");
        tablaWOD.setItems(data);
    }

    public void recargaGposMusc(){
        WODDB woddb = new WODDB();
        listaGposMus.clear();
        listaGposMus = woddb.consultaGruposMusculares();
        cmbGpoMusc.getItems().clear();
        cmbGpoMusc.getItems().add("Todos");
        for(Clasificacion_WOD_DTM grupo : listaGposMus){
            cmbGpoMusc.getItems().add(grupo.getGrupoMuscular());
        }
    }
}
