package org.jnuLib.ui;

import java.text.SimpleDateFormat;

import org.jnuLib.fragment.LeftFragment;
import org.jnuLib.fragment.ViewPagerFragment;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.SlidingMenu.CanvasTransformer;
import com.slidingmenu.lib.app.SlidingActivity;
import com.weibo.sdk.android.Oauth2AccessToken;
import com.weibo.sdk.android.Weibo;
import com.weibo.sdk.android.WeiboAuthListener;
import com.weibo.sdk.android.WeiboDialogError;
import com.weibo.sdk.android.WeiboException;
import com.weibo.sdk.android.api.ActivityInvokeAPI;
import com.weibo.sdk.android.keep.AccessTokenKeeper;
import com.weibo.sdk.android.sso.SsoHandler;
import com.weibo.sdk.android.util.Utility;


/**
 * @author Eric(lqweric@gmail.com)
 *
 * 2013-8-16
 */
public class MainActivity extends SlidingActivity implements LeftFragment.WeiboCallback{
	
	private CanvasTransformer mTransformer; 
	 private Weibo mWeibo;
		public static final String CONSUMER_KEY = "2045436852";// 替换为开发者的appkey，例如"1646212860";
		public static final String REDIRECT_URL = "http://www.sina.com";
		
		public static Oauth2AccessToken accessToken ;
		public static final String TAG="sinasdk";
		/**
		 * SsoHandler 仅当sdk支持sso时有效，
		 */
		 SsoHandler mSsoHandler;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("jnuAssistant");
        setContentView(R.layout.frame_content);
        
		mWeibo = Weibo.getInstance(CONSUMER_KEY, REDIRECT_URL);

        
        initAnimation();  
        
        initSlidingMenu();  
                
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
   
      
    }
    
 
    
    
    private void initSlidingMenu() {

    	  // set the Behind View
        setBehindContentView(R.layout.frame_menu);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        LeftFragment leftFragment = new LeftFragment();
        fragmentTransaction.replace(R.id.menu, leftFragment);
        fragmentTransaction.replace(R.id.content, new ViewPagerFragment(),"Welcome");
        fragmentTransaction.commit();
    	
        // customize the SlidingMenu
      /*  SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidth(50);
        sm.setShadowDrawable(R.drawable.shadow);
        sm.setBehindOffset(80);
        sm.setFadeDegree(0.35f);
        //设置slding menu的几种手势模式
        //TOUCHMODE_FULLSCREEN 全屏模式，在content页面中，滑动，可以打开sliding menu
        //TOUCHMODE_MARGIN 边缘模式，在content页面中，如果想打开slding ,你需要在屏幕边缘滑动才可以打开slding menu
        //TOUCHMODE_NONE 自然是不能通过手势打开啦
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);

        //使用左上方icon可点，这样在onOptionsItemSelected里面才可以监听到R.id.home
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        */
    	SlidingMenu sm = getSlidingMenu();
		sm.setMode(SlidingMenu.LEFT);
		//sm.setBackgroundResource(R.drawable.slidingmenu_bg);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);  
        sm.setBehindScrollScale(0.0f);  
        sm.setBehindCanvasTransformer(mTransformer);  
                
        setSlidingActionBarEnabled(true);
        
    }
    

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case android.R.id.home:
            //toggle就是程序自动判断是打开还是关闭
            toggle();
//          getSlidingMenu().showMenu();// show menu
       //   getSlidingMenu().showContent();//show content
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
	private void initAnimation(){  
		mTransformer = new CanvasTransformer(){  
			@Override
			public void transformCanvas(Canvas canvas, float percentOpen) {  
				float scale = (float) (percentOpen*0.25 + 0.75);  
				canvas.scale(scale, scale, canvas.getWidth()/2, canvas.getHeight()/2);                
			}

			
		};  
	} 
	
	private long exitTime = 0;
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		//双击退出
		if(keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN){
				if((System.currentTimeMillis()-exitTime) > 2000){
				Toast.makeText(getApplicationContext(), "再按一次后退键退出程序", Toast.LENGTH_SHORT).show();
				exitTime = System.currentTimeMillis();
				} else {
				//退出代码
					onDestroy();
				}
				return true;
				}
		return super.onKeyDown(keyCode, event);	
	}
	
	protected void onDestroy() {
		super.onDestroy();
		System.exit(0); // 结束进程
	}




	@Override
	public void onShareKey(boolean flag,String s) {
		// TODO Auto-generated method stub
		 MainActivity. accessToken = AccessTokenKeeper.readAccessToken(this);
	        if (MainActivity.accessToken.isSessionValid()) {
	            //goto share
	        	
	        	if(flag){
	        		share(s);
	        	}
	        	else{
	        		feedback();
	        	}
	        } else {
	        	/**
              * 下面两个注释掉的代码，仅当sdk支持sso时有效，
              */
             
             mSsoHandler =new SsoHandler(MainActivity.this,mWeibo);
             mSsoHandler.authorize( new AuthDialogListener());

	        }
	}
	
	
  
		private void share(String s){
			
			  ActivityInvokeAPI.openSendWeibo(this, s);
		}
		
		private void feedback(){
			
			  ActivityInvokeAPI.openSendWeibo(this, "請提出你的寶貴意見@劉橋偉");
		}


	 
		
		class AuthDialogListener implements WeiboAuthListener {

			@Override
			public void onComplete(Bundle values) {
				String token = values.getString("access_token");
				String expires_in = values.getString("expires_in");
				MainActivity.accessToken = new Oauth2AccessToken(token, expires_in);
				if (MainActivity.accessToken.isSessionValid()) {
					
					AccessTokenKeeper.keepAccessToken(MainActivity.this, accessToken);
					Toast.makeText(MainActivity.this, "认证成功", Toast.LENGTH_SHORT).show();
				     //认证成功，马上发送
					//share();
				}
			}

			@Override
			public void onError(WeiboDialogError e) {
				Toast.makeText(getApplicationContext(), "Auth error : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}

			@Override
			public void onCancel() {
				Toast.makeText(getApplicationContext(), "Auth cancel", Toast.LENGTH_LONG).show();
			}

			@Override
			public void onWeiboException(WeiboException e) {
				Toast.makeText(getApplicationContext(), "Auth exception : " + e.getMessage(),
						Toast.LENGTH_LONG).show();
			}
			
			

		}
		@Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        
	        /**
	         * 下面两个注释掉的代码，仅当sdk支持sso时有效，
	         */
	        if(mSsoHandler!=null){
	            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
	        }
	    }
}
