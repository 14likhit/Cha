package com.example.likhit.cha.adapter;

import android.content.Context;
import android.content.Intent;
import android.location.SettingInjectorService;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.likhit.cha.R;
import com.example.likhit.cha.activity.AppListQuestion;
import com.example.likhit.cha.activity.HomeActivity;
import com.example.likhit.cha.model.AppList;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends RecyclerView.Adapter<AppListAdapter.ViewHolder> implements Filterable{

    private Context context;
    private List<AppList> list;
    private List<AppList> filteredList;
    private AppListSearchAdapter asa;

    public AppListAdapter(Context context, List<AppList> list) {
        this.context = context;
        this.list = list;
        filteredList=list;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public TextView appName,appId;
        public ImageView appLogo,thumbnail;

        public ViewHolder(View itemView){
            super(itemView);
            appName=itemView.findViewById(R.id.tv_android);
            appId=itemView.findViewById(R.id.appId);
            appLogo=itemView.findViewById(R.id.img_android);
            thumbnail=itemView.findViewById(R.id.thumbnail);

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
        View v=LayoutInflater.from(context).inflate(R.layout.app_list_card_view,parent,false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        final AppList app=filteredList.get(position);
        holder.appName.setText(app.getAppName());
        holder.appId.setText("3.5");
        String uri = "@drawable/"+app.getAppName()+app.getImageId();
        int imageResource = context.getResources().getIdentifier(uri, null, context.getPackageName());
        //holder.appLogo.setImageResource(imageResource);
        Glide.with(context).load(imageResource).into(holder.appLogo);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMenu(holder.thumbnail);
            }
        });

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

    private void showPopupMenu(View view){
        PopupMenu popupMenu=new PopupMenu(context,view);
        MenuInflater inflater=popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_app_item,popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListner());
        popupMenu.show();
    }

    public class MyMenuItemClickListner implements PopupMenu.OnMenuItemClickListener{
        public MyMenuItemClickListner(){}

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_add_favourite:
                    Toast.makeText(context, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_info:
                    Toast.makeText(context, "Application Information", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
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
