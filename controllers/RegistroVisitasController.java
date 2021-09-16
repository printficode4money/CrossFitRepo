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
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.Window;
import models.MiembrosModel;
import persistence.RegistroVisitasDB;

import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author DonV3rga
 */
public class RegistroVisitasController implements Initializable{

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Iniciar();
	start();
        EstadoHuellas();

    }

    @FXML
    private ImageView imgHuella;

    @FXML
    private TextField txtMensajes;

    @FXML
    private Button btnMenuPrincipal;


    @FXML
    public void regresaMenuPrincipal(MouseEvent event) {
        if (event.getSource() == btnMenuPrincipal) {
                try {
                    Lector.stopCapture();
                    Stage este = (Stage)((Node) event.getSource()).getScene().getWindow();
                    este.hide();

                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/interfaces/Hub.fxml"));
                    Parent root1 = (Parent) fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.show();

                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
        }
    }

    RegistroVisitasDB registroVisitasDB;
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
    PreparedStatement preparedStatement;


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


    public  void EstadoHuellas(){Platform.runLater(new Runnable() {
            @Override public void run() {
                EnviarTexto("Muestras Necesarias para Huella "+ Reclutador.getFeaturesNeeded());
            }
            });
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
       /* java.awt.Image image=CrearImagenHuella(sample);
        DibujarHuella(image);*/

//        btnVerificar.setEnbtnVerificarabled(true);
//        btnIdentificar.setEnabled(true);

        }catch (DPFPImageQualityException ex) {
        System.err.println("Error: "+ex.getMessage());
        }

        finally {
        EstadoHuellas();
        try {
            identificarHuella();
        } catch (IOException | SQLException ex) {
            Logger.getLogger(RegistroVisitasController.class.getName()).log(Level.SEVERE, null, ex);
        }
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

    public void identificarHuella() throws IOException, SQLException {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    registroVisitasDB = new RegistroVisitasDB();
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
                    //Si no encuentra alguna huella correspondiente al nombre lo indica con un mensaje
                    if (existeHuella == false) {
                        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                        alert.setTitle("Verificación de Huella");
                        alert.setHeaderText(null);
                        alert.setContentText("Elige una opción:");

                        ButtonType btnRegistrarModal = new ButtonType("Registar");
                        /*ButtonType buttonTypeTwo = new ButtonType("Cancelar");*/
                        /* ButtonType buttonTypeThree = new ButtonType("Three");*/
                        ButtonType buttonTypeCancel = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);

                        alert.getButtonTypes().setAll(btnRegistrarModal, buttonTypeCancel);

                        Optional<ButtonType> result = alert.showAndWait();
                        if (result.get() == btnRegistrarModal) {

                            Lector.stopCapture();
                            Window window = txtMensajes.getScene().getWindow();
                            window.hide();

                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/interfaces/Usuarios2.fxml"));
                            Parent root1 = (Parent) fxmlLoader.load();
                            Stage stage = new Stage();
                            stage.setScene(new Scene(root1));
                            stage.show();

                        } /*else if (result.get() == buttonTypeTwo) {
                            // ... user chose "Two"
                        } else if (result.get() == buttonTypeThree) {
                            // ... user chose "Three"
                        } else {
                            // ... user chose CANCEL or closed the dialog
                        }*/
                        setTemplate(null);
                    }
                } catch (SQLException | IOException e) {
                    //Si ocurre un error lo indica en la consola
                    System.err.println("Error al identificar huella dactilar." + e.getMessage());
                } finally {
                    Reclutador.clear();
                }
            }

        });
    }

}
