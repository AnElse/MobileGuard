package com.itanelse.mobileguard.activities;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.itanelse.mobileguard.R;
import com.itanelse.mobileguard.dao.BlackDao;
import com.itanelse.mobileguard.domain.BlackBean;
import com.itanelse.mobileguard.domain.BlackTable;
import com.itanelse.mobileguard.utils.MyConstants;

public class TelSmsSafeActivity extends Activity {

	protected static final int LOADING = 1;// 正在加载数据
	protected static final int FINISH = 2;// 数据加载完成
	private ListView lv_safenumbers;
	private Button bt_addSafeNumber;
	private TextView tv_nodata;
	private ProgressBar pb_loading;
	private BlackDao dao;// 黑名单的数据封装对象
	private MyAdapter adapter;
	private final int MOREDATASCOUNTS = 20;// 分批加载的数据个数
	private List<BlackBean> moreDatas;
	private PopupWindow pw;
	private View contentView;
	private ScaleAnimation sa;

	// 存放黑名单数据的容器
	private List<BlackBean> datas = new ArrayList<BlackBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initView();
		initData();
		initEvent();// 初始化事件
		initPopupWindow();// 弹出窗体，功能：让用户可以从联系人，电话记录，短信记录中添加黑名单数据
	}

	private void showPopupWindow() {
		if (pw != null && pw.isShowing()) {
			pw.dismiss();// 关闭
		} else {
			int[] location = new int[2];
			// 获取添加按钮的坐标
			bt_addSafeNumber.getLocationInWindow(location);

			// 显示动画
			contentView.startAnimation(sa);
			// 设置右上角对齐
			pw.showAtLocation(bt_addSafeNumber, Gravity.RIGHT | Gravity.TOP,
					location[0]
							- (getWindowManager().getDefaultDisplay()
									.getWidth() - bt_addSafeNumber.getWidth()),
					location[1] + bt_addSafeNumber.getHeight());
		}
	}

	private void closePopupWindow() {
		if (pw != null && pw.isShowing()) {
			pw.dismiss();// 关闭
		}
	}

	private void initPopupWindow() {
		// 弹出窗体显示的布局

		contentView = View.inflate(getApplicationContext(),
				R.layout.popup_blacknumber_item, null);

		// 手动添加
		TextView tv_shoudong = (TextView) contentView
				.findViewById(R.id.tv_popup_black_shoudong);
		// 联系人添加
		TextView tv_contact = (TextView) contentView
				.findViewById(R.id.tv_popup_black_contacts);
		// 电话添加
		TextView tv_phonelog = (TextView) contentView
				.findViewById(R.id.tv_popup_black_phonelog);
		// 短信添加
		TextView tv_smslog = (TextView) contentView
				.findViewById(R.id.tv_popup_black_smslog);

		View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tv_popup_black_contacts:// 从联系人导入
					// System.out.println("从联系人导入");
				{
					Intent intent = new Intent(TelSmsSafeActivity.this,
							FriendsActivity.class);
					// 启动联系人的界面，并获取结果
					startActivityForResult(intent, 1);
				}
					break;
				case R.id.tv_popup_black_phonelog:// 从电话日志导入
					System.out.println("从电话日志导入");
					{
						Intent intent = new Intent(TelSmsSafeActivity.this,
								CalllogsActivity.class);
						// 启动联系人的界面，并获取结果
						startActivityForResult(intent, 1);
					}
					break;
				case R.id.tv_popup_black_shoudong:// 手动导入
					// System.out.println("手动导入");
					showInputBlacknumberDialog("");
					break;
				case R.id.tv_popup_black_smslog:// 从短信导入
					System.out.println("从短信导入");

					Intent intent = new Intent(TelSmsSafeActivity.this,
							SmslogsActivity.class);
					// 启动联系人的界面，并获取结果
					startActivityForResult(intent, 1);

					break;

				default:
					break;
				}

				// 关闭popupwindow
				closePopupWindow();
			}
		};

		// 给四个组件添加事件
		tv_smslog.setOnClickListener(listener);
		tv_contact.setOnClickListener(listener);
		tv_phonelog.setOnClickListener(listener);
		tv_shoudong.setOnClickListener(listener);

		pw = new PopupWindow(contentView, -2, -2);

		// 显示动画要有背景
		pw.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

		sa = new ScaleAnimation(1, 1, 0, 1, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0f);
		sa.setDuration(1000);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// 获取联系人，电话记录，短信记录等的电话
		if (data != null) {
			// 用户点击条目获取结果
			String phone = data.getStringExtra(MyConstants.SAFENUMBER);
			// 显示输入黑名单的对话框
			showInputBlacknumberDialog(phone);
		} else {
			// 用户点击返回键 事件处理
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 手动导入
	 */
	protected void showInputBlacknumberDialog(String phone) {
		AlertDialog.Builder ab = new AlertDialog.Builder(this);
		View view = View.inflate(getApplicationContext(),
				R.layout.dialog_addblacknumber, null);

		// 黑名单号码编辑框
		final EditText et_blackNumber = (EditText) view
				.findViewById(R.id.et_telsmssafe_blacknumber);

		// 设置初始的黑名单号码
		et_blackNumber.setText(phone);

		// 短信拦截复选框
		final CheckBox cb_sms = (CheckBox) view
				.findViewById(R.id.cb_telsmssafe_smsmode);

		// 短信拦截复选框
		final CheckBox cb_phone = (CheckBox) view
				.findViewById(R.id.cb_telsmssafe_phonemode);

		// 添加黑名单号码按钮
		Button bt_add = (Button) view.findViewById(R.id.bt_telsmssafe_add);

		// 取消添加黑名单号码按钮
		Button bt_cancel = (Button) view
				.findViewById(R.id.bt_telsmssafe_cancel);

		bt_cancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		bt_add.setOnClickListener(new OnClickListener() {

			private MyAdapter myAdapter;

			@Override
			public void onClick(View v) {
				// 添加黑名单数据
				String phone = et_blackNumber.getText().toString().trim();
				if (TextUtils.isEmpty(phone)) {
					Toast.makeText(getApplicationContext(), "黑名单号码不能为空", 1)
							.show();
					return;
				}

				if (!cb_phone.isChecked() && !cb_sms.isChecked()) {
					// 两个拦截都没选
					Toast.makeText(getApplicationContext(), "至少选择一种拦截模式", 1)
							.show();
					return;
				}

				int mode = 0;
				if (cb_phone.isChecked()) {
					mode |= BlackTable.TEL;// 设置电话拦截模式
				}
				if (cb_sms.isChecked()) {
					mode |= BlackTable.SMS;// 设置电话拦截模式
				}

				// dao.add(phone, mode);//添加数据到黑名单表中

				// 界面看到用户新增的数据
				BlackBean bean = new BlackBean();
				bean.setMode(mode);
				bean.setPhone(phone);

				dao.add(bean);// 添加数据到黑名单表中
				// 如果新增的数据已经存在
				datas.remove(bean);// 该删除方法要靠equals和hashCode两个方法共同判断数据是否一致
				datas.add(0, bean);// 添加数据到List中

				myAdapter = new MyAdapter();
				lv_safenumbers.setAdapter(myAdapter);
				dialog.dismiss();

			}
		});
		ab.setView(view);

		dialog = ab.create();
		dialog.show();// 显示对话框
	}

	private void initEvent() {
		// 给listview设置滑动时间
		lv_safenumbers.setOnScrollListener(new OnScrollListener() {
			/*
			 * (non-Javadoc) onScrollStateChanged 状态改变调用此方法 SCROLL_STATE_FLING:
			 * 惯性滑动 SCROLL_STATE_IDLE: 滑动停止 SCROLL_STATE_TOUCH_SCROLL: 按住滑动
			 * 三种状态，每种状态改变都会触发此方法
			 * 
			 * @see
			 * android.widget.AbsListView.OnScrollListener#onScrollStateChanged
			 * (android.widget.AbsListView, int)
			 */
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				// 监控静止状态SCROLL_STATE_IDLE
				// 当出现SCROLL_STATE_IDLE的状态时候，判断是否显示最后一条数据，如果显示最后一条数据，那就加载更多的数据
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
					// 当出现SCROLL_STATE_IDLE的状态时候
					// 判断是否显示最后一条数据，如果显示最后一条数据，那就加载更多的数据
					// 获取最后显示的数据位置
					int lastVisiblePosition = lv_safenumbers
							.getLastVisiblePosition();
					if (lastVisiblePosition == datas.size() - 1) {
						// 加载更多的数据
						initData();
					}
				}
			}

			/*
			 * (non-Javadoc) onScroll 按住滑动触发事件
			 * 
			 * @see
			 * android.widget.AbsListView.OnScrollListener#onScroll(android.
			 * widget.AbsListView, int, int, int)
			 */
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				// TODO Auto-generated method stub

			}
		});
	}

	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case LOADING:// 正在加载数据
				// 显示progressbar
				pb_loading.setVisibility(View.VISIBLE);
				// 隐藏listView
				lv_safenumbers.setVisibility(View.GONE);
				// 隐藏没有数据
				tv_nodata.setVisibility(View.GONE);
				break;
			case FINISH:
				// 数据加载完成,判断是否有数据
				if (moreDatas.size() != 0) {
					// 有数据
					// 隐藏progressbar
					pb_loading.setVisibility(View.GONE);
					// 显示listView
					lv_safenumbers.setVisibility(View.VISIBLE);
					// 隐藏没有数据
					tv_nodata.setVisibility(View.GONE);

					adapter.notifyDataSetChanged();
				} else {
					if (datas.size() != 0) {// 分批加载数据，没有更多数据
						Toast.makeText(getApplicationContext(), "没有更多数据", 1)
								.show();
						return;
					}
					// 没有数据
					// 显示没有数据
					tv_nodata.setVisibility(View.VISIBLE);
					// 隐藏progressbar
					pb_loading.setVisibility(View.GONE);
					// 隐藏listView
					lv_safenumbers.setVisibility(View.GONE);
				}
				break;

			default:
				break;
			}
		};
	};

	private AlertDialog dialog;

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return datas.size();
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

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ItemView itemView = null;// 声明组件封装对象 初始为null
			if (convertView == null) {
				convertView = View.inflate(getApplicationContext(),
						R.layout.item_telsmssafe_listview, null);
				itemView = new ItemView();
				itemView.tv_phone = (TextView) convertView
						.findViewById(R.id.tv_telsmssafe_listview_item_number);
				itemView.tv_mode = (TextView) convertView
						.findViewById(R.id.tv_telsmssafe_listview_item_mode);
				itemView.iv_delete = (ImageView) convertView
						.findViewById(R.id.iv_telsmssafe_listview_item_delete);
				// 设置标记给convertView
				convertView.setTag(itemView);
			} else {
				itemView = (ItemView) convertView.getTag();
			}

			// 获取当前位置的数据
			final BlackBean bean = datas.get(position);
			// 显示黑名单号码
			itemView.tv_phone.setText(bean.getPhone());
			// 拦截模式进行判断,设置黑名单模式
			switch (bean.getMode()) {
			case BlackTable.SMS:
				itemView.tv_mode.setText("短信拦截");
				break;
			case BlackTable.TEL:
				itemView.tv_mode.setText("电话拦截");
				break;
			case BlackTable.ALL:
				itemView.tv_mode.setText("全部拦截");
				break;
			default:
				break;
			}

			// 设置删除数据的事件
			itemView.iv_delete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					AlertDialog.Builder ab = new AlertDialog.Builder(
							TelSmsSafeActivity.this);
					ab.setTitle("注意");
					ab.setMessage("是否真的删除该数据？");
					ab.setPositiveButton("真删",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// 从数据库中删除当前数据
									dao.delete(bean.getPhone());// 取出当前行数据里的黑名单号码

									// 删除容器中对应的数据
									datas.remove(position);

									// 通知界面更新数据，让用户看到删除数据不存在
									adapter.notifyDataSetChanged();// 只是让listview
																	// 重新去当前显示位置的数据
								}
							});
					ab.setNegativeButton("点错了", null);// 自动关闭对话框
					ab.show();

				}
			});
			return convertView;
		}
	}

	private class ItemView {
		// 显示黑名单号码
		TextView tv_phone;

		// 显示黑名单号码拦截模式
		TextView tv_mode;

		// 删除黑名单数据的 按钮
		ImageView iv_delete;
	}

	/**
	 * 初始化数据 加载更多数据
	 */
	private void initData() {
		// 从db中取黑名单数据，子线程取
		new Thread() {

			public void run() {
				// 取数据之前，发个消息显示正在加载数据的进度条
				mHandler.obtainMessage(LOADING).sendToTarget();

				moreDatas = dao.getMoreDatas(MOREDATASCOUNTS, datas.size());

				datas.addAll(moreDatas);// 把一个容器的所有数据加进来

				// 取完数据,发个消息告诉数据显加载已经完成
				mHandler.obtainMessage(FINISH).sendToTarget();
			};
		}.start();
	}

	/**
	 * 添加黑名单号码
	 * 
	 * @param v
	 */
	public void addBlackNumber(View v) {
		// showInputBlacknumberDialog();
		showPopupWindow();
	}

	/**
	 * 初始化界面
	 */
	private void initView() {
		setContentView(R.layout.activity_telsmssafe);
		// 显示安全号码listview
		lv_safenumbers = (ListView) findViewById(R.id.lv_telsms_safenumbers);

		// 添加黑名单数据的按钮
		bt_addSafeNumber = (Button) findViewById(R.id.bt_telsms_addsafenumber);

		// 没有数据显示的文本
		tv_nodata = (TextView) findViewById(R.id.tv_telsms_nodata);

		// 正在加载数据的进度
		pb_loading = (ProgressBar) findViewById(R.id.pb_telsms_loading);

		// 黑名单的业务对象
		dao = new BlackDao(getApplicationContext());

		// 给Listview设置适配器,取适配器的数据显示
		// 首先 调用adapter的getCount方法来获取多少条数据，如果为0，不显示任何数据，否则调用getView方法依次取出显示位置的数据
		adapter = new MyAdapter();
		lv_safenumbers.setAdapter(adapter);
	}
}
