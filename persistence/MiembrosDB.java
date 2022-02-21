package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;
import org.jetbrains.annotations.NotNull;
import utils.ConnectionUtil;

import java.io.ByteArrayInputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MiembrosDB {

    public String guardarMiembro(@NotNull MiembrosModel miembrosModel, ByteArrayInputStream datosHuella, Integer tamañoHuella) {
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "INSERT INTO MIEMBROS ( NOMBRES, APELLIDO_PAT, APELLIDO_MAT, TELEFONO, EMAIL, SEXO, FECHA_NACIMIENTO, HUELLA, FECHA_REGISTRO, NOMBRE_CONTACTO_EMER, TELEFONO_CONTACTO_EMER, TIPO_SANGRE, OBSERVACIONES) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?)";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, miembrosModel.getNombres());
            preparedStatement.setString(2, miembrosModel.getApellidoPat());
            preparedStatement.setString(3, miembrosModel.getApellidoMat());
            preparedStatement.setString(4, miembrosModel.getTelefono());
            preparedStatement.setString(5, miembrosModel.getEmail());
            preparedStatement.setString(6, miembrosModel.getSexo());
            preparedStatement.setString(7, miembrosModel.getFecha_Nacimiento().toString());
            preparedStatement.setBinaryStream(8, datosHuella,tamañoHuella);
            preparedStatement.setDate(9, sqlDate);
            preparedStatement.setString(10, miembrosModel.getNombreContactoEmer());
            preparedStatement.setString(11, miembrosModel.getTelefonoContactoEmer());
            preparedStatement.setString(12, miembrosModel.getTipoSangre());
            preparedStatement.setString(13, miembrosModel.getObservaciones());
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
            String queryInsert = "UPDATE MIEMBROS SET NOMBRES=?, APELLIDO_PAT=?, APELLIDO_MAT=?, EMAIL=?, SEXO=?, FECHA_NACIMIENTO=?, HUELLA =?, NOMBRE_CONTACTO_EMER=?, TELEFONO_CONTACTO_EMER=?, TIPO_SANGRE=?, OBSERVACIONES=?, TELEFONO=? WHERE IDMIEMBRO = ?";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, miembrosModel.getNombres());
            preparedStatement.setString(2, miembrosModel.getApellidoPat());
            preparedStatement.setString(3, miembrosModel.getApellidoMat());
            preparedStatement.setString(4, miembrosModel.getEmail());
            preparedStatement.setString(5, miembrosModel.getSexo());
            preparedStatement.setString(6, miembrosModel.getFecha_Nacimiento().toString());
            preparedStatement.setBinaryStream(7, datosHuella,tamañoHuella);
            preparedStatement.setString(8, miembrosModel.getNombreContactoEmer());
            preparedStatement.setString(9, miembrosModel.getTelefonoContactoEmer());
            preparedStatement.setString(10, miembrosModel.getTipoSangre());
            preparedStatement.setString(11, miembrosModel.getObservaciones());
            preparedStatement.setString(12, miembrosModel.getTelefono());
            preparedStatement.setString(13, String.valueOf(miembrosModel.getIdMiembro()));
            preparedStatement.execute();
            return "Miembro actualizado con éxito.";
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

    public String actualizaMiembroSinHuella(@NotNull MiembrosModel miembrosModel) {
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "UPDATE MIEMBROS SET NOMBRES=?, APELLIDO_PAT=?, APELLIDO_MAT=?, EMAIL=?, SEXO=?, FECHA_NACIMIENTO=?, NOMBRE_CONTACTO_EMER=?, TELEFONO_CONTACTO_EMER=?, TIPO_SANGRE=?, OBSERVACIONES=?, TELEFONO=? WHERE IDMIEMBRO = ?";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, miembrosModel.getNombres());
            preparedStatement.setString(2, miembrosModel.getApellidoPat());
            preparedStatement.setString(3, miembrosModel.getApellidoMat());
            preparedStatement.setString(4, miembrosModel.getEmail());
            preparedStatement.setString(5, miembrosModel.getSexo());
            preparedStatement.setString(6, miembrosModel.getFecha_Nacimiento().toString());
            preparedStatement.setString(7, miembrosModel.getNombreContactoEmer());
            preparedStatement.setString(8, miembrosModel.getTelefonoContactoEmer());
            preparedStatement.setString(9, miembrosModel.getTipoSangre());
            preparedStatement.setString(10, miembrosModel.getObservaciones());
            preparedStatement.setString(11, miembrosModel.getTelefono());
            preparedStatement.setString(12, String.valueOf(miembrosModel.getIdMiembro()));
            preparedStatement.execute();
            return "Miembro actualizado con éxito.";
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

    public MiembrosModel consultarMiembroMasReciente(){
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

    public MiembrosModel consultarMiembroPorIdMiembro(String idMiembro){
        MiembrosModel miembrosModelRespuesta = new MiembrosModel();
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            String querySelect = "SELECT * FROM MIEMBROS WHERE IDMIEMBRO = ?";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(querySelect);
            preparedStatement.setString(1,idMiembro);
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
                miembrosModelRespuesta.setTelefono(rs.getString("TELEFONO"));
                miembrosModelRespuesta.setTelefonoContactoEmer(rs.getString("TELEFONO_CONTACTO_EMER"));
                miembrosModelRespuesta.setNombreContactoEmer(rs.getString("NOMBRE_CONTACTO_EMER"));
                miembrosModelRespuesta.setObservaciones(rs.getString("OBSERVACIONES"));
                miembrosModelRespuesta.setTipoSangre(rs.getString("TIPO_SANGRE"));
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

    public String cobrarMembresia(MiembrosModel miembrosModel, String tipoSuscripcion, double cantidad_Pago, Date fechaVenc){
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date fechaPago = new java.sql.Date(utilDate.getTime());
        java.sql.Date fechaVencimiento = new java.sql.Date(fechaVenc.getTime());
        PreparedStatement preparedStatement;

        try {
            String st = "INSERT INTO PAGOS_SUSCRIPCION ( IDMIEMBRO, FECHA_PAGO, TIPO_SUSCRIPCION, CANTIDAD_PAGO, VENCIMIENTO) VALUES (?,?,?,?,?)";
            preparedStatement = newCon.conDB().prepareStatement(st);
            preparedStatement.setInt(1, miembrosModel.getIdMiembro());
            preparedStatement.setDate(2, fechaPago);
            preparedStatement.setString(3, tipoSuscripcion);
            preparedStatement.setDouble(4, cantidad_Pago);
            preparedStatement.setDate(5, fechaVencimiento);
            preparedStatement.execute();
            liquidarAdeudos(miembrosModel.getIdMiembro());
            return "Pago registrado con éxito. Su suscripción es " + tipoSuscripcion + " y su fecha de vencimiento es " + fechaVencimiento;
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

    public void liquidarAdeudos(int idMiembro){
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            String st = "UPDATE ADEUDO SET ESTATUS = 'PAGADO' WHERE IDMIEMBRO = ?";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(st);
            preparedStatement.setInt(1, idMiembro);
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public ObservableList consultaMiembrosExistentes() {
        ObservableList<MiembrosDataTableModel> data;
        ConnectionUtil newCon = new ConnectionUtil();
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = newCon.conDB().createStatement().executeQuery("SELECT IDMIEMBRO, NOMBRES, APELLIDO_PAT, APELLIDO_MAT, DATE_FORMAT( FECHA_REGISTRO,  '%d-%m-%Y' ) as FECHA_REGISTRO, CONCAT(NOMBRES, \" \", APELLIDO_PAT,\" \",APELLIDO_MAT) as NOMBRE_COMPLETO FROM MIEMBROS");

            while (rs.next()) {
                MiembrosDataTableModel fila = new MiembrosDataTableModel(null, null, null, null, null, null);
                int idMiembro = rs.getInt("idmiembro");
                String idMiembroStg = String.valueOf(idMiembro);
                fila.setIdmiembro(idMiembroStg);
                fila.setNombres(rs.getString("nombres"));
                fila.setApellido_pat(rs.getString("apellido_pat"));
                fila.setApellido_mat(rs.getString("apellido_mat"));
                fila.setFecha_registro(rs.getString("fecha_registro"));
                fila.setNombreCompleto(rs.getString("NOMBRE_COMPLETO"));
                data.add(fila);
            }
            //tableMiembros.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return data;
    }

    public Adeudo generaAdeudo(int idMiembro, String concepto, double cantidadAdeudo){
        Adeudo adeudo = new Adeudo();
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        Date fechaHoy = new Date();
        Timestamp timestamp = new Timestamp(fechaHoy.getTime());
        PreparedStatement preparedStatement;

        try {
            String st = "INSERT INTO ADEUDO ( IDMIEMBRO, CONCEPTO, CANTIDADADEUDO, FECHAADEUDO, ESTATUS) VALUES (?,?,?,?,?)";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(st);
            preparedStatement.setInt(1, idMiembro);
            preparedStatement.setString(2, concepto);
            preparedStatement.setDouble(3, cantidadAdeudo);
            preparedStatement.setTimestamp(4, timestamp);
            preparedStatement.setString(5, "ADEUDO");
            preparedStatement.execute();
            adeudo.setIdMiembro(idMiembro);
            adeudo.setConcepto(concepto);
            adeudo.setCantidadAdeudo(cantidadAdeudo);
            adeudo.setMensajeRespuesta("Se generó un adeudo con el concepto: "+ concepto + "  con un cargo de: $"+ cantidadAdeudo);
            return adeudo;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            adeudo.setMensajeRespuesta("Ocurrió un error al generar el adeudo.");
            return adeudo;
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public ObservableList consultarAdeudosMultasObs(int idMiembro){
        ObservableList<AdeudoDataTableModel> data;
        ConnectionUtil connection = new ConnectionUtil();
        data = FXCollections.observableArrayList();
        try {
            PreparedStatement identificarStmt = connection.conDB().prepareStatement("SELECT IDADEUDO, IDMIEMBRO, CONCEPTO, CANTIDADADEUDO, DATE_FORMAT( fechaAdeudo,  '%d-%m-%Y' ) as FECHAADEUDO, ESTATUS FROM ADEUDO WHERE IDMIEMBRO = ? AND ESTATUS = 'ADEUDO' AND CONCEPTO LIKE ?");
            identificarStmt.setInt(1, idMiembro);
            identificarStmt.setString(2,"%" + "Multa" + "%");
            ResultSet rs = identificarStmt.executeQuery();
            while(rs.next()){
                AdeudoDataTableModel fila = new AdeudoDataTableModel(null, null, null, 0.0, null, null);
                String idMiembroStg = String.valueOf(idMiembro);
                fila.setIdAdeudo(rs.getString("IDADEUDO"));
                fila.setIdmiembro(idMiembroStg);
                fila.setConcepto(rs.getString("CONCEPTO"));
                fila.setCantidadAdeudo(rs.getDouble("CANTIDADADEUDO"));
                fila.setFechaAdeudo(rs.getString("FECHAADEUDO"));
                fila.setEstatus(rs.getString("ESTATUS"));
                data.add(fila);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return data;
    }

    public List<Adeudo> consultarAdeudosMultas(int idMiembro) throws SQLException {
        ConnectionUtil connection = new ConnectionUtil();
        List<Adeudo> listaDeuda = new ArrayList<>();
        try {
            PreparedStatement identificarStmt = connection.conDB().prepareStatement("SELECT IDADEUDO, IDMIEMBRO, CONCEPTO, CANTIDADADEUDO, DATE_FORMAT( fechaAdeudo,  '%d-%m-%Y' ) as FECHAADEUDO, ESTATUS FROM ADEUDO WHERE IDMIEMBRO = ? AND ESTATUS = 'ADEUDO' AND CONCEPTO LIKE ?");
            identificarStmt.setInt(1, idMiembro);
            identificarStmt.setString(2,"%" + "Multa" + "%");
            ResultSet rs = identificarStmt.executeQuery();
            while(rs.next()){
                Adeudo adeudo = new Adeudo();
                adeudo.setIdAdeudo(rs.getInt("IDADEUDO"));
                adeudo.setIdMiembro(idMiembro);
                adeudo.setConcepto(rs.getString("CONCEPTO"));
                adeudo.setCantidadAdeudo(rs.getDouble("CANTIDADADEUDO"));
                adeudo.setFechaAdeudo(rs.getDate("FECHAADEUDO"));
                adeudo.setEstatus(rs.getString("ESTATUS"));
                listaDeuda.add(adeudo);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }finally {
            connection.conDB().close();
        }
        return listaDeuda;
    }

    public ObservableList consultaUsuariosSistema() {
        ObservableList<UsuariosDTM> data;
        ConnectionUtil newCon = new ConnectionUtil();
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = newCon.conDB().createStatement().executeQuery("SELECT DISTINCT NOMBRE_COMPLETO FROM USUARIOS");

            while (rs.next()) {
                UsuariosDTM fila = new UsuariosDTM(0, null, null, null, false, null);
                fila.setNombre_completo(rs.getString("NOMBRE_COMPLETO"));
                data.add(fila);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return data;
    }
}
