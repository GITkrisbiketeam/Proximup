package pl.krisbiketeam.proximup;

import android.app.Activity;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;

public class MainService extends Service implements SensorEventListener{
	// Debugging
	private static final String TAG = "MainService";
	private static final boolean D = true;
		
	private SensorManager mSensorManager;
	private Sensor mProximity;
	private PowerManager mPowerManager;
	private PowerManager.WakeLock mWakeLock;
	
	KeyguardManager keyguardManager;
	KeyguardLock lock;
	
	
	/* (non-Javadoc)
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		if (D) Log.d(TAG, "+ ON CREATE +");
        
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mProximity = mSensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
		
		super.onCreate();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if (D) Log.d(TAG, "- ON DESTROY -");

		mSensorManager.unregisterListener(this);
		if(mWakeLock.isHeld())
			mWakeLock.release();
		if(lock!= null)
			lock.reenableKeyguard();
		
		
		super.onDestroy();
	}

	/* (non-Javadoc)
	 * @see android.app.Service#onStartCommand(android.content.Intent, int, int)
	 */
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if (D) Log.d(TAG, "+ ON START COMMAND +");
		
		mSensorManager.registerListener(this, mProximity, SensorManager.SENSOR_DELAY_NORMAL);
		
		mPowerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		mWakeLock = mPowerManager.newWakeLock(PowerManager.SCREEN_DIM_WAKE_LOCK 
				 									| PowerManager.ACQUIRE_CAUSES_WAKEUP,
				 									"My Tag");
		keyguardManager = (KeyguardManager)getSystemService(Activity.KEYGUARD_SERVICE);
		lock = keyguardManager.newKeyguardLock(KEYGUARD_SERVICE);
		lock.disableKeyguard();


		//return super.onStartCommand(intent, flags, startId);
		return START_STICKY;
	}

	@Override
	public IBinder onBind(Intent arg0) {
		if (D) Log.d(TAG, "+ ON BIND +");
        // TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onAccuracyChanged(Sensor arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onSensorChanged(SensorEvent arg0) {
		if (D) Log.d(TAG, "+ ON SENSOR CHANGED +");
		
		float tmp = arg0.values[0];
		if(tmp != 0){
			
			if(mWakeLock.isHeld()){
				if (D) Log.d(TAG, "release WakeLock");				
				mWakeLock.release();
				
			}
			else{
				if (D) Log.d(TAG, "acquire WakeLock");				
				mWakeLock.acquire();
			}
			if(mPowerManager.isScreenOn()){
				//TODO: turn off thescreen
			}
		}
		
	}

}
