package com.demo.dewdropdemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class MainActivity extends Activity {

	private DewdropView mView = null;
	private final int mViewWidth = 300;
	private final int mViewHeight = 300;
	public RelativeLayout root_view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.activity_main);

		root_view = (RelativeLayout) findViewById(R.id.root_view);
		mView = new DewdropView(MainActivity.this);
		root_view.addView(mView);
		mView.setImageResource(R.drawable.pigeon);
		RelativeLayout.LayoutParams lp = (LayoutParams) mView.getLayoutParams();
		lp.width = mViewWidth;
		lp.height = mViewHeight;
		mView.setSize(mViewWidth, mViewHeight);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (mView != null) {
			mView.touch(event);
		}
		return super.onTouchEvent(event);
	}

	public static void anim(final Context context) {
		Intent intent = new Intent();
        intent.setClass(context, SlideSecondActivity.class);
        context.startActivity(intent);
        //设置切换动画，从右边进入，左边退出
       //overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

	}

}
