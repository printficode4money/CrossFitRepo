package controllers;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import eu.mihosoft.scaledfx.ScalableContentPane;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
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
    private Button btnRedirigeAgregarUsuarios;

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
    TableColumn FECHA_REGISTRO;

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
    private ImageView imgHuella;

    @FXML
    private TextField txtMensajes;

    @FXML
    private Button btnVerificar;

    @FXML
    private Button btnGuardarMiembro;

    @FXML
    private Button btnSave;

    @FXML
    private ComboBox<String> txtGender;

    @FXML
    TableView tblData;

    @FXML
    private ComboBox comboMembresias;

    @FXML
    private TextField txtMontoMem;

    @FXML
    private TitledPane detallesPane;

    @FXML
    private CheckBox chkDescuentoEspecial;

    @FXML
    private TextField txtPorcenDescuento;

    @FXML
    private Button btnTotalMembresia;

    @FXML
    private Button btnCobrar;
    @FXML private JFXDrawer drawer;
    @FXML private JFXHamburger hamburger;
    @FXML private VBox box;


    private ObservableList<MiembrosDataTableModel> data;
    private MiembrosModel miembrosModel = new MiembrosModel();
    private String sexo;
    final ToggleGroup group = new ToggleGroup();
    private double costoMembresia;


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
        MiembrosDB miembrosDBObj =new MiembrosDB();
        IDMIEMBRO.setCellValueFactory(new PropertyValueFactory<>("idmiembro"));
        NOMBRES.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        APELLIDO_PAT.setCellValueFactory(new PropertyValueFactory<>("apellido_pat"));
        APELLIDO_MAT.setCellValueFactory(new PropertyValueFactory<>("apellido_mat"));
        FECHA_REGISTRO.setCellValueFactory(new PropertyValueFactory<>("fecha_registro"));
        tableMiembros.setEditable(true);
        tableMiembros.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        tableMiembros.getSelectionModel().setCellSelectionEnabled(false);
        data = miembrosDBObj.consultaMiembrosExistentes();
        tableMiembros.setItems(data);

        tableMiembros.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        try {
                            MiembrosDataTableModel miembro = (MiembrosDataTableModel) tableMiembros.getSelectionModel().getSelectedItem();
//                            Node node = (Node) mouseEvent.getSource();
//                            Stage stage = (Stage) node.getScene().getWindow();
//                            stage.setMaximized(false);
//                            stage.close();
//                            Scene scene = null;
//                            scene = new Scene(FXMLLoader.load(getClass().getResource("/views/EditarUsuario.fxml")));
//                            stage.setUserData(miembro);
//                            stage.setScene(scene);
//                            stage.show();


                            Stage currentStage = (Stage) tableMiembros.getScene().getWindow();
                            currentStage.close();
                            //Load second scene
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/EditarUsuario.fxml"));
                            Parent root = loader.load();

                            //Get controller of scene2
                            EditarUsuarioIndividualController scene2Controller = loader.getController();
                            //Pass whatever data you want. You can have multiple method calls here
                            scene2Controller.receiveData(miembro);

                            //Show scene 2 in new window
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root));
                            stage.setTitle("Editar Datos Miembro");
                            stage.show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    @FXML
    public void regresaMenuPrincipal(MouseEvent event) {
        if (event.getSource() == btnMenuPrincipal) {
            try {
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                //stage.setMaximized(true);
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/Hub.fxml")));
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

    @FXML
    public void abreAgregarUsuarios(MouseEvent event){
        if (event.getSource() == btnRedirigeAgregarUsuarios) {
//            try {
//                //Lector.stopCapture();
//                Node node = (Node) event.getSource();
//                Stage stage = (Stage) node.getScene().getWindow();
//                stage.close();
//                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/views/Usuarios2.fxml")));
//                stage.setScene(scene);
//                stage.show();
            try {
                Stage este = (Stage)((Node) event.getSource()).getScene().getWindow();
                este.close();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/views/Usuarios2.fxml"));
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
}
