package com.blog.www.guideview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by binIoter
 */

class MaskView extends ViewGroup {
    /**
     * 高亮区域
     */
    private final RectF mTargetRect = new RectF();
    /**
     * 蒙层区域
     */
    private final RectF mOverlayRect = new RectF();

    /**
     * 中间变量
     */
    private final RectF mChildTmpRect = new RectF();
    /**
     * 蒙层背景画笔
     */
    private final Paint mFullingPaint;
    private int mPadding = 0;
    private int mPaddingLeft = 0;
    private int mPaddingTop = 0;
    private int mPaddingRight = 0;
    private int mPaddingBottom = 0;
    /**
     * 是否覆盖目标区域
     */
    private boolean mOverlayTarget = false;
    /**
     * 圆角大小
     */
    private int mCorner = 0;
    /**
     * 目标区域样式，默认为矩形
     */
    private int mStyle = Component.ROUNDRECT;
    /**
     * 挖空画笔
     */
    private Paint mEraser;
    /**
     * 橡皮擦Bitmap
     */
    private Bitmap mEraserBitmap;
    /**
     * 橡皮擦Cavas
     */
    private Canvas mEraserCanvas;

    private boolean ignoreRepadding;

    private int mInitHeight;
    private int mChangedHeight = 0;
    private boolean mFirstFlag = true;

    public MaskView(Context context) {
        this(context, null, 0);
    }

    public MaskView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MaskView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //自我绘制
        setWillNotDraw(false);

        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getRealMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        mOverlayRect.set(0, 0, width, height);
        mEraserBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ALPHA_8);
        mEraserCanvas = new Canvas(mEraserBitmap);
        mFullingPaint = new Paint();
        mEraser = new Paint();
        mEraser.setColor(0xFFFFFFFF);
        //图形重叠时的处理方式，擦除效果
        mEraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        //位图抗锯齿设置
        mEraser.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        try {
            clearFocus();
            mEraserCanvas.setBitmap(null);
            mEraserBitmap = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int w = MeasureSpec.getSize(widthMeasureSpec);
        final int h = MeasureSpec.getSize(heightMeasureSpec);
        if (mFirstFlag) {
            mInitHeight = h;
            mFirstFlag = false;
        }
        if (mInitHeight > h) {
            mChangedHeight = h - mInitHeight;
        } else if (mInitHeight < h) {
            mChangedHeight = h - mInitHeight;
        } else {
            mChangedHeight = 0;
        }
        setMeasuredDimension(w, h);
        mOverlayRect.set(0, 0, w, h);
        resetOutPath();

        final int count = getChildCount();
        View child;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if (child != null) {
                measureChild(child, widthMeasureSpec, heightMeasureSpec);
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        final float density = getResources().getDisplayMetrics().density;
        View child;
        for (int i = 0; i < count; i++) {
            child = getChildAt(i);
            if (child == null) {
                continue;
            }
            LayoutParams lp = (LayoutParams) child.getLayoutParams();
            if (lp == null) {
                continue;
            }
            switch (lp.targetAnchor) {
                case LayoutParams.ANCHOR_LEFT://左
                    mChildTmpRect.right = mTargetRect.left;
                    mChildTmpRect.left = mChildTmpRect.right - child.getMeasuredWidth();
                    verticalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition);
                    break;
                case LayoutParams.ANCHOR_TOP://上
                    mChildTmpRect.bottom = mTargetRect.top;
                    mChildTmpRect.top = mChildTmpRect.bottom - child.getMeasuredHeight();
                    horizontalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition);
                    break;
                case LayoutParams.ANCHOR_RIGHT://右
                    mChildTmpRect.left = mTargetRect.right;
                    mChildTmpRect.right = mChildTmpRect.left + child.getMeasuredWidth();
                    verticalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition);
                    break;
                case LayoutParams.ANCHOR_BOTTOM://下
                    mChildTmpRect.top = mTargetRect.bottom;
                    mChildTmpRect.bottom = mChildTmpRect.top + child.getMeasuredHeight();
                    horizontalChildPositionLayout(child, mChildTmpRect, lp.targetParentPosition);
                    break;
                case LayoutParams.ANCHOR_OVER://中心
                    mChildTmpRect.left = ((int) mTargetRect.width() - child.getMeasuredWidth()) >> 1;
                    mChildTmpRect.top = ((int) mTargetRect.height() - child.getMeasuredHeight()) >> 1;
                    mChildTmpRect.right = ((int) mTargetRect.width() + child.getMeasuredWidth()) >> 1;
                    mChildTmpRect.bottom = ((int) mTargetRect.height() + child.getMeasuredHeight()) >> 1;
                    mChildTmpRect.offset(mTargetRect.left, mTargetRect.top);
                    break;
            }
            //额外的xy偏移
            mChildTmpRect.offset((int) (density * lp.offsetX + 0.5f),
                    (int) (density * lp.offsetY + 0.5f));
            child.layout((int) mChildTmpRect.left, (int) mChildTmpRect.top, (int) mChildTmpRect.right,
                    (int) mChildTmpRect.bottom);
        }
    }

    private void horizontalChildPositionLayout(View child, RectF rect, int targetParentPosition) {
        switch (targetParentPosition) {
            case LayoutParams.PARENT_START:
                rect.left = mTargetRect.left;
                rect.right = rect.left + child.getMeasuredWidth();
                break;
            case LayoutParams.PARENT_CENTER:
                rect.left = (mTargetRect.width() - child.getMeasuredWidth()) / 2;
                rect.right = (mTargetRect.width() + child.getMeasuredWidth()) / 2;
                rect.offset(mTargetRect.left, 0);
                break;
            case LayoutParams.PARENT_END:
                rect.right = mTargetRect.right;
                rect.left = rect.right - child.getMeasuredWidth();
                break;
        }
    }

    private void verticalChildPositionLayout(View child, RectF rect, int targetParentPosition) {
        switch (targetParentPosition) {
            case LayoutParams.PARENT_START:
                rect.top = mTargetRect.top;
                rect.bottom = rect.top + child.getMeasuredHeight();
                break;
            case LayoutParams.PARENT_CENTER:
                rect.top = (mTargetRect.width() - child.getMeasuredHeight()) / 2;
                rect.bottom = (mTargetRect.width() + child.getMeasuredHeight()) / 2;
                rect.offset(0, mTargetRect.top);
                break;
            case LayoutParams.PARENT_END:
                rect.bottom = mTargetRect.bottom;
                rect.top = mTargetRect.bottom - child.getMeasuredHeight();
                break;
        }
    }

    private void resetOutPath() {
        resetPadding();
    }

    /**
     * 设置padding
     */
    private void resetPadding() {
        if (!ignoreRepadding) {
            if (mPadding != 0 && mPaddingLeft == 0) {
                mTargetRect.left -= mPadding;
            }
            if (mPadding != 0 && mPaddingTop == 0) {
                mTargetRect.top -= mPadding;
            }
            if (mPadding != 0 && mPaddingRight == 0) {
                mTargetRect.right += mPadding;
            }
            if (mPadding != 0 && mPaddingBottom == 0) {
                mTargetRect.bottom += mPadding;
            }
            if (mPaddingLeft != 0) {
                mTargetRect.left -= mPaddingLeft;
            }
            if (mPaddingTop != 0) {
                mTargetRect.top -= mPaddingTop;
            }
            if (mPaddingRight != 0) {
                mTargetRect.right += mPaddingRight;
            }
            if (mPaddingBottom != 0) {
                mTargetRect.bottom += mPaddingBottom;
            }
            ignoreRepadding = true;
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        final long drawingTime = getDrawingTime();
        try {
            View child;
            for (int i = 0; i < getChildCount(); i++) {
                child = getChildAt(i);
                drawChild(canvas, child, drawingTime);
            }
        } catch (NullPointerException e) {

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChangedHeight != 0) {
            mTargetRect.offset(0, mChangedHeight);
            mInitHeight = mInitHeight + mChangedHeight;
            mChangedHeight = 0;
        }
        mEraserBitmap.eraseColor(Color.TRANSPARENT);
        mEraserCanvas.drawColor(mFullingPaint.getColor());
        if (!mOverlayTarget) {
            switch (mStyle) {
                case Component.ROUNDRECT:
                    mEraserCanvas.drawRoundRect(mTargetRect, mCorner, mCorner, mEraser);
                    break;
                case Component.CIRCLE:
                    mEraserCanvas.drawCircle(mTargetRect.centerX(), mTargetRect.centerY(),
                            mTargetRect.width() / 2, mEraser);
                    break;
                default:
                    mEraserCanvas.drawRoundRect(mTargetRect, mCorner, mCorner, mEraser);
                    break;
            }
        }
        canvas.drawBitmap(mEraserBitmap, mOverlayRect.left, mOverlayRect.top, null);
    }

    public void setTargetRect(Rect rect) {
        mTargetRect.set(rect);
    }

    public void setFullingAlpha(int alpha) {
        mFullingPaint.setAlpha(alpha);
    }

    public void setFullingColor(int color) {
        mFullingPaint.setColor(color);
    }

    public void setHighTargetCorner(int corner) {
        this.mCorner = corner;
    }

    public void setHighTargetGraphStyle(int style) {
        this.mStyle = style;
    }

    public void setOverlayTarget(boolean b) {
        mOverlayTarget = b;
    }

    public void setPadding(int padding) {
        this.mPadding = padding;
    }

    public void setPaddingLeft(int paddingLeft) {
        this.mPaddingLeft = paddingLeft;
    }

    public void setPaddingTop(int paddingTop) {
        this.mPaddingTop = paddingTop;
    }

    public void setPaddingRight(int paddingRight) {
        this.mPaddingRight = paddingRight;
    }

    public void setPaddingBottom(int paddingBottom) {
        this.mPaddingBottom = paddingBottom;
    }

    static class LayoutParams extends ViewGroup.LayoutParams {

        public static final int ANCHOR_LEFT = 0x01;
        public static final int ANCHOR_TOP = 0x02;
        public static final int ANCHOR_RIGHT = 0x03;
        public static final int ANCHOR_BOTTOM = 0x04;
        public static final int ANCHOR_OVER = 0x05;

        public static final int PARENT_START = 0x10;
        public static final int PARENT_CENTER = 0x20;
        public static final int PARENT_END = 0x30;

        public int targetAnchor = ANCHOR_BOTTOM;
        public int targetParentPosition = PARENT_CENTER;
        public int offsetX = 0;
        public int offsetY = 0;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
