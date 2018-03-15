package cmput301w18t09.orbid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


/**
 * Created by micag on 2018-03-01.
 */

public class UserProfileDialog extends DialogFragment {

    private User user;

    public interface UserProfileListener{

        void onPositive(DialogFragment dialogFragment, int position);
    }

    UserProfileListener listener;


//    @Override
//    public void onAttach(Activity activity){
//        super.onAttach(activity);
//        try {
//            listener = (UserProfileListener) activity;
//        }
//        catch (ClassCastException e){
//            throw new ClassCastException(activity.toString() + " must implement");
//        }
//    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View content = layoutInflater.inflate(R.layout.dialog_user_profile, null);
        Bundle args = getArguments();
        String username = args.getString("username");

        builder.setView(content)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });


        TextView tvfullname = content.findViewById(R.id.fullnameview);
        TextView tvemail = content.findViewById(R.id.emailview);
        TextView tvphone = content.findViewById(R.id.phoneview);
        TextView tvusername = content.findViewById(R.id.username_title);



        DataManager.getUsers getUsers = new DataManager.getUsers(getActivity());
        ArrayList<String> queryParameters = new ArrayList<>();
        ArrayList<User> returnUsers;
        queryParameters.add("username");
        queryParameters.add(username);
        getUsers.execute(queryParameters);
        try {
            returnUsers = getUsers.get();
            user = returnUsers.get(0);
        }
        catch (Exception e) {
            Log.e("Error", "Failed to get ArrayList intended as return from getUsers");
            e.printStackTrace();
            Toast.makeText(getActivity(), "Error getting user information", Toast.LENGTH_SHORT).show();

        }
        tvusername.setText(user.getUsername());
        tvemail.setText(user.getEmail());
        tvphone.setText(user.getPhoneNumber());
        tvfullname.setText(user.getLastName()+", "+ user.getFirstName());



        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;}}

