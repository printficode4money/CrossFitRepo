package controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import com.sun.javafx.scene.control.skin.DatePickerSkin;
import controllers.ReservacionesExt.*;
import eu.mihosoft.scaledfx.ScalableContentPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import persistence.ReservacionesDB;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ReservacionesController implements Initializable {

    @FXML
    private JFXDrawer drawer;
    @FXML private JFXHamburger hamburger;
    @FXML private VBox box;
    @FXML private GridPane root;

    private DatePickerSkin dps;
    private EventManager eventManager = new EventManager();
    private NotifyPopupController notifyController = new NotifyPopupController();

    public ReservacionesController() {
        ReservacionesDB reservacionesDB = new ReservacionesDB();
        eventManager = EventManager.getInstance();
        eventManager.initEvents(reservacionesDB.read());

//        try (DBDateEventDao db = DateEventDaoFactory.getDBDao()) {
//            eventManager.initEvents(db.read());
//        } catch (SQLException e) {
//            WindowUtils.createErrorAlert("Could not connect to databse!");
//            eventManager.initEvents(new HashSet<>());
//            e.printStackTrace();
//        }

        notifyController = new NotifyPopupController();
        notifyController.initialize();
    }

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

//        DatePicker dp = new DatePickerExt(LocalDate.now(), this);
//        dps = new DatePickerSkin(dp);
//        Node popupContent = dps.getPopupContent();
//        root.setCenter(popupContent);

        try {
            //BorderPane root = new BorderPane();
//            Scene scene = new Scene(root, 400, 400);
//            scene.getStylesheets().add(getClass().getResource("../styling/estilosCSS.css").toExternalForm());
//            Stage stage = new Stage();
            DatePicker dp = new DatePickerExt(LocalDate.now(), this);
            dps = new DatePickerSkin(dp);
            DatePickerSkin datePickerSkin = new DatePickerSkin(new DatePicker(LocalDate.now()));
//            Node popupContent = datePickerSkin.getPopupContent();
            Node popupContent2 = dps.getPopupContent();
//            root.setCenter(popupContent2);
            root.getChildren().add(popupContent2);

//            stage.setScene(scene);
//            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
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

        notifyController.handleEventsAdded(item);
    }

    private void handleEventDeleted(DateCell dc, LocalDate item) {
        if (eventManager.getEventsByDate(item).isEmpty()) {
            if (item.isEqual(LocalDate.now())) {
                dc.setId("date-cell-today");
            } else {
                dc.setId("date-cell-default");
            }
        }

        notifyController.handleEventsDeleted();
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
                    .getResource("/views/EventView.fxml"));
            fxmlLoader.setControllerFactory(c -> new EventController(dc));
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
        notifyController.close();
        String result = null;

        // ask user to save events to db
        Alert alert = WindowUtils.createAlert("Guardar evento(s)?");
        if (alert.getResult() == ButtonType.YES) {
            ReservacionesDB reservacionesDB = new ReservacionesDB();
            result = reservacionesDB.createInsertQuery(eventManager.getEventsAdded());
            eventManager.vaciaEventos();
        }
        return result;
    }

    public String borrarEventosDB(DateEvent de) {
        // cancel all TimerTasks
        notifyController.close();
        String result = null;

        // ask user to save events to db
        Alert alert = WindowUtils.createAlert("Borrar evento(s)?");
        if (alert.getResult() == ButtonType.YES) {
            ReservacionesDB reservacionesDB = new ReservacionesDB();
            result = reservacionesDB.eliminaEvento(de);
            eventManager.vaciaEventos();
        }
        return result;
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

    public List<DateCell> getDateCells() {
        return dps.getPopupContent()
                .lookupAll(".day-cell")
                .stream()
                .map(n -> (DateCell) n)
                .collect(Collectors.toList());
    }

}

