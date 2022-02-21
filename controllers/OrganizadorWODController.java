package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.Catalogo_Ejercicio_DTM;
import models.Clasificacion_WOD_DTM;
import models.RutinaDTM;
import persistence.WODDB;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class OrganizadorWODController implements Initializable {

    @FXML private TableView tablaWOD;
    @FXML private ComboBox cmbGpoMusc;
    @FXML private ComboBox cmbTipo;
    @FXML private TableColumn nombreCol;
    @FXML private TableColumn complejidadCol;
    @FXML private Button btnAgregarEjercicios;
    @FXML private TableView tablaWodDia;
    @FXML private TableColumn colEjercicio;
    @FXML private TableColumn colReps;
    @FXML private TableColumn colTiempo;
    @FXML private TableColumn colAction;
    @FXML private Button btnGuardarSet;
    @FXML private Button btnActualizarSet;
    @FXML private TextField txtNombreSet;
    @FXML private TextField txtTimeSet;

    private ObservableList<Catalogo_Ejercicio_DTM> data;
    private ObservableList<Catalogo_Ejercicio_DTM> seleccionados = FXCollections.observableArrayList();
    private ObservableList<Catalogo_Ejercicio_DTM> fija = FXCollections.observableArrayList();
    private ObservableList<Catalogo_Ejercicio_DTM> lstEjerciciosDia = FXCollections.observableArrayList();
    ObservableList<Catalogo_Ejercicio_DTM> listaFiltrada = FXCollections.observableArrayList();
    List<Clasificacion_WOD_DTM> listaGposMus = new ArrayList<>();
    private Stage stageThis;
    private RutinaDTM rutinaObj;
    private LocalDate eventDate;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        WODDB woddb =  new WODDB();
        btnGuardarSet.setVisible(true);
        btnActualizarSet.setVisible(false);
        listaGposMus.clear();
        cmbGpoMusc.getItems().add("Todos");
        cmbTipo.getItems().add("AMRAP");
        cmbTipo.getItems().add("EMOM");
        cmbTipo.getItems().add("FOR TIME");
        cmbTipo.getItems().add("TABATA");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombreEjercicio"));
        complejidadCol.setCellValueFactory(new PropertyValueFactory<>("complejidad"));
        colEjercicio.setCellValueFactory(new PropertyValueFactory<>("nombreEjercicio"));
        colReps.setCellFactory(TextFieldTableCell.forTableColumn());
        colReps.setCellValueFactory(new PropertyValueFactory<>("reps"));
        colTiempo.setCellFactory(TextFieldTableCell.forTableColumn());
        colTiempo.setCellValueFactory(new PropertyValueFactory<>("tiempo"));
        colAction.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));
        colEjercicio.setStyle( "-fx-alignment: CENTER;");
        colReps.setStyle( "-fx-alignment: CENTER;");
        colTiempo.setStyle( "-fx-alignment: CENTER;");
        colAction.setStyle( "-fx-alignment: CENTER;");

        colReps.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Catalogo_Ejercicio_DTM, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Catalogo_Ejercicio_DTM, String> t) {
                        ((Catalogo_Ejercicio_DTM) t.getTableView().getItems().get(t.getTablePosition().getRow())).setReps(t.getNewValue());
                        String newValue = t.getNewValue();
                        System.out.println(newValue);
                    }
                }
        );

        colTiempo.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<Catalogo_Ejercicio_DTM, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<Catalogo_Ejercicio_DTM, String> t) {
                        ((Catalogo_Ejercicio_DTM) t.getTableView().getItems().get(t.getTablePosition().getRow())).setTiempo(t.getNewValue());
                        String newValue = t.getNewValue();
                        System.out.println(newValue);
                    }
                }
        );

        Callback<TableColumn<Catalogo_Ejercicio_DTM, String>, TableCell<Catalogo_Ejercicio_DTM, String>> cellFactory =
                new Callback<TableColumn<Catalogo_Ejercicio_DTM, String>, TableCell<Catalogo_Ejercicio_DTM, String>>() {
                    @Override
                    public TableCell call(final TableColumn<Catalogo_Ejercicio_DTM, String> param) {
                        final TableCell<Catalogo_Ejercicio_DTM, String> cell = new TableCell<Catalogo_Ejercicio_DTM, String>() {
                            FileInputStream input, input2;
                            {
                                try {
                                    input = new FileInputStream("images/rest.png");
                                    input2 = new FileInputStream("images/minus.png");
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }

                            Image image = new Image(input);
                            Image image2 = new Image(input2);
                            ImageView imageView = new ImageView(image);
                            ImageView imageView2 = new ImageView(image2);
                            final Button btn = new Button("", imageView);
                            final Button btn2 = new Button("", imageView2);
                            HBox pane = new HBox(btn, btn2);
                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                    int posicionRest = 0;
                                    Catalogo_Ejercicio_DTM ejercicio = getTableView().getItems().get(getIndex());
                                    //Catalogo_Ejercicio_DTM seleccionado = (Catalogo_Ejercicio_DTM) tablaWodDia.getSelectionModel().getSelectedItem();
                                    for(int i = 0; i < fija.size(); i++ ){
                                        if(fija.get(i).getIdEjercicio() == ejercicio.getIdEjercicio()){
                                            posicionRest = i+1;
//                                            setTextFill(Color.BLACK); //The text in red
//                                            setStyle("-fx-background-color: blue");
                                            break;
                                        }
                                    }
                                    Catalogo_Ejercicio_DTM descanso = new Catalogo_Ejercicio_DTM();
                                    descanso.setNombreEjercicio("DESCANSO");
                                    descanso.setTiempo("00:00");
                                    descanso.setReps("N/A");
                                    fija.add(posicionRest, descanso);
                                    tablaWodDia.refresh();
                                    });
                                    btn2.setOnAction(event -> {
                                        Catalogo_Ejercicio_DTM ejercicio = getTableView().getItems().get(getIndex());
                                        fija.remove(ejercicio);
                                        tablaWodDia.setItems(fija);
                                    });
                                    setGraphic(pane);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        colAction.setCellFactory(cellFactory);

//        Callback<TableColumn<Catalogo_Ejercicio_DTM, String>, TableCell<Catalogo_Ejercicio_DTM, String>> cellFactory2 =
//                new Callback<TableColumn<Catalogo_Ejercicio_DTM, String>, TableCell<Catalogo_Ejercicio_DTM, String>>() {
//
//
//                    @Override
//                    public TableCell call(final TableColumn<Catalogo_Ejercicio_DTM, String> param) {
//                        final TableCell<Catalogo_Ejercicio_DTM, String> cell2 = new TableCell<Catalogo_Ejercicio_DTM, String>() {
//
//                            @Override
//                            public void updateItem(String item, boolean empty) {
//                                super.updateItem(item, empty);
//                                if (empty) {
//                                    setGraphic(null);
//                                    setText(null);
//                                } else {
//                                    Catalogo_Ejercicio_DTM auxEjer = getTableView().getItems().get(getIndex());
//                                    if (auxEjer.getNombreEjercicio().equals("DESCANSO")) {
//                                        setTextFill(Color.BLACK); //The text in red
//                                        setStyle("-fx-background-color: blue"); //The background of the cell in yellow
//                                    }
//                                }
//                            }
//                        };
//                        return cell2;
//                    }
//                };
//
//        colEjercicio.setCellValueFactory(cellFactory2);

//        tablaWodDia.setRowFactory(row -> new TableRow<Catalogo_Ejercicio_DTM>(){
//            @Override
//            public void updateItem(Catalogo_Ejercicio_DTM item, boolean empty){
//                super.updateItem(item, empty);
//
//                if (item == null || empty) {
//                    setStyle("");
//                } else {
//                    //Now 'item' has all the info of the Person in this row
//                    if (item.getNombreEjercicio().equals("DESCANSO")) {
//                        //We apply now the changes in all the cells of the row
//                        for(int i=0; i<getChildren().size();i++){
//                            ((Labeled) getChildren().get(0)).setTextFill(Color.BLACK);
//                            ((Labeled) getChildren().get(0)).setStyle("-fx-background-color: blue");
//                        }
//                    } else {
//                        if(getTableView().getSelectionModel().getSelectedItems().contains(item)){
//                            for(int i=0; i<getChildren().size();i++){
//                                ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);;
//                            }
//                        }
//                        else{
//                            for(int i=0; i<getChildren().size();i++){
//                                ((Labeled) getChildren().get(i)).setTextFill(Color.BLACK);;
//                            }
//                        }
//                    }
//                }
//            }
//        });

        tablaWOD.setEditable(true);
        tablaWOD.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablaWOD.getSelectionModel().setCellSelectionEnabled(false);
        tablaWodDia.setEditable(true);
        tablaWodDia.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tablaWodDia.getSelectionModel().setCellSelectionEnabled(false);
        data = woddb.consultaEjercicios("Todos");
        tablaWOD.setItems(data);
        listaGposMus = woddb.consultaGruposMusculares();
        for(Clasificacion_WOD_DTM grupo : listaGposMus){
            cmbGpoMusc.getItems().add(grupo.getGrupoMuscular());
        }

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
                            //recargaTablaEjercicios();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        btnAgregarEjercicios.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                seleccionados = tablaWOD.getSelectionModel().getSelectedItems();
                for(Catalogo_Ejercicio_DTM selecc : seleccionados){
                    fija.add(selecc);
                }
                tablaWodDia.setItems(fija);
            }
        });
    }

    public void crearNuevoEjercicio(){
        try {
            int [] numbers = new int [] { 0, 1, 2, 99 };
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

    public void cancelar(){
        stageThis.close();
    }

    public void receiveData(Stage stage, ObservableList<Catalogo_Ejercicio_DTM> lstEjerciciosDia, RutinaDTM rutinaObj) {
        if((!lstEjerciciosDia.isEmpty() || lstEjerciciosDia != null) && rutinaObj != null) {
            btnGuardarSet.setVisible(false);
            btnActualizarSet.setVisible(true);
            this.lstEjerciciosDia = lstEjerciciosDia;
            this.rutinaObj = rutinaObj;
            tablaWodDia.setItems(lstEjerciciosDia);
                txtNombreSet.setText(rutinaObj.getNombre_set());
                txtNombreSet.setDisable(true);
                txtTimeSet.setText(rutinaObj.getTiempo_por_set());
                cmbTipo.setValue(rutinaObj.getTipo());

        }
        stageThis = stage;
    }

    public void receiveData(Stage stage, LocalDate eventDate) {
        stageThis = stage;
        this.eventDate = eventDate;
    }

    public void guardarSet() {
        try {
            if (!txtNombreSet.getText().isEmpty() && !txtTimeSet.getText().isEmpty()) {
                String resultado = null;
                WODDB wodbb = new WODDB();
                List<RutinaDTM> listaEjercicios = new ArrayList<>();

                for (Catalogo_Ejercicio_DTM ejercicio : fija) {
                    RutinaDTM objEjercicio = new RutinaDTM();
                    objEjercicio.setNombre_set(txtNombreSet.getText());
                    objEjercicio.setTiempo_por_set(txtTimeSet.getText());
                    objEjercicio.setTipo((String) cmbTipo.getValue());
                    objEjercicio.setNombre_ejercicio(ejercicio.getNombreEjercicio());
                    objEjercicio.setReps(ejercicio.getReps());
                    objEjercicio.setTiempo(ejercicio.getTiempo());
                    listaEjercicios.add(objEjercicio);
                }
                resultado = wodbb.guardaSetEjercicios(listaEjercicios, eventDate);
                JOptionPane.showMessageDialog(null, resultado, "Mensaje Informativo de Planificador", JOptionPane.INFORMATION_MESSAGE);
                fija.clear();
                txtNombreSet.clear();
                txtTimeSet.clear();
                tablaWodDia.setItems(fija);
            } else {
                JOptionPane.showMessageDialog(null, "Todos los campos deben llenarse correctamente.", "Mensaje Informativo de Planificador", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {

        }
    }


    public void actualizarSet() {
        try {
            if (!txtNombreSet.getText().isEmpty() && !txtTimeSet.getText().isEmpty()) {
                String resultado = null;
                WODDB wodbb = new WODDB();
                List<RutinaDTM> listaEjercicios = new ArrayList<>();

                for (Catalogo_Ejercicio_DTM ejercicio : fija) {
                    RutinaDTM objEjercicio = new RutinaDTM();
                    objEjercicio.setTiempo_por_set(txtTimeSet.getText());
                    objEjercicio.setTipo((String) cmbTipo.getValue());
                    objEjercicio.setNombre_ejercicio(ejercicio.getNombreEjercicio());
                    objEjercicio.setReps(ejercicio.getReps());
                    objEjercicio.setTiempo(ejercicio.getTiempo());
                    objEjercicio.setNombre_set(txtNombreSet.getText());
                    listaEjercicios.add(objEjercicio);
                }
                resultado = wodbb.actualizarSetPorDia(txtNombreSet.getText(), listaEjercicios, eventDate);
                JOptionPane.showMessageDialog(null, resultado, "Mensaje Informativo de Planificador", JOptionPane.INFORMATION_MESSAGE);
                fija.clear();
                tablaWodDia.setItems(fija);
            } else {
                JOptionPane.showMessageDialog(null, "Todos los campos deben llenarse correctamente.", "Mensaje Informativo de Planificador", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {

        }
    }



}
