package controllers;

import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.MiembrosDataTableModel;
import models.MiembrosModel;
import persistence.MiembrosDB;
import persistence.RegistroVisitasDB;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;

public class MiembrosEditarController implements Initializable {

    @FXML
    private Button btnRedirigeEditarUsuarios;

    @FXML
    private Button btnMenuPrincipal;

    @FXML
    TableView tableMiembros;

    @FXML
    TableColumn IDMIEMBRO;

    @FXML
    TableColumn NOMBRES;

    @FXML
    TableColumn APELLIDO_PAT;

    @FXML
    TableColumn APELLIDO_MAT;

    @FXML
    private RadioButton hombreRadBtn;

    @FXML
    private RadioButton mujerRadBtn;

    @FXML
    private TextField txtNombres;

    @FXML
    private TextField txtApellidoPat;

    @FXML
    private TextField txtApellidoMat;

    @FXML
    private TextField txtEmail;

    @FXML
    private DatePicker txtFecNacimiento;

    @FXML
    private TextField txtTelefono;

    @FXML
    private ComboBox cmbTipoSangre;

    @FXML
    private TextField txtNombreContactoEmer;

    @FXML
    private TextField txtTelContactoEmer;

    @FXML
    private TextArea txtAreaObserv;

    @FXML
    private TextField txtMensajes;

    private ObservableList<MiembrosDataTableModel> data;
    private MiembrosModel miembrosModel = new MiembrosModel();
    private String sexo;
    final ToggleGroup group = new ToggleGroup();
    private double costoMembresia;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        MiembrosDB miembrosDBObj =new MiembrosDB();
        IDMIEMBRO.setCellValueFactory(new PropertyValueFactory<>("idmiembro"));
        NOMBRES.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        APELLIDO_PAT.setCellValueFactory(new PropertyValueFactory<>("apellido_pat"));
        APELLIDO_MAT.setCellValueFactory(new PropertyValueFactory<>("apellido_mat"));
        tableMiembros.setEditable(true);
        tableMiembros.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableMiembros.getSelectionModel().setCellSelectionEnabled(false);
        data = miembrosDBObj.consultaUsuariosExistentes();
        tableMiembros.setItems(data);

        tableMiembros.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        try {
                            MiembrosDataTableModel miembro = (MiembrosDataTableModel) tableMiembros.getSelectionModel().getSelectedItem();
                            Node node = (Node) mouseEvent.getSource();
                            Stage stage = (Stage) node.getScene().getWindow();
                            //stage.setMaximized(true);
                            stage.close();
                            Scene scene = null;
                            scene = new Scene(FXMLLoader.load(getClass().getResource("/interfaces/EditarUsuario.fxml")));
                            stage.setScene(scene);
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }



    //only fetch columns
    /*private void fetColumnList() {
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

                tableMiembros.getColumns().removeAll(col);
                tableMiembros.getColumns().addAll(col);
            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }*/

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

    public void identificarHuella() throws SQLException {
        RegistroVisitasDB registroVisitasDB = new RegistroVisitasDB();
        List<MiembrosModel> listaHuellas = registroVisitasDB.identificaHuella();
        boolean existeHuella = false;
        for (int i = 0; i < listaHuellas.size(); i++) {

//            DPFPTemplate referenceTemplate = DPFPGlobal.getTemplateFactory().createTemplate(listaHuellas.get(i).getHuella());
//              setTemplate(referenceTemplate);
//              DPFPVerificationResult result = Verificador.verify(featuresverificacion, getTemplate());
//            if (result.isVerified()) {
//                //crea la imagen de los datos guardado de las huellas guardadas en la base de datos
//                JOptionPane.showMessageDialog(null, "¡Bienvenido " + listaHuellas.get(i).getNombres() + "," + " HDTRPM!", "Verificacion de Huella", JOptionPane.INFORMATION_MESSAGE);
//                try {
//                    registroVisitasDB.registrarVisita(listaHuellas.get(i).getIdMiembro());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//                existeHuella = true;
//                break;
//            }
        }
    }

    @FXML
    private String radioGroupSexo(){
        String resultado= null;
        hombreRadBtn.setToggleGroup(group);
        mujerRadBtn.setToggleGroup(group);
        if(hombreRadBtn.isSelected()){
            resultado = "H";
            mujerRadBtn.setSelected(false);
        }else if(mujerRadBtn.isSelected()){
            resultado = "M";
        }
        return resultado;
    }

    @FXML
    private void HandleEvents(MouseEvent event) {
        sexo = radioGroupSexo();
        if (txtEmail.getText().isEmpty() || txtNombres.getText().isEmpty() || txtApellidoPat.getText().isEmpty() || txtApellidoMat.getText().isEmpty() || txtFecNacimiento.getValue().equals(null)
                || sexo == null || txtTelefono.getText().isEmpty()) {
            txtMensajes.setText("Llena todos los campos");
        } else {
            //MiembrosModel miembrosModel = new MiembrosModel();
            miembrosModel.setNombres(txtNombres.getText());
            miembrosModel.setApellidoPat(txtApellidoPat.getText());
            miembrosModel.setApellidoMat(txtApellidoMat.getText());
            miembrosModel.setTelefono(txtTelefono.getText());
            miembrosModel.setEmail(txtEmail.getText());
            miembrosModel.setFecha_Nacimiento(txtFecNacimiento.getValue().toString());
            miembrosModel.setSexo(sexo);
            miembrosModel.setTipoSangre(cmbTipoSangre.getSelectionModel().getSelectedItem().toString());
            miembrosModel.setNombreContactoEmer(txtNombreContactoEmer.getText());
            miembrosModel.setTelefonoContactoEmer(txtTelContactoEmer.getText());
            miembrosModel.setObservaciones(txtAreaObserv.getText());
            actualizaMiembro(miembrosModel);
        }
    }

    private void actualizaMiembro(MiembrosModel miembrosModelGuarda) {
        //Obtiene los datos del template de la huella actual
        String resultado = null;
        /*ByteArrayInputStream datosHuella = new ByteArrayInputStream(template.serialize());
        Integer tamañoHuella=template.serialize().length;*/
        MiembrosDB miembrosDB = new MiembrosDB();

        try {
         /*   resultado = miembrosDB.guardarMiembro(miembrosModelGuarda, datosHuella, tamañoHuella);
            miembrosModel = miembrosDB.consultarMiembro();
            txtMensajes.setText(resultado);
            //detallesPane.setCollapsible(true);
            //  guardarHuella();
            // fetRowList();
            //clear fields
            clearFields();
            //return "Success";*/

        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            //Reclutador.clear();
        }
    }

    private void clearFields() {
        txtNombres.clear();
        txtApellidoPat.clear();
        txtApellidoMat.clear();
        txtEmail.clear();
        txtTelefono.clear();
        txtFecNacimiento.getEditor().clear();
        hombreRadBtn.setSelected(false);
        mujerRadBtn.setSelected(false);
        txtTelContactoEmer.clear();
        txtAreaObserv.clear();
        txtNombreContactoEmer.clear();
    }
}
