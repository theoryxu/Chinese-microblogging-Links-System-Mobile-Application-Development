package com.example.stxq;

import java.util.ArrayList;
import java.util.List;

import tool.network.EntityDisambiguationService;
import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class SecondActivity extends Activity {

	public EditText editText;
	public ImageButton buttonPrevious;
	public ImageButton buttonSearch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);
		
		editText = (EditText)findViewById(R.id.editText1);
		
		buttonPrevious = (ImageButton)findViewById(R.id.button1);
		buttonPrevious.setImageResource(R.drawable.previous);
		
		buttonSearch = (ImageButton)findViewById(R.id.button2);
		buttonSearch.setImageResource(R.drawable.search);
		
		buttonPrevious.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SecondActivity.this, MainActivity.class);
				startActivity(intent);
			}
			
		});
		
		buttonSearch.setOnClickListener(new Button.OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				
				List<String> text= new ArrayList<String>();
				text.add(editText.getText().toString());
				
				EntityDisambiguationService.setEntitys(text);
				
				Intent intent = new Intent(SecondActivity.this, ThirdActivity.class);
				startActivity(intent);
			}
			
		});
        
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.second, menu);
		return true;
	}

}
