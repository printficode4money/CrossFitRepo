package persistence;

import utils.ConnectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ParametrosDB {

    public double consultaMulta() {
        double resultado = 0.0;
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            String st = "SELECT VALOR FROM PARAMETROS WHERE NOMBREPARAMETRO = ?";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(st);
            preparedStatement.setString(1, "multa_dia");
            preparedStatement.executeQuery();
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                resultado = rs.getDouble("VALOR");
            }
            return resultado;
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return 0.0;
        }
    }
}
