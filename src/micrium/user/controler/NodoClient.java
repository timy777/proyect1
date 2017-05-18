package micrium.user.controler;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

/**
 * 
 * @author marciano
 */
public class NodoClient {
	private String user;
	private String addressIp;
	private long time;
	private Map<String, Boolean> mapListaUrl = new HashMap<String, Boolean>();

	private static Logger log = Logger.getLogger(NodoClient.class);

	public void addUrl(String url, boolean sw) {
		mapListaUrl.put(url, sw);
	}

	public boolean tienePermiso(String usuario, String url) {
		log.info("[usuario: " + usuario + ", url: " + url + "] [Se validara si el usuario tiene permiso para ingresar a la url]");
		return mapListaUrl.containsKey(url) ? mapListaUrl.get(url) : false;
	}

	/**
	 * @return the user
	 */
	public String getUser() {
		return user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public void setUser(String user) {
		this.user = user;
	}

	/**
	 * @return the addressIp
	 */
	public String getAddressIp() {
		return addressIp;
	}

	/**
	 * @param addressIp
	 *            the addressIp to set
	 */
	public void setAddressIp(String addressIp) {
		this.addressIp = addressIp;
	}

	/**
	 * @return the time
	 */
	public long getTime() {
		return time;
	}

	/**
	 * @param time
	 *            the time to set
	 */
	public void setTime(long time) {
		this.time = time;
	}

}
