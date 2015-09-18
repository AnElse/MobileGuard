[1mdiff --git a/res/layout/activity_main.xml b/res/layout/activity_main.xml[m
[1mindex dea99c1..b46c753 100644[m
[1m--- a/res/layout/activity_main.xml[m
[1m+++ b/res/layout/activity_main.xml[m
[36m@@ -7,17 +7,26 @@[m
     <TextView[m
         android:layout_width="match_parent"[m
         android:layout_height="60dip"[m
[31m-        android:textSize="30sp"[m
[31m-        android:gravity="center"[m
         android:background="#5500ff00"[m
[31m-        android:text="主界面" />[m
[31m-    [m
[32m+[m[32m        android:gravity="center"[m
[32m+[m[32m        android:text="主界面"[m
[32m+[m[32m        android:textSize="30sp" />[m
[32m+[m
     <com.itanelse.mobileguard.view.MyTextView[m
[32m+[m[32m        android:layout_width="wrap_content"[m
[32m+[m[32m        android:layout_height="wrap_content"[m
[32m+[m[32m        android:layout_marginTop="5dip"[m
         android:ellipsize="marquee"[m
[31m-        android:layout_width="match_parent"[m
[31m-        android:layout_height="match_parent"[m
[31m-        android:textSize="15sp"[m
         android:singleLine="true"[m
[31m-        android:text="你是我的小啊小卫士,功能强大,怎么爱你都不嫌多,实在是居家旅行,出门探亲,蚊叮虫咬,必备良药..."/>[m
[32m+[m[32m        android:text="你是我的小啊小卫士,功能强大,怎么爱你都不嫌多,实在是居家旅行,出门探亲,蚊叮虫咬,必备良药..."[m
[32m+[m[32m        android:textSize="16sp" />[m
[32m+[m
[32m+[m[32m    <GridView[m
[32m+[m[32m        android:verticalSpacing="5dip"[m
[32m+[m[32m        android:numColumns="3"[m
[32m+[m[32m        android:id="@+id/gv_main_menu"[m
[32m+[m[32m        android:layout_width="match_parent"[m
[32m+[m[32m        android:layout_height="wrap_content" >[m
[32m+[m[32m    </GridView>[m
 [m
 </LinearLayout>[m
\ No newline at end of file[m
[1mdiff --git a/src/com/itanelse/mobileguard/activities/MainActivity.java b/src/com/itanelse/mobileguard/activities/MainActivity.java[m
[1mindex 9cdbc42..fb9b4ac 100644[m
[1m--- a/src/com/itanelse/mobileguard/activities/MainActivity.java[m
[1m+++ b/src/com/itanelse/mobileguard/activities/MainActivity.java[m
[36m@@ -2,20 +2,83 @@[m [mpackage com.itanelse.mobileguard.activities;[m
 [m
 import android.app.Activity;[m
 import android.os.Bundle;[m
[32m+[m[32mimport android.view.View;[m
[32m+[m[32mimport android.view.ViewGroup;[m
[32m+[m[32mimport android.widget.BaseAdapter;[m
[32m+[m[32mimport android.widget.GridView;[m
[32m+[m[32mimport android.widget.ImageView;[m
[32m+[m[32mimport android.widget.TextView;[m
 [m
 import com.itanelse.mobileguard.R;[m
 [m
 public class MainActivity extends Activity {[m
[32m+[m
[32m+[m	[32mprivate GridView gv_menu;// 主界面菜单[m
[32m+[m	[32mprivate int[] icons = { R.drawable.safe, R.drawable.callmsgsafe,[m
[32m+[m			[32mR.drawable.app, R.drawable.taskmanager, R.drawable.netmanager,[m
[32m+[m			[32mR.drawable.trojan, R.drawable.sysoptimize, R.drawable.atools,[m
[32m+[m			[32mR.drawable.settings };// 获取到菜单的图片资源[m
[32m+[m	[32mprivate String[] names = { "手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "病毒查杀",[m
[32m+[m			[32m"缓存清理", "高级工具", "设置中心" };// 获取到菜单的名称[m
[32m+[m
 	@Override[m
 	protected void onCreate(Bundle savedInstanceState) {[m
 		super.onCreate(savedInstanceState);[m
[31m-		initview();//初始化界面的组件[m
[32m+[m		[32minitview();// 初始化界面的组件[m
[32m+[m		[32minitData();// 初始化界面数据[m
[32m+[m	[32m}[m
[32m+[m
[32m+[m	[32m/**[m
[32m+[m	[32m * 初始化界面数据的方法[m
[32m+[m	[32m */[m
[32m+[m	[32mprivate void initData() {[m
[32m+[m		[32m//设置Gridview适配器数据[m
[32m+[m		[32mgv_menu.setAdapter(new MyAdapter());[m
 	}[m
[31m-	[m
[32m+[m
[32m+[m	[32m/**[m
[32m+[m	[32m * 设置适配器[m
[32m+[m	[32m *[m[41m [m
[32m+[m	[32m * @author 毓添[m
[32m+[m	[32m *[m[41m [m
[32m+[m	[32m */[m
[32m+[m	[32mprivate class MyAdapter extends BaseAdapter {[m
[32m+[m
[32m+[m		[32m@Override[m
[32m+[m		[32mpublic int getCount() {[m
[32m+[m			[32mreturn icons.length;[m
[32m+[m		[32m}[m
[32m+[m
[32m+[m		[32m@Override[m
[32m+[m		[32mpublic View getView(int position, View convertView, ViewGroup parent) {[m
[32m+[m			[32mView view = View.inflate(getApplicationContext(), R.layout.item_main_gv_menu, null);[m
[32m+[m			[32m//获取组件[m
[32m+[m			[32mImageView gv_icon = (ImageView) view.findViewById(R.id.iv_item_main_gv_icon);[m
[32m+[m			[32mTextView gv_name = (TextView) view.findViewById(R.id.tv_item_main_gv_name);[m
[32m+[m			[32m//设置图片和名称[m
[32m+[m			[32mgv_icon.setImageResource(icons[position]);[m
[32m+[m			[32mgv_name.setText(names[position]);[m
[32m+[m			[32mreturn view;[m
[32m+[m		[32m}[m
[32m+[m
[32m+[m		[32m@Override[m
[32m+[m		[32mpublic Object getItem(int position) {[m
[32m+[m			[32m// TODO Auto-generated method stub[m
[32m+[m			[32mreturn null;[m
[32m+[m		[32m}[m
[32m+[m
[32m+[m		[32m@Override[m
[32m+[m		[32mpublic long getItemId(int position) {[m
[32m+[m			[32m// TODO Auto-generated method stub[m
[32m+[m			[32mreturn 0;[m
[32m+[m		[32m}[m
[32m+[m	[32m}[m
[32m+[m
 	/**[m
 	 * //初始化界面的组件[m
 	 */[m
 	private void initview() {[m
 		setContentView(R.layout.activity_main);[m
[32m+[m		[32mgv_menu = (GridView) findViewById(R.id.gv_main_menu);[m
 	}[m
 }[m
