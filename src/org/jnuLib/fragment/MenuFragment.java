package org.jnuLib.fragment;

import org.jnuLib.ui.FragmentAdapter;
import org.jnuLib.ui.MainActivity;
import org.jnuLib.ui.R;

import android.app.ActionBar;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.FrameLayout;

import com.slidingmenu.lib.SlidingMenu;

/**
 * @author Eric(lqweric@gmail.com)
 *menu fragment ，主要是用于显示menu菜单
 * 2013-8-16
 */
public class MenuFragment extends PreferenceFragment implements OnPreferenceClickListener{
    private int index = -1;
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
        addPreferencesFromResource(R.xml.menu);
        //add listener
        findPreference("Jnu").setOnPreferenceClickListener(this);
        findPreference("zhaopin").setOnPreferenceClickListener(this);
        findPreference("n").setOnPreferenceClickListener(this);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
       /* if("a".equals(key)) {
            //if the content view is that we need to show . show directly
            if(index == 0) {
                ((MainActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            //otherwise , replace the content view via a new Content fragment
            index = 0;
            mFrameLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            FragmentManager fragmentManager = ((MainActivity)getActivity()).getFragmentManager();
            ContentFragment contentFragment = (ContentFragment)fragmentManager.findFragmentByTag("A");
            fragmentManager.beginTransaction()
            .replace(R.id.content, contentFragment == null ?new ContentFragment():contentFragment ,"A")
            .commit();
        }else */
        	if("Jnu".equals(key)) {
        		initViewPager();
           /* if(index == 0) {
                ((MainActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            index = 0;*/
          /*  mFrameLayout.setVisibility(View.GONE);
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
                    .setText("书目检索")
                    .setTabListener(tabListener));

            actionBar.addTab(actionBar.newTab()
                    .setText("个人信息")
                    .setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab()
                    .setText("当前借阅")
                    .setTabListener(tabListener));*/
        }else if("zhaopin".equals(key)) {
            if(index == 1) {
                ((MainActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            index = 1;
            mFrameLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.VISIBLE);
            
            FragmentAdapter demoFragmentAdapter = new FragmentAdapter(mActivity.getFragmentManager());
            //初始化fragment
             demoFragmentAdapter.addFragment(new ContentFragment());
             demoFragmentAdapter.addFragment(new ContentFragment());
             demoFragmentAdapter.addFragment(new ContentFragment());
             mViewPager.setOffscreenPageLimit(5);
             mViewPager.setAdapter(demoFragmentAdapter);

            mViewPager.setOnPageChangeListener(onPageChangeListener);
            
            ActionBar actionBar = mActivity.getActionBar();
            actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
            actionBar.removeAllTabs();
            actionBar.addTab(actionBar.newTab()
                    .setText("招聘会")
                    .setTabListener(tabListener));

            actionBar.addTab(actionBar.newTab()
                    .setText("应届生")
                    .setTabListener(tabListener));
            actionBar.addTab(actionBar.newTab()
                    .setText("其他")
                    .setTabListener(tabListener));
        }else if("n".equals(key)) {

            if(index == 2) {
                ((MainActivity)getActivity()).getSlidingMenu().toggle();
                return true;
            }
            index = 2;
            mFrameLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.GONE);
            getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
            FragmentManager fragmentManager = ((MainActivity)getActivity()).getFragmentManager();
           
            
            ViewPagerFragment VFragment = (ViewPagerFragment)fragmentManager.findFragmentByTag("N");
            fragmentManager.beginTransaction()
            .replace(R.id.content, VFragment == null ? new ViewPagerFragment():VFragment)
            .commit();
        }
        //anyway , show the sliding menu
        ((MainActivity)getActivity()).getSlidingMenu().toggle();
        return false;
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
