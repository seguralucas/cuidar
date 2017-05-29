package so.cuidar;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import so.cuidar.entidades.MensajeChat;
import so.cuidar.entidades.User;

public class Chatctivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refChat = ref.child(User.comunidad).child("chat");
    TextView lblChatConversacion;
    TextView textChatMensaje;
    ScrollView scrollChat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatctivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        this.textChatMensaje =(TextView) findViewById(R.id.textChat);
        this.lblChatConversacion=(TextView) findViewById(R.id.lblChat);
        this.scrollChat=((ScrollView) findViewById(R.id.scrollChat));
        scrollChat.fullScroll(ScrollView.FOCUS_DOWN);


    }

    @Override
    protected void onStart() {
        super.onStart();
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
                scrollChat.fullScroll(ScrollView.FOCUS_DOWN);
                scrollChat.fullScroll(ScrollView.FOCUS_DOWN);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        scrollChat.fullScroll(ScrollView.FOCUS_DOWN);

    }
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chatctivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void enviarChat(View view) {
        DatabaseReference postChat = refChat.push();
        MensajeChat mensajeChat= new MensajeChat(User.nombre,this.textChatMensaje.getText().toString());
        if(mensajeChat.getMensaje().length()>1000){
            Toast.makeText(this, "Has superado los mil caracteres de mensaje. Envío cancelado",
                    Toast.LENGTH_LONG).show();            return;
        }
        this.textChatMensaje.setText("");
        postChat.setValue(mensajeChat);
    }
}
