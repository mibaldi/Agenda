package com.mikel.agenda;
import android.app.ListActivity;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mikel.agenda.R;

public class Lista_Asignaturas extends ListActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Simulamos que extraemos los datos de la base de datos a un cursor
        String[] columnasBD = new String[] {"_id", "imagen", "textoSuperior"};
        MatrixCursor cursor = new MatrixCursor(columnasBD);
        cursor.addRow(new Object[] {"0", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"1", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"2", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"3", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"4", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"5", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"6", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"7", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"8", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"9", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"10", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"11", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"12", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"13", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"14", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"15", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"16", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"17", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"18", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"19", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"20", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"21", R.drawable.ic_launcher, "PFG"});
        cursor.addRow(new Object[] {"22", R.drawable.ic_launcher, "ABD"});
        cursor.addRow(new Object[] {"23", R.drawable.ic_launcher, "PFG"});
        //Añadimos los datos al Adapter y le indicamos donde dibujar cada dato en la fila del Layout
        String[] desdeEstasColumnas = {"imagen", "textoSuperior"};
        int[] aEstasViews = {R.id.imageView_imagen, R.id.textView_superior};
        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, R.layout.activity_lista__asignaturas, cursor, desdeEstasColumnas, aEstasViews, 0);

        ListView listado = getListView();
        listado.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_lista__asignaturas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onListItemClick(ListView lista, View view, int posicion, long id) {
        // Hacer algo cuando un elemento de la lista es seleccionado
        TextView textoTitulo = (TextView) view.findViewById(R.id.textView_superior);

        CharSequence texto = "Seleccionado: " + textoTitulo.getText();
        Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();
    }

}