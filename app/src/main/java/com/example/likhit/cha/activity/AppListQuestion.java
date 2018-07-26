package com.example.likhit.cha.activity;

import android.app.ProgressDialog;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.likhit.cha.R;
import com.example.likhit.cha.adapter.AppListQuestionAdapter;
import com.example.likhit.cha.model.AppListQuestions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class AppListQuestion extends AppCompatActivity {

    private RecyclerView mRVlist;
    private LinearLayoutManager linearLayoutManager;
    private List<AppListQuestions> questionList;
    private AppListQuestionAdapter mAdapter;
    private Toolbar mToolbar;
    private String appName;
    private int appId;

    String json_url="http://192.168.0.101:5000/index/app/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list_question);
        mToolbar=(Toolbar)findViewById(R.id.toolbar1);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle appListActivityData=getIntent().getExtras();

        if(appListActivityData==null){
            return;
        }

        appName=appListActivityData.getString("appname");
        appId=appListActivityData.getInt("appId");
        //image=mainAppListActivityData.getInt("img");
        //appposition=mainAppListActivityData.getInt("appposition");
        json_url=json_url+appName;

        initCollapsingToolbar();

        getSupportActionBar().setTitle(" ");




        mRVlist=(RecyclerView)findViewById(R.id.questionRV);
        questionList=new ArrayList<>();
        mAdapter=new AppListQuestionAdapter(this,questionList,appName);

        linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        mRVlist.setHasFixedSize(true);
        mRVlist.setLayoutManager(linearLayoutManager);
        mRVlist.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        mRVlist.setAdapter(mAdapter);
        getQuestions();


    }

    private void initCollapsingToolbar(){
        final CollapsingToolbarLayout collapsingToolbar=(CollapsingToolbarLayout)findViewById(R.id.toolbar_layout);
        //collapsingToolbar.setTitle(" ");
        final ImageView expandedImage=(ImageView)findViewById(R.id.expandedImage);
        String uri = "@drawable/"+appName+appId;
        int imageResource = this.getResources().getIdentifier(uri, null, this.getPackageName());
        Glide.with(this).load(imageResource).into(expandedImage);
        AppBarLayout app_bar=(AppBarLayout)findViewById(R.id.app_bar);
        app_bar.setExpanded(true);
        app_bar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {

            boolean isShow=false;
            int scrollRange=-1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if(scrollRange==-1){
                    scrollRange=appBarLayout.getTotalScrollRange();
                }

                if(scrollRange+verticalOffset==0){
                    collapsingToolbar.setTitle(appName);
                    isShow=true;
                }else if (isShow){
                    collapsingToolbar.setTitle(" ");
                    isShow=false;
                }

            }
        });
    }

    private void getQuestions(){

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.POST,json_url,(String)null,
                new Response.Listener<JSONArray>(){

                    @Override
                    public void onResponse(JSONArray response) {
                        for(int count=0;count<response.length();count++){
                            try {
                                JSONObject jsonObject=response.getJSONObject(count);
                                AppListQuestions appQuestion=new AppListQuestions();
                                appQuestion.setQuestion(jsonObject.getString("question"));
                                appQuestion.setAppId(jsonObject.getInt("appid"));
                                appQuestion.setQuestionId(jsonObject.getInt("questionid"));
                                questionList.add(appQuestion);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            mAdapter.notifyDataSetChanged();
                            progressDialog.dismiss();
                        }

                    }
                },new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Check Internet Connection",Toast.LENGTH_SHORT).show();
                error.printStackTrace();

            }
        }
        );
        //MySingelton.getInstance().addToRequestQueue(jsonArrayRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
