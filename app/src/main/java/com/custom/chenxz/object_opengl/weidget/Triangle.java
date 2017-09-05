package com.custom.chenxz.object_opengl.weidget;

import android.opengl.GLES20;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * 自定义三角形
 */

public class Triangle {
    /**
     * 定义着色器
     * 至少需要一个vertexshader来绘制一个形状和一个fragmentshader来为形状上色。
     * 这些形状必须被编译然后被添加到一个OpenGLES program中，program之后被用来绘制形状
     */
    private final String vertexShaderCode =
            // This matrix member variable provides a hook to manipulate
            // the coordinates of the objects that use this vertex shader
            "uniform mat4 uMVPMatrix;" +
                    "attribute vec4 vPosition;" +
                    "void main() {" +
                    // the matrix must be included as a modifier of gl_Position
                    // Note that the uMVPMatrix factor *must be first* in order
                    // for the matrix multiplication product to be correct.
                    "  gl_Position = uMVPMatrix * vPosition;" +
                    "}";

    private final String fragmentShaderCode =
            "precision mediump float;" +
                    "uniform vec4 vColor;" +
                    "void main() {" +
                    "  gl_FragColor = vColor;" +
                    "}";
    private FloatBuffer vertexBuffer;

    private final int mProgram;
    private int mPositionHandle;
    private int mColorHandle;
    private int mMVPMatrixHandle;
    private static final int COORDS_PER_VERTEX = 3;
    public float triangleCoords[] = {//按照逆时针方向排序
            0.0f, 0.622008459f, 0.0f,   // top
            -0.5f, -0.311004243f, 0.0f,   // bottom left
            0.5f, -0.311004243f, 0.0f    // bottom right
    };
    // 设置颜色，分别为red, green, blue 和alpha (opacity)
    float color[] = {0.63671875f, 0.76953125f, 0.22265625f, 1.0f};
    private final int vertexCount = triangleCoords.length / COORDS_PER_VERTEX;
    private final int vertexStride = COORDS_PER_VERTEX * 4; // 4 bytes per vertex

    public Triangle() {
        // 为存放形状的坐标，初始化顶点字节缓冲
        ByteBuffer bb = ByteBuffer.allocateDirect(
                // (坐标数 * 4)float占四字节
                triangleCoords.length * 4);
        // 设用设备的本点字节序
        bb.order(ByteOrder.nativeOrder());
        // 从ByteBuffer创建一个浮点缓冲
        vertexBuffer = bb.asFloatBuffer();
        // 把坐标们加入FloatBuffer中
        vertexBuffer.put(triangleCoords);
        // 设置buffer，从第一个坐标开始读
        vertexBuffer.position(0);
        int vertexShader = CustomRender.loadShader(GLES20.GL_VERTEX_SHADER, vertexShaderCode);
        int fragmentShader = CustomRender.loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentShaderCode);
        mProgram = GLES20.glCreateProgram();  // 创建一个空的OpenGL ES Program
        GLES20.glAttachShader(mProgram, vertexShader); // 将vertex shader添加到program
        GLES20.glAttachShader(mProgram, fragmentShader); // 将fragment shader添加到program
        GLES20.glLinkProgram(mProgram); // 创建可执行的 OpenGL ES program
    }

    /**
     * 设置位置和颜色值到形状的vertexShader和fragmentShader，然后执行绘制功能。
     *
     * @param mvpMatrix The Model View Project matrix in which to draw
     *                  this shape.
     */
    public void draw(float[] mvpMatrix) {
        //将program加入OpenGL ES环境中
        GLES20.glUseProgram(mProgram);
        //获取指向vertex shader的成员vPosition的handle
        mPositionHandle = GLES20.glGetAttribLocation(mProgram, "vPosition");
        // 启用一个指向三角形顶点数组的handle
        GLES20.glEnableVertexAttribArray(mPositionHandle);
        //准备三角形的数据
        GLES20.glVertexAttribPointer(mPositionHandle, COORDS_PER_VERTEX,
                GLES20.GL_FLOAT, false, vertexStride, vertexBuffer);
        //获取指向fragment shader 的成员vColor的handle
        mColorHandle = GLES20.glGetUniformLocation(mProgram, "vColor");
        //设置三角形的颜色
        GLES20.glUniform4fv(mColorHandle, 1, color, 0);
        //获取指向shape变换矩阵的handler
        mMVPMatrixHandle = GLES20.glGetUniformLocation(mProgram, "uMVPMatrix");
        CustomRender.checkGlError("glGetUniformLocation");
        //应用投影和视图变换
        GLES20.glUniformMatrix4fv(mMVPMatrixHandle, 1, false, mvpMatrix, 0);
        CustomRender.checkGlError("glUniformMatrix4fv");
        //画三角形
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, vertexCount);
        //禁止指向三角形的定点数组
        GLES20.glDisableVertexAttribArray(mPositionHandle);
    }

}
