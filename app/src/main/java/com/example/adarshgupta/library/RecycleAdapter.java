package com.example.adarshgupta.library;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collections;
import java.util.List;

/**
* Created by adarsh gupta on 3/23/2015.
*/
public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.MyViewHolder> {

    private LayoutInflater inflator;
    List<Information> data= Collections.emptyList();
    private Context context;
    private ClickListener clickListener;


    public RecycleAdapter(Context context,List<Information> data){
        inflator= LayoutInflater.from(context);
        this.data=data;
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= inflator.inflate(R.layout.custom_row,parent,false);
        MyViewHolder holder=new MyViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Information current= data.get(position);
        holder.title.setText(current.title);
        holder.icon.setImageResource(current.itemId);

//       holder.icon.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view) {
//
//            }
//        }
//);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


//    class MyViewHolder extends RecyclerView.ViewHolder {
         class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView title;
        public ImageView icon;
        public MyViewHolder(View itemView) {
            super(itemView);
            title=(TextView)itemView.findViewById(R.id.listText);
            icon=(ImageView)itemView.findViewById(R.id.listIcon);
           itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
// method 1- to detect          context.startActivity(new Intent(context,SubActivity.class));

            if(clickListener!=null){
                clickListener.itemClicked(v,getPosition());
            }

        }
    }

    //method 2 to open activity via recyclerview
    public interface ClickListener{

        public void itemClicked(View view, int position);

    }

    public void setClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }


}
