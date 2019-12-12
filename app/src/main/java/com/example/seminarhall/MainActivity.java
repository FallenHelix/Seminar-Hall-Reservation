package com.example.seminarhall;


import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private EditText Info;
    private Button Login;
    private Button Register;
    private int counter =5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Name = (EditText) findViewById(R.id.userN);
        Password = (EditText) findViewById(R.id.pass);
        Login = (Button) findViewById(R.id.button);
        Info = (EditText) findViewById(R.id.inf);

        Info.setText("Attempts Remaining: 5");
        Register = (Button) findViewById(R.id.rgs);

        Register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                goToRegister();
            }
        });

        Login.setOnClickListener(new View.OnClickListener (){
            @Override
            public void onClick(View view)
            {
                validate(Name.getText().toString(),Password.getText().toString());
            }
        });


    }

    protected void goToRegister() {
//        Intent intent=new Intent(MainActivity.this,RegisterActivity.class);
        Intent intent=new Intent(MainActivity.this,Singup.class);

        startActivity(intent);
    }

    protected void validate(String UName, String pass) {
        if (UName.equals("Admin") && pass.equals("Admin") ) {
            //Intent int=new Intent(MainActivity.this, secondActivity.class);

        } else {
            counter--;
            Info.setText("Attempts Remaining: "+String.valueOf(counter));
            if(counter==0)
            {
                Login.setEnabled(false);
            }
        }

    }
}

