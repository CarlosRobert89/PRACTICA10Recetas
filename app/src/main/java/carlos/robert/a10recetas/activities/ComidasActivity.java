package carlos.robert.a10recetas.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import carlos.robert.a10recetas.R;
import carlos.robert.a10recetas.adapters.ComidasAdapter;
import carlos.robert.a10recetas.conexiones.ApiConexiones;
import carlos.robert.a10recetas.conexiones.RetrofitObject;
import carlos.robert.a10recetas.databinding.ActivityComidasBinding;
import carlos.robert.a10recetas.helpers.Constantes;
import carlos.robert.a10recetas.modelos.Comida;
import carlos.robert.a10recetas.modelos.Comidas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class ComidasActivity extends AppCompatActivity {
    private ActivityComidasBinding binding;
    private ArrayList<Comida> listaComidas;
    private ComidasAdapter adapter;
    private RecyclerView.LayoutManager lm;
    private Retrofit retrofit;
    private ApiConexiones api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityComidasBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaComidas = new ArrayList<>();
        adapter = new ComidasAdapter(R.layout.row_view_holder, listaComidas, this);
        lm = new LinearLayoutManager(this);

        binding.contenedorComidas.setAdapter(adapter);
        binding.contenedorComidas.setLayoutManager(lm);

        retrofit = RetrofitObject.getConexion();
        api = retrofit.create(ApiConexiones.class);


        String categoria = getIntent().getExtras().getString(Constantes.CATEGORIA);
        if (categoria != null) {
            cargarComidas(categoria);
        }
    }

    private void cargarComidas(String categoria) {
        Call<Comidas> getComidas = api.getComidas(categoria);

        getComidas.enqueue(new Callback<Comidas>() {
            @Override
            public void onResponse(Call<Comidas> call, Response<Comidas> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    listaComidas.addAll(response.body().getMeals());
                    adapter.notifyItemRangeInserted(0, listaComidas.size());
                }
            }

            @Override
            public void onFailure(Call<Comidas> call, Throwable t) {
                Toast.makeText(ComidasActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }
}