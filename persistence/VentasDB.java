package persistence;

import javafx.collections.ObservableList;
import models.InventarioDTM;
import utils.ConnectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class VentasDB {

    public String guardarVenta(double totalCobro, String formaPago, double recibido, double cambio, ObservableList<InventarioDTM> carrito) {
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        int idGenerado = 0;
        Date fechaHoy = new Date();
        Timestamp timestamp = new Timestamp(fechaHoy.getTime());
        ResultSet rs = null;
        try {
            String queryInsert = "INSERT INTO VENTAS ( TOTAL_VENTA, FORMA_PAGO, RECIBIDO, CAMBIO, FECHA_VENTA) VALUES (?,?,?,?,?)";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setDouble(1, totalCobro);
            preparedStatement.setString(2, formaPago);
            preparedStatement.setDouble(3, recibido);
            preparedStatement.setDouble(4, cambio);
            preparedStatement.setTimestamp(5, timestamp);
            preparedStatement.execute();

            String queryUltimoRegistro = "SELECT IDVENTA FROM VENTAS ORDER BY IDVENTA DESC LIMIT 1";
            preparedStatement = newCon.conDB().prepareStatement(queryUltimoRegistro);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                idGenerado = rs.getInt("IDVENTA");
            }

            for (InventarioDTM articulo : carrito) {
                String queryInsert2 = "INSERT INTO DETALLE_VENTAS ( IDVENTA, NOMBRE_ARTICULO, PRECIO, CANTIDAD_VENDIDO) VALUES (?,?,?,?)";
                preparedStatement = newCon.conDB().prepareStatement(queryInsert2);
                preparedStatement.setInt(1, idGenerado);
                preparedStatement.setString(2, articulo.getNombre());
                preparedStatement.setString(3, articulo.getPrecio());
                preparedStatement.setInt(4, articulo.getCantidadVenta());
                preparedStatement.execute();
            }
            return "Venta registrada con éxito";
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

    public String actualizaInventario(ObservableList<InventarioDTM> carrito) {
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            for (InventarioDTM articulo : carrito) {
                String queryInsert = "UPDATE INVENTARIO_VENTAS SET EXISTENCIAS = ? WHERE IDINVENTARIO_VENTAS = ?";
                preparedStatement = newCon.conDB().prepareStatement(queryInsert);
                preparedStatement.setInt(1, (articulo.getExistencias() - articulo.getCantidadVenta()));
                preparedStatement.setInt(2, articulo.getIdInventario());
                preparedStatement.execute();
            }
            return "Actualizado con éxito.";
        } catch (SQLException ex) {
            ex.printStackTrace();
            return "Error. Consulte al administrador.";
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
