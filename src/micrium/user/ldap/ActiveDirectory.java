package micrium.user.ldap;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.NamingEnumeration;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;

import micrium.user.id.ParametroID;
import micrium.user.sys.P;

import org.apache.log4j.Logger;

public class ActiveDirectory {

	public static int EXIT_USER = 1;
	public static int NOT_EXIT_USER = 2;
	public static int ERROR = 3;

	private static Logger log = Logger.getLogger(ActiveDirectory.class);

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static int validarUsuario(String usuario, String password) throws LdapContextException {
		if (usuario == null || usuario.trim().isEmpty() || password == null || password.trim().isEmpty()) {
			return NOT_EXIT_USER;
		}
		Hashtable env = new Hashtable();
		try {
			env.put("java.naming.factory.initial", (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_initial_context_factory));
			env.put("java.naming.provider.url", (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_provider_url));
			env.put("java.naming.security.authentication", (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_authentication));
			env.put("java.naming.security.principal", (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_principal) + usuario);
			env.put("java.naming.security.credentials", password);

		} catch (Exception e) {
			log.error("[validarUsuario] usuario: " + usuario + ", Fallo al establecer conexion con LDAP", e);
			return ERROR;
		}
		try {
			InitialDirContext ctx = new InitialDirContext(env);
			ctx.close();
			return EXIT_USER;
		} catch (Exception e) {
			log.error("[validarUsuario] usuario: " + usuario + ", Fallo al establecer conexion con LDAP", e);
			throw (new LdapContextException(e.getMessage(), e));
			// return NOT_EXIT_USER;
		}
	}

	@SuppressWarnings("rawtypes")
	public static boolean validarGrupo(String grupo) throws LdapContextException {
		InitialDirContext dirC = null;
		NamingEnumeration answer = null;
		NamingEnumeration ae = null;

		Hashtable<String, String> env = new Hashtable<String, String>();

		env.put("java.naming.factory.initial", (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_initial_context_factory));
		env.put("java.naming.provider.url", (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_provider_url));
		env.put("java.naming.security.authentication", (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_authentication));
		env.put("java.naming.security.principal", (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_principal) + (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_user));
		env.put("java.naming.security.credentials", (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_credentials));

		try {
			dirC = new InitialDirContext(env);
		} catch (Exception e) {
			log.error("[validarGrupo] grupo: " + grupo + ", Fallo al validar el grupo", e);
			throw (new LdapContextException(e.getMessage(), e));
		}
		try {
			if (dirC != null) {
				String searchBase = "DC=tigo,DC=net,DC=bo";
				SearchControls searchCtls = new SearchControls();
				searchCtls.setSearchScope(2);
				String searchFilter = "(objectclass=group)";
				String[] returnAtts = { "cn" };
				searchCtls.setReturningAttributes(returnAtts);
				answer = dirC.search(searchBase, searchFilter, searchCtls);

				while (answer.hasMoreElements()) {
					SearchResult sr = (SearchResult) answer.next();
					Attributes attrs = sr.getAttributes();
					if (attrs != null) {
						for (ae = attrs.getAll(); ae.hasMore();) {
							Attribute attr = (Attribute) ae.next();
							if (attr.get().toString().equals(grupo)) {
								return true;
							}
						}

					}

				}

			}

		} catch (Exception e) {
			log.error("[validarGrupo] grupo: " + grupo + ", Fallo al validar el grupo", e);
		} finally {
			try {
				if (dirC != null)
					dirC.close();
			} catch (Exception e) {
				log.warn("[validarGrupo] grupo: " + grupo + ", Fallo al cerrar el contexto LDAP", e);
			}
		}
		return false;
	}

	@SuppressWarnings({ "rawtypes", "unchecked", "unused" })
	public static List<String> getListaGrupos(String usuario) throws LdapContextException {
		List<String> lGrupos = new ArrayList<String>();

		Hashtable env = new Hashtable();
		env.put("java.naming.factory.initial", P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_initial_context_factory));
		env.put("java.naming.provider.url", P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_provider_url));
		env.put("java.naming.security.authentication", P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_authentication));
		env.put("java.naming.security.principal", (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_principal) + (String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_user));
		env.put("java.naming.security.credentials", P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_credentials));

		InitialLdapContext ctx = null;
		try {
			ctx = new InitialLdapContext(env, null);
			String searchBase = "DC=tigo,DC=net,DC=bo";
			SearchControls searchCtls = new SearchControls();
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			String returnAtts[] = { "memberOf" };
			String searchFilter = "(&(objectCategory=person)(objectClass=user)(mailNickname=" + usuario + "))";

			searchCtls.setReturningAttributes(returnAtts);

			NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
			int totalResults = 0;

			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				Attributes attrs = sr.getAttributes();
				if (attrs != null) {
					for (NamingEnumeration ne = attrs.getAll(); ne.hasMore();) {
						Attribute attr = (Attribute) ne.next();
						String grupo;
						for (NamingEnumeration e = attr.getAll(); e.hasMore(); totalResults++) {
							grupo = e.next().toString().trim();
							grupo = grupo.substring(3, grupo.indexOf(",")).trim();
							lGrupos.add(grupo);
						}
					}
				}
			}

		} catch (Exception e) {
			log.error("[getListaGrupos] usuario: " + usuario + ", Error al obtener el listado de grupos", e);
			throw (new LdapContextException(e.getMessage(), e));
		} finally {
			try {
				ctx.close();
			} catch (Exception e2) {
				log.warn("[getListaGrupos] usuario: " + usuario + ", Fallo al cerrar el InitialLdapContext", e2);
			}
		}
		return lGrupos;
	}

	@SuppressWarnings("rawtypes")
	public static String getNombreCompleto(String usuario) throws LdapContextException {

		Conexion conexion = new Conexion();
		conexion.setDominio((String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_dominio));
		conexion.setUrl((String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_provider_url));
		conexion.setUsuario((String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_user));
		conexion.setClave((String) P.getParamVal(ParametroID.DIRECTORIO_ACTIVO_security_credentials));

		Ldap ld = new Ldap(conexion);
		try {
			String[] returnAtts = { "cn", "sAMAccountName", "mail", "sn", "givenName", "initials" };
			Map mapa = ld.obtenerDatos(usuario, returnAtts);
			if (mapa.isEmpty()) {
				return "";
			}

			return String.valueOf(mapa.get("givenName") + " " + (String) mapa.get("sn")).trim();

		} catch (Exception e) {
			log.error("[getNombreCompleto] usuario: " + usuario + ", Fallo al obtener el nombre completo del usuario", e);
			throw (new LdapContextException(e.getMessage(), e));
		}
	}

}