package com.msbshamim60.wallshare.Adapter;

import static android.graphics.Color.GREEN;

import android.graphics.PorterDuff;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.msbshamim60.wallshare.R;

import java.io.File;
import java.util.List;

public class OfflineRecyclerAdapter extends RecyclerView.Adapter<OfflineRecyclerAdapter.ViewHolder> {
    private static final String TAG = "TAG";
    private OnOfflineImageClick offlineImageClick;
    private View parentView;
    private List<File> imageList;

    public OfflineRecyclerAdapter(List<File> imageList,OnOfflineImageClick offlineImageClick) {
        this.imageList = imageList;
        this.offlineImageClick=offlineImageClick;
    }

    public void itemRemoved(int position){
        if(position!=-1 && position<imageList.size()){
            imageList.remove(position);
            this.notifyItemRemoved(position);
            this.notifyItemRangeChanged(position, imageList.size());
        }
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        parentView=parent;
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.offline_recycler_view, parent,false),this.offlineImageClick);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            showImageWithGlide(imageList.get(position),holder.imageView);
    }

    private void showImageWithGlide(File file, ImageView imageView) {
        Glide.with(parentView.getContext())
                .load(file)
                .placeholder(getCircularProgressBar())
                .error(R.drawable.ic_baseline_error_outline_24)
                .centerCrop()
                .into(imageView);
    }
    private CircularProgressDrawable getCircularProgressBar() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(parentView.getContext());
        circularProgressDrawable.setStrokeWidth(5f);
        circularProgressDrawable.setColorFilter(GREEN, PorterDuff.Mode.SRC_IN);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(100f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
    @Override
    public int getItemCount() {
        return (null != imageList ? imageList.size() : 0);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private OnOfflineImageClick offlineImageClick;
        private ImageView imageView;
        public ViewHolder(@NonNull View itemView,OnOfflineImageClick offlineImageClick) {
            super(itemView);
            this.offlineImageClick=offlineImageClick;
            itemView.setOnClickListener(this::onClick);
            imageView=itemView.findViewById(R.id.offline_image_view_recyclerView);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            offlineImageClick.onImageCLick(imageList.get(this.getBindingAdapterPosition()).getAbsolutePath());
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(Menu.NONE, 1, 1, "Delete").setOnMenuItemClickListener(onDelete);
        }
        private final MenuItem.OnMenuItemClickListener onDelete = item -> {
            switch (item.getItemId()) {
                case 1:
                    offlineImageClick.onImageLongCLick(getLayoutPosition());
                    break;
            }
            return true;
        };
    }

    public interface OnOfflineImageClick{
        void onImageCLick(String path);
        void onImageLongCLick(int position);
    }
}
