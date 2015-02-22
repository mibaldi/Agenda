package BD;

/**
 * Created by Mikel on 03/02/2015.
 */
public class EExamen {
    public static final String TABLE_NAME= "examenes";
    public static final String FIELD_ID = "_id";
    public static final String FIELD_NOMBRE = "nombre";
    public static final String FIELD_ASIGNATURA = "asignatura";
    public static final String FIELD_FECHA = "fecha";
    public static final String FIELD_HORA = "hora";
    public static final String CREATE_DB_TABLE = "create table " + TABLE_NAME + " ( " +
                                                  FIELD_ID  + " integer primary key autoincrement,"+
                                                  FIELD_NOMBRE + " text unique,"+
                                                  FIELD_ASIGNATURA +" text,"+
                                                  FIELD_FECHA +" text,"+
                                                  FIELD_HORA +" text,"+
                                                  "FOREIGN KEY ( "+FIELD_ASIGNATURA+" ) REFERENCES "+EAsignatura.TABLE_NAME+" ( "+EAsignatura.FIELD_NOMBRE+" )"
                                                  + " );";
    private int id;
    private String nombre;
    private String asignatura;
    private String fecha;
    private String hora;

    public EExamen(String nombre) {
        this.nombre = nombre;
    }

    public EExamen() {
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

    public String getAsignatura() {
        return asignatura;
    }

    public void setAsignatura(String asignatura) {
        this.asignatura = asignatura;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
