package controllers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Catalogo_Ejercicio_DTM;
import models.Clasificacion_WOD_DTM;
import persistence.WODDB;

import javax.swing.*;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class CreadorEjerciciosController implements Initializable {

    @FXML private ComboBox cmbNuevo;
    @FXML private TextField txtNombreGpoMusc;
    @FXML private Label lblNombre;
    @FXML private TextField txtNombreEjercicio;
    @FXML private Label lblComplex;
    @FXML private ComboBox cmbComplex;
    @FXML private Button btnGuardarGpo;
    @FXML private Button btnGuardarEjercicio;
    @FXML private ComboBox cmbGpoMusc;
    @FXML private Label lblGpoMusc;
    @FXML private Button btnActualizarEjercicio;

    private Stage stageThis;
    Catalogo_Ejercicio_DTM objEjercicioEditar = new Catalogo_Ejercicio_DTM();

    public void initialize(URL url, ResourceBundle rb) {
        cmbNuevo.getItems().add("Grupo Muscular");
        cmbNuevo.getItems().add("Ejercicio");
        cmbComplex.getItems().add("Principiante");
        cmbComplex.getItems().add("Moderado");
        cmbComplex.getItems().add("Avanzado");
        lblNombre.setVisible(false);
        txtNombreGpoMusc.setVisible(false);
        txtNombreEjercicio.setVisible(false);
        lblComplex.setVisible(false);
        cmbComplex.setVisible(false);
        btnGuardarGpo.setVisible(false);
        btnGuardarEjercicio.setVisible(false);
        lblGpoMusc.setVisible(false);
        cmbGpoMusc.setVisible(false);

        cmbNuevo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(newValue != null && newValue.equals("Grupo Muscular")) {
                    lblNombre.setVisible(true);
                    txtNombreGpoMusc.setVisible(true);
                    txtNombreEjercicio.setVisible(false);
                    lblComplex.setVisible(false);
                    cmbComplex.setVisible(false);
                    btnGuardarGpo.setVisible(true);
                    btnGuardarEjercicio.setVisible(false);
                    cmbGpoMusc.setVisible(false);
                    lblGpoMusc.setVisible(false);
                    btnActualizarEjercicio.setVisible(false);
                }else if(newValue != null && newValue.equals("Ejercicio")){
                    lblNombre.setVisible(true);
                    txtNombreGpoMusc.setVisible(false);
                    txtNombreEjercicio.setVisible(true);
                    lblComplex.setVisible(true);
                    cmbComplex.setVisible(true);
                    btnGuardarGpo.setVisible(false);
                    btnGuardarEjercicio.setVisible(true);
                    cmbGpoMusc.setVisible(true);
                    lblGpoMusc.setVisible(true);
                    btnActualizarEjercicio.setVisible(false);
                }
            }
        });
    }

    public void guardarNvoGpoMuscular() {
        String resultado;
        WODDB woddb = new WODDB();
        try {
            if (!txtNombreGpoMusc.getText().isEmpty() && cmbNuevo.getValue().equals("Grupo Muscular")) {
                Clasificacion_WOD_DTM clasificacion_wod_dtm = new Clasificacion_WOD_DTM();
                clasificacion_wod_dtm.setGrupoMuscular(txtNombreGpoMusc.getText());
                resultado = woddb.guardarNvoGpoMuscular(clasificacion_wod_dtm);
                JOptionPane.showMessageDialog(null, resultado, "Mensaje Informativo de Inventario", JOptionPane.INFORMATION_MESSAGE);
                txtNombreGpoMusc.clear();
                stageThis.close();
            } else {
                JOptionPane.showMessageDialog(null, "Todos los campos deben llenarse correctamente.", "Mensaje Informativo de Inventario", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void guardarNvoEjercicio() {
        String resultado;
        WODDB woddb = new WODDB();
        try {
            if (!txtNombreEjercicio.getText().isEmpty() && cmbNuevo.getValue().equals("Ejercicio")) {
                Catalogo_Ejercicio_DTM catalogo_ejercicio_dtm = new Catalogo_Ejercicio_DTM();
                catalogo_ejercicio_dtm.setNombreEjercicio(txtNombreEjercicio.getText());
                catalogo_ejercicio_dtm.setComplejidad((String) cmbComplex.getValue());
                catalogo_ejercicio_dtm.setGrupoMuscular((String) cmbGpoMusc.getValue());
                resultado = woddb.guardarNvoEjercicio(catalogo_ejercicio_dtm);
                JOptionPane.showMessageDialog(null, resultado, "Mensaje Informativo", JOptionPane.INFORMATION_MESSAGE);
                txtNombreEjercicio.clear();
                stageThis.close();
            } else {
                JOptionPane.showMessageDialog(null, "Todos los campos deben llenarse correctamente.", "Mensaje Informativo de Inventario", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void actualizarEjercicio() {
        String resultado;
        WODDB woddb = new WODDB();
        try {
            if (!txtNombreEjercicio.getText().isEmpty()) {
                Catalogo_Ejercicio_DTM catalogo_ejercicio_dtm = new Catalogo_Ejercicio_DTM();
                catalogo_ejercicio_dtm.setIdEjercicio(objEjercicioEditar.getIdEjercicio());
                catalogo_ejercicio_dtm.setNombreEjercicio(txtNombreEjercicio.getText());
                catalogo_ejercicio_dtm.setComplejidad((String) cmbComplex.getValue());
                catalogo_ejercicio_dtm.setGrupoMuscular((String) cmbGpoMusc.getValue());
                resultado = woddb.actualizarEjercicio(catalogo_ejercicio_dtm);
                JOptionPane.showMessageDialog(null, resultado, "Mensaje Informativo", JOptionPane.INFORMATION_MESSAGE);
                txtNombreEjercicio.clear();
                stageThis.close();
            } else {
                JOptionPane.showMessageDialog(null, "Todos los campos deben llenarse correctamente.", "Mensaje Informativo de Inventario", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void cancelar(){
        stageThis.close();
    }

    public void receiveData(Catalogo_Ejercicio_DTM objEjercicio, Stage stage, List<Clasificacion_WOD_DTM> listaGposMus) {
        if(objEjercicio != null) {
            objEjercicioEditar.setIdEjercicio(objEjercicio.getIdEjercicio());
            objEjercicioEditar.setNombreEjercicio(objEjercicio.getNombreEjercicio());
            objEjercicioEditar.setComplejidad(String.valueOf(objEjercicio.getComplejidad()));
            objEjercicioEditar.setGrupoMuscular(objEjercicio.getGrupoMuscular());
            txtNombreEjercicio.setVisible(true);
            lblNombre.setVisible(true);
            lblComplex.setVisible(true);
            lblGpoMusc.setVisible(true);
            txtNombreEjercicio.setText(objEjercicioEditar.getNombreEjercicio());
            cmbComplex.setVisible(true);
            cmbGpoMusc.setVisible(true);
            cmbComplex.setValue(objEjercicioEditar.getComplejidad());
            cmbGpoMusc.setValue(objEjercicioEditar.getGrupoMuscular());
            cmbNuevo.setDisable(true);
            btnGuardarGpo.setVisible(false);
            btnActualizarEjercicio.setVisible(true);
            btnGuardarEjercicio.setVisible(false);
        }
        if(!listaGposMus.isEmpty()){
            for(Clasificacion_WOD_DTM grupo : listaGposMus){
                cmbGpoMusc.getItems().add(grupo.getGrupoMuscular());
            }
        }
        stageThis = stage;
    }
}
