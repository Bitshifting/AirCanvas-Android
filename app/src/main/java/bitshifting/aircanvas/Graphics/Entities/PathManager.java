package bitshifting.aircanvas.Graphics.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import bitshifting.aircanvas.Graphics.Managers.ShaderManager;

/**
 * Created by Kenneth on 2/28/15.
 */
public class PathManager {
    public HashMap<String, Path> listOfPaths;
    int currInd;

    Path lastPath;

    public PathManager() {
        listOfPaths = new HashMap<>();
        currInd = 0;
    }

    public void render(float[] projectionMatrix, float[] viewMatrix) {
        for(int i = 0; i < listOfPaths.size(); i++) {
            listOfPaths.get("" + i).render(projectionMatrix, viewMatrix);
        }

    }

    public void setDrawing(float[] color) {
        lastPath = new Path(color, ShaderManager.getInstance().getShader("NoLightVBO"));
        listOfPaths.put("" + currInd, lastPath);
        currInd++;
    }

    public void update(float[] newPos) {
        if(lastPath != null) {
            lastPath.update(newPos);
        }
    }
}
