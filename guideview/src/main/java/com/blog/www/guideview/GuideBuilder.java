package com.blog.www.guideview;

import android.support.annotation.AnimatorRes;
import android.support.annotation.IdRes;
import android.support.annotation.IntRange;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * <h1>遮罩系统构建器
 * <p>
 * 本系统能够快速的为一个Activity里的任何一个View控件创建一个遮罩式的引导页。
 * </p>
 * <h3>工作原理</h3>
 * <p>
 * 首先它需要一个目标View或者它的id,我们通过findViewById来得到这个View，计算它在屏幕上的区域targetRect,参见
 * {@link #setTargetViewId(int)}与{@link #setTargetView(View)}通过这个区域，
 * 开始绘制一个覆盖整个Activity的遮罩，可以定义蒙板的颜色{@link #setFullingColorId(int)}和透明度
 * {@link #setAlpha(int)}。然而目标View的区域不会被绘制从而实现高亮的效果。
 * </p>
 * <p>
 * 接下来是在相对于这个targetRect的区域绘制一些图片或者文字。我们把这样一张图片或者文字抽象成一个Component接口
 * {@link Component},设置文字或者图片等
 * {@link Component#getView(android.view.LayoutInflater)}
 * . 所有的图片文字都是相对于targetRect来定义的。可以设定额外的x，
 * {@link Component#getXOffset()} ;y偏移量,
 * {@link Component#getYOffset()}。
 * </p>
 * <p>
 * 可以对遮罩系统设置可见状态的发生变化时的监听回调
 * {@link #setOnVisibilityChangedListener(OnVisibilityChangedListener)}
 * </p>
 * <p>
 * 可以对遮罩系统设置开始和结束时的动画效果 {@link #setEnterAnimationId(int)}
 * {@link #setExitAnimationId(int)}
 * </p>
 * </p>
 *
 * Created by binIoter
 **/

public class GuideBuilder {

    public enum SlideState {
        UP,DOWN;
    }

    private Configuration mConfiguration;

    /**
     * Builder被创建后，不允许在对它进行更改
     */
    private boolean mBuilt;

    private List<Component> mComponents = new ArrayList<Component>();
    private OnVisibilityChangedListener mOnVisibilityChangedListener;
    private OnSlideListener mOnSlideListener;

    /**
     * 构造函数
     */
    public GuideBuilder() {
        mConfiguration = new Configuration();
    }

    /**
     * 设置蒙板透明度
     *
     * @param alpha [0-255] 0 表示完全透明，255表示不透明
     * @return GuideBuilder
     */
    public GuideBuilder setAlpha(@IntRange(from = 0, to = 255)  int alpha) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        } else if (alpha < 0 || alpha > 255) {
            alpha = 0;
        }
        mConfiguration.mAlpha = alpha;
        return this;
    }

    /**
     * 设置目标view
     */
    public GuideBuilder setTargetView(View v) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        }
        mConfiguration.mTargetView = v;
        return this;
    }

    /**
     * 设置目标View的id
     *
     * @param id 目标View的id
     * @return GuideBuilder
     */
    public GuideBuilder setTargetViewId(@IdRes int id) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        }
        mConfiguration.mTargetViewId = id;
        return this;
    }

    /**
     * 设置高亮区域的圆角大小
     *
     * @return GuideBuilder
     */
    public GuideBuilder setHighTargetCorner(int corner) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        } else if (corner < 0) {
            mConfiguration.mCorner = 0;
        }
        mConfiguration.mCorner = corner;
        return this;
    }

    /**
     * 设置高亮区域的图形样式
     *
     * @return GuideBuilder
     */
    public GuideBuilder setHighTargetGraphStyle(int style) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        }
        mConfiguration.mGraphStyle = style;
        return this;
    }

    /**
     * 设置蒙板颜色的资源id
     *
     * @param id 资源id
     * @return GuideBuilder
     */
    public GuideBuilder setFullingColorId(@IdRes int id) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        }
        mConfiguration.mFullingColorId = id;
        return this;
    }

    /**
     * 是否在点击的时候自动退出蒙板
     *
     * @param b true if needed
     * @return GuideBuilder
     */
    public GuideBuilder setAutoDismiss(boolean b) {
        if (mBuilt) {
            throw new BuildException("Already created, rebuild a new one.");
        }
        mConfiguration.mAutoDismiss = b;
        return this;
    }

    /**
     * 是否覆盖目标
     *
     * @param b true 遮罩将会覆盖整个屏幕
     * @return GuideBuilder
     */
    public GuideBuilder setOverlayTarget(boolean b) {
        if (mBuilt) {
            throw new BuildException("Already created, rebuild a new one.");
        }
        mConfiguration.mOverlayTarget = b;
        return this;
    }

    /**
     * 设置进入动画
     *
     * @param id 进入动画的id
     * @return GuideBuilder
     */
    public GuideBuilder setEnterAnimationId(@AnimatorRes int id) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        }
        mConfiguration.mEnterAnimationId = id;
        return this;
    }

    /**
     * 设置退出动画
     *
     * @param id 退出动画的id
     * @return GuideBuilder
     */
    public GuideBuilder setExitAnimationId(@AnimatorRes int id) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        }
        mConfiguration.mExitAnimationId = id;
        return this;
    }

    /**
     * 添加一个控件
     *
     * @param component 被添加的控件
     * @return GuideBuilder
     */
    public GuideBuilder addComponent(Component component) {
        if (mBuilt) {
            throw new BuildException("Already created, rebuild a new one.");
        }
        mComponents.add(component);
        return this;
    }

    /**
     * 设置遮罩可见状态变化时的监听回调
     */
    public GuideBuilder setOnVisibilityChangedListener(
            OnVisibilityChangedListener onVisibilityChangedListener) {
        if (mBuilt) {
            throw new BuildException("Already created, rebuild a new one.");
        }
        mOnVisibilityChangedListener = onVisibilityChangedListener;
        return this;
    }

    /**
     * 设置手势滑动的监听回调
     */
    public GuideBuilder setOnSlideListener(
            OnSlideListener onSlideListener) {
        if (mBuilt) {
            throw new BuildException("Already created, rebuild a new one.");
        }
        mOnSlideListener = onSlideListener;
        return this;
    }

    /**
     * 设置遮罩系统是否可点击并处理点击事件
     *
     * @param touchable true 遮罩不可点击，处于不可点击状态 false 可点击，遮罩自己可以处理自身点击事件
     */
    public GuideBuilder setOutsideTouchable(boolean touchable) {
        mConfiguration.mOutsideTouchable = touchable;
        return this;
    }

    /**
     * 设置高亮区域的padding
     *
     * @return GuideBuilder
     */
    public GuideBuilder setHighTargetPadding(int padding) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        } else if (padding < 0) {
            mConfiguration.mPadding = 0;
        }
        mConfiguration.mPadding = padding;
        return this;
    }

    /**
     * 设置高亮区域的左侧padding
     *
     * @return GuideBuilder
     */
    public GuideBuilder setHighTargetPaddingLeft(int padding) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        } else if (padding < 0) {
            mConfiguration.mPaddingLeft = 0;
        }
        mConfiguration.mPaddingLeft = padding;
        return this;
    }

    /**
     * 设置高亮区域的顶部padding
     *
     * @return GuideBuilder
     */
    public GuideBuilder setHighTargetPaddingTop(int padding) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        } else if (padding < 0) {
            mConfiguration.mPaddingTop = 0;
        }
        mConfiguration.mPaddingTop = padding;
        return this;
    }

    /**
     * 设置高亮区域的右侧padding
     *
     * @return GuideBuilder
     */
    public GuideBuilder setHighTargetPaddingRight(int padding) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        } else if (padding < 0) {
            mConfiguration.mPaddingRight = 0;
        }
        mConfiguration.mPaddingRight = padding;
        return this;
    }

    /**
     * 设置高亮区域的底部padding
     *
     * @return GuideBuilder
     */
    public GuideBuilder setHighTargetPaddingBottom(int padding) {
        if (mBuilt) {
            throw new BuildException("Already created. rebuild a new one.");
        } else if (padding < 0) {
            mConfiguration.mPaddingBottom = 0;
        }
        mConfiguration.mPaddingBottom = padding;
        return this;
    }

    /**
     * 创建Guide，非Fragment版本
     *
     * @return Guide
     */
    public Guide createGuide() {
        Guide guide = new Guide();
        Component[] components = new Component[mComponents.size()];
        guide.setComponents(mComponents.toArray(components));
        guide.setConfiguration(mConfiguration);
        guide.setCallback(mOnVisibilityChangedListener);
        guide.setOnSlideListener(mOnSlideListener);
        mComponents = null;
        mConfiguration = null;
        mOnVisibilityChangedListener = null;
        mBuilt = true;
        return guide;
    }

    /**
     * 手势滑动监听
     */
    public static interface OnSlideListener {

        void onSlideListener(SlideState state);
    }

    /**
     * 遮罩可见发生变化时的事件监听
     */
    public static interface OnVisibilityChangedListener {

        void onShown();

        void onDismiss();
    }
}
