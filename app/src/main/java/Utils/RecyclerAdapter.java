package Utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.am.apxtract.DetailActivity;
import com.am.apxtract.R;
import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> implements Filterable {
    Context c;
    List<AppInfo> apps;
    List<AppInfo> appsFull;

    public RecyclerAdapter(Context c, List<AppInfo> apps){
        this.c = c;
        this.apps = apps;
        this.appsFull = new ArrayList(apps);
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(c);
        View view = layoutInflater.inflate(R.layout.listingview,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final AppInfo app = apps.get(position);
        Drawable imgData;
        try { imgData = c.getPackageManager().getApplicationIcon(app.packageName); }
        catch(PackageManager.NameNotFoundException e) {imgData = c.getResources().getDrawable(R.mipmap.ic_launcher_round);}
        holder.title.setText(app.title);
        holder.packName.setText(app.packageName);
        holder.img.setImageDrawable(imgData);
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(c, DetailActivity.class);
                intent.putExtra("appName",app.title);
                intent.putExtra("packageName",app.packageName);
                intent.putExtra("srcDir",app.srcdir);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return apps.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView title,packName;
        ImageView img;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            packName = itemView.findViewById(R.id.textDesc);
            img = itemView.findViewById(R.id.logo);
            mainLayout = itemView.findViewById(R.id.mainlayout);
        }
    }
    @Override
    public Filter getFilter() {
        return recyclerFilter;
    }
    Filter recyclerFilter = new Filter(){
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<AppInfo> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0)
                filteredList.addAll(appsFull);
            for(AppInfo info : appsFull){
                if(info.title.toLowerCase().contains(constraint) ) filteredList.add(info);
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            apps.clear();
            apps.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };
}
