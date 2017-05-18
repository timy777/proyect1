package micrium.user.controler;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import micrium.aes.AlgoritmoAES;
import micrium.user.dao.FormularioDAO;
import micrium.user.id.ParametroID;
import micrium.user.ldap.ActiveDirectory;
import micrium.user.ldap.LdapContextException;
import micrium.user.model.MenuLogin;
import micrium.user.model.MuFormulario;
import micrium.user.sys.P;

import org.apache.log4j.Logger;
import org.primefaces.model.menu.MenuModel;
import org.primefaces.util.Base64;

//import com.tigo.utils.ParametersMU;
import com.tigo.utils.SysMessage;

@ManagedBean(name = "login")
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	ControlPrivilegio controlP;

	@Inject
	ControlLoginImpl controler;

	@Inject
	private FormularioDAO daoFormulario;

	private String userId;
	private String password;

	// Usuarios locales
	private String ci;
	private String ehumano;

	private transient MenuModel model;

	private transient List<MenuLogin> lMenu;

	private static final Logger log = Logger.getLogger(Login.class);

	private static final AlgoritmoAES aes = new AlgoritmoAES();

	public Login() {
		this.userId = "";
		this.password = "";
	}

	public String logout() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession sesion = request.getSession();
		sesion.setAttribute("TEMP$ACTION_MESSAGE_ID", "");
		sesion.setAttribute("TEMP$USER_NAME", "");
		sesion.setAttribute("TEMP$GROUP", "");
		sesion.setAttribute("controlPrivilegio", null);
		sesion.invalidate();
		return "/";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String verifyLogin() throws Exception {
		try {
			decryptPasswordBase64();
		} catch (Exception e) {
			log.warn("[Fallo al intentar desencriptar el password]", e);
			SysMessage.error("Fallo al iniciar sesion");
			return "";
		}
		String validacion = controler.validateIngreso(userId, password);
		if (!validacion.isEmpty()) {
			SysMessage.error(validacion);
			return "";

		}

		boolean banderaDesarrollo = false;
		try {
			String thisIp = InetAddress.getLocalHost().getHostAddress();
			log.info("[verifyLogin] Ip del servidor: " + thisIp);
			banderaDesarrollo = thisIp.trim().equalsIgnoreCase(((String) P.getParamVal(ParametroID.IP_DESARROLLO)).trim());
		} catch (UnknownHostException e) {
		}
		if (!banderaDesarrollo) {
			banderaDesarrollo = !(Boolean) P.getParamVal(ParametroID.USUARIO_sw_active_directory);
		}
		try {

			int valid = banderaDesarrollo ? controler.existUser(userId, password) : ActiveDirectory.validarUsuario(userId, password);
			if (valid == ActiveDirectory.EXIT_USER) {

				List groups = banderaDesarrollo ? controler.getListaGrupo() : ActiveDirectory.getListaGrupos(userId);

				if (groups.size() > 0) {

					long rolId = controler.getIdRole(userId, groups);
					log.info("[verifyLogin] User: " + userId + ", Rol:" + rolId);

					return iniciarVariablesSession("usuario: " + userId + "", "0", userId, rolId);

				} else {
					SysMessage.error("Usted no tiene grupo de trabajo");
					return "";
				}
			} else {
				validacion = controler.controlError(userId);
				SysMessage.error(validacion);
				return "";
			}
		} catch (LdapContextException e) {
			SysMessage.error("Error Login", e.getMessage());
			log.error("Error en LDAP:" + e.getMessage());
			return "";
		}
	}

	private String iniciarVariablesSession(String atributos, String eh, String identificador, long rolId) throws Exception {
		if (rolId > 0) {

			HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();

			request.getSession().setAttribute("TEMP$USER_NAME", identificador);
			request.getSession().setAttribute("TEMP$EH", eh);
			this.userId = identificador;

			String remoteAddr = ((HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest()).getRemoteAddr();
			controler.addBitacoraLogin(identificador, remoteAddr, rolId);

			cargarMenu3(rolId);
			// cargarv2(rolId);
			/***************/
			controlP.cargarAccionPrivilegios(daoFormulario.findPrivilegios(rolId));
			HttpSession sesion = request.getSession();
			sesion.setAttribute(ControlPrivilegio.CONTROL_PRIVILEGIO_BEAN, this.controlP);
			/***************/
			return "/view/menu.xhtml";
		} else {
			SysMessage.error("Usted no tiene ningun rol asignado");
			log.error("[" + atributos + "] [Usted no tiene ningun rol asignado]");
			return "";
		}
	}

	private void cargarMenu3(long rolId) {
		if (lMenu == null) {
			lMenu = new ArrayList<MenuLogin>();
		}
		List<MuFormulario> lform = daoFormulario.findPadres(rolId);
		for (MuFormulario formulario : lform) {
			MenuLogin ml = getMenuLogin(rolId, formulario);
			if (ml != null) {
				lMenu.add(ml);
			}
		}
	}

	private MenuLogin getMenuLogin(long rolId, MuFormulario formulario) {
		List<MuFormulario> lHijos = daoFormulario.findHijos(formulario.getId(), rolId);
		if (lHijos != null && !lHijos.isEmpty()) {
			return new MenuLogin(formulario.getNombre(), lHijos);
		} else {
		}
		return null;
	}

	// private void cargarv2(long roleId) {
	// model = new DefaultMenuModel();
	//
	// DefaultMenuItem home = new DefaultMenuItem();
	// home.setValue("Home");
	// home.setIcon("ui-icon ui-icon-home");
	// home.setUrl("menu.xhtml");
	// model.addElement(home);
	//
	// List<MuFormulario> lform = daoFormulario.findPadres(roleId);
	// for (MuFormulario form : lform) {
	// model.addElement((DefaultSubMenu) cargarv2(form, roleId));
	// }
	//
	// DefaultSubMenu submenu = new DefaultSubMenu();
	// submenu.setLabel("Opciones");
	// DefaultMenuItem item = new DefaultMenuItem();
	// item.setValue("Manual de Usuario");
	// String pathDoc = loginFilter.pathRaiz + "resources/Manual.pdf";
	// String strUrlDoc = "window.open('" + pathDoc + "'); return false;";
	// item.setOnclick(strUrlDoc);
	// item.setIcon("ui-icon ui-icon-document");
	// submenu.addElement(item);
	//
	// model.addElement(submenu);
	// }

	// private MenuElement cargarv2(MuFormulario form, long roleId) {
	// List<MuFormulario> lHijos = daoFormulario.findHijos(form.getId(),
	// roleId);
	// if (lHijos != null && !lHijos.isEmpty()) {
	// DefaultSubMenu submenu = new DefaultSubMenu();
	// submenu.setLabel(form.getNombre());
	// for (MuFormulario hijo : lHijos) {
	// submenu.addElement(cargarv2(hijo, roleId));
	// }
	// return submenu;
	// } else {
	// if ((form.getUrl() == null) && (lHijos != null) && (lHijos.isEmpty())) {
	// DefaultSubMenu submenu = new DefaultSubMenu();
	// submenu.setLabel(form.getNombre());
	// return submenu;
	// }
	// DefaultMenuItem item = new DefaultMenuItem();
	// item.setValue(form.getNombre());
	// item.setUrl(form.getUrl());
	// return item;
	// }
	// }

	public MenuModel getModel() {
		return model;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	private void decryptPasswordBase64() throws Exception {
		if (password != null) {
			byte[] newPassword = Base64.decode(password);
			if(newPassword != null){
				this.password = new String(newPassword, "UTF-8");
			}
		}
	}

	/*
	 * Agregado por Wilmer para el manejo en los xhtml
	 */

	public String getExpresionRegularUsuario() {
		return P.getParametro(ParametroID.EXPRESION_REGULAR_USUARIO).getValorCadena();
	}

	public String getMensajeValidacionUsuario() {
		return P.getParametro(ParametroID.MENSAJE_VALIDACION_USUARIO).getValorCadena();
	}

	public String getExpresionRegularPassword() {
		return P.getParametro(ParametroID.EXPRESION_REGULAR_PASSWORD).getValorCadena();
	}

	public String getMensajeValidacionPassword() {
		return P.getParametro(ParametroID.MENSAJE_VALIDACION_PASSWORD).getValorCadena();
	}

	public String getExpresionRegularGeneral() {
		return P.getParametro(ParametroID.EXPRESION_REGULAR_GENERAL).getValorCadena();
	}

	public String getMensajeValidacionGeneral() {
		return P.getParametro(ParametroID.MENSAJE_VALIDACION_GENERAL).getValorCadena();
	}

	public String getTituloAplicacion() {
		return P.getParametro(ParametroID.TITULO_APLICACION).getValorCadena();
	}

	public String getPiePagina() {
		return P.getParametro(ParametroID.PIE_PAGINA).getValorCadena();
	}

	public String getCi() {
		return ci;
	}

	public void setCi(String ci) {
		this.ci = ci;
	}

	public String getEhumano() {
		return ehumano;
	}

	public void setEhumano(String ehumano) {
		this.ehumano = ehumano;
	}

	public List<MenuLogin> getlMenu() {
		return lMenu;
	}

}
