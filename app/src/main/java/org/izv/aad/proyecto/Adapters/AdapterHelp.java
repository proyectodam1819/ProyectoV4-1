package org.izv.aad.proyecto.Adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.izv.aad.proyecto.Objects.HelpObject;
import org.izv.aad.proyecto.R;

import java.util.ArrayList;

public class AdapterHelp extends RecyclerView.Adapter <AdapterHelp.MyViewHolder> {

    ArrayList<HelpObject> helps;

    public AdapterHelp(ArrayList<HelpObject> helps){
        this.helps = helps;
    }

    @NonNull
    @Override
    public AdapterHelp.MyViewHolder onCreateViewHolder(@NonNull final ViewGroup viewGroup, int i) {
        final View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_help, viewGroup,false);
        return new AdapterHelp.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final AdapterHelp.MyViewHolder myViewHolder, int i) {
        bind(myViewHolder);
    }

    private void bind(final MyViewHolder holder){
        final int position = holder.getAdapterPosition();
        holder.tvTitle.setText(helps.get(position).getTitle());
        holder.tdInformation.setText(helps.get(position).getDescription());

        holder.tvTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.tdInformation.getVisibility() == View.GONE){
                    holder.tdInformation.setVisibility(View.VISIBLE);
                }else{
                    holder.tdInformation.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return helps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tdInformation;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tdInformation = itemView.findViewById(R.id.tdInformation);
        }


    } {
}
}
