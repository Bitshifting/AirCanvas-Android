package bitshifting.aircanvas.Graphics.Managers;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.List;

import bitshifting.aircanvas.Graphics.Entities.BrushStroke;
import bitshifting.aircanvas.Graphics.Entities.Canvas;
import bitshifting.aircanvas.Graphics.Entities.Point;

/**
 * Created by ricardolopez on 2/28/15.
 */
public class CanvasManager {
    private Canvas canvas;
    private String CanvasID;
    private String OwnerID;
    private Firebase canvasesRef;
    private Firebase canvasRef;

    private int color;

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
        this.CanvasID = (canvasID != null) ? generateCanvas(ownerID) : canvasID;
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
     * @param points
     */
    public void addBrushStroke(List<Point> points, float[] color) {
        int idx = canvas.getBrushStrokes().size();
        canvas.getBrushStrokes().put(idx, new BrushStroke(OwnerID, color, points));
        // update firebase
        canvasesRef.setValue(canvas);
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
        canvasRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
