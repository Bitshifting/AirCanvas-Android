package bitshifting.aircanvas;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;

import java.util.ArrayList;
import java.util.List;

import bitshifting.aircanvas.Graphics.Entities.BrushStroke;
import bitshifting.aircanvas.Graphics.Entities.Canvas;
import bitshifting.aircanvas.Graphics.Entities.Point;


public class MainActivity extends CardboardActivity {

    private static final String TAG = "MainActivity";

    private static String FBUrl = "https://poopersmcpop.firebaseio.com/Hello";

    private String UserID;

    //set renderer to the main renderer class
    MainRenderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.setAndroidContext(this.getApplicationContext());

        //get google cardboard
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);

        //set renderer
        renderer = new MainRenderer(getApplicationContext(), cardboardView);
        cardboardView.setRenderer(renderer);
        setCardboardView(cardboardView);



        UserID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);



        //testFireBase(firebase);


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

    private void setUpListener(final Firebase firebase) {
        // set up event listener
        firebase.getRoot().child("message").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue().equals("Test Message")) {
                    Toast.makeText(getApplicationContext(), "Firebase connected! - " + (String) dataSnapshot.getValue(), Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getApplicationContext(), "Firebase failed to connect!", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Firebase Error.", Toast.LENGTH_SHORT).show();
            }
        });

        firebase.child("Hello").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(getApplicationContext(), "Firebase data! - ", Toast.LENGTH_LONG);
                firebase.push();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Toast.makeText(getApplicationContext(), "Firebase fail!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void addCanvas() {
        Canvas canvas = new Canvas(UserID);
        BrushStroke stroke1 = new BrushStroke(UserID, 1);
        BrushStroke stroke2 = new BrushStroke(UserID, 2);

        List<Point> points = new ArrayList<Point>();
        points.add(new Point(1, 1));
        points.add(new Point(1, 2));
        points.add(new Point(2, 2));

        stroke1.setPoints(points);

        points = new ArrayList<Point>();

        points.add(new Point(4, 5));
        points.add(new Point(4,4));

        stroke2.setPoints(points);
        canvas.addBrushStroke(stroke1);
        canvas.addBrushStroke(stroke2);
    }

    private void testFireBase(Firebase firebase) {
        setUpListener(firebase);
        firebase.child("Hello").setValue("WHAT'S UP");
    }
}
