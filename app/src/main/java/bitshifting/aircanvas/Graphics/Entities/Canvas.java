package bitshifting.aircanvas.Graphics.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardolopez on 2/28/15.
 */
public class Canvas {
    private String OwnerID;
    private List<BrushStroke> brushStrokes;

    public Canvas(String ownerID) {
        this.OwnerID = ownerID;
        this.brushStrokes = new ArrayList<BrushStroke>();
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public List<BrushStroke> getBrushStrokes() {
        return brushStrokes;
    }
}
