package com.busglo.globus.domain.user;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.busglo.globus.R;
import com.busglo.globus.rest.IRewardGatewayApi;

import org.sufficientlysecure.htmltextview.HtmlTextView;

import java.util.List;

import io.reactivex.disposables.CompositeDisposable;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mData;
    private Dialog detailsDialog;
    private IRewardGatewayApi rewardGatewayApi;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private RequestOptions option;

    public UsersAdapter(Context mContext, List<User> mData) {
        this.mContext = mContext;
        this.mData = mData;
        option = new RequestOptions().timeout(12000).centerCrop().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.user_row, parent, false);
        final ViewHolder viewHolder = new ViewHolder(itemView);

        detailsDialog = new Dialog(mContext);
        detailsDialog.setContentView(R.layout.details_dialog);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        Glide.with(mContext).load(mData.get(i).getAvatar()).apply(option).into(viewHolder.avatar);
        viewHolder.name.setText(mData.get(i).getName());
        viewHolder.company.setText(mData.get(i).getCompany());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                displayUserDetailsDialog(i);
            }
        });
    }

    private void displayUserDetailsDialog(int i) {
        ImageView detailsAvatar = detailsDialog.findViewById(R.id.details_avatar);
        TextView detailsUserName = detailsDialog.findViewById(R.id.details_user_name);
        TextView detailsJobTitle = detailsDialog.findViewById(R.id.details_job_title);
        TextView detailsCompany = detailsDialog.findViewById(R.id.details_company);
        HtmlTextView detailsBiography = detailsDialog.findViewById(R.id.details_biography);
        Glide.with(mContext).load(mData.get(i).getAvatar()).apply(option).into(detailsAvatar);

        detailsUserName.setText(mData.get(i).getName());
        detailsJobTitle.setText(mData.get(i).getTitle());
        detailsCompany.setText(mData.get(i).getCompany());
        detailsBiography.setHtml(bioReformatted(mData.get(i).getBio()));
        detailsDialog.show();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public String bioReformatted(String bio) {
        String script = "<script type=\"text/javascript\">alert(1);</script>";
        return bio.replace(script, "");
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView avatar;
        TextView name, company;
        HtmlTextView biography;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.avatar);
            name = itemView.findViewById(R.id.name);
            company = itemView.findViewById(R.id.company);
        }
    }
}
