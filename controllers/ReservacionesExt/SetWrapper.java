package controllers.ReservacionesExt;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Set;

/**
 * Created on 6/1/2017.
 * Wrapper for Set of DateEvents used for XML JAXB marshalling.
 */
@XmlRootElement(name = "set")
@XmlAccessorType(XmlAccessType.FIELD)
public class SetWrapper {

    @XmlElement(name = "dateEvent")
    protected Set<DateEvent> setCol = null;

    public Set<DateEvent> getSetCol() {
        return setCol;
    }

    public void setSetCol(Set<DateEvent> setCol) {
        this.setCol = setCol;
    }
}
