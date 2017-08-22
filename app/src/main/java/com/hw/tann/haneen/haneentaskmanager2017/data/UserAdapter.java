package com.hw.tann.haneen.haneentaskmanager2017.data;

import android.content.Context;
import android.support.constraint.solver.ArrayLinkedVariables;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.hw.tann.haneen.haneentaskmanager2017.R;

import java.util.ArrayList;

/**
 * Created by user on 21/08/2017.
 */

public class UserAdapter extends ArrayAdapter<MyUsers>{
    ImageButton ibtnCall;
    ImageButton ibtnDel;
    CheckBox cbSelect;

    TextView tvEmail, tvName;

    private int target;
 //   private int resouce;

    public final static int GROUPMEMBERS=1;
    public final static int SEARCH_RESULT_MEMBERS=2;
    public final static int SELECTED_MEMBERS=3;
    public final static int SPINNER_MEMBERS=4;
    private ArrayList<MyUsers> selectedUsers;

    public UserAdapter(Context context, int resouce, int target){
        super(context, resouce, target);
        selectedUsers=new ArrayList<MyUsers>();
        this.target=target;
    }

    public ArrayList<MyUsers> getSelectedUsers() {
        return selectedUsers;
    }

    public View getView(int position, View convertView, ViewGroup parent){


        if(convertView == null)
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.itmuser,parent,false);

        //find view by ID
        ibtnCall = (ImageButton) convertView.findViewById(R.id.itmBtnUserCall);
        ibtnDel = (ImageButton) convertView.findViewById(R.id.itmBtnUserDel);

        cbSelect = (CheckBox) convertView.findViewById(R.id.itmCBSelect);
        tvEmail = (TextView) convertView.findViewById(R.id.itmtvUserEmail);
        tvName = (TextView) convertView.findViewById(R.id.itmtvUserName);


        final MyUsers myUser = (MyUsers) getItem(position);

        tvEmail.setText(myUser.getName());
        tvName.setText(myUser.getuKey_email());


        switch (target) {
            case GROUPMEMBERS:
                ibtnCall.setVisibility(View.GONE);
                ibtnDel.setVisibility(View.GONE);
                cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        call("");
                    }
                });
                break;
            case SEARCH_RESULT_MEMBERS:
                ibtnCall.setVisibility(View.GONE);
                ibtnDel.setVisibility(View.GONE);
                cbSelect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        select(myUser, isChecked);
                    }
                });
                break;
            case SELECTED_MEMBERS:
                ibtnCall.setVisibility(View.GONE);
                cbSelect.setVisibility(View.GONE);
                ibtnDel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteMember(myUser);
                    }
                });
                break;
            case SPINNER_MEMBERS:
                ibtnCall.setVisibility(View.GONE);
                cbSelect.setVisibility(View.GONE);
                ibtnDel.setVisibility(View.GONE);
                break;
        }
        return convertView;
    }

    private void deleteMember(MyUsers myUser) {
    }

    private void select(MyUsers myUser, boolean isChecked){

        if(isChecked)
        {
            selectedUsers.add(myUser);
        }
        else
        {
            selectedUsers.remove(myUser);
        }

    }
    private void call(String phone){

    }
}


