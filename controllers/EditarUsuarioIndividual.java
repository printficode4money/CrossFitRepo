package controllers;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import models.MiembrosDataTableModel;
import models.MiembrosModel;
import persistence.MiembrosDB;
import persistence.RegistroVisitasDB;

import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class EditarUsuarioIndividual implements Initializable {
    @FXML
    private Button btnRedirigeEditarUsuarios;

    @FXML
    private AnchorPane anchorPaneRaiz;

    @FXML
    private Button btnMenuPrincipal;

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

    private ObservableList<MiembrosDataTableModel> data;
    private MiembrosModel miembrosModel = new MiembrosModel();
    private String sexo;
    final ToggleGroup group = new ToggleGroup();
    private double costoMembresia;
    private MiembrosDataTableModel miembrosDataModel = new MiembrosDataTableModel();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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


    public void receiveData(MiembrosDataTableModel miembro) {
        MiembrosDB miembrosDB = new MiembrosDB();
        String idMiembro = miembro.getIdmiembro();
        miembrosModel = miembrosDB.consultarMiembroPorIdMiembro(idMiembro);

        txtNombres.setText(miembrosModel.getNombres());
        txtApellidoPat.setText(miembrosModel.getApellidoPat());
        txtApellidoMat.setText(miembrosModel.getApellidoMat());
        txtTelefono.setText(miembrosModel.getTelefono());
        txtEmail.setText(miembrosModel.getEmail());
        txtFecNacimiento.setValue(LocalDate.now());
        if(miembrosModel.getSexo().equals("H")){
            hombreRadBtn.setSelected(true);
        }else if(miembrosModel.getSexo().equals("M")) {
            mujerRadBtn.setSelected(true);
        }
        cmbTipoSangre.setValue(miembrosModel.getTipoSangre());
        txtNombreContactoEmer.setText(miembrosModel.getNombreContactoEmer());
        txtTelContactoEmer.setText(miembrosModel.getTelefonoContactoEmer());
        txtAreaObserv.setText(miembrosModel.getObservaciones());

    }
}
