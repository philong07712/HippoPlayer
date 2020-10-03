package com.example.hippoplayer.search;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.OvershootInterpolator;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hippoplayer.R;

import java.util.ArrayList;

public class SearchTitleAdapter extends RecyclerView.Adapter<SearchTitleAdapter.ViewHolder> {
    private ArrayList<String> arrayList = new ArrayList<>();
    private SearchTiltleListener searchTiltleListener;
    private int index;

    public void setIndex(int index) {
        this.index = index;
        notifyDataSetChanged();
    }

    public SearchTitleAdapter(SearchTiltleListener searchTiltleListener) {
        this.searchTiltleListener = searchTiltleListener;
    }

    public void setArrayList(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
        notifyDataSetChanged();
    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(1000);
        anim.setInterpolator(new OvershootInterpolator());
        view.startAnimation(anim);
    }

    @NonNull
    @Override
    public SearchTitleAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_title_search, parent, false);
        return new ViewHolder(view, searchTiltleListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchTitleAdapter.ViewHolder holder, int position) {
        holder.textView.setText(arrayList.get(position));
        setFadeAnimation(holder.itemView);
        setChangeStatusItemClicked(index, position, holder.textView, holder.view);
    }

    private void setChangeStatusItemClicked(int index, int position, TextView textView, View dotView){
        if(index == position){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                textView.animate().setDuration(200).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        textView.setTextColor(Color.parseColor("#000000"));
                    }
                }).setInterpolator(new OvershootInterpolator()).start();
            }
            dotView.animate().scaleX(1f).scaleY(1f).setInterpolator(new OvershootInterpolator()).start();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                textView.animate().setDuration(200).setUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(ValueAnimator animation) {
                        textView.setTextColor(Color.parseColor("#DAD9D9"));
                    }
                }).setInterpolator(new OvershootInterpolator()).start();
            }
            dotView.animate().scaleX(0f).scaleY(0f).setInterpolator(new OvershootInterpolator()).start();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView textView;
        private View view;
        SearchTiltleListener searchTiltleListener;
        public ViewHolder(@NonNull View itemView, SearchTiltleListener searchTiltleListener) {
            super(itemView);
            textView = itemView.findViewById(R.id.text_title_item_search);
            view = itemView.findViewById(R.id.view_dot_selected_search);
            this.searchTiltleListener = searchTiltleListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            searchTiltleListener.searchTitleClicked(getAdapterPosition());
        }
    }

    public interface SearchTiltleListener{
        void searchTitleClicked(int position);
    }


}
