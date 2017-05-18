package micrium.user.filter;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

public class ReferrerHttp {
	private static Logger log = Logger.getLogger(ReferrerHttp.class);
	private static final String CODIFICACION="UTF-8";
	private static final String SEPARADOR_LINEA="\n";
	private static LinkedList<String> referencias=new LinkedList<String>();
	
	private static void cargar(){
		InputStream file=null;
		try{
			ClassLoader myClase = Thread.currentThread().getContextClassLoader();
			if(myClase != null){
				file=myClase.getResourceAsStream("ReferrerValido.referrer");
		        int bytesDisponibles=file.available();
		        byte vector[]=new byte[bytesDisponibles];
		        file.read(vector);
		        String texto=new String(vector,CODIFICACION);
		        log.debug("\nSe ha leido el archivo de Referrer Valido:\n"+texto+"\n");
		        String[] lineasTexto=texto.split(SEPARADOR_LINEA);
		        String x;
		        for(int i=0;i<lineasTexto.length;i++){
		        	x=lineasTexto[i].trim();
		        	if(x.length()>0 && x.charAt(0)!='#'){//Si no cumple esta condición no se debe tomar en cuenta
		        		referencias.add(x);
		        	}
		        }
		        log.debug("Se ha cargado "+referencias.size()+" Referrer en total");
		    }
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
		cargar();
	}
	
	/**
	 * Retorna true si se trata de una petición con el Encabezado Referer que no esté en la lista
	 * Si se trata de la página de error retorna false, sin importar el referrer
	 * Si la lista (Archivo de configuración) no contiene Rutas Referrer configurado, entonces 
	 * se retorna siempre false.
	 * @param request
	 * @return
	 */
	public static boolean isReferrerAjeno(ServletRequest request){
		try {
			HttpServletRequest httpRequest=(HttpServletRequest)request;
			String referencia=httpRequest.getHeader("Referer");
			if(referencia!=null){
				referencia=referencia.trim();
			}
			//log.debug("El Referer es ="+referencia);
			if(referencia==null || referencia.trim().length()==0){
				return false;//Es válido, porque, puede ser invocado desde una nueva pestaña
			}else{
				if(referencia.contains("CorreccionManual.xhtml")){
					log.debug("Se ha recibido el referrer de el supuesto error que estamos analizando es:\n"+referencia);
				}
				log.debug("Se ha consultado por la RUTA: "+httpRequest.getRequestURL().toString()
						+"\nPero, "+httpRequest.getRequestURI()+"  se va a comparar con: "+loginFilter.rederingErrorHacker+
						"\nhttpRequest.getRequestURI():  "+httpRequest.getRequestURI());
				if(httpRequest.getRequestURI().toString().compareToIgnoreCase(loginFilter.rederingErrorHacker )==0){
					log.debug("Se ha detectado la pagina de error, no se va a analizar su referrer");
					return false;//SI ya fue redireccionado no se restringe, porque entraríamos en un bucle infinito
				}
				if(referencias.size()==0){
					return false;//Quedamos en que si se deja vacío el archivo no se va a validar
				}else{
					for(int i=0;i<referencias.size();i++){
						if(referencia.indexOf(referencias.get(i))==0){
							return false;
						}
					}
					log.debug("Se ha detectado como maligno al Referrer ="+referencia+"\nSI no es una petición maligna configure adecuadamente el archivo RererrerValido.referrer");
					return true;
				}
			}
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
	
	public static boolean isPageError(ServletRequest request){
		try {
			HttpServletRequest httpRequest=(HttpServletRequest)request;
			if(httpRequest.getRequestURI().toString().compareToIgnoreCase(loginFilter.rederingErrorHacker )==0){
				log.debug("Esta es la pagina de error");
				return true;//SI ya fue redireccionado no se restringe, porque entraríamos en un bucle infinito
			}
		} catch (Exception e) {
			log.error(e);
		}
		return false;
	}
	
}
