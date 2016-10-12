package com.example.cdp.jueguecito;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.cdp.jueguecito.auxiliares.Dado;
import com.example.cdp.jueguecito.auxiliares.Utilidades;
import com.example.cdp.jueguecito.beans.Jugador;
import com.example.cdp.jueguecito.beans.PropertyReader;
import com.example.cdp.jueguecito.beans.Tablero;
import com.example.cdp.jueguecito.dialogos.MensajeDialogFragment;
import com.example.cdp.jueguecito.dialogos.NumeroTanteoDialogFragment;
import com.example.cdp.jueguecito.timepicker.MyTimePickerDialog;
import com.example.cdp.jueguecito.timepicker.TimePicker;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.example.cdp.jueguecito.R.drawable;
import static com.example.cdp.jueguecito.R.id;
import static com.example.cdp.jueguecito.R.layout;
import static com.example.cdp.jueguecito.R.raw;

public class MainActivity extends ActionBarActivity implements NumeroTanteoDialogFragment.NumberTanteoDialogListener {

    public Context context;
    private ListView listviewjugadores;
    List<Jugador> jugadores = new ArrayList<Jugador>();
    private AdaptadorTanteo adaptador;
    private Cronometro cronometro;
    private Properties properties;

    // Variables para el cronometro
    CustomClockListener clocklistener;
    TextView reloj;
    Button btnstarstop;
    Button btnresetear;
    long salto = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(layout.activity_juego);

        Tablero.generarTablero();
        crearJugadores();

        // Guardamos el contexto
        this.context = getApplicationContext();

        // Parametros
        listviewjugadores = (ListView) findViewById(id.jugadorestanteo);
        // Establecemos el adaptador
        Log.i("MILOG", "Establecemos el adaptador");
        adaptador = new AdaptadorTanteo(this, getTaskId(), jugadores);
        listviewjugadores.setAdapter(adaptador);
        setTitle("Jueguecito sexy");
        reloj = (TextView) findViewById(id.reloj);
        // Creamos el cronometro
        cronometro = new Cronometro(0, salto);
        // Generamos el fichero de propiedades con el tiempo de las cartas
        PropertyReader  propertyReader = new PropertyReader(context);
        properties = propertyReader.getMyProperties("cartas.properties");

        // Establecemos los listeners para los botones
        clocklistener = new CustomClockListener();
        btnstarstop = (Button) findViewById(id.starstop);
        btnresetear = (Button) findViewById(id.resetear);
        btnstarstop.setOnClickListener(clocklistener);
        btnresetear.setOnClickListener(clocklistener);
        reloj.setOnClickListener(clocklistener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_juego, menu);
        return true;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        ((AdaptadorTanteo) listviewjugadores.getAdapter()).notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id) {
            case R.id.reiniciarpartida:
                Log.i("MILOG", "Reiniciamos la partida");
                // Reiniciamos la partida
                reiniciarPartida();
                break;
            case R.id.tirardado:
                tirarDado();
                break;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onNumberSelected(int number, int position) {
        // Modificamos la puntuacion del jugador
        Log.i("MILOG", "Actualizamos los puntos con el dialog");

        // Actualizamos los jugadores
        int tantos = adaptador.jugadores.get(position).getPuntuacion();
        adaptador.jugadores.get(position).setPuntuacion(tantos + number);

        // Actualizamos el backup
        ((AdaptadorTanteo) listviewjugadores.getAdapter()).notifyDataSetChanged();
    }

    // Adaptador para el layout del listview
    public class AdaptadorTanteo extends ArrayAdapter<Jugador> {

        Activity context;
        List<Jugador> jugadores;
        ViewHolder holder;

        private List<Integer> mSelection = new ArrayList<Integer>();

        AdaptadorTanteo(Activity context, int textViewResourceId, List<Jugador> listajugadores) {
            super(context, textViewResourceId, listajugadores);
            this.context = context;
            this.jugadores = listajugadores;
        }

        public void removeSelection(int position) {
            mSelection.remove(Integer.valueOf(position));
            notifyDataSetChanged();
        }

        public void clearSelection() {
            mSelection = new ArrayList<Integer>();
            notifyDataSetChanged();
        }

        public View getView(final int position, View convertView, ViewGroup parent) {

            View item = convertView;

            // Optimizamos el rendimiento de nuestra lista
            // Si la vista no existe, la creamos
            if (item == null) {
                LayoutInflater inflater = context.getLayoutInflater();
                item = inflater.inflate(layout.tanteo_jugador, null);

                holder = new ViewHolder();

                // Declaramos el holder pasandole nuestras vistas
                holder.nombrejugador = (ImageView) item.findViewById(id.nombrejugador);
                holder.casilla = (TextView) item.findViewById(id.casilla);
                holder.botonmas = (ImageButton) item.findViewById(id.sumar);
                holder.botonmenos = (ImageButton) item.findViewById(id.restar);
                holder.botontiempo = (ImageButton) item.findViewById(id.tiempo);
                holder.puntos = (TextView) item.findViewById(id.tantos);

                // Establecemos el tag
                item.setTag(holder);
            }
            // Si la vista existe, la reusamos
            else {
                holder = (ViewHolder) item.getTag();
            }

            // Definimos lo que necesitamos para el cab
            //item.setBackgroundColor(getResources().getColor(android.R.color.background_light)); //default color
            item.setBackgroundColor(jugadores.get(position).getColor());

            if (mSelection.contains(position)) {
                item.setBackgroundColor(getResources().getColor(android.R.color.holo_blue_light));// this is a selected position so make it red
            }

            // Guardamos la posicion en el holder para usarlo en los listener
            holder.botonmas.setTag(position);
            holder.botonmenos.setTag(position);
            holder.casilla.setTag(position);
            holder.botontiempo.setTag(position);
            holder.puntos.setTag(position);

            // Definimos los listener para las vistas
            holder.listener = new CustomListener(position);
            holder.botonmenos.setOnClickListener(holder.listener);
            holder.botonmas.setOnClickListener(holder.listener);
            holder.nombrejugador.setOnClickListener(holder.listener);
            holder.casilla.setOnClickListener(holder.listener);
            holder.botontiempo.setOnClickListener(holder.listener);
            holder.puntos.setOnClickListener(holder.listener);


            // Establecemos el nombre por defecto
            int ic_gender = position == 0? drawable.ic_gender_male : drawable.ic_gender_female;
            holder.nombrejugador.setImageResource(ic_gender);
            holder.casilla.setText(String.valueOf(jugadores.get(position).getCasilla()));
            holder.puntos.setText(String.valueOf(jugadores.get(position).getPuntuacion()));

            // Ponemos el fondo de los botones con un tono mas oscuro
            // boton mas
            holder.botonmas.setBackgroundColor(Utilidades.getDarker(jugadores.get(position).getColor()));
            holder.botonmenos.setBackgroundColor(Utilidades.getDarker(jugadores.get(position).getColor()));
            holder.botonmenos.setBackgroundColor(Utilidades.getDarker(jugadores.get(position).getColor()));
            holder.botontiempo.setBackgroundColor(jugadores.get(position).getColor());
            holder.puntos.setBackgroundColor(Utilidades.getDarker(jugadores.get(position).getColor()));
            return (item);
        }

        public class CustomListener implements View.OnClickListener {
            private int position;

            protected CustomListener(int position) {
                this.position = position;
            }

            @Override
            public void onClick(View v) {
                //Comprobamos que vista ha lanzado el evento y lo gestionamos
                int valor = jugadores.get(position).getCasilla();
                switch (v.getId()) {
                    case id.sumar:
                        Log.i("MILOG", "Sumamos uno");
                        jugadores.get(position).setCasilla(valor + 1);
                        actualizar();
                        break;

                    case id.restar:
                        // Decrementamos el tanteo
                        Log.i("MILOG", "Restamos uno");
                        jugadores.get(position).setCasilla(valor - 1);
                        actualizar();
                        break;
                    case id.tiempo:
                        // Actualizamos el cronometro
                        if ((valor > 0) && (valor <= 200)) {
                            Log.i("MILOG", Tablero.getCasilla(valor - 1));
                            String strTiempo = properties.getProperty(Tablero.getCasilla(valor - 1));
                            Log.i("MILOG", strTiempo);
                            cronometro = new Cronometro(Long.parseLong(strTiempo), salto);
                            cronometro.setActuales(Long.parseLong(strTiempo));
                        }
                        break;
                    case id.nombrejugador:
                    case id.casilla:
                        if ((valor > 0) && (valor <= 200)) {
                            Intent intentcartas = new Intent(getApplicationContext(), JuegoActivity.class);
                            // Pasamos como datos el numero de jugadores seleccionados
                            Bundle b = new Bundle();
                            Log.i("MILOG", "Guardamos los parametros desde la pantalla inicial para llamar al intent de cartas");

                            Log.i("MILOG", "La carta es: " + (valor - 1));
                            b.putInt("idcarta", (valor - 1));
                            //Lo anadimos al intent
                            intentcartas.putExtras(b);
                            // Lanzamos la actividad
                            Log.i("MILOG", "Lanzamos la pantalla de las cartas");
                            startActivity(intentcartas);
                        }
                        break;
                    case id.tantos:
                        NumeroTanteoDialogFragment fragmentotantos = new NumeroTanteoDialogFragment();
                        Bundle bundletantos = new Bundle();
                        bundletantos.putInt("posicion", position);
                        fragmentotantos.setArguments(bundletantos);
                        FragmentManager fragmentManagerborrar = getFragmentManager();
                        fragmentotantos.show(fragmentManagerborrar, "Dialogo_tantos");
                        break;
                }
            }
        }
    }

    static class ViewHolder{
        ImageView nombrejugador;
        TextView puntos;
        TextView casilla;
        ImageButton botonmas;
        ImageButton botonmenos;
        ImageButton botontiempo;
        AdaptadorTanteo.CustomListener listener;
    }


    public void tirarDado(){
        // Lanzamos el dialog
        MensajeDialogFragment fragmentodado = new MensajeDialogFragment();
        Bundle bundlesdado = new Bundle();
        bundlesdado.putString("titulo", "Tu tirada");
        bundlesdado.putString("mensaje", String.valueOf(Dado.tirar()));
        bundlesdado.putInt("tamTexto", 40);
        fragmentodado.setArguments(bundlesdado);
        Log.i("MILOG", "Mostramos el dialog para tirar el dado");
        FragmentManager fragmentManagerdado = this.getFragmentManager();
        fragmentodado.show(fragmentManagerdado, "Dialogo_dado");
    }

    public void reiniciarPartida(){
        jugadores.get(0).setCasilla(0);
        jugadores.get(1).setCasilla(0);
        jugadores.get(0).setPuntuacion(0);
        jugadores.get(1).setPuntuacion(0);
        actualizar();
        Tablero.generarTablero();
    }

    public void actualizar() {
        Log.i("MILOG", "Actualizamos");
        ((AdaptadorTanteo) listviewjugadores.getAdapter()).notifyDataSetChanged();
    }

    public void crearJugadores(){

        Jugador player = new Jugador();
        player.setNombre("Jose");
        player.setPuntuacion(0);
        player.setCasilla(0);
        player.setColor(getResources().getColor(R.color.colorel));
        jugadores.add(player);

        player = new Jugador();
        player.setNombre("Cris");
        player.setPuntuacion(0);
        player.setCasilla(0);
        player.setColor(getResources().getColor(R.color.colorella));
        jugadores.add(player);
    }

    public class CustomClockListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            //Comprobamos que vista ha lanzado el evento y lo gestionamos
            switch (v.getId()) {
                case id.starstop:
                    if (cronometro.getActuales() > 0) {
                        if (cronometro.getEstaPausado()) {
                            Log.i("MILOG", "Arrancamos");
                            btnstarstop.setText("Parar");
                            btnstarstop.setBackgroundColor(getResources().getColor(R.color.colorpausa));
                            long cuantos = cronometro.getActuales();
                            Log.i("MILOG", "Quedan " + String.valueOf(cuantos) + " restantes");
                            cronometro = new Cronometro(cuantos, salto);
                            cronometro.setActuales(cuantos);
                            cronometro.setEstaPausado(false);
                            cronometro.start();
                        } else {
                            Log.i("MILOG", "Paramos");
                            cronometro.setEstaPausado(true);
                            btnstarstop.setText("Iniciar");
                            btnstarstop.setBackgroundColor(getResources().getColor(R.color.colorboton));
                            cronometro.pause();
                        }
                    }
                    break;

                case id.reloj:
                    Log.i("MILOG", "Entramos en configurar");
                    MyTimePickerDialog mTimePicker = new MyTimePickerDialog(MainActivity.this, new MyTimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int minute, int seconds) {
                            Log.i("MILOG", "Actualizamos el tiempo");
                            // TODO Auto-generated method stub
                            if(cronometro.getEstaPausado()) {
                                cronometro = new Cronometro(((minute * 60) + seconds) * 1000, salto);
                                cronometro.setActuales(((minute * 60) + seconds) * 1000);
                            }
                        }
                    }, 0, 0);
                    Log.i("MILOG", "Mostramos el timepicker");
                    mTimePicker.show();
                    break;
                case id.resetear:
                    cronometro.actualizaTiempo(0);
                    cronometro.resetear();
                    cronometro.cancel();
                    break;
            }
        }
    }

    public class Cronometro extends CountDownTimer {

        // Variables
        private boolean estaPausado = true;
        private long restantes = 0;
        private long actuales = 0;
        MediaPlayer reproductor;

        public Cronometro(long millisInFuture, long countDownInterval) {

            super(millisInFuture, countDownInterval);
            Log.i("MILOG", "Creamos el cronometro: " + String.valueOf(millisInFuture));
            actualizaTiempo(millisInFuture);

            // Creamos el reproductor para el sonido del final
            reproductor = MediaPlayer.create(MainActivity.this, raw.orgasmo);
        }

        public void resetear(){
            actuales = 0;
            restantes = 0;
        }

        public long getActuales() {
            return actuales;
        }

        public void setActuales(long restantes) {
            this.actuales = restantes;
        }

        public boolean getEstaPausado() {
            return estaPausado;
        }

        public void setEstaPausado(boolean estaPausado) {
            this.estaPausado = estaPausado;
        }

        private String formatTiempo(long millis){
            return String.format("%02d : %02d",
                    TimeUnit.MILLISECONDS.toMinutes(millis),
                    TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            );
        }

        public void actualizaTiempo(long millis){
            //this.milisegundos = millis;
            reloj.setText(formatTiempo(millis));
        }

        @Override
        public void onTick(long millisUntilFinished) {

            Log.i("MILOG", "OnTick restantes: " + String.valueOf(millisUntilFinished));
            actuales = millisUntilFinished;

            if (Math.round((float)millisUntilFinished / 1000.0f) != restantes)
            {
                restantes = Math.round((float)millisUntilFinished / 1000.0f);
                actualizaTiempo(restantes * 1000);
            }
            //Log.i("test", "millisUntilFinished = " + millisUntilFinished + " till finished = " + restantes);
        }

        @Override
        public void onFinish() {
            Log.i("MILOG", "Finalizamos");
            reproductor.start();
            btnstarstop.setText("Iniciar");
            btnstarstop.setBackgroundColor(getResources().getColor(R.color.colorboton));
        }

        public void pause(){
            Log.i("MILOG", "Paramos reloj");
            cancel();
            Log.i("MILOG", "Quedan " + String.valueOf(actuales));
        }
    }
}


