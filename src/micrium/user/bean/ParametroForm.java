package micrium.user.bean;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import micrium.user.business.ParametroBL;
import micrium.user.business.RolTipoparametroBL;
import micrium.user.business.TipoParametroBL;
import micrium.user.business.UsuarioBL;
import micrium.user.controler.ControlPrivilegio;
import micrium.user.id.FormularioID;
import micrium.user.id.ParametroID;
import micrium.user.id.PrivilegioID;
import micrium.user.ldap.DescriptorBitacora;
import micrium.user.model.MuParametro;
import micrium.user.model.MuTipoParametro;
import micrium.user.model.MuUsuario;
import micrium.user.model.MuUsuarioLocal;
import micrium.user.sys.IControlPrivilegios;
import micrium.user.sys.P;

import org.apache.log4j.Logger;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.context.RequestContext;
import org.primefaces.event.CellEditEvent;

import com.tigo.utils.SysMessage;

@ManagedBean
@ViewScoped
public class ParametroForm implements Serializable, IControlPrivilegios {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ParametroForm.class);
	private static final int EXIT_ERROR = 0;
	private static final int EXIT_OK = 1;

	@Inject
	private ParametroBL parametroBL;
	@Inject
	private TipoParametroBL tipoParametroBL;

	@Inject
	private ControlerBitacora controlerBitacora;

	@Inject
	private UsuarioBL blUsuario;

	@Inject
	private RolTipoparametroBL blRolTipoparametro;

	private List<MuParametro> listParms;

	private MuParametro parametro = new MuParametro();
	private int exit = 1;
	private ControlPrivilegio controlPrivilegio;

	private long tipoParametroId;
	private List<MuTipoParametro> lstTipoParametro;

	private long rolId;

	@PostConstruct
	public void init() {
		try {
			controlPrivilegio = ControlPrivilegio.getInstanceControl();
			parametro = new MuParametro();
			listParms = parametroBL.getParameters(tipoParametroId);
			cargarRolId();
			cargarTipoParametro();
		} catch (Exception e) {
			log.error("[Fallo al inicializar la clase]", e);
		}

	}

	private void cargarRolId() throws Exception {
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
		String usuario = (String) request.getSession().getAttribute("TEMP$USER_NAME");
		MuUsuario muUsuario = blUsuario.getUsuario(usuario);
		if (muUsuario != null) {
			this.rolId = muUsuario.getMuRol().getRolId();
			log.info("[rolId: " + rolId + "] [RolId cargado correctamente para el usuario]");
		} else {
			log.error("[login: " + usuario + "] [No se pudo obtener el usuario desde la base de datos con el login]");
			this.rolId = 0;
		}
	}

	public void cargarTipoParametro() {
		try {
			lstTipoParametro = tipoParametroBL.findAll(this.rolId);
			log.info("[rolId: " + this.rolId + "] [Se encontraron: " + lstTipoParametro.size() + " registros de tipo_paramtro]");
		} catch (Exception e) {
			log.error("[tipoParametroId: " + tipoParametroId + "][Fallo al cargar los tipos de parametros]", e);
		}
	}

	public void cargarParametros() {
		try {
			listParms = parametroBL.getParameters(tipoParametroId);
		} catch (Exception e) {
			log.error("[tipoParametroId: " + tipoParametroId + "][Fallo al cargar los parametros del sistema]");
		}
	}

	public void saveParametro() {
		log.info("[SaveParametro] Guardando Cambios para el parametro:" + parametro.getNombre());
		try {
			if (parametro.getTipo() == 6 || parametro.getTipo() == 7) {
				String mensajesVal = listadoValoresParameterForm.validacionDatos();
				if (!mensajesVal.isEmpty()) {
					SysMessage.warn("VALIDACIÓN", mensajesVal);
					return;
				}
				
				String existeValPorDefecto = "";
				if (this.listadoValoresParameterForm.existenValoresPorDefecto()) {
					existeValPorDefecto = "CUIDADO: Se guardó pero se verificó que hay uno o más items con valores por defecto desde su creación y no se han modifcado.";
				}
				
				String cadenaAGuardar = this.listadoValoresParameterForm.obtenerValoresEnCadena();
				parametro.setValorCadena(cadenaAGuardar);				
				parametroBL.update(parametro);
				restartParameters();
				controlerBitacora.update(DescriptorBitacora.PARAMETRO, parametro.getParametroId() + "", parametro.getNombre());
				listParms = parametroBL.getParameters(tipoParametroId);
				
				RequestContext.getCurrentInstance().execute("PF('dlg').hide()");
				
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "[Ok]", "Operación Satisfactoria... " + existeValPorDefecto));
				DataTable dt1 = (DataTable) FacesContext.getCurrentInstance().getViewRoot().findComponent("frmDetalle:tblparametros");
	            dt1.reset();
			}else{
				String msj = parametroBL.validate(parametro);
				log.info("[RespValidacion]" + msj);
				if (!msj.isEmpty()) {
					exit = EXIT_ERROR;
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Mensaje de error", msj));
				} else {
					exit = EXIT_OK;
					parametroBL.update(parametro);
					P.restartParameter();
					controlerBitacora.update(DescriptorBitacora.PARAMETRO, parametro.getParametroId() + "", parametro.getNombre());
					listParms = parametroBL.getParameters(tipoParametroId);
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "[Ok]", "Operacion Satisfactoria..."));
				}
			}
		} catch (Exception e) {
			log.error("[SaveParametro]Error al Intentar gauradar los Cambios:" + e.getMessage(), e);
			exit = EXIT_ERROR;
		}
	}

	public void editarParametro(int idparm) {
		log.info("********************** Editar parametro[ID=" + idparm + " ] ***************");
		try {
			parametro = parametroBL.getParametro(idparm);
			//>>>>>>>>>>>>>>>>>>>>>
			if (parametro.getTipo() == MuParametro.TIPO_LISTADO_VALORES_TEXTO) {
				this.listadoValoresParameterForm.init();
				this.listadoValoresParameterForm.setValoresNumericos(false);
				this.listadoValoresParameterForm.poblarValores(parametro.getValorCadena());
				
			} else if (parametro.getTipo() == MuParametro.TIPO_LISTADO_VALORES_NUMERICOS) {
				this.listadoValoresParameterForm.init();
				this.listadoValoresParameterForm.setValoresNumericos(true);
				this.listadoValoresParameterForm.poblarValores(parametro.getValorCadena());
			}
			//<<<<<<<<<<<<<<<<<<<<<
			log.info("*** [Datos Iniaciales]" + parametro);
		} catch (Exception e) {
			log.error("[Editar Parametro] Error al Consultar Parametro: " + idparm + ": " + e.getMessage(), e);
		}
	}

	public List<MuParametro> getListParms() {
		return listParms;
	}

	public void setListParms(List<MuParametro> listParms) {
		this.listParms = listParms;
	}

	public MuParametro getParametro() {
		return parametro;
	}

	public void setParametro(MuParametro parametro) {
		this.parametro = parametro;
	}

	public int getExit() {
		return exit;
	}

	public void setExit(int exit) {
		this.exit = exit;
	}

	public List<MuTipoParametro> getLstTipoParametro() {
		return lstTipoParametro;
	}

	public void setLstTipoParametro(List<MuTipoParametro> lstTipoParametro) {
		this.lstTipoParametro = lstTipoParametro;
	}

	public long getTipoParametroId() {
		return tipoParametroId;
	}

	public void setTipoParametroId(long tipoParametroId) {
		this.tipoParametroId = tipoParametroId;
	}

	@Override
	public boolean isAuthorized(int idAccion) {
		log.info("[idAccion: " + idAccion + ", rolId: " + this.rolId + ", tipoParametroId: " + this.tipoParametroId + "] [Se validara la visibilidad del comando editar]");
		if (String.valueOf(idAccion).equalsIgnoreCase(PrivilegioID.ACCION_MODIFICAR)) {
			try {
				return controlPrivilegio.isAuthorized(FormularioID.FORM_GEST_PARAMETROS, idAccion) && blRolTipoparametro.validarRolTipoparametro(this.rolId, this.tipoParametroId);
			} catch (Exception e) {
				log.error("[idAccion: " + idAccion + ", rolId: " + this.rolId + ", tipoParametroId: " + this.tipoParametroId + "] [Fallo al obtener los permisos de edicion desde la base de datos]", e);
				SysMessage.error("No se pudo determinar los permisos asignados");
			}
		}
		return controlPrivilegio.isAuthorized(FormularioID.FORM_GEST_PARAMETROS, idAccion);
	}
	
	
	
	
	
	//>>>>>>>>>>>>>>>>>>>> Lo necesario para el parámetro de tipo Lista >>>>>>>>>>>>>>>>>
	@Inject
	private ListadoValoresParameterForm listadoValoresParameterForm;
	
	private SelectItem parametroSeleccionado;
	
	public void anadirElemento(){
		listadoValoresParameterForm.anadirNuevoItem();
	}

	public ListadoValoresParameterForm getListadoValoresParameterForm() {
		return listadoValoresParameterForm;
	}

	public void setListadoValoresParameterForm(
			ListadoValoresParameterForm listadoValoresParameterForm) {
		this.listadoValoresParameterForm = listadoValoresParameterForm;
	}

	public SelectItem getParametroSeleccionado() {
		return parametroSeleccionado;
	}

	public void setParametroSeleccionado(SelectItem parametroSeleccionado) {
		this.parametroSeleccionado = parametroSeleccionado;
	}
	
	public void onCellEdit(CellEditEvent event) {
		log.info("[rolId: " + rolId + ", rowIndex: " + event.getRowIndex() + ", valorAntiguo: " + event.getOldValue().toString() + ", valorNuevo: " + event.getNewValue().toString() + "] [Se guardara el cambio de permiso]");

		if (this.listadoValoresParameterForm.getValores().get(event.getRowIndex()) != null) {
			try {
				SelectItem item = this.listadoValoresParameterForm.getValores().get(event.getRowIndex());
				
				String val = validarItem(item,this.listadoValoresParameterForm.isValoresNumericos());
				if (!val.isEmpty()) {
					log.info("[rolId: " + rolId + ", rowIndex: " + event.getRowIndex() + ", valorAntiguo: " + event.getOldValue().toString() + ", valorNuevo: " + event.getNewValue().toString() + "] [Cambio guardado correctamente]");
					SysMessage.info("VALIDACIÓN",val);				
				}else{
					log.info("[*******************]");
				}								
			
			} catch (Exception e) {
				log.error("[rolId: " + rolId + ", rowIndex: " + event.getRowIndex() + ", valorAntiguo: " + event.getOldValue().toString() + ", valorNuevo: " + event.getNewValue().toString() + "] [Fallo al guardar el cambio de permiso en la gestion de roles]", e);
				SysMessage.error("VALIDACIÓN","Fallo al validar parametro ingresado");
			}
		} else {
			log.error("[rolId: " + rolId + ", rowIndex: " + event.getRowIndex() + ", valorAntiguo: " + event.getOldValue().toString() + ", valorNuevo: " + event.getNewValue().toString() + "] [No se encontro el registro a editar]");
			SysMessage.warn("VALIDACIÓN","Fallo al validar parametro ingresado");
		}

	}
	
	public void quitarElemento(SelectItem item){
		if(listadoValoresParameterForm.quitarItem(item)==1){
			log.info("[Se ha eliminado un parametro]");
		}else{
			log.warn("[No se elimino ningun parametro]");
		}
		
	}
	
	private String validarItem(SelectItem item, boolean esValorNumerico) {
		String sms = "";
		
		if (esValorNumerico) {
			
			if (item.getValue() == null || String.valueOf(item.getValue()).isEmpty()) {
				sms = "El valor no puede quedar nulo o vacío.";
				
			}else if (!String.valueOf(item.getValue()).matches("^[0-9]+$")) {
				sms = "El valor debe ser numérico.";
			}
			
			
		} else {
			if (item.getValue() == null || String.valueOf(item.getValue()).isEmpty()) {
				sms = "El valor no puede quedar nulo o vacío.";
			}else if (!String.valueOf(item.getValue()).matches("^[a-zA-Z0-9_\\- \\ñ\\ÑÁÉÍÓÚáéíóú()*]+$")) {
				sms = "El valor debe contener sólo letras, números u otro signos:.";
			}
		}
		
		
		return sms;
	}
	
	private void restartParameters(){
		P.restartParameter();
		/*try {
			MuParametro reaload=parametroBL.getParametro(ParametroID.RELOAD_PARAMETER_ID);
			reaload.setValorBooleano(true);
			parametroBL.update(reaload);
		} catch (Exception e) {
			log.error("Error al cargar ó guardar el parametro RELOAD con ID="+ParametroID.RELOAD_PARAMETER_ID,e);
		}*/
	}
	//<<<<<<<<<<<<<< Lo necesario para el parámetro de tipo Lista <<<<<<<<<<<<<<<<<<

}
