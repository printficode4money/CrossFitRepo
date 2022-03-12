package controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import eu.mihosoft.scaledfx.ScalableContentPane;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import models.VentasDTM;
import persistence.VentasDB;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.ResourceBundle;

public class CorteCajaController implements Initializable {

    @FXML private JFXDrawer drawer;
    @FXML private JFXHamburger hamburger;
    @FXML private VBox box;
    @FXML private Label dateLabel;
    @FXML private TableView tablaCorte;
    @FXML private TableColumn colConcepto;
    @FXML private TableColumn colFormaPago;
    @FXML private TableColumn colMonto;
    @FXML private TableColumn colCambio;
    private LocalDate eventDate;
    private SimpleStringProperty eventDateStr;
    private ObservableList<VentasDTM> data;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        VentasDB ventasDB = new VentasDB();
        String fechaInicio;
        String fechaFin;
        colConcepto.setCellValueFactory(new PropertyValueFactory<>("concepto"));
        colFormaPago.setCellValueFactory(new PropertyValueFactory<>("forma_pago"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colCambio.setCellValueFactory(new PropertyValueFactory<>("cambio"));
        tablaCorte.setEditable(true);
        tablaCorte.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tablaCorte.getSelectionModel().setCellSelectionEnabled(false);
        eventDateStr = new SimpleStringProperty();
        eventDate = LocalDate.now();
        setEventDate(eventDate);
        dateLabel.textProperty().bind(eventDateStr);
        fechaInicio = LocalDate.now() + " 00:00:00";
        fechaFin = LocalDate.now() + " 23:59:59";
        SimpleDateFormat formatter6=new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
        Date inicio = null;
        Date fin = null;
//        try {
//            inicio = formatter6.parse(fechaInicio);
//            fin = formatter6.parse(fechaFin);
//            System.out.println(fechaInicio +"  "+ fechaFin);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        data = ventasDB.consultaCorteCaja(fechaInicio, fechaFin);

        tablaCorte.setItems(data);



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

    public void setEventDate(LocalDate eventDate) {
        this.eventDate = eventDate;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy");
        String formattedString = eventDate.format(formatter);
        eventDateStr.setValue(formattedString);
    }
}
