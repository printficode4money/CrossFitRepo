package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.MiembrosDataTableModel;
import models.MiembrosModel;
import org.jetbrains.annotations.NotNull;
import utils.ConnectionUtil;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MiembrosDB {

    public String guardarMiembro(@NotNull MiembrosModel miembrosModel, ByteArrayInputStream datosHuella, Integer tamañoHuella) {
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "INSERT INTO MIEMBROS ( NOMBRES, APELLIDO_PAT, APELLIDO_MAT, EMAIL, SEXO, FECHA_NACIMIENTO, HUELLA, FECHA_REGISTRO, NOMBRE_CONTACTO_EMER, TELEFONO_CONTACTO_EMER, TIPO_SANGRE, OBSERVACIONES) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, miembrosModel.getNombres());
            preparedStatement.setString(2, miembrosModel.getApellidoPat());
            preparedStatement.setString(3, miembrosModel.getApellidoMat());
            preparedStatement.setString(4, miembrosModel.getEmail());
            preparedStatement.setString(5, miembrosModel.getSexo());
            preparedStatement.setString(6, miembrosModel.getFecha_Nacimiento().toString());
            preparedStatement.setBinaryStream(7, datosHuella,tamañoHuella);
            preparedStatement.setDate(8, sqlDate);
            preparedStatement.setString(9, miembrosModel.getNombreContactoEmer());
            preparedStatement.setString(10, miembrosModel.getTelefonoContactoEmer());
            preparedStatement.setString(11, miembrosModel.getTipoSangre());
            preparedStatement.setString(12, miembrosModel.getObservaciones());
            preparedStatement.execute();


            return "Miembro agregado con éxito.";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "Ocurrió un error. Revise los datos.";
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String actualizaMiembro(@NotNull MiembrosModel miembrosModel, ByteArrayInputStream datosHuella, Integer tamañoHuella) {
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "INSERT INTO MIEMBROS ( NOMBRES, APELLIDO_PAT, APELLIDO_MAT, EMAIL, SEXO, FECHA_NACIMIENTO, HUELLA, FECHA_REGISTRO, NOMBRE_CONTACTO_EMER, TELEFONO_CONTACTO_EMER, TIPO_SANGRE, OBSERVACIONES) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, miembrosModel.getNombres());
            preparedStatement.setString(2, miembrosModel.getApellidoPat());
            preparedStatement.setString(3, miembrosModel.getApellidoMat());
            preparedStatement.setString(4, miembrosModel.getEmail());
            preparedStatement.setString(5, miembrosModel.getSexo());
            preparedStatement.setString(6, miembrosModel.getFecha_Nacimiento().toString());
            preparedStatement.setBinaryStream(7, datosHuella,tamañoHuella);
            preparedStatement.setDate(8, sqlDate);
            preparedStatement.setString(9, miembrosModel.getNombreContactoEmer());
            preparedStatement.setString(10, miembrosModel.getTelefonoContactoEmer());
            preparedStatement.setString(11, miembrosModel.getTipoSangre());
            preparedStatement.setString(12, miembrosModel.getObservaciones());
            preparedStatement.execute();


            return "Miembro agregado con éxito.";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "Ocurrió un error. Revise los datos.";
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public MiembrosModel consultarMiembro(){
        MiembrosModel miembrosModelRespuesta = new MiembrosModel();
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            String querySelect = "SELECT * FROM MIEMBROS ORDER BY IDMIEMBRO DESC LIMIT 1";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(querySelect);
            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                miembrosModelRespuesta.setIdMiembro(rs.getInt("IDMIEMBRO"));
                miembrosModelRespuesta.setNombres(rs.getString("NOMBRES"));
                miembrosModelRespuesta.setApellidoPat(rs.getString("APELLIDO_PAT"));
                miembrosModelRespuesta.setApellidoMat(rs.getString("APELLIDO_MAT"));
                miembrosModelRespuesta.setEmail(rs.getString("EMAIL"));
                miembrosModelRespuesta.setSexo(rs.getString("SEXO"));
                miembrosModelRespuesta.setFecha_Nacimiento(rs.getString("FECHA_NACIMIENTO"));
            }
            return miembrosModelRespuesta;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return null;
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public double obtenerCostoMembresia(String tipoMembresia){
        double resultado = 0;
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            String st = "SELECT COSTO FROM CONFIG_MEMBRESIA WHERE TIPO_MEMBRESIA = ?";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(st);
            preparedStatement.setString(1, tipoMembresia);
            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.executeQuery();
            while(rs.next()){
                resultado  = rs.getDouble("COSTO");
            }
            return resultado;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return 0.0;
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String cobrarMembresia(MiembrosModel miembrosModel, String tipoSuscripcion, double cantidad_Pago){
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date fechaPago = new java.sql.Date(utilDate.getTime());
        PreparedStatement preparedStatement;
        try {
            String st = "INSERT INTO PAGOS_SUSCRIPCION ( IDMIEMBRO, FECHA_PAGO, TIPO_SUSCRIPCION, CANTIDAD_PAGO) VALUES (?,?,?,?)";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(st);
            preparedStatement.setInt(1, miembrosModel.getIdMiembro());
            preparedStatement.setDate(2, fechaPago);
            preparedStatement.setString(3, tipoSuscripcion);
            preparedStatement.setDouble(4, cantidad_Pago);
            preparedStatement.execute();
            return "Pago registrado con éxito.";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "Ocurrió un error. Revise los datos.";
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public ObservableList consultaUsuariosExistentes() {
        ObservableList<MiembrosDataTableModel> data;
        ConnectionUtil newCon = new ConnectionUtil();
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = newCon.conDB().createStatement().executeQuery("SELECT IDMIEMBRO, NOMBRES, APELLIDO_PAT, APELLIDO_MAT  FROM MIEMBROS");

            while (rs.next()) {
                MiembrosDataTableModel fila = new MiembrosDataTableModel(null, null, null, null);
                int idMiembro = rs.getInt("idmiembro");
                String idMiembroStg = String.valueOf(idMiembro);
                fila.setIdmiembro(idMiembroStg);
                fila.setNombres(rs.getString("nombres"));
                fila.setApellido_pat(rs.getString("apellido_pat"));
                fila.setApellido_mat(rs.getString("apellido_mat"));
                data.add(fila);
            }
            //tableMiembros.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return data;
    }
}
