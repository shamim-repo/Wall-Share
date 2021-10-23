package com.msbshamim60.wallshare.bottom_nav_ui.all;



import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.msbshamim60.wallshare.Adapter.ViewPagerAdapter;
import com.msbshamim60.wallshare.Adapter.ZoomOutPageTransformer;
import com.msbshamim60.wallshare.R;
import com.msbshamim60.wallshare.bottom_nav_ui.all.newTab.NewTabFragment;
import com.msbshamim60.wallshare.bottom_nav_ui.all.popularTab.PopularTabFragment;

import java.util.ArrayList;
import java.util.List;

public class AllFragment extends Fragment {
    private ViewPager2 viewPager;
    private List<Fragment> fragmentList;
    private List<String> tabTitleList;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.all_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TabLayout tabLayout=view.findViewById(R.id.tab_layout);
        viewPager=view.findViewById(R.id.view_pager_all);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        fragmentList=new ArrayList<>();
        fragmentList.add(NewTabFragment.newInstance());
        fragmentList.add(PopularTabFragment.newInstance());
        tabTitleList=new ArrayList<>();
        tabTitleList.add(getString(R.string.new_title));
        tabTitleList.add(getString(R.string.popular_title));

        viewPager.setAdapter(new ViewPagerAdapter(getChildFragmentManager(),getLifecycle(), fragmentList));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitleList.get(position))
        ).attach();
    }

}