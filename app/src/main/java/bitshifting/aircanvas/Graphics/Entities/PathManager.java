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
    float[] color;

    Path lastPath;

    public PathManager(float[] color) {
        listOfPaths = new ArrayList<>();

        this.color = color;
    }

    public void render(float[] projectionMatrix, float[] viewMatrix) {
        for(int i = 0; i < listOfPaths.size(); i++) {
            listOfPaths.get(i).render(projectionMatrix, viewMatrix);
        }
    }

    public void setDrawing(boolean isDrawing) {
        if(isDrawing) {

            lastPath = new Path(color, ShaderManager.getInstance().getShader("NoLightVBO"));
            listOfPaths.add(lastPath);
        }
    }

    public void update(float[] newPos) {
        if(lastPath != null) {
            lastPath.update(newPos);
        }
    }

}
