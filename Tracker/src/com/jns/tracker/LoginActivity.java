package com.jns.tracker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.jns.tracker.util.Util;


public class LoginActivity extends Activity implements Runnable {
	/** Called when the activity is first created. */

	SharedPreferences sharedPreferences;
	SharedPreferences.Editor editarPreferencia;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.editarPreferencia = this.sharedPreferences.edit();
		
		setContentView(R.layout.login);
		Button btnLogar = (Button) findViewById(R.id.btnLogar);
		Button btSalvarConfig = (Button) findViewById(R.id.btsalvarconfig);
		final EditText edtUsuario = (EditText) findViewById(R.id.edtusuario);
		final EditText edtSenha = (EditText) findViewById(R.id.edtsenha);
		final EditText edtServer = (EditText) findViewById(R.id.edtserver);
		
		if (this.sharedPreferences.getString("server", "")!= null){
			edtServer.setText(this.sharedPreferences.getString("server", ""));
		} if (this.sharedPreferences.getString("server", "").equals("")){
			editarPreferencia.putString("server","julionevess.no-ip.org");
			editarPreferencia.commit();
		}
		
		Button btnCancelar = (Button) findViewById(R.id.btnCancelar);
		
		final CheckBox cbSalvarSenha = (CheckBox) findViewById(R.id.cbsalvarsenha);
		
		edtUsuario.setText("");
		edtSenha.setText("");
		cbSalvarSenha.setChecked(false);
		
		btnCancelar.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				finish();
			}
		});
		
		if (this.sharedPreferences.getString("login","") != null){
			edtUsuario.setText(this.sharedPreferences.getString("login",""));
			edtSenha.setText(this.sharedPreferences.getString("senha",""));
			cbSalvarSenha.setChecked(true);
		}
		
		btnLogar.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
								
//				try {
//					Connector connector = Connector.getInstancia(LoginActivity.this);
//					Usuario usuario = connector.logar(edtUsuario.getText().toString(), edtSenha.getText().toString());
//					if (usuario == null){
//						Util.abrirDialog(LoginActivity.this, "Usuário ou senha inválidos");
//					}else{
						if (cbSalvarSenha.isChecked()){
							editarPreferencia.putString("login",edtUsuario.getText().toString());
							editarPreferencia.putString("senha",edtSenha.getText().toString());
							
						}else{
							editarPreferencia.putString("login","");
							editarPreferencia.putString("senha","");
							edtUsuario.setText("");
							edtSenha.setText("");
						}
						editarPreferencia.commit();
						
						Util.abrirDialog(LoginActivity.this, "aaaa");
						
						new Thread(LoginActivity.this).start();  
						Intent it = new Intent(LoginActivity.this, Tracker.class);
						startActivity(it);
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					Toast.makeText(LoginActivity.this,"problemas na conexão com o servidor.",Toast.LENGTH_LONG).show();
//				} catch (ClassNotFoundException e) {
//					e.printStackTrace();
//				} catch (ParseException e) {
//					e.printStackTrace();
//				}
			}
		});
	}
	public void run() {
		
	}
}