package com.demo.dewdropdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;


public class DewdropView extends View{
    
    //View的宽和高
    private int width;
    private int height;
    
    //是否正在拖拽
    private boolean isDraging = false;
    
    //手指触摸位置
    private int touchPX;
    private int touchPY;
    
    //要绘制的图片资源
    private Resources mRes;
    private Drawable mDrawable;
    
    //View的位置描述：Left、Top、Right、Bottom 左 上 右 下
    private int l,t,r,b;
    
    //手机屏幕尺寸
    private int mScrrenWidth;
    private int mScrrenHeight;
    
    private MainActivity mActivity;
    private float tmpX; //点击后起始位置X
    private float tmpY; //点击后起始位置Y
    
    public DewdropView(MainActivity context) {
        super(context);
        this.mActivity = context;
        init(context);
    }
    
    //初始化
    private void init(Activity context){
        mRes = context.getResources();
        getScrrenSize(context);
    }
    
    //获得手机屏幕大小
    private void getScrrenSize(Activity context){
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        mScrrenWidth = dm.widthPixels;
        mScrrenHeight = dm.heightPixels;
    }
    
    //设置View的显示大小
    public void setSize(int w, int h){
        if(w > mScrrenWidth){
            w = mScrrenWidth;
        }
        if(h > mScrrenHeight){
            h = mScrrenHeight;
        }
        width = w;
        height = h;
        r = l + width;
        b = t + height;
        validPos();
    }
    
    //拖拽事件处理
    private void onDraging(){
        if(!isDraging){
            return;
        }
        l = touchPX - width/2;
        t = touchPY - height/2;
        r = l + width;
        b = t + height;
        
        validPos();
        layout(l, t, r, b);
        
    }
    
    //要显示的图片资源
    public void setImageResource(int id){
        if(mRes != null){
            mDrawable = mRes.getDrawable(id);
        }
    }
    
   //使得View一直显示在屏幕之内
    //后期需求更新 鸽子只能在左边轴上移动
    private void validPos(){
    	Log.i("TAG", "l   " + l);
    	Log.i("TAG", "t   " + t);
    	Log.i("TAG", "r   " + r);
    	Log.i("TAG", "b   " + b);
        if(l < 0){
            l = 0;
            r = width;
//        }else if(r > mScrrenWidth){
//            r = mScrrenWidth;
//            l = r - width;
//        }
          }else if(r > width){
          r = width;
          l = r - width;
      }
        
        if(t < 0){
            t = 0;
            b = height;
        }else if(b > mScrrenHeight){
            b = mScrrenHeight;
            t = b - height;
        }
    }
    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if(mDrawable == null) return;
        mDrawable.setBounds(0, 0, getWidth(), getHeight());
        mDrawable.draw(canvas);
    }

    // 传递进来的touch事件处理
    public void touch(MotionEvent event) {
        
        touchPX = (int)event.getX();
        touchPY = (int)event.getY();
        
        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
                if(isInView()){
                    isDraging = true;
                    tmpX = event.getX();
                    tmpY = event.getY();
                }
                break;
            case MotionEvent.ACTION_UP:
                isDraging = false;
                if(isInView()){
                    //如果起始位置和结束位置距离很小 表示点击
                    if (spacing(tmpX, tmpY, event.getX(), event.getY()) < 5) {
                    	 Log.i("TAG", "算是点中了鸽子");
                    	 //先来一个动画效果
                    	 Animation anim = AnimationUtils.loadAnimation(mActivity, R.anim.test);
                    	 DewdropView.this.startAnimation(anim);
                    	 anim.setAnimationListener(new Animation.AnimationListener() {
                 			
                 			@Override
                 			public void onAnimationStart(Animation animation) {
                 				
                 			}
                 			
                 			@Override
                 			public void onAnimationRepeat(Animation animation) {
                 				
                 			}
                 			
                 			@Override
                 			public void onAnimationEnd(Animation animation) {
                 				//在这里添加一个View
                 				ImageView imageView = new ImageView(mActivity);
                 				imageView.setImageResource(R.drawable.pigeon);
                 				mActivity.root_view.addView(imageView);
                 				RelativeLayout.LayoutParams lp = (LayoutParams) imageView.getLayoutParams();
                 				lp.width = 300;
                 				lp.height = 300;
                 				Log.i("TAG", "l   " + l);
                 		    	Log.i("TAG", "t   " + t);
                 		    	Log.i("TAG", "r   " + r);
                 		    	Log.i("TAG", "b   " + b);
                 		    	Log.i("TAG", "width   " + width);
                 		    	Log.i("TAG", "mScrrenWidth   " + mScrrenWidth);
                 				lp.setMargins(mScrrenWidth - width, t, 0, 0);
                 				//MainActivity.anim(mActivity);
                 				Intent intent = new Intent();
                 		        intent.setClass(mActivity, SlideSecondActivity.class);
                 		       mActivity.startActivity(intent);
                 		        //设置切换动画，从右边进入，左边退出
                 		       
                 		       mActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
                 			}
                 		});
                    	 
                         //回调主Activity方法
                         
					}
                }
                break;
            case MotionEvent.ACTION_MOVE:
                onDraging();
                break;
        }
    }
    
    //判断点击位置是否在此View之上
    private boolean isInView(){
        if(touchPX > l && touchPX < r && touchPY > t && touchPY < b){
            return true;
        }
        return false;
    }
    
    //两点之间的距离
	private float spacing(float x1, float y1, float x2, float y2) {
		float x = x1 - x2;
		float y = y1 - y2;
		return FloatMath.sqrt(x * x + y * y);
	}

}
