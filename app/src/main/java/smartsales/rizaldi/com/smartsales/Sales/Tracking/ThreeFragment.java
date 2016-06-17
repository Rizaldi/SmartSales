package smartsales.rizaldi.com.smartsales.Sales.Tracking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import smartsales.rizaldi.com.smartsales.R;

/**
 * Created by spasi on 20/03/16.
 */
public class ThreeFragment extends Fragment {
    public ThreeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.item_fragment_propose, container, false);
    }
}
