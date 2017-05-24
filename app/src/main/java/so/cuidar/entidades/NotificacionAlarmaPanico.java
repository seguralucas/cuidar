package so.cuidar.entidades;

/**
 * Created by GAS on 20/05/2017.
 */
public class NotificacionAlarmaPanico {
    private String cadena;

    public NotificacionAlarmaPanico(String cadena) {
        this.cadena = cadena;
    }

    public String getCadena() {
        return cadena;
    }

    public void setCadena(String cadena) {
        this.cadena = cadena;
    }
}
