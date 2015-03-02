package bitshifting.aircanvas.Graphics.Managers;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import bitshifting.aircanvas.Graphics.Entities.Path;
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

    public static PathManager pathManager;

    int currStrokeInd;

    float[] currColor;

    Firebase current;

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

        pathManager = new PathManager();
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

    public void sendPath (Path path) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("Color", path.color);
        hashMap.put("Values", path.points);

        current = ref.push();
        current.setValue(hashMap);
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

                if(current != null && dataSnapshot.getKey().contains(current.getKey())) {
                    return;
                }

                HashMap<String, Object> map = (HashMap<String, Object>)dataSnapshot.getValue();


                List<Double> colorArr = (ArrayList<Double>)map.get("Color");
                float[] color = new float[3];

                if(colorArr == null) {
                    return;
                }

                for(int i = 0; i < 3; i++) {
                    color[i] = colorArr.get(i).floatValue();
                }

                //pM.listOfPaths.get(id).color = color;

                List<ArrayList<Double>> posArr = (ArrayList<ArrayList<Double>>) map.get("Values");

                if(posArr == null) {
                    return;
                }

                //make a path
                Path path = new Path(color, ShaderManager.getInstance().getShader("NoLightVBO"));

                for(int i = 0; i < posArr.size(); i++) {
                    float[] pos = new float[3];
                    for(int j = 0; j < 3; j++) {
                        pos[j] = (posArr.get(i)).get(j).floatValue();
                    }

                    path.update(pos);
                }

                pathManager.lock.lock();
                pathManager.listOfPaths.put(dataSnapshot.getKey(), path);
                pathManager.lock.unlock();

            }
        });
    }
}
