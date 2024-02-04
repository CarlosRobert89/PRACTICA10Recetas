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
import carlos.robert.a10recetas.activities.RecetaActivity;
import carlos.robert.a10recetas.helpers.Constantes;
import carlos.robert.a10recetas.modelos.Comida;

public class ComidasAdapter extends RecyclerView.Adapter<ComidasAdapter.ComidasVH> {
    private int resource;
    private List<Comida> objects;
    private Context context;

    public ComidasAdapter(int resource, List<Comida> objects, Context context) {
        this.resource = resource;
        this.objects = objects;
        this.context = context;
    }

    @NonNull
    @Override
    public ComidasVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(resource, null);

        itemView.setLayoutParams(new RecyclerView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        ));
        return new ComidasVH(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ComidasVH holder, int position) {
        Comida comida = objects.get(position);

        holder.nombre.setText(comida.getStrMeal());

        Picasso.get()
                .load(comida.getStrMealThumb())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_foreground)
                .into(holder.foto);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RecetaActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString(Constantes.IDCOMIDA, comida.getIdMeal());
                intent.putExtras(bundle);

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public class ComidasVH extends RecyclerView.ViewHolder {
        ImageView foto;
        TextView nombre;

        public ComidasVH(@NonNull View itemView) {
            super(itemView);
            foto = itemView.findViewById(R.id.imgFotoRow);
            nombre = itemView.findViewById(R.id.lbNombreRow);
        }
    }
}
