package org.jnuLib.fragment;

import org.jnuLib.ui.FragmentAdapter;
import org.jnuLib.ui.MainActivity;
import org.jnuLib.ui.R;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.slidingmenu.lib.SlidingMenu;

/**
 * @author Eric(lqweric@gmail.com)
 *
 * 2013-8-16
 */
public class ViewPagerFragment extends Fragment {
    private ViewPager mViewPager = null;
    private FrameLayout mFrameLayout = null;
    private MainActivity   mActivity = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mActivity = (MainActivity)getActivity();
        mViewPager = (ViewPager)mActivity.findViewById(R.id.viewpager);
        mFrameLayout = (FrameLayout)mActivity.findViewById(R.id.content);
        //set the preference xml to the content view
        initViewPager();
       
    }

    
    public void initViewPager() {

            mFrameLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            
            FragmentAdapter demoFragmentAdapter = new FragmentAdapter(mActivity.getFragmentManager());
           //初始化fragment
            demoFragmentAdapter.addFragment(new ContentFragment());
            demoFragmentAdapter.addFragment(new JWCNewsFragment());
            demoFragmentAdapter.addFragment(new CampusIPFragment());
            mViewPager.setOffscreenPageLimit(5);
            mViewPager.setAdapter(demoFragmentAdapter);
            mViewPager.setOnPageChangeListener(onPageChangeListener);
            
            ActionBar actionBar = mActivity.getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.removeAllTabs();
            actionBar.addTab(actionBar.newTab()
                    .setText("图书馆书目检索")
                    .setTabListener(tabListener));

            actionBar.addTab(actionBar.newTab()
                    .setText("教务处通知")
                    .setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab()
                    .setText("查询校园网IP")
                    .setTabListener(tabListener));
        
    }
    private SlidingMenu getSlidingMenu() {
        return mActivity.getSlidingMenu();
    }
    ViewPager.SimpleOnPageChangeListener onPageChangeListener = new ViewPager.SimpleOnPageChangeListener() {
        @Override
        public void onPageSelected(int position) {
            getActivity().getActionBar().setSelectedNavigationItem(position);
            switch (position) {
                case 0:
                    getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                   // Toast.makeText(mActivity, "00", Toast.LENGTH_SHORT).show();
                    break;
               
                default:
                    getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                    break;
            }
        }

    };
    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mViewPager.getCurrentItem() != tab.getPosition())
                mViewPager.setCurrentItem(tab.getPosition());
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

        }
    };
	
}
