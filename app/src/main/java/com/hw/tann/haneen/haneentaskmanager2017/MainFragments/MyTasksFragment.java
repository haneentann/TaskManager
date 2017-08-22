package com.hw.tann.haneen.haneentaskmanager2017.MainFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.hw.tann.haneen.haneentaskmanager2017.R;
import com.hw.tann.haneen.haneentaskmanager2017.data.DBUtils;
import com.hw.tann.haneen.haneentaskmanager2017.data.MyTasks;
import com.hw.tann.haneen.haneentaskmanager2017.data.TasksAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTasksFragment extends Fragment implements Titleable{
    private ListView lstMyTasks;
    private TasksAdapter taskAdapter;
    public MyTasksFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_tasks, container, false);
        lstMyTasks = (ListView) view.findViewById(R.id.lstTasks);
        //// TODO: 16/08/2017 connect the list view to the data source by adapter
        initListView();
        return view;
    }

    private void initListView() {
        if (taskAdapter == null)
            taskAdapter = new TasksAdapter(getActivity(), R.layout.itm_task);
        taskAdapter.clear();
        String userEmail = DBUtils.auth.getCurrentUser().getEmail();
        DBUtils.myTasksRef.orderByChild("uKey").equalTo(userEmail).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskAdapter.clear();
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    MyTasks myTasks = ds.getValue(MyTasks.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        //// TODO: 17/08/2017 get the data source and set the adapter

        Log.d("DD", "KAN !!");

        //add listener to a single value chage ONLY:
        //DBUtils.myTasksRef.addListenerForSingleValueEvent(new ValueEventListener() {

        //add listener to update screen for !! every change !! in the Firebase dbase
        DBUtils.myTasksRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                taskAdapter.clear();

//                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
//                    DataSnapshot ds = dataSnapshot.getChildren().iterator().next();
//                    MyTasks myTasks = ds.getValue(MyTasks.class);
//                    taskAdapter.add(myTasks);
//                }
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    MyTasks myTasks = ds.getValue(MyTasks.class);
                    taskAdapter.add(myTasks);
                }
                lstMyTasks.setAdapter(taskAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        lstMyTasks.setAdapter(taskAdapter);
    }

    public String getTitle(){
        return "My Tasks";
    }
}
