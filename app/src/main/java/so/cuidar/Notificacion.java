package so.cuidar;

import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import so.cuidar.entidades.MensajeChat;
import so.cuidar.entidades.User;


public class Notificacion extends AppCompatActivity {

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refUltimaNotificacion = ref.child(User.comunidad).child("notificacionAlarmaPanico").child("cadena");
    DatabaseReference refChat = ref.child(User.comunidad).child("chat");
    TextView notificacion;
    TextView textChatMensaje;
    TextView lblChatConversacion;
    GpsService gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacion);
        this.notificacion=(TextView) findViewById(R.id.lblUltimaNotificacion);
        this.textChatMensaje =(TextView) findViewById(R.id.textChat);
        this.lblChatConversacion=(TextView) findViewById(R.id.lblChat);
        FirebaseMessaging.getInstance().subscribeToTopic(User.comunidad);
        gps= new GpsService((LocationManager) getSystemService(LOCATION_SERVICE),this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        refUltimaNotificacion.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                notificacion.setText(Html.fromHtml(value));
                System.out.println(value);
                notificacion.setMovementMethod(LinkMovementMethod.getInstance());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        refChat.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                StringBuilder sb= new StringBuilder();
                sb.append("<html><body>");
                for (DataSnapshot child : dataSnapshot.getChildren()) {
                    //No se porqué no funciona así
                    /*MensajeChat mensajeChat=dataSnapshot.getValue(MensajeChat.class);
                    System.out.println("Key: "+child.getKey() +"Emisor: "+mensajeChat.getEmisor()+" mensaje: "+ mensajeChat.getMensaje());*/
                    MensajeChat mensajeChat= new MensajeChat(child.child("emisor").getValue(String.class),child.child("mensaje").getValue(String.class));
                    sb.append("<b>"+mensajeChat.getEmisor()+"</b>: "+ mensajeChat.getMensaje()+"<br/>");
                }
                sb.append("</body></html>");
                lblChatConversacion.setText(Html.fromHtml(sb.toString()));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void enviarChat(View view) {
        DatabaseReference postChat = refChat.push();
        MensajeChat mensajeChat= new MensajeChat(User.nombre,this.textChatMensaje.getText().toString());
        this.textChatMensaje.setText("");
        postChat.setValue(mensajeChat);
    }

    private Location location;

    public void botonDePanico(View view){
        LocationManager locationManager;
        LocationListener listener;

        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
    //    final String comunidad=((TextView) findViewById(R.id.textComunidad)).getText().toString();
        final String comunidad= User.comunidad;
        String url=null;
        System.out.println("Antes de verificar");
        if (!(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            System.out.println("Verifico correcamente");
            location = gps.getLocation();
            /*location= locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                LocationListener ls= new LocationListener() {
                    @Override
                    public void onLocationChanged(Location newLocation) {
                        location=newLocation;
                        ((TextView) findViewById(R.id.textAct)).setText("Actualizado");
                    }
                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {}
                    @Override
                    public void onProviderEnabled(String provider) {}
                    @Override
                    public void onProviderDisabled(String provider) {}
                };*/


            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            System.out.println("Latitud y longitud" + latitude + " " + longitude);
            url = "<a href=\"https://www.google.com/maps?q=" + latitude + "+" + longitude + "\">Abrir ubicacion</a>";
        }
        else
            url= "No se pudo obtener la ubicacion";

        refUltimaNotificacion.setValue("<html><body>Alarma de panico presionada en fecha y hora: "+dateFormat.format(date)+ " por el usuario "+ User.apellido+", "+User.nombre+ " de la comunidad: "+comunidad+". "+url+"</body></html>");

/*		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);*/

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                RealizadorPeticion r= new RealizadorPeticion();
                r.doInBackground(comunidad);
            }
        });
        t.start();


    }

}
