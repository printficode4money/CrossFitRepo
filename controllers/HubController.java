
package controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
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
    
    PreparedStatement preparedStatement;
    Connection connection;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("fierro");
    }
    
    @FXML
    public void direccionaInterfaces(MouseEvent event) {

        if (event.getSource() == btnUsuariosInterfaz) {
                try {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    //stage.setMaximized(true);
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/interfaces/Usuarios2.fxml")));
                    stage.setScene(scene);
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    System.out.println("ERROR!!!!:  " +ex.getMessage());
                }

            
        }else if(event.getSource() == btnRegistroVisitas){
         try {
                    Node node = (Node) event.getSource();
                    Stage stage = (Stage) node.getScene().getWindow();
                    stage.close();
                    Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/interfaces/RegistroVisitas.fxml")));
                    stage.setScene(scene);
                    stage.show();


                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                    System.out.println("ERROR!!!!:  " +ex.getMessage());
                }
        }
    }
   
}
