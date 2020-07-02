package com.example.locale_lite;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;
    private Context mcontext;
    private List<ChatBox> mchat;
    private String imageurl;
    FirebaseUser fuser;

    public MessageAdapter(Context mcontext, List<ChatBox> mchat, String imageurl){
        this.mchat = mchat;
        this.mcontext = mcontext;
        this.imageurl = imageurl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_right, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }else{
            View view = LayoutInflater.from(mcontext).inflate(R.layout.chat_item_left, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            ChatBox chatBox = mchat.get(position);
            holder.show_message.setText((chatBox.getMessage()));
            if(imageurl==null){
                holder.profilepic.setImageResource(R.drawable.cuslogo);
            }
            else{Picasso.with(mcontext).load(imageurl).into(holder.profilepic);}

    }

    @Override
    public int getItemCount() {
        return mchat.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView show_message;
        public ImageView profilepic;

        public ViewHolder(View itemView){
            super(itemView);

            show_message = itemView.findViewById(R.id.show_message);
            profilepic = itemView.findViewById(R.id.chat_dp);
        }
    }

    @Override
    public int getItemViewType(int position) {
        fuser = FirebaseAuth.getInstance().getCurrentUser();
        if(mchat.get(position).getSender().equals(fuser.getUid())){
            return MSG_TYPE_RIGHT;
        }else{
            return MSG_TYPE_LEFT;
        }
    }
}
