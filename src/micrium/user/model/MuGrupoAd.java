package micrium.user.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the MU_GRUPO_AD database table.
 * 
 */
@Entity
@Table(name = "MU_GRUPO_AD")
public class MuGrupoAd implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "MU_GRUPO_AD_GRUPOID_GENERATOR", sequenceName = "SEQ_MU_GRUPO_AD", allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MU_GRUPO_AD_GRUPOID_GENERATOR")
	@Column(name = "GRUPO_ID")
	private long grupoId;

	private String detalle;

	private boolean estado;

	private String nombre;

	// uni-directional many-to-one association to MuRol
	@ManyToOne
	@JoinColumn(name = "ROL_ID")
	private MuRol muRol;

	public MuGrupoAd() {
	}

	public long getGrupoId() {
		return this.grupoId;
	}

	public void setGrupoId(long grupoId) {
		this.grupoId = grupoId;
	}

	public String getDetalle() {
		return this.detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
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

	public MuRol getMuRol() {
		return this.muRol;
	}

	public void setMuRol(MuRol muRol) {
		this.muRol = muRol;
	}

}