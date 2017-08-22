package com.hw.tann.haneen.haneentaskmanager2017;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hw.tann.haneen.haneentaskmanager2017.data.DBUtils;
import com.hw.tann.haneen.haneentaskmanager2017.data.MyGroup;
import com.hw.tann.haneen.haneentaskmanager2017.data.MyUsers;
import com.hw.tann.haneen.haneentaskmanager2017.data.UserAdapter;

import java.util.ArrayList;

public class AddGroupActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText etGroupName, etEmail, etUserName;
    private ImageButton ibtnSearch, ibtnAddMember, ibtnSaveGroup;
    private ListView lstvMembersSearchResuult, lstvSelectedMembers;

    private UserAdapter userAdapterSearchResult, userAdapterSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);

        etGroupName = (EditText) findViewById(R.id.etGroupName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUserName = (EditText) findViewById(R.id.etName);

        ibtnSearch = (ImageButton) findViewById(R.id.ibtnSearch);
        ibtnAddMember = (ImageButton) findViewById(R.id.ibtnAddMember);
        ibtnSaveGroup = (ImageButton) findViewById(R.id.ibtnSaveGroup);

        lstvMembersSearchResuult = (ListView) findViewById(R.id.lstvMemberSearchResults);
        lstvSelectedMembers = (ListView) findViewById(R.id.lstvSelectedMembers);

        userAdapterSearchResult = new UserAdapter(this, R.layout.itmuser,UserAdapter.SEARCH_RESULT_MEMBERS);
        userAdapterSelect = new UserAdapter(this,R.layout.itmuser,UserAdapter.SELECTED_MEMBERS);

        lstvMembersSearchResuult.setAdapter(userAdapterSearchResult);
        lstvSelectedMembers.setAdapter(userAdapterSelect);

        exentHandler();
    }

    @Override
    public void onClick(View v) {

        if(v==ibtnSearch){
            search();
        }
        if(v==ibtnAddMember){
            addSelectedUsers();
        }
        if(v==ibtnSaveGroup){
            saveGroup();
        }
    }

    private void addSelectedUsers() {
        ArrayList<MyUsers> selectedUsers = userAdapterSearchResult.getSelectedUsers();
        // 2 options for adding all users
        //1
        userAdapterSelect.addAll(selectedUsers);
      //  userAdapterSearchResult.clearSelectedUsers();
        //2
        /*for(MyUsers user:selectedUsers){
            userAdapterSelect.add(user);
        }*/
    }

    public void exentHandler(){
        ibtnSaveGroup.setOnClickListener(this);
        ibtnAddMember.setOnClickListener(this);
        ibtnSearch.setOnClickListener(this);
    }
    public void search(){
        String stEmail = etEmail.getText().toString();
        String stName = etUserName.getText().toString();

        final ProgressDialog progressDialog = ProgressDialog.show(this,"Wait","Searching");
        if(stEmail.length()>0){
            DBUtils.myUsersRef.child(stEmail.replace('.','*'))
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    progressDialog.dismiss();
                    if(dataSnapshot.exists()){
                        MyUsers myUser = dataSnapshot.getValue(MyUsers.class);
                        userAdapterSearchResult.add(myUser);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        if(stName.length()>0){
            Query query = DBUtils.myUsersRef.orderByChild("name").equalTo(stName);
            if(progressDialog.isShowing() == false )progressDialog.show();
            DBUtils.myUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(progressDialog.isShowing())progressDialog.dismiss();
                            for(DataSnapshot ds:dataSnapshot.getChildren()){
                                MyUsers myUser = ds.getValue(MyUsers.class);
                                userAdapterSearchResult.add(myUser);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
        }
    }
    private void updateUsers(String gKey){
        for(int i=0; i<userAdapterSelect.getCount();i++){
            MyUsers myUsers = userAdapterSelect.getItem(i);
            DBUtils.myUsersRef.child(myUsers.getuKey_email().replace('.','*')).child("groupsKeys").child(gKey).setValue(true);
        }
    }
    private void saveGroup(){
        String stGroupName = etGroupName.getText().toString();
        if(stGroupName.length()<2){
            MyGroup myGroup = new MyGroup();
            myGroup.setName(stGroupName);
            String myEmail = DBUtils.auth.getCurrentUser().getEmail();
            myGroup.setMngrUkey(myEmail.replace('.','*'));
            final String gKey = DBUtils.myGroupsRef.push().getKey();
            myGroup.setgKey(gKey);

            for(int i=0;i<userAdapterSearchResult.getCount();i++)
            {
                MyUsers myUsers = userAdapterSelect.getItem(i);
                myGroup.addUserKey(myUsers.getuKey_email().replace('.','*'));

            }
            DBUtils.myGroupsRef.child(gKey).setValue(myGroup).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        updateUsers(gKey);
                        finish();
                        //todo don't forget to update all users in new group
                    }else
                    {
                        Toast.makeText(AddGroupActivity.this,"Adding group failed: "+task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

}
