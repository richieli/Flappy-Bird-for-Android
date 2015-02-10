package liqiang.flybird.view;

import liqiang.flybird.MainActivity;
import liqiang.flybird.config.Constants;
import liqiang.flybird.util.SoundPlayer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class BaseView extends SurfaceView implements Callback, Runnable {

	protected float scaleX;//背景图片缩放比例
	protected float scaleY;
	
	protected MainActivity mainActivity;
	protected SoundPlayer soundPlayer;
	
	protected Canvas canvas;//画布对象
	protected Paint paint;//画笔对象
	protected SurfaceHolder sfh;
	
	protected Thread thread;//绘画线程
	protected boolean threadFlag;//标记线程运行
	
	//构造函数
	public BaseView(Context context, SoundPlayer soundPlayer) {
		super(context);
		this.mainActivity = (MainActivity) context;
		this.soundPlayer = soundPlayer;
		this.sfh = this.getHolder();
		this.sfh.addCallback(this);
		this.paint = new Paint();
	}

	//线程运行的方法
	@Override
	public void run() {}
	
	//初始化图片资源
	public void initBitmap() {}
	
	//释放图片资源
	public void release() {}
	
	//绘图方法
	public void drawSelf() {}

	//视图改变的方法
	@Override
	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {}

	//视图创建的方法
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		Constants.SCREEN_WIDTH = this.getWidth();
		Constants.SCREEN_HEIGHT = this.getHeight();
		this.threadFlag = true;
	}

	//视图销毁的方法
	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		this.threadFlag = false;
	}
	
	public void setThreadFlag(boolean threadFlag) {
		this.threadFlag = threadFlag;
	}

}
