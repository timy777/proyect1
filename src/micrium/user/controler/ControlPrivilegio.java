package micrium.user.controler;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import micrium.user.model.MuRolFormulario;

import org.apache.log4j.Logger;

/**
 * Esta Clase se encarga de controlar o gestionar los privilegios que tiene el
 * usuario al momneto de ingresar al sistema, siendo de tipo session para que
 * sea usada en cualquier bean que requiera trabajar con permisos.
 * 
 * @author: Cesar Augusto Choque S.
 * @version: 20-04-2015
 * **/
@ManagedBean(name = "controlPrivilegio")
@SessionScoped
public class ControlPrivilegio implements Serializable {
	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(ControlPrivilegio.class);
	public static String CONTROL_PRIVILEGIO_BEAN = "controlPrivilegio";

	private HashMap<Long, MuRolFormulario> listPrivilegios;

	public ControlPrivilegio() {
		this.listPrivilegios = new HashMap<Long, MuRolFormulario>();
	}

	public HashMap<Long, MuRolFormulario> getListPrivilegios() {
		return listPrivilegios;
	}

	public void setListPrivilegios(HashMap<Long, MuRolFormulario> listPrivilegios) {
		this.listPrivilegios = listPrivilegios;
	}

	/**
	 * Este metodo es usado para cargar los permisos que se usaran en toda la
	 * session del usuario.
	 * 
	 * @param: listRolForm lista de permisos que tiene el usaurio en el sistema
	 * **/
	public void cargarAccionPrivilegios(List<MuRolFormulario> listRolForm) {
		this.listPrivilegios = new HashMap<Long, MuRolFormulario>();
		for (Iterator<MuRolFormulario> iterator = listRolForm.iterator(); iterator.hasNext();) {
			MuRolFormulario muRolFormulario = iterator.next();
			this.listPrivilegios.put(muRolFormulario.getId().getFormularioId(), muRolFormulario);
		}
	}

	/**
	 * Este metodo indica si un usuario tiene un determinado permiso para
	 * realizar una accion en un formulario especifico.
	 * 
	 * @param idForm
	 *            identifica al formulario que se decea evaluar.
	 * @param idAccion
	 *            Identifica la accion que se esta consultando.
	 * @return retorna si el usuario esta autorizado para realizar dicha accion
	 *         valores posibles: [true= esta autorizado, false = no esta
	 *         autorizado].
	 * @see micrium.user.id.FormularioID
	 * @see micrium.user.id.PrivilegioID
	 * **/
	public boolean isAuthorized(long idForm, int idAccion) {
		MuRolFormulario muRolFormulario = this.listPrivilegios.get(idForm);
		if (muRolFormulario != null) {
			if (muRolFormulario.getPrivilegio() != null) {
				String[] p = muRolFormulario.getPrivilegio().split("[,]");
				List<String> privilegios = Arrays.asList(p);
				return privilegios.contains(idAccion + "");
			}

		}
		return false;
	}

	/**
	 * Este metodo debe ser utilizado para obtener la instancia del Bean de la
	 * clase ControlPrivilegio, es recomendable utilizar este metodo en el
	 * metodo init() del bean donde se esta injectando.
	 * **/
	public static ControlPrivilegio getInstanceControl() {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		HttpSession sesion = request.getSession();
		return (ControlPrivilegio) sesion.getAttribute(CONTROL_PRIVILEGIO_BEAN);
	}
}
