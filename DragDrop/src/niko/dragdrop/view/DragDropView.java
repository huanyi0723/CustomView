package niko.dragdrop.view;


import niko.dragdrop.MainActivity;
import niko.dragdrop.R;
import niko.dragdrop.SlideSecondActivity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class DragDropView extends FrameLayout {

	// 按下坐标 抬起坐标(相对屏幕而言)
	private float startX;
	private float startY;
	private float endX;
	private float endY;
	private MainActivity mainActivity;
	private int mScrrenWidth;
	private int mScrrenHeight;
	private int tmp;

	// 构造方法1
	public DragDropView(MainActivity mainActivity) {
		super(mainActivity);
		this.mainActivity = mainActivity;
	}

	// 构造方法2
	public DragDropView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// //构造方法3
	public DragDropView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	// 添加拖动View
	public void AddDraggableView(View draggableObject, int x, int y, int width, int height) {
		LayoutParams lpDraggableView = new LayoutParams(width, height);
		lpDraggableView.gravity = Gravity.TOP;
		lpDraggableView.leftMargin = x;
		lpDraggableView.topMargin = y;
		if (draggableObject instanceof ImageView) {
			ImageView ivDrag = (ImageView) draggableObject;
			ivDrag.setLayoutParams(lpDraggableView);
			ivDrag.setOnTouchListener(OnTouchToDrag);
			this.addView(ivDrag);
		}
	}

	// OnTouch中实现拖动效果
	private View.OnTouchListener OnTouchToDrag = new View.OnTouchListener() {

		@Override
		public boolean onTouch(final View v, MotionEvent event) {
			final FrameLayout.LayoutParams dragParam = (LayoutParams) v.getLayoutParams();
			DisplayMetrics dm = getResources().getDisplayMetrics();
			mScrrenWidth = dm.widthPixels;
			mScrrenHeight = dm.heightPixels;
			switch (event.getAction()) {
			// 1 按下
			case MotionEvent.ACTION_DOWN: {
				startX = event.getRawX();
				startY = event.getRawY();
				Log.i("TAG", "ACTION_DOWN X" + event.getRawX());
				Log.i("TAG", "ACTION_DOWN Y" + event.getRawY());
				break;
			}
			// 2 拖动
			case MotionEvent.ACTION_MOVE: {
				// dragParam.leftMargin = (int) event.getRawX() - (v.getWidth()
				// / 2);
				// dragParam.topMargin = (int) event.getRawY() -
				// (v.getHeight());
				// v.setLayoutParams(dragParam);
				// 拖动过程中对其限制 只可以Y轴方向上移动
				dragParam.leftMargin = 0;
				dragParam.topMargin = (int) event.getRawY() - (v.getHeight());
				if (dragParam.topMargin <= 0) {
					dragParam.topMargin = 0;
				}
				v.setLayoutParams(dragParam);
				break;
			}
			// 3 抬起
			case MotionEvent.ACTION_UP: {
				// dragParam.leftMargin = (int) event.getRawX() - (v.getWidth()
				// / 2);
				dragParam.leftMargin = 0;
				dragParam.topMargin = (int) event.getRawY() - (v.getHeight());
				if (dragParam.topMargin <= 0) {
					dragParam.topMargin = 0;
				}
				tmp = dragParam.topMargin;
				v.setLayoutParams(dragParam);
				Log.i("TAG", "ACTION_DOWN X" + event.getRawX());
				Log.i("TAG", "ACTION_DOWN Y" + event.getRawY());
				Log.i("TAG", "dragParam.topMargin" + dragParam.topMargin);

				// 在这里计算 如果点下和抬起距离很近 则表示点击该图像
				endX = event.getRawX();
				endY = event.getRawY();
				if (spacing(startX, startY, endX, endY) < 5) {
					Log.i("TAG", "算是点击中了");
					// 先来一个动画效果
					Animation anim = AnimationUtils.loadAnimation(mainActivity, R.anim.test);
					v.startAnimation(anim);
					anim.setAnimationListener(new Animation.AnimationListener() {

						@Override
						public void onAnimationStart(Animation animation) {

						}

						@Override
						public void onAnimationRepeat(Animation animation) {

						}

						@Override
						public void onAnimationEnd(Animation animation) {
							//重新添加一个
							ImageView ivTest = new ImageView(mainActivity);
							ivTest.setImageResource(R.drawable.pigeon);
							DragDropView.this.AddDraggableView(ivTest, mScrrenWidth - v.getWidth(), tmp, 300, 300);
							
							Intent intent = new Intent();
							intent.setClass(mainActivity, SlideSecondActivity.class);
							mainActivity.startActivity(intent);
							// 设置切换动画，从右边进入，左边退出
							mainActivity.overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
						}
					});
				}
				break;
			}

			}
			return true;
		}

		// 两点之间的距离
		private float spacing(float x1, float y1, float x2, float y2) {
			float x = x1 - x2;
			float y = y1 - y2;
			return FloatMath.sqrt(x * x + y * y);
		}
	};

}