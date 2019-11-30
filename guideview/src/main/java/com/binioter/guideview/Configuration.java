package com.binioter.guideview;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;

/**
 * 遮罩系统创建时配置参数的封装 <br/>
 * Created by binIoter
 */

class Configuration implements Parcelable {

  /**
   * 需要被找的View
   */
  View mTargetView = null;

  /**
   * 高亮区域的padding
   */
  int mPadding = 0;
  /**
   * 高亮区域的左侧padding
   */
  int mPaddingLeft = 0;
  /**
   * 高亮区域的顶部padding
   */
  int mPaddingTop = 0;
  /**
   * 高亮区域的右侧padding
   */
  int mPaddingRight = 0;
  /**
   * 高亮区域的底部padding
   */
  int mPaddingBottom = 0;

  /**
   * 是否可以透过蒙层点击，默认不可以
   */
  boolean mOutsideTouchable = false;

  /**
   * 遮罩透明度
   */
  int mAlpha = 255;

  /**
   * 遮罩覆盖区域控件Id
   * <p/>
   * 该控件的大小既该导航页面的大小
   */
  int mFullingViewId = -1;

  /**
   * 目标控件Id
   */
  int mTargetViewId = -1;

  /**
   * 高亮区域的圆角大小
   */
  int mCorner = 0;

  /**
   * 高亮区域的图形样式，默认为矩形
   */
  int mGraphStyle = Component.ROUNDRECT;

  /**
   * 遮罩背景颜色id
   */
  int mFullingColorId = android.R.color.black;

  /**
   * 是否在点击的时候自动退出导航
   */
  boolean mAutoDismiss = true;

  /**
   * 是否覆盖目标控件
   */
  boolean mOverlayTarget = false;

  boolean mShowCloseButton = false;

  int mEnterAnimationId = -1;

  int mExitAnimationId = -1;

  @Override
  public int describeContents() {
    return 0;
  }

  @Override
  public void writeToParcel(Parcel dest, int flags) {
    dest.writeInt(mAlpha);
    dest.writeInt(mFullingViewId);
    dest.writeInt(mTargetViewId);
    dest.writeInt(mFullingColorId);
    dest.writeInt(mCorner);
    dest.writeInt(mPadding);
    dest.writeInt(mPaddingLeft);
    dest.writeInt(mPaddingTop);
    dest.writeInt(mPaddingRight);
    dest.writeInt(mPaddingBottom);
    dest.writeInt(mGraphStyle);
    dest.writeByte((byte) (mAutoDismiss ? 1 : 0));
    dest.writeByte((byte) (mOverlayTarget ? 1 : 0));
  }

  public static final Creator<Configuration> CREATOR = new Creator<Configuration>() {
    @Override
    public Configuration createFromParcel(Parcel source) {
      Configuration conf = new Configuration();
      conf.mAlpha = source.readInt();
      conf.mFullingViewId = source.readInt();
      conf.mTargetViewId = source.readInt();
      conf.mFullingColorId = source.readInt();
      conf.mCorner = source.readInt();
      conf.mPadding = source.readInt();
      conf.mPaddingLeft = source.readInt();
      conf.mPaddingTop = source.readInt();
      conf.mPaddingRight = source.readInt();
      conf.mPaddingBottom = source.readInt();
      conf.mGraphStyle = source.readInt();
      conf.mAutoDismiss = source.readByte() == 1;
      conf.mOverlayTarget = source.readByte() == 1;
      return conf;
    }

    @Override
    public Configuration[] newArray(int size) {
      return new Configuration[size];
    }
  };
}
