package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.InventarioDTM;
import utils.ConnectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InventarioVentasDB {

    public ObservableList consultaInventario() {
        ObservableList<InventarioDTM> data;
        ConnectionUtil newCon = new ConnectionUtil();
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = newCon.conDB().createStatement().executeQuery("SELECT DISTINCT idinventario_ventas, nombre_articulo, descripcion, precio, existencias FROM INVENTARIO_VENTAS");

            while (rs.next()) {
                InventarioDTM fila = new InventarioDTM();
                fila.setIdInventario(rs.getInt("idinventario_ventas"));
                fila.setNombre(rs.getString("nombre_articulo"));
                fila.setDescripcion(rs.getString("descripcion"));
                fila.setPrecio(rs.getString("precio"));
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
            String queryInsert = "INSERT INTO INVENTARIO_VENTAS ( NOMBRE_ARTICULO, DESCRIPCION, PRECIO, EXISTENCIAS) VALUES (?,?,?,?)";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, inventarioObj.getNombre());
            preparedStatement.setString(2, inventarioObj.getDescripcion());
            //preparedStatement.setDouble(3, inventarioObj.getPrecio());
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

    public String eliminarFilaInventario(InventarioDTM inventarioObj) {
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "DELETE FROM INVENTARIO_VENTAS WHERE IDINVENTARIO_VENTAS = ? ";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setInt(1, inventarioObj.getIdInventario());
            preparedStatement.execute();
            return "Fila eliminada con éxito.";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "Ocurrió un error.";
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    public String actualizaFilaInventario(InventarioDTM inventarioObj) {
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "UPDATE INVENTARIO_VENTAS SET NOMBRE_ARTICULO=?, DESCRIPCION=?, EXISTENCIAS=?, PRECIO=? WHERE IDINVENTARIO_VENTAS = ?";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, inventarioObj.getNombre());
            preparedStatement.setString(2, inventarioObj.getDescripcion());
            preparedStatement.setInt(3, inventarioObj.getExistencias());
            //preparedStatement.setDouble(4, inventarioObj.getPrecio()); //TODO REVISAR ESTO
            preparedStatement.setInt(5, inventarioObj.getIdInventario());
            preparedStatement.execute();
            return "Inventario actualizado con éxito.";
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
            return "Ocurrió un error.";
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
