package com.example.chattest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.chattest.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    String nametxt,phonetxt,emailtxt;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        nametxt = binding.userName.getText().toString();
        phonetxt=binding.phoneNumber.getText().toString();
        emailtxt = binding.email.getText().toString();


        SharedPreferences sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE);
        String num = sharedPreferences.getString("phonee","");
        String name = sharedPreferences.getString("namee","");
        String email = sharedPreferences.getString("emaill","");
        if (!num.isEmpty()){
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("phone",num);
            intent.putExtra("name",name);
            intent.putExtra("email",email);
            startActivity(intent);
            finish();
        }



        binding.registerBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog=new ProgressDialog(MainActivity.this);
                waiting();
                nametxt = binding.userName.getText().toString();
                phonetxt=binding.phoneNumber.getText().toString();
                emailtxt = binding.email.getText().toString();
                if (true){

                    SharedPreferences sharedPreferences = getSharedPreferences("shared", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    /*editor.putString("phonee",phonetxt);
                    editor.putString("namee",nametxt);
                    editor.putString("emaill",emailtxt);
                    editor.apply();*/

                    databaseReference.child("users").child(phonetxt).child("email").setValue(emailtxt);
                    databaseReference.child("users").child(phonetxt).child("profile pic").setValue("");
                    databaseReference.child("users").child(phonetxt).child("name").setValue(nametxt).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful())
                                editor.putString("phonee",phonetxt);
                            editor.putString("namee",nametxt);
                            editor.putString("emaill",emailtxt);
                            editor.apply();
                                dialog.dismiss();
                            Toast.makeText(MainActivity.this, "Added", Toast.LENGTH_SHORT).show();
                            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
                            intent.putExtra("phone",phonetxt);
                            intent.putExtra("name",nametxt);
                            intent.putExtra("email",emailtxt);
                            startActivity(intent);
                            finish();
                        }
                    });


                }
                else{
                    Toast.makeText(MainActivity.this, "fady", Toast.LENGTH_SHORT).show();
                dialog.dismiss();}

            }
        });



    }


    private boolean validate(){
        if(!nametxt.isEmpty() || !phonetxt.isEmpty() || !emailtxt.isEmpty()){
            return true;
        }
        else
            return false;
    }
    private void waiting(){
        dialog.setMessage("loading...");
        dialog.setCancelable(false);
        dialog.show();
    }

}