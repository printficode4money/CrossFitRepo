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
import utils.ConnectionUtil;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class UsuariosController implements Initializable{
    
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
    private Button btnSave;
    
    @FXML
    private ComboBox<String> txtGender;
    
   // @FXML
   // Label lblMensajes;

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

    /**
     * Initializes the controller class.
     */
    PreparedStatement preparedStatement;
    Connection connection;

    public UsuariosController() {
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
    
    public void guardarHuella(){
     ConnectionUtil newCon = new ConnectionUtil(); 
     //Obtiene los datos del template de la huella actual
     ByteArrayInputStream datosHuella = new ByteArrayInputStream(template.serialize());
     Integer tamañoHuella=template.serialize().length;

     //Pregunta el nombre de la persona a la cual corresponde dicha huella
     String nombre = txtNombres.getText();
     try {
     //Establece los valores para la sentencia SQL
     //Connection c=con.conectar(); //establece la conexion con la BD
     PreparedStatement guardarStmt = newCon.conDB().prepareStatement("INSERT INTO HUELLA(HUELLANOMBRE, HUELLA) values(?,?)");

     guardarStmt.setString(1,nombre);
     guardarStmt.setBinaryStream(2, datosHuella,tamañoHuella);
     //Ejecuta la sentencia
     guardarStmt.execute();
     guardarStmt.close();
     JOptionPane.showMessageDialog(null,"Huella Guardada Correctamente");
     //connection.close();
     newCon.conDB().close();
     //btnGuardar.setEnabled(false);
     //btnVerificar.grabFocus();
     } catch (SQLException ex) {
     //Si ocurre un error lo indica en la consola
     System.out.println("Error: "+ ex);
     System.err.println("Error al guardar los datos de la huella.");
     }finally{
         try {
             //connection.close();
               newCon.conDB().close();
         } catch (SQLException ex) {
             Logger.getLogger(UsuariosController.class.getName()).log(Level.SEVERE, null, ex);
         }
     }
   }
    
    public void EnviarTexto(String string) {
       txtMensajes.setText(string + "\n");
    }
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
        if (txtEmail.getText().isEmpty() /*|| txtNombres.getText().isEmpty() || txtApellidoPat.getText().isEmpty() || txtApellidoMat.getText().isEmpty() || txtFecNacimiento.getValue().equals(null)*/) {
            txtMensajes.setText("Llena todos los campos");
            //lblMensajes.setTextFill(Color.TOMATO);
        } else {
            saveData();
        }

    }

    private void clearFields() {
        txtNombres.clear();
        txtApellidoPat.clear();
        txtApellidoMat.clear();
        txtEmail.clear();
    }
    
    

    private String saveData() {
    //Obtiene los datos del template de la huella actual
    ConnectionUtil newCon = new ConnectionUtil(); 
     
     ByteArrayInputStream datosHuella = new ByteArrayInputStream(template.serialize());
     Integer tamañoHuella=template.serialize().length;
        try {
            String st = "INSERT INTO MIEMBROS ( NOMBRES, APELLIDO_PAT, APELLIDO_MAT, EMAIL, SEXO, FECHA_NACIMIENTO, HUELLA) VALUES (?,?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(st);
            preparedStatement.setString(1, txtNombres.getText());
            preparedStatement.setString(2, txtApellidoPat.getText());
            preparedStatement.setString(3, txtApellidoMat.getText());
            preparedStatement.setString(4, txtEmail.getText());
            preparedStatement.setString(5, "H");
            preparedStatement.setString(6, txtFecNacimiento.getValue().toString());
            preparedStatement.setBinaryStream(7, datosHuella,tamañoHuella);

            preparedStatement.execute();
            //txtMensajes.setTextFill(Color.GREEN);
            txtMensajes.setText("Miembro Agregado");
          //  guardarHuella();
         //   fetRowList();
            //clear fields
            clearFields();
            return "Success";

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            //lblMensajes.setTextFill(Color.TOMATO);
            txtMensajes.setText(ex.getMessage());
            return "Exception";
        }finally{
        try {
                Reclutador.clear();
                newCon.conDB().close();
            } catch (SQLException ex) {
                Logger.getLogger(UsuariosController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
   /* @FXML
    private void verificaHuella() {
      verificarHuella("Elma");  
      stop();
      start();
    }
    
    public void verificarHuella(String nom) {
    try {
    String duenioDedo = null;
    ConnectionUtil newCon = new ConnectionUtil(); 
    //Obtiene la plantilla correspondiente a la persona indicada
    PreparedStatement verificarStmt = newCon.conDB().prepareStatement("SELECT HUELLA FROM MIEMBROS WHERE NOMBRES=?");
    verificarStmt.setString(1,nom);
    ResultSet rs = verificarStmt.executeQuery();

    //Si se encuentra el nombre en la base de datos
    if (rs.next()){
    //Lee la plantilla de la base de datos
    byte templateBuffer[] = rs.getBytes("HUELLA");
    //duenioDedo = rs.getString("NOMBRES");
    //Crea una nueva plantilla a partir de la guardada en la base de datos
    DPFPTemplate referenceTemplate = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
    //Envia la plantilla creada al objeto contendor de Template del componente de huella digital
    setTemplate(referenceTemplate);

    // Compara las caracteriticas de la huella recientemente capturda con la
    // plantilla guardada al usuario especifico en la base de datos
    DPFPVerificationResult result = Verificador.verify(featuresverificacion, getTemplate());
    
    PreparedStatement verificarStmt2 = newCon.conDB().prepareStatement("SELECT NOMBRES FROM MIEMBROS WHERE HUELLA=?");
    byte[] vrr = template.serialize();
    verificarStmt2.setBytes(1,vrr);
    ResultSet rs2 = verificarStmt2.executeQuery();
     if (rs.next()){
    duenioDedo = rs2.getString("NOMBRES");
        System.out.println("EL DEDO ES DE:: " + duenioDedo);
     }
    //compara las plantilas (actual vs bd)
    if (result.isVerified())
    JOptionPane.showMessageDialog(null, "Las huella capturada coinciden con la de "+nom,"Verificacion de Huella", JOptionPane.INFORMATION_MESSAGE);
    else
    JOptionPane.showMessageDialog(null, "No corresponde la huella con "+nom, "Verificacion de Huella", JOptionPane.ERROR_MESSAGE);

    //Si no encuentra alguna huella correspondiente al nombre lo indica con un mensaje
    } else {
    JOptionPane.showMessageDialog(null, "No existe un registro de huella para "+nom, "Verificacion de Huella", JOptionPane.ERROR_MESSAGE);
    }
    } catch (SQLException e) {
    //Si ocurre un error lo indica en la consola
    System.err.println("Error al verificar los datos de la huella.");
        System.out.println("Error :" +e);
    }finally{
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(UsuariosController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   }*/

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
    
    public void identificarHuella() throws IOException{
     try {
       //Establece los valores para la sentencia SQL
       ConnectionUtil newCon = new ConnectionUtil();

       //Obtiene todas las huellas de la bd
       PreparedStatement identificarStmt = newCon.conDB().prepareStatement("SELECT NOMBRES,HUELLA FROM MIEMBROS");
       ResultSet rs = identificarStmt.executeQuery();

       //Si se encuentra el nombre en la base de datos
       while(rs.next()){
            //Lee la plantilla de la base de datos
            byte templateBuffer[] = rs.getBytes("HUELLA");
            String nombre=rs.getString("NOMBRES");
            //Crea una nueva plantilla a partir de la guardada en la base de datos
            DPFPTemplate referenceTemplate = DPFPGlobal.getTemplateFactory().createTemplate(templateBuffer);
            //Envia la plantilla creada al objeto contendor de Template del componente de huella digital
            setTemplate(referenceTemplate);

            // Compara las caracteriticas de la huella recientemente capturda con la
            // alguna plantilla guardada en la base de datos que coincide con ese tipo
            DPFPVerificationResult result = Verificador.verify(featuresverificacion, getTemplate());

            //compara las plantilas (actual vs bd)
            //Si encuentra correspondencia dibuja el mapa
            //e indica el nombre de la persona que coincidió.
            if (result.isVerified()){
                //crea la imagen de los datos guardado de las huellas guardadas en la base de datos
                JOptionPane.showMessageDialog(null, "Las huella capturada es de "+nombre,"Verificacion de Huella", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
       }
       newCon.conDB().close();

     //Si no encuentra alguna huella correspondiente al nombre lo indica con un mensaje
       JOptionPane.showMessageDialog(null, "No existe ningún registro que coincida con la huella", "Verificacion de Huella", JOptionPane.ERROR_MESSAGE);
       setTemplate(null);
       } catch (SQLException e) {
       //Si ocurre un error lo indica en la consola
       System.err.println("Error al identificar huella dactilar."+e.getMessage());
       }finally{
         Reclutador.clear();
       }
   }

    
   
}
