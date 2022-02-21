package persistence;

import controllers.ReservacionesExt.DateEvent;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Catalogo_Ejercicio_DTM;
import models.Clasificacion_WOD_DTM;
import models.RutinaDTM;
import utils.ConnectionUtil;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class WODDB {

    public String guardarNvoGpoMuscular(Clasificacion_WOD_DTM clasificacion_wod_dtm) {
        String resultado = null;
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "INSERT INTO CLASIFICACION_WOD ( GRUPO_MUSCULAR) VALUES (?)";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, clasificacion_wod_dtm.getGrupoMuscular());
            preparedStatement.execute();
            return "Agregado con éxito.";
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

    public String guardarNvoEjercicio(Catalogo_Ejercicio_DTM catalogo_ejercicio_dtm) {
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "INSERT INTO CATALOGO_EJERCICIOS (NOMBREEJERCICIO, COMPLEJIDAD, GRUPO_MUSCULAR) VALUES (?, ?, ?)";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, catalogo_ejercicio_dtm.getNombreEjercicio());
            preparedStatement.setString(2, catalogo_ejercicio_dtm.getComplejidad());
            preparedStatement.setString(3, catalogo_ejercicio_dtm.getGrupoMuscular());
            preparedStatement.execute();
            return "Agregado con éxito.";
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

    public String actualizarEjercicio(Catalogo_Ejercicio_DTM catalogo_ejercicio_dtm) {
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "UPDATE CATALOGO_EJERCICIOS SET NOMBREEJERCICIO = ?, COMPLEJIDAD = ?, GRUPO_MUSCULAR = ? WHERE IDEJERCICIO = ?";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, catalogo_ejercicio_dtm.getNombreEjercicio());
            preparedStatement.setString(2, catalogo_ejercicio_dtm.getComplejidad());
            preparedStatement.setString(3, catalogo_ejercicio_dtm.getGrupoMuscular());
            preparedStatement.setInt(4, catalogo_ejercicio_dtm.getIdEjercicio());
            preparedStatement.execute();
            return "Actualizado con éxito.";
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

    public List<Clasificacion_WOD_DTM> consultaGruposMusculares() {
        List<Clasificacion_WOD_DTM> data;
        ConnectionUtil newCon = new ConnectionUtil();
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = newCon.conDB().createStatement().executeQuery("SELECT DISTINCT GRUPO_MUSCULAR FROM CLASIFICACION_WOD");

            while (rs.next()) {
                Clasificacion_WOD_DTM fila = new Clasificacion_WOD_DTM();
                fila.setGrupoMuscular(rs.getString("GRUPO_MUSCULAR"));
                data.add(fila);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return data;
    }

    public ObservableList consultaEjercicios(String gpoMuscular) {
        ObservableList<Catalogo_Ejercicio_DTM> data;
        ConnectionUtil newCon = new ConnectionUtil();
        data = FXCollections.observableArrayList();
        ResultSet rs;

        try {
            PreparedStatement ps;
            if (gpoMuscular.equals("Todos")) {
                ps = newCon.conDB().prepareStatement("SELECT IDEJERCICIO, NOMBREEJERCICIO, COMPLEJIDAD, GRUPO_MUSCULAR FROM CATALOGO_EJERCICIOS");
            } else {
                ps = newCon.conDB().prepareStatement("SELECT IDEJERCICIO, NOMBREEJERCICIO, COMPLEJIDAD, GRUPO_MUSCULAR FROM CATALOGO_EJERCICIOS WHERE GRUPO_MUSCULAR = ?");
                ps.setString(1, gpoMuscular);
            }
            rs = ps.executeQuery();
            while (rs.next()) {
                Catalogo_Ejercicio_DTM fila = new Catalogo_Ejercicio_DTM();
                fila.setIdEjercicio(rs.getInt("IDEJERCICIO"));
                fila.setNombreEjercicio(rs.getString("NOMBREEJERCICIO"));
                fila.setComplejidad(rs.getString("COMPLEJIDAD"));
                fila.setGrupoMuscular(rs.getString("GRUPO_MUSCULAR"));
                data.add(fila);
            }
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
        return data;
    }

    public Set<DateEvent> consultaWOD() {
        ConnectionUtil newCon = new ConnectionUtil();
        Set<DateEvent> events = new HashSet<>();
        //result = statement.executeQuery(query);

        LocalDateTime fecha_set;
        String set_nombre; // nombreCompleto;
        int idSet;

        ResultSet rs = null;
        try {
            rs = newCon.conDB().createStatement().executeQuery("SELECT IDSET, SET_NOMBRE, FECHA_SET FROM SET_EJERCICIO");

            while (rs.next()) {
                idSet = rs.getInt("IDSET");
                fecha_set = rs.getTimestamp("FECHA_SET").toLocalDateTime();
                //notify = rs.getTimestamp("NOTIFY_TIME").toLocalDateTime();
                set_nombre = rs.getString("SET_NOMBRE");
                //nombreCompleto = rs.getString("MIEMBRO");
                events.add(new DateEvent(idSet, fecha_set, set_nombre));
            }

        } catch (SQLException ex) {

        }
        return events;
    }

//    public String createInsertQuery(Set<RutinaDTM> in) {
//        Timestamp tsDateTime;
//        Timestamp tsNotify;
//        String resultado = null;
//        for (RutinaDTM e : in) {
//            tsDateTime = Timestamp.valueOf(e.getDateTime());
//            tsNotify = Timestamp.valueOf(e.getNotifyTime());
//            ConnectionUtil newCon = new ConnectionUtil();
//            java.util.Date utilDate = new java.util.Date();
//            java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
//            java.sql.Date fecha_wod = new java.sql.Date(utilDate.getTime());
//            PreparedStatement preparedStatement;
//            try {
//                String queryInsert = "INSERT INTO WOD_POR_DIA ( NOMBRE_EJERCICIO, TIPO, REPS, TIEMPO, FECHA_WOD, NOMBRE_SET) VALUES (?,?,?,?,?,?)";
//                preparedStatement = newCon.conDB().prepareStatement(queryInsert);
//                preparedStatement.setString(1, e.getNombre_ejercicio());
//                preparedStatement.setString(2, e.getTipo());
//                preparedStatement.setString(3, e.getReps());
//                preparedStatement.setString(4, e.getTiempo());
//                preparedStatement.setDate(5, fecha_wod);
//                preparedStatement.setString(6, e.getNombre_set());
//
//                preparedStatement.execute();
//                resultado = "Evento agregado con éxito.";
//            } catch (SQLException ex) {
//                System.out.println(ex.getMessage());
//                resultado = "Ocurrió un error, contacte al administrador.";
//            } finally {
//                try {
//                    newCon.conDB().close();
//                } catch (SQLException ex) {
//                    ex.printStackTrace();
//                }
//            }
//        }
//        return resultado;
//    }

    public String guardaSetEjercicios(List<RutinaDTM> listaEjercicios, LocalDate eventDate) {
        String resultado = null;
        boolean flagInsertSet = true;
        for (RutinaDTM ejercicio : listaEjercicios) {
            ConnectionUtil newCon = new ConnectionUtil();
            PreparedStatement preparedStatement;
            try {
                if(flagInsertSet){
                    String queryInsert = "INSERT INTO SET_EJERCICIO ( SET_NOMBRE, FECHA_SET, TIEMPO_SET, TIPO) VALUES (?,?,?,?)";
                    preparedStatement = newCon.conDB().prepareStatement(queryInsert);
                    preparedStatement.setString(1, ejercicio.getNombre_set());
                    preparedStatement.setString(2, String.valueOf(eventDate));
                    preparedStatement.setString(3, ejercicio.getTiempo_por_set());
                    preparedStatement.setString(4, ejercicio.getTipo());
                    preparedStatement.execute();
                    resultado = "Set agregado con éxito.";
                }
                String queryInsert2 = "INSERT INTO WOD_POR_DIA ( NOMBRE_EJERCICIO, TIPO, REPS, TIEMPO, NOMBRE_SET) VALUES (?,?,?,?,?)";
                preparedStatement = newCon.conDB().prepareStatement(queryInsert2);
                preparedStatement.setString(1, ejercicio.getNombre_ejercicio());
                preparedStatement.setString(2, ejercicio.getTipo());
                preparedStatement.setString(3, ejercicio.getReps());
                preparedStatement.setString(4, ejercicio.getTiempo());
                preparedStatement.setString(5, ejercicio.getNombre_set());
                preparedStatement.execute();
                flagInsertSet = false;
                resultado = "Set agregado con éxito.";
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } finally {
                try {
                    newCon.conDB().close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                    resultado = "Ocurrió un error, contacte al administrador.";
                }
            }
        }
        return resultado;
    }

    public ObservableList consultaWODporFecha(LocalDate fecha) {
        ObservableList<RutinaDTM> data = FXCollections.observableArrayList();
        ConnectionUtil newCon = new ConnectionUtil();
        String set_nombre;
        int idSet;
        PreparedStatement preparedStatement;

        ResultSet rs = null;
        try {
            String queryInsert = "SELECT SET_NOMBRE FROM SET_EJERCICIO WHERE FECHA_SET = ?";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setDate(1, Date.valueOf(fecha));
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                RutinaDTM set = new RutinaDTM();
                set.setNombre_set(rs.getString("SET_NOMBRE"));
                data.add(set);
            }
            return data;
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

    public ObservableList consultaDetallesSetPorFecha(RutinaDTM objRutina, LocalDate fecha) {
        ObservableList<Catalogo_Ejercicio_DTM> data = FXCollections.observableArrayList();
        ConnectionUtil newCon = new ConnectionUtil();
        PreparedStatement preparedStatement;
        ResultSet rs;
        try {
            String queryInsert = "SELECT NOMBRE_EJERCICIO, TIPO, REPS, TIEMPO FROM WOD_POR_DIA WHERE NOMBRE_SET = ?";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, objRutina.getNombre_set());
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                Catalogo_Ejercicio_DTM set = new Catalogo_Ejercicio_DTM();
                set.setNombreEjercicio(rs.getString("NOMBRE_EJERCICIO"));
                set.setReps(rs.getString("REPS"));
                set.setTiempo(rs.getString("TIEMPO"));
                data.add(set);
            }
        return data;
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

    public RutinaDTM consultaSetPorFecha(LocalDate fecha, String nombreRutina) {
        RutinaDTM set = new RutinaDTM();
        ConnectionUtil newCon = new ConnectionUtil();
        String set_nombre;
        int idSet;
        PreparedStatement preparedStatement;
        ResultSet rs = null;
        try {
            String queryInsert = "SELECT SET_NOMBRE, FECHA_SET, TIEMPO_SET, TIPO FROM SET_EJERCICIO WHERE  FECHA_SET = ? AND SET_NOMBRE = ?";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setDate(1, Date.valueOf(fecha));
            preparedStatement.setString(2, nombreRutina);
            rs = preparedStatement.executeQuery();
            while (rs.next()) {
                set.setNombre_set(rs.getString("SET_NOMBRE"));
                set.setTiempo_por_set(rs.getString("TIEMPO_SET"));
                set.setTipo(rs.getString("TIPO"));
            }
            return set;
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

    public String actualizarSetPorDia(String nombreRutina, List<RutinaDTM> listaEjercicios, LocalDate eventDate){
        String resultado =  null;
        ConnectionUtil newCon = new ConnectionUtil();
        java.util.Date utilDate = new java.util.Date();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        PreparedStatement preparedStatement;
        try {
            String queryInsert = "DELETE FROM WOD_POR_DIA WHERE NOMBRE_SET = ? ";
            preparedStatement = newCon.conDB().prepareStatement(queryInsert);
            preparedStatement.setString(1, nombreRutina);
            preparedStatement.execute();

            guardaSetEjercicios(listaEjercicios, eventDate);

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
}
