package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.InventarioDTM;
import utils.ConnectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventarioDB {

    public ObservableList consultaInventario() {
        ObservableList<InventarioDTM> data;
        ConnectionUtil newCon = new ConnectionUtil();
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = newCon.conDB().createStatement().executeQuery("SELECT idinventario, nombre, descripcion, precio, existencias FROM INVENTARIO");

            while (rs.next()) {
                InventarioDTM fila = new InventarioDTM();
                fila.setNombre(rs.getString("nombre"));
                fila.setDescripcion(rs.getString("descripcion"));
                fila.setPrecio(rs.getDouble("precio"));
                fila.setExistencias(rs.getInt("existencias"));
                data.add(fila);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return data;
    }

    public String guardarArticuloInventario(InventarioDTM inventarioObj) {
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "INSERT INTO INVENTARIO ( NOMBRE, DESCRIPCION, PRECIO, EXISTENCIAS) VALUES (?,?,?,?)";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, inventarioObj.getNombre());
            preparedStatement.setString(2, inventarioObj.getDescripcion());
            preparedStatement.setDouble(3, inventarioObj.getPrecio());
            preparedStatement.setInt(4, inventarioObj.getExistencias());
            preparedStatement.execute();
            return "Artículo(s) agregado(s) con éxito.";
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

    public String buscarArticuloInventario(InventarioDTM inventarioObj) {
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "SELECT NOMBRE, DESCRIPCION, PRECIO, EXISTENCIAS FROM INVENTARIO ";
            preparedStatement = (PreparedStatement) newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, inventarioObj.getNombre());
            preparedStatement.setString(2, inventarioObj.getDescripcion());
            preparedStatement.setDouble(3, inventarioObj.getPrecio());
            preparedStatement.setInt(4, inventarioObj.getExistencias());
            preparedStatement.execute();
            return "Artículo(s) agregado(s) con éxito.";
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
}