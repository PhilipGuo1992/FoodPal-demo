package com.example.phili.foodpaldemo.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;


import com.example.phili.foodpaldemo.DisplayRestaurantsActivity;
import com.example.phili.foodpaldemo.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class RestaurantsFragment extends android.support.v4.app.Fragment {

    public final static String MESSAGE_CITY = "CITY";
    public final static String MESSAGE_CUISINE = "CUISINE";

    private Button btn_discover_restaurant;
    private Spinner spinner_city;
    private EditText txt_search_for_restaurants;

    public RestaurantsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View restView = inflater.inflate(R.layout.fragment_restaurants, container, false);
        btn_discover_restaurant = restView.findViewById(R.id.btn_discover_restaurant);
        spinner_city = restView.findViewById(R.id.spinner_city);
        txt_search_for_restaurants = restView.findViewById(R.id.txt_search_for_restaurants);

        ArrayAdapter<CharSequence> cityArrays = ArrayAdapter.createFromResource(getContext(),
                R.array.cities_array, R.layout.spinner_item);
        cityArrays.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner_city.setAdapter(cityArrays);

        btn_discover_restaurant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),
                        DisplayRestaurantsActivity.class);
                intent.putExtra(MESSAGE_CITY, spinner_city.getSelectedItem().toString());
                intent.putExtra(MESSAGE_CUISINE, txt_search_for_restaurants.getText().toString());
                startActivity(intent);
            }
        });
        return restView;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
