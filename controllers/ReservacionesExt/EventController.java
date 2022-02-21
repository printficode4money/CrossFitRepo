package controllers.ReservacionesExt;

import com.jfoenix.controls.JFXComboBox;
import controllers.AutoCompleteComboBoxListener;
import controllers.ReservacionesController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.MiembrosDataTableModel;
import org.controlsfx.control.textfield.TextFields;
import persistence.MiembrosDB;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Created on 5/21/2017.
 * Controller for EventView.fxml. This window is created after click on datcell.
 */
public class EventController{

    private final EventManager eventManager;
    private final DateCell dc;

    /**
     * String property binded to dateLabel. DateLabel cannot be initialized
     * in ctor so it has to use this property to be initialized properly with
     * dateCell date.
     */
    private final SimpleStringProperty eventDateStr;

    @FXML
    private Label dateLabel;
    @FXML
    private TextArea descField;
    @FXML
    private TextField placeField;
    @FXML
    private TableView<DateEvent> eventTable;
    @FXML
    private TableColumn<DateEvent, String> dateColumn;
    @FXML
    private TableColumn<DateEvent, String> descColumn;
    @FXML
    private TableColumn<DateEvent, String> miembroColumn;
    @FXML
    private TableColumn<DateEvent, Integer> idColumn;
    @FXML
    private Spinner<Integer> hourSpinner;
    @FXML
    private Spinner<Integer> minSpinner;
    @FXML
    private Spinner<String> notifySpinner;
    private ObservableList<DateEvent> data;
    private LocalDate eventDate;
    @FXML private Button btnBorrarEvento;
    @FXML private  JFXComboBox cmbMiembro;

    /**
     * Ctor. This class has no zero argument ctor so it has to be set by
     * setControllerFactory with fxml loader when creating this window.
     *
     * @param dc DateCell which called this ctor.
     */
    public EventController(DateCell dc) {
        eventDateStr = new SimpleStringProperty();
        eventManager = EventManager.getInstance();
        setEventDate(dc.getItem());
        this.dc = dc;
    }

    /**
     * Initializes spinners and tableView factories and properties.
     */
    @FXML
    public void initialize() {
        eventTable.setPlaceholder(new Label("No hay eventos para este día"));
        MiembrosDB miembrosDB = new MiembrosDB();
        cmbMiembro.setEditable(true);
        TextFields.bindAutoCompletion(cmbMiembro.getEditor(), cmbMiembro.getItems());
        ObservableList<MiembrosDataTableModel> listaMiembros = miembrosDB.consultaMiembrosExistentes();
        for (MiembrosDataTableModel miembro : listaMiembros){
            cmbMiembro.getItems().add(miembro.getNombreCompleto());
        }
        new AutoCompleteComboBoxListener<>(cmbMiembro);
        int currHour = LocalDateTime.now().getHour();
        int currMin = LocalDateTime.now().getMinute();

        ObservableList<String> notify =
                FXCollections.observableArrayList("Never", "10 sec", "1 hour", "1 day");
        notifySpinner.setValueFactory(
                new SpinnerValueFactory.ListSpinnerValueFactory<>(notify));
        hourSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23, currHour));
        minSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, currMin, 1));


        descField.setWrapText(true);
        dateLabel.textProperty().bind(eventDateStr);

        data = FXCollections.observableArrayList(
                eventManager.getEventsByDate(eventDate));

        dateColumn.setCellValueFactory(o -> new SimpleStringProperty(
                o.getValue().getDateTime().format(DateTimeFormatter
                        .ofPattern("HH:mm"))));
        descColumn.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        miembroColumn.setCellValueFactory(
                new PropertyValueFactory<>("nombreCompleto"));
//        idColumn.setCellValueFactory(
//                new PropertyValueFactory<>("idEvento"));

        eventTable.setItems(data);
       //eventTable.setOnKeyPressed(this::borrarEvento);
    }

    /**
     * Handles deleting events with TableView by DEL key.
     * It notifies dateCell about it to update it.
     */
    @FXML
    private void borrarEvento(ActionEvent event) {
            if (eventTable.getSelectionModel() != null) {
                String resultadoDB = null;
                ReservacionesController reservacionesController = new ReservacionesController();
                DateEvent de = eventTable.getSelectionModel().getSelectedItem();
                resultadoDB = reservacionesController.borrarEventosDB(de);
                if(!resultadoDB.contains("error")){
                    data.remove(de);
                    eventManager.deleteEvents(de);
                    Event.fireEvent(dc, new EventsChangedEvent(EventsChangedEvent.DELETED));
//                    Node source = (Node) event.getSource();
//                    Stage stage = (Stage) source.getScene().getWindow();
//                    stage.close();
                }
            }
    }

    /**
     * Handles "Add event" button.
     */
    @FXML
    public void addEvent(ActionEvent event) {
        String resultadoDB = null;
        ReservacionesController reservacionesController = new ReservacionesController();
        String desc = descField.getText();
        String nombreCompleto = (String) cmbMiembro.getValue();
        int hour = hourSpinner.getValue();
        int min = minSpinner.getValue();
        int secMinus = parseNotifySpinner(notifySpinner.getValue());
        DateEvent newDateEvent = new DateEvent(eventDate, hour, min, secMinus, nombreCompleto, desc);
        eventManager.addEvents(newDateEvent);
        data.add(newDateEvent);
        //placeField.clear();
        descField.clear();

        Event.fireEvent(dc, new EventsChangedEvent(EventsChangedEvent.ADDED));
        resultadoDB = reservacionesController.handleSaveEventsToDB();
        if(!resultadoDB.contains("error")){
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
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

    /**
     * Parses spinner value to seconds
     *
     * @param value string value of spinner
     * @return int number of seconds
     */
    private int parseNotifySpinner(String value) {
        switch (value) {
            case "10 sec":
                return 10;
            case "1 hour":
                return 3600;
            case "1 day":
                return 3600 * 24;
            default:
                return 0;
        }
    }
}
