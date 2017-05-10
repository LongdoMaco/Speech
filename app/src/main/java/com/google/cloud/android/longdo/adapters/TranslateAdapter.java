package com.google.cloud.android.longdo.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.cloud.android.longdo.R;
import com.google.cloud.android.longdo.helps.ImageConverter;
import com.google.cloud.android.longdo.models.Translate;

import java.util.Collections;
import java.util.List;

import static com.google.cloud.android.longdo.R.id.flagFrom;
import static com.google.cloud.android.longdo.R.id.flagTo;


/**
 * Created by longdo on 18/04/2017.
 */

public class TranslateAdapter extends RecyclerView.Adapter<TranslateAdapter.View_Holder> {

    private List<Translate> translateList = Collections.emptyList();
    Context context;

    public TranslateAdapter(List<Translate> translateList, Context context) {
        this.translateList = translateList;
        this.context = context;
    }

    @Override
    public View_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycleview_item, parent, false);
        View_Holder holder = new View_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(View_Holder holder, int position) {
        holder.title.setText(translateList.get(position).getText_from());
        holder.description.setText(translateList.get(position).getText_to());

        if(translateList.get(position).getFlag_from()!=null && !"".equals(translateList.get(position).getFlag_from()))
            {
                Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),context.getResources().getIdentifier(translateList.get(position).getFlag_from(), "drawable", context.getPackageName()));
                Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 80);
                holder.flag_from.setImageBitmap(circularBitmap);
            }
        else {
            holder.flag_from.setImageResource(R.drawable.vn);
        }
        if(translateList.get(position).getFlag_to()!=null && !"".equals(translateList.get(position).getFlag_to()))
        {
            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),context.getResources().getIdentifier(translateList.get(position).getFlag_to(), "drawable", context.getPackageName()));
            Bitmap circularBitmap = ImageConverter.getRoundedCornerBitmap(bitmap, 80);
            holder.flag_to.setImageBitmap(circularBitmap);
        }
        else {
            holder.flag_to.setImageResource(R.drawable.vn);
        }

        if(position%2==0){
            holder.relativeLayout.setBackgroundResource(R.drawable.background_blue);
        }else{
            holder.relativeLayout.setBackgroundResource(R.drawable.background_grey);

        }
        holder.language_from.setText(translateList.get(position).getLanguage_from());
        holder.language_to.setText(translateList.get(position).getLanguage_to());

    }

    @Override
    public int getItemCount() {
        if (translateList!=null){
            return translateList.size();
        }
        else return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    // Insert a new item to the RecyclerView on a predefined position
    public void insert(int position, Translate data) {
        translateList.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(Translate data) {
        int position = translateList.indexOf(data);
        translateList.remove(position);
        notifyItemRemoved(position);
    }
    /**
     * View holder class
     * */
    public class View_Holder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView title;
        TextView language_from,language_to;
        TextView description;
        ImageView flag_from,flag_to;
        LinearLayout relativeLayout;

        View_Holder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.title);
            language_from = (TextView) itemView.findViewById(R.id.languagefrom);
            language_to = (TextView) itemView.findViewById(R.id.languageto);
            description = (TextView) itemView.findViewById(R.id.description);
            flag_from = (ImageView) itemView.findViewById(flagFrom);
            flag_to = (ImageView) itemView.findViewById(flagTo);
            relativeLayout=(LinearLayout) itemView.findViewById(R.id.background);
        }
    }
}
