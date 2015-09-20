package com.itanelse.mobileguard.activities;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.domain.ContactInfo;
import com.itanelse.mobileguard.utils.ContactInfoUtils;
import com.itanelse.mobileguard.utils.MyConstants;

/**
 * 当页面中只有listView一个组件时,可以继承ListActivity 它里面封装了一个组件组成的listView 显示所有好友信息的界面
 * 
 * @author 毓添
 * 
 */
public class FriendsActivity extends ListActivity {
	private List<ContactInfo> contactInfos;// 联系人信息的集合
	protected static final int LOADING = 1;// 获取联系人时的加载对话框
	protected static final int FINISH = 2;// 获取联系人的数据加载成功
	private ListView lv_datas;// 显示联系人信息的listView
	private ProgressDialog pd;// 正在加载联系人数据的对话框

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();// 初始化界面组件
		initDatas();// 初始化数据
		initEvent();// 初始化事件
	}

	/**
	 * 初始化listview的条目点击事件
	 */
	private void initEvent() {
		lv_datas.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// 当点击到哪个items时,就把信息保存起来,并且回传到setup3界面
				// 先获取点击在哪个条目
				ContactInfo contactInfo = contactInfos.get(position);
				// 在获取电话号码
				String phone = contactInfo.getPhone();
				// 配合startActiviityForResult和onActivityResult一起使用
				Intent datas = new Intent();
				// 保存电话号码
				datas.putExtra(MyConstants.SAFENUMBER, phone);// 保存安全号码
				// 设置数据
				setResult(1, datas);
				// 关闭自己，一旦返回就回到了Setup3Activity中的onActivityResult中...
				finish();
			}
		});
	}

	private Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			// 更新界面
			switch (msg.what) {
			case LOADING:
				pd = new ProgressDialog(FriendsActivity.this);
				pd.setTitle("注意:");
				pd.setMessage("正在玩命加载中....");
				pd.show();
				break;
			case FINISH:
				if (pd != null) {
					pd.dismiss();// 关闭对话框
					pd = null;// 垃圾回收释放内存
				}
				// 显示联系人的电话
				lv_datas.setAdapter(new MyAdapter());
				break;
			default:
				break;
			}
		};
	};

	/**
	 * 初始化界面组件
	 */
	private void initView() {
		lv_datas = getListView();// 继承ListActivity时,可以通过getListView获取到
	}

	/**
	 * 获取联系人信息的适配器
	 * 
	 * @author 毓添
	 * 
	 */
	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return contactInfos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			View view = View.inflate(getApplicationContext(),
					R.layout.item_setup3_lv_contacts, null);
			// 获取组件
			TextView contactname = (TextView) view
					.findViewById(R.id.tv_friends_contactname);
			TextView contactphone = (TextView) view
					.findViewById(R.id.tv_friends_contactphone);
			// 获取每个联系人的姓名和电话
			ContactInfo contactInfo = contactInfos.get(position);
			String name = contactInfo.getName();
			String phone = contactInfo.getPhone();
			// 设置姓名和电话
			contactname.setText(name);
			contactphone.setText(phone);
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
	 * 初始化数据
	 */
	private void initDatas() {
		// 获取数据,2种,一是本地数据,一是网络数据,都存在耗时操作
		// 所以在子线程中运行
		new Thread() {
			public void run() {
				// 显示获取数据的进度
				Message msg = Message.obtain();
				msg.what = LOADING;
				mHandler.sendMessage(msg);
				// 获取数据成功
				contactInfos = ContactInfoUtils
						.getAllContactInfos(getApplicationContext());
				// 数据获取完成,发送数据加载完成的消息
				msg = Message.obtain();
				msg.what = FINISH;
				mHandler.sendMessage(msg);
			};
		}.start();
	}
}
