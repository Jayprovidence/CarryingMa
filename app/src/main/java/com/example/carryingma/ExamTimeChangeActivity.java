package com.example.carryingma;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ExamTimeChangeActivity extends Activity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerUser,spinnerExamDate;
    private TextView averageTextView;
    private String[] user = {"A","B","C","D","E"};
    private String[] examDate = {"1","2","3","4","5","6","7"};
    private ArrayAdapter<String> userAdapter,examDateAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam_time_change);
        findView();
        setSpinner();
        setHttpPost();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Spinner spinner =(Spinner) parent;
        if(spinner.getId() == R.id.spinnerUser) {
            Toast.makeText(this, "你選擇" + user[position], Toast.LENGTH_SHORT).show();
        }else if(spinner.getId() == R.id.spinnerExamDate) {
            Toast.makeText(this, "你選擇" + examDate[position], Toast.LENGTH_SHORT).show();
        }
        setData();
    }

    //Buttons action
    public void ChangeExamTimeBtn(View view) {
        sentExamDate();
    }

    private void setData() {
        //TODO: Set the data about user and examDate then prepare to sent data to server
    }

    private void sentExamDate() {
        //TODO: Find the user and update his examDate
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void findView() {
        spinnerUser = (Spinner)findViewById(R.id.spinnerUser);
        spinnerExamDate = (Spinner)findViewById(R.id.spinnerExamDate);
        averageTextView = (TextView)findViewById(R.id.averageTextView);
    }

    private void setSpinner() {
        userAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,user);
        examDateAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,examDate);
        userAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerUser.setAdapter(userAdapter);
        spinnerExamDate.setAdapter(examDateAdapter);
        spinnerUser.setOnItemSelectedListener(this);
        spinnerExamDate.setOnItemSelectedListener(this);
    }

    private void setHttpPost() {
        String uriAPI = "";

    }
}
