package com.example.likhit.cha.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.example.likhit.cha.R;
import com.example.likhit.cha.adapter.NavigationDrawerAdapter;
import com.example.likhit.cha.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.List;

public class FragmentDrawer extends Fragment {
    private static String TAG=FragmentDrawer.class.getSimpleName();

    private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    private static String[] titles=null;
    private FragmentDrawerListener drawerListner;

    public FragmentDrawer() {
    }

    public void setDrawerListner(FragmentDrawerListener drawerListner) {
        this.drawerListner = drawerListner;
    }

    public static List<NavDrawerItem> getData(){
        List<NavDrawerItem> data=new ArrayList<>();

        //preparing navigation Drawer Item
        for(int i=0;i<titles.length;i++){
            NavDrawerItem navItem=new NavDrawerItem();
            navItem.setTitle(titles[i]);
            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setting drawer labels
        titles=getActivity().getResources().getStringArray(R.array.nav_drawer_labels);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //inflating view Layout
        View layout=inflater.inflate(R.layout.fragment_navigation_drawer,container,false);
        recyclerView=(RecyclerView)layout.findViewById(R.id.drawerlist);

        adapter=new NavigationDrawerAdapter(getActivity(),getData());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListner(getActivity(),recyclerView,new ClickListner(){
            @Override
            public void onClick(View view, int position) {
                drawerListner.onDrawerItemSelected(view,position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        return layout;
    }

    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar){
        containerView=getActivity().findViewById(fragmentId);
        mDrawerLayout=drawerLayout;
        mDrawerToggle=new ActionBarDrawerToggle(getActivity(),drawerLayout,toolbar,R.string.drawer_open,R.string.drawer_close){
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                toolbar.setAlpha(1-slideOffset/2);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getActivity().invalidateOptionsMenu();
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                getActivity().invalidateOptionsMenu();
            }
        };
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });
    }

    public static interface ClickListner{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }
    static class RecyclerTouchListner implements RecyclerView.OnItemTouchListener{
        private GestureDetector gestureDetector;
        private ClickListner clickListner;

        public RecyclerTouchListner(Context context,final RecyclerView recyclerView,final ClickListner clickListner ){

            this.clickListner=clickListner;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child != null && clickListner != null){
                        clickListner.onLongClick(child,recyclerView.getChildAdapterPosition(child));
                    }
                }
            });

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && gestureDetector.onTouchEvent(e)){
                clickListner.onClick(child,rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    public interface FragmentDrawerListener{
        public void onDrawerItemSelected(View view,int position);
    }
}
