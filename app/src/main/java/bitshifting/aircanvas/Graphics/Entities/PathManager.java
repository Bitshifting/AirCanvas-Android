package bitshifting.aircanvas.Graphics.Entities;

import android.opengl.GLES30;

import java.util.ArrayList;
import java.util.List;

import bitshifting.aircanvas.Graphics.Managers.ShaderManager;
import bitshifting.aircanvas.MainRenderer;

/**
 * Created by Kenneth on 2/28/15.
 */
public class PathManager {
    private List<Path> listOfPaths;
    private List<float[]> listOfColors;

    Path lastPath;

    public PathManager() {
        listOfPaths = new ArrayList<>();
        listOfColors = new ArrayList<>();
    }

    public void render(float[] projectionMatrix, float[] viewMatrix) {
        for(int i = 0; i < listOfPaths.size(); i++) {
            listOfPaths.get(i).render(projectionMatrix, viewMatrix);
        }
    }

    public void setDrawing(float[] color) {
        listOfColors.add(color);
        lastPath = new Path(color, ShaderManager.getInstance().getShader("NoLightVBO"));
        listOfPaths.add(lastPath);
    }

    public void update(float[] newPos) {
        if(lastPath != null) {
            lastPath.update(newPos);
        }
    }

}
