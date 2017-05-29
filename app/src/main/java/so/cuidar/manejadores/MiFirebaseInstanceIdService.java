package so.cuidar.manejadores;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessagingService;

/**
 * Created by GAS on 16/05/2017.
 */
public class MiFirebaseInstanceIdService extends FirebaseInstanceIdService{
    public static final String TAG="TOKEN";
    @Override
    public void onTokenRefresh() {
        String token= FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG,"Tokens: "+token);
        System.out.println("Tokens: "+token);
    }
}
