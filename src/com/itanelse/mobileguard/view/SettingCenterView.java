package com.itanelse.mobileguard.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.itanelse.mobileguard.R;

public class SettingCenterView extends LinearLayout {

	private TextView tv_title;
	private TextView tv_content;
	private CheckBox cb_check;
	private String[] contents;// 分割后的数组
	private View item;// 设置中心的item

	/**
	 * 配置文件中,发射实例化设置属性参数
	 * 
	 * @param context
	 * @param attrs
	 */
	public SettingCenterView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
		initEvent();
		String content = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.itanelse.mobileguard",
				"content");
		String title = attrs.getAttributeValue(
				"http://schemas.android.com/apk/res/com.itanelse.mobileguard",
				"title");
		contents = content.split("-");
		tv_title.setText(title);
	}

	/**
	 * 由于复选框是自定义View的内部组件,所以外面想访问,要设置公有的方法让外部访问 
	 * 设置item里面checkbox的状态
	 * @param isChecked
	 */
	public void setCheck(boolean isChecked) {
		cb_check.setChecked(isChecked);
	}

	/**
	 * 获取item里checkbox的状态
	 * @return
	 */
	public boolean isChecked() {
		return cb_check.isChecked();
	}

	/**
	 * 给item根布局设置点击事件
	 * 
	 * @param listener
	 */
	public void setItemClickListener(OnClickListener listener) {
		item.setOnClickListener(listener);
	}

	/**
	 * 初始化事件
	 */
	private void initEvent() {
		/*
		 * item.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * cb_check.setChecked(!cb_check.isChecked()); } });
		 */

		// 当选择状态发生变化时触发是事件
		cb_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					tv_content.setText(contents[1]);
					tv_content.setTextColor(Color.GREEN);
				} else {
					tv_content.setText(contents[0]);
					tv_content.setTextColor(Color.RED);
				}
			}
		});
	}

	/**
	 * 初始化LinearLayout的子组件
	 */
	public void initView() {
		// 给LinearLayout添加子组件
		item = View.inflate(getContext(), R.layout.item_settingcenter_view,
				null);
		tv_title = (TextView) item
				.findViewById(R.id.tv_settingcenter_autoupdate_title);
		tv_content = (TextView) item
				.findViewById(R.id.tv_settingcenter_autoupdate_content);
		cb_check = (CheckBox) item
				.findViewById(R.id.cb_settingcenter_autoupdate_checked);
		addView(item);
	}

	/**
	 * 代码实例化调用该构造函数
	 * 
	 * @param context
	 */
	public SettingCenterView(Context context) {
		super(context);
		initView();
	}

}
