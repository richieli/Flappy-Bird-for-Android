package liqiang.flybird.view;

import liqiang.flybird.R;
import liqiang.flybird.config.Config;
import liqiang.flybird.config.Constants;
import liqiang.flybird.util.SoundPlayer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.os.Looper;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class LoadingView extends BaseView {
	
	private Bitmap bgImg;
	private Bitmap logoImg;
	private Bitmap textImg;
	
	//private float bgImgX;//图片坐标
	//private float bgImgY;
	
	private float logoImgX;
	private float logoImgY;
	
	private float textImgX;
	private float textImgY;
	
	private String author = "仿写者：李强";//绘制文字区域
	private Rect rect;
	
	private float strWidth;
	private float strHeight;
	
	private float textX;
	private float textY;
	
	public LoadingView(Context context, SoundPlayer soundPlayer) {
		super(context, soundPlayer);
		rect = new Rect();
		this.thread = new Thread(this);
	}
	
	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		super.surfaceCreated(arg0);
		initBitmap();
		if(this.thread.isAlive()) {
			this.thread.start();
		} else {
			this.thread = new Thread(this);
			this.thread.start();
		}
	}
	
	@Override
	public void initBitmap() {
		this.bgImg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		this.logoImg = BitmapFactory.decodeResource(getResources(), R.drawable.logo);
		this.textImg = BitmapFactory.decodeResource(getResources(), R.drawable.text_logo);
		
		this.scaleX = Constants.SCREEN_WIDTH / this.bgImg.getWidth();
		this.scaleY = Constants.SCREEN_HEIGHT / this.bgImg.getHeight();
		
		this.textImgX = (Constants.SCREEN_WIDTH - this.textImg.getWidth()) / 2;
		this.textImgY = Constants.SCREEN_HEIGHT / 2 - this.textImg.getHeight() * 2;
		
		this.logoImgX = (Constants.SCREEN_WIDTH - this.logoImg.getWidth()) / 2;
		this.logoImgY = Constants.SCREEN_HEIGHT / 2 - this.logoImg.getWidth() * 0;
	
		this.paint.setTextSize(40);
		this.paint.getTextBounds(author, 0, author.length(), rect);
		
		this.strWidth = rect.width();
		this.strHeight = rect.height();
		
		this.textX = Constants.SCREEN_WIDTH / 2 - this.strWidth / 2;
		this.textY = Constants.SCREEN_HEIGHT / 2 + logoImg.getHeight() + this.strHeight * 2;
	}

	@Override
	public void run() {
		while(this.threadFlag) {
			drawSelf();
			try {
				Thread.sleep(Config.LOADING_GAME_INTERVAL);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			this.threadFlag = false;
		}
		mainActivity.getHandler().sendEmptyMessage(Config.TO_MAIN_VIEW);
	}

	@Override
	public void drawSelf() {
		try {
			canvas = sfh.lockCanvas();
			canvas.save();
			canvas.scale(this.scaleX, this.scaleY);
			canvas.drawBitmap(bgImg, 0, 0, paint);
			canvas.restore();
			canvas.drawBitmap(textImg, textImgX, textImgY, paint);
			canvas.drawBitmap(logoImg, logoImgX, logoImgY, paint);
			canvas.drawText(author, textX, textY, paint);
		} catch(Exception err) {
			err.printStackTrace();
		} finally {
			if(canvas != null) {
				sfh.unlockCanvasAndPost(canvas);
			}
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		super.surfaceDestroyed(arg0);
		release();
	}
	
	@Override
	public void release() {
		if(!this.bgImg.isRecycled()){
			this.bgImg.recycle();
		}
		if(!this.logoImg.isRecycled()){
			this.logoImg.recycle();
		}
		if(!this.textImg.isRecycled()){
			this.textImg.recycle();
		}
	}
	
}
