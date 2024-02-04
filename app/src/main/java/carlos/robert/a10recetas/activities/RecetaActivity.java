package carlos.robert.a10recetas.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.squareup.picasso.Picasso;

import java.lang.reflect.GenericArrayType;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

import carlos.robert.a10recetas.R;
import carlos.robert.a10recetas.conexiones.ApiConexiones;
import carlos.robert.a10recetas.conexiones.RetrofitObject;
import carlos.robert.a10recetas.databinding.ActivityRecetaBinding;
import carlos.robert.a10recetas.helpers.Constantes;
import carlos.robert.a10recetas.modelos.Comida;
import carlos.robert.a10recetas.modelos.Comidas;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecetaActivity extends AppCompatActivity {
    private ActivityRecetaBinding binding;
    private Comida comida;
    private FirebaseUser user;
    private FirebaseDatabase database;
    private DatabaseReference reference;
    private ArrayList<Comida> listaRecetas;
    private SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecetaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        listaRecetas = new ArrayList<>();
        database = FirebaseDatabase.getInstance("https://ejemplofirebase-29414-default-rtdb.europe-west1.firebasedatabase.app/");

        sp = getSharedPreferences(Constantes.ULTIMA_RECETA, MODE_PRIVATE);

        String id = getIntent().getExtras().getString(Constantes.IDCOMIDA);
        if (id != null) {
            cargarReceta(id);
        }

        binding.btnGuardarReceta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    //ESTÁ LOGEADO
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putString(Constantes.EMAIL, user.getEmail());
                    editor.putString(Constantes.RECETA, comida.getStrMeal());

                    reference = database.getReference(user.getUid()).child("recetas");

                    //LEER EN LA BD
                    reference.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            if(task.isSuccessful()){
                                GenericTypeIndicator<ArrayList<Comida>> gti = new GenericTypeIndicator<ArrayList<Comida>>() {
                                };
                                ArrayList<Comida> listaTemporal = task.getResult().getValue(gti);
                                if(listaTemporal!=null) {
                                    listaRecetas.addAll(listaTemporal);
                                }
                                //ESCRIBIR LA LISTA EN LA BD
                                listaRecetas.add(comida);
                                reference.setValue(listaRecetas);
                                Toast.makeText(RecetaActivity.this, "Receta añadida", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                } else {
                    //NO ESTÁ LOGEADO
                    startActivity(new Intent(RecetaActivity.this, LoginActivity.class));
                }
            }
        });
    }

    private void cargarReceta(String id) {
        ApiConexiones api = RetrofitObject.getConexion().create(ApiConexiones.class);

        Call<Comidas> getReceta = api.getRecetas(id);
        getReceta.enqueue(new Callback<Comidas>() {
            @Override
            public void onResponse(Call<Comidas> call, Response<Comidas> response) {
                if (response.code() == HttpsURLConnection.HTTP_OK) {
                    comida = response.body().getMeals().get(0);
                    if (comida != null) {
                        rellenarVista();
                    }
                }
            }

            @Override
            public void onFailure(Call<Comidas> call, Throwable t) {
                Toast.makeText(RecetaActivity.this, "ERROR", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rellenarVista() {
        binding.lbNombreReceta.setText(comida.getStrMeal());
        binding.lbCategoriaReceta.setText(comida.getStrCategory());
        binding.lbAreaReceta.setText(comida.getStrArea());
        binding.lbInstruccionesReceta.setText(comida.getStrInstructions());
        Picasso.get()
                .load(comida.getStrMealThumb())
                .into(binding.imFotoReceta);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_logout, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == R.id.logOutMenu){
            FirebaseAuth.getInstance().signOut();
        }
        return true;
    }
}