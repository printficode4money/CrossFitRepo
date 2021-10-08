package controllers;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import utils.ConnectionUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class MiembrosEditarController implements Initializable {

    @FXML
    private Button btnRedirigeEditarUsuarios;

    @FXML
    private Button btnMenuPrincipal;

    @FXML
    TableView tblData;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fetColumnList();
        fetRowList();

        tblData.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        System.out.println("Double clicked");
                    }
                }
            }
        });
    }
    Connection connection;


    private ObservableList<ObservableList> data;
    String SQL = "SELECT IDMIEMBRO, NOMBRES, APELLIDO_PAT, APELLIDO_MAT  FROM MIEMBROS";

    //only fetch columns
    private void fetColumnList() {
        ConnectionUtil newCon = new ConnectionUtil();
        try {
            ResultSet rs = newCon.conDB().createStatement().executeQuery(SQL);

            //SQL FOR SELECTING ALL OF CUSTOMER
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tblData.getColumns().removeAll(col);
                tblData.getColumns().addAll(col);
            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList() {
        ConnectionUtil newCon = new ConnectionUtil();
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = newCon.conDB().createStatement().executeQuery(SQL);

            while (rs.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);
                //data.add(new MiembrosDataTableModel(3, "sd", "sd", "df"));

            }
            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @FXML
    public void regresaMenuPrincipal(MouseEvent event) {
        if (event.getSource() == btnMenuPrincipal) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/interfaces/Hub.fxml")));
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

//    @FXML
//    public void clickItem(MouseEvent event)
//    {
//        TableRow<MiembrosDataTableModel> row = new TableRow<>();
//        if (event.getClickCount() == 1){
//            System.out.println(tblData.getSelectionModel().getSelectedItem());
//            MiembrosDataTableModel rowData = row.getItem();
//        }
//    }
}
