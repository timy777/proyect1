package micrium.user.bean;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;

import micrium.user.model.LazyBitacoraModel;
import micrium.user.model.MuBitacora;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.primefaces.model.LazyDataModel;

import com.tigo.utils.SysExcel;

@ManagedBean
@ViewScoped
public class BitacoraBean implements Serializable {

	private static final long serialVersionUID = 1L;

	public static Logger log = Logger.getLogger(BitacoraBean.class);

	@Inject
	private LazyBitacoraModel listBitacora;

	@PostConstruct
	public void init() {
	}

	public LazyDataModel<MuBitacora> getListBitacora() {
		return listBitacora;
	}

	public void postProcessXLS(Object document) {
		log.info("[Se exporta a excel los cambios]");
		HSSFWorkbook wb = (HSSFWorkbook) document;
		SysExcel.pintarCabecera(wb);
	}

}
