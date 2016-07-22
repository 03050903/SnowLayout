package com.licrafter.snowlayout.library;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.util.Random;

/**
 * author: shell
 * date 16/7/21 下午5:26
 **/
public class SnowLayout extends FrameLayout {

    private Drawable[] mDrawables;
    private int mInterval = 400;
    private Random mRandom = new Random();
    private Handler mHandler;
    private Runnable mRunnable;


    public SnowLayout(Context context) {
        super(context);
        init(context);
    }

    public SnowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SnowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void init(Context context) {
        //初始化生成循环
        mHandler = new Handler();
        mRunnable = new Runnable() {
            @Override
            public void run() {
                generateSnow();
                mHandler.postDelayed(this, mInterval);
            }
        };
    }

    public void setDrawables(Drawable[] drawables) {
        this.mDrawables = drawables;
    }

    /**
     * 生成snow并且添加动画
     */
    private void generateSnow() {
        if (mDrawables == null) {
            throw new RuntimeException("Snow drawables are not set");
        }
        int position = mRandom.nextInt(mDrawables.length);
        PointF startPoint = getStartPoint();
        ImageView snow = new ImageView(getContext());
        snow.setImageDrawable(mDrawables[position]);

        int dWidth = mDrawables[position].getIntrinsicWidth();
        int dHeight = mDrawables[position].getIntrinsicHeight();
        LayoutParams lp = new LayoutParams(dWidth, dHeight);
        lp.gravity = Gravity.TOP;
        snow.setLayoutParams(lp);
        snow.setX(startPoint.x);
        snow.setY(-50);
        addView(snow);
        AnimatorSet set = getAnimator(snow, startPoint);
        set.start();
    }

    /**
     * 获取生成动画和滑落动画的animatorset
     *
     * @param target
     * @param startPoint
     * @return
     */
    private AnimatorSet getAnimator(final View target, PointF startPoint) {
        AnimatorSet enterAnimator = getEnterAnimator(target);
        Animator bezierAnimator = getBezierAnimator(target, startPoint);
        AnimatorSet set = new AnimatorSet();
        set.playSequentially(enterAnimator, bezierAnimator);
        set.setTarget(target);
        set.setInterpolator(new AccelerateInterpolator());
        set.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                removeView(target);
            }
        });
        return set;
    }

    /**
     * 得到费塞尔曲线
     *
     * @param target
     * @param starPoint
     * @return
     */
    private Animator getBezierAnimator(final View target, PointF starPoint) {
        ValueAnimator bezierAnimator = ValueAnimator.ofObject(new BezierEvaluator(getAuxiliaryPoint(0), getAuxiliaryPoint(1))
                , starPoint, getEndPoint());
        bezierAnimator.setInterpolator(new AccelerateInterpolator());
        bezierAnimator.setDuration(2000);
        bezierAnimator.setTarget(target);
        bezierAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PointF pointF = (PointF) valueAnimator.getAnimatedValue();
                target.setX(pointF.x);
                target.setY(pointF.y);
            }
        });
        return bezierAnimator;
    }

    /**
     * 得到雪花出现的动画，缩放和渐变效果
     *
     * @param target
     * @return
     */
    private AnimatorSet getEnterAnimator(View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, View.ALPHA, 0.2f, 0.8f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, View.SCALE_X, 0.2f, 0.8f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, View.SCALE_Y, 0.2f, 0.8f);

        AnimatorSet enterSet = new AnimatorSet();
        enterSet.playTogether(alpha, scaleX, scaleY);
        enterSet.setDuration(500);
        enterSet.setTarget(target);
        enterSet.setInterpolator(new LinearInterpolator());

        return enterSet;
    }

    /**
     * 得到费塞尔曲线终止点
     *
     * @return
     */
    private PointF getEndPoint() {
        float endX = mRandom.nextInt(getWidth());
        return new PointF(endX, getHeight());
    }

    /**
     * 得到费塞尔曲线的起始点
     *
     * @return
     */
    private PointF getStartPoint() {
        int x = mRandom.nextInt(getWidth());
        return new PointF(x, -50);
    }

    /**
     * 得到两个控制点
     *
     * @param index
     * @return
     */
    private PointF getAuxiliaryPoint(int index) {
        PointF pointF = new PointF();
        //控制点x坐标在50和width之间
        pointF.x = mRandom.nextInt(getWidth());
        //第一个控制点坐标y在屏幕上半部，第二个控制点y在屏幕下半部
        pointF.y = mRandom.nextInt(getHeight() / 2) + getHeight() / 2 * index;
        return pointF;
    }

    /**
     * 开始雪花
     */
    public void start() {
        mHandler.postDelayed(mRunnable, mInterval);
    }

    /**
     * 停止雪花
     */
    public void stop() {
        mHandler.removeCallbacks(mRunnable);
    }
}
