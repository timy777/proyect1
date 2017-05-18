package micrium.user.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import micrium.user.business.RoleBL;
import micrium.user.business.UsuarioBL;
import micrium.user.controler.ControlPrivilegio;
import micrium.user.id.FormularioID;
import micrium.user.id.ParametroID;
import micrium.user.ldap.ActiveDirectory;
import micrium.user.ldap.DescriptorBitacora;
import micrium.user.ldap.LdapContextException;
import micrium.user.model.MuRol;
import micrium.user.model.MuUsuario;
import micrium.user.sys.IControlPrivilegios;
import micrium.user.sys.P;

import org.apache.log4j.Logger;

import com.tigo.utils.SysMessage;

@ManagedBean
@ViewScoped
public class RoleUserForm implements Serializable, IControlPrivilegios {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(RoleUserForm.class);

	@Inject
	private UsuarioBL userBL;

	@Inject
	private RoleBL rolBL;

	@Inject
	private ControlerBitacora controlerBitacora;

	private List<MuUsuario> listUser;

	private MuUsuario user = new MuUsuario();
	private List<SelectItem> selectItems;
	private String select;

	private String userId;
	private boolean edit;

	private boolean visibleNuevoEditar;
	private ControlPrivilegio controlPrivilegio;

	@PostConstruct
	public void init() {
		try {
			controlPrivilegio = ControlPrivilegio.getInstanceControl();
			user = new MuUsuario();
			listUser = userBL.getUsers();
			visibleNuevoEditar = false;
			fillSelectItems();
		} catch (Exception e) {
			log.error("init|Fallo al inicializar la clase. " + e.getMessage());
		}
	}

	private void fillSelectItems() throws Exception {

		selectItems = new ArrayList<SelectItem>();
		selectItems.add(new SelectItem("-1", "Grupos_Rol"));
		List<MuRol> listaRol = rolBL.getRoles();
		for (MuRol role : listaRol) {
			SelectItem sel = new SelectItem(role.getRolId(), role.getNombre());
			selectItems.add(sel);
		}
	}

	public void saveUser() {
		int idRole = Integer.parseInt(select);
		if (idRole == -1) {
			SysMessage.error("Campo rol es requerido");
			return;
		}
		if (user.getLogin().trim().isEmpty()) {
			SysMessage.error("Campo usuario es requerido");
			return;
		}

		String name;
		try {
			name = ActiveDirectory.getNombreCompleto(user.getLogin().trim());
			if (name.trim().isEmpty()) {
				SysMessage.error("No se pudo obtener el nombre del usuario");
				return;
			}

			if (!name.isEmpty())
				user.setNombre(name);
		} catch (LdapContextException e1) {
			SysMessage.error("Error LDAP [" + P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_user) + "]", e1.getMessage());
			log.error("Error Validar Usurio LDAP:" + e1.getMessage());
			return;
		}

		String str = "";
		try {
			str = userBL.validate(user, userId);
		} catch (Exception e) {
			log.error("[saveUser] Fallo al intentar validar los parametros.", e);
			str = "error con la conexion a la BD o otro problema";
		}

		if (!str.isEmpty()) {
			SysMessage.error(str);
			return;
		}

		try {
			if (!edit) {
				userBL.saveUserRole(user, idRole);
				controlerBitacora.insert(DescriptorBitacora.USUARIO, user.getUsuarioId() + "", user.getLogin());
			} else {
				int id = Integer.parseInt(userId);

				user.setUsuarioId(id);
				userBL.updateUser(user, idRole);
				controlerBitacora.update(DescriptorBitacora.USUARIO, user.getUsuarioId() + "", user.getLogin());
			}
			listUser = userBL.getUsers();
			SysMessage.info("Se guardo correctamente.");
			newUser();
			visibleNuevoEditar = false;
		} catch (Exception e) {
			log.error("[saveUser] usuario: " + user.getNombre() + ", Fallo al guardar el usuario", e);
			SysMessage.error("Fallo al guardar el usuario");
		}
	}

	public void editRoleUser() {
		if (user != null) {
			if (user.getUsuarioId() == 1) {
				SysMessage.warn("El Usuario " + user.getLogin() + " es el Aministrador del Sistema por defecto, No es posible su edicion..!");
			} else {
				controlerBitacora.accion(DescriptorBitacora.USUARIO, "Se pretende editar el USUARIO: " + user.getNombre() + ".");
				userId = user.getUsuarioId() + "";
				select = user.getMuRol().getRolId() + "";
				edit = true;
				visibleNuevoEditar = true;
			}

		} else {
			log.warn("[editar] No se encontro ningun registro seleccionado.");
			SysMessage.warn("No se encontro ningun registro seleccionado.");
		}

	}

	public void editRoleUser(MuUsuario muUsuario) {
		user = muUsuario;
		editRoleUser();
	}

	public String deleteRoleUser() {
		if (user != null) {
			if (user.getUsuarioId() == 1) {
				SysMessage.error("Este Usuario no se puede elimnar es usuario Interno.");
				return "";
			}

			try {
				userBL.deleteUser(user.getUsuarioId());
				controlerBitacora.delete(DescriptorBitacora.USUARIO, user.getUsuarioId() + "", user.getLogin());
				listUser = userBL.getUsers();
				SysMessage.info("Se elimino correctamente.");
			} catch (Exception e) {
				log.error("[deleteRoleUser]  error al eliminar el usuario id:" + user.getUsuarioId() + "  " + e);
				SysMessage.error("Fallo al eliminar.");
			}
		} else {
			log.warn("[eliminar] No se encontro ningun registro seleccionado.");
			SysMessage.warn("No se encontro ningun registro seleccionado.");
		}
		return "";
	}

	public String deleteRoleUser(MuUsuario muUsuario) {
		user = muUsuario;
		return deleteRoleUser();
	}

	public void newUser() {
		edit = false;
		user = new MuUsuario();
		select = "-1";
		visibleNuevoEditar = true;
	}

	/**/
	public String getColor(boolean estado) {
		String color;
		try {
			if (estado)
				color = "background-color:#FFFFFF";
			else
				color = "background-color:#" + P.getParamVal(ParametroID.USUARIO_ROL_DELETE_COLOR);
		} catch (Exception e) {
			log.error("[Get Color] Error al evalor Color UsuarioRol:" + e.getMessage(), e);
			color = "background-color:#FFFFFF";
		}
		return color;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public MuUsuario getUser() {
		return user;
	}

	public void setUser(MuUsuario user) {
		this.user = user;
	}

	public List<SelectItem> getSelectItems() {
		return selectItems;
	}

	public void setSelectItems(List<SelectItem> selectItems) {
		this.selectItems = selectItems;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public List<MuUsuario> getListUser() {
		return listUser;
	}

	public void setListUser(List<MuUsuario> listUser) {
		this.listUser = listUser;
	}

	public boolean isVisibleNuevoEditar() {
		return visibleNuevoEditar;
	}

	public void setVisibleNuevoEditar(boolean visibleNuevoEditar) {
		this.visibleNuevoEditar = visibleNuevoEditar;
	}

	@Override
	public boolean isAuthorized(int idAccion) {
		return controlPrivilegio.isAuthorized(FormularioID.FORM_GEST_USUARIO, idAccion);
	}

}
