package com.example.hackathon_ai_v1;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder>
{
    ArrayList<ViewPagerItem> viewPagerItems;
    public VPAdapter(ArrayList<ViewPagerItem> viewPagerItems)
    {
        this.viewPagerItems = viewPagerItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewpager_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        ViewPagerItem viewPagerItem = viewPagerItems.get(position);
        holder.imageButton.setImageResource(viewPagerItem.imageID);
        holder.tvDescription.setText(viewPagerItem.description);
    }

    @Override
    public int getItemCount()
    {
        return viewPagerItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        ImageButton imageButton;
        TextView tvDescription;
        public ViewHolder(@NonNull View itemView)
        {
            super(itemView);
            imageButton = itemView.findViewById(R.id.imageButton);
            tvDescription = itemView.findViewById(R.id.description);
        }
    }
}
