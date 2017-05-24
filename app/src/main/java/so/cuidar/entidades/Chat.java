package so.cuidar.entidades;

import java.util.ArrayList;

/**
 * Created by GAS on 20/05/2017.
 */
public class Chat {
    private String name;
    private ArrayList<MensajeChat> mensajes;

    public Chat(String name) {
        this.name = name;
        this.mensajes = new ArrayList<MensajeChat>();

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<MensajeChat> getMensajes() {
        return mensajes;
    }

    public void setMensajes(ArrayList<MensajeChat> mensajes) {
        this.mensajes = mensajes;
    }
}
