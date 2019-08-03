package com.blog.www.guideview;

import android.view.LayoutInflater;
import android.view.View;

/**
 * * 遮罩系统中相对于目标区域而绘制一些图片或者文字等view需要实现的接口. <br>
 *  * <br>
 *  * {@link #getView(LayoutInflater)} <br>
 *  * {@link #getAnchor()} <br>
 *  * {@link #getFitPosition()} <br>
 *  * {@link #getXOffset()} <br>
 *  * {@link #getYOffset()}
 *  * <br>
 *  * 具体创建遮罩的说明请参加{@link GuideBuilder}
 *  *
 *
 * Created by binIoter
 */

public interface Component {

  public final static int FIT_START = MaskView.LayoutParams.PARENT_START;

  public final static int FIT_END = MaskView.LayoutParams.PARENT_END;

  public final static int FIT_CENTER = MaskView.LayoutParams.PARENT_CENTER;

  public final static int ANCHOR_LEFT = MaskView.LayoutParams.ANCHOR_LEFT;

  public final static int ANCHOR_RIGHT = MaskView.LayoutParams.ANCHOR_RIGHT;

  public final static int ANCHOR_BOTTOM = MaskView.LayoutParams.ANCHOR_BOTTOM;

  public final static int ANCHOR_TOP = MaskView.LayoutParams.ANCHOR_TOP;

  public final static int ANCHOR_OVER = MaskView.LayoutParams.ANCHOR_OVER;

  /**
   * 圆角矩形&矩形
   */
  public final static int ROUNDRECT = 0;

  /**
   * 圆形
   */
  public final static int CIRCLE = 1;

  /**
   * 需要显示的view
   *
   * @param inflater use to inflate xml resource file
   * @return the component view
   */
  View getView(LayoutInflater inflater);

  /**
   * 相对目标View的锚点
   *
   * @return could be {@link #ANCHOR_LEFT}, {@link #ANCHOR_RIGHT},
   * {@link #ANCHOR_TOP}, {@link #ANCHOR_BOTTOM}, {@link #ANCHOR_OVER}
   */
  int getAnchor();

  /**
   * 相对目标View的对齐
   *
   * @return could be {@link #FIT_START}, {@link #FIT_END},
   * {@link #FIT_CENTER}
   */
  int getFitPosition();

  /**
   * 相对目标View的X轴位移，在计算锚点和对齐之后。
   *
   * @return X轴偏移量, 单位 dp
   */
  int getXOffset();

  /**
   * 相对目标View的Y轴位移，在计算锚点和对齐之后。
   *
   * @return Y轴偏移量，单位 dp
   */
  int getYOffset();
}
