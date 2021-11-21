package controllers;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.*;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import models.MiembrosDataTableModel;
import models.MiembrosModel;
import persistence.MiembrosDB;
import persistence.RegistroVisitasDB;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class EditarUsuarioIndividualController implements Initializable {
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

//    @FXML
//    private Button btnVerificar;
    @FXML
    private Button btnActualizarHuella;

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
    private String idMiembro;
    final ToggleGroup group = new ToggleGroup();
    private double costoMembresia;
    boolean actualizaHuella =false;

    private MiembrosDataTableModel miembrosDataModel = new MiembrosDataTableModel();
    private DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();
    private DPFPEnrollment Reclutador = DPFPGlobal.getEnrollmentFactory().createEnrollment();
    private DPFPVerification Verificador = DPFPGlobal.getVerificationFactory().createVerification();
    private DPFPTemplate template;
    public static String TEMPLATE_PROPERTY = "template";

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cmbTipoSangre.getItems().add("O+");
        cmbTipoSangre.getItems().add("O-");
        cmbTipoSangre.getItems().add("A-");
        cmbTipoSangre.getItems().add("A+");
        cmbTipoSangre.getItems().add("B-");
        cmbTipoSangre.getItems().add("B+");
        cmbTipoSangre.getItems().add("AB-");
        cmbTipoSangre.getItems().add("AB+");

    }

    protected void Iniciar(){
        Lector.addDataListener(new DPFPDataAdapter() {
            @Override public void dataAcquired(final DPFPDataEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnviarTexto("La Huella Digital ha sido Capturada");
                        //lblMensajes.setText("La Huella Digital ha sido Capturada" + "\n");
                        //lblMensajes.setTextFill(Color.BLUE);
                        ProcesarCaptura(e.getSample());
                    }
                });
            }
        });

        Lector.addReaderStatusListener(new DPFPReaderStatusAdapter() {
            @Override
            public void readerConnected(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable(){
                    public void run(){
                        EnviarTexto("Ya puede escanear el espolón");
                        //lblMensajes.setText("Ya puede escanear el espolón" + "\n");
                        //lblMensajes.setTextFill(Color.ORANGE);
                    }
                });
            }

            @Override
            public void readerDisconnected(final DPFPReaderStatusEvent e) {
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        EnviarTexto("El Sensor de Huella Digital esta Desactivado o no Conectado");
                        //lblMensajes.setText("El Sensor de Huella Digital esta Desactivado o no Conectado" + "\n");
                        //lblMensajes.setTextFill(Color.RED);
                    }});}
        });

        Lector.addSensorListener(new DPFPSensorAdapter() {
            @Override
            public void fingerTouched(final DPFPSensorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {	public void run() {
                    EnviarTexto("El dedo ha sido colocado sobre el Lector de Huella");
                    //lblMensajes.setText("El dedo ha sido colocado sobre el Lector de Huella" + "\n");
                    //lblMensajes.setTextFill(Color.CRIMSON);
                }});}


            @Override
            public void fingerGone(final DPFPSensorEvent e) {
                SwingUtilities.invokeLater(new Runnable() {	public void run() {
                    EnviarTexto("El dedo ha sido quitado del Lector de Huella");
                    //lblMensajes.setText("El dedo ha sido quitado del Lector de Huella" + "\n");
                    //lblMensajes.setTextFill(Color.CRIMSON);
                }});}
        });

        Lector.addErrorListener(new DPFPErrorAdapter(){
            public void errorReader(final DPFPErrorEvent e){
                SwingUtilities.invokeLater(new Runnable() {  public void run() {
                    EnviarTexto("Error: "+e.getError());
                    //lblMensajes.setText("Error: "+e.getError());
                    //lblMensajes.setTextFill(Color.CRIMSON);
                }});}
        });
    }

    public  void start(){
        Lector.startCapture();
        EnviarTexto("Utilizando el Lector de Huella Dactilar ");
    }

    public  void stop(){
        Lector.stopCapture();
        EnviarTexto("No se está usando el Lector de Huella Dactilar ");
    }

    public void EnviarTexto(String string) {
        txtMensajes.setText(string + "\n");
    }

    public DPFPTemplate getTemplate() {
        return template;
    }

    public void setTemplate(DPFPTemplate template) {
        DPFPTemplate old = this.template;
        this.template = template;
        // firePropertyChange(TEMPLATE_PROPERTY, old, template);
    }

    public DPFPFeatureSet featuresinscripcion;
    public DPFPFeatureSet featuresverificacion;

    public  DPFPFeatureSet extraerCaracteristicas(DPFPSample sample, DPFPDataPurpose purpose){
        DPFPFeatureExtraction extractor = DPFPGlobal.getFeatureExtractionFactory().createFeatureExtraction();
        try{
            return extractor.createFeatureSet(sample, purpose);
        }catch (DPFPImageQualityException e) {
            return null;
        }
    }
    public  java.awt.Image CrearImagenHuella(DPFPSample sample) {
        return DPFPGlobal.getSampleConversionFactory().createImage(sample);
    }

    public void DibujarHuella(java.awt.Image image) {
        Image image2 = SwingFXUtils.toFXImage((BufferedImage) image, null);
        imgHuella.setImage(image2);
    }


    public  void EstadoHuellas(){
        Platform.runLater(
                new Runnable() {
                    @Override
                    public void run() {
                        EnviarTexto("Muestras necesarias: "+ Reclutador.getFeaturesNeeded());
                    }
                });
        //lblMensajes.setText("Muestras Necesarias para Huella "+ Reclutador.getFeaturesNeeded());
        //lblMensajes.setTextFill(Color.RED);
        //EnviarTexto("Muestras Necesarias para Huella "+ Reclutador.getFeaturesNeeded());
    }


    public  void ProcesarCaptura(DPFPSample sample){
        // Procesar la muestra de la huella y crear un conjunto de características con el propósito de inscripción.
        featuresinscripcion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_ENROLLMENT);

        // Procesar la muestra de la huella y crear un conjunto de características con el propósito de verificacion.
        featuresverificacion = extraerCaracteristicas(sample, DPFPDataPurpose.DATA_PURPOSE_VERIFICATION);

        // Comprobar la calidad de la muestra de la huella y lo añade a su reclutador si es bueno
        if (featuresinscripcion != null)
            try{
                System.out.println("Las Caracteristicas de la Huella han sido creada");
                Reclutador.addFeatures(featuresinscripcion);// Agregar las caracteristicas de la huella a la plantilla a crear

                // Dibuja la huella dactilar capturada.
                java.awt.Image image=CrearImagenHuella(sample);
                DibujarHuella(image);

//        btnVerificar.setEnbtnVerificarabled(true);
//        btnIdentificar.setEnabled(true);

            }catch (DPFPImageQualityException ex) {
                System.err.println("Error: "+ex.getMessage());
            }

            finally {
                EstadoHuellas();
                // Comprueba si la plantilla se ha creado.
                switch(Reclutador.getTemplateStatus())
                {
                    case TEMPLATE_STATUS_READY:	// informe de éxito y detiene  la captura de huellas
                        stop();
                        setTemplate(Reclutador.getTemplate());
                        EnviarTexto("La huella ha sido creada");
                        break;

                    //case TEMPLATE_STATUS_INSUFFICIENT:


                    case TEMPLATE_STATUS_FAILED: // informe de fallas y reiniciar la captura de huellas
                        Reclutador.clear();
                        stop();
//               EstadoHuellas();
                        setTemplate(null);
                        //JOptionPane.showMessageDialog(CapturaHuella.this, "La Plantilla de la Huella no pudo ser creada, Repita el Proceso", "Inscripcion de Huellas Dactilares", JOptionPane.ERROR_MESSAGE);
                        start();
                        break;
                }
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
//            Optional<ButtonType> resultadoOpcModal;
//            Alert alert = new Alert(Alert.AlertType.INFORMATION);
//            alert.setTitle("Actualización de Datos");
//            alert.setHeaderText("¿Desea recapturar la huella del miembro?");
//            alert.setContentText("Elige una opción:");
//            ButtonType btnAceptar = new ButtonType("Recapturar Huella");
//            ButtonType btnMantener = new ButtonType("Mantener Huella");
//            ButtonType btnCancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
//            alert.getButtonTypes().setAll(btnAceptar, btnMantener, btnCancelar);
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

//            resultadoOpcModal = alert.showAndWait();
//            if (resultadoOpcModal.get() == btnAceptar) {
//                actualizaHuella = true;
//                JOptionPane.showMessageDialog(null, "Captura una nueva huella digital", "Verificación de Huella", JOptionPane.INFORMATION_MESSAGE);
//            }
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            actualizaMiembro(miembrosModel, actualizaHuella, stage);
        }
    }

    @FXML
    private void actualizarHuella(MouseEvent event){
        actualizaHuella =true;
        JOptionPane.showMessageDialog(null, "Captura una nueva huella digital antes de guardar", "Verificación de Huella", JOptionPane.INFORMATION_MESSAGE);
        Iniciar();
        start();
        EstadoHuellas();
        btnActualizarHuella.setDisable(true);
    }

    private void actualizaMiembro(MiembrosModel miembro, boolean actualizaHuella, Stage stage) {
        String resultado;
        miembro.setIdMiembro(Integer.parseInt(idMiembro));
        MiembrosDB miembrosDB = new MiembrosDB();
        try {
            if (actualizaHuella){
                if (txtMensajes.getText().equals("La huella ha sido creada")) {
                    ByteArrayInputStream datosHuella = new ByteArrayInputStream(template.serialize());
                    Integer tamañoHuella = template.serialize().length;
                    resultado = miembrosDB.actualizaMiembro(miembro, datosHuella, tamañoHuella);
                    JOptionPane.showMessageDialog(null, resultado, "Actualización de Datos", JOptionPane.INFORMATION_MESSAGE);
                    clearFields();
                    Reclutador.clear();
                    stage.close();
                }else{
                    JOptionPane.showMessageDialog(null, "Debes capturar las muestras necesarias para crear una nueva huella digital", "Actualización de Datos", JOptionPane.ERROR_MESSAGE);
                    stage.setAlwaysOnTop(true);
                }
            } else {
                resultado = miembrosDB.actualizaMiembroSinHuella(miembro);
                JOptionPane.showMessageDialog(null, resultado, "Actualización de Datos", JOptionPane.INFORMATION_MESSAGE);
                clearFields();
                Reclutador.clear();
                stage.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
           // Reclutador.clear();
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
        idMiembro = miembro.getIdmiembro();
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
