package BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


/**
 * Created by Mikel on 03/02/2015.
 */
public class BD  extends SQLiteOpenHelper{
    private static final String DB_NAME= "agenda";
    private static final int SCHEME_VERSION = 1;
    private SQLiteDatabase db;
    public BD(Context context) {
        super(context, DB_NAME, null, SCHEME_VERSION);
        Log.e("HolaMundo", "se ha creado la bd");
        db=this.getWritableDatabase();
    }
    private ContentValues generarValores(EAsignatura asignatura){
        ContentValues valores= new ContentValues();
        valores.put(EAsignatura.FIELD_NOMBRE,asignatura.getNombre());
        valores.put(EAsignatura.FIELD_ENLACES,asignatura.getEnlaces());
        return valores;
    }private ContentValues generarValores2(EExamen examen){
        ContentValues valores= new ContentValues();
        valores.put(EExamen.FIELD_NOMBRE,examen.getNombre());
        valores.put(EExamen.FIELD_ASIGNATURA,examen.getAsignatura());
        return valores;
    }
    public void insertarAsignatura(EAsignatura asignatura){
        db.insert(EAsignatura.TABLE_NAME,null,generarValores(asignatura));
    }
    public void insertarExamen(EExamen examen){
        db.insert(EExamen.TABLE_NAME,null,generarValores2(examen));
    }
    public ArrayList<EAsignatura> getAsignaturas(){
        ArrayList<EAsignatura> ArrayAsignaturas =new ArrayList<>();
        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES};
       Cursor c= db.query(EAsignatura.TABLE_NAME,columnas,null,null,null,null,null);
        if (c.moveToFirst()){
            do {
                EAsignatura a=new EAsignatura();
                a.setId(c.getInt(0));
                a.setNombre(c.getString(1));
                ArrayAsignaturas.add(a);
            }while (c.moveToNext());
        }
        return ArrayAsignaturas;
    }
    public Cursor getAsignaturasCursor(){
        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES};
        Cursor c= db.query(EAsignatura.TABLE_NAME,columnas,null,null,null,null,null);
        return c;
    }
    public Cursor buscarAsignatura(String nombre){

        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES};
        Cursor c= db.query(EAsignatura.TABLE_NAME,columnas,EAsignatura.FIELD_NOMBRE+"=?",new String[]{nombre},null,null,null);
        return c;
    }
    public EAsignatura getAsignaturaObj(String ID){
        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES};

        Cursor c= db.query(EAsignatura.TABLE_NAME,columnas,EAsignatura.FIELD_ID+"=?",new String[]{ID},null,null,null);
        if (c.getCount()>0){
            c.moveToFirst();
            Log.e("numero", String.valueOf(c.getCount()));
        String rowName = c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
        String rowEnlaces= c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_ENLACES));
        EAsignatura asig= new EAsignatura(rowName);
        asig.setEnlaces(rowEnlaces);
        return asig;
        }else
            return null;


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(EAsignatura.CREATE_DB_TABLE);
        db.execSQL(EExamen.CREATE_DB_TABLE);
        db.execSQL("PRAGMA foreign_keys = ON;");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
