package carlos.robert.a10recetas.modelos;

import java.util.List;

public class Categorias{
	private List<Categoria> categories;

	public void setCategories(List<Categoria> categories){
		this.categories = categories;
	}

	public List<Categoria> getCategories(){
		return categories;
	}

	@Override
 	public String toString(){
		return 
			"Categorias{" + 
			"categories = '" + categories + '\'' + 
			"}";
		}
}