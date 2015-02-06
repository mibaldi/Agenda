package BD;

/**
 * Created by Mikel on 03/02/2015.
 */
public class EAsignatura {
    public static final String TABLE_NAME= "asignaturas";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NOMBRE = "nombre";
    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
                                                  FIELD_ID  + " integer primary key autoincrement,"+
                                                  FIELD_NOMBRE + " text"
                                                  + " );";
    private int id;
    private String nombre;

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
}
