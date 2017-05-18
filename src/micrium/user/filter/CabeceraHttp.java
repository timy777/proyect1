package micrium.user.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import sun.misc.FormattedFloatingDecimal.Form;

public class CabeceraHttp {
	
	private static Logger log = Logger.getLogger(CabeceraHttp.class);
	private static HashMap<String, String> encabezados=new HashMap<String, String>();
	private static HashMap<String, String> encabezadosSeguro=new HashMap<String, String>();
	private static final String CODIFICACION="UTF-8";
	private static final String SEPARADOR_LINEA="\n";
	private static final String SEPARADOR_COLUMNA="|h|";//Le pondremos |s| como separador si se aplica sólo cuando es seguro (https), |h| si se aplica en cualquier caso
	private static final String SEPARADOR_COLUMNA_SEGURO="|s|";
	
	private static void cargarCabezas(){
		InputStream file=null;
		try{ 
        	ClassLoader myClase = Thread.currentThread().getContextClassLoader();
	        if(myClase!=null){
		        file=myClase.getResourceAsStream("Cabeceras.header");
		        int bytesDisponibles=file.available();
		        byte vector[]=new byte[bytesDisponibles];
		        file.read(vector);
		        String texto=new String(vector,CODIFICACION);
		        log.debug("\nSe ha leido el archivo de encabezados:\n"+texto+"\n");
		        String[] cabezas=texto.split(SEPARADOR_LINEA);
		        log.debug("Se ha leido "+cabezas.length+" encabezados en el archivo");
		        for(int i=0;i<cabezas.length;i++){
		        	try{
		        		//log.debug("En la iteracion cabezas["+i+"] ="+cabezas[i]);
		        		int indiceSeparador=cabezas[i].indexOf(SEPARADOR_COLUMNA);
			        	//String[] cabe_valor=cabezas[i].split( SEPARADOR_COLUMNA);
		        		if(indiceSeparador!=-1){
			        		String clave=cabezas[i].substring(0, indiceSeparador);
			        		String valor=cabezas[i].substring(indiceSeparador+SEPARADOR_COLUMNA.length(), cabezas[i].length() );
				        	log.debug("clave ="+clave+"; valor ="+valor);
				        	encabezados.put(clave.trim(), valor.trim());
			        	}else{
			        		int indiceSeparadorSeg=cabezas[i].indexOf(SEPARADOR_COLUMNA_SEGURO);
			        		if(indiceSeparadorSeg!=-1){
			        			String clave=cabezas[i].substring(0, indiceSeparadorSeg);
				        		String valor=cabezas[i].substring(indiceSeparadorSeg+SEPARADOR_COLUMNA_SEGURO.length(), cabezas[i].length() );
					        	log.debug("clave ="+clave+"; valor ="+valor);
					        	encabezadosSeguro.put(clave.trim(), valor.trim());
			        		}
			        	}
		        	}catch(Exception e2){
		        		log.error("En iteracion "+i+" "+e2); 
		        	}
		        }
		        log.debug("Se han cargado "+cabezas.length+" encabezados");
		    }
        }catch(RuntimeException ex){
        	log.error(ex);
        }catch(Exception ex){
        	log.error(ex);
        } finally {
        	try {
        		if(file!=null){
        			file.close();
        		}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
        }
	}
	static{
		cargarCabezas();
	}
	public static void ponerCabeceras(HttpServletResponse httpResponse,ServletRequest request){
		try {
			for(Entry<String, String> e: encabezados.entrySet()){
				String K,V;
				K=e.getKey();
				V=e.getValue();
				if(!httpResponse.containsHeader(K)){
					httpResponse.addHeader(K, V);
					/*log.debug("Se ha agregado el encabezado HTTP   "+K);*/
				}/*else{
					log.debug("La respuesta ya contenía encabezado "+K+" por lo tanto no se ha agregado en la respuesta HTTP");
				}*/
			}
			if(request.isSecure()){
				log.debug("Esta es una peticion HTTPS");
				for(Entry<String, String> e: encabezadosSeguro.entrySet()){
					String K,V;
					K=e.getKey();
					V=e.getValue();
					if(!httpResponse.containsHeader(K)){
						httpResponse.addHeader(K, V);
						/*log.debug("Se ha agregado el encabezado HTTPS   "+K);*/
					}/*else{
						log.debug("La respuesta ya contenía encabezado "+K+" por lo tanto no se ha agregado en la respuesta HTTPS");
					}*/
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
	}

}
