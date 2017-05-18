package micrium.user.model;

import java.util.List;

public class MenuLogin {

	private String nombre;
	private List<MuFormulario> lMenu;

	public MenuLogin() {
		this("", null);
	}

	public MenuLogin(String nombre, List<MuFormulario> lMenu) {
		this.nombre = nombre;
		this.lMenu = lMenu;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<MuFormulario> getlMenu() {
		return lMenu;
	}

	public void setlMenu(List<MuFormulario> lMenu) {
		this.lMenu = lMenu;
	}

	@Override
	public String toString() {
		return nombre;
	}

}
