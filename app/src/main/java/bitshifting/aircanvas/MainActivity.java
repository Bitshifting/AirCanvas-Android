package bitshifting.aircanvas;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import bitshifting.aircanvas.Graphics.Managers.CanvasManager;


public class MainActivity extends CardboardActivity {

    private static final String TAG = "MainActivity";

    private static String FBUrl = "https://aircanvasfb.firebaseio.com/";

    private String UserID;

    //set renderer to the main renderer class
    MainRenderer renderer;
    Firebase firebaseRef;
    CanvasManager canvasManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this.getApplicationContext());
        firebaseRef = new Firebase(FBUrl);
        firebaseRef.removeValue();
        UserID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        // set up the canvas manager
        canvasManager = new CanvasManager(firebaseRef, UserID);

        //get google cardboard
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        //set renderer
        renderer = new MainRenderer(getApplicationContext(), cardboardView, canvasManager);
        cardboardView.setRenderer(renderer);
        setCardboardView(cardboardView);
    }

    //called when button is pressed
    @Override
    public void onCardboardTrigger() {
        renderer.onCardboardTrigger();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    /** Check if this device has a camera */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            Toast.makeText(context, "Camera detected!", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(context, "You need a camera!", Toast.LENGTH_SHORT).show();
        return false;
    }
}
