package com.bs;

import com.R;
import com.bs.frags.InteractiveFrag;
import com.bs.frags.LiveFrag;

/**
 * @author
 * @date 2017-5-3
 * 
 * 主页
 * */
public class LiveActivity extends BaseActivity {

	@Override
	public int getLayoutId() {
		
		return R.layout.activity_live;
	}

	@Override
	public void initData() {

		super.initData();

		// 加载直播fragment
		LiveFrag liveFrag = new LiveFrag();
		getSupportFragmentManager().beginTransaction().add(R.id.fl_root, liveFrag).commit();

        // 加载
		new InteractiveFrag().show(getSupportFragmentManager(), "InteractiveFrag");
	}
}