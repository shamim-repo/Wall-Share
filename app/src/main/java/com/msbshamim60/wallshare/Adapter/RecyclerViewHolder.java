package com.msbshamim60.wallshare.Adapter;

import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.msbshamim60.wallshare.R;
import com.msbshamim60.wallshare.click_interface.OnClickHandler;
import com.msbshamim60.wallshare.click_interface.OnMenuItemClickListenerDelete;
import com.msbshamim60.wallshare.click_interface.OnMenuItemClickListenerDeleteINT;
import com.msbshamim60.wallshare.click_interface.OnclickHandlerInt;

public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
    TextView userNameTextView;
    TextView downloadTextView;
    TextView sizeTextView;
    ImageView profileImageView;
    ImageView imageView;
    private OnclickHandlerInt onclickHandlerInt;
    private OnMenuItemClickListenerDeleteINT onMenuItemClickListenerDeleteINT;
    public RecyclerViewHolder(@NonNull View itemView, OnclickHandlerInt onclickHandlerInt) {
        super(itemView);
        this.onclickHandlerInt = onclickHandlerInt;
        itemView.setOnClickListener(this);
        userNameTextView=itemView.findViewById(R.id.user_name_textView_recyclerView);
        downloadTextView=itemView.findViewById(R.id.download_count_recyclerView_recyclerview);
        sizeTextView=itemView.findViewById(R.id.size_textView_recyclerView);
        profileImageView=itemView.findViewById(R.id.profile_imageView_recyclerView);
        imageView=itemView.findViewById(R.id.image_view_recyclerView);

    }

    public RecyclerViewHolder(@NonNull View itemView, OnclickHandlerInt onclickHandlerInt, OnMenuItemClickListenerDeleteINT onMenuItemClickListenerDeleteINT) {
        super(itemView);
        this.onclickHandlerInt = onclickHandlerInt;
        this.onMenuItemClickListenerDeleteINT = onMenuItemClickListenerDeleteINT;
        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
        userNameTextView=itemView.findViewById(R.id.user_name_textView_recyclerView);
        downloadTextView=itemView.findViewById(R.id.download_count_recyclerView_recyclerview);
        sizeTextView=itemView.findViewById(R.id.size_textView_recyclerView);
        profileImageView=itemView.findViewById(R.id.profile_imageView_recyclerView);
        imageView=itemView.findViewById(R.id.image_view_recyclerView);

    }

    @Override
    public void onClick(View view) {
        if (onclickHandlerInt != null) onclickHandlerInt.onClickRecyclerItem(getAbsoluteAdapterPosition());
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(Menu.NONE, 1, 1, "Delete").setOnMenuItemClickListener(onDelete);
    }
    private final MenuItem.OnMenuItemClickListener onDelete = item -> {
        switch (item.getItemId()) {
            case 1:
                onMenuItemClickListenerDeleteINT.onDeleteSelected(getAbsoluteAdapterPosition());
                break;
        }
        return true;
    };
}
