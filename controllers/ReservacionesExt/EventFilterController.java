package controllers.ReservacionesExt;

import controllers.ReservacionesController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;

/**
 * Created on 5/27/2017.
 * Controller for EventFliterView.fxml.
 */
public class EventFilterController {

    private final EventManager eventManager;
    private final ReservacionesController cc;

    @FXML
    private DatePicker datePickerStart;
    @FXML
    private DatePicker datePickerEnd;
    @FXML
    private TableView<DateEvent> eventTable;
    @FXML
    private TableColumn<DateEvent, String> dateColumn;
    @FXML
    private TableColumn<DateEvent, String> descColumn;
    @FXML
    private TableColumn<DateEvent, String> miembroColumn;
//    @FXML
//    private TableColumn<DateEvent, Integer> idColumn;

    /**
     * date start picked by user
     */
    private LocalDate start;
    /**
     * date end picked by user
     */
    private LocalDate end;
    /**
     * observable data for table view
     */
    private ObservableList<DateEvent> data;

    /**
     * Ctor. This class has no zero argument ctor so it has to be set by
     * setControllerFactory with fxml loader when creating this window.
     *
     * @param cc this class uses and notifies main controller
     */
    public EventFilterController(ReservacionesController cc) {
        eventManager = EventManager.getInstance();
        this.cc = cc;
    }

    /**
     * Initializes datePickers and tableView factories and properties.
     */
    @FXML
    public void initialize() {
        datePickerStart.setValue(LocalDate.now());
        datePickerEnd.setValue(LocalDate.now());

        datePickerStart.setOnAction(e -> updateEventTable());
        datePickerEnd.setOnAction(e -> updateEventTable());

        start = datePickerStart.getValue();
        end = datePickerEnd.getValue();

        data = FXCollections.observableArrayList(
                eventManager.getEventsBetween(start, end));
        dateColumn.setCellValueFactory(o -> new SimpleStringProperty(
                o.getValue().getDateTime().format(DateTimeFormatter
                        .ofPattern("yyyy-MM-dd -> HH:mm"))));
        descColumn.setCellValueFactory(
                new PropertyValueFactory<>("description"));
        miembroColumn.setCellValueFactory(
                new PropertyValueFactory<>("nombreCompleto"));
//        idColumn.setCellValueFactory(
//                new PropertyValueFactory<>("idEvento"));

        eventTable.setOnKeyPressed(this::handleDelete);
        eventTable.setItems(data);
    }

    /**
     * Handles deleting events with TableView by DEL key. It notifies all
     * date cells about this forcing them to update.
     *
     * @param e pressed key
     */
    private void handleDelete(KeyEvent e) {
        if (e.getCode().equals(KeyCode.DELETE)
                && eventTable.getSelectionModel() != null) {
            DateEvent de = eventTable.getSelectionModel().getSelectedItem();
            data.remove(de);
            eventManager.deleteEvents(de);
            for (DateCell dc : cc.getDateCells()) {
                Event.fireEvent(dc,
                        new EventsChangedEvent(EventsChangedEvent.DELETED));
            }

        }
    }

    /**
     * Handler for delete button. It deletes all events that are in filter.
     * It notifies all date cells about this forcing them to update.
     */
    @FXML
    public void deleteEvents() {
        eventManager.deleteEvents(new HashSet<>(data));
        data.clear();
        List<DateCell> cells = cc.getDateCells();
        EventsChangedEvent event =
                new EventsChangedEvent(EventsChangedEvent.DELETED);
        cells.forEach(o -> o.fireEvent(event));
    }


    /**
     * Handler for DatePickers actions. Updates tableView and date
     * properties.
     */
    private void updateEventTable() {
        start = datePickerStart.getValue();
        end = datePickerEnd.getValue();
        data = FXCollections.observableArrayList(
                eventManager.getEventsBetween(start, end));
        eventTable.setItems(data);
    }
}
