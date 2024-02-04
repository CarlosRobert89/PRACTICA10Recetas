package carlos.robert.a10recetas.conexiones;

import carlos.robert.a10recetas.modelos.Categorias;
import carlos.robert.a10recetas.modelos.Comidas;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiConexiones {
    @GET("/api/json/v1/1/categories.php") //traer todas las categorías
    Call<Categorias> getCategorias();

    @GET("/api/json/v1/1/filter.php") //traer todas las categorías
    Call<Comidas> getComidas(@Query("c") String categoria);

    @GET("/api/json/v1/1/lookup.php") //traer todas las categorías
    Call<Comidas> getRecetas(@Query("i") String id);



}
