package micrium.user.model;

import java.io.Serializable;

public class RolModel implements Serializable, Comparable<RolModel> {

	private static final long serialVersionUID = 1L;

	private long id;
	private String nombre;
	private long rolID;

	public RolModel() {
		this(0, "", 0);
	}

	public RolModel(long id, String nombre, long rolID) {
		this.id = id;
		this.nombre = nombre;
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

	public long getRolID() {
		return rolID;
	}

	public void setRolID(long rolID) {
		this.rolID = rolID;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RolModel other = (RolModel) obj;
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return nombre;
	}

	@Override
	public int compareTo(RolModel rol) {
		return this.id == rol.getId() ? 0 : (this.id > rol.getId() ? 1 : -1);
	}

}
