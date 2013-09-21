package org.jnuLib.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jnuLib.Util.JsoupUtil;
import org.jnuLib.Util.NetWorkUtil;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
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
public class BookDetailActivity extends Activity {
	private TextView title;
	private TextView author;
	private TextView chubanshe;
	private TextView fuzhu;
	private TextView zaiti;
	private TextView biaozhunhao;
	private TextView zhuti;
	private TextView guancangInfo;
	private ImageButton website;
	private TextView titlebar;
	private List<Map<String, Object>> InfoList=new ArrayList<Map<String, Object>>();
    private String detailURL=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.bookdetail_layout);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE,R.layout.title_layout);


		titlebar=(TextView) findViewById(R.id.titletext);
		
		titlebar.setText("图书详情");
	

		title=(TextView) findViewById(R.id.title);
		author=(TextView) findViewById(R.id.author);
		chubanshe=(TextView) findViewById(R.id.chubanshe);
		fuzhu=(TextView) findViewById(R.id.fuzhu);
		zaiti=(TextView) findViewById(R.id.zaiti);
		biaozhunhao=(TextView) findViewById(R.id.biaozhunhao);
		zhuti=(TextView) findViewById(R.id.zhuti);
		guancangInfo=(TextView) findViewById(R.id.guancang);
       //获取其他Activity传递过来的参数
		Intent intent=getIntent(); 
		detailURL=intent.getStringExtra("Detail_URL"); 
		website=(ImageButton) findViewById(R.id.website);
		website.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
			   
				Uri u = Uri.parse(detailURL);  
				Intent it = new Intent(Intent.ACTION_VIEW, u); 
				BookDetailActivity.this.startActivity(it); 
			}
			
		});
				
		if(NetWorkUtil.isNetworkAvailable(this)){
		DownTask  task= new DownTask();      
		task.execute(detailURL);

		}else{
    		Toast.makeText(this, "无网络连接", Toast.LENGTH_LONG).show();

		}
		
	}

	class DownTask extends AsyncTask<String, Integer, List<Map<String, Object>>>
	{
		// 可变长的输入参数，与AsyncTask.exucute()对应
		ProgressDialog pdialog;
		// 定义记录已经读取行的数量
		int hasRead = 0;

		@Override
		protected List<Map<String, Object>> doInBackground(String... params)
		{		
			try
			{
				InfoList.clear();
				InfoList=JsoupUtil.getBookDetailInfo(params[0]);   
				return InfoList;
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(List<Map<String, Object>> result)
		{  

			title.setText(InfoList.get(0).get("BookTitle").toString());			
			author.setText(InfoList.get(0).get("Author").toString());
			chubanshe.setText(InfoList.get(0).get("Publisher").toString());
			fuzhu.setText(InfoList.get(0).get("FuZhu").toString());
			zaiti.setText(InfoList.get(0).get("ZaiTi").toString());
			biaozhunhao.setText(InfoList.get(0).get("BiaoZhunHao").toString());
			zhuti.setText(InfoList.get(0).get("ZhuTi").toString());
			
			//如果size==1证明没有馆藏信息，InfoList[0]为书本详细信息
			if(InfoList.size()>1){

				for(int i=1;i<InfoList.size();i++){

					if(InfoList.get(i)!=null){
						int n=i%4;
						switch(n){
						case 1:
							guancangInfo.append("序号:"+(InfoList.get(i).get("GuanCang").toString())+"\n");				
							break;
						case 2:
							guancangInfo.append("索书号:"+(InfoList.get(i).get("GuanCang").toString())+"\n");				
							break;
						case 3:
							guancangInfo.append("馆藏地:"+(InfoList.get(i).get("GuanCang").toString())+"\n");				
							break;
						case 0:
							guancangInfo.append("处理状态:"+(InfoList.get(i).get("GuanCang").toString())+"\n");				
							guancangInfo.append("\n");
							break;

						}

					}
				}
			}

			else{
				guancangInfo.setText("暂无馆藏");
			}

			pdialog.dismiss();
		}


		@Override
		protected void onPreExecute()
		{
			pdialog = new ProgressDialog(BookDetailActivity.this);
			// 设置对话框的标题
			pdialog.setTitle("请稍等");
			// 设置对话框 显示的内容
			pdialog.setMessage("正在查找...");
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
