package co.sethspace.voyageralert;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pkmmte.view.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Anirudh on 3/17/2016.
 */
public class VoyagerHistoryAdapter extends RecyclerView.Adapter<VoyagerHistoryAdapter.ViewHolder> {

    private List<VoyagerHistory> vHistoryList;

    //Public Constructor
    public VoyagerHistoryAdapter(List<VoyagerHistory> voyagerHistories) {
        vHistoryList = voyagerHistories;
    }

    public  void addAllItems(List<VoyagerHistory> vHistoryList){
        this.vHistoryList = vHistoryList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        return new ViewHolder(layoutInflater.inflate(R.layout.item_voyager_threats, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(vHistoryList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return vHistoryList.size();
    }
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView vHistoryText;
        public CircularImageView vHistoryImage;
        public TextView vHistoryScore;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            vHistoryText = (TextView) itemView.findViewById(R.id.voyager_threat_text);
            vHistoryScore = (TextView) itemView.findViewById(R.id.voyager_threat_level);
            vHistoryImage = (CircularImageView) itemView.findViewById(R.id.voyager_threat_image);
        }

        public void bind(VoyagerHistory voyagerHistory, int position) {
            vHistoryText.setText(voyagerHistory.getvText());
            vHistoryScore.setText(Integer.toString(voyagerHistory.getvLevel()));
            // Load the History image
            Picasso.with(itemView.getContext())
                    .load("http://" + VoyagerServerConfig.DOMAIN_NAME + VoyagerServerConfig
                            .SERVER_IMAGE_PATH
                            + position + "." + VoyagerServerConfig.SERVER_IMAGE_EXT)
                    .placeholder(R.drawable.voyager_default_avatar)
                    .into(vHistoryImage);
        }


    }
}
