package BD;

/**
 * Created by Mikel on 03/02/2015.
 */
public class EAsignatura {
    public static final String TABLE_NAME = "asignaturas";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NOMBRE = "nombre";
    public static final String FIELD_ENLACES = "enlaces";
    public static final String FIELD_EVALUACION = "evaluacion";
    public static final String FIELD_NOTA = "nota";
    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + "( " +
            FIELD_ID + " integer primary key autoincrement," +
            FIELD_NOMBRE + " text unique not null," +
            FIELD_ENLACES + " text," +
            FIELD_EVALUACION + " text" +
            FIELD_NOTA + " integer"
            + " );";
    private int id;
    private String nombre;
    private String enlaces;
    private String evaluacion;


    private int nota;

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

    public String getEvaluacion() {
        return evaluacion;
    }

    public void setEvaluacion(String evaluacion) {
        this.evaluacion = evaluacion;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
