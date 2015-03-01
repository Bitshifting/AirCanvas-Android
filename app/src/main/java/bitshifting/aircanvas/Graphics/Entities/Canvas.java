package bitshifting.aircanvas.Graphics.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ricardolopez on 2/28/15.
 */
public class Canvas {
    private String OwnerID;
    private Map<Integer, BrushStroke> brushStrokes;

    public Canvas(String ownerID) {
        this.OwnerID = ownerID;
        this.brushStrokes = new HashMap<Integer, BrushStroke>();
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public Map<Integer, BrushStroke> getBrushStrokes() {
        return brushStrokes;
    }
}
