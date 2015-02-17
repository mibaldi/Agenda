package BD;

/**
 * Created by Mikel on 03/02/2015.
 */
public class EAsignatura {
    public static final String TABLE_NAME= "asignaturas";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NOMBRE = "nombre";
    public static final String FIELD_ENLACES = "enlaces";
    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
                                                  FIELD_ID  + " integer primary key autoincrement,"+
                                                  FIELD_NOMBRE + " text unique,"+
                                                  FIELD_ENLACES + " text"
                                                  + " );";
    /*public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
                                                  FIELD_ID  + " integer,"+
                                                  FIELD_NOMBRE + " text,"+
                                                  FIELD_ENLACES + " text,"+
                                                  "PRIMARY KEY ("+FIELD_ID+","+FIELD_NOMBRE +")"
                                                  + " );";*/
    private int id;
    private String nombre;
    private String enlaces;

    public EAsignatura(String nombre) {
        this.nombre = nombre;
    }

    public EAsignatura() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEnlaces() {
        return enlaces;
    }

    public void setEnlaces(String enlaces) {
        this.enlaces = enlaces;
    }
}
