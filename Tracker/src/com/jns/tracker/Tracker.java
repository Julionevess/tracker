package com.jns.tracker;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.jns.tracker.util.Constant;

public class Tracker extends Activity implements OnItemClickListener {

    public static final String CONFIGURE_O_NUMERO_E_A_SENHA_DO_RASTREADOR_NAS_OPCOES_DE_CONFIGURACOES = "Configure o número e a senha do rastreador nas opções de configurações.";
    private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editarPreferencia;
	private GridView grid;
	
	private Switch swStop;
	private Switch swMode;
	private Switch swMove;
	private Switch swDistance;
	private Switch swFix;

	private String phoneNumber;
	private String passwordNumber;

    private AlertDialog.Builder alertConfirmationSMS;

	private String[] actions = {"Localizar", "Endereço", "Status" };
	int[] images = {R.drawable.localizar, R.drawable.endereco, R.drawable.ok};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		swStop = (Switch) findViewById(R.id.swblock);
		swMode = (Switch) findViewById(R.id.swmode);
		swMove = (Switch) findViewById(R.id.swmove);
		swDistance = (Switch) findViewById(R.id.swbdistance);
		swFix = (Switch) findViewById(R.id.swfix);
		
		this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		this.editarPreferencia = this.sharedPreferences.edit();

		initPreferences();

			CustomGrid adapter = new CustomGrid(Tracker.this, actions, images);
			grid = (GridView) findViewById(R.id.gridView);
			grid.setAdapter(adapter);
			grid.setOnItemClickListener(this);
			
			swStop.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (passwordNumber == null || passwordNumber.trim().equals("") || phoneNumber == null || phoneNumber.trim().equals("")) {
						openDialogOk(CONFIGURE_O_NUMERO_E_A_SENHA_DO_RASTREADOR_NAS_OPCOES_DE_CONFIGURACOES);
                        //Toast.makeText(Tracker.this, CONFIGURE_O_NUMERO_E_A_SENHA_DO_RASTREADOR_NAS_OPCOES_DE_CONFIGURACOES, Toast.LENGTH_SHORT).show();
					} else {
						String commandStop;
						if (swStop.isChecked()){
							commandStop = "stop" + passwordNumber;
							openDialogConfirmBlock(commandStop);						
						}else{
							commandStop = "begin" + passwordNumber;
							sendSMS(commandStop, Constant.ACAO_BLOQUEIO);
						}
					}
				}
			});
			
			swDistance.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (passwordNumber == null || passwordNumber.trim().equals("") || phoneNumber == null || phoneNumber.trim().equals("")) {
						Toast.makeText(Tracker.this, CONFIGURE_O_NUMERO_E_A_SENHA_DO_RASTREADOR_NAS_OPCOES_DE_CONFIGURACOES, Toast.LENGTH_SHORT).show();
					} else {
						if (swDistance.isChecked()){
							String commandDistance = "distance" + passwordNumber;
							openDialogDistance(commandDistance);
						}else{
							String commandDistanceOff = "no distance" + passwordNumber;
							sendSMS(commandDistanceOff, Constant.ACAO_ALARME_DISTANCIA );
						}
					}
				}
			});
			
			swMode.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (passwordNumber == null || passwordNumber.trim().equals("") || phoneNumber == null || phoneNumber.trim().equals("")) {
						Toast.makeText(Tracker.this, CONFIGURE_O_NUMERO_E_A_SENHA_DO_RASTREADOR_NAS_OPCOES_DE_CONFIGURACOES, Toast.LENGTH_SHORT).show();
					} else {
					String commandTracker;
						if (swMode.isChecked()){
							commandTracker = "tracker" + passwordNumber;
							sendSMS(commandTracker, Constant.ACAO_MODO_RASTREAMENTO);

						}else{
							commandTracker = "monitor" + passwordNumber;
							sendSMS(commandTracker, Constant.ACAO_MODO_RASTREAMENTO);
						}
					}
				}
			});
			
			swMove.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (passwordNumber == null || passwordNumber.trim().equals("") || phoneNumber == null || phoneNumber.trim().equals("")) {
						Toast.makeText(Tracker.this, CONFIGURE_O_NUMERO_E_A_SENHA_DO_RASTREADOR_NAS_OPCOES_DE_CONFIGURACOES, Toast.LENGTH_SHORT).show();
					} else {
					String commandMove;
						if (swMove.isChecked()){
							commandMove = "move" + passwordNumber;
							sendSMS(commandMove, Constant.ACAO_ALARME_MOVIMENTTO);
						}else{
							commandMove = "no move" + passwordNumber;
							sendSMS(commandMove, Constant.ACAO_ALARME_MOVIMENTTO);
						}
					}
				}
			});
	
			swFix.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (passwordNumber == null || passwordNumber.trim().equals("") || phoneNumber == null || phoneNumber.trim().equals("")) {
						Toast.makeText(Tracker.this, CONFIGURE_O_NUMERO_E_A_SENHA_DO_RASTREADOR_NAS_OPCOES_DE_CONFIGURACOES, Toast.LENGTH_SHORT).show();
					} else {
						String commandFix;
						if (swFix.isChecked()){
							commandFix = "fix";
							openDialogFix(commandFix);
						}else{
							commandFix = "nofix" + passwordNumber;
							sendSMS(commandFix, Constant.ACAO_LOCALIZACAO_REPETIDA);
						}
					}
				}
			});
		}

	private void initPreferences() {
		swStop.setChecked(this.sharedPreferences.getBoolean(Constant.COMMAND_STOP, false));
		swDistance.setChecked(this.sharedPreferences.getBoolean(Constant.COMMAND_DISTANCE, false));
		swFix.setChecked(this.sharedPreferences.getBoolean(Constant.COMMAND_FIX, false));
		swMode.setChecked(this.sharedPreferences.getBoolean(Constant.COMMAND_MODE, false));
		swMove.setChecked(this.sharedPreferences.getBoolean(Constant.COMMAND_MOVE, false));
		passwordNumber = this.sharedPreferences.getString(Constant.PASSWORD_NUMBER, "");
		phoneNumber = this.sharedPreferences.getString(Constant.PHONE_NUMBER, "");
	}

	@Override
	protected void onResume() {
		super.onResume();
		initPreferences();
		BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				//Pega os dados enviados pelo SMSReceiver.
				Bundle msg = intent.getBundleExtra("msg");
				String sender = msg.getString("sender");
				String body = msg.getString("body");
				long timestamp = msg.getLong("timestamp");

				//formatando data para exibi��o.
				Date date = new Date(timestamp);
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm - dd/MM/yyyy");

				//gera uma string com as informa��es.
				String mensagem = sdf.format(date) + "\nDe: " + sender + "\nMensagem: " + body;

				openDialogReturnSMS(body);
				
//				Toast.makeText(Tracker.this, mensagem, Toast.LENGTH_LONG).show();
			}
		};

		//Cria um filtro baseado na a��o configurada em nosso SMSReceiver.
		IntentFilter intentFilter = new IntentFilter("SMSRECEIVER");
		//Registra o broadcast.
		this.registerReceiver(broadcastReceiver, intentFilter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

        switch (id){
            case R.id.action_settings:
                Intent it = new Intent(Tracker.this, ConfigActivity.class);
                startActivity(it);
                break;
            case R.id.action_create_admin:
                String comanndAddAdmin = "admin" + passwordNumber;

                openDialogAdmin(comanndAddAdmin);
                break;
        }
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

		if (passwordNumber == null || passwordNumber.trim().equals("") || phoneNumber == null || phoneNumber.trim().equals("")) {
			Toast.makeText(Tracker.this, "Configure o número e a senha do rastreador nas opções de configurações.", Toast.LENGTH_SHORT).show();
		} else {
			String comanndAddAdmin = "admin" + passwordNumber;
			String comanndAddress = "adress" + passwordNumber;
			String comanndCheck = "check" + passwordNumber;
				if (position == Constant.ACAO_LOCALIZAR) {
					String newCommand = "fix020s001n" + passwordNumber;
					sendSMS(newCommand, Constant.ACAO_LOCALIZAR);
				} else if (position == Constant.ACAO_ENDERECO) {
					sendSMS(comanndAddress, Constant.ACAO_ENDERECO);
				} else if (position == Constant.ACAO_CHECK) {
					sendSMS(comanndCheck, Constant.ACAO_CHECK);
			}
		}
	}

	private void openDialogConfirmBlock(final String command) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Confirma Bloqueio?");
		alert.setIcon(R.drawable.config_icon);
		alert.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				sendSMS(command, Constant.ACAO_BLOQUEIO);
			}
		});

		alert.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
				swStop.setChecked(false);
			}
		});
		alert.show();
	}
	
	private void openDialogReturnSMS(String sms) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		final TextView message = new TextView(Tracker.this);
		final SpannableString s = new SpannableString(sms);
		Linkify.addLinks(s, Linkify.WEB_URLS);
		message.setText(s);
		message.setMovementMethod(LinkMovementMethod.getInstance());	  
		
		alert.setTitle("SMS Recebido");
		alert.setIcon(R.drawable.config_icon);
//		alert.setMessage(sms);
		alert.setView(message);
		alert.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();
	}
	
	public void openDialogConfirmationSMS() {
		alertConfirmationSMS = new AlertDialog.Builder(this);

		alertConfirmationSMS.setTitle("Envio de SMS");
		alertConfirmationSMS.setIcon(R.drawable.config_icon);
		alertConfirmationSMS.setMessage("Enviado com sucesso");
		alertConfirmationSMS.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alertConfirmationSMS.show();
	}

	private void openDialogErro(String sms) {
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);
		
		alert.setTitle("Erro");
		alert.setIcon(R.drawable.config_icon);
		alert.setMessage(sms);
		alert.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();
	}

	private void openDialogFix(final String command) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.dialog_parameter_fix, null);
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Parametros");
		alert.setIcon(R.drawable.config_icon);
		alert.setView(textEntryView);
		alert.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				final EditText interval = (EditText) textEntryView.findViewById(R.id.edtinterval);
				final EditText repetition = (EditText) textEntryView.findViewById(R.id.edtrepeticoes);
                interval.setText("030");
                repetition.setText("005");
                String pInterval = interval.getText().toString();
				String pRepetition = repetition.getText().toString();
				if (pInterval.length() > 3 || pRepetition.length() > 3){
					openDialogErro("Os valores 'INTERVALO' e 'REPETIÇÕES' precisam ter no máximo 3 digitos cada.");
				}else{
					if (pInterval.length() < 3){
						while (pInterval.length() < 3){
							pInterval = "0" + pInterval;
						}
					}
					if (pRepetition.length() < 3){
						while (pInterval.length() < 3){
							pInterval = "0" + pInterval;
						}
					}
					String newCommand = command + pInterval + "s" + pRepetition + "n" + passwordNumber;
					sendSMS(newCommand, Constant.ACAO_LOCALIZACAO_REPETIDA);
					}
			}
		});

		alert.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();
	}
	
	private void openDialogAdmin(final String command) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.dialog_parameter_admin, null);
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Cadastrar Admin");
		alert.setIcon(R.drawable.config_icon);
		alert.setView(textEntryView);
		alert.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				final EditText admin = (EditText) textEntryView.findViewById(R.id.edtnumberphoneadmin);
				String numberPhone = admin.getText().toString();
				String newCommand = command + " " + numberPhone;
				sendSMS(newCommand, Constant.ACAO_CADASTRAR_ADMIN);
			}
		});

		alert.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();
	}
	
	private void openDialogDistance(final String command) {
		LayoutInflater factory = LayoutInflater.from(this);
		final View textEntryView = factory.inflate(R.layout.dialog_parameter_distance, null);
		final AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("Informar a distancia");
		alert.setIcon(R.drawable.config_icon);
		alert.setView(textEntryView);
		alert.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				final EditText distance = (EditText) textEntryView.findViewById(R.id.edtdistance);
				String pDistance = distance.getText().toString();
				String newCommand = command + " " + pDistance;
				sendSMS(newCommand, Constant.ACAO_ALARME_DISTANCIA );
			}
		});

		alert.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.cancel();
			}
		});
		alert.show();
	}

    private void openDialogOk(final String mensagem) {
        LayoutInflater factory = LayoutInflater.from(this);
        final View textEntryView = factory.inflate(R.layout.dialog_ok, null);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Status da mensagem");
        alert.setMessage(mensagem);
        alert.setIcon(R.drawable.ok_mini);
        alert.setView(textEntryView);
        alert.setPositiveButton(R.string.label_ok, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });
/*
        alert.setNegativeButton(R.string.label_cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.cancel();
            }
        });*/
        alert.show();
    }
	
	private void sendSMS(String message, Integer comando){
		new MinhaAsyncTask().execute(message, comando.toString());
	}

	private void sendSMSManager(final String message, final String comando)
    {
            BroadcastReceiver broadcastSendSMS = new BroadcastReceiver(){
	        @Override
	        public void onReceive(Context  arg0, Intent arg1) {
	            switch (getResultCode())
	            {
	                case Activity.RESULT_OK:
                        openDialogOk("A mensagem foi enviada com sucesso");
	                   // Toast.makeText(getBaseContext(), "SMS enviado com sucesso. ",Toast.LENGTH_SHORT).show();
//	                	alertConfirmationSMS.create();
//	                	alertConfirmationSMS.setMessage("Enviado com Sucesso.");
	                	break;
	                case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        openDialogOk("Falha Genérica no envio da mensagem");
	                   // Toast.makeText(getBaseContext(), "Falha Genérica no envio da mensagem", Toast.LENGTH_SHORT).show();
	                    break;
	                case SmsManager.RESULT_ERROR_NO_SERVICE:
                        openDialogOk("Serviço de envio de mensagens indisponível");
	                   // Toast.makeText(getBaseContext(), "Serviço de envio de mensagens indisponível", Toast.LENGTH_SHORT).show();
	                    break;
	                case SmsManager.RESULT_ERROR_NULL_PDU:
                        openDialogOk("Null PDU");
	                    //Toast.makeText(getBaseContext(), "Null PDU", Toast.LENGTH_SHORT).show();
	                    break;
	                case SmsManager.RESULT_ERROR_RADIO_OFF:
                        openDialogOk("Sem sinal");
	                    //Toast.makeText(getBaseContext(), "Sem sinal", Toast.LENGTH_SHORT).show();
	                    break;
	            }
	            unregisterReceiver(this);
	        }
	       
	    };
	    
	    
	    BroadcastReceiver broadcastResultSMS = new BroadcastReceiver(){
	        @Override
	        public void onReceive(Context  arg0, Intent arg1) {
	            switch (getResultCode())
	            {
	                case Activity.RESULT_OK:

                        openDialogOk("Comando entregue ao destinatario com sucesso");

                        switch (Integer.parseInt(comando)){
                            case 0:
                                editarPreferencia.putString(Constant.ADMIN_UM, message.substring(7));
                                editarPreferencia.commit();
                                sharedPreferences.getString(Constant.ADMIN_UM, "");
                                break;
                            case 4:
                                if (swMode.isChecked()){
                                    editarPreferencia.putBoolean(Constant.COMMAND_MODE, true);
                                }else{
                                    editarPreferencia.putBoolean(Constant.COMMAND_MODE, false);
                                }
                                editarPreferencia.commit();
                                break;
                            case 5:
                                if (swStop.isChecked()){
                                    editarPreferencia.putBoolean(Constant.COMMAND_STOP, true);
                                }else{
                                    editarPreferencia.putBoolean(Constant.COMMAND_STOP, false);
                                }
                                editarPreferencia.commit();
                                break;
                            case 6:
                                if (swFix.isChecked()){
                                    editarPreferencia.putBoolean(Constant.COMMAND_FIX, true);
                                }else{
                                    editarPreferencia.putBoolean(Constant.COMMAND_FIX, false);
                                }
                                    editarPreferencia.commit();
                                break;
                            case 7:
                                if (swDistance.isChecked()){
                                    editarPreferencia.putBoolean(Constant.COMMAND_DISTANCE, true);
                                }else{
                                    editarPreferencia.putBoolean(Constant.COMMAND_DISTANCE, false);
                                }
                                    editarPreferencia.commit();
                                break;
                            case 8:
                                if (swMove.isChecked()){
                                    editarPreferencia.putBoolean(Constant.COMMAND_MOVE, true);
                                }else{
                                    editarPreferencia.putBoolean(Constant.COMMAND_MOVE, false);
                                }
                                    editarPreferencia.commit();
                                break;
                        }
	                    break;
	                case Activity.RESULT_CANCELED:
                        openDialogOk("SMS não foi entregue, tente novamente em poucos estantes");
	                    //Toast.makeText(getBaseContext(), "SMS não foi entregue", Toast.LENGTH_SHORT).show();
	                    break;                        
	            }
	            unregisterReceiver(this);
	        }
	    };
	    
		phoneNumber = this.sharedPreferences.getString(Constant.PHONE_NUMBER, "");
        String  SENT = "SMS_SENT";
        String  DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
            new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
            new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(broadcastSendSMS, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(broadcastResultSMS, new IntentFilter(DELIVERED));        

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, sentPI, deliveredPI);
        
//        unregisterReceiver(broadcastSendSMS);
//        unregisterReceiver(broadcastResultSMS);
    }

	class MinhaAsyncTask 
	  extends AsyncTask <String, Void, String> {

	  private ProgressDialog progressDialog;

	  protected void onPreExecute() {
	    progressDialog = new ProgressDialog(Tracker.this);
	    progressDialog.setMessage("Aguarde...");
	    progressDialog.show();
	  }

	  protected String doInBackground(String... v) {
		  sendSMSManager(v[0], v[1] );
          //Toast.makeText(Tracker.this, "v0 = " + v[0], Toast.LENGTH_SHORT).show();
          //Toast.makeText(Tracker.this, "v1 = " + v[1], Toast.LENGTH_SHORT).show();

          return v[1];
	  }

	  protected void onPostExecute(String result) {
          Log.i("Tracker", "fim do task: " + result);
          progressDialog.dismiss();
	  }
	}
}
