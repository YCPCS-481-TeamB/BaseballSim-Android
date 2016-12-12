package kylemeyers22.heroku.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

import kylemeyers22.heroku.ApprovalsFragment;
import kylemeyers22.heroku.R;
import kylemeyers22.heroku.apiControllers.ApprovalController;
import kylemeyers22.heroku.apiObjects.Approval;

public class ApprovalListItemAdapter extends ArrayAdapter<Approval> {
    private ArrayList<Approval> approvalList;
    private SharedPreferences preferences = getContext().getSharedPreferences("LoginActivity", Context.MODE_PRIVATE);
    private String authToken = preferences.getString("apiToken", null);

    public ApprovalListItemAdapter(Context context, int textViewResourceID, ArrayList<Approval> items) {
        super(context, textViewResourceID, items);
        this.approvalList = items;
    }

    @NonNull
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        View convert = convertView;

        if (convert == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convert = inflater.inflate(R.layout.approvalrow, parent, false);
        }

        final Approval current = approvalList.get(position);
        final ApprovalController approveCont = new ApprovalController(authToken);

        if (current != null) {
            TextView approvalId = (TextView) convert.findViewById(R.id.approvalID);
            TextView approvalStatus = (TextView) convert.findViewById(R.id.approvalStatus);
            TextView approvalDate = (TextView) convert.findViewById(R.id.approvalDate);

            approvalId.setText(getContext().getString(R.string.approval_header, current.getId()));
            approvalStatus.setText(current.getStatus());
            approvalDate.setText(current.getDate());
        }

        convert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (current != null) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(v.getContext());
                    alert.setTitle("Approve Request");
                    // This portion handles instances where the list of approval requests may
                    // contain "pending" and "approved" requests
//                    if (current.getStatus().equals("pending")) {
//                        alert.setMessage("Approve this game?");
//                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                // Approve this approval request
//                                try {
//                                    approveCont.approveRequest(current);
//                                    // Remove the newly approved request
//                                    approvalList.remove(current);
//                                    notifyDataSetChanged();
//                                } catch (IOException iexc) {
//                                    iexc.getCause();
//                                }
//                            }
//                        });
//                        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                            }
//                        });
//                    } else if (current.getStatus().equals("approved")) {
//                        alert.setMessage("This is already approved!");
//                        alert.setCancelable(true);
//                    }
                    // Only "pending" approval requests are valid options
                    alert.setMessage("Approve this game?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Approve this approval request
                            try {
                                approveCont.approveRequest(current);
                                // Remove the newly approved request
                                approvalList.remove(current);
                                notifyDataSetChanged();
                            } catch (IOException iexc) {
                                iexc.getCause();
                            }
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            }
        });

        return convert;
    }
}
