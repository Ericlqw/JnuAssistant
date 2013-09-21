package org.jnuLib.fragment;

import org.jnuLib.Util.JsoupUtil;
import org.jnuLib.ui.R;
import org.jsoup.select.Elements;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Description:
 * <br/>website: <a href="http://www.crazyit.org">crazyit.org</a>
 * <br/>Copyright (C), 2001-2014, Yeeku.H.Lee
 * <br/>This program is protected by copyright laws.
 * <br/>Program Name:
 * <br/>Date:
 * @author Yeeku.H.Lee kongyeeku@163.com
 * @version 1.0
 */
public class LeftCategoryFragment extends Fragment implements OnClickListener
		
{
	
    private View view;
    private Button xiaoneizhaopin;
    private Button yingjiesheng;
    private Button qita;
    private Button share;
    private Button setting;
    private TextView content;
    
    RelativeLayout layoutMenu;//菜单的总布局
	Button btnSina, btnQQ, btnQQzone, btnRenRen;//菜单里面的四个分享按钮
	Animation animationScale,animationMenuIn,animationMenuOut;//三种动画 
	final int STATE_IN = 1, STATE_OUT = 2, STATE_SCALE = 3;//记录当前需要显示的动画

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);     
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflater the layout 
        view = inflater.inflate(R.layout.leftfragment_barbtu, null);
        xiaoneizhaopin=(Button) view.findViewById(R.id.jnuzp);
        yingjiesheng=(Button) view.findViewById(R.id.yingjiesheng);
        qita=(Button) view.findViewById(R.id.zhuhai);
        
        share=(Button) view.findViewById(R.id.share);
        setting=(Button) view.findViewById(R.id.setting);
        
	    content=(TextView) view.findViewById(R.id.zp_middle_text);

	  //  getMenuViews();//加载控件
		//loadSomeAnimations();//初始化一些动画实例
		
        xiaoneizhaopin.setOnClickListener(this);
        yingjiesheng.setOnClickListener(this);
        qita.setOnClickListener(this);
        share.setOnClickListener(this);
        setting.setOnClickListener(this);
			
        
        return view;
    }
    
    
    private void loadSomeAnimations() {
		animationScale = AnimationUtils.loadAnimation(getActivity(),
				R.anim.my_scale_alpha);//加载anim文件里面的动画，动画即可以在XML中定义，也可以在代码中定义
		
		//如果你的动画是固定的，不需要考虑距离的，那么建议在XML中定义，因为这样效率快，而且穿件一个实例之后就可以多次使用，而不是反复的去new一个实例

	}

	private void getMenuViews() {
		layoutMenu = (RelativeLayout) view.findViewById(R.id.layout_menu);
		btnSina = (Button) view.findViewById(R.id.btn_sina);
		btnQQ = (Button) view.findViewById(R.id.btn_qq);
		btnQQzone = (Button) view.findViewById(R.id.btn_qqzone);
		btnRenRen = (Button) view.findViewById(R.id.btn_renren);
		btnSina.setOnClickListener(this);
		btnQQ.setOnClickListener(this);
		btnQQzone.setOnClickListener(this);
		btnRenRen.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.jnuzp:
			 new DownTask().execute("校内招聘");
             break;
		case R.id.yingjiesheng:
			 new DownTask().execute("应届生招聘");
			break;
		case R.id.zhuhai:
			break;
		case R.id.share:
			if(layoutMenu.isShown()){
				showMenu(STATE_OUT);
			}else{
				showMenu(STATE_IN);
			}
		}
	}
    
    
private void showMenu(int state) {
		
		//这里面是重要的方法，根据传进来的值，设置相应的动画
		switch (state) {
		case STATE_IN://从下往上弹出来的动画
			animationMenuIn = null;
			int height = (int) layoutMenu.getBottom();//获得底部到中间的距离
			
			animationMenuIn = new TranslateAnimation(0, 0, height, 0);
			animationMenuIn.setFillAfter(false);
			animationMenuIn.setDuration(500);
			animationMenuIn.setInterpolator(new OvershootInterpolator(2f));//动画插入器
			
			layoutMenu.setVisibility(View.VISIBLE);//动画加载的时候让菜单可见，这样动画效果才有效
			layoutMenu.startAnimation(animationMenuIn);
			break;
		case STATE_OUT://从中间弹到底部消失的动画
			animationMenuIn = null;
			int height2 = (int) layoutMenu.getBottom();
			animationMenuIn = new TranslateAnimation(0, 0, 0, height2 + 10);
			animationMenuIn.setFillAfter(false);
			animationMenuIn.setDuration(500);
			layoutMenu.startAnimation(animationMenuIn);
			layoutMenu.setVisibility(View.INVISIBLE);
			break;
		case STATE_SCALE://由原来的状态缩小大小为0个一个动画
			layoutMenu.startAnimation(animationScale);
			layoutMenu.setVisibility(View.INVISIBLE);
			break;

		default:
			break;
		}
	}
    
	class DownTask extends AsyncTask<String, Integer, Elements>
	{
		// 可变长的输入参数，与AsyncTask.exucute()对应
		ProgressDialog pdialog;
        boolean flag=false;//判断是否是应届生的招聘会
		@Override
		protected Elements doInBackground(String... params)
		{
			Elements s=null;
			try
			{
				if(params[0].contains("校内招聘"))
				//校内招聘
				s=JsoupUtil.getJnuZhaopinhui("http://career.jnu.edu.cn/showarticle.php?actiontype=5&id=763");   
				
				if(params[0].contains("应届生招聘"))
					{
					s=JsoupUtil.getyingjiesZhaopinhui("http://zph.yingjiesheng.com/zphlist.php?remark=0&city=guangzhou");
					flag=true;
					}
				return s;
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
			if (result.toString() != null)
			{
				
				if(flag){
					content.setText("");//清除之前的内容
				
					for(int i=4;i<result.size();i++){
						
						switch(i%3){
						case 1:
							content.append(Html.fromHtml(result.get(i).toString()));
							content.append("\n");
						    break;
						case 2:
							content.append(Html.fromHtml(result.get(i).toString()));
							content.append("\n");
							break;
						case 0:
							content.append("");
							content.append("\n");
							break;

							
						}
					}
				}else
				  content.setText(Html.fromHtml(result.toString()));	
				
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
			// 更新进度
			//show.setText("已经读取了【" + values[0] + "】行！");
			//	pdialog.setProgress(values[0]);
		}
	}
	    
	  
	
}
