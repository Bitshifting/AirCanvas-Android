package bitshifting.aircanvas.Graphics.Managers;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import bitshifting.aircanvas.Graphics.Entities.PathManager;

/**
 * Created by ricardolopez on 2/28/15.
 */
public class CanvasManager {
    public static final String TAG = "CanvasManager";

    public static String OwnerID;

    //overarching reference
    private Firebase ref;

    //random when needed
    Random rand;

    public HashMap<String, PathManager> otherPathManagers;

    int currStrokeInd;

    float[] currColor;

    /**
     * Construct a manager without a canvasID (i.e. generate a new canvas)
     *
     * @param ref
     * @param ownerID
     */
    public CanvasManager(Firebase ref, String ownerID) {
        this.ref = ref;
        this.OwnerID = ownerID;

        rand = new Random();
        addRefEventListener();
        currStrokeInd = 0;
    }


    /**
     * Add a new stroke given a list of points. This method handles creation of a new stroke
     * object and updates the canvas and firebase
     *
     * @param color
     */
    public void addBrushStroke(float[] color) {
        currStrokeInd++;

        this.currColor = color;
    }

    /**
     *
     * @param point
     */
    public void addPointToStroke(float[] point) {
        if (point == null) {
            return;
        }

        HashMap<String, Object> objectToEnter = new HashMap<>();
        objectToEnter.put("Color", currColor);
        objectToEnter.put("Pos", point);
        String ID = OwnerID + currStrokeInd;

        //set as a reference
        ref.child(ID).setValue(objectToEnter);

    }

    public void addRefEventListener() {

        ref.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                //look at them
                //if not part of the child being added, set a listener
                addValue( dataSnapshot,  s);

            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                addValue( dataSnapshot,  s);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

            public void addValue(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().contains(OwnerID)) {
                    return;
                }

                //get other values from other people
                String id = dataSnapshot.getKey();
                if(!otherPathManagers.containsKey(id)) {
                    otherPathManagers.put(id, new PathManager());

                    float[] color = new float[3];
                    color[0] = rand.nextFloat();
                    color[1] = rand.nextFloat();
                    color[2] = rand.nextFloat();

                    otherPathManagers.get(id).setDrawing(color);
                }

                PathManager pM = otherPathManagers.get(id);

                HashMap<String, Object> map = (HashMap<String, Object>)dataSnapshot.getValue();


                List<Double> colorArr = (ArrayList<Double>)map.get("Color");
                float[] color = new float[3];

                for(int i = 0; i < 3; i++) {
                    color[i] = colorArr.get(i).floatValue();
                }

                //pM.listOfPaths.get(id).color = color;

                List<Double> posArr = (ArrayList<Double>) map.get("Pos");

                float[] pos = new float[3];

                for(int i = 0; i < 3; i++) {
                    pos[i] = posArr.get(i).floatValue();
                }

                pM.update(pos);

            }
        });
    }
}
