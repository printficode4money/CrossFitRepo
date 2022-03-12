
package controllers;

import eu.mihosoft.scaledfx.ScalableContentPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

/**
 *
 * @author DonV3rga
 */
public class HubController implements Initializable {

    @FXML
    private Button btnUsuariosInterfaz;
    @FXML
    private Button btnRegistroVisitas;
    @FXML
    private Button btnInventario;
    @FXML
    private Button btnEstadisticas;
    @FXML
    private Button btnReservaciones;
    @FXML
    private Button btnPlanificador;
    @FXML
    private Button btnVentas;
    @FXML
    private Button btnCorteCaja;

    public HubController() {
        connection = (Connection) ConnectionUtil.conDB();
    }

    Connection connection;
    private Button test;
    // private static final String IDLE_BUTTON_STYLE = "-fx-background-color: transparent;";
    private static final String HOVERED_BUTTON_STYLE = "-fx-border-color: #4287f5";


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //btnUsuariosInterfaz.setStyle(IDLE_BUTTON_STYLE);
        btnUsuariosInterfaz.setOnMouseEntered(e -> btnUsuariosInterfaz.setStyle(HOVERED_BUTTON_STYLE));
        //btnUsuariosInterfaz.setOnMouseExited(e -> btnUsuariosInterfaz.setStyle(IDLE_BUTTON_STYLE));
    }

    @FXML
    public void direccionaInterfaces(MouseEvent event) {
        if (event.getSource() == btnUsuariosInterfaz) {
            try {
//                    Node node = (Node) event.getSource();
//                    Stage stage = (Stage) node.getScene().getWindow();
//                    //stage.setMaximized(true);
//                    stage.close();
//                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/Usuarios2.fxml")));
//                    stage.setScene(scene);
//                    stage.show();

                Stage este = (Stage) ((Node) event.getSource()).getScene().getWindow();
                este.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Miembros.fxml"));
                Parent root = fxmlLoader.load();
                ScalableContentPane scp = new ScalableContentPane(root);
                Stage stage = new Stage();
                stage.setMaximized(true);
                stage.setScene(new Scene(scp));
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        } else if (event.getSource() == btnRegistroVisitas) {
            try {
//                    Node node = (Node) event.getSource();
//                    Stage stage = (Stage) node.getScene().getWindow();
//                    stage.close();
//                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/RegistroVisitas_resize.fxml")));
//                    stage.setScene(scene);
//                    //stage.setMaximized(true);
//                    stage.show();

                Stage este = (Stage) ((Node) event.getSource()).getScene().getWindow();
                este.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/RegistroVisitas.fxml"));
                Parent root = fxmlLoader.load();
                ScalableContentPane scp = new ScalableContentPane(root);
                Stage stage = new Stage();
                stage.setMaximized(true);
                stage.setScene(new Scene(scp));
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        } else if (event.getSource() == btnInventario) {
            try {
                Stage este = (Stage) ((Node) event.getSource()).getScene().getWindow();
                este.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Inventario.fxml"));
                Parent root = fxmlLoader.load();
                ScalableContentPane scp = new ScalableContentPane(root);
                Stage stage = new Stage();
                stage.setMaximized(true);
                stage.setScene(new Scene(scp));
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        } else if (event.getSource() == btnEstadisticas) {
            try {
                Stage este = (Stage) ((Node) event.getSource()).getScene().getWindow();
                este.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/test.fxml"));
                Parent root = fxmlLoader.load();
                ScalableContentPane scp = new ScalableContentPane(root);
                Stage stage = new Stage();
                stage.setMaximized(true);
                stage.setScene(new Scene(scp));
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        } else if (event.getSource() == btnReservaciones) {
            try {
                Stage este = (Stage) ((Node) event.getSource()).getScene().getWindow();
                este.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/TestCal.fxml"));
                Parent root = fxmlLoader.load();
                ReservacionesController cc = fxmlLoader.getController();

                ScalableContentPane scp = new ScalableContentPane(root);
                Stage stage = new Stage();
                stage.setMaximized(true);
                stage.setScene(new Scene(scp));
                //cc.initStageActions(stage);
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        } else if (event.getSource() == btnPlanificador) {
            try {
                Stage este = (Stage) ((Node) event.getSource()).getScene().getWindow();
                este.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Planificador.fxml"));
                Parent root = fxmlLoader.load();
                PlanificadorController cc = fxmlLoader.getController();
                ScalableContentPane scp = new ScalableContentPane(root);
                Stage stage = new Stage();
                stage.setMaximized(true);
                stage.setScene(new Scene(scp));
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }

        } else if (event.getSource() == btnVentas) {
            try {
                Stage este = (Stage) ((Node) event.getSource()).getScene().getWindow();
                este.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Ventas.fxml"));
                Parent root = fxmlLoader.load();
                ScalableContentPane scp = new ScalableContentPane(root);
                Stage stage = new Stage();
                stage.setMaximized(true);
                stage.setScene(new Scene(scp));
                stage.show();

            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        } else if (event.getSource() == btnCorteCaja) {
            try {
                Stage este = (Stage) ((Node) event.getSource()).getScene().getWindow();
                este.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/CorteCajaView.fxml"));
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

    }
}
