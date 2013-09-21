package org.jnuLib.fragment;

import java.util.HashMap;

import org.jnuLib.Util.JsoupUtil;
import org.jnuLib.data.InfoContent;
import org.jnuLib.ui.R;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.spring.menu.animation.EnlargeAnimationOut;
import com.spring.menu.animation.ShrinkAnimationOut;
import com.spring.menu.animation.SpringAnimation;
import com.spring.menu.animation.ZoomAnimation;
import com.spring.menu.utility.DeviceUtility;
import com.weibo.sdk.android.demo.WeiboActivity;
import com.weibo.sdk.android.keep.AccessTokenKeeper;

public class LeftFragment extends Fragment implements OnClickListener
		
{	//public static Oauth2AccessToken accessToken;
    private View view;
    private Button xiaoneizhaopin;
    private Button yingjiesheng;
    private Button zhuhai;
    private TextView content;
    private InfoContent s;
    private boolean	areMenusShowing;
	private ViewGroup menusWrapper;
	private View imageViewPlus;
	private View shrinkRelativeLayout;
	// 顺时针旋转动画
	private Animation animRotateClockwise;
	// 你试着旋转动画
	private Animation animRotateAntiClockwise;
	
	
//	private Weibo mWeibo;
	// IWeiboAPI weiboAPI;
	/**
     * SsoHandler 仅当sdk支持sso时有效，
     */
  //  private SsoHandler mSsoHandler;
	private WeiboCallback mWeiboCallback;
    //由于微博SDK在fragment获取不到token
    //所以定义一个weibocallback接口，让MainActivity来获取token
    public interface WeiboCallback{
    	public void onShareKey(boolean b,String s);
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);     
		//mWeibo = Weibo.getInstance(WeiboActivity.CONSUMER_KEY, WeiboActivity.REDIRECT_URL);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflater the layout 
        view = inflater.inflate(R.layout.left_fragment, null);
        xiaoneizhaopin=(Button) view.findViewById(R.id.jnuzp);
        yingjiesheng=(Button) view.findViewById(R.id.yingjiesheng);
        zhuhai=(Button) view.findViewById(R.id.zhuhai);
        
     
	    content=(TextView) view.findViewById(R.id.zp_middle_text);

	//    mWeibo = Weibo.getInstance(ConstantS.APP_KEY, ConstantS.REDIRECT_URL,ConstantS.SCOPE);
        
		
        xiaoneizhaopin.setOnClickListener(this);
        yingjiesheng.setOnClickListener(this);
        zhuhai.setOnClickListener(this);
     
		
        
     // 初始化
     		initViews();
        
        return view;
    }
	// 当该Fragment被添加、显示到Activity时，回调该方法
   @Override
	public void onAttach(Activity activity){
	   super.onAttach(activity);
	   if(!(activity instanceof WeiboCallback)){
		   throw new IllegalStateException(
				   "LeftFragment 所在的Activity 必须实现WeiboCallback接口");
		   
	   }
	   mWeiboCallback=(WeiboCallback)activity;
   }
	
	// 当该Fragment从它所属的Activity中被删除时回调该方法
	@Override
	public void onDetach()
	{
		super.onDetach();
		// 将mCallbacks赋为null。
		mWeiboCallback = null;
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
			 new DownTask().execute("珠海招聘");
			break;
		case R.id.menu_setting:
			
			startSpringMenuAnimations(v);
            Toast.makeText(getActivity(), "已注销微博", Toast.LENGTH_SHORT).show();
			 AccessTokenKeeper.clear(getActivity());
			//share2weibo();
			//mWeibo.anthorize(getActivity(), new AuthDialogListener());
			break;
		case R.id.menu_share:
			startSpringMenuAnimations(v);
          //  Toast.makeText(getActivity(), "share", Toast.LENGTH_LONG).show();
           // mSsoHandler =new SsoHandler(getActivity(),mWeibo);
          //  mSsoHandler.authorize( new AuthDialogListener());
			if(s!=null)
			mWeiboCallback.onShareKey(true,s.getUrl());//回调给MainActivity进行处理
			else
				Toast.makeText(getActivity(), "冇内容", Toast.LENGTH_LONG).show();	
            
            // share2weibo();
            // Intent intent = new Intent(getActivity(), WeiboActivity.class);
            // startActivity(intent);
            break;
		case R.id.menu_thought:			
			mWeiboCallback.onShareKey(false,"feedback");//回调给MainActivity进行处理		
			break;
			
		}
	}
	
	
  
	// 初始化
		private void initViews(){
			imageViewPlus = view.findViewById(R.id.imageview_plus);
			menusWrapper = (ViewGroup) view.findViewById(R.id.menus_wrapper);
			shrinkRelativeLayout = view.findViewById(R.id.relativelayout_shrink);
			//layoutMain = (RelativeLayout) view.findViewById(R.id.layout_content);
			
			animRotateClockwise = AnimationUtils.loadAnimation(
					getActivity(),R.anim.rotate_clockwise);
			animRotateAntiClockwise = AnimationUtils.loadAnimation(
					getActivity(),R.anim.rotate_anticlockwise);

			shrinkRelativeLayout.setOnClickListener(new OnClickListener() {

				public void onClick(View v) {
					// TODO Auto-generated method stub
					showLinearMenus();
				}
			});
			
			for (int i = 0; i < menusWrapper.getChildCount(); i++) {
				menusWrapper.getChildAt(i).setOnClickListener(this);
			}
		}

		/**
		 * 以直线型展开菜单
		 */
		private void showLinearMenus() {
			int[] size = DeviceUtility.getScreenSize(getActivity());
			
			if (!areMenusShowing) {
				SpringAnimation.startAnimations(
						this.menusWrapper, ZoomAnimation.Direction.SHOW, size);
				this.imageViewPlus.startAnimation(this.animRotateClockwise);
			} else {
				SpringAnimation.startAnimations(
						this.menusWrapper, ZoomAnimation.Direction.HIDE, size);
				this.imageViewPlus.startAnimation(this.animRotateAntiClockwise);
			}
			
			areMenusShowing = !areMenusShowing;
		}

		
		/**
		 * 展现菜单动画效果
		 * @param view
		 * @param runnable
		 */
		private void startSpringMenuAnimations(View view) {
			areMenusShowing = true;
			Animation shrinkOut1 = new ShrinkAnimationOut(300);
			Animation growOut = new EnlargeAnimationOut(300);
			shrinkOut1.setInterpolator(new AnticipateInterpolator(2.0F));
			shrinkOut1.setAnimationListener(new Animation.AnimationListener() {

				public void onAnimationEnd(Animation animation) {
					// TODO Auto-generated method stub
					imageViewPlus.clearAnimation();
				}

				public void onAnimationRepeat(Animation animation) {
					// TODO Auto-generated method stub
					
				}

				public void onAnimationStart(Animation animation) {
					// TODO Auto-generated method stub
					
				}
			});
			
			view.startAnimation(growOut);
		}
	
	
	
	
    
	class DownTask extends AsyncTask<String, Integer, InfoContent>
	{
		// 可变长的输入参数，与AsyncTask.exucute()对应
		ProgressDialog pdialog;
        boolean flag=false;//判断是否是应届生的招聘会
		@Override
		protected InfoContent doInBackground(String... params)
		{
			s=new InfoContent();
			try
			{
				if(params[0].contains("校内招聘"))
				//校内招聘
				s.setEls(JsoupUtil.getJnuZhaopinhui("http://career.jnu.edu.cn/showarticle.php?actiontype=5&id=763"));   
				s.setUrl("暨大招聘会信息："+"http://career.jnu.edu.cn/showarticle.php?actiontype=5&id=763");
				if(params[0].contains("应届生招聘"))
					{
					s.setEls(JsoupUtil.getyingjiesZhaopinhui("http://zph.yingjiesheng.com/zphlist.php?remark=0&city=guangzhou"));
					s.setUrl("应届生招聘会信息："+"http://zph.yingjiesheng.com/zphlist.php?remark=0&city=guangzhou");
					flag=true;
					}
				if(params[0].contains("珠海招聘"))
					{
					s.setEls(JsoupUtil.getzhuhaiZhaopinhui("http://zph.yingjiesheng.com/zhuhaizhaopinhui/"));   
					s.setUrl("珠海招聘会信息："+"http://zph.yingjiesheng.com/zhuhaizhaopinhui/");
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
		protected void onPostExecute(InfoContent result)
		{  
			if (result != null)
			{
				
				if(flag){
					content.setText("");//清除之前的内容
				
					for(int i=4;i<result.getEls().size();i++){
						
						switch(i%3){
						case 1:
							content.append(Html.fromHtml(result.getEls().get(i).toString()));
							content.append("\n");
						    break;
						case 2:
							content.append(Html.fromHtml(result.getEls().get(i).toString()));
							content.append("\n");
							break;
						case 0:
							content.append("");
							content.append("\n");
							break;

							
						}
					}
				}else
				  content.setText(Html.fromHtml(result.getEls().toString()));	
				
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
		/*UserAPI.show(uid,new RequestListener(){

            @Override
            public void onComplete(String response) {
                    // TODO Auto-generated method stub
                    //执行后回调执行onComplete，response为返回的json字符串，再进行处理吧
                    
            }

            @Override
            public void onIOException(IOException e) {
                    // TODO Auto-generated method stub
                    
            }

            @Override
            public void onError(WeiboException e) {
                    // TODO Auto-generated method stub
                    
            }
            
    });*/
		
		


