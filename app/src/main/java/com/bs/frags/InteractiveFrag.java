package com.bs.frags;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.R;


/**
 * 观众功能交互页面, 滑动隐藏效果
 * 
 * @author
 * @date 2017-5-3
 */
public class InteractiveFrag extends DialogFragment{

	public View view;
	public Context myContext;
	private ViewPager vp_interactive;
	private LayerFrag layerFrag;
	
	@Override
	public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

		view = inflater.inflate(R.layout.frag_interactive, null);
		
		// 初始化
		initView();
		initData();
		initListener();
		return view;
	}
	
	/**
	 * 初始化View
	 * */
	public void initView() {
		
		vp_interactive = (ViewPager)view.findViewById(R.id.vp_interactive);
	}

	/**
	 * 初始化数据
	 * */
	public void initData() {
		
        // EmptyFrag：什么都没有
        // LayerFrag：交互界面
        // 这样就达到了滑动隐藏交互的需求
		vp_interactive.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
			
			@Override
			public int getCount() {

				return 2;
			}
			
			@Override
			public Fragment getItem(int position) {

				if (position == 0){
					
					return new EmptyFrag(); // 返回空界面的fragment
				}else if (position == 1){
					
					return layerFrag = new LayerFrag(); // 返回交互界面的frag
				}else{ // 设置默认
					
					return new EmptyFrag();
				}
			}
		});
		// 设置默认显示交互界面
		vp_interactive.setCurrentItem(1);
		
		// 同时将界面改为resize已达到软键盘弹出时Fragment不会跟随移动
		getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
	}
	
	/**
	 * 初始化监听
	 * */
	public void initListener() {
		
		vp_interactive.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				
				if(position == 0){

//					layerFrag.hideKeyboard();
				}
			}
			
			@Override
			public void onPageScrolled(int position, float arg1, int arg2) {
			}
			
			@Override
			public void onPageScrollStateChanged(int position) {
			}
		});
	}
	
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
    	
    	// 设置DialogFragment的样式，这里的代码最好还是用我的，大家不要改动
        Dialog dialog = new Dialog(getActivity(), R.style.MainDialog){
           
        	@Override
            public void onBackPressed() {
                super.onBackPressed();
            
                getActivity().finish();
            }
        };
        return dialog;
    }
}