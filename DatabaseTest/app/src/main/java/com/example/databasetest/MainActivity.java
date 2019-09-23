package com.example.databasetest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new MyDatabaseHelper(this, "BookStore.db", null, 1);

        //创建或者获取数据库
        Button createDatabase = findViewById(R.id.create_database);
        createDatabase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbHelper.getWritableDatabase();
            }
        });

        //添加数据
        Button addData = findViewById(R.id.add_data);
        addData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //安卓的方法
//                ContentValues values = new ContentValues();
//                // 开始组装第一条数据
//                values.put("name", "The Da Vinci Code");
//                values.put("author", "Dan Brown");
//                values.put("pages", 454);
//                values.put("price", 16.96);
//                db.insert("Book", null, values);
//                values.clear();
//                // 开始组装第二条数据
//                values.put("name", "The Lost Symbol");
//                values.put("author", "Dan Brown");
//                values.put("pages", 510);
//                values.put("price", 19.95);
//                db.insert("Book", null, values); // 插入第二条数据

                //SQL方法
                db.execSQL("insert into Book (name, author, pages, price) values(?, ?, ?, ?)", new String[]{"The Da Vinci Code", "Dan Brown", "454", "16.96"});
                db.execSQL("insert into Book (name, author, pages, price) values(?, ?, ?, ?)", new String[]{"The Lost Symbol", "Dan Brown", "510", "19.95"});

            }
        });

        //更新数据
        Button updateData = findViewById(R.id.update_data);
        updateData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //安卓方法
//                ContentValues values = new ContentValues();
//                values.put("price", 10.99);
//                db.update("Book", values, "name = ?", new String[]{"The Da Vinci Code"});

                //SQL方法
                db.execSQL("update Book set price = ? where name = ?", new String[]{"10.99", "The Da Vinci Code"});
            }
        });

        //删除数据
        Button deleteButton = findViewById(R.id.delete_data);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();
                //安卓方法
//                db.delete("Book", "pages > ?", new String[]{"500"});

                //SQL方法
                db.execSQL("delete from Book where pages > ?", new String[]{"500"});
            }
        });

        //查询数据
        Button queryButton = findViewById(R.id.query_data);
        queryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SQLiteDatabase db = dbHelper.getWritableDatabase();

                //安卓方法
                //查询Book表中所有的数据
                Cursor cursor = db.query("Book", null, null, null, null, null, null, null);
                //SQL方法
                db.rawQuery("select * from Book", null);

                if (cursor.moveToFirst()) {
                    do {
                        String name = cursor.getString(cursor.getColumnIndex("name"));
                        String author = cursor.getString(cursor.getColumnIndex("author"));
                        int pages = cursor.getInt(cursor.getColumnIndex("pages"));
                        double price = cursor.getDouble(cursor.getColumnIndex("price"));
                        Log.d("MainActivity", "book name is " + name);
                        Log.d("MainActivity", "book author is " + author);
                        Log.d("MainActivity", "book pages is " + pages);
                        Log.d("MainActivity", "book price is " + price);
                    }
                    while (cursor.moveToNext());
                }
                cursor.close();
            }
        });


    }
}
