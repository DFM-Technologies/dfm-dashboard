package za.co.dfmsoftware.utility.utils;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.ref.WeakReference;
import java.util.List;

import io.reactivex.subjects.PublishSubject;
import za.co.dfmsoftware.utility.R;
import za.co.dfmsoftware.utility.model.model.Probe;

/**
 * PROBE LIST ADAPTER
 * This will be the recycler view for the probe list
 * This list will hold the probe number and the status of the probe.
 */
public class ProbeListAdapter extends RecyclerView.Adapter<ProbeListAdapter.ViewHolder>{

    private List<Probe> probeList;
    private WeakReference<Context> weakContext;
    private PublishSubject<Probe> onItemClickedSubject = PublishSubject.create();

    public ProbeListAdapter(@NonNull Context context) {
        this.weakContext = new WeakReference<>(context);
    }

    @NonNull
    @Override
    public ProbeListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_probe_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Probe probe = this.probeList.get(position);

        holder.probeTitle.setText(probe.getProbe());
        holder.probeIdTitle.setText(String.valueOf(probe.getProbeId()));
        holder.mView.setColor(this.getProbeStatusColor(probe.getProbeStatus()));
        holder.itemView.setTag(probe);
        holder.itemView.setOnClickListener(view -> onItemClickedSubject.onNext((Probe) view.getTag()));
    }

    @Override
    public int getItemCount() {
        return (this.probeList != null) ? this.probeList.size() : 0;
    }

    public void clearAndApply(@NonNull List<Probe> probesList) {
        if(this.probeList != null) {
            this.probeList.clear();
        }

        this.probeList = probesList;
        this.notifyDataSetChanged();
    }

    private int getProbeStatusColor(int probeStatus) {
        Resources resources = this.weakContext.get().getResources();
        TypedArray colors = resources.obtainTypedArray(R.array.probeStatusColor);

        int colorTransparent = resources.getColor(R.color.transparent);

        if(probeStatus >= colors.length()){
            return colorTransparent;
        }

        int statusColor = colors.getColor(probeStatus, colorTransparent);

        colors.recycle();

        return statusColor;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView probeTitle, probeIdTitle;
        public ProbeStatusIndicatorView mView;

        public ViewHolder(View view){
            super(view);
            probeTitle = view.findViewById(R.id.block_name_textview);
            probeIdTitle = view.findViewById(R.id.probe_id_textview);
            mView = view.findViewById(R.id.status_color);
        }
    }

    public PublishSubject<Probe> getOnItemClickedSubject() {
        return onItemClickedSubject;
    }
}
