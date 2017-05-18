package micrium.user.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import micrium.user.business.FormularioBL;
import micrium.user.controler.ControlPrivilegio;
import micrium.user.id.FormularioID;
import micrium.user.ldap.DescriptorBitacora;
import micrium.user.model.MuAccion;
import micrium.user.model.MuFormulario;
import micrium.user.sys.IControlPrivilegios;

import org.apache.log4j.Logger;

import com.tigo.utils.SysMessage;

@ManagedBean
@ViewScoped
public class MuFormularioBean implements Serializable, IControlPrivilegios {
	private static final long serialVersionUID = 1L;
	public static Logger log = Logger.getLogger(MuFormularioBean.class);

	@Inject
	private FormularioBL formularioBL;
	

	@Inject
	private ControlerBitacora controlerBitacora;

	private boolean visibleNuevoEditar;
	private ControlPrivilegio controlPrivilegio;

	private MuFormulario formulario;
	private List<MuFormulario> listMod;
	private List<MuFormulario> listPages;
	private List<MuFormulario> listPagesMod;
	private List<MuAccion> listAcciones;
	private List<String> listAccionsSelect;
	private List<String> listPagesOrder;

	private long idFormMod;

	private boolean setUpOrder;
	private boolean edit;
	private boolean tipoVista;

	@PostConstruct
	public void init() {
		try {
			controlPrivilegio = ControlPrivilegio.getInstanceControl();
			this.formulario = new MuFormulario();
			this.listMod = formularioBL.getModulos();
			this.listPages = formularioBL.getPages();
			this.listAcciones = formularioBL.getAccionesActivas();
			this.listPagesMod = new ArrayList<MuFormulario>();
			this.listPagesOrder = new ArrayList<String>();
		} catch (Exception e) {
			log.error("[Error Iniciar] No se pudo cargar datos iniciales.." + e.getMessage(), e);
		}

	}

	public String obtenerNombMod(long idRefForm) {
		if (this.listMod != null) {
			for (Iterator<MuFormulario> iterator = listMod.iterator(); iterator.hasNext();) {
				MuFormulario muFormulario = iterator.next();
				if (muFormulario.getId() == idRefForm)
					return muFormulario.getNombre();
			}
		}
		return "Not Found";
	}

	public void editFormulario() {
		if (formulario != null) {
			if (formulario.getFormularioMenu() != null) {
				log.debug("*** Inicializando  Formulario para modificacion de registro Interfaz_Web");
				controlerBitacora.accion(DescriptorBitacora.FORMULARIO, "Se pretende editar la Vista: " + formulario.getNombre() + ".");
				idFormMod = formulario.getFormularioMenu().getId();
				tipoVista = (formulario.getFormularioMenu() == null);
				edit = true;
				visibleNuevoEditar = true;
				this.listAccionsSelect = new ArrayList<String>();
				if (formulario.getMuAccions() != null) {
					log.debug("* Acciones Formulario:" + formulario.getMuAccions().size());
					for (MuAccion muAccion : formulario.getMuAccions()) {
						this.listAccionsSelect.add(muAccion.getId() + "");
						log.debug("[Select]" + muAccion);
					}
				}
			} else {
				tipoVista = (formulario.getFormularioMenu() == null);
				edit = true;
				visibleNuevoEditar = true;
			}
		} else {
			log.warn("[editar] No se encontro ningun registro seleccionado.");
			SysMessage.warn("No se encontro ningun registro seleccionado.");
		}
	}

	public void editFormulario(MuFormulario formulario) {
		this.formulario = formulario;
		editFormulario();
	}

	public void saveOrderPages() {
		long orden = 1;
		try {
			for (String pagina : listPagesOrder) {
				for (MuFormulario muFormulario : listPagesMod) {
					if (muFormulario.getNombre().equals(pagina)) {
						muFormulario.setOrden(orden);
						formularioBL.updateFormulario(muFormulario);
						break;
					}
				}
				orden++;
			}
			this.setUpOrder = false;
			SysMessage.info("Reordenamiento de Paginas Exitosamente..");
		} catch (Exception e) {
			log.error("[Reordenamiento]No se pudo actulizar  las Vistas: " + e.getMessage(), e);
			SysMessage.error("No se pudo Reordenar los Items..");
		}
	}

	public void saveFormulario() {
		log.debug("** Save Interfaz web [ " + (this.tipoVista == MuFormulario.V_MODULO ? "MODULO" : "PAGINA") + "]");
		String msg = formularioBL.validate(formulario, this.tipoVista);
		if (msg.isEmpty()) {
			if (this.tipoVista == MuFormulario.V_MODULO) {
				try {
					saveModulo();
					this.visibleNuevoEditar = false;
					this.listMod = formularioBL.getModulos();
					this.listPages = formularioBL.getPages();
					SysMessage.info("Registro", "Operacion Satisfactoria..");
				} catch (Exception e) {
					log.error("[Error Modulo]No se pudo  Guardar el Formulario" + e.getMessage(), e);
					SysMessage.error("ERROR SAVE Modulo", "No se pudo guardar el registro..");
				}
			} else {
				try {
					savePagina();
					this.visibleNuevoEditar = false;
					this.listPages = formularioBL.getPages();
					SysMessage.info("Registro", "Operacion Satisfactoria..");
				} catch (Exception e) {
					log.error("[Error Pagina]No se pudo  Guardar el Formulario" + e.getMessage(), e);
					SysMessage.error("ERROR SAVE Vista", "No se pudo guardar el registro..");
				}
			}
		} else
			SysMessage.error("Validacion", msg);
	}

	private void saveModulo() throws Exception {
		this.formulario.setFormularioMenu(null);
		this.formulario.setUrl(null);
		this.formulario.setMuAccions(null);
		if (!this.edit) {
			long newId = formularioBL.obtenerNextIdFormulario();
			this.formulario.setId(newId);
			this.formulario.setEstado(true);
			this.formulario.setOrden(formularioBL.obtenerNextOrdenMod());
			formularioBL.saveFormulario(this.formulario);
			controlerBitacora.insert(DescriptorBitacora.FORMULARIO, formulario.getId() + "", formulario.getNombre());
		} else {
			this.formulario.setOrden(formularioBL.updateOrdenMod(formulario.getId()));
			formularioBL.updateFormulario(this.formulario);
			controlerBitacora.update(DescriptorBitacora.FORMULARIO, formulario.getId() + "", formulario.getNombre());
		}
		log.debug(formulario);
	}

	private void savePagina() throws Exception {
		// this.formulario.setFormularioId(idFormMod);

		MuFormulario mf = formularioBL.get(idFormMod);
		this.formulario.setFormularioMenu(mf);

		this.formulario.setOrden(formularioBL.obtenerNextOrdenForm(idFormMod));
		List<MuAccion> listAc = new ArrayList<MuAccion>();
		log.debug("* Estableciendo Acciones para:" + formulario.getNombre());
		for (MuAccion muAccion : this.listAcciones) {
			if (listAccionsSelect.contains(muAccion.getId() + "")) {
				log.debug("Accion " + muAccion.getNombre() + " [Select]");
				listAc.add(muAccion);
			}
		}
		this.formulario.setMuAccions(listAc);
		if (!this.edit) {
			long newId = formularioBL.obtenerNextIdFormulario();
			this.formulario.setId(newId);
			this.formulario.setEstado(true);
			formularioBL.saveFormulario(this.formulario);
			controlerBitacora.insert(DescriptorBitacora.FORMULARIO, formulario.getId() + "", formulario.getNombre());
		} else {
			formularioBL.updateFormulario(this.formulario);
			controlerBitacora.update(DescriptorBitacora.FORMULARIO, formulario.getId() + "", formulario.getNombre());
		}

	}

	public void deleteInterfaz() {
		if (formulario != null) {
			if (formulario.getFormularioMenu() != null) {
				try {
					formulario.setMuAccions(null);
					formularioBL.updateFormulario(formulario);
					formularioBL.deleteFormulario(formulario.getId());
					controlerBitacora.delete(DescriptorBitacora.FORMULARIO, formulario.getId() + "", formulario.getNombre());
					listPages = formularioBL.getPages();
					SysMessage.info("Se elimino correctamente");
				} catch (Exception e) {
					log.error("Error al eliminar el formulario[" + formulario.getId() + "]", e);
					SysMessage.error("Fallo al eliminar");
				}
			} else {
				try {
					// formulario.setEstado(false);
					// formularioBL.updateFormulario(formulario);
					formularioBL.deleteFormularioLogicoCascade(formulario.getId());
					listPages = formularioBL.getPages();
					SysMessage.info("Se elimino correctamente");
				} catch (Exception e) {
					log.error("Error al eliminar el Formulario[" + formulario.getId() + "]", e);
					SysMessage.error("Fallo al eliminar");
				}
			}
		} else {
			log.warn("[eliminar] No se encontro ningun registro seleccionado.");
			SysMessage.warn("No se encontro ningun registro seleccionado.");
		}
	}

	public void deleteInterfaz(MuFormulario muFormulario) {
		this.formulario = muFormulario;
		deleteInterfaz();
	}

	public void newFormulario() {
		log.debug("*** Inicializando  Formulario para registro de nueva Interfaz_Web");
		this.formulario = new MuFormulario();
		this.listAccionsSelect = new ArrayList<String>();
		this.visibleNuevoEditar = true;
		this.tipoVista = MuFormulario.V_MODULO;
		this.edit = false;
	}

	public void setpUpModulosOrder() {
		this.idFormMod = 1;
		this.setUpOrder = true;
		cargarListaPagesForMod();
	}

	public void cargarListaPagesForMod() {
		this.listPagesOrder = new ArrayList<String>();
		try {
			this.listPagesMod = formularioBL.getPages(this.idFormMod);
			for (MuFormulario muFormulario : listPagesMod) {
				this.listPagesOrder.add(muFormulario.getNombre());
			}
		} catch (Exception e) {
			log.error("[Error]No se pudo Cargar los Formularios:" + e.getMessage());
		}
	}

	public Boolean getEdit() {
		return edit;
	}

	public void setEdit(Boolean edit) {
		this.edit = edit;
	}

	public boolean isVisibleNuevoEditar() {
		return visibleNuevoEditar;
	}

	public void setVisibleNuevoEditar(boolean visibleNuevoEditar) {
		this.visibleNuevoEditar = visibleNuevoEditar;
	}

	public MuFormulario getFormulario() {
		return formulario;
	}

	public void setFormulario(MuFormulario formulario) {
		this.formulario = formulario;
	}

	public List<MuFormulario> getListMod() {
		return listMod;
	}

	public void setListMod(List<MuFormulario> listMod) {
		this.listMod = listMod;
	}

	public List<MuFormulario> getListPages() {
		return listPages;
	}

	public void setListPages(List<MuFormulario> listPages) {
		this.listPages = listPages;
	}

	public long getIdFormMod() {
		return idFormMod;
	}

	public void setIdFormMod(long idFormMod) {
		this.idFormMod = idFormMod;
	}

	public List<MuAccion> getListAcciones() {
		return listAcciones;
	}

	public void setListAcciones(List<MuAccion> listAcciones) {
		this.listAcciones = listAcciones;
	}

	public List<String> getListAccionsSelect() {
		return listAccionsSelect;
	}

	public void setListAccionsSelect(List<String> listAccionsSelect) {
		this.listAccionsSelect = listAccionsSelect;
	}

	public void Vacio() {
		log.info("*****************");
	}

	public String getNombreMod() {
		if (this.idFormMod > 0) {
			for (MuFormulario muFormulario : listMod) {
				if (muFormulario.getId() == this.idFormMod)
					return muFormulario.getNombre();
			}
		}
		return "";
	}

	public boolean isTipoVista() {
		return tipoVista;
	}

	public void setTipoVista(boolean tipoVista) {
		this.tipoVista = tipoVista;
	}

	public List<MuFormulario> getListPagesMod() {
		return listPagesMod;
	}

	public void setListPagesMod(List<MuFormulario> listPagesMod) {
		this.listPagesMod = listPagesMod;
	}

	public boolean isSetUpOrder() {
		return setUpOrder;
	}

	public void setSetUpOrder(boolean setUpOrder) {
		this.setUpOrder = setUpOrder;
	}

	public List<String> getListPagesOrder() {
		return listPagesOrder;
	}

	public void setListPagesOrder(List<String> listPagesOrder) {
		this.listPagesOrder = listPagesOrder;
	}

	@Override
	public boolean isAuthorized(int idAccion) {
		return controlPrivilegio.isAuthorized(FormularioID.FORM_GEST_VISTAS, idAccion);
	}

}
