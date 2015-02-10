package liqiang.flybird.object;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public abstract class GameObject {

	protected int currentFrame;//��ǰ����֡
	
	protected float obj_x;//�������Ͻ�����
	protected float obj_y;
	
	protected float obj_mid_x;//�������ĺ�����
	protected float obj_mid_y;//��������������
	
	protected float obj_width;//������
	protected float obj_height;//����߶�
	
	protected Resources resources;
	protected Paint paint;
	
	public GameObject(Resources resources) {
		this.resources = resources;
		paint = new Paint();
	}
	
	//�����˶��߼�
	public abstract void step();
	
	//��ͼ����
	public abstract void drawSelf(Canvas canvas);
	
	//��ʼ����Ƭ��Դ
	public abstract void initBitmap();
	
	//�ͷ�ͼƬ��Դ
	public abstract void release();
	
	public void setObjX(float x) {
		this.obj_x = x;
	}
	
	public float getObjX() {
		return this.obj_x;
	}
	
	public void setObjY(float y) {
		this.obj_y = y;
	}
	
	public float getObjY() {
		return this.obj_y;
	}
	
	public void setObjMidX(float x) {
		this.obj_mid_x = x;
	}
	
	public float getObjMidX() {
		return this.obj_mid_x;
	}
	
	public void setObjMidY(float y) {
		this.obj_mid_y = y;
	}
	
	public float getObjMidY() {
		return this.obj_mid_y;
	}
	
	public void setObjWidth(float w) {
		this.obj_width = w;
	}
	
	public float getObjWidth() {
		return this.obj_width;
	}
	
	public void setObjHeight(float h) {
		this.obj_height = h;
	}
	
	public float getObjHeight() {
		return this.obj_height;
	}
	
}
