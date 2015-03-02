package bitshifting.aircanvas.Graphics.Entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import bitshifting.aircanvas.Graphics.Managers.ShaderManager;

/**
 * Created by Kenneth on 2/28/15.
 */
public class PathManager {
    public HashMap<String, Path> listOfPaths;
    public int currInd;
    public final Lock lock = new ReentrantLock();

    Path lastPath;

    public PathManager() {
        listOfPaths = new HashMap<>();
        currInd = 0;
    }

    public void render(float[] projectionMatrix, float[] viewMatrix) {
        for(String keys : listOfPaths.keySet()) {
            lock.lock();
            listOfPaths.get(keys).render(projectionMatrix, viewMatrix);
            lock.unlock();
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
