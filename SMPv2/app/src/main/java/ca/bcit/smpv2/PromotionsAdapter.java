package ca.bcit.smpv2;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import me.originqiu.library.EditTag;

public class PromotionsAdapter extends ArrayAdapter<Pair<Promotions, Boolean>> {

    public PromotionsAdapter (Context context, ArrayList<Pair<Promotions, Boolean>> promotions)
    {
        super(context, 0, promotions);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //get data for this position
        Pair<Promotions, Boolean> test = getItem(position);
        Promotions promotion = getItem(position).first;
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_promotions, parent, false);
        }
        // Lookup view for data population
        TextView promotionBusinessName = (TextView) convertView.findViewById(R.id.promotionBusinessName);
        TextView shortPromotionDetails = (TextView) convertView.findViewById(R.id.shortPromotionDetails);
        TextView promotionMinimumPoints = (TextView) convertView.findViewById(R.id.promotionMinimumPoints);

        ImageView promotionIcon = convertView.findViewById(R.id.iconImageView);
        ImageView promotionUploadImage = convertView.findViewById(R.id.promoImageView);
        // Populate the data into the template view using the data object
        if(getItem(position).second) {
            convertView.setBackgroundColor(getContext().getResources().getColor(R.color.green));
            ((TextView) convertView.findViewById(R.id.promotionBusinessName)).setTextColor(getContext().getResources().getColor(R.color.white));
            ((TextView) convertView.findViewById(R.id.promotionMinimumPoints)).setTextColor(getContext().getResources().getColor(R.color.white));
            ((TextView) convertView.findViewById(R.id.shortPromotionDetails)).setTextColor(getContext().getResources().getColor(R.color.white));
        }
        else
            convertView.setBackgroundColor(Color.TRANSPARENT);

        if(BusinessDashboard.business != null)
            ((TextView) convertView.findViewById(R.id.promotionClicks)).setText("Views: " + Integer.toString(promotion.getClicks()));
        else
            ((TextView) convertView.findViewById(R.id.promotionClicks)).setText("");

        promotionBusinessName.setText(promotion.getBusinessName());
        shortPromotionDetails.setText(promotion.getShortDescription());
        promotionMinimumPoints.setText(String.valueOf(promotion.getMinTier().getName()));
        try {
            Picasso.get().load("https://s3.amazonaws.com/superpoints-userfiles-mobilehub-467637819/promo/" + promotion.getPromotionID() + ".jpg")
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(promotionIcon);
            Picasso.get().load("https://s3.amazonaws.com/superpoints-userfiles-mobilehub-467637819/promo/" + promotion.getPromotionID() + ".jpg")
                    .memoryPolicy(MemoryPolicy.NO_CACHE, MemoryPolicy.NO_STORE).networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(promotionUploadImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Return the completed view to render on screen

        return convertView;
    }

}
