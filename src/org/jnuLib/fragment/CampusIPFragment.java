package org.jnuLib.fragment;

import org.jnuLib.data.GlobleData;
import org.jnuLib.ui.BookListActivity;
import org.jnuLib.ui.NetInfoActivity;
import org.jnuLib.ui.R;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author Eric(lqweric@gmail.com)
 *
 * 2013-8-16
 */
public class CampusIPFragment extends Fragment {
	
	private EditText student_no;
	private EditText student_name;
	private Button searchBtu;
    String text = null;
    View view=null;
    public CampusIPFragment() {
    }

   
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);     
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //inflater the layout 
        view = inflater.inflate(R.layout.search_campus_ip, null);
        searchBtu  =(Button)view.findViewById(R.id.Ip_Button);
        student_no=(EditText)view.findViewById(R.id.student_no);
        student_name=(EditText)view.findViewById(R.id.student_name);
        searchBtu.setOnClickListener(new OnClickListener(){
        	 
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
			GlobleData.SEARCHTEXT=	student_name.getText().toString();
	       GlobleData.SEARCHNO=student_no.getText().toString();
			
	       Intent intent = new Intent(getActivity(), NetInfoActivity.class);
	    
	       startActivity(intent);     
			}
        });
        return view;
    }
    
 
    
}
