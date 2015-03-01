package bitshifting.aircanvas.Graphics.Entities;

import android.opengl.GLES30;
import android.opengl.Matrix;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import bitshifting.aircanvas.MainRenderer;

/**
 * Created by Kenneth on 2/28/15.
 */
public class Path {

    public final int MAX_VERTICES = 10000;

    public float[] pos;
    public float[] color;

    private int size;

    //GLSL information
    int program;

    int colorID;
    int projectionID;
    int viewID;
    int modelID;

    int[] vertexBuffer;

    public Path(float[] color, int shader) {
        this.color = color;

        //set shader
        program = shader;

        //get parameters from shader
        modelID = GLES30.glGetUniformLocation(program, "model");
        viewID = GLES30.glGetUniformLocation(program, "view");
        projectionID = GLES30.glGetUniformLocation(program, "projection");
        colorID = GLES30.glGetUniformLocation(program, "color");

        vertexBuffer = new int[1];

        //generate buffers
        GLES30.glGenBuffers(1, vertexBuffer, 0);

        //bind buffer
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBuffer[0]);
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, MAX_VERTICES * 4, null, GLES30.GL_STREAM_DRAW);

        //unbind
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        MainRenderer.checkGLError("COMPILING");

        size = 0;
    }

    public void render(float[] projectionMatrix, float[] viewMatrix) {

        GLES30.glLineWidth(10.f);

        GLES30.glUseProgram(program);

        //once program has been bound, set the attributes of within the shader
        //set parameter
        GLES30.glUniformMatrix4fv(projectionID, 1, false, projectionMatrix, 0);
        GLES30.glUniformMatrix4fv(viewID, 1, false, viewMatrix, 0);
        GLES30.glUniform3fv(colorID, 1, color, 0);

        GLES30.glEnableVertexAttribArray(0);
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBuffer[0]);
        //set up attribs
        GLES30.glVertexAttribPointer(0, 3, GLES30.GL_FLOAT, false, 0, 0);

        GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, size);


        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);
        GLES30.glDisableVertexAttribArray(0);


        //enable vertex attrib array for those that need it (the attributes)
        GLES30.glUseProgram(0);
    }

    public void update(float[] newPos) {
        //subbuffer data
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vertexBuffer[0]);

        //set up the float buffers
        ByteBuffer tempVertBuff = ByteBuffer.allocateDirect(newPos.length * 4);
        tempVertBuff.order(ByteOrder.nativeOrder());
        FloatBuffer verticesBuff;
        verticesBuff = tempVertBuff.asFloatBuffer();
        verticesBuff.put(newPos);
        verticesBuff.position(0);
        GLES30.glBufferSubData(GLES30.GL_ARRAY_BUFFER, size * 4 * 3, 3 * 4, verticesBuff);

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0);

        if(size < MAX_VERTICES) {
            size++;
        }
    }
}
