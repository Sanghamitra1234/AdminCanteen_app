package xyz.sleepygamers.admincanteen_app.editmenu;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import xyz.sleepygamers.admincanteen_app.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FoodMenuFragment extends Fragment {


    public FoodMenuFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_food_menu, container, false);
    }

}
