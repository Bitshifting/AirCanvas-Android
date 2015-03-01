package bitshifting.aircanvas.Graphics.Entities;

import java.util.HashMap;

import bitshifting.aircanvas.Graphics.Managers.ShaderManager;

/**
 * Created by Kenneth on 2/28/15.
 */
public class PathManager {
    public HashMap<Integer, Path> listOfPaths;
    public HashMap<Integer, float[]> listOfColors;

    Path lastPath;

    public PathManager() {
        listOfPaths = new HashMap<>();
        listOfColors = new HashMap<>();
    }

    public void render(float[] projectionMatrix, float[] viewMatrix) {
        for(Integer key : listOfPaths.keySet()) {
            listOfPaths.get(key).render(projectionMatrix, viewMatrix);
        }
    }

    public void setDrawing(float[] color, int index) {
        listOfColors.put(index, color);
        lastPath = new Path(color, ShaderManager.getInstance().getShader("NoLightVBO"));
        listOfPaths.put(index, lastPath);
    }

    public void update(float[] newPos) {
        if(lastPath != null) {
            lastPath.update(newPos);
        }
    }

    public void setColor(float[] color, int index) {
        listOfColors.put(index, color);
        listOfPaths.get(index).color = color;
    }
}
