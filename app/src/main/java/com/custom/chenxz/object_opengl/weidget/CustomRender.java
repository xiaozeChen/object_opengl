package com.custom.chenxz.object_opengl.weidget;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.util.Log;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by Administrator on 2017/9/5.
 */

public class CustomRender implements GLSurfaceView.Renderer {

    private Triangle triangle;
    private Square square;
    private float mAngle;
    // mMVPMatrix is an abbreviation for "Model View Projection Matrix"
    private final float[] mMVPMatrix = new float[16];
    private final float[] mProjectionMatrix = new float[16];
    private final float[] mViewMatrix = new float[16];
    private final float[] mRotationMatrix = new float[16];

    @Override
    public void onSurfaceCreated(GL10 gl10, EGLConfig eglConfig) {
        //定义绘制的背景颜色
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
        //初始化一个三角形
        triangle = new Triangle();
        //初始化一个正方形
        square = new Square();
    }

    @Override
    public void onSurfaceChanged(GL10 gl10, int width, int height) {
        GLES20.glViewport(0, 0, width, height);//定义画布大小
        float ratio = (float) width / height;
        // this projection matrix is applied to object coordinates
        // in the onDrawFrame() method
        Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1, 1, 3, 7);
//        /*GL_PROJECTION，是投影的意思，就是要对投影相关进行操作，也就是把物体投影到一个平面上，就像我们照相一样，把3维物体投到2维的平面上。这样，接下来的语句可以是跟透视相关的函数，比如glFrustum()或gluPerspective()；
//          GL_MODELVIEW，是对模型视景的操作，接下来的语句描绘一个以模型为基础的适应，这样来设置参数，接下来用到的就是像gluLookAt()这样的函数；
//          GL_TEXTURE，就是对纹理相关进行操作；
//          三个操作都是基于矩阵操作*/
//        gl10.glMatrixMode(GL10.GL_PROJECTION);//投影矩阵
//        gl10.glLoadIdentity();//清除之前的绘画
//        gl10.glFrustumf(-400, 400, -240, 240, 0.3f, 100);//设置映射关系
    }

    @Override
    public void onDrawFrame(GL10 gl10) {
        float[] scratch = new float[16];
        //绘制背景颜色
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        //设置相机位置(视图矩阵)
        Matrix.setLookAtM(mViewMatrix, 0, 0, 0, -3f, 0f, 0f, 0f, 0f, 1f, 0.0f);
        //计算投影和视图变换
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0);
        //绘制正方形
//        square.draw(mMVPMatrix);
        //为三角形创建一个旋转
        //使用下面的代码生成常量旋转。
        //离开这个代码时使用触摸事件。
//         long time = SystemClock.uptimeMillis() % 4000L;
//         float mAngle = 0.090f * ((int) time);
        Matrix.setRotateM(mRotationMatrix, 0, mAngle, 0, 0, 1.0f);
        //把旋转矩阵合并到投影和相机矩阵
        Matrix.multiplyMM(scratch, 0, mMVPMatrix, 0, mRotationMatrix, 0);
        //绘制三角形
        triangle.draw(scratch);
    }

    /**
     * 着色器编译工具类
     *
     * @param type
     * @param shaderCode
     * @return
     */
    public static int loadShader(int type, String shaderCode) {
        // 创建一个vertex shader类型(GLES20.GL_VERTEX_SHADER)
        // 或fragment shader类型(GLES20.GL_FRAGMENT_SHADER)
        int shader = GLES20.glCreateShader(type);
        //将源码添加到shader并编译之
        GLES20.glShaderSource(shader, shaderCode);
        GLES20.glCompileShader(shader);
        return shader;
    }

    /**
     * Utility method for debugging OpenGL calls. Provide the name of the call
     * just after making it:
     * mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
     * MyGLRenderer.checkGlError("glGetUniformLocation");
     * <p>
     * If the operation is not successful, the check throws an error.
     *
     * @param glOperation - Name of the OpenGL call to check.
     */
    public static void checkGlError(String glOperation) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e("cxz", glOperation + ": glError " + error);
            throw new RuntimeException(glOperation + ": glError " + error);
        }
    }

    /**
     * Returns the rotation angle of the triangle shape (mTriangle).
     *
     * @return - A float representing the rotation angle.
     */
    public float getAngle() {
        return mAngle;
    }

    /**
     * Sets the rotation angle of the triangle shape (mTriangle).
     */
    public void setAngle(float angle) {
        mAngle = angle;
    }

}
