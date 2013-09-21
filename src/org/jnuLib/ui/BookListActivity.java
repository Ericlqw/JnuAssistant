package org.jnuLib.ui;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.jnuLib.Util.JsoupUtil;
import org.jnuLib.Util.NetWorkUtil;
import org.jnuLib.data.GlobleData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


/**
 * @author Eric(lqweric@gmail.com)
 *
 * 2013-8-16
 */
public class BookListActivity extends Activity
{
	private TextView sumNumber;
	private TextView pageNumber;
	private Button nextButton;
	private Button preButton;
	private ListView bookList;
	private List<Map<String, Object>> InfoList=null;
	private int page=1;
	private String   searchtext =null;
    private Context mActivity;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		bookList=(ListView)findViewById(R.id.search_section_list);
		// 总页数
		sumNumber = (TextView) findViewById(R.id.sum_number);
		pageNumber = (TextView) findViewById(R.id.page_number);
		// 上一页、下一页按钮
		nextButton = (Button) findViewById(R.id.next);
		preButton = (Button) findViewById(R.id.pre);

		
		mActivity=getApplicationContext();
		DownTask  task= new DownTask();

		try {
			//先对内容进行utf-8编码
			searchtext   =   java.net.URLEncoder.encode(GlobleData.SEARCHTEXT,   "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   

		if(NetWorkUtil.isNetworkAvailable(this)){
	    	
			task.execute(GlobleData.SEARCHBOOK_URL+searchtext+GlobleData.SEARCHBOOK_URLTAIL);
    	                          
    	}
    	else{
    		Toast.makeText(this, "无网络连接", Toast.LENGTH_LONG).show();
    	}

		preButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (page <= 1) {
					Toast.makeText(getApplicationContext(), "已经是第一页了！",
							Toast.LENGTH_SHORT).show();
				} else{
					
					if(NetWorkUtil.isNetworkAvailable(mActivity)){
					new DownTask().execute(GlobleData.PREPAGE); 
					page--;
					}
					else{
			    		Toast.makeText(mActivity, "无网络连接", Toast.LENGTH_LONG).show();

					}
					

				}
			}
		});

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			
				if (page >=Integer.parseInt(GlobleData.TOTLEPAGE)) {
					Toast.makeText(getApplicationContext(), "已经是最后一页了！",
							Toast.LENGTH_SHORT).show();
				} else {
					
					if(NetWorkUtil.isNetworkAvailable(mActivity)){
						new DownTask().execute(GlobleData.NEXTPAGE); 
						page++;
						}
						else{
				    		Toast.makeText(mActivity, "无网络连接", Toast.LENGTH_LONG).show();

						}
					
				}
			}
		});

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
			InfoList=new ArrayList<Map<String, Object>>();
			try
			{
				InfoList.clear();
				InfoList=JsoupUtil.getSearchBookInfo(params[0]);   

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

			sumNumber.setText(GlobleData.TOTLERECORD);
			pageNumber.setText(page+"/"+GlobleData.TOTLEPAGE);

			SimpleAdapter listAdapter = new SimpleAdapter(
					getApplicationContext(), InfoList, R.layout.book_list,
					new String[] { "bookTitle",  "bookDetail" }, 
					new int[] { R.id.bookTitle,  R.id.bookPublisher });
			bookList.setAdapter(listAdapter);
			bookList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					// TODO Auto-generated method stub
					// System.out.println(arg0);
					// System.out.println(arg1);
					Toast.makeText(getApplicationContext(), 
							InfoList.get(position).get("bookTitle")+"被单击了",
							Toast.LENGTH_SHORT).show();

					Intent intent = new Intent(getApplicationContext(),BookDetailActivity.class);

					Bundle bundle=new Bundle(); 
					bundle.putString("Detail_URL", InfoList.get(position).get("bookHref").toString()); 
					intent.putExtras(bundle); 

					startActivity(intent);     

				}
			});



			pdialog.dismiss();
		}


		@Override
		protected void onPreExecute()
		{
			pdialog = new ProgressDialog(BookListActivity.this);
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
