package persistence;

import models.EstadisticasModel;
import utils.ConnectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EstadisticasDB {

    public List<EstadisticasModel> consultarVisitasDiariasPorRango(String desde, String hasta) {
        List<EstadisticasModel> lista = new ArrayList<>();
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            String querySelect = "SELECT  DATE(fecha_ultima_visita) FECHA, COUNT(DISTINCT idmiembro) TOTAL\n" +
                    "FROM    registro_visitas\n" +
                    "WHERE fecha_ultima_visita BETWEEN ? AND ?\n" +
                    "GROUP   BY  DATE(fecha_ultima_visita) \n" +
                    "ORDER BY FECHA ASC";
            preparedStatement = newCon.conDB().prepareStatement(querySelect);
            preparedStatement.setString(1, desde);
            preparedStatement.setString(2, hasta);
            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                EstadisticasModel estadisticasObj = new EstadisticasModel();
                estadisticasObj.setFechas(rs.getString("FECHA"));
                estadisticasObj.setTotal(rs.getInt("TOTAL"));
                lista.add(estadisticasObj);
            }
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
        return lista;
    }
}
