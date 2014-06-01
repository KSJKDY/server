package kr.co.kumoh.neighbor;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class AlarmActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alarm);

		Button MapButton = (Button) findViewById(R.id.button1);
		Button HomeButton = (Button) findViewById(R.id.button2);
		Button RankingButton = (Button) findViewById(R.id.button3);
		Button ProfileButton = (Button) findViewById(R.id.button4);
		Button SettingButton = (Button) findViewById(R.id.button5);

		MapButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ShowMap.class);
				startActivity(intent);
				finish();
			}
		});
		HomeButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ShowProfile.class);
				startActivity(intent);
				finish();
			}
		});
		RankingButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						RankActivity.class);
				startActivity(intent);
				finish();
			}
		});
		ProfileButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ShowProfile.class);
				startActivity(intent);
				finish();
			}
		});
		SettingButton.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						SettingActivity.class);
				startActivity(intent);
				finish();
			}
		});

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {
		case KeyEvent.KEYCODE_BACK:

			Toast.makeText(this, "뒤로가기버튼 눌림", Toast.LENGTH_SHORT).show();

			new AlertDialog.Builder(this)
					.setTitle("프로그램 종료")
					.setMessage("프로그램을 종료 하시겠습니까?")
					.setPositiveButton("예",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									android.os.Process
											.killProcess(android.os.Process
													.myPid());
								}
							}).setNegativeButton("아니오", null).show();
			break;
		default:
			break;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

	}

}
