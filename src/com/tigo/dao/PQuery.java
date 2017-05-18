package com.tigo.dao;

import java.util.HashMap;
import java.util.Map;

/*
 * Clase utilitaria para parar parametros en una consulta
 */

public class PQuery {

	private Map<String, Object> parametros;

	private PQuery() {
		this.parametros = new HashMap<String, Object>();
	}

	public void put(String nombre, Object valor) {
		parametros.put(nombre, valor);
	}

	public Map<String, Object> getParametros() {
		return parametros;
	}

	public static PQuery getInstancia() {
		return new PQuery();
	}

}
