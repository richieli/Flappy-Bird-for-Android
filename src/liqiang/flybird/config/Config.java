package liqiang.flybird.config;

public class Config {

	public final static int TO_MAIN_VIEW = 1;//�����л�������ͼ
	public final static int END_GAME = 0;//������Ϸ����
	
	public final static int LOADING_GAME_INTERVAL = 2000;//���ؽ���ʱ��
	
	public final static int SPEED = (int) (Constants.SCREEN_WIDTH * 5 / 480);//column��ground���˶��ٶ�
	
	public final static float COLUMN_Y_GAP = Constants.SCREEN_HEIGHT * 173 / 854;//columnͼƬ���Ӽ��϶�߶ȣ������޸ģ���ͼƬ������
	public final static float COLUMN_X_GAP = Constants.SCREEN_WIDTH / 2 + 50;//�������Ӽ��ˮƽ���
	
	public final static double v0 = Constants.SCREEN_HEIGHT * 23 / 854;//С�����׳��ٶ�
	public final static double g = Constants.SCREEN_HEIGHT * 3 / 854;//�������ٶ�
	public final static double t = 0.6;//ʱ����
	
}
