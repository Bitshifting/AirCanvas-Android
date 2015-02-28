package bitshifting.aircanvas.Graphics.Managers;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kenneth on 2/28/15.
 */
//help draw various shapes
public class DrawHelper {

    public static List<List<float[]>> getCube(float[] pos, float halfEdgeLength) {
        List<float[]> listOfVertices = new ArrayList<>();
        List<float[]> listOfNormals = new ArrayList<>();

        //get a point based on every side
        float[] frontTopLeft = getPoint(true, true, true, halfEdgeLength, pos);
        float[] frontTopRight = getPoint(true, false, true, halfEdgeLength, pos);
        float[] frontBottomLeft = getPoint(false, true, true, halfEdgeLength, pos);
        float[] frontBottomRight = getPoint(false, false, true, halfEdgeLength, pos);

        float[] backTopLeft = getPoint(true, true, false, halfEdgeLength, pos);
        float[] backTopRight = getPoint(true, false, false, halfEdgeLength, pos);
        float[] backBottomLeft = getPoint(false, true, false, halfEdgeLength, pos);
        float[] backBottomRight = getPoint(false, false, false, halfEdgeLength, pos);

        //get the normals of each point
        float[] frontTopLeftNorm = getNormal(frontTopLeft, backTopLeft, frontTopRight, frontBottomLeft);
        float[] frontTopRightNorm = getNormal(frontTopRight, frontTopLeft, backTopRight, frontBottomRight);
        float[] frontBottomLeftNorm = getNormal(frontBottomLeft, frontBottomRight, backBottomLeft, frontTopLeft);
        float[] frontBottomRightNorm = getNormal(frontBottomRight, backBottomRight, frontBottomLeft, frontTopRight);

        float[] backTopLeftNorm = getNormal(backTopLeft, backTopRight, frontTopLeft, backBottomLeft);
        float[] backTopRightNorm = getNormal(backTopRight, frontTopRight, backTopLeft, backBottomRight);
        float[] backBottomLeftNorm = getNormal(backBottomLeft, frontBottomLeft, backBottomRight, backTopLeft);
        float[] backBottomRightNorm = getNormal(backBottomRight, backBottomLeft, frontBottomRight, backTopRight);

        //top plane
        List<List<float[]>> topFace = drawPlane(frontTopLeft, frontTopRight, backTopRight, backTopLeft, frontTopLeftNorm, frontTopRightNorm, backTopRightNorm, backTopLeftNorm);
        listOfVertices.addAll(topFace.get(0));
        listOfNormals.addAll(topFace.get(1));

        //bottom face
        List<List<float[]>> bottomFace = drawPlane(backBottomLeft, backBottomRight, frontBottomRight, frontBottomLeft, backBottomLeftNorm, backBottomRightNorm, frontBottomRightNorm, frontBottomLeftNorm);
        listOfVertices.addAll(bottomFace.get(0));
        listOfNormals.addAll(bottomFace.get(1));

        //left face
        List<List<float[]>> leftFace = drawPlane(frontTopLeft, backTopLeft, backBottomLeft, frontBottomLeft, frontTopLeftNorm, backTopLeftNorm, backBottomLeftNorm, frontBottomLeftNorm);
        listOfVertices.addAll(leftFace.get(0));
        listOfNormals.addAll(leftFace.get(1));

        //right face
        List<List<float[]>> rightFace = drawPlane(backTopRight, frontTopRight, frontBottomRight, backBottomRight, backTopRightNorm, frontTopRightNorm, frontBottomRightNorm, backBottomRightNorm);
        listOfVertices.addAll(rightFace.get(0));
        listOfNormals.addAll(rightFace.get(1));

        //font face
        List<List<float[]>> frontFace = drawPlane(frontTopRight, frontTopLeft, frontBottomLeft, frontBottomRight, frontTopRightNorm, frontTopLeftNorm, frontBottomLeftNorm, frontBottomRightNorm);
        listOfVertices.addAll(frontFace.get(0));
        listOfNormals.addAll(frontFace.get(1));

        //back face
        List<List<float[]>> backFace = drawPlane(backTopLeft, backTopRight, backBottomRight, backBottomLeft, backTopLeftNorm, backTopRightNorm, backBottomRightNorm, backBottomLeftNorm);
        listOfVertices.addAll(backFace.get(0));
        listOfNormals.addAll(backFace.get(1));

        //return both vertices and normals list
        List<List<float[]>> returnList = new ArrayList<>();
        returnList.add(listOfVertices);
        returnList.add(listOfNormals);

        return returnList;
    }

    public static List<float[]> getSphere(float[] pos, float radius) {
        return null;
    }

    public static List<List<float[]>> drawPlane(float[] topLeft, float[] topRight, float[] bottomRight, float[] bottomLeft,
                                    float[] topLeftNorm, float[] topRightNorm, float[] bottomRightNorm, float[] bottomLeftNorm) {
        List<float[]> listOfVertices = new ArrayList<>();
        List<float[]> listOfNormals = new ArrayList<>();

        listOfVertices.add(bottomRight);
        listOfVertices.add(topRight);
        listOfVertices.add(topLeft);

        listOfVertices.add(bottomRight);
        listOfVertices.add(topLeft);
        listOfVertices.add(bottomLeft);

        listOfNormals.add(bottomLeftNorm);
        listOfNormals.add(topRightNorm);
        listOfNormals.add(topLeftNorm);

        listOfNormals.add(bottomRightNorm);
        listOfNormals.add(topLeftNorm);
        listOfNormals.add(bottomLeftNorm);

        List<List<float[]>> returnList = new ArrayList<>();
        returnList.add(listOfVertices);
        returnList.add(listOfNormals);

        return returnList;
    }

    private static float[] getPoint(boolean isTop, boolean isLeft, boolean isFront, float size, float[] pos) {
        float[] returnPos = new float[3];

        System.arraycopy(pos, 0, returnPos, 0, pos.length);

        //once have returnPos
        returnPos[0] += isLeft ? -size : size;
        returnPos[1] += isTop ? size : -size;
        returnPos[2] += isFront ? -size : size;

        return returnPos;
    }

    private static float[] getNormal(float[] target, float[] right, float[] left, float[] down)
    {
        float[] vec1 = subVectors(right, target);
        float[] vec2 = subVectors(left, target);
        float[] vec3 = subVectors(down, target);

        //now use the combined vectors to get the normals
        float[] norm1 = crossVec(vec1, vec2);
        float[] norm2 = crossVec(vec2, vec3);
        float[] norm3 = crossVec(vec3, vec1);

        //return the resulting normal
        return normalize(mulVectors(addVectors(addVectors(norm1, norm2), norm3), 1.f / 3.0f));
    }


    public static float[] addVectors(float[] vec1, float[] vec2) {
        if(vec1.length != vec2.length) {
            return null;
        }

        float[] returnFloat = new float[vec1.length];

        for(int i = 0; i < vec1.length; i++) {
            returnFloat[i] = vec1[i] + vec2[i];
        }

        return returnFloat;
    }

    public static float[] subVectors(float[] vec1, float[] vec2) {
        if(vec1.length != vec2.length) {
            return null;
        }

        float[] returnFloat = new float[vec1.length];

        for(int i = 0; i < vec1.length; i++) {
            returnFloat[i] = vec1[i] - vec2[i];
        }

        return returnFloat;
    }

    public static float[] mulVectors(float[] vec1, float mult) {
        float[] returnFloat = new float[vec1.length];

        for(int i = 0; i < vec1.length; i++) {
            returnFloat[i] = vec1[i] * mult;
        }

        return returnFloat;

    }

    public static float[] crossVec(float[] vec1, float[] vec2) {
        if((vec1.length != vec2.length) || (vec1.length != 3)) {
            return null;
        }

        float[] returnFloat = new float[vec1.length];

        returnFloat[0] = (vec1[1] * vec2[2]) - (vec2[1] * vec1[2]);
        returnFloat[1] = (vec1[2] * vec2[0]) - (vec2[2] * vec1[0]);
        returnFloat[2] = (vec1[0] * vec2[1]) - (vec2[0] * vec1[1]);

        return returnFloat;
    }

    public static float[] normalize(float[] vec) {
        float magnitude = 0.f;

        for(int i = 0; i < vec.length; i++) {
            magnitude += i;
        }

        magnitude /= vec.length;

        float[] returnFloat = new float[vec.length];

        for(int i = 0; i < vec.length; i++) {
            returnFloat[i] = vec[i] / magnitude;
        }

        return returnFloat;
    }

}
