package bitshifting.aircanvas;

import android.opengl.GLES30;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

/**
 * Created by Kenneth on 2/28/15.
 */

//This class will hold the starting point of all the opengl code
public class MainRenderer implements CardboardView.StereoRenderer {

    public static final String TAG = "MainRenderer";

    //called every frame (update)
    @Override
    public void onNewFrame(HeadTransform headTransform) {

    }

    //render function
    @Override
    public void onDrawEye(Eye eye) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
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

        //set a clear color
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 0.f);

        //set depth test
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);

        //set alpha test
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc (GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);

    }

    //constructor
    public MainRenderer() {

    }

}
