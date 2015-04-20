package BD;

/**
 * Created by Mikel on 03/02/2015.
 */
public class ENota{
    public static final String TABLE_NAME= "notas";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_EXAMEN = "examen";
    public static final String FIELD_ASIGNATURA = "asignatura";
    public static final String FIELD_NOTA = "nota";
    public static final String FIELD_NOTASOBRE = "nota_sobre";
    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + " ( " +
                                                  FIELD_ID  + " integer primary key autoincrement,"+
                                                   FIELD_EXAMEN + " text not null,"+
                                                  FIELD_ASIGNATURA +" text not null,"+
                                                    FIELD_NOTA +" integer,"+
                                                    FIELD_NOTASOBRE +" integer,"+
                                                    "unique ("+FIELD_EXAMEN+", "+FIELD_ASIGNATURA+")"
                                                  + " );";
    private int id;
    private String examen;
    private String asignatura;
    private int nota;
    private int nota_sobre;

    public ENota(String examen, String asignatura, int nota,int nota_sobre) {
        this.examen = examen;
        this.asignatura = asignatura;
        this.nota = nota;
        this.nota_sobre=nota_sobre;
    }

    public ENota() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getExamen() {
        return examen;
    }

    public void setExamen(String examen) {
        this.examen = examen;
    }

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }

    public int getNota_sobre() {
        return nota_sobre;
    }

    public void setNota_sobre(int nota_sobre) {
        this.nota_sobre = nota_sobre;
    }
}
