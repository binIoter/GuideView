package com.demo.aty;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.demo.guide.R;

public class HomeActivity extends Activity implements View.OnClickListener {
  private Button mBtnAty, mBtnFrag, mBtnList, mBtnView, mBtnFragView, mBtnMore;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    mBtnAty = (Button) findViewById(R.id.btn_aty);
    mBtnFrag = (Button) findViewById(R.id.btn_frag);
    mBtnFragView = (Button) findViewById(R.id.btn_frag_view);
    mBtnList = (Button) findViewById(R.id.btn_list);
    mBtnMore = (Button) findViewById(R.id.btn_more);
    mBtnView = (Button) findViewById(R.id.btn_view);
    mBtnAty.setOnClickListener(this);
    mBtnFrag.setOnClickListener(this);
    mBtnFragView.setOnClickListener(this);
    mBtnList.setOnClickListener(this);
    mBtnMore.setOnClickListener(this);
    mBtnView.setOnClickListener(this);
  }

  @Override public void onClick(View view) {
    switch (view.getId()) {
      case R.id.btn_aty:
        startActivity(new Intent(HomeActivity.this, FullActivity.class));
        break;
      case R.id.btn_frag:
        startActivity(new Intent(HomeActivity.this, FragActivity.class).putExtra("fragmentId",0));
        break;
      case R.id.btn_frag_view:
        startActivity(new Intent(HomeActivity.this, FragActivity.class).putExtra("fragmentId",1));
        break;
      case R.id.btn_list:
        startActivity(new Intent(HomeActivity.this, MyListActivity.class));
        break;
      case R.id.btn_more:
        startActivity(new Intent(HomeActivity.this, SimpleGuideViewActivity.class));
        break;
      case R.id.btn_view:
        startActivity(new Intent(HomeActivity.this, ViewActivity.class));
        break;
    }
  }
}
