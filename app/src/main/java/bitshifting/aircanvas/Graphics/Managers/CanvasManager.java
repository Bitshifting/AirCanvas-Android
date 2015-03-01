package bitshifting.aircanvas.Graphics.Managers;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.List;

import bitshifting.aircanvas.Graphics.Entities.BrushStroke;
import bitshifting.aircanvas.Graphics.Entities.Canvas;
import bitshifting.aircanvas.Graphics.Entities.Point;

/**
 * Created by ricardolopez on 2/28/15.
 */
public class CanvasManager {
    public static final String TAG = "CanvasManager";

    private Canvas canvas;
    private String CanvasID;
    private String OwnerID;
    private Firebase canvasesRef;
    private Firebase canvasRef;

    private int color;

    private String currentStroke;

    private ChildEventListener currListener;

    /**
     * Construct a manager without a canvasID (i.e. generate a new canvas)
     *
     * @param ref
     * @param ownerID
     */
    public CanvasManager(Firebase ref, String ownerID) {
        this(ref, ownerID, null);
    }

    /**
     * Construct a manager with a canvasID in order to connect to an existing
     * canvas
     *
     * @param ref
     * @param ownerID
     * @param canvasID
     */
    public CanvasManager(Firebase ref, String ownerID, String canvasID) {
        this.OwnerID = ownerID;
        this.canvasesRef = ref.child("canvases");

        // set up the CanvasID
        this.CanvasID = (canvasID == null) ? generateCanvas(ownerID) : canvasID;
        // connect to the canvas
        this.canvasRef = canvasesRef.child(CanvasID);
    }

    /**
     * Get the current canvasID
     *
     * @return
     */
    public String getCanvasID() {
        return CanvasID;
    }

    /**
     * Add a new stroke given a list of points. This method handles creation of a new stroke
     * object and updates the canvas and firebase
     *
     * @param color
     */
    public void addBrushStroke(float[] color) {
        int idx = canvas.getBrushStrokes().size();
        Firebase holder = canvasRef.push();
        canvas.getBrushStrokes().put(idx, new BrushStroke(OwnerID, color));
        // update Firebase
        holder.setValue(canvas);
        updateEventListener(currentStroke, holder.getKey());
        currentStroke = holder.getKey();
    }

    /**
     *
     * @param point
     */
    public void addPointToStroke(float[] point) {
        int idx = canvas.getBrushStrokes().size();
        canvas.getBrushStrokes().get(idx).getPoints().add(point);
        canvasRef.child("brushStrokes").child(currentStroke).push().setValue(point);
    }

    /**
     *
     * @param oldStroke
     * @param newStroke
     */
    public void updateEventListener(String oldStroke, String newStroke)  {
        if (currListener != null) {
            canvasRef.child("brushStrokes").child(oldStroke).removeEventListener(currListener);
        }

        currListener = canvasRef.child("brushStroke").child(newStroke).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e(TAG, dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, "fb error");

            }
        });
    }

    /**
     * Set up the firebase event listeners to update the user's canvas
     *
     */
    private void setUpEventListeners() {
        if (canvasRef == null) {
            return;
        }

        // set up the current canvas brushStrokes listener
        canvasRef.child("brushStrokes").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, dataSnapshot.getValue().toString());
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Log.e(TAG, dataSnapshot.getValue().toString());

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                Log.e(TAG, dataSnapshot.getValue().toString());

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e(TAG, "fb error");

            }
        });
    }

    /**
     * Private function to encapsulate creation of a new canvas
     *
     * @param ownerID
     * @return
     */
    private String generateCanvas(String ownerID) {
        Firebase newPostRef = canvasesRef.push();

        canvas = new Canvas(ownerID);
        newPostRef.setValue(canvas);

        return newPostRef.getKey();
    }
}
