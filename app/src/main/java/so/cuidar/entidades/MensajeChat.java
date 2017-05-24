package so.cuidar.entidades;

/**
 * Created by GAS on 23/05/2017.
 */
public class MensajeChat {

    private String emisor;
    private String mensaje;
    public MensajeChat(){}
    public MensajeChat(String emisor, String mensaje) {
        this.emisor = emisor;
        this.mensaje = mensaje;
    }

    public String getEmisor() {
        return emisor;
    }

    public void setEmisor(String emisor) {
        this.emisor = emisor;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
