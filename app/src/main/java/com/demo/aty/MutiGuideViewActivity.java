package com.demo.aty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.demo.component.MutiComponent;
import com.demo.component.SimpleComponent;
import com.demo.guide.R;

public class MutiGuideViewActivity extends AppCompatActivity {

    private Button button1, button2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muti_guide_view);
        button1 = (Button) findViewById(R.id.btn1);
        button2 = (Button) findViewById(R.id.btn2);
        button1.post(new Runnable() {
            @Override
            public void run() {
                showGuideView();
            }
        });
    }

    public void showGuideView() {
        final GuideBuilder builder1 = new GuideBuilder();
        builder1.setTargetView(button1)
                .setAlpha(150)
                .setOverlayTarget(true)
                .setOutsideTouchable(false);
        builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
                //  Toast.makeText(MutiGuideViewActivity.this, "show", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDismiss() {
                button2.post(new Runnable() {
                    @Override
                    public void run() {
                        showGuideView2();
                    }
                });
                // Toast.makeText(MutiGuideViewActivity.this, "dismiss", Toast.LENGTH_SHORT).show();
            }
        });

        builder1.addComponent(new SimpleComponent());
        Guide guide = builder1.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(MutiGuideViewActivity.this);
    }

    public void showGuideView2() {
        final GuideBuilder builder1 = new GuideBuilder();
        builder1.setTargetView(button2)
                .setAlpha(150)
                .setOverlayTarget(false)
                .setOutsideTouchable(false);
        builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
              //  Toast.makeText(MutiGuideViewActivity.this, "show", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDismiss() {
             //   Toast.makeText(MutiGuideViewActivity.this, "dismiss", Toast.LENGTH_SHORT).show();
            }
        });

        builder1.addComponent(new MutiComponent());
        Guide guide = builder1.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(MutiGuideViewActivity.this);
    }
}
