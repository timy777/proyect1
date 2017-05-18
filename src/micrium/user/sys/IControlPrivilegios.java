package micrium.user.sys;

/**
 * Esta Interface define la manera de como se hara la interacion de los
 * Beans(Vistas) a la hora de establecer los permisos anivel de acciones, es
 * necesario redefinir los metodos en cada vista segun el formulario(xhtml) al
 * que representa el Bean.
 * 
 * @author: Cesar Augusto Choque S
 * @version: 20-04-2015
 * 
 * **/
public interface IControlPrivilegios {

	/**
	 * Este metodo indica si el usuario tiene permiso para realizar una
	 * determinada accion,
	 * 
	 * @param idAccion
	 *            es el codigo de la accion que queremos evaluar, si tiene
	 *            permizo o no de realizar la accion.
	 * @return indica si esta autorizado 'true', sino esta autorizado 'false'.
	 * @see micrium.user.id.PrivilegioID
	 * **/
	public boolean isAuthorized(int idAccion);

}
