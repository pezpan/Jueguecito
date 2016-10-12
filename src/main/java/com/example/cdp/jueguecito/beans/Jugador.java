package com.example.cdp.jueguecito.beans;

public class Jugador {
	
	public String nombre;
	public int color;
	public int casilla;
	public int puntuacion;

	public Jugador(){
		puntuacion = 0;
		casilla = 0;
	}
	
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public int getColor() {
		return color;
	}
	public void setColor(int color) {
		this.color = color;
	}
	public int getCasilla() {
		return casilla;
	}
	public void setCasilla(int casilla) {
		this.casilla = casilla;
	}
	public int getPuntuacion() {
		return puntuacion;
	}
	public void setPuntuacion(int puntuacion) {
		this.puntuacion = puntuacion;
	}
	
}
