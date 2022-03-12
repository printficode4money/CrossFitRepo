package controllers;

import com.jfoenix.controls.JFXComboBox;
import controllers.ReservacionesExt.EventManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DateCell;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Catalogo_Ejercicio_DTM;
import models.RutinaDTM;
import models.UsuariosDTM;
import org.controlsfx.control.textfield.TextFields;
import persistence.MiembrosDB;
import persistence.WODDB;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class RutinasController {

    private final EventManager eventManager;
    private final DateCell dc;

    private final SimpleStringProperty eventDateStr;

    @FXML private Label dateLabel;
    @FXML private TableView<RutinaDTM> eventTable;
    @FXML private TableColumn<RutinaDTM, String> set_nombreCol;
    private ObservableList<RutinaDTM> data;
    private LocalDate eventDate;
    @FXML private JFXComboBox cmbEntrenador;
    private ObservableList<Catalogo_Ejercicio_DTM> seleccionados = FXCollections.observableArrayList();

    /**
     * Ctor. This class has no zero argument ctor so it has to be set by
     * setControllerFactory with fxml loader when creating this window.
     *
     * @param dc DateCell which called this ctor.
     */
    public RutinasController(DateCell dc) {
        WODDB woddb = new WODDB();
        eventDateStr = new SimpleStringProperty();
        eventManager = EventManager.getInstance();
        //eventManager.initEvents(woddb.consultaWOD());
        setEventDate(dc.getItem());
        this.dc = dc;
    }

    /**
     * Initializes spinners and tableView factories and properties.
     */
    @FXML
    public void initialize() {
        WODDB woddb = new WODDB();
        eventTable.setPlaceholder(new Label("No hay entrenamientos para este día"));
        MiembrosDB miembrosDB = new MiembrosDB();
        cmbEntrenador.setEditable(true);
        TextFields.bindAutoCompletion(cmbEntrenador.getEditor(), cmbEntrenador.getItems());
        ObservableList<UsuariosDTM> listaMiembros = miembrosDB.consultaUsuariosSistema();
        for (UsuariosDTM miembro : listaMiembros) {
            cmbEntrenador.getItems().add(miembro.getNombre_completo());
        }
        new AutoCompleteComboBoxListener<>(cmbEntrenador);

        dateLabel.textProperty().bind(eventDateStr);

        data = woddb.consultaWODporFecha(eventDate);

//        set_nombreCol.setCellValueFactory(o -> new SimpleStringProperty(o.getValue().getDateTime().format(DateTimeFormatter.ofPattern("HH:mm"))));
//        descColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        set_nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre_set"));

        eventTable.setItems(data);

        eventTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        try {
                            RutinaDTM setDelDia = eventTable.getSelectionModel().getSelectedItem();
                            if (setDelDia != null) {
                                ObservableList<Catalogo_Ejercicio_DTM> lstEjerciciosDia = FXCollections.observableArrayList();
                                RutinaDTM rutinaObj = new RutinaDTM();
                                rutinaObj = woddb.consultaSetPorFecha(eventDate, setDelDia.getNombre_set());
                                lstEjerciciosDia = woddb.consultaDetallesSetPorFecha(setDelDia, eventDate);
                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PlaneadorWODView.fxml"));
                                Parent root = loader.load();
                                OrganizadorWODController scene2Controller = loader.getController();
                                Stage stage = new Stage();
                                stage.setScene(new Scene(root));
                                stage.setTitle("Planeación de Ejercicios");
                                scene2Controller.receiveData(stage, lstEjerciciosDia, rutinaObj);
                                stage.initModality(Modality.APPLICATION_MODAL);
                                stage.showAndWait();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }


    public void agregarSet() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/PlaneadorWODView.fxml"));
            Parent root = loader.load();
            OrganizadorWODController scene2Controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Planeación de Ejercicios");
            scene2Controller.receiveData(stage, eventDate);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            recargaSetsporDia();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void recargaSetsporDia() {
        WODDB woddb = new WODDB();
        data =woddb.consultaWODporFecha(eventDate);
        eventTable.setItems(data);
}

    /**
     * Sets event date and its string representation property used by view.
     */
    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd LLLL yyyy");
        String formattedString = eventDate.format(formatter);
        eventDateStr.setValue(formattedString);
    }
}
