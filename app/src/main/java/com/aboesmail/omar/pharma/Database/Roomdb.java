package com.aboesmail.omar.pharma.Database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.aboesmail.omar.pharma.Database.UserDB.LocalUser;
import com.aboesmail.omar.pharma.Database.UserDB.UserDAO;
import com.aboesmail.omar.pharma.Database.product.CartProduct;
import com.aboesmail.omar.pharma.Database.product.ProductDao;

@Database(entities = {CartProduct.class, LocalUser.class}, version = 2,exportSchema = false)
public abstract class Roomdb extends RoomDatabase {
    private static Roomdb instance;

    public abstract ProductDao productDao();
    public abstract UserDAO userDAO();

   public static synchronized Roomdb getInstance(Context context){
        if (instance == null){
            instance= Room.databaseBuilder(context.getApplicationContext(),
                    Roomdb.class,"AppDB")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }
    private static RoomDatabase.Callback roomCallBack =new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new DBAsyncTask(instance).execute();
        }
    };
    private static class DBAsyncTask extends AsyncTask<Void,Void,Void>{
        ProductDao productDao;
        UserDAO userDAO;
        private DBAsyncTask(Roomdb db){
            productDao=db.productDao();
            userDAO=db.userDAO();
        }
        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
