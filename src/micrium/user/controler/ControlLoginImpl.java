package micrium.user.controler;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.business.BitacoraBL;
import micrium.user.business.ParametroBL;
import micrium.user.business.RoleBL;
import micrium.user.business.UsuarioBL;
import micrium.user.id.ParametroID;
import micrium.user.ldap.ActiveDirectory;
import micrium.user.model.MuBitacora;
import micrium.user.model.MuParametro;
import micrium.user.model.MuPassword;
import micrium.user.model.MuRolFormulario;
import micrium.user.model.MuUsuarioLocal;
import micrium.user.sys.P;

import org.apache.log4j.Logger;

@Named
public class ControlLoginImpl implements Serializable {

	private static final long serialVersionUID = 1L;

	public static int MENU_MENU = 1;
	public static int MENU_RAIZ = 2;
	public static int MENU_ITEM = 3;
	// private static int nroOption = ParametersCM.NRO_INTENTOS;
	private static final int nroOption = ((BigDecimal) P.getParamVal(ParametroID.USUARIO_nro_opciones)).intValue();
	// private static int timeOut = ParametersCM.MINUTOS_FUERA;
	private static int timeOut = ((BigDecimal) P.getParamVal(ParametroID.USUARIO_tiempo_fuera)).intValue();

	@Inject
	private BitacoraBL logBL;

	@Inject
	private UsuarioBL userBL;

	@Inject
	private RoleBL roleBL;

	@Inject
	private ControlTimeOutImpl controlTA;

	@Inject
	private ParametroBL blParametro;

	private static Logger log = Logger.getLogger(ControlLoginImpl.class);

	public String validateIngreso(String login, String passWord) {

		if (login == null)
			return "Usuario invalido!";

		if (login.trim().isEmpty())
			return "Usuario invalido!";

		if (passWord == null)
			return "Contrasena invalido!";

		if (passWord.trim().isEmpty())
			return "Contrasena invalido!";

		try {
			if (controlTA != null) {
				if (controlTA.exisUserIngreso(login)) {
					log.info("existe:" + login);
					int cont = controlTA.countIntentoUserIngreso(login);
					log.info(nroOption + " contador:" + cont);
					if (nroOption == cont) {
						Date dateAct = new Date();
						Calendar cal = Calendar.getInstance();
						NodoIngresoUser user = controlTA.getNodoIngresoUser(login);
						cal.setTime(user.getDate());
						cal.add(Calendar.MINUTE, timeOut);
						Date dateUser = cal.getTime();
						if (dateAct.before(dateUser)) {
							String str = "EL USUARIO " + '"' + login + '"' + " HA SIDO BLOQUEADO.VUELVA A INTENTAR EN " + timeOut + " MINUTOS.";
							return str;
						} else {
							user.setCount(0);
							user.setDate(new Date());
						}
					}
				}
			} else {
				throw new Exception("controlTA is null");
			}
			return "";
		} catch (Exception e) {
			return "Error de obtener Login. " + e.getMessage();
		}

	}

	public String validateIngresoUsuarioLocal(String eh, String ci, String password) {

		if (eh == null || eh.trim().isEmpty())
			return "Campo 'EHumano' es requerido";

		if (ci == null || ci.trim().isEmpty())
			return "Campo 'C.I.' es requerido";

		if (password == null || password.trim().isEmpty())
			return "Campo 'Password' es requerido";

		// try {
		// if (controlTA != null) {
		// if (controlTA.exisUserIngreso(ci)) {
		// log.info("existe:" + ci);
		// int cont = controlTA.countIntentoUserIngreso(ci);
		// log.info(nroOption + " contador:" + cont);
		// if (nroOption == cont) {
		// Date dateAct = new Date();
		// Calendar cal = Calendar.getInstance();
		// NodoIngresoUser user = controlTA.getNodoIngresoUser(ci);
		// cal.setTime(user.getDate());
		// cal.add(Calendar.MINUTE, timeOut);
		// Date dateUser = cal.getTime();
		// if (dateAct.before(dateUser)) {
		// String str = "El usuario con EHumano: " + eh + ", C.I.: " + ci +
		// ", ha sido bloqueado. Vuelva a intentar en " + " minutos";
		// return str;
		// } else {
		// user.setCount(0);
		// user.setDate(new Date());
		// }
		// }
		// }
		// } else {
		// throw new Exception("controlTA is null");
		// }
		// return "";
		// } catch (Exception e) {
		// return "Error de obtener Login. " + e.getMessage();
		// }

		return "";

	}

	public boolean passwordVigente(MuPassword muPassword) throws Exception {
		MuParametro muParametro = blParametro.getParametro(27);
		int DIAS_VIGENCIA = muParametro.getValorNumerico().intValue();

		Date date = muPassword.getFechaRegistro();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DAY_OF_MONTH, DIAS_VIGENCIA);

		return Calendar.getInstance().getTime().compareTo(cal.getTime()) < 0;
	}

	public String controlError(String login) {

		String str = "";
		if (controlTA.exisUserIngreso(login)) {
			controlTA.incrementarIntentoUsuario(login);
			NodoIngresoUser user = controlTA.getNodoIngresoUser(login);
			int cont = user.getCount();
			// nroOption = nroOption - cont;

			if (nroOption - cont > 0)
				str = "EL USUARIO O CONTRASEÑA INGRESADOS SON INCORRECTOS";
			else {
				str = "EL USUARIO " + '"' + login + '"' + " HA SIDO BLOQUEADO.VUELVA A INTENTAR EN " + timeOut + " MINUTOS.";
			}
		} else {
			controlTA.insertUserIngreso(login);
			str = "EL USUARIO O CONTRASEÑA INGRESADOS SON INCORRECTOS";
		}
		return str;

	}

	public String controlErrorLocal(MuUsuarioLocal muUsuarioLocal) {

		String str = "";
		if (controlTA.exisUserIngreso(String.valueOf(muUsuarioLocal.getCi()))) {
			controlTA.incrementarIntentoUsuario(String.valueOf(muUsuarioLocal.getCi()));
			NodoIngresoUser user = controlTA.getNodoIngresoUser(String.valueOf(muUsuarioLocal.getCi()));
			int cont = user.getCount();

			if (nroOption - cont > 0)
				str = "Los datos ingresados para inicio de sesion son incorrectos";
			else {
				// user.setCount(0);
				// user.setDate(new Date());
				controlTA.reiniciarIntentoUsuario(String.valueOf(muUsuarioLocal.getCi()));
				str = "El usuario con ehumano: " + muUsuarioLocal.getEhumano() + " y ci: " + muUsuarioLocal.getCi() + " ha sido bloqueado";
				muUsuarioLocal.setBloqueado(true);
			}
		} else {
			controlTA.insertUserIngreso(String.valueOf(muUsuarioLocal.getCi()));
			str = "Los datos ingresados para inicio de sesion son incorrectos";
		}
		return str;

	}

	public void reiniciarIntentoUsuario(MuUsuarioLocal muUsuarioLocal) {
		controlTA.reiniciarIntentoUsuario(String.valueOf(muUsuarioLocal.getCi()));
	}

	public long getIdRole(String userID, List<Object> userGroups) {
		return userBL.getIdRole(userID, userGroups);
	}

	public int existUser(String userId, String password) {

		// String strLogin = ParametersCM.LOGIN;
		String strLogin = (String) P.getParamVal(ParametroID.USUARIO_login);

		boolean swLogin = userId.indexOf(strLogin) != -1 ? true : false;

		// if (swLogin && password.equals(ParametersCM.PASSWORD_USER))
		if (swLogin && password.equals((String) P.getParamVal(ParametroID.USUARIO_password)))
			return ActiveDirectory.EXIT_USER;
		else
			return ActiveDirectory.NOT_EXIT_USER;
	}

	public int existeUsuarioLocal(String eh, String ci, String password) {
		return ActiveDirectory.EXIT_USER;
	}

	public void addBitacoraLogin(String strIdUs, String address, long idRol) throws Exception {

		MuBitacora bitacora = new MuBitacora();
		bitacora.setFormulario("INICIO");
		bitacora.setAccion("Ingreso al Sistema");
		bitacora.setUsuario(strIdUs);
		bitacora.setDireccionIp(address);
		bitacora.setFecha(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		logBL.save(bitacora);

		List<MuRolFormulario> lista = roleBL.getRolFormulario(idRol);

		NodoClient nd = new NodoClient();
		nd.setUser(strIdUs);
		nd.setAddressIp(address);
		nd.setTime(new Date().getTime());

		for (MuRolFormulario rolFormulario : lista) {
			nd.addUrl(rolFormulario.getMuFormulario().getUrl(), rolFormulario.getEstado());
		}
		nd.addUrl("menu.xhtml", true);
		controlTA.addNodoClient(nd);

	}

	public void salirBitacora(String strIdUs, String address) throws Exception {

		MuBitacora logg = new MuBitacora();
		logg.setFormulario("");
		logg.setAccion("Salio del Sistema");
		logg.setUsuario(strIdUs);
		String addr = controlTA.getAddressIP(strIdUs);
		logg.setDireccionIp(address);
		logBL.save(logg);
		if (addr.equals(address))
			controlTA.remove(strIdUs);

	}

	public List<String> getListaGrupo() {
		List<String> groups = new ArrayList<String>();
		groups.add("Administradores");
		return groups;
	}

}
