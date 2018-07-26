package com.example.likhit.cha.activity;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.SearchView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.likhit.cha.MySingelton;
import com.example.likhit.cha.R;
import com.example.likhit.cha.adapter.AppListAdapter;
import com.example.likhit.cha.adapter.AppListSearchAdapter;
import com.example.likhit.cha.model.AppList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView mList;
    private Toolbar toolbar;
    //private SwipeRefreshLayout swipeRefreshLayout;
    private SearchView searchView;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private DividerItemDecoration dividerItemDecoration;
    private List<AppList> appList;
    private AppListAdapter adapter;
    private AppListSearchAdapter searchAdapter;

    String json_url="http://192.168.0.101:5000/index/app";

    public HomeFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView=inflater.inflate(R.layout.fragment_home,container,false);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //swipeRefreshLayout=getActivity().findViewById(R.id.simpleSwipeRefreshLayout);
        //setHasOptionsMenu(true);
        toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar);

        initCollapsingToolbar();
        mList = getActivity().findViewById(R.id.main_list);

        appList = new ArrayList<>();
        adapter = new AppListAdapter(getActivity(),appList);


//        linearLayoutManager = new LinearLayoutManager(getActivity());
//        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        //dividerItemDecoration = new DividerItemDecoration(mList.getContext(), linearLayoutManager.getOrientation());

        gridLayoutManager=new GridLayoutManager(getActivity(),2);
        mList.addItemDecoration(new GridSpacingItemDecoration(2,dptopx(10),true));
        mList.setItemAnimator(new DefaultItemAnimator());

        mList.setHasFixedSize(true);
        //mList.setLayoutManager(linearLayoutManager);
        //mList.addItemDecoration(dividerItemDecoration);
        mList.setLayoutManager(gridLayoutManager);
        mList.setAdapter(adapter);
        getData();
        /*swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                swipeRefreshLayout.setRefreshing(false);
                getData();

            }
        });*/

    }

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbarLayout=getActivity().findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(" ");
        AppBarLayout appBarLayout=getActivity().findViewById(R.id.appbar);
        appBarLayout.setExpanded(true);

        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow=false;
            int scrollRange= -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if(scrollRange == -1){
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if(scrollRange + verticalOffset == 0){
                    collapsingToolbarLayout.setTitle(getString(R.string.app_name));
                    isShow=true;
                }
                else if(isShow){
                    collapsingToolbarLayout.setTitle(" ");
                    isShow=false;
                }

            }
        });

    }


    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration{

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount,int spacing,boolean includeEdge){
            this.spanCount=spanCount;
            this.spacing=spacing;
            this.includeEdge=includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position=parent.getChildAdapterPosition(view);
            int column= position%spanCount;

            if(includeEdge){
                outRect.left = spacing- column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if(position < spanCount){
                    outRect.top = spacing;
                }

                outRect.bottom = spacing;
            }
            else{
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if(position >= spanCount){
                    outRect.top = spacing;
                }
            }
        }
    }

    private int dptopx(int dp){
        Resources r=getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,r.getDisplayMetrics()));
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        SearchManager searchManager=(SearchManager)getActivity().getSystemService(Context.SEARCH_SERVICE);
        searchView=(SearchView)menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id=item.getItemId();

        if (id==R.id.action_settings){
            return false;
        }

        if (id==R.id.action_search){
            return  true;
        }

        //return super.onOptionsItemSelected(item);
        return false;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void getData(){

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST,json_url,(String)null,
                new Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(JSONArray response) {
                        for(int count=0;count<response.length();count++){
                            try {
                                JSONObject jsonObject=response.getJSONObject(count);
                                AppList app=new AppList();
                                app.setAppName(jsonObject.getString("appname"));
                                app.setImageId(jsonObject.getInt("appid"));
                                appList.add(app);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            adapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }

                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(),"Check Internet Connection",Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }
        );
        MySingelton.getInstance().addToRequestQueue(jsonArrayRequest);
//        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
//        requestQueue.add(jsonArrayRequest);

    }


}
