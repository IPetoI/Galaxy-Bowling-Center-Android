package com.example.galaxybowlingcentera.Adapterek;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.galaxybowlingcentera.Activityk.PalyafoglalasActivity;
import com.example.galaxybowlingcentera.AdatModellek.Idopontok;
import com.example.galaxybowlingcentera.R;

import java.util.ArrayList;

    public class IdopontokAdapter
            extends RecyclerView.Adapter<IdopontokAdapter.ViewHolder> {

        private ArrayList<Idopontok> arrayList;
        private boolean radioBChecked = false;
        private int selectedPosition = -1;

        public IdopontokAdapter(ArrayList<Idopontok> arrayList) {
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View view = layoutInflater.inflate(R.layout.idopontok_lista,parent,false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Idopontok currentItem = arrayList.get(position);

            holder.textView.setText(currentItem.getIdopontok());

            holder.radioButton.setChecked(position == selectedPosition);
        }

        @Override public int getItemCount() {
            return arrayList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            private RadioButton radioButton;
            private TextView textView;

            public ViewHolder(@NonNull View itemView)
            {
                super(itemView);

                radioButton = itemView.findViewById(R.id.idopontokRadioButton);
                textView = itemView.findViewById(R.id.melyikIdopont);

                radioButton.setOnClickListener(v -> handleRadiobuttonChecks(getAdapterPosition()));
            }
        }

        private void handleRadiobuttonChecks(int adapterPosition) {
            if (selectedPosition == adapterPosition) {
                return;
            } else {
                selectedPosition = adapterPosition;
            }
            radioBChecked = true;
            arrayList.get(selectedPosition).setSelected(false);
            arrayList.get(adapterPosition).setSelected(true);
            selectedPosition = adapterPosition;
            PalyafoglalasActivity.melyikOraban(arrayList.get(adapterPosition).getIdopontok());
            notifyDataSetChanged();
        }

        public void setKijelolesekKiszedese(boolean kijelolesekKiszedese) {
            if (kijelolesekKiszedese) {
                selectedPosition = -1;
            }
            notifyDataSetChanged();
        }
    }