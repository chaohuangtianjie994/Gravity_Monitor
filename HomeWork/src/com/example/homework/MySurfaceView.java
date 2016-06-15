package com.example.homework;

import android.app.Service;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;



/**
 * 
 * ���ߣ� ����ǿ
 * ѧ�ţ�  2015110512
 *
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder sfh;
	private Paint paint;
	private Thread th;
	private boolean flag;
	private Canvas canvas;
	private int screenW, screenH;
		//����һ��������������
		private SensorManager sm;
		//����һ��������
		private Sensor sensor;
		//����һ��������������
		private SensorEventListener mySensorListener;
		//Բ�ε�X,Y����
		private int arc_x, arc_y;
		//��������xyzֵ
		private float x = 0, y = 0, z = 0;
		 

	/**
	 * SurfaceView��ʼ������
	 */
	public MySurfaceView(Context context) {
		super(context);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		setFocusable(true);
		//��ȡ������������ʵ��
		sm = (SensorManager) MainActivity.instance.getSystemService(Service.SENSOR_SERVICE);
		//ʵ��һ������������ʵ��  
		sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
		//ʵ��������������
		mySensorListener = new SensorEventListener() {
			@Override
			//��������ȡֵ�����ı�ʱ����Ӧ�˺���  
			public void onSensorChanged(SensorEvent event) {
				x = event.values[0]; 
				//x>0 ˵����ǰ�ֻ��� x<0�ҷ�       
				y = event.values[1];
				//y>0 ˵����ǰ�ֻ��·� y<0�Ϸ�  
				z = event.values[2]; 
				//z>0 �ֻ���Ļ���� z<0 �ֻ���Ļ����  
				arc_x -= x;
				arc_y += y;
			}
			@Override
			//�������ľ��ȷ����ı�ʱ��Ӧ�˺���  
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
		//Ϊ������ע�������
		sm.registerListener(mySensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
		
	}

	/**
	 * SurfaceView��ͼ��������Ӧ�˺���
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		flag = true;
		//ʵ���߳�
		th = new Thread(this);
		//�����߳�
		th.start();
	}

	/**
	 * ��Ϸ��ͼ
	 */
	public void myDraw() {
		
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				paint.setColor(Color.RED);
				canvas.drawArc(new RectF(arc_x, arc_y, arc_x + 100, arc_y + 100), 0, 360, true, paint);
				paint.setColor(Color.YELLOW);
				canvas.drawText("��ǰ������������ֵ:", arc_x - 50, arc_y - 30, paint);
				canvas.drawText("x=" + x+"    "+ "y=" + y +"    "+ "z=" + z, arc_x - 50, arc_y, paint);
				String Title="�ֻ���̬�����";
				String temp_str = "��ܰ��ʾ�� ";
				String temp_str2 = "";
				String temp_str3 = "";
				String info="����һ���ֻ���̬��������������ֻ����Դ���������������ʵ������Ϣ��ʵʱ�ɼ���";
				String author="����������ǿ";
				String num="ѧ�ţ�2015110512";
				if (x < 1 && x > -1 && y < 1 && y > -1) {
					temp_str2 += "��ǰ�ֻ�����ˮƽ���õ�״̬";
					if (z > 0) {
						temp_str3 += "������Ļ����"+"     "+"X��Ĳ���ֵ:" + x + "      " + "Y��Ĳ���ֵ:" + y + "      " + "Z��Ĳ���ֵ:" + z;
					} else {
						temp_str3 += "������Ļ����,��ʾ���������ֻ������۾�����Ӵ~"+"     "+"X��Ĳ���ֵ:" + x + "      " + "Y��Ĳ���ֵ:" + y + "      " + "Z��Ĳ���ֵ:" + z;
					}
				} else {
					if (x > 1) {
						temp_str2 += "��ǰ�ֻ��������󷭵�״̬"+"     ";
					} else if (x < -1) {
						temp_str2 += "��ǰ�ֻ��������ҷ���״̬"+"     ";
					}
					if (y > 1) {
						temp_str2 += "��ǰ�ֻ��������·���״̬"+"     ";
					} else if (y < -1) {
						temp_str2 += "��ǰ�ֻ��������Ϸ���״̬"+"     ";
					}
					if (z > 0) {
						temp_str3 += "������Ļ����"+"     "+"X��Ĳ���ֵ:" + x + "      " + "Y��Ĳ���ֵ:" + y + "      " + "Z��Ĳ���ֵ:" + z;
					} else {
						temp_str3 += "������Ļ����,��ʾ���������ֻ������۾�����Ӵ~"+"     "+"X��Ĳ���ֵ:" + x + "      " + "Y��Ĳ���ֵ:" + y + "      " + "Z��Ĳ���ֵ:" + z;
					}
				}
				paint.setTextSize(50);
				paint.setColor(Color.RED);
				canvas.drawText(Title, 350, 50, paint);  //����
				canvas.drawText(author, 0, 700, paint);  //����
				canvas.drawText(num, 0, 800, paint);     //ѧ��
				
				paint.setColor(Color.YELLOW);
				paint.setTextSize(28);
				canvas.drawText(info, 0, 500, paint);   //�������
				
				paint.setTextSize(25);
				canvas.drawText(temp_str, 0, 200, paint);
				canvas.drawText(temp_str2, 0, 230, paint);
				canvas.drawText(temp_str3, 0, 260, paint);
				
				
				
				

			}
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			if (canvas != null)
				sfh.unlockCanvasAndPost(canvas);
		}
	}
	

	@Override
	public void run() {
		while (flag) {
			long start = System.currentTimeMillis();
			myDraw();

			long end = System.currentTimeMillis();
			try {
				if (end - start < 50) {
					Thread.sleep(50 - (end - start));
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * SurfaceView��ͼ״̬�����ı䣬��Ӧ�˺���
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	/**
	 * SurfaceView��ͼ����ʱ����Ӧ�˺���
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}
}
