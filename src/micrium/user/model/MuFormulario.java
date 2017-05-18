package micrium.user.model;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the MU_FORMULARIO database table.
 * 
 */
@Entity
@Table(name = "MU_FORMULARIO")
public class MuFormulario implements Serializable {
	private static final long serialVersionUID = 1L;
	public static final boolean V_MODULO = true;
	public static final boolean V_PAGINA = false;

	@Id
	private long id;

	private boolean estado;

	// @Column(name = "FORMULARIO_ID")
	// private Long formularioId;
	// uni-directional many-to-one association to MuRol
	@ManyToOne
	@JoinColumn(name = "FORMULARIO_ID")
	private MuFormulario formularioMenu;

	private String nombre;

	private long orden;

	private String url;

	// bi-directional many-to-many association to MuAccion
	@ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	@JoinTable(name = "MU_FORMULARIO_ACCION", joinColumns = { @JoinColumn(name = "FORMULARIO_ID") }, inverseJoinColumns = { @JoinColumn(name = "ACCION_ID") })
	private List<MuAccion> muAccions;

	public MuFormulario() {
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public boolean getEstado() {
		return this.estado;
	}

	public void setEstado(boolean estado) {
		this.estado = estado;
	}

	// public Long getFormularioId() {
	// return this.formularioId;
	// }
	//
	// public void setFormularioId(Long formularioId) {
	// this.formularioId = formularioId;
	// }

	public String getNombre() {
		return this.nombre;
	}

	public MuFormulario getFormularioMenu() {
		return formularioMenu;
	}

	public void setFormularioMenu(MuFormulario formularioMenu) {
		this.formularioMenu = formularioMenu;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public long getOrden() {
		return this.orden;
	}

	public void setOrden(long orden) {
		this.orden = orden;
	}

	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<MuAccion> getMuAccions() {
		return this.muAccions;
	}

	public void setMuAccions(List<MuAccion> muAccions) {
		this.muAccions = muAccions;
	}

	@Override
	public String toString() {
		return "MuFormulario [id=" + id + ", estado=" + estado + ", nombre=" + nombre + ", orden=" + orden + ", url=" + url + ", formularioMenu=" + formularioMenu + "]";
	}

	// @PreRemove
	// private void removeFormularioAcciones() {
	// for (MuAccion u : muAccions) {
	// u.getMuFormularios().remove(this);
	// }
	// muAccions = null;
	// }

}