package kr.co.kumoh.neighbor;

import static kr.co.kumoh.neighbor.MainFragment.userNameView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Vector;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class ShowProfile extends ImageSelectHelperActivity{
	
	String myName, myAge, mySex, myPhone, myAddress;
	TextView result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.showprofile);

		Button MapButton = (Button) findViewById(R.id.button1);
		Button HomeButton = (Button) findViewById(R.id.button2);
		Button RankingButton = (Button) findViewById(R.id.button3);
		Button AlarmButton = (Button) findViewById(R.id.button8);
		Button SettingButton = (Button) findViewById(R.id.button5);
		Button ModificationButton = (Button) findViewById(R.id.button6);
		Button ConfirmationButton = (Button) findViewById(R.id.button7);
		final EditText NameEditText = (EditText) findViewById(R.id.name);
		final EditText AgeEditText = (EditText) findViewById(R.id.age);
		final EditText SexEditText = (EditText) findViewById(R.id.sex);
		final EditText ContactEditText = (EditText) findViewById(R.id.phone);
		final EditText AddressEditText = (EditText) findViewById(R.id.address);
		
		result = (TextView)findViewById(R.id._result);
		
		findViewById(R.id.imageView2).setOnClickListener(new View.OnClickListener(){
			@Override
			public void onClick(View v) {
				startSelectImage();
				getSelectedImageFile();
			}
		});
		

		MapButton.setOnClickListener(new OnClickListener() { //map버튼 누르면 showmap 클래스 실행
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ShowMap.class);
				startActivity(intent);
				finish();
			}
		});
		HomeButton.setOnClickListener(new OnClickListener() {   // homw버튼 누르면 showprofile 클래스 실행
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						ShowProfile.class);
				startActivity(intent);
				finish();
			}
		});
		



		
		 RankingButton.setOnClickListener(new OnClickListener() { public void
		 onClick(View v) { Intent intent = new
		 Intent(getApplicationContext(),RankActivity.class);
		 startActivity(intent); finish(); } });
		 
		 AlarmButton.setOnClickListener(new OnClickListener() { public void
		 onClick(View v) { Intent intent = new
		 Intent(getApplicationContext(),AlarmActivity.class);
		 startActivity(intent); finish(); } });
		 SettingButton.setOnClickListener(new OnClickListener() { public void
		 onClick(View v) { Intent intent = new
		 Intent(getApplicationContext(),SettingActivity.class);
		 startActivity(intent); finish(); } });	

		ModificationButton.setOnClickListener(new OnClickListener() {  // 수정 누르면 적을수 있음 
			public void onClick(View v) {
				NameEditText.setEnabled(true);
				AgeEditText.setEnabled(true);
				SexEditText.setEnabled(true);
				ContactEditText.setEnabled(true);
				AddressEditText.setEnabled(true);
			}
		});
		ConfirmationButton.setOnClickListener(new OnClickListener() {  // 확인 누르면 갑이
			public void onClick(View v) {
				NameEditText.setEnabled(false);
				AgeEditText.setEnabled(false);
				SexEditText.setEnabled(false);
				ContactEditText.setEnabled(false);
				AddressEditText.setEnabled(false);
				
				myName = ((EditText)(findViewById(R.id.name))).getText().toString();
				myAge = ((EditText)(findViewById(R.id.age))).getText().toString();
				myPhone = ((EditText)(findViewById(R.id.phone))).getText().toString();
				myAddress = ((EditText)(findViewById(R.id.address))).getText().toString();
				mySex = ((EditText)(findViewById(R.id.sex))).getText().toString();
				
				new HttpTask().execute();
				
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
	
	// ------------------------------
	// Http Post로 주고 받기
	// ------------------------------
	class HttpTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpPost request_profile = new HttpPost(
						"http://ec2-54-200-168-218.us-west-2.compute.amazonaws.com/m_insert.php"); // 서버
																									// 연결

				Vector<NameValuePair> nameValue = new Vector<NameValuePair>();

				// 이름, 나이, 성별, 연락처, 주소

				nameValue.add(new BasicNameValuePair("name", userNameView.getText()
						.toString()));
				nameValue.add(new BasicNameValuePair("sex", mySex));
				nameValue.add(new BasicNameValuePair("phone", myPhone));
				nameValue.add(new BasicNameValuePair("address", myAddress));
				nameValue.add(new BasicNameValuePair("age", myAge));

				HttpEntity enty = new UrlEncodedFormEntity(nameValue, HTTP.UTF_8);

				request_profile.setEntity(enty);

				HttpClient client = new DefaultHttpClient();
				HttpResponse res = client.execute(request_profile);

				
				  HttpEntity entityResponse = res.getEntity(); 
				  InputStream im = entityResponse.getContent(); 
				  BufferedReader reader = new
				  BufferedReader( new InputStreamReader(im, HTTP.UTF_8));
				  
				  String total = ""; String tmp = ""; // 버퍼에 있는거 전부 더해주기 
				  //readLine -> 파일내용을 줄 단위로 읽기
				  
				  
				  while ((tmp = reader.readLine()) != null) { if (tmp != null) {
				  total += tmp; }
				  
				  }
				  
				 im.close();
				 

				// return total;

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// 오류시 null 반환
			return null;

		}

		protected void onPostExecute(String value) {
			super.onPostExecute(value);
			result.setText(value);

		}

	}

}


