package com.gallapillo.testtask.ui.adpaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gallapillo.testtask.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PermissionsAdapter extends RecyclerView.Adapter<PermissionsAdapter.ViewHolder> {

    private final ArrayList<String> permissions;

    public PermissionsAdapter(ArrayList<String> permissions) {
        this.permissions = permissions;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.permission_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        holder.tvPermission.setText(permissions.get(position));
    }

    @Override
    public int getItemCount() {
        return permissions.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPermission;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            tvPermission = itemView.findViewById(R.id.tv_permission);
        }
    }
}
