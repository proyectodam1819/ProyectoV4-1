package org.izv.aad.proyecto.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.izv.aad.proyecto.Objects.Author;
import org.izv.aad.proyecto.R;

import java.util.List;

public class AdapterAuthorList extends RecyclerView.Adapter<AdapterAuthorList.MyViewHolder> {

    List<Author> authors;

    public AdapterAuthorList(List<Author> authors) {
        this.authors = authors;
    }

    @NonNull
    @Override
    public AdapterAuthorList.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_authorl_list, viewGroup, false);
        return new AdapterAuthorList.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterAuthorList.MyViewHolder myViewHolder, int i) {
        bind(myViewHolder);
    }

    private void bind(final MyViewHolder holder) {
        final int position = holder.getAdapterPosition();
        holder.tvAuthor.setText(authors.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return authors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvAuthor;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvAuthor = itemView.findViewById(R.id.authorTV);

        }


    }
}
