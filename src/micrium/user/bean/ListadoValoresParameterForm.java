package micrium.user.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.model.SelectItem;

import org.apache.log4j.Logger;

@ManagedBean
@ViewScoped
public class ListadoValoresParameterForm implements Serializable {
	private static final long serialVersionUID = 1L;
	private static Logger log = Logger.getLogger(ListadoValoresParameterForm.class);

	private List<SelectItem> valores;
	
	private boolean valoresNumericos;
	
	
	@PostConstruct
	public void init() {
		try {
			
		inicializar();
		} catch (Exception e) {
			log.error("[Fallo al inicializar la clase]", e);
		}

	}

	private void inicializar() {
		valores = new ArrayList<SelectItem>();
		setValoresNumericos(false);//Por defecto almacena id de tipo cadena
	}
	
	public void anadirNuevoItem(){
				
		SelectItem nuevoItem;
		//Recurda valores numéricos pero como cadena
		int cant = valores.size() +1;
		String desc = "DESCRIPCION " + cant;
		if (valoresNumericos) {
			nuevoItem = new SelectItem("0",desc);	
		} else {
			nuevoItem = new SelectItem("VALOR " + cant,desc);
		}
		log.info("Anadiendo un nuevo item, label: " + nuevoItem.getLabel() + ", valor" + nuevoItem.getValue());
		
		this.valores.add(0, nuevoItem);//Siempre lo añade al primer lugar de la lista.
		
	}
	/**
	 * 
	 * @param item
	 * @return 1 si se ha eliminado algun parametro y -1 sy no se ha eliminado ninguno
	 */
	public int quitarItem(SelectItem item){
		for (int i=0;i<valores.size();i++) {
			SelectItem param=valores.get(i);
			if(param.getLabel().equals(item.getLabel())&&param.getValue().equals(item.getValue())){
				valores.remove(i);
				return 1;
			}
		}
		return -1;
	}

	public boolean existenValoresPorDefecto() {
		boolean existe = false;
		if (valores.size()>0) {
			String desc = "DESCRIPCION ";
			if (valoresNumericos) {
				for (SelectItem item : valores) {
					if (item.getLabel().contains(desc) ) {
						existe = true;
						break;
					}
				}					
			} else {
				for (SelectItem item : valores) {
					if (item.getLabel().contains(desc) || ((String)item.getValue()).contains("VALOR ") ) {
						existe = true;
						break;
					}
				}
			}	
		}
		
		return existe;
		
	}
	
	public boolean existenValoresInvalidos(){
		boolean band = false;
		for (SelectItem item : this.valores) {
			if (item.getValue() == null || String.valueOf(item.getValue()).trim().equals("")) {
				band = true;
				break;
			}						
			if (this.valoresNumericos) {
				 if (!String.valueOf(item.getValue()).matches("^[0-9]+$")) {
					band = true;
					break;
				}
			} else {
				if (!String.valueOf(item.getValue()).matches("^[a-zA-Z0-9_\\- \\ñ\\ÑÁÉÍÓÚáéíóú()*]+$")) {
					band = true;
					break;

				}
			}			
		}
		return band;
	}
	
	public String validacionDatos(){
		String mensaje = "";
		if (existenValoresInvalidos()) {
			mensaje = "No se pueden dejar valores vacíos para el lista. Verifique que todos tengan valores asignados.";
		}
		
		if (existenLabelsInvalidos()) {
			mensaje = "No se pueden dejar etiquetas vacías para el lista. Verifique que todos tengan etiquetas asignados.";
		}		
		
		return mensaje;
	}
	
	public boolean existenLabelsInvalidos(){
		boolean band = false;
		for (SelectItem item : this.valores) {
			if (item.getLabel() == null || String.valueOf(item.getLabel()).trim().equals("")) {
				band = true;
				break;
			}
		}
		return band;		
	}	

	public void poblarValores(String cadena){
		try{
			log.info(cadena);
			String[] valores2 = cadena.split(";");
			this.valores = new ArrayList<SelectItem>();
			for (String string : valores2) {
				String[] val = string.split("\\|"); 
				log.info(val[0]+" # "+val[1]);
				SelectItem s = new SelectItem(String.valueOf(val[0]), val[1]);
				valores.add(s);
			}
		}catch (Exception e ){
			log.info("Error al poblar");
		}
		for (SelectItem se : valores) {
			log.info(se.getValue()+"  "+se.getLabel());
		}
	
	}
	
	/**
	 * Obtiene la 
	 * @return valores empaquetados
	 */
	public String obtenerValoresEnCadena(){
		StringBuffer cadenaFormateada = new StringBuffer();
		for (SelectItem item : this.valores) {
			cadenaFormateada = cadenaFormateada.append(String.valueOf(item.getValue())).append("|").append(item.getLabel()).append(";"); 
		}
		
		String cadena = cadenaFormateada.toString();
		cadena = cadena.substring(0,cadena.length());
		log.info("cadena formateada: " + cadena);
		return cadena;
	}



	public List<SelectItem> getValores() {
		return valores;
	}

	public void setValores(List<SelectItem> valores) {
		this.valores = valores;
	}

	public boolean isValoresNumericos() {
		return valoresNumericos;
	}

	public void setValoresNumericos(boolean valoresNumericos) {
		this.valoresNumericos = valoresNumericos;
	}



}
