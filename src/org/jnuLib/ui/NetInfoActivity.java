package org.jnuLib.ui;

import org.jnuLib.Util.JsoupUtil;
import org.jnuLib.Util.NetWorkUtil;
import org.jnuLib.data.GlobleData;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Eric(lqweric@gmail.com)
 *
 * 2013-8-16
 */
public class NetInfoActivity extends Activity {
	
	private TextView content;
    private TextView titlebar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.net_info_layout);

		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_layout);

		
		content=(TextView) findViewById(R.id.netinfo);
		titlebar=(TextView) findViewById(R.id.titletext);
		
		titlebar.setText("校园网用户信息");
		/*website=(ImageButton) findViewById(R.id.website);
		website.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			   
				Uri u = Uri.parse(GlobleData.NEWSDETAIL_URL);  
				Intent it = new Intent(Intent.ACTION_VIEW, u); 
				NetInfoActivity.this.startActivity(it); 
			}
			
		});*/
		if(NetWorkUtil.isNetworkAvailable(this)){
		DownTask  task= new DownTask();      
		task.execute();
		}else{
    		Toast.makeText(this, "无网络连接", Toast.LENGTH_LONG).show();

		}
	
	}

	class DownTask extends AsyncTask<Elements, Integer, Elements>
	{
		// 可变长的输入参数，与AsyncTask.exucute()对应
		ProgressDialog pdialog;
	
		@Override
		protected Elements doInBackground(Elements... params)
		{		
			try
			{
				
				  
				return JsoupUtil.getNetInfo(GlobleData.SEARCHNO,GlobleData.SEARCHTEXT); 
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Elements result)
		{  
           
//			if(result.text()==""){
//			System.out.println("denglushiabsdha"+result.text());
//			}
			
			
		if(result.text()==""){
			
			content.setText("\n\n\n登陆失败，请输入正确的学号和姓名\n");
			}
			else{
			//直接显示标签内容，保持页面排版
			int i=0;
			content.append("\n");
			for(Element td:result){
				
				if(i<24){
				switch(i%2){
				case 0:	
					content.append(td.text());
					break;
				case 1:
				    content.append(Html.fromHtml(td.toString()));
				    content.append("\n");
				    break;
				}
				}
				else{
				    content.append(Html.fromHtml(td.toString()));
				}
				i++;
				
			}
		}

			pdialog.dismiss();
		}


		@Override
		protected void onPreExecute()
		{
			pdialog = new ProgressDialog(NetInfoActivity.this);
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
