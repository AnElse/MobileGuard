package com.itanelse.mobileguard.engine;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import android.app.Activity;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.SystemClock;
import android.widget.Toast;

/**
 * 短信备份和还原的业务类封装
 * 
 * @author Administrator
 * 
 */
public class SmsEngine {

	public interface BaikeProgress {
		/**
		 * 进度的显示回调
		 */
		void show();

		/**
		 * @param max
		 *            回调显示进度的最大值
		 */
		void setMax(int max);

		/**
		 * 回调显示当前进度
		 * 
		 * @param progress
		 */
		void setProgress(int progress);

		/**
		 * 进度完成的回调
		 */
		void end();
	}

	private static class Data {
		int progress;
	}

	/**
	 * 短信的备份
	 * 
	 * @param context
	 * @param pd
	 *            接口回调备份的数据
	 */
	public static void smsBaikeJson(final Activity context, final BaikeProgress pd) {
		// 1,通过内容提供者获取到短信
		Uri uri = Uri.parse("content://sms");
		// 获取电话记录的联系人游标
		final Cursor cursor = context.getContentResolver().query(uri,
				new String[] { "address", "date", "body", "type" }, null, null,
				" _id desc");

		// 2,写到文件中
		File file = new File(Environment.getExternalStorageDirectory(),
				"sms.json");

		try {
			FileOutputStream fos = new FileOutputStream(file);

			PrintWriter out = new PrintWriter(fos);
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					pd.show();
					pd.setMax(cursor.getCount());// 设置进度条总进度

				}
			});

			final Data data = new Data();

			// 写根标记 {"count":"10"
			out.println("{\"count\":\"" + cursor.getCount() + "\"");
			// ,"smses":[
			out.println(",\"smses\":[");
			while (cursor.moveToNext()) {
				data.progress++;
				SystemClock.sleep(100);
				// 取短信
				if (cursor.getPosition() == 0) {
					out.println("{");
				} else {
					out.println(",{");
				}

				// address 封装 "address":"hello"
				out.println("\"address\":\"" + cursor.getString(0) + "\",");
				// date 封装
				out.println("\"date\":\"" + cursor.getString(1) + "\",");
				// body 封装
				out.println("\"body\":\"" + cursor.getString(2) + "\",");
				// type 封装
				out.println("\"type\":\"" + cursor.getString(3) + "\"");

				out.println("}");
				// 封装成xml标记
				
				context.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						pd.setProgress(data.progress);
						Toast.makeText(context, "备份成功", 0).show();
					}
				});

			}

			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					pd.end();
				}
			});
			// 写根标记结束标记
			out.println("]}");

			out.flush();
			out.close();// 关闭流
			cursor.close();// 关闭游标
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 短信的备份
	 */
	public static void smsBaikeXml(Activity context, final BaikeProgress pd) {
		// 1,通过内容提供者获取到短信
		Uri uri = Uri.parse("content://sms");
		// 获取电话记录的联系人游标
		final Cursor cursor = context.getContentResolver().query(uri,
				new String[] { "address", "date", "body", "type" }, null, null,
				" _id desc");

		// 2,写到文件中
		File file = new File(Environment.getExternalStorageDirectory(),
				"sms.xml");

		try {
			FileOutputStream fos = new FileOutputStream(file);

			PrintWriter out = new PrintWriter(fos);
			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					pd.show();
					pd.setMax(cursor.getCount());// 设置进度条总进度

				}
			});

			final Data data = new Data();

			// 写根标记
			out.println("<smses count='" + cursor.getCount() + "'>");
			while (cursor.moveToNext()) {
				data.progress++;
				SystemClock.sleep(100);
				// 取短信
				out.println("<sms>");

				// address 封装
				out.println("<address>" + cursor.getString(0) + "</address>");
				// date 封装
				out.println("<date>" + cursor.getString(1) + "</date>");
				// body 封装
				out.println("<body>" + cursor.getString(2) + "</body>");
				// type 封装
				out.println("<type>" + cursor.getString(3) + "</type>");

				out.println("</sms>");
				// 封装成xml标记

				context.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						// TODO Auto-generated method stub
						pd.setProgress(data.progress);
					}
				});

			}

			context.runOnUiThread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					pd.end();
				}
			});
			// 写根标记结束标记
			out.println("</smses>");

			out.flush();
			out.close();// 关闭流
			cursor.close();// 关闭游标
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
