package bitshifting.aircanvas.Graphics.Entities;

import android.opengl.Matrix;

import java.util.ArrayList;
import java.util.List;

import bitshifting.aircanvas.Graphics.Managers.DrawHelper;

/**
 * Created by Kenneth on 2/28/15.
 */
public class Cube {
    public float[] pos;
    public float[] color;
    public float[] modelMatrix;

    public float[] vertices;
    public float[] normals;

    public Cube(float[] pos, float[] color, float halfEdge) {
        this.pos = pos;
        this.color = color;

        List<List<float[]>> pointsOfCube = DrawHelper.getCube(pos, halfEdge);

        int sizeOfArrays = pointsOfCube.get(0).size() * 3;
        vertices = new float[sizeOfArrays];
        normals = new float[sizeOfArrays];

        //convert to list of floats
        for(int i = 0; i < 2; i++) {
            for(int j = 0; j < pointsOfCube.get(0).size(); j++) {
                for(int k = 0; k < 3; k++) {
                    if(i == 0) {
                        vertices[(j * 3) + k]  = pointsOfCube.get(i).get(j)[k];
                    } else {
                        normals[(j * 3) + k] = pointsOfCube.get(i).get(j)[k];
                    }
                }
            }
        }

        //set model matrix to identity
        modelMatrix = new float[16];
        //once received vertices and normals, set model matrix to identity
        Matrix.setIdentityM(modelMatrix, 0);
    }

    public void render(float[] projectionMatrix, float[] viewMatrix) {

    }

    public void update(float dt) {

    }

}
