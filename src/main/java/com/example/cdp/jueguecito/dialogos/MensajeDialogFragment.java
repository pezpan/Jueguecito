package com.example.cdp.jueguecito.dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TextView;

import com.example.cdp.jueguecito.R;

/**
 * Created by CDP on 13/01/2016.
 */
public class MensajeDialogFragment extends DialogFragment {
  
    private String titulo;
    private String mensaje;
    private int tamTexto;
  
  public MensajeDialogFragment(){
    
  }
  
  public void getParameters(){
        Bundle bundle = getArguments();
        this.titulo = bundle.getString("titulo");
        this.mensaje = bundle.getString("mensaje");
        this.tamTexto = bundle.getInt("tamTexto");
    }
  
  @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Activity actividad = getActivity();
        final TextView texto = new TextView(actividad);


        // Obtenemos los parametros
        getParameters();
        // Modificamos el textview
        texto.setText(mensaje);
        texto.setGravity(Gravity.CENTER_HORIZONTAL);
        texto.setPadding(20,20,20,20);
        texto.setTextSize(this.tamTexto);
        texto.setTextColor(Color.parseColor("#212121"));
        
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);
        builder.setTitle(titulo)
                .setView(texto)
                .setView(texto)
                .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dismiss();
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
