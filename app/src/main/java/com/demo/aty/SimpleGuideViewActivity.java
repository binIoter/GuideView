package com.demo.aty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.LinearLayout;
import com.blog.www.guideview.Component;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.demo.component.MutiComponent;
import com.demo.component.SimpleComponent;
import com.demo.guide.R;

public class SimpleGuideViewActivity extends AppCompatActivity {

  private Button header_imgbtn;
  private LinearLayout ll_nearby;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_simple_guide_view);
    header_imgbtn = (Button) findViewById(R.id.header_imgbtn);
    ll_nearby = (LinearLayout) findViewById(R.id.ll_nearby);
    header_imgbtn.post(new Runnable() {
      @Override public void run() {
        showGuideView();
      }
    });
  }

  public void showGuideView() {
    GuideBuilder builder = new GuideBuilder();
    builder.setTargetView(header_imgbtn)
        .setAlpha(150)
        .setHighTargetCorner(20)
        .setHighTargetPadding(20)
        .setOverlayTarget(false)
        .setOutsideTouchable(false);
    builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
      @Override public void onShown() {
        // Toast.makeText(SimpleGuideViewActivity.this, "show", Toast.LENGTH_SHORT).show();

      }

      @Override public void onDismiss() {
        // Toast.makeText(SimpleGuideViewActivity.this, "dismiss", Toast.LENGTH_SHORT)
        // .show();
        showGuideView2();
      }
    });

    builder.addComponent(new SimpleComponent());
    Guide guide = builder.createGuide();
    guide.setShouldCheckLocInWindow(false);
    guide.show(SimpleGuideViewActivity.this);
  }

  public void showGuideView2() {
    final GuideBuilder builder1 = new GuideBuilder();
    builder1.setTargetView(ll_nearby)
        .setAlpha(150)
        .setHighTargetGraphStyle(Component.CIRCLE)
        .setOverlayTarget(false)
        .setExitAnimationId(android.R.anim.fade_out)
        .setOutsideTouchable(false);
    builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
      @Override public void onShown() {
        //  Toast.makeText(MutiGuideViewActivity.this, "show", Toast.LENGTH_SHORT).show();

      }

      @Override public void onDismiss() {
        //   Toast.makeText(MutiGuideViewActivity.this, "dismiss", Toast.LENGTH_SHORT).show();
      }
    });

    builder1.addComponent(new MutiComponent());
    Guide guide = builder1.createGuide();
    guide.setShouldCheckLocInWindow(false);
    guide.show(SimpleGuideViewActivity.this);
  }
}
