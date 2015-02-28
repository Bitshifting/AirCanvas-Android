package bitshifting.aircanvas;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;


public class MainActivity extends CardboardActivity {

    private static final String TAG = "MainActivity";

    //set renderer to the main renderer class
    CardboardCamera renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //get google cardboard
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);

        //set renderer
        renderer = new CardboardCamera(cardboardView);
        cardboardView.setRenderer(renderer);
        setCardboardView(cardboardView);

        if (!checkCameraHardware(getApplicationContext())) {
            return;
        }
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
