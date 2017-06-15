package so.cuidar;

import android.os.AsyncTask;

import com.google.firebase.iid.FirebaseInstanceId;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.entity.ByteArrayEntity;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import cz.msebera.android.httpclient.params.BasicHttpParams;
import cz.msebera.android.httpclient.params.HttpConnectionParams;
import cz.msebera.android.httpclient.params.HttpParams;
import so.cuidar.entidades.User;
import so.cuidar.manejadores.Session;

/**
 * Created by GAS on 17/05/2017.
 */
public class RealizadorPeticion extends AsyncTask<String, Void, HttpResponse> {


    @Override
    protected HttpResponse doInBackground(String... params) {
        String comunidad= params[0];
        String usuario= params[1];
        int TIMEOUT_MILLISEC = 10000;  // = 10 seconds
        String postMessage="{   \"to\": \"/topics/"+ comunidad +"\",   \"notification\": {      \"title\": \"Botón de pánico activado\",      \"body\": \"El usuario:"+ usuario +" ha presionado el boton de panico.\"   },   \"data\": {      \"titulo\": \"Este es el titular\",      \"descripcion\": \"Aquí estará todo el contenido de la noticia\"   } }"; //HERE_YOUR_POST_STRING.
        HttpParams httpParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT_MILLISEC);
        HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);
        HttpClient client = new DefaultHttpClient(httpParams);

        HttpPost request = new HttpPost("https://fcm.googleapis.com/fcm/send");
        try {
            request.setHeader("Content-Type", "application/json");
            request.setHeader("Authorization", "key=AAAAaEAZUUk:APA91bGpFA-Z-uZJvHWeCy1zlCe3UfmRI_gKvDPOLagcE0l4cVx8jM5W4K1UTDPkKhe3rWDGTJwIlQJ4eJLOiSqDsVB8ypQZGabZAwbPL2l-d9LmItQ_srNApq8lOMSJFMGXK-l9McC6");
            request.setEntity(new ByteArrayEntity(
                    postMessage.toString().getBytes("UTF8")));

            HttpResponse response = client.execute(request);
            String line="";
            String data="";
            try{
                BufferedReader br=new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                while((line=br.readLine())!=null){
                    System.out.println("line: "+line);
                }
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

        
        return null;
    }
}
