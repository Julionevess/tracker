package com.jns.tracker.util;

import android.app.Dialog;
import android.content.Context;

public class Util {
	
	public static void abrirDialog(Context context, String texto) {
		Dialog dialog = new Dialog(context);
		dialog.setTitle(texto);
		dialog.setCancelable(true);
		dialog.setCanceledOnTouchOutside(true);
		dialog.show();
	}

}
