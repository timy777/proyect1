/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package micrium.user.controler;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.business.BitacoraBL;
import micrium.user.id.ParametroID;
import micrium.user.model.MuBitacora;
import micrium.user.sys.P;

/**
 * 
 * @author Marciano
 */
@Named
public class ControlTimeOutImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private BitacoraBL logBL;

	private static Map<String, NodoClient> mapClient = new HashMap<String, NodoClient>();
	private static Map<String, NodoIngresoUser> mapIngresClient = new HashMap<String, NodoIngresoUser>();

	// private static int timeOut = ParametersCM.MINUTOS_FUERA;
	private static int timeOut = ((BigDecimal) P.getParamVal(ParametroID.USUARIO_tiempo_fuera)).intValue();
	private int indexPath = 0;

	public ControlTimeOutImpl() {

	}

	public void addRutaBase(String path) {
		int k = path.lastIndexOf("/");
		indexPath = k + 1;
	}

	public void addNodoClient(NodoClient nd) {
		mapClient.put(nd.getUser(), nd);
	}

	public NodoClient getNodoClient(String user) {
		return mapClient.get(user);
	}

	public void createNewClient(String user, String addressIp) {

		NodoClient nd = new NodoClient();
		nd.setUser(user);
		nd.setAddressIp(addressIp);
		nd.setTime(new Date().getTime());
		mapClient.put(user, nd);
	}

	public void setDatos(String user, long d) {
		NodoClient nd = mapClient.get(user);
		nd.setTime(d);
	}

	public void remove(String user) {
		mapClient.remove(user);
	}

	public String getAddressIP(String user) {
		return mapClient.get(user).getAddressIp();
	}

	@SuppressWarnings("rawtypes")
	public void registerOutTime(long timeMax) {

		Map<String, NodoClient> mapAux = new HashMap<String, NodoClient>(mapClient);
		Iterator em = mapAux.keySet().iterator();
		Date d = new Date();
		long tp = d.getTime();
		try {
			//
			while (em.hasNext()) {
				String key = (String) em.next();
				long value = mapClient.get(key).getTime();
				if ((tp - value) > timeMax) {

					MuBitacora logg = new MuBitacora();
					logg.setFormulario("");
					logg.setAccion("Salio del Sistema por expiracion de Tiempo");
					logg.setUsuario(key);
					logg.setFecha(new Timestamp(value + timeMax));
					logg.setDireccionIp(mapClient.get(key).getAddressIp());
					logBL.save(logg);
					mapClient.remove(key);
				}
			}
		} catch (Exception e) {
		}
	}

	@SuppressWarnings("rawtypes")
	public void clearUserCliente() {
		int nroTime = timeOut;
		long timeMax = ((long) nroTime * 60000l);
		Map<String, NodoIngresoUser> mapAux = new HashMap<String, NodoIngresoUser>(mapIngresClient);
		Iterator em = mapAux.keySet().iterator();
		Date d = new Date();
		long tp = d.getTime();
		try {

			while (em.hasNext()) {
				String key = (String) em.next();
				long value = mapIngresClient.get(key).getTime();
				if ((tp - value) > timeMax) {
					mapIngresClient.remove(key);
				}
			}
		} catch (Exception e) {
		}

	}

	public boolean exisUserIngreso(String user) {

		return mapIngresClient.containsKey(user);
	}

	public void insertUserIngreso(String user) {

		NodoIngresoUser nd = new NodoIngresoUser();
		nd.setDate(new Date());
		nd.setCount(1);
		nd.setUser(user);
		mapIngresClient.put(user, nd);
	}

	public void deleteUserIngreso(String user) {
		mapIngresClient.remove(user);
	}

	public int countIntentoUserIngreso(String user) {
		NodoIngresoUser nd = mapIngresClient.get(user);
		return nd.getCount();
	}

	public void incrementarIntentoUsuario(String user) {
		NodoIngresoUser nd = mapIngresClient.get(user);
		nd.setDate(new Date());
		nd.increment();
	}

	public void reiniciarIntentoUsuario(String user) {
		NodoIngresoUser nd = mapIngresClient.get(user);
		if (nd != null) {
			nd.setDate(new Date());
			nd.setCount(0);
		}
	}

	public NodoIngresoUser getNodoIngresoUser(String user) {
		return mapIngresClient.get(user);
	}

	public boolean isPaginaUsuario(String user, String path) {

		NodoClient nd = getNodoClient(user);
		String strPg = path.substring(indexPath);
		return nd.tienePermiso(user, strPg);

	}

}