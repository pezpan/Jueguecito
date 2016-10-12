package com.example.cdp.jueguecito.beans;


import android.graphics.Color;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Tablero {
	
	private static List<String> tablero;
	private static final int TAM_COLOR = 50;

	public static enum Fondos{
		AMARILLO("#ffeb3b", "amarillo"),
		NARANJA("#ff9800", "naranja"),
		ROJO("#f44336", "rojo"),
		NEGRO("#000000", "negro");

		private final String colorfondo;
		private final String txtcolor;

		Fondos(String valor, String color){
			this.colorfondo = valor;
			this.txtcolor = color;
		}

		public String getColorfondo() {
			return colorfondo;
		}

		public String getTxtcolor(){
			return txtcolor;
		}
	}

	private Tablero(){

	}
	
	public static List<String> getTablero(){
		return tablero;
	}
	
	public static void generarTablero(){
		// Creamos nuestro tablero
		Log.i("MILOG", "Creamos el tablero");
		tablero = new ArrayList<String>();
		// Preparamos el tablero, eligiendo los cuatro colores y colocandolos en orden aleatorio
		for (Fondos fondo : Fondos.values()){
			tablero.addAll(generaListaAleatoria(fondo.getTxtcolor()));
		}
	}
	
	private static List<String> generaListaAleatoria(String patron){

		Log.i("MILOG", "Generamos la lista aleatoria");
		List<String> casillas = new ArrayList<String>();
		// Primero vamos a generar una lista con los nombres de las imagenes
		for (int index = 1; index <= TAM_COLOR; index++)
		{
		    casillas.add(patron + "_" + String.valueOf(index));
		}
		// Ordenamos al azar
		long seed = System.nanoTime();
		Collections.shuffle(casillas, new Random(seed));
		// Devolvemos nuestra lista aleatoria
		return casillas;
	}
	
	// Devolvemos la carta situada en la casilla solicitada
	public static String getCasilla(int casilla){
		return tablero.get(casilla);
	}

}
