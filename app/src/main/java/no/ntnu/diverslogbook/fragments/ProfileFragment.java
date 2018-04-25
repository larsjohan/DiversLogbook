package no.ntnu.diverslogbook.fragments;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.jetbrains.annotations.Nullable;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.model.Diver;
import no.ntnu.diverslogbook.task.DownloadProfileImageTask;
import no.ntnu.diverslogbook.util.Database;
import no.ntnu.diverslogbook.util.Observer;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.d("DiverApp", "Creating profile");
        Diver diver = Database.getLoggedInDiver();


        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        if(diver == null) {
            Database.registerObserver(changedObject -> {
                if(changedObject instanceof Diver && ((Diver) changedObject).getId().equals(Database.getLoggedInDiverGuid())){
                    updateView(view, (Diver) changedObject);
                    view.invalidate();
                }
            });
        } else {
            updateView(view, diver);
        }


        return view;
    }

    private void updateView(View view, Diver diver){

        ImageView image = view.findViewById(R.id.iv_profile_image);
        TextView name = view.findViewById(R.id.tv_profile_name);
        TextView email = view.findViewById(R.id.tv_profile_email);
        TextView phone = view.findViewById(R.id.tv_profile_phone);

        image.setImageDrawable(getActivity().getDrawable(R.drawable.ic_image_placeholder_50dp));
        name.setText(diver.getName());
        email.setText(diver.getEmail());
        phone.setText(diver.getPhone());

        new DownloadProfileImageTask(this.getActivity()).execute(diver.getProfilePhotoURI());

    }

}