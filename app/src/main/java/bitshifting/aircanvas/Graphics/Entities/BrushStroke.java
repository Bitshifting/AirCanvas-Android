package bitshifting.aircanvas.Graphics.Entities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ricardolopez on 2/28/15.
 */
public class BrushStroke {
    private String OwnerID;
    private int color;
    private List<Point> points;

    public BrushStroke(String ownerID, int color) {
        this.OwnerID = ownerID;
        this.color = color;
        this.points = new ArrayList<Point>();
    }

    public BrushStroke(String ownerID, int color,  List<Point> points) {
        this.OwnerID = ownerID;
        this.color = color;
        this.points = points;
    }

    public String getOwnerID() {
        return OwnerID;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }
}
