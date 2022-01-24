package persistence;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.Catalogo_Ejercicio_DTM;
import models.Clasificacion_WOD_DTM;
import utils.ConnectionUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
            if(gpoMuscular.equals("Todos")){
                ps = newCon.conDB().prepareStatement("SELECT IDEJERCICIO, NOMBREEJERCICIO, COMPLEJIDAD, GRUPO_MUSCULAR FROM CATALOGO_EJERCICIOS");
            }else {
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
}
