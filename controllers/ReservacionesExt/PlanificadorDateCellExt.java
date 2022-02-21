package controllers.ReservacionesExt;

import controllers.PlanificadorController;
import javafx.scene.control.DateCell;
import java.time.LocalDate;

public class PlanificadorDateCellExt extends DateCell{

    private final PlanificadorController cc;

    public PlanificadorDateCellExt(PlanificadorController cc) {
        super();
        this.cc = cc;
        initalize();
    }

    /**
     * Sets view preferences
     */
    private void initalize() {
        //setPrefHeight(200);
        //getStylesheets().add(getClass().getResource("../styling/DateCellExt.css").toExternalForm());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void updateItem(LocalDate item, boolean empty) {
        super.updateItem(item, empty);
        cc.updateDateCell(this, item);
    }
}