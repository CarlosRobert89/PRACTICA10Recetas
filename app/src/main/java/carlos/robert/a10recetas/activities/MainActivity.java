package carlos.robert.a10recetas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import carlos.robert.a10recetas.R;
import carlos.robert.a10recetas.adapters.CategoriasAdapter;
import carlos.robert.a10recetas.conexiones.ApiConexiones;
import carlos.robert.a10recetas.conexiones.RetrofitObject;
import carlos.robert.a10recetas.databinding.ActivityMainBinding;
import carlos.robert.a10recetas.helpers.Constantes;
import carlos.robert.a10recetas.modelos.Categoria;
import carlos.robert.a10recetas.modelos.Categorias;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ArrayList<Categoria> listaCategorias;
    private CategoriasAdapter adapter;
    private RecyclerView.LayoutManager lm;

    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaCategorias = new ArrayList<>();
        sp = getSharedPreferences(Constantes.ULTIMA_RECETA,MODE_PRIVATE);

        verUltimoAcceso();

        adapter = new CategoriasAdapter(R.layout.row_view_holder, listaCategorias, this);
        lm = new LinearLayoutManager(this);

        binding.contenedorCategorias.setAdapter(adapter);
        binding.contenedorCategorias.setLayoutManager(lm);



        cargarListaCategorias();
    }

    private void verUltimoAcceso() {
        String email = sp.getString(Constantes.EMAIL, "");
        String receta = sp.getString(Constantes.RECETA, "");

        Toast.makeText(this, "EMAIL "+email+" RECETA "+receta, Toast.LENGTH_SHORT).show();
    }

    private void cargarListaCategorias() {
        Retrofit retrofit;
        ApiConexiones api;

        retrofit = RetrofitObject.getConexion();
        api = retrofit.create(ApiConexiones.class);

        Call<Categorias> getCategorias = api.getCategorias();

        getCategorias.enqueue(new Callback<Categorias>() {
            @Override
            public void onResponse(Call<Categorias> call, Response<Categorias> response) {
                if(response.code()== HttpsURLConnection.HTTP_OK){
                    ArrayList<Categoria> temp = (ArrayList<Categoria>) response.body().getCategories();
                    listaCategorias.addAll(temp);
                    adapter.notifyItemRangeInserted(0, temp.size());
                }

            }

            @Override
            public void onFailure(Call<Categorias> call, Throwable t) {
                Toast.makeText(MainActivity.this, "ERROR"+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}