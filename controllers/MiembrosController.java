/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.digitalpersona.onetouch.*;
import com.digitalpersona.onetouch.capture.DPFPCapture;
import com.digitalpersona.onetouch.capture.event.*;
import com.digitalpersona.onetouch.processing.DPFPEnrollment;
import com.digitalpersona.onetouch.processing.DPFPFeatureExtraction;
import com.digitalpersona.onetouch.processing.DPFPImageQualityException;
import com.digitalpersona.onetouch.verification.DPFPVerification;
import com.digitalpersona.onetouch.verification.DPFPVerificationResult;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import models.MiembrosModel;
import persistence.MiembrosDB;
import persistence.RegistroVisitasDB;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class MiembrosController implements Initializable{
    
    @FXML
    private Button btnVerificar;
    
    @FXML
    private Button btnGuardarMiembro;
    
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
    private Button btnSave;
    
    @FXML
    private ComboBox<String> txtGender;

    @FXML
    private RadioButton hombreRadBtn;

    @FXML
    private RadioButton mujerRadBtn;

    @FXML
    TableView tblData;
    
    @FXML
    private ImageView imgHuella;
    
    @FXML
    private Button btnMenuPrincipal;
    
    //@FXML
    //private TitledPane menuLateralPane;
    
    @FXML
    private TextField txtMensajes;

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

    @FXML
    private ComboBox cmbTipoSangre;

    @FXML
    private TextField txtNombreContactoEmer;

    @FXML
    private TextField txtTelContactoEmer;

    @FXML
    private TextArea txtAreaObserv;

    @FXML
    private Button btnRedirigeEditarUsuarios;

    private MiembrosModel miembrosModel = new MiembrosModel();
    private String sexo;
    final ToggleGroup group = new ToggleGroup();
    private double costoMembresia;

    public double getCostoMembresia() {
        return costoMembresia;
    }

    public void setCostoMembresia(double costoMembresia) {
        this.costoMembresia = costoMembresia;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    @FXML
    public void regresaMenuPrincipal(MouseEvent event) {
        if (event.getSource() == btnMenuPrincipal) {
                try {
                    Lector.stopCapture();
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

    @FXML
    public void abreEditarUsuarios(MouseEvent event){
        if (event.getSource() == btnRedirigeEditarUsuarios) {
            try {
                Lector.stopCapture();
                Node node = (Node) event.getSource();
                Stage stage = (Stage) node.getScene().getWindow();
                stage.close();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource("/interfaces/Usuarios_Todos_Editar.fxml")));
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                System.err.println(ex.getMessage());
            }
        }
    }

    /**
     * Initializes the controller class.
     */
    PreparedStatement preparedStatement;
    Connection connection;

    public MiembrosController() {
        //connection = (Connection) ConnectionUtil.conDB();
    }

    private DPFPCapture Lector = DPFPGlobal.getCaptureFactory().createCapture();

    //Varible que permite establecer las capturas de la huellas, para determina sus caracteristicas
    // y poder estimar la creacion de un template de la huella para luego poder guardarla
    private DPFPEnrollment Reclutador = DPFPGlobal.getEnrollmentFactory().createEnrollment();

    //Esta variable tambien captura una huella del lector y crea sus caracteristcas para auntetificarla
    // o verificarla con alguna guardada en la BD
    private DPFPVerification Verificador = DPFPGlobal.getVerificationFactory().createVerification();

    //Variable que para crear el template de la huella luego de que se hallan creado las caracteriticas
    // necesarias de la huella si no ha ocurrido ningun problema
    private DPFPTemplate template;
    public static String TEMPLATE_PROPERTY = "template";
    
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
                EnviarTexto("Muestras Necesarias para Huella "+ Reclutador.getFeaturesNeeded());
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
               EnviarTexto("La Plantilla de la Huella ha Sido Creada, ya puede Verificarla o Identificarla");
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

    
    public void EnviarTexto(String string) {
       txtMensajes.setText(string + "\n");
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        comboMembresias.getItems().add("Mensual");
        comboMembresias.getItems().add("Por día");
        comboMembresias.getItems().add("Cortesía");
        cmbTipoSangre.getItems().add("O+");
        cmbTipoSangre.getItems().add("O-");
        cmbTipoSangre.getItems().add("A-");
        cmbTipoSangre.getItems().add("A+");
        cmbTipoSangre.getItems().add("B-");
        cmbTipoSangre.getItems().add("B+");
        cmbTipoSangre.getItems().add("AB-");
        cmbTipoSangre.getItems().add("AB+");

        //detallesPane.setCollapsible(false);
        /*// TODO
        txtGender.getItems().addAll("Male", "Female", "Other");
        txtGender.getSelectionModel().select("Male");
        fetColumnList();
        fetRowList();*/
       /* menuLateralPane.setExpanded(true);
        menuLateralPane.setAnimated(true);*/
        Iniciar();
	    start();
        EstadoHuellas();

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
            guardarMiembro(miembrosModel);

        }

    }

    @FXML
    private void abreDetallesPane(MouseEvent event) {
        //MiembrosDB miembrosDB = new MiembrosDB();
       // System.out.println(comboMembresias.getSelectionModel().getSelectedItem().toString());
       //costoMembresia = miembrosDB.obtenerCostoMembresia(comboMembresias.getSelectionModel().getSelectedItem().toString());
        txtMontoMem.setEditable(false);
    }

    @FXML
    private void comboMembresiasOnChange(ActionEvent event) {
        MiembrosDB miembrosDB = new MiembrosDB();
        costoMembresia = miembrosDB.obtenerCostoMembresia(comboMembresias.getSelectionModel().getSelectedItem().toString());
        txtMontoMem.setText(String.valueOf(costoMembresia));
        txtMontoMem.setStyle("-fx-font-weight: bold");
    }

    @FXML
    private void calculaPorcentaje(MouseEvent event){
            Double precioMembresia;
            Double porcentajeDescuento;
            precioMembresia = Double.valueOf(txtMontoMem.getText().toString());
            porcentajeDescuento = Double.valueOf(txtPorcenDescuento.getText().toString());
            Double totalMembresia = (precioMembresia*porcentajeDescuento)/100;
            totalMembresia = precioMembresia - totalMembresia;
            txtMontoMem.setText(String.valueOf(totalMembresia));
            txtMontoMem.setStyle("-fx-font-weight: bold");
    }

    @FXML
    private void cobrarMembresia(){
        MiembrosDB miembrosDB = new MiembrosDB();
        String resultado = null;
        String tipoSuscripcion = comboMembresias.getSelectionModel().getSelectedItem().toString();
        Double cantidad_Pago = Double.valueOf(txtMontoMem.getText());
        java.util.Date fechaVencimiento = new java.util.Date();
        fechaVencimiento = validaFecha_Vencimiento(fechaVencimiento, tipoSuscripcion);
        resultado = miembrosDB.cobrarMembresia(miembrosModel, tipoSuscripcion, cantidad_Pago, fechaVencimiento);
        if(resultado.contains("éxito")) {
            JOptionPane.showMessageDialog(null, resultado, "Resultado Cobro", JOptionPane.INFORMATION_MESSAGE);
        }else if(resultado.contains("error")){
            JOptionPane.showMessageDialog(null, resultado + " ," + " HDTRPM!", "Resultado Cobro", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    private Calendar dateToCalendar(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;
    }

    private Date calendarToDate(Calendar calendar) {
        return calendar.getTime();
    }

    public Date validaFecha_Vencimiento(Date utilDate, String tipoSuscripcion){
        Calendar calendar = new GregorianCalendar(TimeZone.getTimeZone("GMT-7"));
        calendar.setTime(utilDate);
        if(tipoSuscripcion.equals("Mensual")){
            calendar.add(Calendar.DATE, 30);
        }else if(tipoSuscripcion.equals("Por Día")){
            calendar.add(Calendar.DATE, 1);
        }else if(tipoSuscripcion.equals("Cortesía")){
            calendar.add(Calendar.DATE, 0);
        }else if(tipoSuscripcion.equals("Semanal")) {
            calendar.add(Calendar.DATE, 7);
        }
        return utilDate = calendar.getTime();
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
    
    

    private void guardarMiembro(MiembrosModel miembrosModelGuarda) {
    //Obtiene los datos del template de la huella actual
     String resultado = null;
     ByteArrayInputStream datosHuella = new ByteArrayInputStream(template.serialize());
     Integer tamañoHuella=template.serialize().length;
     MiembrosDB miembrosDB = new MiembrosDB();

        try {
            resultado = miembrosDB.guardarMiembro(miembrosModelGuarda, datosHuella, tamañoHuella);
            miembrosModel = miembrosDB.consultarMiembroMasReciente();
            txtMensajes.setText(resultado);
            detallesPane.setCollapsible(true);
          //  guardarHuella();
            // fetRowList();
            //clear fields
            clearFields();
            //return "Success";

        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            Reclutador.clear();
        }
    }

    private ObservableList<ObservableList> data;
    String SQL = "SELECT * FROM MIEMBROS";

    //only fetch columns
    private void fetColumnList() {

        try {
            ResultSet rs = connection.createStatement().executeQuery(SQL);

            //SQL FOR SELECTING ALL OF CUSTOMER
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());
                col.setCellValueFactory(new Callback<CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });

                tblData.getColumns().removeAll(col);
                tblData.getColumns().addAll(col);

                System.out.println("Column [" + i + "] ");

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }
    }

    //fetches rows and data from the list
    private void fetRowList() {
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = connection.createStatement().executeQuery(SQL);

            while (rs.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }

            tblData.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
    
    private void setTxtMensajes(Color color, String text) {
        //txtMensajes.setTextFill(color);
        txtMensajes.setText(text);
        System.out.println(text);
    }

    public void identificarHuella() throws  SQLException {
        RegistroVisitasDB registroVisitasDB = new RegistroVisitasDB();
        List<MiembrosModel> listaHuellas = registroVisitasDB.identificaHuella();
        boolean existeHuella = false;
        for (int i = 0; i < listaHuellas.size(); i++) {

            DPFPTemplate referenceTemplate = DPFPGlobal.getTemplateFactory().createTemplate(listaHuellas.get(i).getHuella());
            setTemplate(referenceTemplate);
            DPFPVerificationResult result = Verificador.verify(featuresverificacion, getTemplate());
            if (result.isVerified()) {
                //crea la imagen de los datos guardado de las huellas guardadas en la base de datos
                JOptionPane.showMessageDialog(null, "¡Bienvenido " + listaHuellas.get(i).getNombres() + "," + " HDTRPM!", "Verificacion de Huella", JOptionPane.INFORMATION_MESSAGE);
                try {
                    registroVisitasDB.registrarVisita(listaHuellas.get(i).getIdMiembro());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                existeHuella = true;
                break;
            }
        }
    }
   
}
