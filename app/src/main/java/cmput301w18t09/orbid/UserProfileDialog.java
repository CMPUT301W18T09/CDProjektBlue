package cmput301w18t09.orbid;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RatingBar;
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
        final Bundle args = getArguments();
        final String username = args.getString("username");

        // Set the functionality of the "OK" dialog button
        builder.setView(content)
                .setNeutralButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();

                    }
                });

        // Let the current user review only those individuals they have had a completed task interaction with
        if (args.getBoolean("canReview") && !username.equals(LoginActivity.getCurrentUsername())) {
            builder.setView(content).setPositiveButton("Add Review", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(getActivity(), AddReviewActivity.class);
                    if (args.get("reviewType").equals("Requester")) {
                        intent.putExtra("reviewType", "Requester");
                    }
                    else if (args.get("reviewType").equals("Provider")) {
                        intent.putExtra("reviewType", "Provider");
                    }
                    intent.putExtra("taskID", args.get("taskID").toString());
                    intent.putExtra("reviewee", username);
                    startActivity(intent);
                }
            });
        }

        // Assign the layout's text views
        TextView tvfullname = content.findViewById(R.id.fullnameview);
        TextView tvemail = content.findViewById(R.id.emailview);
        TextView tvphone = content.findViewById(R.id.phoneview);
        TextView tvusername = content.findViewById(R.id.username_title);
        RatingBar rbRating = content.findViewById(R.id.ratingBar);

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

        //Get review score from user
        Float scoreSum = 0.0f;
        Float ave = 3.7f;
        ArrayList<Review> revList = user.getReviewList();
        if (revList.size() > 0) {
            for (int i = 0; i < revList.size(); i++) {
                scoreSum += revList.get(i).getRating();
            }
            ave = scoreSum/revList.size();
        }
        rbRating.setRating(ave);
        rbRating.setIsIndicator(true);  //Stops user from changing value


        rbRating.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Intent intent = new Intent(getActivity(), ListReviewActivity.class);
                intent.putExtra("username", username);
                startActivity(intent);
                return false;
            }
        });


        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }


}

