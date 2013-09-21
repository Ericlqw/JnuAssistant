package org.jnuLib.fragment;

import org.jnuLib.data.GlobleData;
import org.jnuLib.ui.BookListActivity;
import org.jnuLib.ui.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Eric(lqweric@gmail.com)
 *
 * 2013-8-16
 */
public class ContentFragment extends Fragment {
	
	private EditText searchText;
    String text = null;
    View view=null;
    public ContentFragment() {
    }

   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);     
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflater the layout 
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 

        view = inflater.inflate(R.layout.fragment_text, null);
        Button searchBtu=(Button)view.findViewById(R.id.searchButton);
         searchText=(EditText)view.findViewById(R.id.searchText);
        
        searchBtu.setOnClickListener(new OnClickListener(){
        	 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			GlobleData.SEARCHTEXT=	searchText.getText().toString();
	       Intent intent = new Intent(getActivity(), BookListActivity.class);
	     //  Bundle bundle=new Bundle(); 

         //  bundle.putString("searchText", searchText.getText().toString()); 

          // bundle.putString("content",content); 
          // ContentFragment ontentFragment  =new ContentFragment();
          // view.seta
           
        //   intent.putExtras(bundle); 
          // ontentFragment.setArguments(args)
           //也可以用这种方式传递. 

        //   intent.putExtra("result", "android"); 

            
	       
	       startActivity(intent);     
			}
        });
        return view;
    }
    
 
    
}
