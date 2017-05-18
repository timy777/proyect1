package micrium.user.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the MU_TIPO_PARAMETRO database table.
 * 
 */
@Entity
@Table(name = "MU_TIPO_PARAMETRO")
public class MuTipoParametro implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "MU_TIPO_PARAMETRO_ID")
	private long muTipoParametroId;

	private String descripcion;

	private boolean estado;

	private String nombre;

	public MuTipoParametro() {
	}

	public long getMuTipoParametroId() {
		return this.muTipoParametroId;
	}

	public void setMuTipoParametroId(long muTipoParametroId) {
		this.muTipoParametroId = muTipoParametroId;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

}