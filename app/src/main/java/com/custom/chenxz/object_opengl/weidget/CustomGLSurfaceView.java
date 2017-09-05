package com.custom.chenxz.object_opengl.weidget;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Administrator on 2017/9/5.
 */

public class CustomGLSurfaceView extends GLSurfaceView {

    private final CustomRender customRender;
    private final float TOUCH_SCALE_FACTOR = 180.0f / 320;
    private float mPreviousX;
    private float mPreviousY;

    public CustomGLSurfaceView(Context context) {
        super(context);
        // 创建一个OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        //设置一个Renderer 用于绘制GLSurfaceView
        customRender = new CustomRender();
        setRenderer(customRender);
        //只在绘制数据发生改变时才绘制view。
        // 此设置会阻止绘制GLSurfaceView的帧，直到你调用了requestRender()，这样会非常高效。
        // Render模式默认值为GLSurfaceView.RENDERMODE_CONTINUOUSLY，Render接口的onDrawFrame()方法每隔16ms都会回调一次完成刷新绘制。
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    public CustomGLSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 创建一个OpenGL ES 2.0 context
        setEGLContextClientVersion(2);
        //设置一个Renderer 用于绘制GLSurfaceView
        customRender = new CustomRender();
        setRenderer(customRender);
        //只在绘制数据发生改变时才绘制view。
        // 此设置会阻止绘制GLSurfaceView的帧，直到你调用了requestRender()，这样会非常高效。
        // Render模式默认值为GLSurfaceView.RENDERMODE_CONTINUOUSLY，Render接口的onDrawFrame()方法每隔16ms都会回调一次完成刷新绘制。
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // MotionEvent带有从触摸屏幕来的输入的详细信息以及其它输入控制
        // 此处，你只需对触摸位置的改变感兴趣即可。
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                float dx = x - mPreviousX;
                float dy = y - mPreviousY;
                //中线以上的反向旋转方向
                if (y > getHeight() / 2) {
                    dx = dx * -1;
                }
                //中线向左旋转的反向方向
                if (x < getWidth() / 2) {
                    dy = dy * -1;
                }
                customRender.setAngle(customRender.getAngle() + ((dx + dy) * TOUCH_SCALE_FACTOR));// = 180.0f / 320
                requestRender();
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}
