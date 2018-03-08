package cmput301w18t09.orbid;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Created by micag on 2018-03-01.
 */

public class UserProfileDialog extends DialogFragment {

    public interface UserProfileListener{

        void onPositive(DialogFragment dialogFragment, int position);
    }

    UserProfileListener listener;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        try {
            listener = (UserProfileListener) activity;
        }
        catch (ClassCastException e){
            throw new ClassCastException(activity.toString() + " must implement");
        }
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater layoutInflater = getActivity().getLayoutInflater();
        final View content = layoutInflater.inflate(R.layout.dialog_user_profile, null);
        final Bundle args = getArguments();

        builder.setView(content)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {


                    }
                });/*.setNegativeButton(R.string.cancel_action, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });*/


        TextView fullname = content.findViewById(R.id.fullnameview);
        TextView useremail = content.findViewById(R.id.emailview);
        TextView userphone = content.findViewById(R.id.phoneview);
        TextView username = content.findViewById(R.id.username_title);

        fullname.setText(args.getString(" fullname"));
        useremail.setText(args.getString("email"));
        userphone.setText(args.getString("phone"));
        username.setText(args.getString("username"));

 /*       date.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }


           @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String text = .getText().toString();
                int textlength = date.getText().length();
                if (text.endsWith("-")){
                    return;
                }

                if (textlength == 5 || textlength == 8){
                    date.setText(new StringBuilder(text).insert(text.length()-1, "-").toString());
                    date.setSelection(date.getText().length());
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
*/

        final AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;}}

 /*      dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
           @Override
            public void onClick(View view) {

                TextView username = content.findViewById(R.id.subPrice);
                TextView name = content.findViewById(R.id.subName);
                TextView date = content.findViewById(R.id.subDate);
                //if (name.getText().length() >= 1 && date.getText().length() == 10 && price.getText().length() >= 1){
                    listener.onEditPositive(UserProfileDialog.this, args.getInt("Position"));
                    dialog.dismiss();
                }
                else {
                    Toast.makeText(getActivity(), "Not all required fields are filled. Please try again", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return dialog;
        }
    }*/
