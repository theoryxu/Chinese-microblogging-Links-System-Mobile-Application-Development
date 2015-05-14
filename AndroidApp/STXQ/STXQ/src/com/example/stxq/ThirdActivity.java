package com.example.stxq;

import tool.network.EntityDisambiguationService;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class ThirdActivity extends Activity {

	public ListView listView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_third);
		//获取列表控件
		listView = (ListView)findViewById(R.id.MyListView);
		//设置网络传输进度条
		final Dialog dialog = new Dialog(this, R.style.NoTitle);
		dialog.setContentView(R.layout.loading);
		dialog.setCancelable(false);
		dialog.setTitle("XX");
		dialog.show();
        //使用Handler保证网络数据接收到的时候马上展示给用户
		final Handler handler = new Handler(){
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				
				String[] text = (String[])msg.getData().get("text");				
				listView.setAdapter(new ArrayAdapter<String>(ThirdActivity.this, 
						android.R.layout.simple_list_item_checked, text));
				dialog.dismiss();
			}
		};
		
		Thread thread = new Thread(){
			public void run() {
				String[] text = EntityDisambiguationService.formDisambiguatedResult(EntityDisambiguationService.disambiguate());
				
				Message msg = new Message();
				Bundle bundle = new Bundle();
				bundle.putStringArray("text", text);
				msg.setData(bundle);
				
				handler.sendMessage(msg);
			}
		};
		
		thread.start();
		
				
		listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent= new Intent();        
			    intent.setAction("android.intent.action.VIEW");    
			    Uri content_url = Uri.parse(EntityDisambiguationService.getItemLinks()[arg2]);  
			    intent.setData(content_url);  
			    startActivity(intent);
			}

        });
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.third, menu);
		return true;
	}

}
