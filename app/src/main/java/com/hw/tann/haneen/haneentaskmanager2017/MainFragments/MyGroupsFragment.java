package com.hw.tann.haneen.haneentaskmanager2017.MainFragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hw.tann.haneen.haneentaskmanager2017.GroupActivity;
import com.hw.tann.haneen.haneentaskmanager2017.R;
import com.hw.tann.haneen.haneentaskmanager2017.data.DBUtils;
import com.hw.tann.haneen.haneentaskmanager2017.data.GroupAdapter;
import com.hw.tann.haneen.haneentaskmanager2017.data.MyGroup;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyGroupsFragment extends Fragment implements Titleable, AdapterView.OnItemClickListener{
    private ListView lstVGroups;
    private GroupAdapter groupAdapter;

    public MyGroupsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_groups, container, false);
        lstVGroups = (ListView) view.findViewById(R.id.lstGroups);
        lstVGroups.setOnItemClickListener(this);
        //// TODO: 16/08/2017 connect the list view to the data source by adapter
        groupAdapter = new GroupAdapter(getActivity(), R.layout.itm_groups);
        initListView();
        return view;
    }

    private void initListView() {


        String userEmail = DBUtils.auth.getCurrentUser().getEmail();
        DBUtils.myUsersRef.child(userEmail.replace('.','*')+"/gKey").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String,Object> groupKeys=(HashMap<String,Object>) dataSnapshot.getValue();
                groupAdapter.clear();
                if(groupKeys!=null){
                    for(String gr:groupKeys.keySet()){
                        DBUtils.myGroupsRef.child(gr).addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                MyGroup myGroup = dataSnapshot.getValue(MyGroup.class);
                                groupAdapter.add(myGroup);
                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        DBUtils.myGroupsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                groupAdapter.clear();
                lstVGroups.setAdapter(groupAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getTitle(){
        return "My Groups";
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(getActivity(),"Group Clicked: "+position,Toast.LENGTH_SHORT).show();
        MyGroup myGroup = (MyGroup) parent.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(), GroupActivity.class);
        intent.putExtra("gr", myGroup);
        startActivity(intent);
    }
}
