package micrium.user.model;

import java.io.Serializable;

public class AccionModel implements Serializable {
	private static final long serialVersionUID = 1L;

	private long id;
	private String nombre;
	private long fomrID;
	private long rolID;

	public AccionModel(long id, String nombre, long fomrID, long rolID) {
		this.id = id;
		this.nombre = nombre;
		this.fomrID = fomrID;
		this.rolID = rolID;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getFomrID() {
		return fomrID;
	}

	public void setFomrID(long fomrID) {
		this.fomrID = fomrID;
	}

	public long getRolID() {
		return rolID;
	}

	public void setRolID(long rolID) {
		this.rolID = rolID;
	}

	@Override
	public String toString() {
		return nombre;
	}

}
