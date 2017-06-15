package so.cuidar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
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
import so.cuidar.manejadores.Session;

public class ChatActivity extends AppCompatActivity {

    User user;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    DatabaseReference refChat;
    TextView lblChatConversacion;
    TextView textChatMensaje;
    ScrollView scrollChat;
    Session session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        session= new Session(getApplicationContext());
        refChat=ref.child(session.getUser().getComunidad()).child("chat");
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

    public void enviarChat(View view) {
        DatabaseReference postChat = refChat.push();
        MensajeChat mensajeChat= new MensajeChat(session.getUser().getUsuario(),this.textChatMensaje.getText().toString());
        if(mensajeChat.getMensaje().length()>1000){
            Toast.makeText(this, "Has superado los mil caracteres de mensaje. Envío cancelado",
                    Toast.LENGTH_LONG).show();            return;
        }
        this.textChatMensaje.setText("");
        postChat.setValue(mensajeChat);
    }
}
