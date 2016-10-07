package com.jns.tracker;

import com.jns.tracker.util.Constant;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ConfigActivity extends Activity {

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editarPreferencia;
	private String pnoneNumber ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.config);

		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.editarPreferencia = this.sharedPreferences.edit();

		pnoneNumber = this.sharedPreferences.getString(Constant.PHONE_NUMBER, "");
		
		final EditText edtPhone = (EditText) findViewById(R.id.edtphone);
		final EditText edtPassword = (EditText) findViewById(R.id.edtpassword);
		final Button btnSave = (Button) findViewById(R.id.btsalvarconfig);
		final Button btnCancel = (Button) findViewById(R.id.btcancelarconfig);
		final EditText edtadm1 = (EditText) findViewById(R.id.edtadm1);
		final EditText edtadm2 = (EditText) findViewById(R.id.edtadm2);
		final EditText edtadm3 = (EditText) findViewById(R.id.edtadm3);
		final EditText edtadm4 = (EditText) findViewById(R.id.edtadm4);
		final EditText edtadm5 = (EditText) findViewById(R.id.edtadm5);

		if (this.sharedPreferences.getString(Constant.PHONE_NUMBER, "") != null
				&& (this.sharedPreferences.getString(Constant.PASSWORD_NUMBER, "") != null)) {
			edtPhone.setText(this.sharedPreferences.getString(Constant.PHONE_NUMBER, ""));
			edtPassword.setText(this.sharedPreferences.getString(Constant.PASSWORD_NUMBER, ""));
			edtadm1.setText(this.sharedPreferences.getString(Constant.ADMIN_UM, ""));
			edtadm2.setText(this.sharedPreferences.getString(Constant.ADMIN_DOIS, ""));
			edtadm3.setText(this.sharedPreferences.getString(Constant.ADMIN_TRES, ""));
			edtadm4.setText(this.sharedPreferences.getString(Constant.ADMIN_QUATRO, ""));
			edtadm5.setText(this.sharedPreferences.getString(Constant.ADMIN_CINCO, ""));
			//		} if (this.sharedPreferences.getString("server", "").equals("")){
			//			editarPreferencia.putString("server","julionevess.no-ip.org");
			//			editarPreferencia.commit();
			//		}
		}

		btnSave.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				editarPreferencia.putString(Constant.PHONE_NUMBER, edtPhone.getText().toString());
				editarPreferencia.putString(Constant.PASSWORD_NUMBER, edtPassword.getText().toString());
				String admin2 = edtadm2.getText().toString();
				String admin3 = edtadm3.getText().toString();
				String admin4 = edtadm4.getText().toString();
				String admin5 = edtadm5.getText().toString();
				String admin2Preference = sharedPreferences.getString(Constant.ADMIN_DOIS, "");
				String admin3Preference = sharedPreferences.getString(Constant.ADMIN_TRES, "");
				String admin4Preference = sharedPreferences.getString(Constant.ADMIN_QUATRO, "");
				String admin5Preference = sharedPreferences.getString(Constant.ADMIN_CINCO, "");
				
				String content;
				if (!admin2.trim().equals("") && !admin2.equals(admin2Preference)){
					content = "admin" + admin2 + edtPassword.getText().toString();
					editarPreferencia.putString(Constant.ADMIN_DOIS, admin2);
					sendSMS(content);
				}
				if (!admin3.trim().equals("") && !admin3.equals(admin3Preference)){
					content = "admin" + admin3 + edtPassword.getText().toString();
					editarPreferencia.putString(Constant.ADMIN_TRES, admin3);
					sendSMS(content);
				}
				if (!admin4.trim().equals("") && !admin4.equals(admin4Preference)){
					content = "admin" + admin4 + edtPassword.getText().toString();
					editarPreferencia.putString(Constant.ADMIN_QUATRO, admin4);
					sendSMS(content);
				}
				if (!admin5.trim().equals("") && !admin5.equals(admin5Preference)){
					content = "admin" + admin5 + edtPassword.getText().toString();
					editarPreferencia.putString(Constant.ADMIN_CINCO, admin5);
					sendSMS(content);
				}
				editarPreferencia.commit();
				finish();
			}
		});
		
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ConfigActivity.this.finish();
			}
		});
		

	}
	private void sendSMS(String content) {
		try {
			pnoneNumber = this.sharedPreferences.getString(Constant.PHONE_NUMBER, "");
			SmsManager smsManager = SmsManager.getDefault();
			smsManager.sendTextMessage(pnoneNumber, null, content, null, null);
			Toast.makeText(getApplicationContext(), "SMS enviado com sucesso:\b " + content, Toast.LENGTH_LONG).show();
			
		} catch (Exception e) {
			Toast.makeText(getApplicationContext(), "Falha no envio da SMS", Toast.LENGTH_LONG).show();
			
			e.printStackTrace();
		}
	}
}
