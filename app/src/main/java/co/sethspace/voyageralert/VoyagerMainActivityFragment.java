package co.sethspace.voyageralert;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A placeholder fragment containing a simple view.
 */
public class VoyagerMainActivityFragment extends Fragment {

    public VoyagerMainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_voyager_main, container, false);

        //Set the toolbar
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.voyager_toolbar);
        toolbar.setTitle(R.string.app_name);
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);

        return v;
    }


}
