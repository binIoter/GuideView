package com.demo.aty;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import com.demo.guide.R;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ListView listView;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listView = (ListView) findViewById(R.id.lv);
        initListData();
        listView.setAdapter(new MyAdapter(this, mList));
    }

    private void initListData() {
        mList = new ArrayList<String>();
        mList.add("SimpleGuideViewActivity");
        mList.add("MutiGuideViewActivity");
    }

    private void gotoOthersActivity(int i) {
        switch (i) {
            case 0:
                startActivity(new Intent(MainActivity.this, SimpleGuideViewActivity.class));
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, MutiGuideViewActivity.class));
                break;
        }
    }

    class MyAdapter extends BaseAdapter {

        List<String> mList;
        Context context;

        public MyAdapter() {
        }

        public MyAdapter(Context context, List<String> list) {
            this.context = context;
            this.mList = list;
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int i) {
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int i, View view, ViewGroup viewGroup) {
            viewGroup = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.item, null);
            Button btn = (Button) viewGroup.findViewById(R.id.btn);
            btn.setText(mList.get(i));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    gotoOthersActivity(i);
                }
            });
            return viewGroup;
        }
    }
}
