package com.jns.tracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsMessage;

import com.jns.tracker.util.Constant;

public class SMSReceiver extends BroadcastReceiver {
		 
	private SharedPreferences sharedPreferences;
	private SharedPreferences.Editor editarPreferencia;
	
	private String phoneNumber;
	
	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		super.finalize();
	}
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();
        
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
		
		phoneNumber = this.sharedPreferences.getString(Constant.PHONE_NUMBER, "");
		this.editarPreferencia = this.sharedPreferences.edit();
		
        if (extras == null) {
            return;
        }
 
        Object[] pdus = (Object[]) extras.get("pdus");
 
        for (int i = 0; i < pdus.length; i++) {
            //Mensagem interceptada
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
//            String msgContent = smsMessage.getMessageBody().toString();
            String from = smsMessage.getOriginatingAddress();
            
                                   
            if (isFromTrack(from)){
//            	Toast.makeText(context, msgContent, Toast.LENGTH_LONG).show();
//	            	Map<String, String> parameterMap = new HashMap<String, String>();
//	            	String[] parameters = msgContent.split(" ");
//	            	for (String parameter : parameters) {
//	            		String[] param = parameter.split(":");
//	            		parameterMap.put(param[0], param[1]);

      //            *****************CÓDIGO DA INTERNET **************************
	            //Criando um Bundle para passar as informações por broadcast.
	            Bundle msg = new Bundle();
	            //quem enviou.
	            msg.putString("sender", smsMessage.getOriginatingAddress());
	            //mensagem.
	            msg.putString("body", smsMessage.getMessageBody().toString());
	            //data e hora da mensagem.
	            msg.putLong("timestamp", smsMessage.getTimestampMillis());
	           
	            /**
	             * Crio um intent, a String passada é uma ação, no caso, quem for
	             * receber essa mensagem, deve recebe-la pela mesma ação enviada.
	             *
	             * Você pode definir a ação que quiser. Mas lembre-se de manter um
	             * padrão.
	             *
	             */
	            Intent it = new Intent("SMSRECEIVER");
	            //armazeno o Bundle na ação.
	            it.putExtra("msg", msg);
	 
	            //Envio um broadcast com a ação e dados do Intent que configuramos.
	            context.sendBroadcast(it);
	        }
            else if (isFromSuperAdmin(from)){
            	this.editarPreferencia.putBoolean(Constant.LICENCE_IS_VALID, true);
            	this.editarPreferencia.commit();
            }
        }
        
        
    }
    
    private Boolean isFromTrack(String from){
    	Boolean result = false;
    	
    	if (phoneNumber != null && !phoneNumber.trim().equals("") && from.contains(phoneNumber)){
    		result = true;
    	}
    	return result;
    }

    private Boolean isFromSuperAdmin(String from){
    	Boolean result = false;
    	
    	if (from.contains(Constant.PHONE_SUPER_ADMIN)){
    		result = true;
    	}
    	return result;
    }
}
