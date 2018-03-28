package cmput301w18t09.orbid;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;

/**
 * The dialog shown when a username is clicked anywhere in the application -- shows
 * all information pertaining to the user that was clicked.
 *
 * Created by micag on 2018-03-01.
 *
 * @author Mica Grant-Hagen
 * @see User
 */
public class UserProfileDialog extends DialogFragment {

    private User user;

    /**
     * Called when the dialog box is first created to instantiate the dialog object.
     *
     * @param savedInstanceState Holds the username to populate the dialog's text views
     * @return The instantiated user dialog box
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        // Inflate the custom dialog box view and fetch username
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View content = layoutInflater.inflate(R.layout.dialog_user_profile, null);
        Bundle args = getArguments();
        String username = args.getString("username");

        // Set the functionality of the "OK" dialog button
        builder.setView(content)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

        // Assign the layout's text views
        TextView tvfullname = content.findViewById(R.id.fullnameview);
        TextView tvemail = content.findViewById(R.id.emailview);
        TextView tvphone = content.findViewById(R.id.phoneview);
        TextView tvusername = content.findViewById(R.id.username_title);

        // Set up the data manager
        DataManager.getUsers getUsers = new DataManager.getUsers(getActivity());
        ArrayList<String> queryParameters = new ArrayList<>();
        ArrayList<User> returnUsers;

        // Get the information for the clicked user
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
            // TODO: Must return with error before populating dialog box with non-existing information
        }

        // Populate the text views with the information for the clicked user and return to caller
        tvusername.setText(user.getUsername());
        tvemail.setText(user.getEmail());
        tvphone.setText(user.getPhoneNumber());
        tvfullname.setText(user.getLastName()+", "+ user.getFirstName());
        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }
}

