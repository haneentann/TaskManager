package com.hw.tann.haneen.haneentaskmanager2017.GroupFragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hw.tann.haneen.haneentaskmanager2017.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupMembersFragment extends Fragment {


    public GroupMembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_members, container, false);
    }

}
