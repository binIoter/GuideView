package com.demo.aty;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.blog.www.guideview.Guide;
import com.blog.www.guideview.GuideBuilder;
import com.demo.component.MutiComponent;
import com.demo.guide.R;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间: 2016/08/24 16:58 <br>
 * 作者: zhangbin <br>
 * 描述:
 */
public class MyListActivity extends Activity {
  ListView listView;
  BaseAdapter adapter;
  static List<String> arrayList = new ArrayList<>();

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.my_list_layout);
    for (int i = 1; i < 200; i++) {
      arrayList.add("第" + i + "行");
    }
    listView = (ListView) findViewById(R.id.list);
    adapter = new MyAdapter(this);
    listView.setAdapter(adapter);
  }

  private static class MyAdapter extends BaseAdapter {
    private Context mContext;
    private int showTimes = 0;

    public MyAdapter(Context context) {
      this.mContext = context;
    }

    @Override public int getCount() {
      return arrayList.size();
    }

    @Override public Object getItem(int i) {
      return arrayList.get(i);
    }

    @Override public long getItemId(int i) {
      return i;
    }

    @Override public View getView(int i, View view, ViewGroup viewGroup) {
      ViewHolder holder;
      if (view == null) {
        view = LayoutInflater.from(mContext).inflate(R.layout.item, viewGroup, false);
        holder = new ViewHolder();
        holder.btn = (Button) view.findViewById(R.id.btn);
        view.setTag(holder);
      } else {
        holder = (ViewHolder) view.getTag();
      }
      holder.btn.setText(arrayList.get(i));
      if (i == 0 && showTimes == 0) {
        final View finalView = view;
        view.post(new Runnable() {
          @Override public void run() {
            showGuideView(finalView);
          }
        });
      }
      return view;
    }

    public void showGuideView(View targetView) {
      showTimes++;
      GuideBuilder builder = new GuideBuilder();
      builder.setTargetView(targetView)
          .setFullingViewId(R.id.ll_view_group)
          .setAlpha(150)
          .setHighTargetCorner(20)
          .setHighTargetPadding(10)
          .setOverlayTarget(false)
          .setOutsideTouchable(false);
      builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
        @Override public void onShown() {
        }

        @Override public void onDismiss() {
        }
      });

      builder.addComponent(new MutiComponent());
      Guide guide = builder.createGuide();
      guide.setShouldCheckLocInWindow(true);
      guide.show((Activity) mContext);
    }

    private static class ViewHolder {
      private Button btn;
    }
  }
}
