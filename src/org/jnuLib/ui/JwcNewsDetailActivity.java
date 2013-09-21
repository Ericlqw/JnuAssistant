package org.jnuLib.ui;

import org.jnuLib.Util.JsoupUtil;
import org.jnuLib.Util.NetWorkUtil;
import org.jnuLib.data.GlobleData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Eric(lqweric@gmail.com)
 *
 * 2013-8-16
 */
public class JwcNewsDetailActivity extends Activity {
	private TextView title;
	private String s;
	private TextView content;
    private ImageButton website;
    private TextView titlebar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.news_detail);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_layout);

		title=(TextView) findViewById(R.id.n_title);
		content=(TextView) findViewById(R.id.n_content);
		titlebar=(TextView) findViewById(R.id.titletext);
		
		titlebar.setText("教务处通知");
		website=(ImageButton) findViewById(R.id.website);
		website.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			   
				Uri u = Uri.parse(GlobleData.NEWSDETAIL_URL);  
				Intent it = new Intent(Intent.ACTION_VIEW, u); 
				JwcNewsDetailActivity.this.startActivity(it); 
			}
			
		});
		if(NetWorkUtil.isNetworkAvailable(this)){
		DownTask  task= new DownTask();      
		task.execute(GlobleData.NEWSDETAIL_URL);
		}else{
    		Toast.makeText(this, "无网络连接", Toast.LENGTH_LONG).show();

		}
	
	}

	class DownTask extends AsyncTask<String, Integer, String>
	{
		// 可变长的输入参数，与AsyncTask.exucute()对应
		ProgressDialog pdialog;
	
		@Override
		protected String doInBackground(String... params)
		{		
			try
			{
				
				s=JsoupUtil.getJWCNewsDetail(params[0]);   
				return s;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result)
		{  
            String r=GlobleData.NEWSTITLE;
			title.setText(r);		
			
			//直接显示标签内容，保持页面排版
			  content.setText(Html.fromHtml(s));	
			pdialog.dismiss();
		}


		@Override
		protected void onPreExecute()
		{
			pdialog = new ProgressDialog(JwcNewsDetailActivity.this);
			// 设置对话框的标题
			pdialog.setTitle("请稍等");
			// 设置对话框 显示的内容
			pdialog.setMessage("玩命加载中...");
			// 设置对话框不能用“取消”按钮关闭
			pdialog.setCancelable(true);
			// 设置该进度条的最大进度值
			//pdialog.setMax(202);
			// 设置对话框的进度条风格
			pdialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			// 设置对话框的进度条是否显示进度
			pdialog.setIndeterminate(false);
			pdialog.show();
		}

		@Override
		protected void onProgressUpdate(Integer... values)
		{
			// 更新进度
			//show.setText("已经读取了【" + values[0] + "】行！");
			//	pdialog.setProgress(values[0]);
		}
	}
}
