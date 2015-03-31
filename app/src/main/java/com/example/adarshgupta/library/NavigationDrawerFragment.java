package com.example.adarshgupta.library;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;




/**
 * A simple {@link Fragment} subclass.
 */
//public class NavigationDrawerFragment extends Fragment {
    public class NavigationDrawerFragment extends Fragment implements RecycleAdapter.ClickListener {
    private RecyclerView recyclerView;

    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    public static final String KEY_USER_LEARNED_DRAWER = "user_learned_drawer";

    public static final String PREF_FILE_NAME = "testpref";

    private boolean mUserLearnedDrawer;
    private boolean mFromSavedInstanceState;
    private View containerView;
    private RecycleAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUserLearnedDrawer = Boolean.valueOf(readFromPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, "false"));

        if (savedInstanceState != null) {
            mFromSavedInstanceState = true;
        }
    }

    public NavigationDrawerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View layout = inflater.inflate(R.layout.fragment_navigation_drawer, container, false);
        recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new RecycleAdapter(getActivity(), getData());
        adapter.setClickListener(this);

//        DefaultItemAnimator animator=new DefaultItemAnimator();
//        animator.setAddDuration(1000);
//        animator.setRemoveDuration(1000);
//        recyclerView.setItemAnimator(animator);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.OnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
//            @Override
//            public void OnClick(View view, int position) {
//
//            }
//
//            @Override
//            public void OnLongClick(View view, int position) {
//
//            }
//        }));
        return layout;
    }

    public static List<Information> getData() {
        List<Information> data = new ArrayList<>();
        int[] icons = {R.drawable.book, R.drawable.book, R.drawable.book, R.drawable.book};
        String[] titles = {"E-BOOKS", "SCIENCE PAPERS", "JOURNALS", "MAGAZINES"};

        for (int i = 0; i < icons.length && i < titles.length; i++) {
            Information current = new Information();
            current.itemId = icons[i];
            current.title = titles[i];
            data.add(current);
        }
        return data;

    }


    public void setup(int fragmentId, DrawerLayout drawerlayout, final Toolbar toolbar) {

        containerView = getActivity().findViewById(fragmentId);

        mDrawerLayout = drawerlayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerlayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            //we have to ovveride the following methods


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!mUserLearnedDrawer) {
                    mUserLearnedDrawer = true;
                    saveToPreferences(getActivity(), KEY_USER_LEARNED_DRAWER, mUserLearnedDrawer + "");
                }

                getActivity().invalidateOptionsMenu();

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (slideOffset < 0.6) {
                    toolbar.setAlpha(1 - slideOffset);
                }
            }
        };

        if (!mUserLearnedDrawer && !mFromSavedInstanceState) {
            //when user hasn't opened the navigationdrawer
            mDrawerLayout.openDrawer(containerView);
        }

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });


    }

    public void saveToPreferences(Context context, String preferenceName, String preferenceValues) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(preferenceName, preferenceValues);
        editor.apply();
        //apply has been used instead of commit.apply doesnt tell the status of the task done
    }

    public static String readFromPreferences(Context context, String preferenceName, String defaultValue) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(PREF_FILE_NAME, context.MODE_PRIVATE);
        return sharedPreferences.getString(preferenceName, defaultValue);
    }

    //
////method 2 to detect clicks from fragment
    @Override
    public void itemClicked(View view, int position) {
        //add here what activity to open on what clicked
        if (position == 0) {
            startActivity(new Intent(getActivity(), Ebooks1Activity.class));
        }
        if (position == 1) {
            startActivity(new Intent(getActivity(), Research1Activity.class));
        }
        if (position == 2) {
            startActivity(new Intent(getActivity(), Journals1Activity.class));
        }
        if (position == 3) {
            startActivity(new Intent(getActivity(), Magazines1Activity.class));
        }
    }
}


