package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.InventarioDTM;
import models.VentasDTM;
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
        ResultSet rs;
        if (formaPago.equals("Efectivo") && recibido < totalCobro) {
            return "Error. El monto recibido no cubre el costo total.";
        } else {
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
                    preparedStatement.setString(4, articulo.getCantidadVenta());
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
    }

    public String actualizaInventario(ObservableList<InventarioDTM> carrito) {
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            for (InventarioDTM articulo : carrito) {
                String queryInsert = "UPDATE INVENTARIO_VENTAS SET EXISTENCIAS = ? WHERE IDINVENTARIO_VENTAS = ?";
                preparedStatement = newCon.conDB().prepareStatement(queryInsert);
                preparedStatement.setInt(1, (articulo.getExistencias() - Integer.parseInt(articulo.getCantidadVenta())));
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

    public ObservableList consultaCorteCaja(String fechaInicio, String fechaFin) {
        ObservableList<VentasDTM> data;
        ConnectionUtil newCon = new ConnectionUtil();
        data = FXCollections.observableArrayList();
        PreparedStatement preparedStatement;
        ResultSet rs;
        try {
            String querySelect ="select distinct det.idDetalle_venta, vta.fecha_venta, det.nombre_articulo, det.CANTIDAD_VENDIDO * det.precio as Monto, vta.forma_pago, vta.cambio\n" +
                    "from detalle_ventas det\n" +
                    "join ventas vta\n" +
                    "on det.idVenta = vta.idventa\n" +
                    "where vta.cambio = 0\n" +
                    "AND DATE(vta.fecha_venta) BETWEEN ? AND ?\n" +
                    "\n" +
                    "UNION all\n" +
                    "\n" +
                    "select distinct det.idDetalle_venta, DATE(vta.fecha_venta), det.nombre_articulo, det.CANTIDAD_VENDIDO * det.precio as Monto, vta.forma_pago, vta.cambio\n" +
                    "from detalle_ventas det\n" +
                    "join ventas vta\n" +
                    "on det.idVenta = vta.idventa\n" +
                    "where vta.cambio != 0\n" +
                    "AND vta.fecha_venta >= ? AND vta.fecha_venta  <= ?";

            preparedStatement = newCon.conDB().prepareStatement(querySelect);
            preparedStatement.setString(1, fechaInicio);
            preparedStatement.setString(2, fechaFin);
            preparedStatement.setString(3, fechaInicio);
            preparedStatement.setString(4, fechaFin);
            preparedStatement.executeQuery();
            rs = preparedStatement.executeQuery();

            while (rs.next()) {
                VentasDTM fila = new VentasDTM();
                fila.setIdDetalle_venta(rs.getInt("idDetalle_venta"));
                fila.setFecha_venta(rs.getDate("fecha_venta"));
                fila.setConcepto(rs.getString("nombre_articulo"));
                fila.setMonto(rs.getDouble("Monto"));
                fila.setForma_pago(rs.getString("forma_pago"));
                fila.setCambio(rs.getDouble("cambio"));
                data.add(fila);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            //return "Error. Consulte al administrador.";
        } finally {
            try {
                newCon.conDB().close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return data;
    }
}
