package smartsales.rizaldi.com.smartsales.Sales.Tracking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import smartsales.rizaldi.com.smartsales.R;

/**
 * Created by spasi on 15/03/2016.
 */
public class OneFragment extends Fragment {
    Button location;
    public OneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View da = inflater.inflate(R.layout.fragment_your_location, container, false);
        return da;
    }
}
