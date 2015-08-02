package com.example.carryingma;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/**
 * Created by SC on 2015/8/2.
 * This is for an Activity has to show a dialog when it gets a notification.
 * You have to set a condition yourself  to decide to call this NotificationDialog or not
 * And you  must  implement what should do in your Activity when a user choose a button. (New an obj of OnSureClickListener and override it.)
 *
 * See the sample:
 * NotificationDialog.OnSureClickListener listener = new OnSureClickListener() {
 *    @Override
 *     public  void chooseOkButton() {
 *         // Do something you want  when a user choose the Ok button on the dialog in your Activity
 *         //We default the function to do a task when get a known notification
 *         //For example: to chang the user's ExamDate when get a notification "My exam date is different from  the mode of classmates'"
 *     }
 * }
 * NotificationDialog dialog = new NotificationDialog(this, listener);
 * dialog.show();       // dialog will show out.
 */
public class NotificationDialog extends Dialog {

    private Context context;
    private OnSureClickListener mListener;
    private TextView messageTitle, messageContent;
    private Button OkButton, CancelButton;


    public NotificationDialog(Context context) {
        super(context);
        this.context = context;
    }
    public NotificationDialog(Context context, OnSureClickListener listener) {
        super(context);
        mListener = listener;
    }
    protected  void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_notification);
        this.setTitle("推播訊息");
        findView();

        messageTitle.setText("Hello! "+Definition.USER_NAME+": \n"+"Your exam date is different from others!!");
        messageContent.setText("Do you want to change your exam date?\n"+ Definition.USER_EXAM_DATE + "->" + Definition.MODE_EAXM_DATE +"?");

        //Cancel Button
        //When a user click in the dialog, it will call the function "chooseCancelButton()"
        //Then the "chooseCancelButton()" you implement in your Activity will be call.
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.chooseCancelButton();
                dismiss();
            }
        });
        // OK Button
        OkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.chooseOkButton();
                dismiss();
            }
        });
    }

    private void findView() {
        messageTitle = (TextView)findViewById(R.id.messageTitle);
        messageContent = (TextView)findViewById(R.id.messageContent);
        OkButton = (Button)findViewById(R.id.okBtn);
        CancelButton = (Button)findViewById(R.id.cancelBtn);
    }
    public interface OnSureClickListener {
    //For an Activity calling a NotificationDialog, it can implement what should do when a user push a OK or Cancel button.
        void chooseOkButton();
        void chooseCancelButton();
    }

}
