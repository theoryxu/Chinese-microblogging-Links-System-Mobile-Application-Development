package com.example.stxq;

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

public class MainActivity extends Activity {
	
	public EditText editText;
	public ImageButton button;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        
        editText = (EditText)findViewById(R.id.editText0);
        button = (ImageButton)findViewById(R.id.button0);
        button.setImageResource(R.drawable.next);
        
        button.setOnClickListener(new Button.OnClickListener(){
        	
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Editable text = editText.getText();
				EntityDisambiguationService.setWeibos(text.toString());
				
				Intent intent = new Intent(MainActivity.this, SecondActivity.class);
				startActivity(intent);
			} 
        	
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
