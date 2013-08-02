package pl.krisbiketeam.proximup;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private static final boolean D = true;
	
	
	CheckBox mCheckBox1;
	Intent serviceIntent;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (D) Log.d(TAG, "+ ON CREATE +");
        super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mCheckBox1 = (CheckBox)findViewById(R.id.checkBox1); 
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	/** Called when the user touches the button1 */
	public void button1Click(View view) {
		if (D) Log.d(TAG, "+ ON BUTTON 1 +");
		
		
	}
	
	/** Called when the user touches the checkBox1 */
	public void checkBox1Click(View view) {
		if (D) Log.d(TAG, "+ ON CHECK BOX 1 +");
        
		if(mCheckBox1.isChecked()){		//start the service
			serviceIntent = new Intent(this, MainService.class);
			startService(serviceIntent);
			
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			
		}
		else{								//stop the service
			if(serviceIntent!=null)
				stopService(serviceIntent);
			//getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);
			
		}
		
	}
	
}
