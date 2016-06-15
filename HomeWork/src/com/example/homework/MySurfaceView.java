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
 * 作者： 杨文强
 * 学号：  2015110512
 *
 */
public class MySurfaceView extends SurfaceView implements Callback, Runnable {
	private SurfaceHolder sfh;
	private Paint paint;
	private Thread th;
	private boolean flag;
	private Canvas canvas;
	private int screenW, screenH;
		//声明一个传感器管理器
		private SensorManager sm;
		//声明一个传感器
		private Sensor sensor;
		//声明一个传感器监听器
		private SensorEventListener mySensorListener;
		//圆形的X,Y坐标
		private int arc_x, arc_y;
		//传感器的xyz值
		private float x = 0, y = 0, z = 0;
		 

	/**
	 * SurfaceView初始化函数
	 */
	public MySurfaceView(Context context) {
		super(context);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setColor(Color.WHITE);
		paint.setAntiAlias(true);
		setFocusable(true);
		//获取传感器管理类实例
		sm = (SensorManager) MainActivity.instance.getSystemService(Service.SENSOR_SERVICE);
		//实例一个重力传感器实例  
		sensor = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
		//实例传感器监听器
		mySensorListener = new SensorEventListener() {
			@Override
			//传感器获取值发生改变时在响应此函数  
			public void onSensorChanged(SensorEvent event) {
				x = event.values[0]; 
				//x>0 说明当前手机左翻 x<0右翻       
				y = event.values[1];
				//y>0 说明当前手机下翻 y<0上翻  
				z = event.values[2]; 
				//z>0 手机屏幕朝上 z<0 手机屏幕朝下  
				arc_x -= x;
				arc_y += y;
			}
			@Override
			//传感器的精度发生改变时响应此函数  
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
			}
		};
		//为传感器注册监听器
		sm.registerListener(mySensorListener, sensor, SensorManager.SENSOR_DELAY_GAME);
		
	}

	/**
	 * SurfaceView视图创建，响应此函数
	 */
	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		screenW = this.getWidth();
		screenH = this.getHeight();
		flag = true;
		//实例线程
		th = new Thread(this);
		//启动线程
		th.start();
	}

	/**
	 * 游戏绘图
	 */
	public void myDraw() {
		
		try {
			canvas = sfh.lockCanvas();
			if (canvas != null) {
				canvas.drawColor(Color.BLACK);
				paint.setColor(Color.RED);
				canvas.drawArc(new RectF(arc_x, arc_y, arc_x + 100, arc_y + 100), 0, 360, true, paint);
				paint.setColor(Color.YELLOW);
				canvas.drawText("当前重力传感器的值:", arc_x - 50, arc_y - 30, paint);
				canvas.drawText("x=" + x+"    "+ "y=" + y +"    "+ "z=" + z, arc_x - 50, arc_y, paint);
				String Title="手机姿态监测仪";
				String temp_str = "温馨提示： ";
				String temp_str2 = "";
				String temp_str3 = "";
				String info="这是一款手机姿态监测仪器，利用手机中自带的重力传感器，实现了信息的实时采集。";
				String author="姓名：杨文强";
				String num="学号：2015110512";
				if (x < 1 && x > -1 && y < 1 && y > -1) {
					temp_str2 += "当前手机处于水平放置的状态";
					if (z > 0) {
						temp_str3 += "并且屏幕朝上"+"     "+"X轴的测试值:" + x + "      " + "Y轴的测试值:" + y + "      " + "Z轴的测试值:" + z;
					} else {
						temp_str3 += "并且屏幕朝下,提示别躺着玩手机，对眼睛不好哟~"+"     "+"X轴的测试值:" + x + "      " + "Y轴的测试值:" + y + "      " + "Z轴的测试值:" + z;
					}
				} else {
					if (x > 1) {
						temp_str2 += "当前手机处于向左翻的状态"+"     ";
					} else if (x < -1) {
						temp_str2 += "当前手机处于向右翻的状态"+"     ";
					}
					if (y > 1) {
						temp_str2 += "当前手机处于向下翻的状态"+"     ";
					} else if (y < -1) {
						temp_str2 += "当前手机处于向上翻的状态"+"     ";
					}
					if (z > 0) {
						temp_str3 += "并且屏幕朝上"+"     "+"X轴的测试值:" + x + "      " + "Y轴的测试值:" + y + "      " + "Z轴的测试值:" + z;
					} else {
						temp_str3 += "并且屏幕朝下,提示别躺着玩手机，对眼睛不好哟~"+"     "+"X轴的测试值:" + x + "      " + "Y轴的测试值:" + y + "      " + "Z轴的测试值:" + z;
					}
				}
				paint.setTextSize(50);
				paint.setColor(Color.RED);
				canvas.drawText(Title, 350, 50, paint);  //标题
				canvas.drawText(author, 0, 700, paint);  //作者
				canvas.drawText(num, 0, 800, paint);     //学号
				
				paint.setColor(Color.YELLOW);
				paint.setTextSize(28);
				canvas.drawText(info, 0, 500, paint);   //软件介绍
				
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
	 * SurfaceView视图状态发生改变，响应此函数
	 */
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	/**
	 * SurfaceView视图消亡时，响应此函数
	 */
	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
	}
}
