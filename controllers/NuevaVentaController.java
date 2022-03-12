package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.InventarioDTM;

import java.net.URL;
import java.util.ResourceBundle;

public class NuevaVentaController implements Initializable {

    private Stage stageThis;
    private ObservableList<InventarioDTM> data;
    private ObservableList<InventarioDTM> carrito = FXCollections.observableArrayList();
    @FXML private ComboBox cmbArtVenta;
    @FXML private TableView tblCarrito;
    @FXML private TableColumn colArt;
    @FXML private TableColumn colCantidad;
    @FXML private TableColumn colTotal;
    @FXML private TableColumn colPrecioUnitario;
    @FXML private TextField txtTotalVenta;
    @FXML private Button btnPagar;

    public void initialize(URL url, ResourceBundle rb) {
        colArt.setStyle( "-fx-alignment: CENTER;");
        colCantidad.setStyle( "-fx-alignment: CENTER;");
        colTotal.setStyle( "-fx-alignment: CENTER;");
        colPrecioUnitario.setStyle( "-fx-alignment: CENTER;");
        colArt.setCellFactory(TextFieldTableCell.forTableColumn());
        colArt.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colCantidad.setCellFactory(TextFieldTableCell.forTableColumn());
        colCantidad.setCellValueFactory(new PropertyValueFactory<>("cantidadVenta"));
        colTotal.setCellFactory(TextFieldTableCell.forTableColumn());
        //colTotal.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("totalPrecioUnitario"));
        colPrecioUnitario.setCellFactory(TextFieldTableCell.forTableColumn());
        colPrecioUnitario.setCellValueFactory(new PropertyValueFactory<>("precio"));
        txtTotalVenta.setEditable(false);

        colCantidad.setOnEditCommit(
                new EventHandler<TableColumn.CellEditEvent<InventarioDTM, String>>() {
                    @Override
                    public void handle(TableColumn.CellEditEvent<InventarioDTM, String> t) {
                        t.getTableView().getItems().get(t.getTablePosition().getRow()).setCantidadVenta(t.getNewValue());
                        String cantidad = t.getNewValue();
                        double totalPrecioUnitario = 0.0;
                        InventarioDTM inventarioObj = (InventarioDTM) tblCarrito.getSelectionModel().getSelectedItem();
                        if(Integer.parseInt(cantidad) > 0) {
                            totalPrecioUnitario = Double.parseDouble(inventarioObj.getPrecio()) * Double.parseDouble(cantidad);
                            for (InventarioDTM articulo : carrito) {
                                if (inventarioObj.getIdInventario()==(articulo.getIdInventario())) {
                                    articulo.setTotalPrecioUnitario(String.valueOf(totalPrecioUnitario));
                                }
                            }
                        }
//                        }else{
//                            totalPrecioUnitario =   Double.parseDouble(inventarioObj.getPrecio()) * 1;
//                        }

                        tblCarrito.setItems(carrito);
                        tblCarrito.refresh();
                        calculaTotal();
                    }
                }
        );

        tblCarrito.setEditable(true);
        tblCarrito.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tblCarrito.getSelectionModel().setCellSelectionEnabled(false);


    }

    public void receiveData(ObservableList<InventarioDTM> listaCarrito , Stage stage) {
        carrito = listaCarrito;
        stageThis = stage;
        for (InventarioDTM articulo : carrito){
            articulo.setTotalPrecioUnitario(articulo.getPrecio());
        }
        tblCarrito.setItems(carrito);
        calculaTotal();
    }

    public void calculaTotal(){
        double sumatoria=0.0;
        for (InventarioDTM articulo : carrito){
            sumatoria = sumatoria + Double.parseDouble(articulo.getTotalPrecioUnitario());
        }
        txtTotalVenta.setText("$"+String.valueOf(sumatoria));
    }

    public void pagar(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ConfirmacionVenta.fxml"));
            Parent root = loader.load();
            ConfimacionVentaController scene2Controller = loader.getController();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Nueva Venta");
            String  totalVenta = txtTotalVenta.getText();
            totalVenta = totalVenta.substring(1); //remueve simbolo $
            scene2Controller.receiveData(stage, Double.parseDouble(totalVenta), carrito);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.showAndWait();
            stageThis.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public void cancelar(){
        stageThis.close();
    }

}


