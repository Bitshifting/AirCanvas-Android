package bitshifting.aircanvas.Graphics.Managers;

import android.util.Log;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import bitshifting.aircanvas.Graphics.Entities.PathManager;

/**
 * Created by ricardolopez on 2/28/15.
 */
public class CanvasManager {
    public static final String TAG = "CanvasManager";

    private String OwnerID;

    //overarching reference
    private Firebase ref;

    //current owner
    private Firebase ownerRef;

    //current stroke we are on
    private Firebase currStroke;

    //current points of database
    private Firebase currPoints;

    //used to get the next point index
    int currPointInd;

    //used to get the current brush
    int currStrokeInd;

    //random when needed
    Random rand;

    public HashMap<String, PathManager> otherPathManagers;

    /**
     * Construct a manager without a canvasID (i.e. generate a new canvas)
     *
     * @param ref
     * @param ownerID
     */
    public CanvasManager(Firebase ref, String ownerID) {
        this.OwnerID = ownerID;
        ownerRef = ref.child(this.OwnerID);
        //remove the last value
        ownerRef.removeValue();
        currStrokeInd = 0;
        this.ref = ref;

        rand = new Random();

        addRefEventListener();
    }


    /**
     * Add a new stroke given a list of points. This method handles creation of a new stroke
     * object and updates the canvas and firebase
     *
     * @param color
     */
    public void addBrushStroke(float[] color) {
        // update Firebase
        currStroke = ownerRef.child("" + currStrokeInd);
        currStrokeInd++;

        currStroke.child("Color").setValue(color);

        currPoints = currStroke.child("Points");

        currPointInd = 0;
    }

    /**
     *
     * @param point
     */
    public void addPointToStroke(float[] point) {
        if (currPoints == null) {
            return;
        }

        currStroke.child("" + currPointInd).setValue(point);
        currPointInd++;
    }

    public void addRefEventListener() {

        ChildEventListener tempChildListener = new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                String hey = dataSnapshot.getKey();
                System.out.println("ASDFLKJ");
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                if (dataSnapshot != null) {

                    if (dataSnapshot.getValue() == null) {
                        return;
                    }

                    HashMap<String, Object> tempMap = (HashMap<String, Object>) dataSnapshot.getValue();

                    //check if it's own
                    if (dataSnapshot.getKey().equals(OwnerID)) {
                        return;
                    }

                    String newOwner = dataSnapshot.getKey();
                    //if never seen before, create new entry
                    if (!otherPathManagers.containsKey(newOwner)) {
                        otherPathManagers.put(newOwner, new PathManager());
                    }

                    //iterate through owner keys
                    for (int i = 0; i < tempMap.size(); i++) {


                        //iterate through brush
                        Map<String, Object> strokeNum = new HashMap<>();// = (Map<String, Object>) tempMap.get((Object)("ASDF"));

                        if (strokeNum == null) {
                            continue;
                        }

                        for (String nBrushNum : strokeNum.keySet()) {
                            //if never seen brush before add the brush to path managers
                            PathManager tempPathManage = otherPathManagers.get(newOwner);

                            Map<String, Object> colorValBranch = (Map<String, Object>) strokeNum.get(nBrushNum);

                            if (colorValBranch == null) {
                                continue;
                            }

                            for (String lastKey : colorValBranch.keySet()) {
                                int ind = Integer.parseInt(nBrushNum);
                                Map<Integer, Float> lastMap = (Map<Integer, Float>) colorValBranch.get(lastKey);
                                if (tempPathManage.listOfPaths.containsKey(ind)) {
                                    //check if color
                                    if (lastKey.equals("Color")) {
                                        float[] color = new float[3];
                                        color[0] = lastMap.get(0);
                                        color[1] = lastMap.get(1);
                                        color[2] = lastMap.get(2);

                                        tempPathManage.setColor(color, ind);
                                    } else {
                                        float[] newPos = new float[3];
                                        newPos[0] = lastMap.get(0);
                                        newPos[1] = lastMap.get(1);
                                        newPos[2] = lastMap.get(2);
                                        tempPathManage.update(newPos);
                                    }

                                } else {
                                    float[] color = new float[3];
                                    if (lastKey.equals("Color")) {
                                        color[0] = lastMap.get(0);
                                        color[1] = lastMap.get(1);
                                        color[2] = lastMap.get(2);
                                    } else {
                                        color[0] = rand.nextFloat();
                                        color[1] = rand.nextFloat();
                                        color[2] = rand.nextFloat();
                                    }

                                    tempPathManage.setDrawing(color, ind);
                                }
                            }

                        }

                    }
                }
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                Log.e("FAIL", "FAILURE TO READ");
            }
        };

        if(!this.OwnerID.equals("85b364c34013facc")) {
            ref.child("85b364c34013facc").addChildEventListener(tempChildListener);
        }

        if(!this.OwnerID.equals("7a4257a0d655f168")) {
            ref.child("7a4257a0d655f168").addChildEventListener(tempChildListener);
        }
    }
}
