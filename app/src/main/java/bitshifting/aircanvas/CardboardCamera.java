package bitshifting.aircanvas;

import android.content.Context;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.GLES30;
import android.opengl.Matrix;
import android.util.Log;

import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.Viewport;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import bitshifting.aircanvas.Graphics.Managers.ShaderManager;

/**
 * Created by Kenneth on 2/28/15.
 */

//This class will hold the starting point of all the opengl code
public class CardboardCamera implements SurfaceTexture.OnFrameAvailableListener {

    public static final String TAG = "CardboardCamera";

    private static final int GL_TEXTURE_EXTERNAL_OES = 0x8D65;

    private Camera camera;
    private SurfaceTexture surface;
    private float[] mView;
    private float[] mCamera;

    private FloatBuffer vertexBuffer, textureVerticesBuffer, vertexBuffer2;
    private ShortBuffer drawListBuffer, buf2;
    private int mProgram;
    private int mPositionHandle, mPositionHandle2;
    private int mColorHandle;
    private int mTextureCoordHandle;
    CardboardView cardboardView;

    Context context;

    static MainRenderer instanceOfRenderer;

    private short drawOrder[] =  {0, 2, 1, 1, 2, 3 }; // order to draw vertices
    private short drawOrder2[] = {2, 0, 3, 3, 0, 1}; // order to draw vertices

    static float textureVertices[] = {
            0.0f, 1.0f,  // A. left-bottom
            1.0f, 1.0f, // B. right-bottom
            0.0f, 0.0f, // C. left-top
            1.0f, 0.0f // D. right-top
    };

    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    private ByteBuffer indexBuffer;    // Buffer for index-array

    private int texture;


    //for the camera
    //This variable is responsible for getting and setting the camera settings
    private Camera.Parameters parameters;
    //this variable stores the camera preview size
    private Camera.Size previewSize;
    //this array stores the pixels as hexadecimal pairs
    private int[] pixels;

    static final int COORDS_PER_VERTEX = 2;

    static float squareVertices[] = { // in counterclockwise order:
            -1.0f, -1.0f,   // 0.left - mid
            1.0f, -1.0f,   // 1. right - mid
            -1.0f, 1.0f,   // 2. left - top
            1.0f, 1.0f,   // 3. right - top
    };

    int count;

    //called every frame (update)
    public void onNewFrame(HeadTransform headTransform) {
        float[] mtx = new float[16];
        surface.updateTexImage();

        surface.getTransformMatrix(mtx);
    }

    //render function
    public void onDrawEye(Eye eye) {
        GLES30.glUseProgram(mProgram);

        GLES30.glBindTexture(GL_TEXTURE_EXTERNAL_OES, texture);

        mPositionHandle = GLES30.glGetAttribLocation(mProgram, "position");
        GLES30.glEnableVertexAttribArray(mPositionHandle);
        GLES30.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX, GLES30.GL_FLOAT,
                false,vertexStride, vertexBuffer);


        mTextureCoordHandle = GLES30.glGetAttribLocation(mProgram, "inputTextureCoordinate");
        GLES30.glEnableVertexAttribArray(mTextureCoordHandle);
        GLES30.glVertexAttribPointer(mTextureCoordHandle, COORDS_PER_VERTEX, GLES30.GL_FLOAT,
                false,vertexStride, textureVerticesBuffer);

        mColorHandle = GLES30.glGetAttribLocation(mProgram, "s_texture");

        GLES30.glDisable(GLES30.GL_DEPTH_TEST);
        GLES30.glDepthMask(false);

        GLES30.glDrawElements(GLES30.GL_TRIANGLES, drawOrder.length,
                GLES30.GL_UNSIGNED_SHORT, drawListBuffer);

        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glDepthMask(true);

        // Disable vertex array
        GLES30.glDisableVertexAttribArray(mPositionHandle);
        GLES30.glDisableVertexAttribArray(mTextureCoordHandle);

        Matrix.multiplyMM(mView, 0, eye.getEyeView(), 0, mCamera, 0);
    }

    //called when button is pressed
    public void onCardboardTrigger() {

    }


    //called when frame has finished
    public void onFinishFrame(Viewport viewport) {

    }

    public void onRendererShutdown() {
        Log.i(TAG, "onRendererShutdown");
    }

    public void onSurfaceChanged(int width, int height) {
        Log.i(TAG, "onSurfaceChanged");
    }

    // called when world starts
    public void onSurfaceCreated(EGLConfig config) {
        Log.i(TAG, "onSurfaceCreated");
        GLES30.glClearColor(0.1f, 0.1f, 0.1f, 0.5f); // Dark background so text shows up well

        ByteBuffer bb = ByteBuffer.allocateDirect(squareVertices.length * 4);
        bb.order(ByteOrder.nativeOrder());
        vertexBuffer = bb.asFloatBuffer();
        vertexBuffer.put(squareVertices);
        vertexBuffer.position(0);


        ByteBuffer dlb = ByteBuffer.allocateDirect(drawOrder.length * 2);
        dlb.order(ByteOrder.nativeOrder());
        drawListBuffer = dlb.asShortBuffer();
        drawListBuffer.put(drawOrder);
        drawListBuffer.position(0);


        ByteBuffer bb2 = ByteBuffer.allocateDirect(textureVertices.length * 4);
        bb2.order(ByteOrder.nativeOrder());
        textureVerticesBuffer = bb2.asFloatBuffer();
        textureVerticesBuffer.put(textureVertices);
        textureVerticesBuffer.position(0);

        mProgram = ShaderManager.getInstance().getShader("Texture");

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

        parameters = camera.getParameters();


        previewSize = parameters.getPreviewSize();
        pixels = new int[previewSize.width * previewSize.height];

        //set camera height and width for main renderer
        MainRenderer.cameraHeight = previewSize.height;
        MainRenderer.cameraWidth = previewSize.width;

        try
        {
            camera.setPreviewTexture(surface);
            camera.setPreviewCallback(instanceOfRenderer);
            camera.startPreview();
        }
        catch (IOException ioe)
        {
            Log.e("MainActivity","CAM LAUNCH FAILED");
        }

    }

    //constructor
    public CardboardCamera(CardboardView cardboardView, Context ctx, MainRenderer renderer) {
        this.cardboardView = cardboardView;
        this.context = ctx;
        mView = new float[16];
        mCamera = new float[16];
        count = 0;

        instanceOfRenderer = renderer;
    }

    @Override
    public void onFrameAvailable(SurfaceTexture surfaceTexture) {

        this.cardboardView.requestRender();
    }


    static void decodeYUV420SP(int[] rgb, byte[] yuv420sp, int width, int height) {

        final int frameSize = width * height;

        for (int j = 0, yp = 0; j < height; j++) {       int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
            for (int i = 0; i < width; i++, yp++) {
                int y = (0xff & ((int) yuv420sp[yp])) - 16;
                if (y < 0)
                    y = 0;
                if ((i & 1) == 0) {
                    v = (0xff & yuv420sp[uvp++]) - 128;
                    u = (0xff & yuv420sp[uvp++]) - 128;
                }

                int y1192 = 1192 * y;
                int r = (y1192 + 1634 * v);
                int g = (y1192 - 833 * v - 400 * u);
                int b = (y1192 + 2066 * u);

                if (r < 0)                  r = 0;               else if (r > 262143)
                    r = 262143;
                if (g < 0)                  g = 0;               else if (g > 262143)
                    g = 262143;
                if (b < 0)                  b = 0;               else if (b > 262143)
                    b = 262143;

                rgb[yp] = 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
            }
        }
    }

    static int getMiddlePixel(byte[] yuv420sp, int width, int height) {

        final int frameSize = width * height;


        int i = width / 2;
        int j = height / 2;

        int uvp = frameSize + (j >> 1) * width, u = 0, v = 0;
        int yp = frameSize / 2;


        int y = (0xff & ((int) yuv420sp[yp])) - 16;
        if (y < 0)
            y = 0;
        if ((i & 1) == 0) {
            v = (0xff & yuv420sp[uvp++]) - 128;
            u = (0xff & yuv420sp[uvp++]) - 128;
        }

        int y1192 = 1192 * y;
        int r = (y1192 + 1634 * v);
        int g = (y1192 - 833 * v - 400 * u);
        int b = (y1192 + 2066 * u);

        if (r < 0)                  r = 0;               else if (r > 262143)
            r = 262143;
        if (g < 0)                  g = 0;               else if (g > 262143)
            g = 262143;
        if (b < 0)                  b = 0;               else if (b > 262143)
            b = 262143;

        return 0xff000000 | ((r << 6) & 0xff0000) | ((g >> 2) & 0xff00) | ((b >> 10) & 0xff);
    }
}