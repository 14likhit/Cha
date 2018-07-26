package com.example.likhit.cha.activity;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.likhit.cha.MySingelton;
import com.example.likhit.cha.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;


public class Fragment_steps extends Fragment {

    private String appName;
    private int appId;
    private int questionId;
    private int page;
    private Context context;
    private LinearLayout layoutBottomsheet;
    private BottomSheetBehavior sheetBehavior;

    String json_url="http://192.168.0.101:5000/index/app/";

    public static Fragment_steps newInstance(int page,String appName,int appId,int questionId){
        Fragment_steps fragSteps=new Fragment_steps();
        Bundle args=new Bundle();
        args.putInt("page",page);
        args.putString("appName",appName);
        args.putInt("appId",appId);
        args.putInt("questionId",questionId);
        fragSteps.setArguments(args);
        return fragSteps;
    }

    public Fragment_steps() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page=getArguments().getInt("page");
        appName=getArguments().getString("appName");
        appId=getArguments().getInt("appId");
        questionId=getArguments().getInt("questionId");

        json_url=json_url+appName+"/"+questionId;
        context=getActivity().getApplicationContext();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_fragment_steps,container,false);

            //final TextView stepLabel=(TextView)view.findViewById(R.id.stepText);

            final ImageView stepImage=(ImageView)view.findViewById(R.id.stepimage);

        layoutBottomsheet=view.findViewById(R.id.bottom_sheet);
        sheetBehavior=BottomSheetBehavior.from(layoutBottomsheet);

        final TextView bottom_detail=(TextView)view.findViewById(R.id.bottomsheet_detail);
        final TextView bottom_title=(TextView)view.findViewById(R.id.bottomsheet_title);


        //stepLabel.setMovementMethod(new ScrollingMovementMethod());

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, json_url, (String) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {

                        int count = 0;
                        while (count < response.length()) {

                            try {
                                JSONObject jsonObject = response.getJSONObject(count);
                                switch (page) {
                                    case 1:
                                        if ((appName.equalsIgnoreCase("youtube"))) {
                                            stepImage.setImageResource(R.drawable.step1);
                                        }
//                                        stepLabel.setText(//stepLabel.getText() + "\n" +
//                                                (jsonObject.getString("step1")));
                                        bottom_title.setText("Step:1");
                                        bottom_detail.setText(jsonObject.getString("step1"));
                                        break;
                                    case 2:
                                        if ((appName.equalsIgnoreCase("youtube"))) {
                                            stepImage.setImageResource(R.drawable.step2);
                                        }
//                                        stepLabel.setText(//stepLabel.getText() + "\n" +
//                                                (jsonObject.getString("step2")));
                                        bottom_title.setText("Step:2");
                                        bottom_detail.setText(jsonObject.getString("step2"));
                                        break;
                                    case 3:
                                        if ((appName.equalsIgnoreCase("youtube"))) {
                                            stepImage.setImageResource(R.drawable.step3);
                                        }
//                                        stepLabel.setText(//stepLabel.getText() + "\n" +
//                                                (jsonObject.getString("step3")));
                                        bottom_title.setText("Step:3");
                                        bottom_detail.setText(jsonObject.getString("step3"));
                                        break;
                                    case 4:
                                        if ((appName.equalsIgnoreCase("youtube"))) {
                                            stepImage.setImageResource(R.drawable.step4);
                                        }
//                                        stepLabel.setText(//stepLabel.getText() + "\n" +
//                                                (jsonObject.getString("step4")));
                                        bottom_title.setText("Step:4");
                                        bottom_detail.setText(jsonObject.getString("step4"));
                                        break;
                                    case 5:
                                        if ((appName.equalsIgnoreCase("youtube"))) {
                                            stepImage.setImageResource(R.drawable.step5);
                                        }
//                                        stepLabel.setText(//stepLabel.getText() + "\n" +
//                                                (jsonObject.getString("step5")));
                                        bottom_title.setText("Step:5");
                                        bottom_detail.setText(jsonObject.getString("step5"));
                                        break;
                                    default:
//                                        stepLabel.setText(//stepLabel.getText() + "\n" +
//                                                (jsonObject.getString("step1")));
                                        bottom_title.setText("Step:1");
                                        bottom_detail.setText(jsonObject.getString("step1"));
                                        break;

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            count++;
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                stepLabel.setText(stepLabel.getText()+"\n"+"Error.....");
                bottom_title.setText("Error loading data. Please Try Again");
                Toast.makeText(context, "Error....", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }
        );

        //MySingelton.getInstance().addToRequestQueue(jsonArrayRequest);
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(jsonArrayRequest);

        //stepLabel.setMovementMethod(new ScrollingMovementMethod());

        //sheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

        stepImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sheetBehavior.getState()!=BottomSheetBehavior.STATE_EXPANDED){
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
                else{
                    sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        return view;
    }
}
