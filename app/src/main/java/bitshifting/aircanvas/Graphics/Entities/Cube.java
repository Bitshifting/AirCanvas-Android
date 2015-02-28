package bitshifting.aircanvas.Graphics.Entities;

import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.List;

import bitshifting.aircanvas.Graphics.Managers.DrawHelper;
import bitshifting.aircanvas.MainRenderer;

/**
 * Created by Kenneth on 2/28/15.
 */
public class Cube {
    public float[] pos;
    public float[] color;
    public float[] modelMatrix;

    public float[] vertices;
    public float[] normals;

    public FloatBuffer verticesBuff;
    public FloatBuffer normalsBuff;


    //GLSL information
    int program;

    int verticesID;
    int colorID;
    int normalID;
    int projectionID;
    int viewID;
    int modelID;

    public Cube(float[] pos, float[] color, float halfEdge, int shader) {
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

        //set shader
        program = shader;

        GLES30.glUseProgram(program);

        MainRenderer.checkGLError("COMPILING");

        //get parameters from shader
        modelID = GLES30.glGetUniformLocation(program, "model");
        viewID = GLES30.glGetUniformLocation(program, "view");
        projectionID = GLES30.glGetUniformLocation(program, "projection");
        colorID = GLES30.glGetUniformLocation(program, "color");

        verticesID = GLES30.glGetAttribLocation(program, "vertices");
        normalID = GLES30.glGetAttribLocation(program, "normals");

        MainRenderer.checkGLError("COMPILING");


        //enable vertex attrib array for those that need it (the attributes)
        GLES30.glEnableVertexAttribArray(verticesID);
        GLES30.glEnableVertexAttribArray(normalID);

        GLES30.glUseProgram(0);

        //set up the float buffers
        ByteBuffer tempVertBuff = ByteBuffer.allocateDirect(vertices.length * 4);
        tempVertBuff.order(ByteOrder.nativeOrder());
        verticesBuff = tempVertBuff.asFloatBuffer();
        verticesBuff.put(vertices);
        verticesBuff.position(0);

        ByteBuffer tempNormalBuff = ByteBuffer.allocateDirect(normals.length * 4);
        tempNormalBuff.order(ByteOrder.nativeOrder());
        normalsBuff = tempNormalBuff.asFloatBuffer();
        normalsBuff.put(normals);
        normalsBuff.position(0);
    }

    public void render(float[] projectionMatrix, float[] viewMatrix) {
        GLES30.glUseProgram(program);

        //once program has been bound, set the attributes of within the shader
        //set parameters
        GLES30.glUniformMatrix4fv(modelID, 1, false, modelMatrix, 0);
        GLES30.glUniformMatrix4fv(projectionID, 1, false, projectionMatrix, 0);
        GLES30.glUniformMatrix4fv(viewID, 1, false, viewMatrix, 0);
        GLES30.glUniform3fv(colorID, 1, color, 0);

        //set up attribs
        GLES30.glVertexAttribPointer(verticesID, 3, GLES30.GL_FLOAT,
                false, 0, verticesBuff);

//        GLES30.glVertexAttribPointer(normalID, 3, GLES30.GL_FLOAT,
//                false, 0, normalsBuff);
//        MainRenderer.checkGLError("RENDER");

        GLES30.glDrawArrays(GLES30.GL_TRIANGLES, 0, vertices.length / 3);
    }

    public void update(float dt) {

    }

}
