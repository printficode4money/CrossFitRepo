package persistence;

import controllers.ReservacionesExt.DateEvent;
import utils.ConnectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

public class ReservacionesDB {

    public String createInsertQuery(Set<DateEvent> in) {
        Timestamp tsDateTime;
        Timestamp tsNotify;
        String resultado = null;
        for (DateEvent e : in) {
            tsDateTime = Timestamp.valueOf(e.getDateTime());
            tsNotify = Timestamp.valueOf(e.getNotifyTime());
            ConnectionUtil newCon = new ConnectionUtil();
            java.util.Date utilDate = new java.util.Date();
            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            PreparedStatement preparedStatement;
            try {
                String queryInsert = "INSERT INTO EVENTOS ( HORA_EVENTO, NOTIFY_TIME, DESCRIPCION, MIEMBRO) VALUES (?,?,?,?)";
                preparedStatement = newCon.conDB().prepareStatement(queryInsert);
                preparedStatement.setTimestamp(1, tsDateTime);
                preparedStatement.setTimestamp(2, tsNotify);
                preparedStatement.setString(3, e.getDescription());
                preparedStatement.setString(4, e.getNombreCompleto());
                preparedStatement.execute();
                resultado = "Evento agregado con éxito.";
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                resultado = "Ocurrió un error, contacte al administrador.";
            } finally {
                try {
                    newCon.conDB().close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return resultado;
    }

    public String eliminaEvento(DateEvent in) {
        int idEvento = 0;
        String resultado = null;
            ConnectionUtil newCon = new ConnectionUtil();
            PreparedStatement preparedStatement;
            idEvento = in.getIdEvento();
            try {
                String queryInsert = "DELETE FROM EVENTOS WHERE IDEVENTO = ?";
                preparedStatement = newCon.conDB().prepareStatement(queryInsert);
                preparedStatement.setInt(1, idEvento);
                preparedStatement.execute();
                resultado = "Evento eliminado con éxito.";
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                resultado = "Ocurrió un error, contacte al administrador.";
            } finally {
                try {
                    newCon.conDB().close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        return resultado;
    }

    public Set<DateEvent> read(){
        ConnectionUtil newCon = new ConnectionUtil();
        Set<DateEvent> events = new HashSet<>();
        //result = statement.executeQuery(query);

        LocalDateTime ldt, notify;
        String desc, nombreCompleto;
        int idEvento;

        ResultSet rs = null;
        try {
            rs = newCon.conDB().createStatement().executeQuery("SELECT IDEVENTO, HORA_EVENTO, NOTIFY_TIME, DESCRIPCION, MIEMBRO FROM EVENTOS");

            while (rs.next()) {
                idEvento = rs.getInt("IDEVENTO");
                ldt = rs.getTimestamp("HORA_EVENTO").toLocalDateTime();
                notify = rs.getTimestamp("NOTIFY_TIME").toLocalDateTime();
                desc = rs.getString("DESCRIPCION");
                nombreCompleto = rs.getString("MIEMBRO");
                events.add(new DateEvent(idEvento, ldt, notify, desc, nombreCompleto));
            }

        } catch (SQLException ex) {

        }
        return events;
    }
}
