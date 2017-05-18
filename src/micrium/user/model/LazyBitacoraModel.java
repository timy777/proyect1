package micrium.user.model;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.inject.Inject;
import javax.inject.Named;

import micrium.user.business.BitacoraBL;

import org.apache.log4j.Logger;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortOrder;

import com.tigo.dao.PQuery;

@Named
public class LazyBitacoraModel extends LazyDataModel<MuBitacora> {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(LazyBitacoraModel.class);

	@Inject
	private BitacoraBL bl;
	
	private Integer cantidadTuplas=-1;

	public LazyBitacoraModel() {
	}

	@Override
	public MuBitacora getRowData(String rowKey) {
//		log.debug("Me han llamado");
		try {
			long key = Long.valueOf(rowKey);
			return (MuBitacora) bl.find(key, MuBitacora.class);
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public Object getRowKey(MuBitacora object) {
		return object.getId();
	}

//	private static int secuencia=0;
//	private static int siguienteValor(){secuencia++;return secuencia;}
	
	@SuppressWarnings("unchecked")
	@Override
	public int getRowCount() {
//		log.debug("Me han llamado, secuencia ="+siguienteValor());
		if( consultaTabla==null || consultaTabla.isEmpty()){
			String sql = "SELECT COUNT(0) AS TOTAL FROM MU_BITACORA";
			try {
				Object objeto =bl.findSingleResultNativeQuery(sql, null);
				if(objeto!=null){
					String numeroEnCadena=String.valueOf(objeto);
					cantidadTuplas=new BigInteger(numeroEnCadena).intValue();
				}else{cantidadTuplas=0;}
			} catch (Exception e) {
				log.error("Fallo al cargar la cantidad de datos existente en la bitacora", e);
			}
		}else{
			if(cantidadTuplas==-1){
				try{
//					cantidadTuplas =bl.findAllQuery(MuBitacora.class, consultaTabla, parametrosFiltro).size();
//					cantidadTuplas =bl.countAllQuery(MuBitacora.class, consultaTabla, parametrosFiltro);
					String total=String.valueOf( bl.findSingleResultQuery( consultaTabla.replaceFirst("SELECT b ", "Select count(b) "), parametrosFiltro));
					cantidadTuplas =Integer.parseInt(total);
//					log.debug("Se ha contado "+cantidadTuplas + " tuplas");
				}catch(Exception e ){
					log.error("Al contar las tuplas",e);
				}
			}
		}
		return cantidadTuplas;
	}
	
	private static String consultaTabla;
	private static PQuery parametrosFiltro;
	
	

	@SuppressWarnings("unchecked")
	@Override
	public List<MuBitacora> load(int first, int pageSize, String sortField, SortOrder sortOrder, Map<String, Object> filters) {
//		log.debug("Me han llamado, secuencia ="+siguienteValor());
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("SELECT b FROM MuBitacora b");

			PQuery p = PQuery.getInstancia();
			int contador = 0;
			StringBuilder where = new StringBuilder();
//			for (String key : filters.keySet()) {
//				if (!filters.get(key).toString().trim().isEmpty()) {
//					p.put(key, "%" + filters.get(key).toString().trim().toLowerCase() + "%");// Se ha agregado .trim().toLowerCase()
//					if (!key.equals("fecha")) {
//						where.append("LOWER(b.");
//						where.append(key);
//					} else {
//						where.append("TO_CHAR(b.");
//						where.append(key);
//						where.append(", 'DD/MM/YYYY HH24:MI:SS'");
//					}
//					where.append(") LIKE :");
//					where.append(key);
//					where.append(" AND ");
//					contador++;
//				}
//			}
			for (Map.Entry<String, Object> entry : filters.entrySet()) {
				if (!entry.getKey().toString().trim().isEmpty()) {
					p.put(entry.getKey(), "%" + entry.getValue().toString().trim().toLowerCase() + "%");// Se ha agregado .trim().toLowerCase()
					if (!entry.getKey().equals("fecha")) {
						where.append("LOWER(b.");
						where.append(entry.getKey());
					} else {
						where.append("TO_CHAR(b.");
						where.append(entry.getKey());
						where.append(", 'DD/MM/YYYY HH24:MI:SS'");
					}
					where.append(") LIKE :");
					where.append(entry.getKey());
					where.append(" AND ");
					contador++;
				}
			}
			if (contador > 0) {
				sb.append(" WHERE ");
				where.setLength(where.length() - 4);
				sb.append(where.toString());
			}
			consultaTabla=sb.toString();//Para contar no necesitamos que ordene, postgre lo rechaza el order by cuando se hace un count
			if(sortField != null && !sortField.trim().isEmpty() && sortOrder != null){
				if ( sortOrder.equals(SortOrder.ASCENDING)) {
					sb.append(" ORDER BY b.");
					sb.append(sortField);
					sb.append(" ASC ");
				} else if ( sortOrder.equals(SortOrder.DESCENDING)) {
					sb.append(" ORDER BY b.");
					sb.append(sortField);
					sb.append(" DESC ");
				}
			}else{
				sb.append(" ORDER BY b.id DESC");
			}
			cantidadTuplas=-1;//Esto para que actualize la cantidad tras cada página
//			consultaTabla=sb.toString();
			log.debug("ConsultaTabla:\n"+consultaTabla);
			parametrosFiltro=p;
			List<MuBitacora> listaResultante=bl.findAllQuery(MuBitacora.class,sb.toString() , p, first, pageSize);
			return listaResultante;
		} catch (Exception e) {
			log.error("[Fallo al guardar los datos de la bitacora]", e);
		}
		return new ArrayList<MuBitacora>();
	}
}
