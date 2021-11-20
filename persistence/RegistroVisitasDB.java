package persistence;

import models.MiembrosModel;
import models.Pagos_Suscripcion_Model;
import utils.ConnectionUtil;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegistroVisitasDB {

    public void registrarVisita(int idMiembro) throws IOException, SQLException {
        ConnectionUtil connection = new ConnectionUtil();
        PreparedStatement preparedStatement;
        String query = null;
        Date fechaHoy = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Timestamp timestamp = new Timestamp(fechaHoy.getTime());
        try {
            query = "INSERT INTO REGISTRO_VISITAS (IDMIEMBRO, FECHA_ULTIMA_VISITA) VALUES (?,?)";
            preparedStatement = (PreparedStatement) connection.conDB().prepareStatement(query);
            preparedStatement.setInt(1, idMiembro);
            preparedStatement.setTimestamp(2, timestamp);
            preparedStatement.execute();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }finally {
            connection.conDB().close();
        }
    }

    public List<MiembrosModel> identificaHuella() throws SQLException {
        ConnectionUtil connection = new ConnectionUtil();
        List<MiembrosModel> listaMiembros = new ArrayList<>();
        try {
            PreparedStatement identificarStmt = connection.conDB().prepareStatement("SELECT NOMBRES, IDMIEMBRO, HUELLA FROM MIEMBROS");
            ResultSet rs = identificarStmt.executeQuery();
            while(rs.next()){
                MiembrosModel miembroHuella = new MiembrosModel();
                byte templateBuffer[] = rs.getBytes("HUELLA");
                miembroHuella.setHuella(templateBuffer);
                miembroHuella.setNombres(rs.getString("NOMBRES"));
                miembroHuella.setIdMiembro(rs.getInt("IDMIEMBRO"));
                listaMiembros.add(miembroHuella);
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }finally {
            connection.conDB().close();
        }
        return listaMiembros;
    }

    public Pagos_Suscripcion_Model consultaVigenciaMembresia(int idMiembro)throws SQLException {
        ConnectionUtil connection = new ConnectionUtil();
        List<MiembrosModel> listaMiembros = new ArrayList<>();
        Pagos_Suscripcion_Model resultado = new Pagos_Suscripcion_Model();
        try {
            PreparedStatement identificarStmt = connection.conDB().prepareStatement("SELECT * FROM PAGOS_SUSCRIPCION WHERE IDMIEMBRO = ? ORDER BY FECHA_PAGO DESC LIMIT 1");
            identificarStmt.setInt(1, idMiembro);
            ResultSet rs = identificarStmt.executeQuery();
            while(rs.next()){
                resultado.setIdPago(rs.getInt("IDPAGO"));
                resultado.setIdMiembro(rs.getInt("IDMIEMBRO"));
                resultado.setFechaPago(rs.getDate("FECHA_PAGO"));
                resultado.setTipoSuscripcion(rs.getString("TIPO_SUSCRIPCION"));
                resultado.setCantidadPago(rs.getDouble("CANTIDAD_PAGO"));
                resultado.setVencimiento(rs.getDate("VENCIMIENTO"));
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }finally {
            connection.conDB().close();
        }
    return resultado;
    }
}