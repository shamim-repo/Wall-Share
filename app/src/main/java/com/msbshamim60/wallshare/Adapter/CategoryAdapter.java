package com.msbshamim60.wallshare.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.msbshamim60.wallshare.R;
import com.msbshamim60.wallshare.click_interface.OnCategoryClick;
import com.msbshamim60.wallshare.click_interface.OnClickHandler;
import com.msbshamim60.wallshare.click_interface.OnclickHandlerInt;
import com.msbshamim60.wallshare.dataModel.Category;
import com.msbshamim60.wallshare.dataModel.Post;

public class CategoryAdapter extends FirestorePagingAdapter<Category,CategoryAdapter.ViewHolder> implements OnclickHandlerInt {

    private static final String TAG = "TAG";
    private Context context;
    private OnCategoryClick onCategoryClick;
    public CategoryAdapter(@NonNull FirestorePagingOptions<Category> options,Context context,OnCategoryClick onCategoryClick) {
        super(options);
        this.context=context;
        this.onCategoryClick =onCategoryClick;
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Category model) {
        holder.categoryTitle.setText(model.getCategory());
        if(model.getUrl() != null ){
            showImageWithGlide(model.getUrl(),holder.imageView);
        }
    }
    private void showImageWithGlide(String photoUrl, ImageView imageView) {
        Glide.with(context)
                .load(photoUrl)
                .centerCrop()
                .placeholder(getCircularProgressBar())
                .error(R.drawable.ic_baseline_error_outline_24)
                .into(imageView);
    }
    private CircularProgressDrawable getCircularProgressBar() {
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(context);
        circularProgressDrawable.setStrokeWidth(10f);
        circularProgressDrawable.setCenterRadius(60f);
        circularProgressDrawable.start();
        return circularProgressDrawable;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_recycler_view, parent,false),this );
    }

    @Override
    public void onClickRecyclerItem(int postNum) {
        onCategoryClick.onClickCategory(getItem(postNum).get("category").toString());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView categoryTitle;
        ImageView imageView;
        private OnclickHandlerInt onclickHandlerInt;
        public ViewHolder(@NonNull View itemView,OnclickHandlerInt onclickHandlerInt) {
            super(itemView);
            this.onclickHandlerInt = onclickHandlerInt;
            itemView.setOnClickListener(this);
            categoryTitle=itemView.findViewById(R.id.category_name_textView);
            imageView=itemView.findViewById(R.id.image_view_category_recyclerView);
        }

        @Override
        public void onClick(View v) {
            onclickHandlerInt.onClickRecyclerItem(getAbsoluteAdapterPosition());
        }
    }
}
