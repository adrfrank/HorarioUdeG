package com.example.macmini1_cuceimobile.horarioudeg;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/*
*   Actividad que tiene el horario de la semana
* */
public class HorarioActivity extends Activity implements ExpandableListView.OnChildClickListener {

    private Button boton;
    private ExpandableListView expandableList;
    private ArrayList<String> parentItems = new ArrayList<String>();
    private ArrayList<Object> childItems = new ArrayList<Object>();
    private SQLiteDatabase database;
    private String img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_horario);
        expandableList = (ExpandableListView) findViewById(R.id.list);
        expandableList.setDividerHeight(2);
        expandableList.setGroupIndicator(null);
        expandableList.setClickable(true);


        setGroupParents();
        setChildData();

        final MyExpandableAdapter adapter = new MyExpandableAdapter(parentItems,
                childItems, this);
        adapter.setInflater(
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                this);
        expandableList.setAdapter(adapter);

        for(int i=0; i < adapter.getGroupCount(); i++)
            expandableList.expandGroup(i);

        ImageView  botnmenu =(ImageView) findViewById(R.id.botnmenu);

        final DrawerLayout main2Layout = (DrawerLayout) findViewById(R.id.drawer);
        //Activiti_info

       /* expandableList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(HorarioActivity.this,"click",Toast.LENGTH_LONG).show();
                return true;
            }
        });
*/
        expandableList.setOnChildClickListener(this);
        botnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                main2Layout.openDrawer(Gravity.LEFT);
            }
        });

        LinearLayout linearLayoutOculto = (LinearLayout) findViewById(R.id.linearLayout);
        ListView listView = (ListView) findViewById(R.id.lista_main2);
        ArrayList<String> arrayList = new ArrayList<String>();
        arrayList.add("");

         listView.setAdapter(new AdaptadorHorario(this, arrayList, linearLayoutOculto));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.horario, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();
        if (id == R.id.cerrarSesion) {
            cerrarSesion();
            return true;
        }
		/*
		if (id == R.id.cambiarVista) {
			cambiarVista();
			return true;
		}
		*/

        if (id == R.id.agregarAlarmas) {
            try{
                agregarRecordatorios();

            }catch(Exception e){
                Toast.makeText(this, "No se pudieron agregar las Alarmas",
                        Toast.LENGTH_SHORT).show();
                Log.e("Error ", e.toString());
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void cerrarSesion() {
        Conexion db = new Conexion(this, "default", null, 1);
        //database = db.getWritableDatabase();
        db.deleteData();
        //TODO: Delete this if it is not necesary
        //database.execSQL("DELETE FROM alumno");
        //database.execSQL("DELETE FROM materias");
        //database.close();
        db.close();
        Log.i("SIIAU", "Cerrar Sesion");
        System.out.println("Borrado");
        Intent i = new Intent(this, LoginActivity.class);
        startActivity(i);
        finish();
    }
    /*
    private void cambiarVista(){
        Intent i = new Intent(this, HorarioSemanal.class);
        startActivity(i);
    }*/
    public void setGroupParents() {// Los dias de la semana
        parentItems.add("Datos del Alumno");
        parentItems.add("Lunes");
        parentItems.add("Martes");
        parentItems.add("Miercoles");
        parentItems.add("Jueves");
        parentItems.add("Viernes");
        parentItems.add("Sabado");
    }

    public void setChildData() {// las materias
        ArrayList<String> alumno = new ArrayList<String>();
        ArrayList<String> lunes = new ArrayList<String>();// Lunes
        ArrayList<String> martes = new ArrayList<String>();// Martes
        ArrayList<String> miercoles = new ArrayList<String>();// Miercoles
        ArrayList<String> jueves = new ArrayList<String>();// Jueves
        ArrayList<String> viernes = new ArrayList<String>();// Viernes
        ArrayList<String> sabado = new ArrayList<String>();// Sabado

        alumno = getAlumno();
        lunes = getDia(7, "L");
        martes = getDia(8, "M");
        miercoles = getDia(9, "I");
        jueves = getDia(10, "J");
        viernes = getDia(11, "V");
        sabado = getDia(12, "S");

        if (lunes.isEmpty()) {
            lunes.add("No tienes clases");
        }
        if (martes.isEmpty()) {
            martes.add("No tienes clases");
        }
        if (miercoles.isEmpty()) {
            miercoles.add("No tienes clases");
        }
        if (jueves.isEmpty()) {
            jueves.add("No tienes clases");
        }
        if (viernes.isEmpty()) {
            viernes.add("No tienes clases");
        }
        if (sabado.isEmpty()) {
            sabado.add("No tienes clases");
        }
        childItems.add(alumno);

        childItems.add(lunes);
        childItems.add(martes);
        childItems.add(miercoles);
        childItems.add(jueves);
        childItems.add(viernes);
        childItems.add(sabado);

    }

    private ArrayList<String> getAlumno() {
        ArrayList<String> arreglo = new ArrayList<String>();
        Conexion db = new Conexion(this, "default", null, 1);
        database = db.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM alumno", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                arreglo.add("Codigo: " + c.getString(1) + "\n Nombre: "+ c.getString(2)+ "\n"+ c.getString(3) + "\n Carrera:  "+ c.getString(4));// codigo
               // arreglo.add(c.getString(2));// nombre
                //arreglo.add(c.getString(3));// centro
                //arreglo.add(c.getString(4));// carrera
            } while (c.moveToNext());
        }
        c.close();
        database.close();
        db.close();
        return arreglo;
    }

    private ArrayList<String> getDia(int d, String dia) {
        ArrayList<String> arreglo = new ArrayList<String>();
        Conexion db = new Conexion(this, "default", null, 1);
        database = db.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM materias order by horario",
                null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                if (c.getString(d).equalsIgnoreCase(dia)) {
					/*
					 * System.out.println("3 " + c.getString(3));
					 * System.out.println("6 " + c.getString(6));
					 * System.out.println("13 " + c.getString(13));
					 * System.out.println("14 " + c.getString(14));
					 */arreglo.add("Materia: "+c.getString(3) + "\nHorario: " + c.getString(6) + "\nEdificio: "
                            + c.getString(13) + " \nAula: " + c.getString(14)+"\nProf: "+c.getString(15));
                }
            } while (c.moveToNext());
        }
        c.close();
        database.close();
        db.close();
        return arreglo;
    }

    private void agregarRecordatorio(String materia, String horario,
                                     String location, String inicio, String fin, String dia) {

        Calendar beginTime;
        Calendar endTime;

        Intent calendarIntent = new Intent(Intent.ACTION_INSERT,
                CalendarContract.Events.CONTENT_URI);

        int anio = Integer.parseInt(getAnio(inicio));
        int mes = Integer.parseInt(getMes(inicio));
        int diaN = Integer.parseInt(getDia(inicio));
        String[] hr = horario.split("-");
        int hora = Integer.parseInt(getHora(hr[0]));
        int minuto = Integer.parseInt(getMinuto(hr[0]));

        beginTime = Calendar.getInstance();
        beginTime.set(anio, mes, diaN, hora, minuto);

        hora = Integer.parseInt(getHora(hr[1]));
        minuto = Integer.parseInt(getMinuto(hr[1]));

        endTime = Calendar.getInstance();
        endTime.set(anio, mes, diaN, hora, minuto);

        anio = Integer.parseInt(getAnio(fin));
        mes = Integer.parseInt(getMes(fin));
        diaN = Integer.parseInt(getDia(fin));

        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME,
                beginTime.getTimeInMillis() - (60000 * 5));// 60000
        calendarIntent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME,
                endTime.getTimeInMillis());
        calendarIntent.putExtra(CalendarContract.Events.TITLE, materia);// Materia
        calendarIntent.putExtra(CalendarContract.Events.EVENT_LOCATION, location);// Edificio
        calendarIntent.putExtra(CalendarContract.Events.RRULE,
                frecuenciaRepeticion(anio, mes, diaN, dia));

        calendarIntent.putExtra(
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME, "SIIAU");

        startActivity(calendarIntent);
    }

    public void agregarRecordatorios() {
        Conexion db = new Conexion(this, "default", null, 1);
        database = db.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM materias order by horario",
                null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                String materia = c.getString(3);// Materia
                String horario = c.getString(6);// Horario
                String edificio = c.getString(13);// Edificio
                String aula = c.getString(14);// Aula
                String fecInicial = c.getString(16);// Fecha Inicial
                String fecFinal = c.getString(17);// Fecha Final
                String dia = c.getString(7) + c.getString(8) + c.getString(9)
                        + c.getString(10) + c.getString(11) + c.getString(12);
                agregarRecordatorio(materia, horario, edificio + " " + aula,
                        fecInicial, fecFinal, dia);

            } while (c.moveToNext());
        }
        c.close();
        database.close();
        db.close();
    }

    private String getAnio(String horario) {
        String aux = "";
        String[] resultado = horario.split("-");
        aux = resultado[2];
        aux = rellena(aux);
        return aux;
    }

    private String getMes(String horario) {
        String aux = "";
        String[] resultado = horario.split("-");
        aux = resultado[1];
        aux = convienteANumero(aux);
        return aux;
    }

    private String getDia(String horario) {
        String aux = "";
        String[] resultado = horario.split("-");
        aux = resultado[0];
        return aux;
    }

    private String getHora(String horario) {
        String aux;
        aux = horario.substring(0, 2);
        return aux;
    }

    private String getMinuto(String horario) {
        String aux;
        aux = horario.substring(2, 4);
        return aux;
    }

    private String convienteANumero(String cad) {
        String aux = "";
        if (cad.equalsIgnoreCase("ene")) {
            aux = "0";
        }
        if (cad.equalsIgnoreCase("feb")) {
            aux = "1";
        }
        if (cad.equalsIgnoreCase("mar")) {
            aux = "2";
        }
        if (cad.equalsIgnoreCase("abr")) {
            aux = "3";
        }
        if (cad.equalsIgnoreCase("may")) {
            aux = "4";
        }
        if (cad.equalsIgnoreCase("jun")) {
            aux = "5";
        }
        if (cad.equalsIgnoreCase("jul")) {
            aux = "6";
        }
        if (cad.equalsIgnoreCase("ago")) {
            aux = "7";
        }
        if (cad.equalsIgnoreCase("sep")) {
            aux = "8";
        }
        if (cad.equalsIgnoreCase("oct")) {
            aux = "8";
        }
        if (cad.equalsIgnoreCase("nov")) {
            aux = "10";
        }
        if (cad.equalsIgnoreCase("dic")) {
            aux = "11";
        }
        if(aux.equals("")){
            int x=Integer.parseInt(cad);
            x=x-1;
            aux=""+x;
        }
        return aux;
    }

    private String rellena(String cad) {
        String aux = "";
        if (cad.length() < 3) {
            aux = "20" + cad;
        }else{
            aux=cad;
        }
        return aux;
    }

    private String frecuenciaRepeticion(int anio, int mes, int diaN, String dia) {
        String cad = "FREQ=WEEKLY;UNTIL=" + anio + getMesS(mes) + getDiaS(diaN)
                + "T005954Z;WKST=SU;BYDAY=" + getDiaSemana(dia);
        // FREQ=WEEKLY;UNTIL=20140614T005954Z;WKST=SU;BYDAY=TU");
        return cad;
    }

    private String getMesS(int mes) {
        String aux = "";
        if (mes + 1 < 10) {
            aux = "0" + (mes + 1);
        } else {
            aux = "" + (mes + 1);
        }
        return aux;
    }

    private String getDiaS(int dia) {
        String aux = "";
        if (dia < 10) {
            aux = "0" + dia;
        } else {
            aux = "" + dia;
        }
        return aux;
    }

    private String getDiaSemana(String dia) {
        String aux = "", aux2 = "";
        dia = dia.replace("{", "");
        dia = dia.replace("}", "");
        for (int i = 0; i < dia.length(); i++) {
            if (dia.charAt(i) == 'L') {
                aux = "MO";
            }
            if (dia.charAt(i) == 'M') {
                aux = "TU";
            }
            if (dia.charAt(i) == 'I') {
                aux = "WE";
            }
            if (dia.charAt(i) == 'J') {
                aux = "TH";
            }
            if (dia.charAt(i) == 'V') {
                aux = "FR";
            }
            if (dia.charAt(i) == 'S') {
                aux = "SA";
            }
            if (aux2.equals("")) {
                aux2 = aux;
            } else {
                aux2 = aux2 + "," + aux;
            }
        }
        return aux2;
    }

    private int RESULT_LOAD_IMAGE = 1;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = { MediaStore.Images.Media.DATA };

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            ImageView imageView = (ImageView) findViewById(R.id.imgUsuario);
            imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));

        }

    }



    public void CambiarImg (View view) {

        Intent i = new Intent(
                Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    @Override
    public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
        if (parent.isGroupExpanded(groupPosition))
        Toast.makeText(HorarioActivity.this,"click",Toast.LENGTH_LONG).show();
       // Log.d("",childItems.get(parentItems.get(groupPosition)).get(childPosition));
        return true;
        //return false;
    }



/*
    public String getPath(Uri uri) {
        // just some safety built in
        if( uri == null ) {
            // TODO perform some logging or show user feedback
            return null;
        }
        // try to retrieve the image from the media store first
        // this will only work for images selected from gallery
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if( cursor != null ){
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        }
        // this is our fallback here
        return uri.getPath();
    }

//Base de datos
database.rawQuery("UPDATE imagen SET img='"+filePathColumn+"' " +
                    " WHERE alumno.nombre=imagen.alumno FROM alumno, imagen ", null);







*/


}
