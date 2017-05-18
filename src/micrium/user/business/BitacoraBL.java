package micrium.user.business;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.Calendar;

import javax.inject.Named;

import micrium.user.ldap.DescriptorBitacora;
import micrium.user.model.MuBitacora;

import org.apache.log4j.Logger;

import com.tigo.dao.MasterDao;

@Named
public class BitacoraBL extends MasterDao implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Logger log = Logger.getLogger(BitacoraBL.class);

	@SuppressWarnings("rawtypes")
	public void accionInsert(String user, String ip, Enum dato, String id, String name) {

		String ele = ((DescriptorBitacora) dato).getFormulario();
		String formulario = ((DescriptorBitacora) dato).getFormulario();
		String accion = "Se adiciono:" + ele + " con Id:" + id + ", Nombre:" + name;

		MuBitacora bitacora = new MuBitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		bitacora.setFecha(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		try {
			save(bitacora);
		} catch (Exception e) {
			log.error("[user: " + user + ", ip: " + ip + "] [Fallo al guardar la accion insert en bitacora del sistema]", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void accionDelete(String user, String ip, Enum dato, String id, String name) {

		String ele = ((DescriptorBitacora) dato).getFormulario();
		String formulario = ((DescriptorBitacora) dato).getFormulario();
		String accion = "Se elimino:" + ele + " con Id:" + id + ", Nombre:" + name;

		MuBitacora bitacora = new MuBitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		bitacora.setFecha(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		try {
			save(bitacora);
		} catch (Exception e) {
			log.error("[user: " + user + ", ip: " + ip + "] [Fallo al guardar la accion eliminar en bitacora del sistema]", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void accionUpdate(String user, String ip, Enum dato, String id, String name) {

		String ele = ((DescriptorBitacora) dato).getFormulario();
		String formulario = ((DescriptorBitacora) dato).getFormulario();
		String accion = "Se modifico:" + ele + " con Id:" + id + ", Nombre:" + name;

		MuBitacora bitacora = new MuBitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		bitacora.setFecha(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		try {
			save(bitacora);
		} catch (Exception e) {
			log.error("[user: " + user + ", ip: " + ip + "] [Fallo al guardar la accion update en bitacora del sistema]", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void accionFind(String user, String ip, Enum dato, String id, String name) {

		String ele = ((DescriptorBitacora) dato).getFormulario();
		String formulario = ((DescriptorBitacora) dato).getFormulario();
		String accion = "Se busco:" + ele + " con Campos:" + id + ", Otros:" + name;

		MuBitacora bitacora = new MuBitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		bitacora.setFecha(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		try {
			save(bitacora);
		} catch (Exception e) {
			log.error("[user: " + user + ", ip: " + ip + "] [Fallo al guardar la accion buscar en bitacora del sistema]", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void accionCortar(String user, String ip, Enum dato, String id) {

		String ele = ((DescriptorBitacora) dato).getFormulario();
		String formulario = ((DescriptorBitacora) dato).getFormulario();
		String accion = "Se corto:" + ele + " con NumeroTelefono:" + id;

		MuBitacora bitacora = new MuBitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		bitacora.setFecha(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		try {
			save(bitacora);
		} catch (Exception e) {
			log.error("[user: " + user + ", ip: " + ip + "] [Fallo al guardar la accion cortar en bitacora del sistema]", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void accionReconectar(String user, String ip, Enum dato, String id) {

		String ele = ((DescriptorBitacora) dato).getFormulario();
		String formulario = ((DescriptorBitacora) dato).getFormulario();
		String accion = "Se reconecto:" + ele + " con NumeroTelefono:" + id;

		MuBitacora bitacora = new MuBitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		bitacora.setFecha(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		try {
			save(bitacora);
		} catch (Exception e) {
			log.error("[user: " + user + ", ip: " + ip + "] [Fallo al guardar la accion reconectar en bitacora del sistema]", e);
		}
	}

	@SuppressWarnings("rawtypes")
	public void accion(String user, String ip, Enum dato, String accion) {

		String ele = ((DescriptorBitacora) dato).getFormulario();
		String formulario = ((DescriptorBitacora) dato).getFormulario();
		accion = ele + " - " + accion;

		MuBitacora bitacora = new MuBitacora();
		bitacora.setUsuario(user);
		bitacora.setFormulario(formulario);
		bitacora.setDireccionIp(ip);
		bitacora.setAccion(accion);
		bitacora.setFecha(new Timestamp(Calendar.getInstance().getTimeInMillis()));
		try {
			save(bitacora);
		} catch (Exception e) {
			log.error("[user: " + user + ", ip: " + ip + ", accion: " + accion + "] [Fallo al guardar la accion en bitacora del sistema]", e);
		}
	}
}
