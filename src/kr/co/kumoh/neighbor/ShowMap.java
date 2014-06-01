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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMyLocationChangeListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowMap extends FragmentActivity implements
		OnMyLocationChangeListener, LocationListener {

	private GoogleMap mMap;
	Location l;
	LocationManager l1;
	LatLng my_location1 = null;
	LatLng users_location = null;
	String latitude, longitude;
	Criteria criteria;
	Marker my_loc;
	Marker my_last_loc;
	Marker Marker1;
	Marker Marker2;
	Marker[] Marker3 = new Marker[30];
	Marker[] Marker4 = new Marker[30];
	int count = 0;
	double a1 = 0;
	double my_locationLat = 0;
	double my_locationLog = 0;
	double last_my_location_lat = 1;
	double last_my_location_log = 1;
	long end = 0;
	double userLa = 36.142955;
	double userLo = 128.394409;
	int c1 = 0;

	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.showmap);

		criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_COARSE);
		criteria.setAltitudeRequired(false);
		criteria.setBearingRequired(false);
		criteria.setSpeedRequired(false);

		LocationManager l1;
		l1 = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		String provider = l1.getBestProvider(criteria, true);

		l1.requestLocationUpdates(provider, 1000, 0, this);
		if (provider == null)
			provider = "network";
		Location location = l1.getLastKnownLocation(provider);

		mMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map)).getMap();
		mMap.setMyLocationEnabled(true);

		// 내위치 찾기
		mMap.setOnMyLocationChangeListener(ShowMap.this);
		// mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my_location1,16));
		// 다른사용자 위치 띄우기
	}

	public void onMyLocationChange(Location location) {
		// TODO Auto-generated method stub

		my_locationLat = location.getLatitude();
		my_locationLog = location.getLongitude();
		c1 = count;
		if (end == 0 || System.currentTimeMillis() > end + 3000) {
			// location table속의 사용자 찾기

			if (end != 0) {
				for (int c2 = 0; c2 < c1; c2++) {
					Marker4[c2].remove();
				}
			}
			count = 1 + (int) (Math.random() * 20);
			searchusers(count, userLa, userLo);
			userLa = userLa + 0.00001;
			userLo = userLo - 0.00004;

			end = System.currentTimeMillis();

		}
		if ((my_locationLat > last_my_location_lat + 0.00005 && my_locationLog > last_my_location_log + 0.00005)
				|| (my_locationLat > last_my_location_lat + 0.00005 && my_locationLog < last_my_location_log - 0.00005)
				|| (my_locationLat < last_my_location_lat - 0.00005 && my_locationLog > last_my_location_log + 0.00005)
				|| (my_locationLat < last_my_location_lat - 0.00005 && my_locationLog < last_my_location_log - 0.00005)
				|| last_my_location_lat == 0) {
			last_my_location_lat = my_locationLat;
			last_my_location_log = my_locationLog;

			if (my_location1 != null) {

				my_last_loc.remove();
				Marker2.remove();

			}

			// 마커 표시
			Bitmap marker_bmp = BitmapFactory.decodeResource(getResources(),
					R.drawable.aa);
			my_location1 = new LatLng(my_locationLat, my_locationLog);
			mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

			mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(my_location1, 16));

			String c = String.valueOf(my_locationLat);
			String d = String.valueOf(my_locationLog);

			Marker1 = mMap.addMarker(new MarkerOptions().position(my_location1)
					.title(c).snippet(d));
			my_loc = mMap.addMarker(new MarkerOptions().position(my_location1)
					.icon(BitmapDescriptorFactory.fromBitmap(marker_bmp)));

			my_last_loc = my_loc;
			Marker2 = Marker1;
		}

	}

	public void searchusers(int count, double userLa, double userLo) {

		for (int c3 = 0; c3 < count; c3++) {
			double a = 0.00001 * c3;
			double b = 0.00001 * c3;
			double userLa_temp = userLa + a;
			double userLo_temp = userLo + b;
			String f = String.valueOf(c3);
			users_location = new LatLng(userLa_temp, userLo_temp);
			Marker3[c3] = mMap.addMarker(new MarkerOptions()
					.position(users_location).title("마커").snippet(f));
			Marker4[c3] = Marker3[c3];

		}

	}
	
	/*

	class HttpTask extends AsyncTask<Void, Void, String> {

		@Override
		protected String doInBackground(Void... params) {
			// TODO Auto-generated method stub
			try {
				HttpPost request = new HttpPost(
						"http://ec2-54-200-168-218.us-west-2.compute.amazonaws.com/m_location.php"); // 좌표
																										// 테이블
																										// 서버로
																										// 연결

				Vector<NameValuePair> nameValue = new Vector<NameValuePair>();

				nameValue.add(new BasicNameValuePair("locationLat", Double
						.toString(my_locationLat))); // 경도 보내기
				nameValue.add(new BasicNameValuePair("locationLog", Double
						.toString(my_locationLog))); // 위도 보내기
				nameValue.add(new BasicNameValuePair("name", userNameView
						.getText().toString())); // 이름 보내기

				HttpEntity enty = new UrlEncodedFormEntity(nameValue,
						HTTP.UTF_8);

				request.setEntity(enty);

				HttpClient client = new DefaultHttpClient();
				HttpResponse res = client.execute(request);
				// 웹서버에서 값 받기

				HttpEntity entityResponse = res.getEntity();
				InputStream im = entityResponse.getContent();
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(im, HTTP.UTF_8));

				String total = "";
				String tmp = "";
				// 버퍼에 있는거 전부 더해주기
				// readLine -> 파일내용을 줄 단위로 읽기

				while ((tmp = reader.readLine()) != null) {
					if (tmp != null) {
						total += tmp;
					}

				}

				im.close();

				return total;

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
			// result.setText(value);

		}

	}
	*/

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}
}