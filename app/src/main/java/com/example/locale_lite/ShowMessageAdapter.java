package com.example.locale_lite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.locale_lite.ui.dashboard.DashboardFragmentSP;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ShowMessageAdapter extends RecyclerView.Adapter<ShowMessageAdapter.ViewHolder> {

    private Context mcontext;
    private List<Customers> musers;

    public ShowMessageAdapter(Context mcontext, List<Customers> musers){
        this.musers = musers;
        this.mcontext = mcontext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mcontext).inflate(R.layout.list_row,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        final Customers c = musers.get(position);
        holder.username.setText(c.getFirstname()+" "+c.getLastname());
        holder.profilepic.setImageResource(R.drawable.cuslogo);
        holder.phone.setText(c.getPhonenum());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mcontext,Chat.class);
                intent.putExtra("type","Customer");
                intent.putExtra("name",c.getFirstname()+" "+c.getLastname());
                intent.putExtra("phone",c.getPhonenum());
                intent.putExtra("userid",c.getId());
                mcontext.startActivity(intent);}

        });


    }

    @Override
    public int getItemCount() {
        return musers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView username;
        public ImageView profilepic;
        public TextView phone;

        public ViewHolder(View itemView){
            super(itemView);

            username = itemView.findViewById(R.id.name_list);
            profilepic = itemView.findViewById(R.id.providers_image);
            phone = itemView.findViewById(R.id.info_list);
        }
    }
}
