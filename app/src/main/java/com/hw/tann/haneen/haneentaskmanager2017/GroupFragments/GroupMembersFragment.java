package com.hw.tann.haneen.haneentaskmanager2017.GroupFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hw.tann.haneen.haneentaskmanager2017.R;
import com.hw.tann.haneen.haneentaskmanager2017.data.DBUtils;
import com.hw.tann.haneen.haneentaskmanager2017.data.MyGroup;
import com.hw.tann.haneen.haneentaskmanager2017.data.MyUsers;
import com.hw.tann.haneen.haneentaskmanager2017.data.UserAdapter;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupMembersFragment extends Fragment {

    private ListView lstVGroupMembers;
    private UserAdapter userAdapter;
    private MyGroup myGroup;

    public GroupMembersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_members, container, false);
        lstVGroupMembers = (ListView) view.findViewById(R.id.lstvGrpMembers);
        userAdapter = new UserAdapter(getActivity(),R.layout.itmuser,UserAdapter.GROUPMEMBERS);
        lstVGroupMembers.setAdapter(userAdapter);

        Intent i = getActivity().getIntent();
        if(getActivity().getIntent()!=null){
            if(i.getExtras()!=null){
                myGroup = (MyGroup) i.getExtras().get("gr");
            }
        }
        initListView();
        return view;
    }

    private void initListView() {
        if(myGroup!=null)
            userAdapter.clear();
            for (String userKey:myGroup.getUserKeys().keySet()){
                DBUtils.myUsersRef.child(userKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        MyUsers myUsers = dataSnapshot.getValue(MyUsers.class);
                        userAdapter.add(myUsers);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    }
