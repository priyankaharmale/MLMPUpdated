package hnwebproject.com.mlmp.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import hnwebproject.com.mlmp.Activity.DashBoardActivity;
import hnwebproject.com.mlmp.Fragment.ContactDetailsFragment;
import hnwebproject.com.mlmp.Model.ContactModel;
import hnwebproject.com.mlmp.R;


/**
 * Created by hnwebmarketing on 1/29/2018.
 */

public class ContactAdapter  extends RecyclerView.Adapter<ContactAdapter.MyViewHolder> {

    private ArrayList<ContactModel> contactModels;
    Context context;
    Drawable drawable;
    ContactDetailsFragment fragment;


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_contact_name;
        public ImageView iv_pic;
        public ProgressBar progress_item;
        public RelativeLayout lay;

        public MyViewHolder(View view) {
            super(view);
            tv_contact_name = (TextView) view.findViewById(R.id.tv_contact_name);
            iv_pic = (ImageView) view.findViewById(R.id.iv_pic);
            progress_item = (ProgressBar) view.findViewById(R.id.progress_item);
            lay = (RelativeLayout) view.findViewById(R.id.lay);
            drawable = ContextCompat.getDrawable(context, R.drawable.sllider_menu_avtar);

        }
    }


    public ContactAdapter(ArrayList<ContactModel> contactModels, Context context) {
        this.contactModels = contactModels;
        this.context = context;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_contacts, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final ContactModel Contact = contactModels.get(position);
        holder.tv_contact_name.setText(Contact.getContact_name());


        try {
            Glide.with(context)
                    .load(Contact.getContact_image())
                    .error(drawable)
                    .centerCrop()
                    .crossFade()
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.progress_item.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.progress_item.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.iv_pic);
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }

        holder.lay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                fragment = new ContactDetailsFragment();
                replaceFragment(fragment);
            }
        });
    }


    @Override
    public int getItemCount() {
        return contactModels.size();
    }

    private void replaceFragment(Fragment fragment) {

        String backStateName = fragment.getClass().getName();
        FragmentManager manager = ((DashBoardActivity) context).getSupportFragmentManager();
        FragmentTransaction ft = manager.beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(backStateName);
        ft.commit();
    }
}
