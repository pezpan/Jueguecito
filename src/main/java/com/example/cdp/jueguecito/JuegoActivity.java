package com.example.cdp.jueguecito;

import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.os.Bundle;
import android.widget.TextView;

import com.example.cdp.jueguecito.beans.Tablero;

import java.util.List;

public class JuegoActivity extends FragmentActivity {

    ImageFragmentPagerAdapter imageFragmentPagerAdapter;
    ViewPager viewPager;
    static List<String> cartas = null;
    private static int cartainicial = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartas);
        Log.i("MILOG", "Obtenemos el tablero");
        cartas = Tablero.getTablero();
        Log.i("MILOG", "El tamaño del tablero es: " + cartas.size());
        Bundle bundle = getIntent().getExtras();
        cartainicial = bundle.getInt("idcarta");
        Log.i("MILOG", "Carta inicial: " + cartainicial);
        Log.i("MILOG", "La carta es: " + cartas.get(cartainicial));
        imageFragmentPagerAdapter = new ImageFragmentPagerAdapter(getSupportFragmentManager());
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setAdapter(imageFragmentPagerAdapter);
        Log.i("MILOG", "La carta inicial es: " + ((Integer) cartainicial).toString());
        viewPager.setCurrentItem(cartainicial);
    }

    public static class ImageFragmentPagerAdapter extends FragmentPagerAdapter {
        public ImageFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return cartas.size();
        }

        @Override
        public Fragment getItem(int position) {
            SwipeFragment fragment = new SwipeFragment();
            Log.i("MILOG", "Obtenemos el fragment con la posicion: " + position);
            return SwipeFragment.newInstance(position);
        }
    }

    public static class SwipeFragment extends Fragment {
        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            Log.i("MILOG", "Entramos en swipefragment");
            View swipeView = inflater.inflate(R.layout.swipe_fragment, container, false);
            ImageView imageView = (ImageView) swipeView.findViewById(R.id.imageView);
            Bundle bundle = getArguments();
            int position = bundle.getInt("position");
            String imageFileName = cartas.get(position);
            TextView carta = (TextView)swipeView.findViewById(R.id.carta);
            carta.setText("Casilla: " + String.valueOf(position + 1));
            if(position < 50){
                carta.setBackgroundColor(Color.parseColor(Tablero.Fondos.AMARILLO.getColorfondo()));
                carta.setTextColor(Color.BLACK);
            }else if(position < 100){
                carta.setBackgroundColor(Color.parseColor(Tablero.Fondos.NARANJA.getColorfondo()));
                carta.setTextColor(Color.BLACK);
            }else if(position < 150){
                carta.setBackgroundColor(Color.parseColor(Tablero.Fondos.ROJO.getColorfondo()));
                carta.setTextColor(Color.BLACK);
            }else {
                carta.setBackgroundColor(Color.parseColor(Tablero.Fondos.NEGRO.getColorfondo()));
                carta.setTextColor(Color.WHITE);
            }

            Log.i("MILOG", "La imagen es: " + imageFileName);
            int imgResId = getResources().getIdentifier(imageFileName, "drawable", "com.example.cdp.jueguecito");
            imageView.setImageResource(imgResId);
            return swipeView;
        }

        static SwipeFragment newInstance(int position) {
            Log.i("MILOG", "Entramos en newInstance");
            SwipeFragment swipeFragment = new SwipeFragment();
            Bundle bundle = new Bundle();
            bundle.putInt("position", position);
            swipeFragment.setArguments(bundle);
            return swipeFragment;
        }
    }
}
