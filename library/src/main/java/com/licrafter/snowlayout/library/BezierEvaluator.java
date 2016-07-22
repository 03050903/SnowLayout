package com.licrafter.snowlayout.library;

import android.animation.TypeEvaluator;
import android.graphics.PointF;

/**
 * author: shell
 * date 16/7/21 下午5:26
 **/
public class BezierEvaluator implements TypeEvaluator<PointF> {

    private PointF mAuxiliaryOne;
    private PointF mAuxiliaryTwo;

    public BezierEvaluator(PointF pointF1, PointF pointF2) {
        this.mAuxiliaryOne = pointF1;
        this.mAuxiliaryTwo = pointF2;
    }

    @Override
    public PointF evaluate(float t, PointF startValue, PointF endValue) {
        return BezierUtil.CalculateBezierPointForCubic(t, startValue, mAuxiliaryOne, mAuxiliaryTwo, endValue);
    }
}