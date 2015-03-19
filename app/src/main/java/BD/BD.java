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
        valores.put(EAsignatura.FIELD_EVALUACION,asignatura.getEvaluacion());
        return valores;
    }private ContentValues generarValores2(EExamen examen){
        ContentValues valores= new ContentValues();
        valores.put(EExamen.FIELD_NOMBRE,examen.getNombre());
        valores.put(EExamen.FIELD_ASIGNATURA,examen.getAsignatura());
        valores.put(EExamen.FIELD_FECHA,examen.getFecha());
        valores.put(EExamen.FIELD_HORA,examen.getHora());
        valores.put(EExamen.FIELD_TIPOGUARDADO,examen.getTipoGuardado());
        valores.put(EExamen.FIELD_CALENDARIOID,examen.getCalendarioid());
        valores.put(EExamen.FIELD_EVENTOID,examen.getEventoid());
        return valores;
    }
    public long  insertarAsignatura(EAsignatura asignatura){

        long res=db.insert(EAsignatura.TABLE_NAME,null,generarValores(asignatura));
        return res;
    }
    public void insertarExamen(EExamen examen){
        db.insert(EExamen.TABLE_NAME,null,generarValores2(examen));
    }
    public ArrayList<EAsignatura> getAsignaturas(){
        ArrayList<EAsignatura> ArrayAsignaturas =new ArrayList<>();
        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES,EAsignatura.FIELD_EVALUACION};
       Cursor c= db.query(EAsignatura.TABLE_NAME,columnas,null,null,null,null,null);
        if (c.moveToFirst()){
            do {
                EAsignatura a=new EAsignatura();
                a.setId(c.getInt(0));
                a.setNombre(c.getString(1));
                a.setEnlaces(c.getString(2));
                a.setEvaluacion(c.getString(3));
                ArrayAsignaturas.add(a);
            }while (c.moveToNext());
        }
        return ArrayAsignaturas;
    }
    public Cursor getAsignaturasCursor(){
        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES,EAsignatura.FIELD_EVALUACION};
        String orderBy =  EAsignatura.FIELD_NOMBRE;
        Cursor c= db.query(EAsignatura.TABLE_NAME,columnas,null,null,null,null,orderBy);
        return c;
    }
    public Cursor getAsignaturasExamenesCursor(){
        String consulta= "select distinct asignaturas.nombre from asignaturas,examenes where asignaturas.nombre=examenes.asignatura";
       Cursor c= db.rawQuery(consulta,null);
        return c;
    }
    public Cursor getExamenesCursor(){
        String columnas[]={EExamen.FIELD_ID,EExamen.FIELD_NOMBRE,EExamen.FIELD_ASIGNATURA,EExamen.FIELD_FECHA,EExamen.FIELD_HORA,EExamen.FIELD_TIPOGUARDADO,EExamen.FIELD_CALENDARIOID,EExamen.FIELD_EVENTOID};
        String orderBy =  EExamen.FIELD_ASIGNATURA;
        Cursor c= db.query(EExamen.TABLE_NAME,columnas,null,null,null,null,orderBy);
        return c;
    }
    public Cursor getExamenesAsignaturaCursor(String Asignatura){
        String columnas[]={EExamen.FIELD_ID,EExamen.FIELD_NOMBRE,EExamen.FIELD_ASIGNATURA,EExamen.FIELD_FECHA,EExamen.FIELD_HORA,EExamen.FIELD_TIPOGUARDADO,EExamen.FIELD_CALENDARIOID,EExamen.FIELD_EVENTOID};
        String orderBy =  EExamen.FIELD_ASIGNATURA;
        Cursor c= db.query(EExamen.TABLE_NAME,columnas,EExamen.FIELD_ASIGNATURA+"=?",new String[]{Asignatura},null,null,orderBy);
        return c;
    }
    public Cursor buscarAsignatura(String nombre){

        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES,EAsignatura.FIELD_EVALUACION};
        Cursor c= db.query(EAsignatura.TABLE_NAME,columnas,EAsignatura.FIELD_NOMBRE+"=?",new String[]{nombre},null,null,null);
        return c;
    }
    public Cursor buscarExamen(String nombre){
        String columnas[]={EExamen.FIELD_ID,EExamen.FIELD_NOMBRE,EExamen.FIELD_ASIGNATURA,EExamen.FIELD_FECHA,EExamen.FIELD_HORA,EExamen.FIELD_TIPOGUARDADO,EExamen.FIELD_CALENDARIOID,EExamen.FIELD_EVENTOID};
        Cursor c= db.query(EExamen.TABLE_NAME,columnas,EExamen.FIELD_NOMBRE+"=?",new String[]{nombre},null,null,null);
        return c;
    }
   public EAsignatura getEAsignatura(String ID){
        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES,EAsignatura.FIELD_EVALUACION};

        Cursor c= db.query(EAsignatura.TABLE_NAME,columnas,EAsignatura.FIELD_ID+"=?",new String[]{ID},null,null,null);
        if (c.getCount()>0){
            c.moveToFirst();
            Log.e("numero", String.valueOf(c.getCount()));
        String rowName = c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
        String rowEnlaces= c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_ENLACES));
        String rowEvaluacion= c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_EVALUACION));
        EAsignatura asig= new EAsignatura(rowName);
        asig.setEnlaces(rowEnlaces);
        asig.setEvaluacion(rowEvaluacion);
        return asig;
        }else
            return null;
    }

    public EExamen getEExamen(String ID){
        String columnas[]={EExamen.FIELD_ID,EExamen.FIELD_NOMBRE,EExamen.FIELD_ASIGNATURA,EExamen.FIELD_FECHA,EExamen.FIELD_HORA,EExamen.FIELD_TIPOGUARDADO,EExamen.FIELD_CALENDARIOID,EExamen.FIELD_EVENTOID};

        Cursor c= db.query(EExamen.TABLE_NAME,columnas,EExamen.FIELD_ID+"=?",new String[]{ID},null,null,null);
        if (c.getCount()>0){
            c.moveToFirst();
            Log.e("numero", String.valueOf(c.getCount()));
            String rowName = c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_NOMBRE));
            String rowAsig= c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_ASIGNATURA));
            String rowFecha= c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_FECHA));
            String rowHora= c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_HORA));
            String rowTipoGuardado= c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_TIPOGUARDADO));
            String rowCalendarioId= c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_CALENDARIOID));
            String rowEventoId= c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_EVENTOID));
            EExamen exa= new EExamen(rowName);
            exa.setAsignatura(rowAsig);
            exa.setFecha(rowFecha);
            exa.setHora(rowHora);
            exa.setTipoGuardado(rowTipoGuardado);
            exa.setCalendarioid(rowCalendarioId);
            exa.setEventoid(rowEventoId);
            return exa;
        }else
            return null;


    }
    public void deleteExamen(String ID){
        int id=Integer.valueOf(ID);

        String consulta= "delete from examenes where _id="+id;
        db.execSQL(consulta);
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
