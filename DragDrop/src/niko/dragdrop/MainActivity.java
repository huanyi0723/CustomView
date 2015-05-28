package niko.dragdrop;

import niko.dragdrop.view.DragDropView;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.app.Activity;
import android.content.Context;

public class MainActivity extends Activity {
	
	public RelativeLayout llContainerMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		llContainerMain = (RelativeLayout) findViewById(R.id.llMainContainer);
		
		DragDropView dragDropView = new DragDropView( MainActivity.this);
		dragDropView.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		ImageView ivTest = new ImageView(getApplicationContext());
		ivTest.setImageResource(R.drawable.pigeon);
		//ivTest.setImageDrawable(getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher));
		dragDropView.AddDraggableView(ivTest, 0, 100, 300, 300);
		
		llContainerMain.addView(dragDropView);
	}

}
