package so.cuidar.entidades;

/**
 * Created by GAS on 16/05/2017.
 */
public class User {
    private String usuario;
    private String comunidad;
    private static User instance;

    public static synchronized User getInstance(){
        if(instance==null)
            instance=new User();
        return instance;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getComunidad() {
        return comunidad;
    }

    public void setComunidad(String comunidad) {
        this.comunidad = comunidad;
    }
}
