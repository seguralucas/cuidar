package so.cuidar;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
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
import java.util.List;
import java.util.Locale;

import so.cuidar.entidades.User;
import so.cuidar.manejadores.GpsService;
import so.cuidar.manejadores.Session;


public class NotificacionActivity extends AppCompatActivity {

    User user;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refUltimaNotificacion;
    TextView notificacion;
    GpsService gps;
    Session session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session= new Session(getApplicationContext());
        user=session.getUser();
        if(!session.isLogIn())
            finish();
        refUltimaNotificacion=ref.child(user.getComunidad()).child("notificacionAlarmaPanico").child("cadena");
        gps= new GpsService((LocationManager) getSystemService(LOCATION_SERVICE),this);
        location = gps.getLocation();
        setContentView(R.layout.activity_notificacion);
        this.notificacion=(TextView) findViewById(R.id.lblUltimaNotificacion);
        FirebaseMessaging.getInstance().subscribeToTopic(user.getComunidad());
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


    }
/**********************************************************/
/*Botones*/
/*********************************************************/
    private Location location;

    public void botonDePanico(View view){
        LocationListener listener;
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
    //    final String comunidad=((TextView) findViewById(R.id.textComunidad)).getText().toString();
        String url=null;
        String direccion=null;
        if (!(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            location = gps.getLocation();
            double longitude = location.getLongitude();
            double latitude = location.getLatitude();
            Geocoder geocoder;
            List<Address> addresses;
            geocoder = new Geocoder(this, Locale.getDefault());
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                direccion= " (Direccion: "+address+" Ciudad: "+city+")";
            }
            catch(Exception e){
                direccion="( Direccion no encontrada)";
            }
            url = "<a href=\"https://www.google.com/maps?q=" + latitude + "+" + longitude + "\">Abrir ubicacion</a>";
        }
        else {
            url = "No se pudo obtener la ubicacion";
            direccion="( Direccion no encontrada)";
        }
        refUltimaNotificacion.setValue("<html><body>(ICONO)"+dateFormat.format(date)+ " <br/>Usuario "+ user.getUsuario() +"<br/>Comunidad: "+user.getComunidad()+".<br/> "+url+" "+direccion+"</body></html>");

/*		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);*/

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                RealizadorPeticion r= new RealizadorPeticion();
                r.doInBackground(user.getComunidad(), user.getUsuario());
            }
        });
        t.start();


    }

    public void mostrarChat(View view){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

    public void logOut(View view){
        session.deleteSession();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }


}
