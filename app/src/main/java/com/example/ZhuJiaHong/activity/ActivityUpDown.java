package com.example.ZhuJiaHong.activity;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mdbs.basechart.view.updown.UpDownActivityView;
import com.mdbs.starwave_meta.params.RFSymbolName;

public class ActivityUpDown extends com.mdbs.basechart.activity.ActivityUpDown {

    private UpDownActivityView.UpDownViewHolderHelper helper = new UpDownActivityView.UpDownViewHolderHelper() {
        @Override
        public RecyclerView.ViewHolder createViewHolder() {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, RFSymbolName rfSymbolName, int i) {

        }
    };

    @Override
    protected UpDownActivityView.UpDownViewHolderHelper createHelper(){ return helper; }

    public class AppViewHolder extends RecyclerView.ViewHolder{

        public AppViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
