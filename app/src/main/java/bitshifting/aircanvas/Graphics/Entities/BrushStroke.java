package bitshifting.aircanvas.Graphics.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardolopez on 2/28/15.
 */
public class BrushStroke {
    private String OwnerID;
    private float[] color;
    private List<Point> points;

    public BrushStroke(String ownerID, float[] color) {
        this.OwnerID = ownerID;
        this.color = color;
        this.points = new ArrayList<Point>();
    }

    public BrushStroke(String ownerID, float[] color,  List<Point> points) {
        this.OwnerID = ownerID;
        this.color = color;
        this.points = points;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public float[] getColor() {
        return color;
    }

    public List<Point> getPoints() {
        return points;
    }
}
