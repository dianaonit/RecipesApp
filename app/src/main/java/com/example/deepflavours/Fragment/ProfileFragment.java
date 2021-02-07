package com.example.deepflavours.Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;


import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.deepflavours.FirebaseDatabaseHelper;
import com.example.deepflavours.Model.Recipe;
import com.example.deepflavours.R;
import com.example.deepflavours.RecyclerView_Config;

import java.util.List;


public class ProfileFragment extends Fragment {

    private RecyclerView mRecyclerView_MyPost;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile,container,false);



      mRecyclerView_MyPost = (RecyclerView) view.findViewById(R.id.foodpost_profile_RecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView_MyPost.setLayoutManager(linearLayoutManager);


         new FirebaseDatabaseHelper().readRecipes(new FirebaseDatabaseHelper.DataStatus() {
             @Override
             public void DataIsLoaded(List<Recipe> recipes, List<String> keys) {
                 new RecyclerView_Config().setConfig(mRecyclerView_MyPost,getContext(),recipes,keys);
             }

             @Override
             public void DataIsInserted() {

             }

             @Override
             public void DataIsUpdated() {

             }

             @Override
             public void DataIsDeleted() {

             }
         });



        return view;
    }
}