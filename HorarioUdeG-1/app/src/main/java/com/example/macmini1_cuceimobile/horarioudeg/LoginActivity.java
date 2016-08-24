package com.example.macmini1_cuceimobile.horarioudeg;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


/*
*  Activity for login manage
* */
public class LoginActivity extends Activity{
    private Button boton;
    private Spinner spinner;
    private String usuario;
    private String nip;
    private Switch etiCalendario;
    private String nombre;
    private String carrera;
    private String centro;
    private SQLiteDatabase database;
    private Object etiquetacalendario;
    private String d1,d2,d3;

    private TextView textView,textView2,txtAlumno,txtCalendario,txtStatus,txtCarrera,txtUni;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Universidad de Guadalajara");
        if (conectado()) {
            enviarAHorario();
        }else{
            spinner = (Spinner) findViewById(R.id.spinner1);
            etiCalendario = (Switch) findViewById(R.id.SCalendario);
            boton = (Button) findViewById(R.id.botonIngresar);
            addItemsOnSpinner();}

        textView = (TextView) findViewById(R.id.textView);
        textView2 = (TextView) findViewById(R.id.textView2);

/*
        txtAlumno = (TextView) findViewById(R.id.txtAlumno);
        txtCarrera = (TextView) findViewById(R.id.txtCarrera);
        txtUni = (TextView) findViewById(R.id.txtUni);

  */
        ImageView bmenu =(ImageView) findViewById(R.id.btnmenu);

        //Drawer
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer2);

        bmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        //TODO: Verificar si esto se necesita

//        LinearLayout linearLayoutOculto = (LinearLayout) findViewById(R.id.layout_menu);
//        ListView listView = (ListView) findViewById(R.id.lista_drawer);
//        ArrayList<String> arrayList = new ArrayList<String>();
//        arrayList.add("");
//        listView.setAdapter(new AdaptadorDrawer(this, arrayList, linearLayoutOculto));




        //





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;

    }


    public void onCheckedChanged(View view) {
        boolean isChecked = ((Switch) view).isChecked();
        if(isChecked) {
            spinner.setVisibility(View.VISIBLE);
            textView.setVisibility(View.VISIBLE);
            textView2.setVisibility(View.VISIBLE);
            etiCalendario.setVisibility(View.VISIBLE);
        }
        else{
            spinner.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
            textView2.setVisibility(View.INVISIBLE);
            etiCalendario.setVisibility(View.INVISIBLE);
        }}


    public void Calendario (View view) {
        boolean isChecked = ((Switch) view).isChecked();
        if(isChecked) {
            etiCalendario.setText("Calendario B");
        }
        else{
            etiCalendario.setText("Calendario A");

        }
    }





    private void enviarAHorario() {
        Intent i = new Intent(this, HorarioActivity.class);
        startActivity(i);
        finish();
    }


    private boolean conectado() {
        boolean bandera = false;
        Conexion db = new Conexion(this, "default", null, 1);
        database = db.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM alumno", null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            bandera = true;
        }
        c.close();
        database.close();
        db.close();
        return bandera;
    }


    private void addItemsOnSpinner(){
        Calendar beginTime = Calendar.getInstance();
        int anio = beginTime.get(Calendar.YEAR);
        List<String> list = new ArrayList<String>();
        for(int i=0;i<5;i++){
            list.add(""+(anio-i));
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
    }

    private void exit() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void login(View view) {

        if (!camposValidos()) {
            return;
        }
        if (!isOnline()) {
            return;
        }
        peticionWeb();

    }


    private boolean camposValidos() {

        TextView textoUsuario = (TextView) findViewById(R.id.textoUsuario);
        TextView textoPassword = (TextView) findViewById(R.id.textoPassword);

        usuario = textoUsuario.getText().toString();
        nip = textoPassword.getText().toString();
        etiquetacalendario = etiCalendario.getText().toString();

        if (usuario.equals("")) {
            Toast.makeText(this, "Ingrese su codigo", Toast.LENGTH_SHORT)
                    .show();
            return false;
        }
        if (nip.equals("")) {
            Toast.makeText(this, "Ingrese su NIP", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    public boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        Toast.makeText(this, "Comprueba tu conexion a Internet",
                Toast.LENGTH_SHORT).show();
        return false;
    }

    private void peticionWeb() {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("p_codigo_c", usuario));
        nameValuePairs.add(new BasicNameValuePair("p_clave_c", nip));
        nameValuePairs.add(new BasicNameValuePair("formato", ""));
        if (!etiquetacalendario.equals("")) {
            nameValuePairs.add(new BasicNameValuePair("pciclo", getCalendario()));
        }else{
            nameValuePairs.add(new BasicNameValuePair("pciclo", ""));
        }

        new ConexionWeb().execute(nameValuePairs);



    }

    private String getCalendario(){
        String aux="";
        aux=spinner.getSelectedItem().toString();
        if(etiCalendario.getText().toString().equals("Calendario A")){
            aux=aux+"10";
        }else{
            aux=aux+"20";
        }

        return aux;
    }


    private String conectar(List<NameValuePair> nameValuePairs) {
        String aux = "";
        String url = "http://148.202.248.34/alumnos.php";

        if(!nameValuePairs.get(3).getValue().equals("")){
            url="http://148.202.248.34/alumnosHA.php";
        }
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            // HttpPost httpPost = new
            // HttpPost("http://localhost/siiau1/profesores.php");
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();

            httpResponse.getStatusLine();

            if (httpEntity != null) {
                aux = EntityUtils.toString(httpEntity);
            } else {
                aux = "3Error";
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return aux;
    }

    private boolean loginValido(String aux) {
        boolean bandera = true;
        if (aux.equalsIgnoreCase("1Error")) {
            Toast.makeText(this, "Codigo o NIP incorrecto.", Toast.LENGTH_LONG)
                    .show();
            System.out.println("Error 1");
            Log.e("SIIAU Login", "Codigo o NIP incorrecto");
            bandera = false;
        } else {
            if (aux.equalsIgnoreCase("2Error")) {
                Toast.makeText(this, "No se pudo obtener la carrera",
                        Toast.LENGTH_SHORT).show();
                Log.e("SIIAU Login", "No se pudo obtener la carrera");
                bandera = false;
            } else {
                if (aux.equalsIgnoreCase("3Error")) {
                    Toast.makeText(this, "Regreso cadena vacia",
                            Toast.LENGTH_SHORT).show();
                    Log.e("SIIAU Login", "Regreso cadena vacia");
                    bandera = false;
                }
            }
        }
        return bandera;
    }

    private void procesaJson(String aux) {
        String ant="",nue="";
        try {
            JSONObject jObj = new JSONObject(aux);
            JSONArray arreglo = jObj.getJSONArray("materias");
            for (int i=0;i<arreglo.length();i++) {
                JSONObject materia = arreglo.getJSONObject(i);
                try {
                    nue=materia.getString("materia");
                }catch(JSONException e){
                    nue=ant;
                }
                guardarDatosM(materia,nue);
                ant=nue;
            }
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
    }


    private void guardarDatosM(JSONObject materia,String aux){
        Conexion db = new Conexion(this, "default", null, 1);
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();

        try {values.put("nrc", materia.getString("nrc"));}catch(JSONException e){values.put("nrc","");}
        try {values.put("clave", materia.getString("clave") );}catch(JSONException e){values.put("clave","");}
        try {values.put("materia", materia.getString("materia") );}catch(JSONException e){values.put("materia",aux);}
        try {values.put("seccion", materia.getString("seccion") );}catch(JSONException e){values.put("seccion","");}
        try {values.put("creditos", materia.getString("creditos"));}catch(JSONException e){values.put("creditos","");}
        try {values.put("horario", materia.getString("horario"));}catch(JSONException e){values.put("horarios","");}
        try {values.put("lunes", materia.getString("lunes"));}catch(JSONException e){values.put("lunes","");}
        try {values.put("martes", materia.getString("martes"));}catch(JSONException e){values.put("martes","");}
        try {values.put("miercoles",  materia.getString("miercoles"));}catch(JSONException e){values.put("miercoles","");}
        try {values.put("jueves", materia.getString("jueves"));}catch(JSONException e){values.put("jueves","");}
        try {values.put("viernes", materia.getString("viernes"));}catch(JSONException e){values.put("viernes","");}
        try {values.put("sabado", materia.getString("sabado"));}catch(JSONException e){values.put("sabado","");}

        try {values.put("edificio", materia.getString("edificio"));}catch(JSONException e){values.put("edificio","");}
        try {values.put("aula", materia.getString("aula"));}catch(JSONException e){values.put("aula","");}
        try {values.put("profesor", materia.getString("profesor"));}catch(JSONException e){values.put("profesor","");}
        try {values.put("fecha_inicio", materia.getString("fecha_inicio"));}catch(JSONException e){values.put("fecha_inicio","");}
        try {values.put("fecha_fin", materia.getString("fecha_fin"));}catch(JSONException e){values.put("fecha_fin","");}

        database.insert("materias", null, values);
        database.close();
        db.close();

    }

    private void guardarDatosA(String aux){
        procesaJsonA(aux);
        Conexion db = new Conexion(this,"default",null,1);
        SQLiteDatabase database = db.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("codigo",usuario);
        values.put("pass","");
        values.put("nombre", nombre);
        values.put("centro", centro);
        values.put("carrera", carrera);
        database.insert("alumno", null, values);

        database.close();
        db.close();
    }

    private void procesaJsonA(String aux){
        try {
            JSONObject jObj = new JSONObject(aux);
            JSONObject alumno = jObj.getJSONObject("alumno");
            //String codigo=alumno.getString("codigo");
            nombre=alumno.getString("nombre");
            carrera=alumno.getString("carrera");
            centro=alumno.getString("centro");
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }

    }

    private boolean procesaJsonE(String aux){
        try {
            JSONObject jObj = new JSONObject(aux);
            Log.e("SIIAU", jObj.getString("error"));
            Toast.makeText(this, " No tienes horarios activos.", Toast.LENGTH_LONG)
                    .show();
            return true;
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        }
        return false;
    }


    private boolean Test(){

        boolean bandera = false;
        Conexion db = new Conexion(this, "default", null, 1);
        database = db.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM alumno", null);

        if (c.getCount() > 0) {
            c.moveToFirst();

            txtAlumno.setText(""+database.rawQuery("SELEC nombre FROM alumno ", null));
            txtCarrera.setText(""+carrera);
            txtUni.setText(""+centro);


            bandera = true;
        }

        c.close();
        database.close();
        db.close();
        return bandera;


    }


    class ConexionWeb extends AsyncTask<List<NameValuePair>, Void, String> {
        @Override
        protected void onPreExecute() {
            boton.setVisibility(View.INVISIBLE);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(List<NameValuePair>... params) {
            // TODO Auto-generated method stub
            return conectar(params[0]);

        }

        @Override
        protected void onPostExecute(String aux) {
            System.out.println(aux);
            if (loginValido(aux)) {
                Intent i = new Intent(getApplication(), HorarioActivity.class);
                guardarDatosA(aux);

                if(!procesaJsonE(aux)){
                    procesaJson(aux);
                }
                startActivity(i);
                finish();
            }else{
                boton.setVisibility(View.VISIBLE);
            }

        }





    }

    /*
    public void ver(){

        Conexion db = new Conexion(this, "default", null, 1);
        database = db.getReadableDatabase();
        Cursor c = database.rawQuery("SELECT * FROM alumno", null);
        if (c.getCount() > 0) {
            c.moveToFirst();

            txtAlumno.setText(""+database.rawQuery("SELEC nombre FROM alumno ", null));
            txtCarrera.setText(""+database.rawQuery("SELEC carrera FROM alumno ", null));
            txtUni.setText(""+database.rawQuery("SELEC centro FROM alumno ", null));
        }
        c.close();
        database.close();
        db.close();


        }

        //Calendario de Admicion, Status , Carrera , Centro Universitario


            txtAlumno.setText(""+database.rawQuery("SELEC nombre FROM alumno ", null));
            txtCarrera.setText(""+database.rawQuery("SELEC carrera FROM alumno ", null));
            txtUni.setText(""+database.rawQuery("SELEC centro FROM alumno ", null));

String cadena;
        Cursor fila=database.rawQuery("select * from alumnos",null);

        if(fila.moveToFirst()){
            do {
                txtAlumno.setText("" + fila.getString(0));
                txtUni.setText(""+fila.getString(1));
                txtCarrera.setText(""+fila.getString(2));

            }while(fila.moveToNext());
        }else
            Toast.makeText(this, "No existe el registro", Toast.LENGTH_SHORT).show();

*/
    //   database.rawQuery("INSERT INTO imagen VALUES( '" +nombre+ "') ", null);











}
