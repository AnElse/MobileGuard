package com.itanelse.mobileguard.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.itanelse.mobileguard.R;

public class MainActivity extends Activity {

	private GridView gv_menu;// 主界面菜单
	private int[] icons = { R.drawable.safe, R.drawable.callmsgsafe,
			R.drawable.gv_list_selector_app, R.drawable.taskmanager, R.drawable.netmanager,
			R.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,
			R.drawable.settings };// 获取到菜单的图片资源
	private String[] names = { "手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "病毒查杀",
			"缓存清理", "高级工具", "设置中心" };// 获取到菜单的名称

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initview();// 初始化界面的组件
		initData();// 初始化界面数据
	}

	/**
	 * 初始化界面数据的方法
	 */
	private void initData() {
		//设置Gridview适配器数据
		gv_menu.setAdapter(new MyAdapter());
	}

	/**
	 * 设置适配器
	 * 
	 * @author 毓添
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return icons.length;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(), R.layout.item_main_gv_menu, null);
			//获取组件
			ImageView gv_icon = (ImageView) view.findViewById(R.id.iv_item_main_gv_icon);
			TextView gv_name = (TextView) view.findViewById(R.id.tv_item_main_gv_name);
			//设置图片和名称
			gv_icon.setImageResource(icons[position]);
			gv_name.setText(names[position]);
			return view;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}
	}

	/**
	 * //初始化界面的组件
	 */
	private void initview() {
		setContentView(R.layout.activity_main);
		gv_menu = (GridView) findViewById(R.id.gv_main_menu);
	}
}
