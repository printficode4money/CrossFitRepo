package controllers.ReservacionesExt;

import controllers.ReservacionesController;
import javafx.scene.control.DateCell;

import java.time.LocalDate;

/**
 * Created on 5/20/2017.
 */
public class DateCellExt extends DateCell {

    private final ReservacionesController cc;

    public DateCellExt(ReservacionesController cc) {
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
