package com.demo.aty;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.demo.component.SimpleComponent;
import com.demo.guide.R;

public class SimpleGuideViewActivity extends AppCompatActivity {

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_guide_view);
        button = (Button) findViewById(R.id.btn);
        button.post(new Runnable() {
            @Override
            public void run() {
                showGuideView();
            }
        });
    }

    public void showGuideView() {
        GuideBuilder builder = new GuideBuilder();
        builder.setTargetView(button)
               .setAlpha(150)
               .setOverlayTarget(false)
               .setOutsideTouchable(false);
        builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
            @Override
            public void onShown() {
                // Toast.makeText(SimpleGuideViewActivity.this, "show", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onDismiss() {
                // Toast.makeText(SimpleGuideViewActivity.this, "dismiss", Toast.LENGTH_SHORT)
                // .show();
            }
        });

        builder.addComponent(new SimpleComponent());
        Guide guide = builder.createGuide();
        guide.setShouldCheckLocInWindow(false);
        guide.show(SimpleGuideViewActivity.this);
    }

}
