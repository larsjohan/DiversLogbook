package no.ntnu.diverslogbook.fragments;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import no.ntnu.diverslogbook.R;
import no.ntnu.diverslogbook.models.Diver;
import no.ntnu.diverslogbook.tasks.DownloadProfileImageTask;
import no.ntnu.diverslogbook.utils.Database;
import no.ntnu.diverslogbook.utils.ImageCache;


public class ProfileFragment extends Fragment {


    public ProfileFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Diver diver = Database.getLoggedInDiver();


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        if(diver == null) {
            Database.registerObserver(changedObject -> {
                if(changedObject instanceof Diver && ((Diver) changedObject).getId().equals(Database.getLoggedInDiver().getId())){
                    updateView(view, (Diver) changedObject);
                    view.invalidate();
                    return true; // Unregister observer if found
                }
                return false; // Keep the observer if not loaded yet
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

        Bitmap profileImg = ImageCache.get(this.getContext(), diver.getId());
        if(profileImg != null){
            image.setImageBitmap(profileImg);
        } else {
            new DownloadProfileImageTask(image, diver.getId()).execute(diver.getProfilePhotoURI());
        }

    }

}