package so.cuidar.entidades;

/**
 * Created by GAS on 20/05/2017.
 */
public class Comunidad {

    private Chat chat;
    private NotificacionAlarmaPanico notificacionAlarmaPanico;
    private String nombre;

    public Comunidad(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        this.chat = chat;
    }

    public NotificacionAlarmaPanico getNotificacionAlarmaPanico() {
        return notificacionAlarmaPanico;
    }

    public void setNotificacionAlarmaPanico(NotificacionAlarmaPanico notificacionAlarmaPanico) {
        this.notificacionAlarmaPanico = notificacionAlarmaPanico;
    }
}
