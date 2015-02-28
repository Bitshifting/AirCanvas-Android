package bitshifting.aircanvas;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import bitshifting.aircanvas.Graphics.Entities.Cube;
import bitshifting.aircanvas.Graphics.Managers.ShaderManager;

/**
 * Created by Kenneth on 2/28/15.
 */

//This class will hold the starting point of all the opengl code
public class MainRenderer implements CardboardView.StereoRenderer {

    //used to get perspective matrix
    private static final float Z_NEAR = 0.1f;
    private static final float Z_FAR = 100.0f;

    //used for view matrix
    private static final float CAMERA_Z = 0.01f;


    public static final String TAG = "MainRenderer";

    Context context;
    ShaderManager shaderManager;

    float[] projectionMatrix;
    float[] viewMatrix;

    //view matrix helper
    float[] camera;

    Cube testCube;

    //called every frame (update)
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        Matrix.setLookAtM(camera, 0, 0.0f, 0.0f, CAMERA_Z, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
    }

    //render function
    @Override
    public void onDrawEye(Eye eye) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        projectionMatrix = eye.getPerspective(Z_NEAR, Z_FAR);
        Matrix.multiplyMM(viewMatrix, 0, eye.getEyeView(), 0, camera, 0);
        testCube.render(projectionMatrix, viewMatrix);
    }

    //called when button is pressed
    public void onCardboardTrigger() {

    }


    //called when frame has finished
    @Override
    public void onFinishFrame(Viewport viewport) {

    }

    @Override
    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    @Override
    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }

    // called when world starts
    @Override
    public void onSurfaceCreated(EGLConfig config) {
        //load shaders
        loadShaders();

        //set a clear color
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 0.f);

        //set depth test
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        //set alpha test
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc (GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);

        //create a cube a a bit aways
        float[] pos = {0, 0, -10.f};
        float[] color = {1, 0, 0};
        testCube = new Cube(pos, color, 5.f, shaderManager.getShader("NoLight"));

    }

    private void loadShaders() {
        shaderManager.addShader(R.raw.nolightvert, R.raw.nolightfrag, "NoLight");
    }

    //constructor
    public MainRenderer(Context ctx) {
        context = ctx;
        shaderManager = new ShaderManager(ctx);
        viewMatrix = new float[16];
        camera = new float[16];
    }

    public static void checkGLError(String label) {
        int error;
        while ((error = GLES30.glGetError()) != GLES30.GL_NO_ERROR) {
            Log.e(TAG, label + ": glError " + error);
            throw new RuntimeException(label + ": glError " + error);
        }
    }

}
