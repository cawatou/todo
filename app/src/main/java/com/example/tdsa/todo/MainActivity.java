package com.example.tdsa.todo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    private ArrayList<String> arrayListToDo;
    private ArrayAdapter<String> arrayAdapterToDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        arrayListToDo = new ArrayList<String>();
        arrayAdapterToDo = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayListToDo);

        ListView listViewToDo = (ListView)findViewById(R.id.listViewToDo);
        listViewToDo.setAdapter(arrayAdapterToDo);

        registerForContextMenu(listViewToDo);

        try{
            Log.i("ON CREATE", "Start App");
            Scanner scanner = new Scanner(openFileInput("toDo.txt"));
            while(scanner.hasNextLine()){
                String toDo = scanner.nextLine();
                arrayAdapterToDo.add(toDo);
            }
        }catch(Exception e){
            Log.i("ON CREATE", e.getMessage());
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        if(v.getId() != R.id.listViewToDo){
            return;
        }

        menu.setHeaderTitle("You gonna do this:");
        String[] options = {"delete", "resume"};
        for(String option : options){
            menu.add(option);
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        int selectedIndex = info.position;

        if(item.getTitle().equals("delete")){
            arrayListToDo.remove(selectedIndex);
            arrayAdapterToDo.notifyDataSetChanged();
        }

        return true;
    }

    public void addBtnClick(View v){
        EditText editTextToDo = (EditText) findViewById(R.id.editTextToDo);
        String toDo = editTextToDo.getText().toString().trim();

        if(toDo.isEmpty()){
            return;
        }

        arrayListToDo.add(0, toDo);
        arrayAdapterToDo.notifyDataSetChanged();
        editTextToDo.setText("");

        try{
            Log.i("addBtnClick()", "write task");
            PrintWriter pw = new PrintWriter(openFileOutput("toDo.txt", Context.MODE_PRIVATE));

            for(String task : arrayListToDo){
                pw.println(task);
            }

            pw.close();
        }catch(Exception e){
            Log.i("addBtnClick()", e.getMessage());
        }
    }
}
