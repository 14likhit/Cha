package com.example.likhit.cha.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.likhit.cha.R;
import com.example.likhit.cha.activity.ActivitySteps;
import com.example.likhit.cha.model.AppListQuestions;

import java.util.List;

public class AppListQuestionAdapter extends RecyclerView.Adapter<AppListQuestionAdapter.ViewHolder> {

    private Context context;
    private List<AppListQuestions> listQuestions;
    private String appName;


    public AppListQuestionAdapter(Context context, List<AppListQuestions> listQuestions, String appName) {
        this.context = context;
        this.listQuestions = listQuestions;
        this.appName = appName;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView textQuestion;
        public ImageView imageApp;

        public ViewHolder(View itemView){
            super(itemView);
            textQuestion=itemView.findViewById(R.id.textQuestion);
            imageApp=itemView.findViewById(R.id.imageApp);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.question_app_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final AppListQuestions appListQuestion=listQuestions.get(position);
        holder.textQuestion.setText(appListQuestion.getQuestion());
        String uri = "@drawable/"+appName+appListQuestion.getAppId();
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        Glide.with(context).load(imageResource).apply(RequestOptions.circleCropTransform()).into(holder.imageApp);
        //holder.imageApp.setImageResource(imageResource);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,ActivitySteps.class);
                intent.putExtra("appName",appName);
                intent.putExtra("appId",appListQuestion.getAppId());
                intent.putExtra("questionId",appListQuestion.getQuestionId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listQuestions.size();
    }
}
