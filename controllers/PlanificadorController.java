package controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import controllers.ReservacionesExt.*;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Catalogo_Ejercicio_DTM;
import models.Clasificacion_WOD_DTM;
import persistence.ReservacionesDB;
import persistence.WODDB;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PlanificadorController implements Initializable {

    @FXML private JFXDrawer drawer;
    @FXML private JFXHamburger hamburger;
    @FXML private VBox box;
    @FXML private TableView tablaWOD;
    @FXML private ComboBox cmbGpoMusc;
    @FXML private TableColumn nombreCol;
    @FXML private TableColumn complejidadCol;
    @FXML private GridPane root;
    @FXML private TitledPane paneRutinas;
    @FXML private Accordion accPanes;

    private ObservableList<Catalogo_Ejercicio_DTM> data;
    ObservableList<Catalogo_Ejercicio_DTM> listaFiltrada = FXCollections.observableArrayList();;
    List<Clasificacion_WOD_DTM> listaGposMus = new ArrayList<>();
    private DatePickerSkin dps;
    private EventManager eventManager = new EventManager();

    public PlanificadorController() {
        WODDB woddb = new WODDB();
        eventManager.initEvents(woddb.consultaWOD());
        //eventManager.initEvents(reservacionesDB.read());
//        setEventDate(dc.getItem());
//        this.dc = dc;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        accPanes.setExpandedPane(paneRutinas);
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

        try{
            DatePicker dp = new PlanificadorDatePickerExt(LocalDate.now(), this);
            dps = new DatePickerSkin(dp);
            DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
            Node popupContent2 = dps.getPopupContent();
            root.getChildren().add(popupContent2);
        }catch (Exception e){
            e.printStackTrace();
        }
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

    public void updateDateCell(DateCell dc, LocalDate item) {
        dc.setId(null);
        handleToday(dc, item);
        handleEventAdded(dc, item);
        handleEventDeleted(dc, item);
        addHandlers(dc, item);
    }

    private void handleToday(DateCell dc, LocalDate item) {
        if (item.isEqual(LocalDate.now())) {
            if (!eventManager.getEventsByDate(item).isEmpty()) {
                dc.setId("date-cell-today-event-"+ Settings.getData().cellColor);
            } else {
                dc.setId("date-cell-today");
            }
        }
    }

    private void handleEventAdded(DateCell dc, LocalDate item) {
        if (!eventManager.getEventsByDate(item).isEmpty()) {
            dc.setId("date-cell-event-"+ Settings.getData().cellColor);
        }

        //notifyController.handleEventsAdded(item);
    }

    private void handleEventDeleted(DateCell dc, LocalDate item) {
        if (eventManager.getEventsByDate(item).isEmpty()) {
            if (item.isEqual(LocalDate.now())) {
                dc.setId("date-cell-today");
            } else {
                dc.setId("date-cell-default");
            }
        }

       // notifyController.handleEventsDeleted();
    }

    public void handleCellEvent(DateCell dc, MouseEvent e) {
        if (e.getClickCount() == 2) {
            createEventMenu(dc);
        }
    }

    public void handleCellEvent(DateCell dc, KeyEvent e) {
        if (e.getCode().equals(KeyCode.ENTER)) {
            createEventMenu(dc);
        }
    }

    private void addHandlers(DateCell dc, LocalDate item) {
        dc.addEventHandler(EventsChangedEvent.ADDED,
                e -> handleEventAdded(dc, item));
        dc.addEventHandler(EventsChangedEvent.DELETED,
                e -> handleEventDeleted(dc, item));
        dc.setOnMouseClicked(e -> handleCellEvent(dc, e));
        dc.setOnKeyPressed(e -> handleCellEvent(dc, e));
    }

    private void createEventMenu(DateCell dc) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass()
                    .getResource("/views/RutinasView.fxml"));
            fxmlLoader.setControllerFactory(c -> new RutinasController(dc));
            GridPane root = fxmlLoader.load();
            Scene scene = new Scene(root, 900, 600);
            Stage stage = new Stage();
            stage.setTitle("Events of " + dc.getItem());
            stage.setScene(scene);
            stage.getIcons().add(new Image("/images/calendar-icon.png"));
            stage.show();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public void initStageActions(Stage stage) {
        stage.setOnCloseRequest(e -> handleSaveEventsToDB());
    }

    public String handleSaveEventsToDB() {
        // cancel all TimerTasks
        //notifyController.close();
        String result = null;

        // ask user to save events to db
        Alert alert = WindowUtils.createAlert("Guardar set?");
        if (alert.getResult() == ButtonType.YES) {
            WODDB woddb = new WODDB();
            //result = woddb.createInsertQuery(eventManager.getEjerciciosAgregados());
            eventManager.vaciaEventos();
        }
        return result;
    }

    public String borrarEventosDB(DateEvent de) {
        // cancel all TimerTasks
        //notifyController.close();
        String result = null;
        Alert alert = WindowUtils.createAlert("Borrar evento?");
        if (alert.getResult() == ButtonType.YES) {
            ReservacionesDB reservacionesDB = new ReservacionesDB();
            result = reservacionesDB.eliminaEvento(de);
            eventManager.vaciaEventos();
        }
        return result;
    }

    public List<DateCell> getDateCells() {
        return dps.getPopupContent()
                .lookupAll(".day-cell")
                .stream()
                .map(n -> (DateCell) n)
                .collect(Collectors.toList());
    }
}
