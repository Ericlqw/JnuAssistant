package org.jnuLib.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jnuLib.Util.JsoupUtil;
import org.jnuLib.Util.MyListView;
import org.jnuLib.Util.MyListView.OnRefreshListener;
import org.jnuLib.Util.NetWorkUtil;
import org.jnuLib.data.GlobleData;
import org.jnuLib.ui.JwcNewsDetailActivity;
import org.jnuLib.ui.R;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

/**
 * @author Eric(lqweric@gmail.com)
 *
 * 2013-8-16
 */
public class JWCNewsFragment extends Fragment {	

	private View view=null;
	private List<Map<String, Object>> n_list=	new ArrayList<Map<String, Object>>();
	private MyListView jwc_listView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {      
		//inflater the layout 
		view = inflater.inflate(R.layout.jwc_news_layout, null);

		// 取得屏幕尺寸大小 
		displayScreenSize(); 
		jwc_listView = (MyListView) view.findViewById(R.id.jwc_listView);
		refreshFragment(); 
		
	
			
		
		jwc_listView.setOnRefreshListener(new OnRefreshListener()

		{
			public void onRefresh()
			{
				RefreshTask rTask = new RefreshTask();
				rTask.execute(1000);
			}
		});

		return view;

	}
	private void setdefaultdata(){
		
		for(int i=0;i<8;i++){
			HashMap<String, Object>	defaultMap = new HashMap<String, Object>();
				defaultMap.put("newsTitle", "没内容");
				defaultMap.put("newsHref","null");
			
				defaultMap.put("newsDate","[8-7]");
				n_list.add(defaultMap);
			}
		setNewsList();
	}

	public void refreshFragment(){
		DownTask  task= new DownTask();
		if(NetWorkUtil.isNetworkAvailable(getActivity())){

			task.execute("http://jwcweb.jnu.edu.cn/index-new.asp");

		}
		else{
			setdefaultdata();
			Toast.makeText(getActivity(), "无网络连接", Toast.LENGTH_LONG).show();
		    
		}


	}

	class DownTask extends AsyncTask<String, Integer, List<Map<String, Object>>>
	{
		// 可变长的输入参数，与AsyncTask.exucute()对应
		ProgressDialog pdialog;

		@Override
		protected List<Map<String, Object>> doInBackground(String... params)
		{
			try
			{
             	n_list=JsoupUtil.getJWCNews(params[0]);   

				return n_list;
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

			if(n_list!=null){
				setNewsList();


			}else{
		
				Toast.makeText(getActivity(), "哎呀，出错了，刷新试试", Toast.LENGTH_SHORT).show();
 				
				
			}

			pdialog.dismiss();
		}


		@Override
		protected void onPreExecute()
		{
			pdialog = new ProgressDialog(getActivity());
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

		}
	}

	private void setNewsList(){
		SimpleAdapter listAdapter = new SimpleAdapter(
				getActivity(), n_list, R.layout.news_list,
				new String[] { "newsTitle",  "newsDate" }, 
				new int[] { R.id.newsTitle,  R.id.date });
		jwc_listView.setAdapter(listAdapter);
		jwc_listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				if(NetWorkUtil.isNetworkAvailable(getActivity())){

					GlobleData.NEWSDETAIL_URL=n_list.get(position).get("newsHref").toString();
					GlobleData.NEWSTITLE=n_list.get(position).get("newsTitle").toString();
					Intent intent = new Intent(getActivity(),JwcNewsDetailActivity.class);
					startActivity(intent); 
				}
				else{
				
					Toast.makeText(getActivity(), "无网络连接", Toast.LENGTH_LONG).show();
				    
				}

				    

			}
		});

	}




	//AsyncTask异步任务

	class RefreshTask extends AsyncTask<Integer, Integer, String>

	{   

		@Override

		protected String doInBackground(Integer... params)

		{

			try

			{

				Thread.sleep(params[0]);

			}
			catch (Exception e)

			{

				e.printStackTrace();

			}



			// 在data最前添加数据

			// data.addFirst("刷新后的内容");

			
			n_list=JsoupUtil.getJWCNews(
					"http://jwcweb.jnu.edu.cn/index-new.asp"); 
			return null;

		}  

		@Override
		protected void onPostExecute(String result)

		{

			super.onPostExecute(result);
			if(n_list!=null){
				setNewsList();
			}else{
				Toast.makeText(getActivity(), "哎呀，出错了，刷新试试", Toast.LENGTH_SHORT).show();

			}

			jwc_listView.onRefreshComplete();

		}      

	}


	// 取得屏幕尺寸大小

	public void displayScreenSize()

	{

		// 获得屏幕大小1

		WindowManager manager = getActivity().getWindowManager();

		int width = manager.getDefaultDisplay().getWidth();

		int height = manager.getDefaultDisplay().getHeight();


		// 获得屏幕大小2

		DisplayMetrics dMetrics = new DisplayMetrics();
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dMetrics);

		int screenWidth = dMetrics.widthPixels;

		int screenHeight = dMetrics.heightPixels;


	}

}
