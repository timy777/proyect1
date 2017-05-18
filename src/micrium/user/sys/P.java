package micrium.user.sys;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import javax.faces.context.FacesContext;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.persistence.metamodel.ListAttribute;
import javax.sql.DataSource;

import micrium.user.model.MuParametro;

import org.apache.log4j.Logger;

/**
 * Esta Clase implememta un patron de tipo Singleton para tener una sola
 * instancia de la lista de parametros que se cargan al monento de desplegar la
 * aplicacion.
 * 
 * @author: Cesar Augusto Choque S.
 * @version: 20-04-2015
 * 
 * */

public class P {
	private static Logger log = Logger.getLogger(P.class);
	private static HashMap<Integer, MuParametro> listParametro;

	/**
	 * Este metodo devuelve un objeto de tipo Parametro con todos sus atributos
	 * cargados que es identificado por medio del ID coincidente en la Tabla de
	 * parametros de la BD.
	 * 
	 * @param idParametro
	 *            es el que identifica al Parametro como unico objeto dentro el
	 *            conjunto de parametros, defeinidos en la Clase ParametroID
	 * @return Objeto Parametro si esque se encuentra en caso contrario devuelve
	 *         nulo
	 * @see micrium.user.id.ParametroID
	 * */
	public static synchronized MuParametro getParametro(Integer idParametro) {
		try {
			return cargarParametros().get(idParametro);
		} catch (SQLException e) {
			log.error("[Carga de Parametros] Error:" + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Este metodo devuelve de manera directa el valor del parametro segun el
	 * tipo de dato que esta definido dicho parametro y que es identificado por
	 * medio del ID coincidente en la Tabla de parametros de la BD, aqui se debe
	 * tomar en cuenta que cuando se usa este metodo, sabemos el tipo de
	 * parametro que estamos consultado y por ende sabemos que tipo de
	 * conversion debemos hacer.
	 * 
	 * @param idParametro
	 *            es el que identifica al Parametro como unico objeto dentro el
	 *            conjunto de parametros, defeinidos en la Clase ParametroID
	 * @return Objeto que puede ser de tipo String, Date, BigDecimal, Booleano
	 *         si esque se encuentra el parametro en caso contrario devuelve
	 *         nulo
	 * @see micrium.user.id.ParametroID
	 * */
	public static synchronized Object getParamVal(Integer idParametro) {
		try {
			MuParametro p = cargarParametros().get(idParametro);

			switch (p.getTipo()) {
			case MuParametro.TIPO_CADENA:
				return p.getValorCadena();

			case MuParametro.TIPO_COLOR:
				return p.getValorCadena();

			case MuParametro.TIPO_FECHA:
				return p.getValorFecha();

			case MuParametro.TIPO_NUMERICO:
				return p.getValorNumerico();

			case MuParametro.TIPO_BOOLEANO:
				return p.getValorBooleano();
			}

			return null;

		} catch (SQLException e) {
			log.error("[Carga de Parametros] Error:" + e.getMessage(), e);
			return null;
		}
	}

	/**
	 * Este metodo debe ser invocado cuando se haga alguna modificacion a un
	 * Parametro para que el cambio se manifieste en el resto del sistema. Si en
	 * el transcurso del Desarrollo se crean terceras clases que son de tipo
	 * singleton estas clases deberan proverer mecanismos para reinicar sus
	 * atributos propios para que desde aqui sean invocados y asi el cambio del
	 * Parametro sean aplicables en todo contexto.
	 * 
	 * **/
	public static synchronized void restartParameter() {
		log.info("****** Reiniciarparametros..");
		listParametro = null;
	}

	// private static synchronized HashMap<Integer, Parametro> inicializar() {
	// if (listParametro == null) {
	// listParametro = new HashMap<Integer, Parametro>();
	// return null;
	// }
	// return listParametro;
	// }

	private static synchronized HashMap<Integer, MuParametro> cargarParametros() throws SQLException {
		// if (inicializar() == null) {
		if (listParametro == null) {
			listParametro = new HashMap<Integer, MuParametro>();
			Connection conection = getConnection();
			String sql = "SELECT * FROM MU_PARAMETRO order by PARAMETRO_ID";
			Statement st = null;
			ResultSet r = null;
			if (conection != null) {
				
				try {
					st = conection.createStatement();
					if (st != null) {
						r = st.executeQuery(sql);
						while ((r != null) && r.next()) {
							MuParametro item = cargar(r);
							listParametro.put(item.getParametroId(), item);
							log.info(item);
						}
					}
				} finally {
					try {
						if (r != null) {
							r.close();
						}
					} catch (SQLException ex) {
						log.error(ex, ex);
					}
					try {
						if (st != null) {
							st.close();
						}
					} catch (SQLException ex) {
						log.error(ex, ex);
					}
					try {
						if (conection != null) {
							conection.close();
						}
					} catch (SQLException ex) {
						log.error(ex, ex);
					}
				}
			}
		}
		return listParametro;
	}

	private static MuParametro cargar(ResultSet rs) throws SQLException {
		MuParametro parametro;
//		DESCOMENTAR EL BigDecimal SI ESTA UTLIZANDO COMO BASE DE DATOS ORACLE, CASO NO HACER NADA SI ES BASE DE DATOS POSTGRES.
//		BigDecimal parametroId = (BigDecimal) rs.getObject("PARAMETRO_ID");
//		Integer parametroId = (Integer) rs.getObject("PARAMETRO_ID"); //VIEJA LÍNEA ORACLE
		Integer parametroId = Integer.parseInt( String.valueOf( rs.getObject("PARAMETRO_ID")));//Nueva línea Multi BD (ya no es necesario eso de modificar código si es oracle o postgre)

		String nombre = (String) rs.getObject("NOMBRE");

//		DESCOMENTAR EL BigDecimal SI ESTA UTLIZANDO COMO BASE DE DATOS ORACLE, CASO NO HACER NADA SI ES BASE DE DATOS POSTGRES.
//		BigDecimal tipo = (BigDecimal) rs.getObject("TIPO");
//		Integer tipo = (Integer) rs.getObject("TIPO");//VIEJA LÍNEA ORACLE
		Integer tipo = Integer.parseInt( String.valueOf( rs.getObject("TIPO")));//Nueva línea Multi BD (ya no es necesario eso de modificar código si es oracle o postgre)

		String valorCadena = (String) rs.getObject("VALORCADENA");

		parametro = new MuParametro(parametroId.intValue(), nombre, tipo.intValue(), valorCadena);

		java.sql.Timestamp valorFecha = (java.sql.Timestamp) rs.getObject("VALORFECHA");
		if (valorFecha != null)
			parametro.setValorFecha(valorFecha);
		else
			parametro.setValorFecha(null);

		BigDecimal valorNumerico = (BigDecimal) rs.getObject("VALORNUMERICO");
		if (valorNumerico != null)
			parametro.setValorNumerico(valorNumerico);
		else
			parametro.setValorNumerico(null);

		if (rs.getObject("VALORBOOLEANO") instanceof Boolean) {
			Boolean valorBooleano = (Boolean) rs.getObject("VALORBOOLEANO");
			if (valorBooleano != null) {
				parametro.setValorBooleano(valorBooleano);
			}
		}
		if (rs.getObject("VALORBOOLEANO") instanceof BigDecimal) {
			BigDecimal valorBooleano = (BigDecimal) rs.getObject("VALORBOOLEANO");
			if (valorBooleano != null) {
				if (valorBooleano.intValue() == 1)
					parametro.setValorBooleano(true);
				else
					parametro.setValorBooleano(false);
			}
		}

		return parametro;
	}

	private static Connection getConnection() {
		Context c;
		String param;
		try {
			c = new InitialContext();
			param = FacesContext.getCurrentInstance().getExternalContext().getInitParameter("dataSource.name.jni");
			log.info("Nombre parametro:" + param);
			try {
				return ((DataSource) c.lookup("java:/" + param)).getConnection();
			} catch (Exception e) {
				log.error("Fallo al crear la connection. ", e);
			}
		} catch (Exception e) {
			log.error("Fallo al crear el InitialContext. ", e);
		}
		return null;
	}
}
