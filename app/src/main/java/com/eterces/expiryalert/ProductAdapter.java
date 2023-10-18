package com.eterces.expiryalert;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private Context context;
    private ArrayList<Product> productList;

    public ProductAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_layout, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Set data to views
        holder.textViewName.setText(product.getName());
        holder.textViewDate.setText(product.getDate());
        holder.textViewDaysRemaining.setText("Days Remaining: " + product.getDaysRemaining());
// Set text color based on the number of days remaining
        int daysRemaining = Integer.parseInt(product.getDaysRemaining());
        if (daysRemaining < 15) {
            holder.textViewDaysRemaining.setTextColor(context.getResources().getColor(R.color.red)); // Change to your desired color
        } else {
            holder.textViewDaysRemaining.setTextColor(context.getResources().getColor(R.color.black)); // Change to your default color
        }
        holder.category.setText(product.getCategory());

        // Load image using Glide
        Glide.with(context)
                .load(product.getImageUri())
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Update the data in the adapter
    public void updateData(ArrayList<Product> updatedList) {
        productList.clear();
        productList.addAll(updatedList);
        notifyDataSetChanged();
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textViewName, textViewDate, textViewDaysRemaining, category;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewDate = itemView.findViewById(R.id.textViewDate);
            textViewDaysRemaining = itemView.findViewById(R.id.textViewDaysRemaining);
            category = itemView.findViewById(R.id.category);
        }
    }

}
