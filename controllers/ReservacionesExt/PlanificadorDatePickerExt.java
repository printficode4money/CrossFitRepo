package controllers.ReservacionesExt;

import controllers.PlanificadorController;
import javafx.scene.control.DatePicker;

import java.time.LocalDate;

public class PlanificadorDatePickerExt extends DatePicker {
    public PlanificadorDatePickerExt(LocalDate date, PlanificadorController obj) {
        setDayCellFactory(param -> new PlanificadorDateCellExt(obj));
    }

}