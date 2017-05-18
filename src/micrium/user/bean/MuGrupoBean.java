package micrium.user.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import micrium.user.business.GrupoAdBL;
import micrium.user.business.RoleBL;
import micrium.user.controler.ControlPrivilegio;
import micrium.user.id.FormularioID;
import micrium.user.id.ParametroID;
import micrium.user.ldap.ActiveDirectory;
import micrium.user.ldap.DescriptorBitacora;
import micrium.user.model.MuGrupoAd;
import micrium.user.model.MuRol;
import micrium.user.sys.IControlPrivilegios;
import micrium.user.sys.P;

import org.apache.log4j.Logger;

import com.tigo.utils.SysMessage;

@ManagedBean
@ViewScoped
public class MuGrupoBean implements Serializable, IControlPrivilegios {
	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(MuGrupoBean.class);

	@Inject
	private GrupoAdBL groupBL;

	@Inject
	private RoleBL rolBL;

	@Inject
	private ControlerBitacora controlerBitacora;

	private List<MuGrupoAd> listGroup;

	private MuGrupoAd group = new MuGrupoAd();
	private String groupId;
	private boolean edit;

	private List<SelectItem> selectItems;
	private String select;

	private boolean visibleNuevoEditar;
	private ControlPrivilegio controlPrivilegio;

	@PostConstruct
	public void init() {
		try {
			controlPrivilegio = ControlPrivilegio.getInstanceControl();
			group = new MuGrupoAd();
			listGroup = groupBL.getGroups();
			fillSelectItems();
		} catch (Exception e) {
			log.error("[Error Iniciar] No se pudo cargar datos iniciales.." + e.getMessage(), e);
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

	public String saveGroup() {
		int idRole = Integer.parseInt(select);
		if (idRole == -1) {
			SysMessage.error("Campo 'rol' es requerido");
			return "";
		}

		String str = groupBL.validate(group, groupId);
		if (str.trim().isEmpty())
			str = validarExisteEnAD(group.getNombre().trim());
		if (!str.isEmpty()) {
			SysMessage.error(str);
			return "";
		}

		try {
			if (!edit) {
				groupBL.saveGroupRole(group, idRole);
				controlerBitacora.insert(DescriptorBitacora.GRUPO, group.getGrupoId() + "", group.getNombre());
			} else {
				int id = Integer.parseInt(groupId);
				group.setGrupoId(id);
				groupBL.updateGroup(group, idRole);
				controlerBitacora.update(DescriptorBitacora.GRUPO, group.getGrupoId() + "", group.getNombre());
			}
			listGroup = groupBL.getGroups();
			this.visibleNuevoEditar = false;
			SysMessage.info("Se guardo correctamente.");
			// newGroup();
		} catch (Exception e) {
			log.error("[saveGroup] error al momento de modificar o guardar: " + group.getNombre() + " " + e);
			SysMessage.error("Error al guardar en la Base de Datos.");

		}
		return "";
	}

	public void editRoleGroup() {
		if (group != null) {
			controlerBitacora.accion(DescriptorBitacora.GRUPO, "Se pretende editar el GRUPO: " + group.getNombre() + ".");
			select = group.getMuRol().getRolId() + "";
			edit = true;
			visibleNuevoEditar = true;

		} else {
			log.warn("[editar] No se encontro ningun registro seleccionado.");
			SysMessage.warn("No se encontro ningun registro seleccionado.");
		}
	}

	public void editRoleGroup(MuGrupoAd muGrupoAd) {
		group = muGrupoAd;
		editRoleGroup();
	}

	public void deleteRoleGroup() {
		if (group != null) {
			try {
				groupBL.deleteGroup(group.getGrupoId());
				controlerBitacora.delete(DescriptorBitacora.GRUPO, group.getGrupoId() + "", group.getNombre());
				listGroup = groupBL.getGroups();
				SysMessage.info("Se elimino correctamente.");
			} catch (Exception e) {
				log.error("[deleteRoleGroup]  error al eliminar el menu id:" + group.getGrupoId() + "  " + e);
				SysMessage.error("Fallo al eliminar.");
			}
		} else {
			log.warn("[eliminar] No se encontro ningun registro seleccionado.");
			SysMessage.warn("No se encontro ningun registro seleccionado.");
		}
	}

	public void deleteRoleGroup(MuGrupoAd muGrupoAd) {
		this.group = muGrupoAd;
		deleteRoleGroup();
	}

	public void newGroup() {
		edit = false;
		group = new MuGrupoAd();
		select = "-1";
		visibleNuevoEditar = true;
	}

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

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String GroupId) {
		this.groupId = GroupId;
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public MuGrupoAd getGroup() {
		return group;
	}

	public void setGroup(MuGrupoAd Group) {
		this.group = Group;
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

	public List<MuGrupoAd> getListGroup() {
		return listGroup;
	}

	public void setListGroup(List<MuGrupoAd> listGroup) {
		this.listGroup = listGroup;
	}

	private String validarExisteEnAD(String nameNewGrupo) {
		try {
			if (!ActiveDirectory.validarGrupo(nameNewGrupo)) {
				return "No existe un grupo  '" + nameNewGrupo + "' en Active Directory";
			}
		} catch (Exception e) {
			log.error("[validarExisteEnAD] Error al intentar verificar si grupo existe en AD  nombre=" + nameNewGrupo);
			return "No se puede comprobar si este grupo existe en Active Directory";
		}
		return "";
	}

	public boolean isVisibleNuevoEditar() {
		return visibleNuevoEditar;
	}

	public void setVisibleNuevoEditar(boolean visibleNuevoEditar) {
		this.visibleNuevoEditar = visibleNuevoEditar;
	}

	@Override
	public boolean isAuthorized(int idAccion) {
		return controlPrivilegio.isAuthorized(FormularioID.FORM_GEST_GRUPO, idAccion);
	}

}
