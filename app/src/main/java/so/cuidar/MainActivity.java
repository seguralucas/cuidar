package so.cuidar;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

import so.cuidar.entidades.Chat;
import so.cuidar.entidades.Comunidad;
import so.cuidar.entidades.NotificacionAlarmaPanico;
import so.cuidar.entidades.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Token: "+ FirebaseInstanceId.getInstance().getToken());



/*        Chat c= new Chat("Lucas");
        test.put("Chat",c);
        test.put("Notificaciones","Aún no hay notificaciones");*/

    }

    public void irAPrincipal(View view) {
        User.nombre=((TextView) findViewById(R.id.textNombre)).getText().toString();
        User.apellido=((TextView) findViewById(R.id.textApellido)).getText().toString();
        User.comunidad=((TextView) findViewById(R.id.textComunidad)).getText().toString();
        inicializarBaseComunidad();
    }

    private Activity thisAtributo=this;
    private void inicializarBaseComunidad(){
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference();

        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.hasChild(User.comunidad)) {
                                HashMap<String, Object> comunidadMapa = new HashMap<String, Object>();/*No es necesario usar hashmap pero lo utilizo para recodar que se puede usar*/

                                Comunidad comunidad = new Comunidad(User.comunidad);
                                comunidad.setNotificacionAlarmaPanico(new NotificacionAlarmaPanico("<html><body>No hay notificaciones aun</body></html>"));
                                Map<String, Object> userMap = new ObjectMapper().convertValue(comunidad, Map.class);
                                comunidadMapa.put(comunidad.getNombre(), userMap);
                                ref.updateChildren(comunidadMapa);
                                System.out.println("Abriendo notificaciones... ");
                            }
                        Intent intent = new Intent(thisAtributo, Notificacion.class);
                        startActivity(intent);
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        System.out.println("Error");
                    }
                });
    }
}
