package com.example.homework;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
/**
 * 
 * ���ߣ� ����ǿ
 * ѧ�ţ�  2015110512
 *
 */
public class MainActivity extends Activity {
	public static MainActivity instance;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		instance = this;
		//����ȫ��
		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//��ʾ�Զ����SurfaceView��ͼ
		setContentView(new MySurfaceView(this));
	}
}