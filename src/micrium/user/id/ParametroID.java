package micrium.user.id;

/**
 * Esta Clase tiene como finalidad principal identificar los distintos
 * parametros definidos en nuestro proyecto, los cuales son identificados por el
 * ID correspondiente al ID de la BD de la tabla Parametro.
 * 
 * @author: Cesar Augusto Choque S
 * @version: 20-04-2015
 * 
 * **/
public class ParametroID {

	/**
	 * Para nuevos Parametros es necesario agregar su respectivo atributo que lo
	 * identifique en el sistema, y que es representado por esta clase, el valor
	 * del paramtro debe ser el ID que lo identifica en la tabla Parametro.
	 * */
	public static final int IP_DESARROLLO = 1;
	public static final int DIRECTORIO_ACTIVO_initial_context_factory = 2;
	public static final int DIRECTORIO_ACTIVO_provider_url = 3;
	public static final int DIRECTORIO_ACTIVO_security_authentication = 4;
	public static final int DIRECTORIO_ACTIVO_security_principal = 5;
	public static final int DIRECTORIO_ACTIVO_security_credentials = 6;
	public static final int DIRECTORIO_ACTIVO_security_user = 7;
	public static final int DIRECTORIO_ACTIVO_dominio = 8;
	public static final int USUARIO_sw_active_directory = 9;
	public static final int USUARIO_login = 10;
	public static final int USUARIO_password = 11;
	public static final int USUARIO_nro_opciones = 12;
	public static final int USUARIO_tiempo_fuera = 13;
	public static final int ACCION_DELETE_ROLE_CASCADE = 14;
	public static final int USUARIO_ROL_DELETE_COLOR = 15;
	// public static final int testFecha = 16;

	public static final int EXPRESION_REGULAR_USUARIO = 16;
	public static final int MENSAJE_VALIDACION_USUARIO = 17;
	public static final int EXPRESION_REGULAR_PASSWORD = 18;
	public static final int MENSAJE_VALIDACION_PASSWORD = 19;
	public static final int EXPRESION_REGULAR_GENERAL = 20;
	public static final int MENSAJE_VALIDACION_GENERAL = 21;
	public static final int TITULO_APLICACION = 22;
	public static final int PIE_PAGINA = 23;

	public static final int CONTRASENIA_POR_DEFECTO=24;
	public static final int CONTRASENIA_GENERADA=25;
	public static final int LONGITUD_CONTRASENIA_GENERADA=26;
	public static final int DIAS_VIGENCIA_CONTRASENIA=27;
	public static final int CANTIDAD_CONTRASENIAS_ALMACENADAS=28;
	
	public static final int LISTA_DE_PLANES=51;
	public static final int EXPRESION_REGULAR_CADENA=52;
	
}
