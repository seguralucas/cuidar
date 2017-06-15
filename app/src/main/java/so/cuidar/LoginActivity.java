package so.cuidar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;

import so.cuidar.entidades.Comunidad;
import so.cuidar.entidades.NotificacionAlarmaPanico;
import so.cuidar.entidades.User;
import so.cuidar.manejadores.Session;

public class LoginActivity extends AppCompatActivity {
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session= new Session(getApplicationContext());
        setContentView(R.layout.activity_main);
        System.out.println("Token: "+ FirebaseInstanceId.getInstance().getToken());
        if(session.isLogIn())
            abrirNotificaciones();


/*        Chat c= new Chat("Lucas");
        test.put("Chat",c);
        test.put("Notificaciones","Aún no hay notificaciones");*/

    }
    /**********************************************************/
    /*Botones*/
    /*********************************************************/
    public void irAPrincipal(View view) {
        String userName=((TextView) findViewById(R.id.textUsuario)).getText().toString();
        String comunidad=((TextView) findViewById(R.id.textComunidad)).getText().toString();
        String password=((TextView) findViewById(R.id.textPassword)).getText().toString();
        session.setUser(userName,comunidad,password);
        if(!session.isLogIn()){
            Toast.makeText(this, "Constraseña incorrecta",
                    Toast.LENGTH_LONG).show();
        }
        inicializarBaseComunidad();
    }

    private Activity thisAtributo=this;
    private void inicializarBaseComunidad(){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User user= session.getUser();
                            if (!dataSnapshot.hasChild(user.getComunidad())) {
                                HashMap<String, Object> comunidadMapa = new HashMap<String, Object>();/*No es necesario usar hashmap pero lo utilizo para recodar que se puede usar*/

                                Comunidad comunidad = new Comunidad(user.getComunidad());
                                comunidad.setNotificacionAlarmaPanico(new NotificacionAlarmaPanico("<html><body>No hay notificaciones aun</body></html>"));
                                Map<String, Object> userMap = new ObjectMapper().convertValue(comunidad, Map.class);
                                comunidadMapa.put(comunidad.getNombre(), userMap);
                                ref.updateChildren(comunidadMapa);
                            }
                        System.out.println("Abriendo notificaciones... ");
                        abrirNotificaciones();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Error");
                    }
                });
    }
/**********************************************************/
/*Otras funciones*/
    /*********************************************************/

    private void abrirNotificaciones(){
        Intent intent = new Intent(thisAtributo, NotificacionActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
