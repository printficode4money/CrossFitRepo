
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
public class HubController implements Initializable{
    
    @FXML
    private Button btnUsuariosInterfaz;  
    
    @FXML
    private Button btnRegistroVisitas;
    
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
//                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/interfaces/Usuarios2.fxml")));
//                    stage.setScene(scene);
//                    stage.show();

                    Stage este = (Stage)((Node) event.getSource()).getScene().getWindow();
                    este.close();

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/interfaces/Usuarios2.fxml"));
                    Parent root = fxmlLoader.load();
                    ScalableContentPane scp = new ScalableContentPane (root);
                    Stage stage = new Stage();
                    stage.setMaximized(true);
                    stage.setScene(new Scene(scp));
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
        }else if(event.getSource() == btnRegistroVisitas){
         try {
//                    Node node = (Node) event.getSource();
//                    Stage stage = (Stage) node.getScene().getWindow();
//                    stage.close();
//                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/interfaces/RegistroVisitas_resize.fxml")));
//                    stage.setScene(scene);
//                    //stage.setMaximized(true);
//                    stage.show();

             Stage este = (Stage)((Node) event.getSource()).getScene().getWindow();
             este.close();

             FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/interfaces/RegistroVisitas_resize.fxml"));
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
    }
   
}
