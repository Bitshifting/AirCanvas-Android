package bitshifting.aircanvas.Picture;

import android.hardware.Camera;
import android.util.Log;

/**
 * Created by Kenneth on 2/28/15.
 */
public class PictureTaken implements Camera.PictureCallback {

    @Override
    public void onPictureTaken(byte[] data, Camera camera) {
        Log.e("PICTURETAKEN", "HELLO");
    }
}
