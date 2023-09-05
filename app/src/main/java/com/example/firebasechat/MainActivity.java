package com.example.firebasechat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private static  int SIGN_IN_REQUEST_CODE=1;
    private FirebaseListAdapter<Message> adapter;
    RelativeLayout activity_main;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

       // activity_main=(RelativeLayout)findViewById(R.id.activity_main);
        button=(Button)findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText input =(EditText)findViewById(R.id.editText);
                //считываем сообщение из поля ввода и отправляем новый экземпляр в бд
                FirebaseDatabase.getInstance().getReference().push()
                        .setValue(new Message(input.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getEmail()));
                input.setText("");
            }
        });
        //если пользователь не авторизован, то показываем экран авторизации
        //создается метод, которому  мы передаем интент создающий и настраивающий окно авторизации и константу, хранящую код авторизации
        //ctr+alt+t
        if (FirebaseAuth.getInstance().getCurrentUser()==null) {
            startActivityForResult(AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .build(),SIGN_IN_REQUEST_CODE);
        } else {
            displayChat();
        }
    }
    private  void displayChat(){
        //crate list messages
        ListView listMessage=(ListView)findViewById(R.id.ListView);
        adapter=new FirebaseListAdapter<Message>(this, Message.class,R.layout.item,
                FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, Message model, int position) {
            TextView textMessage, autor, timeMessage;
            textMessage=(TextView)findViewById(R.id.tvMessage);
            autor=(TextView)findViewById(R.id.tvUser);
            timeMessage=(TextView)findViewById(R.id.tvTime);
            textMessage.setText(model.getTextMessage());
            autor.setText(model.getAutor());
            //import android.text.format.DateFormat;
            timeMessage.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",model.getTimeMessage()));
            }
        };
        listMessage.setAdapter(adapter);
    }
    //показ окна чата после авторизации
}