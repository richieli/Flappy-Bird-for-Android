package liqiang.flybird.view;

import java.util.ArrayList;
import java.util.List;

import liqiang.flybird.R;
import liqiang.flybird.config.Config;
import liqiang.flybird.config.Constants;
import liqiang.flybird.object.Bird;
import liqiang.flybird.object.Column;
import liqiang.flybird.object.Ground;
import liqiang.flybird.util.FileManager;
import liqiang.flybird.util.SoundPlayer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

public class MainView extends BaseView {
	
	private Ground ground;
	private Column column1;
	private Column column2;
	private Column column3;
	private Bird bird;
	
	private FileManager fileManager;
	
	private Bitmap bgImg;
	private Bitmap startImg;
	private Bitmap endImg;
	private Bitmap restartButtonImg;
	private Bitmap exitButtonImg;
	private Bitmap noticeImg;
	private Bitmap pauseButtonImg;
	private Bitmap bigNumbersImg;
	private Bitmap smallNumbersImg;
	private Bitmap medalImg;
	
	private float startImgX;
	private float startImgY;
	private float endImgX;
	private float endImgY;
	private float noticeImgX;
	private float noticeImgY;
	private float restartButtonImgX;
	private float restartButtonImgY;
	private float exitButtonImgX;
	private float exitButtonImgY;
	private float pauseButtonImgX;
	private float pauseButtonImgY;
	private float bigNumbersImgX;
	private float bigNumbersImgY;
	private float smallNumbersImgX;//bestScore位置
	private float smallNumbersImgY;
	private float smallScoreX;
	private float smallScoreY;
	private float medalImgX;
	private float medalImgY;
	
	private boolean isStart;
	private boolean isHit;
	private boolean isOver;
	private boolean isPause;
	private boolean isWrite;
	
	private int score;
	private int bestScore;
	
	//private Rect rect;
	
	public MainView(Context context, SoundPlayer soundPlayer) {
		super(context, soundPlayer);
		isStart = false;
		isHit = false;
		isOver = false;
		isPause = false;
		isWrite = false;
		//rect = new Rect();
		
		//Toast.makeText(this.mainActivity.getApplicationContext(), Constants.SCREEN_WIDTH + " " + Constants.SCREEN_HEIGHT, Toast.LENGTH_LONG).show();
		
		fileManager = new FileManager();
		if(fileManager.sdIsAvalible()) {
			fileManager.initFile();
			if(fileManager.fileReader().length() <= 0) {
				bestScore = 0;
			} else {
				bestScore = Integer.parseInt(fileManager.fileReader());
			}
		} else {
			//Looper.prepare();
			Toast.makeText(this.mainActivity.getApplicationContext(), "SD卡不可用，将无法保存您的最高纪录", Toast.LENGTH_LONG).show();
			//Looper.loop();
		}
		ground = new Ground(getResources());
		column1 = new Column(getResources(), Config.COLUMN_X_GAP * 2, ground.getObjHeight());
		column2 = new Column(getResources(), Config.COLUMN_X_GAP + column1.getObjMidX(), ground.getObjHeight());
		column3 = new Column(getResources(), Config.COLUMN_X_GAP + column2.getObjMidX(), ground.getObjHeight());
		bird = new Bird(getResources(), ground.getObjHeight());
		this.thread = new Thread(this);
	}
	
	@Override
	public void run() {
		while(this.threadFlag) {
			if(!isHit && !isOver) {
				ground.step();
			}
			if(isStart && !isHit &&!isOver) {
				column1.step();
				column2.step();
				column3.step();
			}
			if(isStart) {
				bird.step();
			}
			drawSelf();
			if(isOver) {
				threadFlag = false;
			}
			if(isPause) {
				synchronized (thread) {  
				    try {  
				    	thread.wait();  
				    } catch (InterruptedException e) {  
				        e.printStackTrace();  
				    }  
				}
			}
			try {
				Thread.sleep(1000 / 60);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			drawSelf();

			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			drawNotice();

			if(fileManager.sdIsAvalible()) {
				if(score > bestScore) {
					fileManager.fileWriter(String.valueOf(score));
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for(int i=0; i<=score; i++) {
				drawResult(i);
				try {
					Thread.sleep(1000/60);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			synchronized(thread) {
			drawMedal();
			}
			
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			drawButton();
			isWrite = true;
	}

	@Override	
	public void drawSelf() {
		try {
			canvas = sfh.lockCanvas();
			
			drawObject();
			
			if(!isHit) {
				if(bird.pass(column1) || bird.pass(column2) || bird.pass(column3)) {
					soundPlayer.playSound(2, 0);
					score++;
				}
				if(bird.hitColumn(column1) || bird.hitColumn(column2) ||bird.hitColumn(column3)){
					soundPlayer.playSound(3, 0);
					paint.setAlpha(50);
					paint.setColor(Color.WHITE);
					canvas.drawRect(0, 0, Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT, paint);
					isHit = true;
				}
			}
			
			/*paint.setTextSize(26);
			paint.setColor(Color.WHITE);
			paint.getTextBounds(String.valueOf(score), 0, String.valueOf(score).length(), rect);
			canvas.drawText(String.valueOf(score), 10, rect.height() + 5, paint);
			*/
			if(!isOver) {
				drawScore(bigNumbersImg, bigNumbersImgX, bigNumbersImgY, score);
			}
			
			if(isOver) {
				soundPlayer.playSound(5, 0);
				canvas.drawBitmap(endImg, endImgX, endImgY, paint);
			}
			if(!isOver) {
				if(bird.hitGround(ground)){
					soundPlayer.playSound(4, 0);
					isOver = true;
				}
			}
			if(!isStart) {
				if(!isStart) {
					canvas.drawBitmap(startImg, startImgX, startImgY, paint);
				}
			}
			if(isStart && !isHit && !isOver) {
				if(!isPause) {
					canvas.save();
					canvas.clipRect(pauseButtonImgX, pauseButtonImgY, pauseButtonImgX + pauseButtonImg.getWidth(), pauseButtonImgY + pauseButtonImg.getHeight() / 2);
					canvas.drawBitmap(pauseButtonImg, pauseButtonImgX, pauseButtonImgY, paint);
					canvas.restore();
				}
				else {
					canvas.save();
					canvas.clipRect(pauseButtonImgX, pauseButtonImgY, pauseButtonImgX + pauseButtonImg.getWidth(), pauseButtonImgY + pauseButtonImg.getHeight() / 2);
					canvas.drawBitmap(pauseButtonImg, pauseButtonImgX, pauseButtonImgY - pauseButtonImg.getHeight() / 2, paint);
					canvas.restore();
				}
			}
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if(canvas != null) {
				sfh.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public void drawNotice() {
		try {
			canvas = sfh.lockCanvas();
			
			drawObject();
			
			soundPlayer.playSound(5, 0);
			canvas.drawBitmap(endImg, endImgX, endImgY, paint);

			canvas.drawBitmap(noticeImg, noticeImgX, noticeImgY, paint);
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if(canvas != null) {
				sfh.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public void drawResult(int i) {
		try {
			canvas = sfh.lockCanvas();
			
			drawObject();
			
			//soundPlayer.playSound(5, 0);
			canvas.drawBitmap(endImg, endImgX, endImgY, paint);
	
			canvas.drawBitmap(noticeImg, noticeImgX, noticeImgY, paint);
			
			/*
			paint.setTextSize(26);
			paint.setColor(Color.WHITE);
			paint.getTextBounds(String.valueOf(bestScore), 0, String.valueOf(bestScore).length(), rect);
			canvas.drawText(String.valueOf(bestScore), noticeImgX + noticeImg.getWidth() - rect.width(), noticeImgY + noticeImg.getHeight() - rect.height(), paint);
			*/

			drawScore(smallNumbersImg, smallScoreX, smallScoreY, i);

			//drawScore(smallNumbersImg, smallNumbersImgX, smallNumbersImgY, bestScore);
			
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if(canvas != null) {
				sfh.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public void drawMedal() {
		try {
			canvas = sfh.lockCanvas();
			
			drawObject();
			
			soundPlayer.playSound(5, 0);
			canvas.drawBitmap(endImg, endImgX, endImgY, paint);

			canvas.drawBitmap(noticeImg, noticeImgX, noticeImgY, paint);
			
			/*
			paint.setTextSize(26);
			paint.setColor(Color.WHITE);
			paint.getTextBounds(String.valueOf(bestScore), 0, String.valueOf(bestScore).length(), rect);
			canvas.drawText(String.valueOf(bestScore), noticeImgX + noticeImg.getWidth() - rect.width(), noticeImgY + noticeImg.getHeight() - rect.height(), paint);
			*/

			drawScore(smallNumbersImg, smallScoreX, smallScoreY, score);

			drawScore(smallNumbersImg, smallNumbersImgX, smallNumbersImgY, bestScore);
			
			drawMedalImg();
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if(canvas != null) {
				sfh.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public void drawButton() {
		try {
			canvas = sfh.lockCanvas();
			
			drawObject();
			
			soundPlayer.playSound(5, 0);
			canvas.drawBitmap(endImg, endImgX, endImgY, paint);

			canvas.drawBitmap(noticeImg, noticeImgX, noticeImgY, paint);
			
			drawScore(smallNumbersImg, smallScoreX, smallScoreY, score);

			drawScore(smallNumbersImg, smallNumbersImgX, smallNumbersImgY, bestScore);
			
			drawMedalImg();
			
			canvas.drawBitmap(restartButtonImg, restartButtonImgX, restartButtonImgY, paint);
			canvas.drawBitmap(exitButtonImg, exitButtonImgX, exitButtonImgY, paint);
		} catch (Exception err) {
			err.printStackTrace();
		} finally {
			if(canvas != null) {
				sfh.unlockCanvasAndPost(canvas);
			}
		}
	}
	
	public void drawObject() {
		canvas.save();
		canvas.scale(scaleX, scaleY);
		canvas.drawBitmap(bgImg, 0, 0, paint);
		canvas.restore();
		column1.drawSelf(canvas);
		column2.drawSelf(canvas);
		column3.drawSelf(canvas);
		bird.drawSelf(canvas);
		ground.drawSelf(canvas);
	}
	
	public void drawMedalImg() {
		canvas.save();
		canvas.clipRect(medalImgX, medalImgY, medalImgX + medalImg.getWidth(), medalImgY + medalImg.getHeight() / 2);
		if(score >= 60) {
			canvas.drawBitmap(medalImg, medalImgX, medalImgY - medalImg.getHeight() / 2, paint);
		}
		else {
			canvas.drawBitmap(medalImg, medalImgX, medalImgY, paint);
		}
		canvas.restore();
	}
	
	public void drawScore(Bitmap numbersImg, float x, float y, int num) {
		List<Integer> list = new ArrayList<Integer>();
		int scoreCopy = num;
		int quotient = 0;
		
		while((quotient = scoreCopy / 10) != 0) {
			list.add(scoreCopy % 10);
			scoreCopy = quotient;
		}
		list.add(scoreCopy % 10);
		
		float posX = x;
		float posY = y;
		
		int len = list.size();
		
		float oddNumW = numbersImg.getWidth() / 10;
		float oddNumH = numbersImg.getHeight();
		
		posX -= len * oddNumW / 2;
		
		canvas.save();
		for(int i=len-1; i>=0; i--) {
			switch(list.get(i)) {
				case 0:
					canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
					canvas.drawBitmap(numbersImg, posX - 0 * oddNumW, posY, paint);
					posX += oddNumW;
					canvas.restore();
					canvas.save();
					break;

				case 1:
					canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
					canvas.drawBitmap(numbersImg, posX - 1 * oddNumW, posY, paint);
					posX += oddNumW;
					canvas.restore();
					canvas.save();
					break;

				case 2:
					canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
					canvas.drawBitmap(numbersImg, posX - 2 * oddNumW, posY, paint);
					posX += oddNumW;
					canvas.restore();
					canvas.save();
					break;

				case 3:
					canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
					canvas.drawBitmap(numbersImg, posX - 3 * oddNumW, posY, paint);
					posX += oddNumW;
					canvas.restore();
					canvas.save();
					break;
					
				case 4:
					canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
					canvas.drawBitmap(numbersImg, posX - 4 * oddNumW, posY, paint);
					posX += oddNumW;
					canvas.restore();
					canvas.save();
					break;

				case 5:
					canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
					canvas.drawBitmap(numbersImg, posX - 5 * oddNumW, posY, paint);
					posX += oddNumW;
					canvas.restore();
					canvas.save();
					break;

				case 6:
					canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
					canvas.drawBitmap(numbersImg, posX - 6 * oddNumW, posY, paint);
					posX += oddNumW;
					canvas.restore();
					canvas.save();
					break;

				case 7:
					canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
					canvas.drawBitmap(numbersImg, posX - 7 * oddNumW, posY, paint);
					posX += oddNumW;
					canvas.restore();
					canvas.save();
					break;
					
				case 8:
					canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
					canvas.drawBitmap(numbersImg, posX - 8 * oddNumW, posY, paint);
					posX += oddNumW;
					canvas.restore();
					canvas.save();
					break;

				case 9:
					canvas.clipRect(posX, posY, posX + oddNumW, posY + oddNumH);
					canvas.drawBitmap(numbersImg, posX - 9 * oddNumW, posY, paint);
					posX += oddNumW;
					canvas.restore();
					canvas.save();
					break;

			}
		}
		canvas.restore();
	}
	
	// 响应触屏事件的方法
			@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN && event.getPointerCount() == 1) {	
			float x = event.getX();
			float y = event.getY();
			
			if(isWrite) {				
				if(x >= restartButtonImgX && x <= restartButtonImgX + restartButtonImg.getWidth() && y >= restartButtonImgY && y <= restartButtonImgY + restartButtonImg.getHeight()) {
					mainActivity.getHandler().sendEmptyMessage(Config.TO_MAIN_VIEW);
				}
				
				if(x >= exitButtonImgX && x <= exitButtonImgX + exitButtonImg.getWidth() && y >= exitButtonImgY && y <= exitButtonImgY + exitButtonImg.getHeight()) {
					mainActivity.getHandler().sendEmptyMessage(Config.END_GAME);
				}
			}
			
			if(!isStart) {
				isStart = true;
			}
			if(!isHit && !isOver) {
				if(x <= pauseButtonImgX || x>= pauseButtonImgX + pauseButtonImg.getWidth() || y <= pauseButtonImgY || y>= pauseButtonImgY + pauseButtonImg.getHeight() / 2) {
					bird.flappy();
					soundPlayer.playSound(1, 0);
				}
			}
			
			if(isStart && !isHit && !isOver) {
				if(x >= pauseButtonImgX && x<= pauseButtonImgX + pauseButtonImg.getWidth() && y >= pauseButtonImgY && y<= pauseButtonImgY + pauseButtonImg.getHeight() / 2) {
					isPause = !isPause;
					if(isPause == false) {
						synchronized(this.thread) {
							thread.notify(); 
						}
					}
				}
			}
			
			return true;
		} 
		return false;
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
		bgImg = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
		startImg = BitmapFactory.decodeResource(getResources(), R.drawable.start);
		endImg = BitmapFactory.decodeResource(getResources(), R.drawable.text_gameover);
		restartButtonImg = BitmapFactory.decodeResource(getResources(), R.drawable.restartbutton);
		exitButtonImg = BitmapFactory.decodeResource(getResources(), R.drawable.exitbutton);
		noticeImg = BitmapFactory.decodeResource(getResources(), R.drawable.notice);
		pauseButtonImg = BitmapFactory.decodeResource(getResources(), R.drawable.pausebutton);
		bigNumbersImg = BitmapFactory.decodeResource(getResources(), R.drawable.bignumbers);
		smallNumbersImg = BitmapFactory.decodeResource(getResources(), R.drawable.smallnumbers);
		medalImg = BitmapFactory.decodeResource(getResources(), R.drawable.medal);
		
		this.scaleX = Constants.SCREEN_WIDTH / bgImg.getWidth();
		this.scaleY = Constants.SCREEN_HEIGHT / bgImg.getHeight();
		
		startImgX = Constants.SCREEN_WIDTH / 2 - startImg.getWidth() / 2;
		startImgY = Constants.SCREEN_HEIGHT / 2 - startImg.getHeight() / 2;
		endImgX = Constants.SCREEN_WIDTH / 2 - endImg.getWidth() / 2;
		endImgY = Constants.SCREEN_HEIGHT / 2 - endImg.getHeight() * 3;
		noticeImgX = Constants.SCREEN_WIDTH / 2 - noticeImg.getWidth() / 2;
		noticeImgY = Constants.SCREEN_HEIGHT / 2 - endImg.getHeight();
		restartButtonImgX = Constants.SCREEN_WIDTH / 2 - restartButtonImg.getWidth() * 5 / 4;
		restartButtonImgY = Constants.SCREEN_HEIGHT / 2 + noticeImg.getHeight();
		exitButtonImgX = Constants.SCREEN_WIDTH / 2 + exitButtonImg.getWidth() / 4;
		exitButtonImgY = Constants.SCREEN_HEIGHT / 2 + noticeImg.getHeight();
		pauseButtonImgX = 0;
		pauseButtonImgY = Constants.SCREEN_HEIGHT - pauseButtonImg.getHeight() / 2;
		bigNumbersImgX = Constants.SCREEN_WIDTH / 2;
		bigNumbersImgY = 10;
		smallNumbersImgX = noticeImgX + noticeImg.getWidth() * 5 / 6;
		smallNumbersImgY =noticeImgY + noticeImg.getHeight() - smallNumbersImg.getHeight() * 2;
		smallScoreX = smallNumbersImgX;
		smallScoreY = smallNumbersImgY - smallNumbersImg.getHeight() * 23 / 10;
		medalImgX = noticeImgX + noticeImg.getWidth() / 8;
		medalImgY = noticeImgY + noticeImg.getHeight() * 7 / 20;
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		super.surfaceDestroyed(arg0);
		release();
	}
	
	@Override
	public void release() {
		if(!bgImg.isRecycled()) {
			bgImg.recycle();
		}
		if(!startImg.isRecycled()) {
			startImg.recycle();
		}
		if(!endImg.isRecycled()) {
			endImg.recycle();
		}
		if(!restartButtonImg.isRecycled()) {
			restartButtonImg.recycle();
		}
		if(!exitButtonImg.isRecycled()) {
			exitButtonImg.recycle();
		}
		if(!noticeImg.isRecycled()) {
			noticeImg.recycle();
		}
		if(!pauseButtonImg.isRecycled()) {
			pauseButtonImg.recycle();
		}
		if(!bigNumbersImg .isRecycled()) {
			bigNumbersImg .recycle();
		}
		if(!smallNumbersImg .isRecycled()) {
			smallNumbersImg .recycle();
		}
		if(!medalImg.isRecycled()) {
			medalImg.recycle();
		}
		ground.release();
		column1.release();
		column2.release();
		column3.release();
		bird.release();
	}

}
