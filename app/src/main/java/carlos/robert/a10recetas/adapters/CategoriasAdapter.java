package carlos.robert.a10recetas.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import carlos.robert.a10recetas.R;
import carlos.robert.a10recetas.activities.ComidasActivity;
import carlos.robert.a10recetas.helpers.Constantes;
import carlos.robert.a10recetas.modelos.Categoria;

public class CategoriasAdapter extends RecyclerView.Adapter<CategoriasAdapter.CategoriaVH> {
    private int resources; //vista de cada fila cargada
    private List<Categoria> objects; //lista de filas en espera
    private Context context; //actividad donde se mostrará el Recycler

    //CONSTRUCTOR
    public CategoriasAdapter(int resources, List<Categoria> objects, Context context) {
        this.resources = resources;
        this.objects = objects;
        this.context = context;
    }

    //CREA LA ESTRUCTURA VACÍA DE LA FILA Y DEVUELVE UN VIEWHOLDER LISTO PARA RELLENAR CON DATOS
    @NonNull
    @Override
    public CategoriaVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(resources, null);
        itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new CategoriaVH(itemView);
    }

    //RELLENA EL VIEWHOLDER CON LOS DATOS DEL OBJETO (SEGÚN LA POSICIÓN DADA).
    @Override
    public void onBindViewHolder(@NonNull CategoriaVH holder, int position) {
        Categoria categoria = objects.get(position); //se obtiene el objeto según la posición
        holder.nombre.setText(categoria.getStrCategory());

        Picasso.get()
                .load(categoria.getStrCategoryThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ComidasActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constantes.CATEGORIA, categoria.getStrCategory());
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
    }

    //TAMAÑO DE LA LISTA CATEGORIAS
    @Override
    public int getItemCount() {
        return objects.size();
    }

    //CLASE INTERNA (para acceder a los elementos de la vista mediante holder.
    public class CategoriaVH extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView nombre;

        public CategoriaVH(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imgFotoRow);
            nombre = itemView.findViewById(R.id.lbNombreRow);
        }
    }
}

