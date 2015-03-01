package bitshifting.aircanvas.Graphics.Managers;

import android.content.Context;
import android.opengl.GLES30;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

/**
 * Created by Kenneth on 2/28/15.
 */
public class ShaderManager {

    public final String TAG = "ShaderManager";

    private Hashtable<String, Integer> listOfShaders;

    static ShaderManager instance = null;

    public Context context;

    protected ShaderManager() {
        listOfShaders = new Hashtable<>();
    }

    public static ShaderManager getInstance() {
        if(instance == null) {
            instance = new ShaderManager();
        }

        return instance;
    }

    public void setContext(Context ctx) {
        this.context = ctx;
    }

    //add shader
    public int addShader(int vertex, int fragment, String key) {
        //get vertex and fragment shader
        int vertShader = loadGLShader(GLES30.GL_VERTEX_SHADER, vertex);
        int fragShader = loadGLShader(GLES30.GL_FRAGMENT_SHADER, fragment);

        int program = GLES30.glCreateProgram();
        GLES30.glAttachShader(program, vertShader);
        GLES30.glAttachShader(program, fragShader);
        GLES30.glLinkProgram(program);

        listOfShaders.put(key, program);

        return program;
    }

    public boolean removeShader(String key) {

        if(listOfShaders.containsKey(key)) {
            listOfShaders.remove(key);
            return true;
        }

        return false;
    }

    public int getShader(String key) {
        return listOfShaders.get(key);
    }

    private int loadGLShader(int type, int resId) {
        String code = readRawTextFile(resId);
        int shader = GLES30.glCreateShader(type);
        GLES30.glShaderSource(shader, code);
        GLES30.glCompileShader(shader);

        // Get the compilation status.
        final int[] compileStatus = new int[1];
        GLES30.glGetShaderiv(shader, GLES30.GL_COMPILE_STATUS, compileStatus, 0);

        // If the compilation failed, delete the shader.
        if (compileStatus[0] == 0) {
            Log.e(TAG, "Error compiling shader: " + GLES30.glGetShaderInfoLog(shader));
            GLES30.glDeleteShader(shader);
            shader = 0;
        }

        if (shader == 0) {
            throw new RuntimeException("Error creating shader.");
        }

        return shader;
    }

    private String readRawTextFile(int resId) {
        InputStream inputStream = context.getResources().openRawResource(resId);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
