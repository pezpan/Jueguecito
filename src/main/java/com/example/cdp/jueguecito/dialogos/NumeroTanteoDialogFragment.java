package com.example.cdp.jueguecito.dialogos;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.widget.NumberPicker;

/**
 * Created by CDP on 17/10/2015.
 */
// Clase para el numero de jugadores
public class NumeroTanteoDialogFragment extends DialogFragment {

    private String titulo;
    private int posicion;
    private final int MAX = 20;
    private final int MIN = 1;

    public NumeroTanteoDialogFragment(){

    }

    public void getParameters(){
        Bundle bundle = getArguments();
        this.titulo = bundle.getString("titulo");
        this.posicion = bundle.getInt("posicion");
    }

    // Container Activity must implement this interface
    public interface NumberTanteoDialogListener {
        public void onNumberSelected(int number, int position);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Activity actividad = getActivity();
        final NumberPicker myNumberPicker = new NumberPicker(actividad);

        // Obtenemos los parametros
        getParameters();

        myNumberPicker.setMaxValue(MAX);
        myNumberPicker.setMinValue(MIN);

        final NumberTanteoDialogListener activity = (NumberTanteoDialogListener) getActivity();

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(actividad);
        builder.setTitle(this.titulo)
                .setView(myNumberPicker)
                .setNegativeButton("Restar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Declaramos un objeto de la interfaz que hemos definido para devolver el valor
                        activity.onNumberSelected(myNumberPicker.getValue() * (-1), posicion);
                    }
                })
                .setPositiveButton("Sumar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Declaramos un objeto de la interfaz que hemos definido para devolver el valor
                        activity.onNumberSelected(myNumberPicker.getValue(), posicion);
                    }
                });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
