package BD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.util.Log;

import com.mikel.agenda.ActividadPrincipal;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by Mikel on 03/02/2015.
 */
public class BD  extends SQLiteOpenHelper{
    public static final String DB_NAME= "agenda";
    private static final int SCHEME_VERSION = 3;

    private SQLiteDatabase db;

    String columnasExamen[]={EExamen.FIELD_ID,EExamen.FIELD_NOMBRE,EExamen.FIELD_ASIGNATURA,EExamen.FIELD_FECHA,EExamen.FIELD_HORA,EExamen.FIELD_TIPOGUARDADO,EExamen.FIELD_CALENDARIOID,EExamen.FIELD_EVENTOID,EExamen.FIELD_CALENDARIONOMBRE,EExamen.FIELD_DESCRIPCION};
    public BD(Context context) {
        super(context, DB_NAME, null, SCHEME_VERSION);
        Log.e("HolaMundo", "se ha creado la bd");
        db=this.getWritableDatabase();
    }

    public int openBD(){
        try{
            db=this.getWritableDatabase();
            return 1;
        }catch(Exception e){
            return -1;
        }
    }
    public  int closeBD(){
        try{
            db.close();
            return 1;
        }catch(Exception e){
            return -1;
        }

    }

    private ContentValues generarValoresNota(ENota nota){
        ContentValues valores= new ContentValues();
        valores.put(ENota.FIELD_ASIGNATURA,nota.getAsignatura());
        valores.put(ENota.FIELD_EXAMEN,nota.getExamen());
        valores.put(ENota.FIELD_NOTA,nota.getNota());
        valores.put(ENota.FIELD_NOTASOBRE,nota.getNota_sobre());
        return valores;
    }
    private ContentValues generarValoresAsignatura(EAsignatura asignatura){
        ContentValues valores= new ContentValues();
        valores.put(EAsignatura.FIELD_NOMBRE,asignatura.getNombre());
        valores.put(EAsignatura.FIELD_ENLACES,asignatura.getEnlaces());
        valores.put(EAsignatura.FIELD_EVALUACION,asignatura.getEvaluacion());
        valores.put(EAsignatura.FIELD_NOTA,asignatura.getNota());
        return valores;
    }private ContentValues generarValoresExamen(EExamen examen){
        ContentValues valores= new ContentValues();
        valores.put(EExamen.FIELD_NOMBRE,examen.getNombre());
        valores.put(EExamen.FIELD_ASIGNATURA,examen.getAsignatura());
        valores.put(EExamen.FIELD_FECHA,examen.getFecha());
        valores.put(EExamen.FIELD_HORA,examen.getHora());
        valores.put(EExamen.FIELD_TIPOGUARDADO,examen.getTipoGuardado());
        valores.put(EExamen.FIELD_CALENDARIOID,examen.getCalendarioid());
        valores.put(EExamen.FIELD_CALENDARIONOMBRE,examen.getCalendarionombre());
        valores.put(EExamen.FIELD_EVENTOID,examen.getEventoid());
        valores.put(EExamen.FIELD_DESCRIPCION,examen.getDescripcion());
        return valores;
    }
    public long  insertarAsignatura(EAsignatura asignatura){
        return db.insert(EAsignatura.TABLE_NAME,null,generarValoresAsignatura(asignatura));
    }
    public long insertarExamen(EExamen examen){
           return db.insert(EExamen.TABLE_NAME,null,generarValoresExamen(examen));
    }
    public void insertarNota(ENota nota){
        db.insert(ENota.TABLE_NAME,null,generarValoresNota(nota));
    }
    public ArrayList<EAsignatura> getAsignaturas(){
        ArrayList<EAsignatura> ArrayAsignaturas =new ArrayList<>();
        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES,EAsignatura.FIELD_EVALUACION,EAsignatura.FIELD_NOTA};
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
        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES,EAsignatura.FIELD_EVALUACION,EAsignatura.FIELD_NOTA};
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
       // String columnas[]={EExamen.FIELD_ID,EExamen.FIELD_NOMBRE,EExamen.FIELD_ASIGNATURA,EExamen.FIELD_FECHA,EExamen.FIELD_HORA,EExamen.FIELD_TIPOGUARDADO,EExamen.FIELD_CALENDARIOID,EExamen.FIELD_EVENTOID};
        String orderBy =  EExamen.FIELD_ASIGNATURA;
        Cursor c= db.query(EExamen.TABLE_NAME,columnasExamen,null,null,null,null,orderBy);
        return c;
    }
    public Cursor getExamenesAsignaturaCursor(String Asignatura){
        //String columnas[]={EExamen.FIELD_ID,EExamen.FIELD_NOMBRE,EExamen.FIELD_ASIGNATURA,EExamen.FIELD_FECHA,EExamen.FIELD_HORA,EExamen.FIELD_TIPOGUARDADO,EExamen.FIELD_CALENDARIOID,EExamen.FIELD_EVENTOID};
        String orderBy =  EExamen.FIELD_ASIGNATURA;
        Cursor c= db.query(EExamen.TABLE_NAME,columnasExamen,EExamen.FIELD_ASIGNATURA+"=?",new String[]{Asignatura},null,null,orderBy);
        return c;
    }
    public Cursor buscarAsignatura(String nombre){

        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES,EAsignatura.FIELD_EVALUACION,EAsignatura.FIELD_NOTA};
        Cursor c= db.query(EAsignatura.TABLE_NAME,columnas,EAsignatura.FIELD_NOMBRE+"=?",new String[]{nombre},null,null,null);
        return c;
    }
    public Cursor buscarExamen(String nombre){
        //String columnas[]={EExamen.FIELD_ID,EExamen.FIELD_NOMBRE,EExamen.FIELD_ASIGNATURA,EExamen.FIELD_FECHA,EExamen.FIELD_HORA,EExamen.FIELD_TIPOGUARDADO,EExamen.FIELD_CALENDARIOID,EExamen.FIELD_EVENTOID};
        Cursor c= db.query(EExamen.TABLE_NAME,columnasExamen,EExamen.FIELD_NOMBRE+"=?",new String[]{nombre},null,null,null);
        return c;
    }
    /*17-04-15 al 8-05-15*/
    public long countExamenesAsig(String asig){
        //String columnas[]={EExamen.FIELD_ID,EExamen.FIELD_NOMBRE,EExamen.FIELD_ASIGNATURA,EExamen.FIELD_FECHA,EExamen.FIELD_HORA,EExamen.FIELD_TIPOGUARDADO,EExamen.FIELD_CALENDARIOID,EExamen.FIELD_EVENTOID};
        Cursor c= db.query(EExamen.TABLE_NAME,columnasExamen,EExamen.FIELD_ASIGNATURA+"=?",new String[]{asig},null,null,null);
        return c.getCount();
    }
    /*19-04-15*/
    public float getNotaRestante(String asig){
        String columnas[]={ENota.FIELD_EXAMEN,ENota.FIELD_ASIGNATURA,ENota.FIELD_NOTA,ENota.FIELD_NOTASOBRE};
        Cursor c= db.query(ENota.TABLE_NAME,columnas,ENota.FIELD_ASIGNATURA+"=?",new String[]{asig},null,null,null);
        Cursor c2 =buscarAsignatura(asig);
        float nota=100;
        if (c2.moveToFirst()){
            nota=c2.getFloat(c2.getColumnIndexOrThrow(EAsignatura.FIELD_NOTA));
        }
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                nota=nota-c.getFloat(c.getColumnIndexOrThrow(ENota.FIELD_NOTASOBRE));
            } while(c.moveToNext());
            if (nota<0){
                nota=0;
            }
        }
        return nota;
    }
    /*11-05-15*/
    public float getNotaTotal(String asig){
        String columnas[]={ENota.FIELD_EXAMEN,ENota.FIELD_ASIGNATURA,ENota.FIELD_NOTA,ENota.FIELD_NOTASOBRE};
        Cursor c= db.query(ENota.TABLE_NAME,columnas,ENota.FIELD_ASIGNATURA+"=?",new String[]{asig},null,null,null);
        Cursor c2 =buscarAsignatura(asig);
        float nota=0;
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                nota=nota+c.getFloat(c.getColumnIndexOrThrow(ENota.FIELD_NOTA));
            } while(c.moveToNext());
        }
        return nota;
    }
   public EAsignatura getEAsignatura(String ID){
        String columnas[]={EAsignatura.FIELD_ID,EAsignatura.FIELD_NOMBRE,EAsignatura.FIELD_ENLACES,EAsignatura.FIELD_EVALUACION,EAsignatura.FIELD_NOTA};

        Cursor c= db.query(EAsignatura.TABLE_NAME,columnas,EAsignatura.FIELD_ID+"=?",new String[]{ID},null,null,null);
        if (c.getCount()>0){
            c.moveToFirst();
            Log.e("numero", String.valueOf(c.getCount()));
        String rowName = c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_NOMBRE));
        String rowEnlaces= c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_ENLACES));
        String rowEvaluacion= c.getString(c.getColumnIndexOrThrow(EAsignatura.FIELD_EVALUACION));
        float rowNota= c.getFloat(c.getColumnIndexOrThrow(EAsignatura.FIELD_NOTA));
        EAsignatura asig= new EAsignatura(rowName);
        asig.setId(Integer.parseInt(ID));
        asig.setEnlaces(rowEnlaces);
        asig.setEvaluacion(rowEvaluacion);
        asig.setNota(rowNota);
        return asig;
        }else
            return null;
    }

    public EExamen getEExamen(String ID){
        //String columnas[]={EExamen.FIELD_ID,EExamen.FIELD_NOMBRE,EExamen.FIELD_ASIGNATURA,EExamen.FIELD_FECHA,EExamen.FIELD_HORA,EExamen.FIELD_TIPOGUARDADO,EExamen.FIELD_CALENDARIOID,EExamen.FIELD_EVENTOID};

        Cursor c= db.query(EExamen.TABLE_NAME,columnasExamen,EExamen.FIELD_ID+"=?",new String[]{ID},null,null,null);
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
            String rowCalendarioNombre=c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_CALENDARIONOMBRE));
            String rowDescripcion=c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_DESCRIPCION));
            EExamen exa= new EExamen(rowName);
            exa.setAsignatura(rowAsig);
            exa.setFecha(rowFecha);
            exa.setHora(rowHora);
            exa.setTipoGuardado(rowTipoGuardado);
            exa.setCalendarioid(rowCalendarioId);
            exa.setEventoid(rowEventoId);
            exa.setCalendarionombre(rowCalendarioNombre);
            exa.setId(Integer.parseInt(ID));
            exa.setDescripcion(rowDescripcion);
            return exa;
        }else
            return null;


    }
    //CAMBIAR POR DB.DELETE
    public long deleteExamen(String ID,String nombre){
        int id=Integer.valueOf(ID);

        try {
            db.beginTransaction();
            db.delete(EExamen.TABLE_NAME, EExamen.FIELD_ID + "=" + id, null);
            deleteNotaExamen(nombre);
            db.setTransactionSuccessful();
        } catch(SQLException e) {
            // do some error handling
            return 0;
        } finally {
            db.endTransaction();
            return 1;
        }
      // return db.delete(EExamen.TABLE_NAME, EExamen.FIELD_ID + "=" + id, null);
       /* String consulta= "delete from examenes where _id="+id;
        db.execSQL(consulta);*/
    }
    public long deleteNotaExamen(String examen){
        String[] args = new String[]{examen};
        return db.delete(ENota.TABLE_NAME, ENota.FIELD_EXAMEN + "=?", args);
    }

    //CAMBIAR POR DB.DELETE
   /*17-04-15 al 8-05-15*/
    public int deleteAsignatura(int id,String asignatura){
       if(countExamenesAsig(asignatura)==0){
           /*String consulta= "delete from "+ EAsignatura.TABLE_NAME+" where "+ EAsignatura.FIELD_ID+" = "+id;
           db.execSQL(consulta);
           return 200;//se puede seguir asignatura borrada*/
           String[] args = new String[]{String.valueOf(id)};
           return db.delete(EAsignatura.TABLE_NAME, EAsignatura.FIELD_ID + "=?", args);
       }else{
           return -1;//no se puede borrar

       }
    }
    /*20-04-15*/
    public int updateAsignatura(EAsignatura asignatura){
        String id=String.valueOf(asignatura.getId());
        String[] args = new String[]{id};
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put(EAsignatura.FIELD_ENLACES,asignatura.getEnlaces());
        nuevoRegistro.put(EAsignatura.FIELD_NOTA, asignatura.getNota());
        nuevoRegistro.put(EAsignatura.FIELD_EVALUACION,asignatura.getEvaluacion());
        int res= db.update(EAsignatura.TABLE_NAME,nuevoRegistro,EAsignatura.FIELD_ID + " =?",args);
        return  res;
    }
    /*21-04-15*/
    public String getNotaExamen(String examen){
        String res="";
        String columnas[]={ENota.FIELD_EXAMEN,ENota.FIELD_ASIGNATURA,ENota.FIELD_NOTA,ENota.FIELD_NOTASOBRE};
        Cursor c= db.query(ENota.TABLE_NAME,columnas,ENota.FIELD_EXAMEN+"=?",new String[]{examen},null,null,null);
        if (c.moveToFirst()){
            float nota=c.getFloat(c.getColumnIndexOrThrow(ENota.FIELD_NOTA));
            float notaMax=c.getFloat(c.getColumnIndexOrThrow(ENota.FIELD_NOTASOBRE));
            res=nota+"/"+notaMax;
        }
        return res;
    }
    public ENota getNotaExamenObj(String examen){

        String columnas[]={ENota.FIELD_ID,ENota.FIELD_EXAMEN,ENota.FIELD_ASIGNATURA,ENota.FIELD_NOTA,ENota.FIELD_NOTASOBRE};
        Cursor c= db.query(ENota.TABLE_NAME,columnas,ENota.FIELD_EXAMEN+"=?",new String[]{examen},null,null,null);
        ENota notaExamen=new ENota();
        if (c.moveToFirst()){
            float nota=c.getFloat(c.getColumnIndexOrThrow(ENota.FIELD_NOTA));
            float notaMax=c.getFloat(c.getColumnIndexOrThrow(ENota.FIELD_NOTASOBRE));
            String asig=c.getString(c.getColumnIndexOrThrow(ENota.FIELD_ASIGNATURA));
            int id=c.getInt(c.getColumnIndexOrThrow(ENota.FIELD_ID));
            notaExamen.setExamen(examen);
            notaExamen.setNota(nota);
            notaExamen.setNota_sobre(notaMax);
            notaExamen.setAsignatura(asig);
            notaExamen.setId(id);
        }
        return notaExamen;
    }
    /*21-04-15*/
    public int updateExamen(EExamen examen,ENota nota){

        String e=String.valueOf(nota.getExamen());
        String[] args2= new String[]{e};
        ContentValues nuevoRegistro = new ContentValues();
        nuevoRegistro.put(EExamen.FIELD_ASIGNATURA,examen.getAsignatura());
        nuevoRegistro.put(EExamen.FIELD_FECHA,examen.getFecha());
        nuevoRegistro.put(EExamen.FIELD_HORA,examen.getHora());
        nuevoRegistro.put(EExamen.FIELD_CALENDARIOID,examen.getCalendarioid());
        nuevoRegistro.put(EExamen.FIELD_EVENTOID,examen.getEventoid());
        nuevoRegistro.put(EExamen.FIELD_TIPOGUARDADO,examen.getTipoGuardado());
        nuevoRegistro.put(EExamen.FIELD_CALENDARIONOMBRE,examen.getCalendarionombre());
        nuevoRegistro.put(EExamen.FIELD_DESCRIPCION,examen.getDescripcion());
        ContentValues nuevoRegistroNota = new ContentValues();
        nuevoRegistroNota.put(ENota.FIELD_ASIGNATURA,examen.getAsignatura());
        nuevoRegistroNota.put(ENota.FIELD_NOTA,nota.getNota());
        nuevoRegistroNota.put(ENota.FIELD_NOTASOBRE,nota.getNota_sobre());
        int res2=0;
        int res= db.update(EExamen.TABLE_NAME,nuevoRegistro,EExamen.FIELD_ID + " ="+examen.getId(),null);
        if(res>0){
            res2=db.update(ENota.TABLE_NAME,nuevoRegistroNota,ENota.FIELD_EXAMEN+"=?",args2);
        }
        return  res+res2;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {

            db.execSQL(EAsignatura.CREATE_DB_TABLE);
            db.execSQL(EExamen.CREATE_DB_TABLE);
            // db.execSQL(ENota.CREATE_DB_TABLE);
            upgrade_2(db);
            upgrade_3(db);
            db.execSQL("PRAGMA foreign_keys = ON;");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if(oldVersion == 1 && newVersion >= 2){
            upgrade_2(db);
            //upgrade_2_modificar();
        }
        if (oldVersion>=1 && newVersion>=3){
            upgrade_3(db);
        }
    }
    /*22-04-15*/
    private void upgrade_2(SQLiteDatabase db)
    {
        //
        // Upgrade versión 2: Agregar nota, tabla de notas y el nombre del calendario
        //

            String SQLUpdateV2 = "ALTER TABLE "+ EAsignatura.TABLE_NAME+" ADD COLUMN "+ EAsignatura.FIELD_NOTA +" float";
            String SQLUpdateV2Examen = "ALTER TABLE "+ EExamen.TABLE_NAME+" ADD COLUMN "+ EExamen.FIELD_CALENDARIONOMBRE +" text";
            db.execSQL(SQLUpdateV2);
            db.execSQL(SQLUpdateV2Examen);
            db.execSQL(ENota.CREATE_DB_TABLE);


    }
    /*11-05-15*/
    private void upgrade_3(SQLiteDatabase db)
    {
        //
        // Upgrade versión 3: Agregar descripción
        //
        String SQLUpdateV3Examen = "ALTER TABLE "+ EExamen.TABLE_NAME+" ADD COLUMN "+ EExamen.FIELD_DESCRIPCION +" text";
        db.execSQL(SQLUpdateV3Examen);
    }
    /*22-04-15*/
    public void actualizar_schema2(){

        Cursor cAsig=getAsignaturasCursor();
        if (cAsig.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                int idAsig=cAsig.getInt(cAsig.getColumnIndexOrThrow(EAsignatura.FIELD_ID));
                ContentValues nuevoRegistroAsig = new ContentValues();
                nuevoRegistroAsig.put(EAsignatura.FIELD_NOTA,100);
                db.update(EAsignatura.TABLE_NAME,nuevoRegistroAsig,EAsignatura.FIELD_ID+" ="+idAsig,null);
            } while(cAsig.moveToNext());
        }
        Cursor c= getExamenesCursor();
        //Nos aseguramos de que existe al menos un registro
        if (c.moveToFirst()) {
            //Recorremos el cursor hasta que no haya más registros
            do {
                ENota nota= new ENota();
                String examen=c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_NOMBRE));
                String asig=c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_ASIGNATURA));
                String tipo=c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_TIPOGUARDADO));
                String calendarId=c.getString(c.getColumnIndexOrThrow(EExamen.FIELD_CALENDARIOID));
                int id=c.getInt(c.getColumnIndexOrThrow(EExamen.FIELD_ID));
                nota.setExamen(examen);
                nota.setAsignatura(asig);
                nota.setNota(0);
                nota.setNota_sobre(0);
                db.insert(ENota.TABLE_NAME,null,generarValoresNota(nota));
                if(tipo.equals("Ambos")){
                    new CallAPI().execute(calendarId,String.valueOf(id) );
                }
            } while(c.moveToNext());
        }

        Log.i(this.getClass().toString(), "Actualización versión 2 finalizada");

    }
    /*22-04-15*/
    private class CallAPI  extends AsyncTask<String, String, Void> {


        @Override
        protected Void doInBackground(String... params) {
            try {
                String calId=params[0];
                int id=Integer.parseInt(params[1]);
                com.google.api.services.calendar.Calendar client;
                client=ActividadPrincipal.client;
                com.google.api.services.calendar.model.Calendar cal =client.calendars().get(calId).execute();
                String calNombre= cal.getSummary();
                ContentValues nuevoRegistro = new ContentValues();
                nuevoRegistro.put(EExamen.FIELD_CALENDARIONOMBRE,calNombre);
                db.update(EExamen.TABLE_NAME,nuevoRegistro,EExamen.FIELD_ID +" ="+id,null);
            } catch (IOException e) {

            }
            return null;
        }
    }


}
