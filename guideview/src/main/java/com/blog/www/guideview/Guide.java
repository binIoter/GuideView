package com.blog.www.guideview;

import android.app.Activity;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * 遮罩系统的封装 <br>
 * 外部需要调用{@link com.blog.www.guideview.GuideBuilder}来创建该实例，实例创建后调用
 * {@link #show(Activity)} 控制显示； 调用 {@link #dismiss()}让遮罩系统消失。 <br>
 *
 * Created by binIoter
 */
public class Guide implements View.OnKeyListener, View.OnClickListener {
  /**
   * Cannot initialize out of package <font
   * color=red>包内才可见，外部使用时必须调用GuideBuilder来创建.</font>
   *
   * @see com.blog.www.guideview.GuideBuilder
   */
  Guide() {
  }

  private Configuration mConfiguration;
  private MaskView mMaskView;
  private Component[] mComponents;
  // 根据locInwindow定位后，是否需要判断loc值非0
  private boolean mShouldCheckLocInWindow = true;
  private GuideBuilder.OnVisibilityChangedListener mOnVisibilityChangedListener;

  void setConfiguration(Configuration configuration) {
    mConfiguration = configuration;
  }

  void setComponents(Component[] components) {
    mComponents = components;
  }

  void setCallback(GuideBuilder.OnVisibilityChangedListener listener) {
    mOnVisibilityChangedListener = listener;
  }

  /**
   * 显示该遮罩, <br>
   * 外部借助{@link com.blog.www.guideview.GuideBuilder}
   * 创建好一个Guide实例后，使用该实例调用本函数遮罩才会显示
   *
   * @param activity 目标Activity
   */
  public void show(Activity activity) {
    if (mMaskView == null) {
      mMaskView = onCreateView(activity);
    }
    ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
    if (mMaskView.getParent() == null) {
      content.addView(mMaskView);
      if (mConfiguration.mEnterAnimationId != -1) {
        Animation anim = AnimationUtils.loadAnimation(activity, mConfiguration.mEnterAnimationId);
        assert anim != null;
        anim.setAnimationListener(new Animation.AnimationListener() {
          @Override public void onAnimationStart(Animation animation) {

          }

          @Override public void onAnimationEnd(Animation animation) {
            if (mOnVisibilityChangedListener != null) {
              mOnVisibilityChangedListener.onShown();
            }
          }

          @Override public void onAnimationRepeat(Animation animation) {

          }
        });
        mMaskView.startAnimation(anim);
      } else {
        if (mOnVisibilityChangedListener != null) {
          mOnVisibilityChangedListener.onShown();
        }
      }
    }
  }

  /**
   * 隐藏该遮罩并回收资源相关
   */
  public void dismiss() {
    if (mMaskView == null) {
      return;
    }
    final ViewGroup vp = (ViewGroup) mMaskView.getParent();
    if (vp == null) {
      return;
    }
    if (mConfiguration.mExitAnimationId != -1) {
      // mMaskView may leak if context is null
      Context context = mMaskView.getContext();
      assert context != null;

      Animation anim = AnimationUtils.loadAnimation(context, mConfiguration.mExitAnimationId);
      assert anim != null;
      anim.setAnimationListener(new Animation.AnimationListener() {
        @Override public void onAnimationStart(Animation animation) {

        }

        @Override public void onAnimationEnd(Animation animation) {
          vp.removeView(mMaskView);
          if (mOnVisibilityChangedListener != null) {
            mOnVisibilityChangedListener.onDismiss();
          }
          onDestroy();
        }

        @Override public void onAnimationRepeat(Animation animation) {

        }
      });
      mMaskView.startAnimation(anim);
    } else {
      vp.removeView(mMaskView);
      if (mOnVisibilityChangedListener != null) {
        mOnVisibilityChangedListener.onDismiss();
      }
      onDestroy();
    }
  }

  /**
   * 根据locInwindow定位后，是否需要判断loc值非0
   */
  public void setShouldCheckLocInWindow(boolean set) {
    mShouldCheckLocInWindow = set;
  }

  private MaskView onCreateView(Activity activity) {
    ViewGroup content = (ViewGroup) activity.findViewById(android.R.id.content);
    // ViewGroup content = (ViewGroup) activity.getWindow().getDecorView();
    MaskView maskView = new MaskView(activity);
    maskView.setFullingColor(activity.getResources().getColor(mConfiguration.mFullingColorId));
    maskView.setFullingAlpha(mConfiguration.mAlpha);
    maskView.setHighTargetCorner(mConfiguration.mCorner);
    maskView.setPadding(mConfiguration.mPadding);
    maskView.setPaddingLeft(mConfiguration.mPaddingLeft);
    maskView.setPaddingTop(mConfiguration.mPaddingTop);
    maskView.setPaddingRight(mConfiguration.mPaddingRight);
    maskView.setPaddingBottom(mConfiguration.mPaddingBottom);
    maskView.setHighTargetGraphStyle(mConfiguration.mGraphStyle);
    maskView.setOverlayTarget(mConfiguration.mOverlayTarget);
    maskView.setOnKeyListener(this);

    // For removing the height of status bar we need the root content view's
    // location on screen
    int parentX = 0;
    int parentY = 0;
    final int[] loc = new int[2];
    content.getLocationInWindow(loc);
    parentY = loc[1];//通知栏的高度
    if (mShouldCheckLocInWindow && parentY == 0) {
      Class<?> localClass;
      try {
        localClass = Class.forName("com.android.internal.R$dimen");
        Object localObject = localClass.newInstance();
        int i5 =
            Integer.parseInt(localClass.getField("status_bar_height").get(localObject).toString());
        parentY = activity.getResources().getDimensionPixelSize(i5);
      } catch (ClassNotFoundException e) {
        e.printStackTrace();
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      } catch (InstantiationException e) {
        e.printStackTrace();
      } catch (NumberFormatException e) {
        e.printStackTrace();
      } catch (IllegalArgumentException e) {
        e.printStackTrace();
      } catch (SecurityException e) {
        e.printStackTrace();
      } catch (NoSuchFieldException e) {
        e.printStackTrace();
      }
    }
    // if (content != null) {
    // int[] loc = new int[2];
    // content.getLocationInWindow(loc);
    // parentX = loc[0];
    // parentY = loc[1];
    // }

    if (mConfiguration.mTargetView != null) {
      maskView.setTargetRect(Common.getViewAbsRect(mConfiguration.mTargetView, parentX, parentY));
    } else {
      // Gets the target view's abs rect
      View target = activity.findViewById(mConfiguration.mTargetViewId);
      if (target != null) {
        maskView.setTargetRect(Common.getViewAbsRect(target, parentX, parentY));
      }
    }

    // Gets the fulling view's abs rect
    View fulling = activity.findViewById(mConfiguration.mFullingViewId);
    if (fulling != null) {
      maskView.setFullingRect(Common.getViewAbsRect(fulling, parentX, parentY));
    }

    if (mConfiguration.mOutsideTouchable) {
      maskView.setClickable(false);
    } else {
      maskView.setOnClickListener(this);
    }

    // Adds the components to the mask view.
    for (Component c : mComponents) {
      maskView.addView(Common.componentToView(activity.getLayoutInflater(), c));
    }

    return maskView;
  }

  private void onDestroy() {
    mConfiguration = null;
    mComponents = null;
    mOnVisibilityChangedListener = null;
    mMaskView.removeAllViews();
    mMaskView = null;
  }

  @Override public boolean onKey(View v, int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
      if (mConfiguration != null && mConfiguration.mAutoDismiss) {
        dismiss();
        return true;
      } else {
        return false;
      }
    }
    return false;
  }

  @Override public void onClick(View v) {
    if (mConfiguration != null && mConfiguration.mAutoDismiss) {
      dismiss();
    }
  }
}
