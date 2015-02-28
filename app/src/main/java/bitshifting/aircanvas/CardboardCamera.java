package bitshifting.aircanvas;

import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraManager;
import android.opengl.GLES30;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import java.io.IOException;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Kenneth on 2/28/15.
 */

//This class will hold the starting point of all the opengl code
public class CardboardCamera implements CardboardView.StereoRenderer, SurfaceTexture.OnFrameAvailableListener {

    public static final String TAG = "MainRenderer";

    private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    private Camera camera;
    private SurfaceTexture surface;
    private float[] mView;
    private float[] mCamera;
    private int texture;

    CardboardView cardboardView;

    //called every frame (update)
    @Override
    public void onNewFrame(HeadTransform headTransform) {
        float[] mtx = new float[16];
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);
        surface.updateTexImage();
        surface.getTransformMatrix(mtx);
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

        texture = createTexture();

        startCamera(texture);
    }

    static private int createTexture()
    {
        int[] texture = new int[1];

        GLES30.glGenTextures(1,texture, 0);
        GLES30.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture[0]);
        GLES30.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MIN_FILTER,GL10.GL_LINEAR);
        GLES30.glTexParameterf(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_MAG_FILTER, GL10.GL_LINEAR);
        GLES30.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_S, GL10.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GL_TEXTURE_EXTERNAL_OES,
                GL10.GL_TEXTURE_WRAP_T, GL10.GL_CLAMP_TO_EDGE);

        return texture[0];
    }

    private void startCamera(int texture) {
        surface = new SurfaceTexture(texture);
        surface.setOnFrameAvailableListener(this);

        camera = Camera.open();

        try
        {
            camera.setPreviewTexture(surface);
            camera.startPreview();
        }
        catch (IOException ioe)
        {
            Log.e("MainActivity","CAM LAUNCH FAILED");
        }

    }

    //constructor
    public CardboardCamera(CardboardView cardboardView) {
        this.cardboardView = cardboardView;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {
        this.cardboardView.requestRender();
    }
}