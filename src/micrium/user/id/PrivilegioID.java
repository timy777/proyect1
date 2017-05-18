package micrium.user.id;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 * Esta Clase tiene como objetivo unico el de proporcionar todas las acciones
 * disponibles en el sistema y puedan ser consultadas desde las diferentes
 * vistas xhtml.
 * 
 * @author: Cesar Augusto Choque S.
 * @version: 20-04-2015
 * **/
@ManagedBean(name = "privilegio")
@ViewScoped
public class PrivilegioID implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * En caso de ser necesario es posible que se creen mas acciones,
	 * dependiendo de las necesidades del proyecto, por lo cual debera incluirse
	 * las nuevas acciones aqui, manteniendo el comportamiento de la clase.
	 * */

	public static final String ACCION_CREAR = "1";
	public static final String ACCION_MODIFICAR = "2";
	public static final String ACCION_ELIMINAR = "3";
	public static final String ACCION_EXPORTAR = "4";
	public static final String ACCION_REINTENTO_SINGLE = "5";
	public static final String ACCION_REINTENTO_ALL = "6";
	public static final String ACCION_INICIAR_PROCESS = "7";
	public static final String ACCION_PARAR_PROCESS = "8";
	public static final String ACCION_ENVIAR_MAIL = "9";
	public static final String ACCION_CONFIGURAR = "10";
	public static final String ACCION_CARGA_MASIVA = "11";
	public static final String ACCION_BUSCAR = "12";
	public static final String ACCION_PERMISOS_POR_TIPO_PARAMETRO = "13";
	public static final String ACCION_DESBLOQUEAR = "14";
	public static final String ACCION_VER_CONTRASENIA = "15";
	
	public String getACCION_CREAR() {
		return ACCION_CREAR;
	}

	public String getACCION_MODIFICAR() {
		return ACCION_MODIFICAR;
	}

	public String getACCION_ELIMINAR() {
		return ACCION_ELIMINAR;
	}

	public String getACCION_EXPORTAR() {
		return ACCION_EXPORTAR;
	}

	public String getACCION_REINTENTO_SINGLE() {
		return ACCION_REINTENTO_SINGLE;
	}

	public String getACCION_REINTENTO_ALL() {
		return ACCION_REINTENTO_ALL;
	}

	public String getACCION_INICIAR_PROCESS() {
		return ACCION_INICIAR_PROCESS;
	}

	public String getACCION_PARAR_PROCESS() {
		return ACCION_PARAR_PROCESS;
	}

	public String getACCION_ENVIAR_MAIL() {
		return ACCION_ENVIAR_MAIL;
	}

	public String getACCION_CONFIGURAR() {
		return ACCION_CONFIGURAR;
	}

	public String getACCION_CARGA_MASIVA() {
		return ACCION_CARGA_MASIVA;
	}

	public String getACCION_BUSCAR() {
		return ACCION_BUSCAR;
	}

	public String getPERMISOS_POR_TIPO_PARAMETRO() {
		return ACCION_PERMISOS_POR_TIPO_PARAMETRO;
	}

	public String getACCION_DESBLOQUEAR() {
		return ACCION_DESBLOQUEAR;
	}

	public String getACCION_VER_CONTRASENIA() {
		return ACCION_VER_CONTRASENIA;
	}

	
	 
}
