package com.msbshamim60.wallshare.bottom_nav_ui.category;

import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.msbshamim60.wallshare.Adapter.CategoryAdapter;
import com.msbshamim60.wallshare.Adapter.RecyclerAdapter;
import com.msbshamim60.wallshare.R;
import com.msbshamim60.wallshare.click_interface.OnCategoryClick;
import com.msbshamim60.wallshare.dataModel.Category;
import com.msbshamim60.wallshare.dataModel.Post;

public class CategoryFragment extends Fragment implements OnCategoryClick {
    private static final String CATEGORY="CATEGORY";
    private FirebaseFirestore db;
    private CategoryAdapter adapter;
    private RecyclerView recyclerView;
    private FirestorePagingOptions<Category> options;

    public static CategoryFragment newInstance() {
        return new CategoryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.category_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView= view.findViewById(R.id.new_post_recyclerView);
        if(checkConnection()) {
            setupFirestore();
            setupRecyclerView();
        }
    }

    private boolean checkConnection() {
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }
    private void setupRecyclerView() {
        adapter=new CategoryAdapter(options,getContext(),this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    private void setupFirestore() {
        db=FirebaseFirestore.getInstance();
        CollectionReference postReference=db.collection("Category");
        Query query=postReference.orderBy("category",Query.Direction.ASCENDING);
        PagingConfig pagingConfig=new PagingConfig(10,2,false);

        options=new FirestorePagingOptions.Builder<Category>()
                .setLifecycleOwner(this)
                .setQuery(query,pagingConfig,Category.class)
                .build();

    }
    @Override
    public void onClickCategory(String category) {
        Bundle bundle = new Bundle();
        bundle.putString(CATEGORY, category);
        Navigation.findNavController(getView()).navigate(R.id.action_bottom_navigation_category_to_category_of_fragment,bundle);

    }
}