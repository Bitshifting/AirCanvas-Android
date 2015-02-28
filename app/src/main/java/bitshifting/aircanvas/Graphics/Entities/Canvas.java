package bitshifting.aircanvas.Graphics.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardolopez on 2/28/15.
 */
public class Canvas {
    private String OwnerID;
    private String CanvasID;
    private List<BrushStroke> brushStrokes;

    public Canvas(String ownerID) {
        this.OwnerID = ownerID;
        this.CanvasID = genCanvasID();
        this.brushStrokes = new ArrayList<BrushStroke>();
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public String getCanvasID() {
        return CanvasID;
    }

    public void addBrushStroke(BrushStroke stroke) {
        brushStrokes.add(stroke);
    }

    private static String genCanvasID() {
        // TODO
        return "123231232isdfkj";
    }
}
