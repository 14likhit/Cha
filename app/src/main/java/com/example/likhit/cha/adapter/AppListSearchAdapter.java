package com.example.likhit.cha.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.likhit.cha.R;
import com.example.likhit.cha.activity.AppListQuestion;
import com.example.likhit.cha.model.AppList;

import java.util.ArrayList;
import java.util.List;

public class AppListSearchAdapter extends RecyclerView.Adapter<AppListSearchAdapter.ViewHolder> implements Filterable{

    private Context context;
    private List<AppList> list;
    private List<AppList> filteredList;

    public AppListSearchAdapter(Context context, List<AppList> list) {
        this.context = context;
        this.list = list;
        filteredList=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView appName;
        public ImageView appLogo;

        public ViewHolder(View itemView){
            super(itemView);
            appName=itemView.findViewById(R.id.appName);
            appLogo=itemView.findViewById(R.id.appLogo);

            /*itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    appListAdapterListner.onAppSelected();
                }
            });*/
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(context).inflate(R.layout.app_list,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final AppList app=filteredList.get(position);
        holder.appName.setText(app.getAppName());
        String uri = "@drawable/"+app.getAppName()+app.getImageId();
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        //holder.appLogo.setImageResource(imageResource);
        Glide.with(context).load(imageResource).into(holder.appLogo);

        //final String appNa=app.getAppName();
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,AppListQuestion.class);
                intent.putExtra("appname",app.getAppName());
                intent.putExtra("appId",app.getImageId());
                context.startActivity(intent);
            }
        });

    }


    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString=constraint.toString();
                if (charString.isEmpty()){
                    filteredList=list;
                }
                else{
                    List<AppList> appFilteredList=new ArrayList<>();
                    for(AppList app : list){
                        if(app.getAppName().toLowerCase().contains(charString.toLowerCase())){
                            appFilteredList.add(app);
                        }
                    }
                    filteredList=appFilteredList;
                }
                FilterResults filterResults=new FilterResults();
                filterResults.values=filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList=(ArrayList<AppList>)results.values;
                notifyDataSetChanged();
            }
        };
    }
}
