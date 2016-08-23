package com.example.macmini1_cuceimobile.horarioudeg;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Location;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.TextView;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class MyExpandableAdapter extends BaseExpandableListAdapter {

	private Activity activity;
	private ArrayList<Object> childtems;
	private LayoutInflater inflater;
	private ArrayList<String> parentItems, child;
	private Context context;
	private GeoLocationManager geoLocationManager;

	public MyExpandableAdapter(ArrayList<String> parents, ArrayList<Object> childern, Context context) {
		this.parentItems = parents;
		this.childtems = childern;
		this.context = context;
		this.geoLocationManager = new GeoLocationManager(context);
	}

	public void setInflater(LayoutInflater inflater, Activity activity) {
		this.inflater = inflater;
		this.activity = activity;
	}


	public void postData() {
		// Create a new HttpClient and Post Header

	}

	boolean isValidTime(String time,String sdia){
		//Valor comun del tiempo:  1700-1855
		//validar si es una cadena válida
		String cad = time.trim();
		if(!Pattern.matches("\\d{3,4}-\\d{3,4}$",cad)){
			//no es una cadena valida
			return false;
		}
		int dia=0;
		try{
			dia = Integer.parseInt(sdia.trim());
		}catch (Exception ex){
;
		}

		if(dia==0)
			return false; //Error en formato de día

		boolean ret;
		int horas = 1;
		String sclassTime = cad.split("-")[0];
		Calendar startTime = Calendar.getInstance();
		startTime.set(Calendar.HOUR_OF_DAY,Integer.parseInt (sclassTime.substring(0,2)));
		startTime.set(Calendar.MINUTE,Integer.parseInt (sclassTime.substring(2)));
		Calendar endTime = Calendar.getInstance();
		endTime.set(Calendar.HOUR_OF_DAY, startTime.get(Calendar.HOUR_OF_DAY)+horas);
		endTime.set(Calendar.MINUTE, startTime.get(Calendar.MINUTE));
		Calendar now = Calendar.getInstance();

		//This is for test
		//now.set(Calendar.HOUR_OF_DAY,16);

		ret = now.after(startTime) && now.before(endTime) && dia == now.get(Calendar.DAY_OF_WEEK)-1;
		return ret	;
	}

	boolean isValidLocation(){
		//checamos si tenemos permisos para acceder a la ubicación
		if(!geoLocationManager.isPermissionGranted())
			return false;
		Location location = geoLocationManager.getLastLocation();
		Coordinate coord = new Coordinate(location.getLatitude(),location.getLongitude());
		//coord = new Coordinate(20.658560, -103.325810); //dentro de cucei
		//coord = new Coordinate(20.661036, -103.333111); //fuera de cucei
		Coordinate[] cuceiPolygon = new Coordinate[]{
				new Coordinate(20.660781, -103.326195),
				new Coordinate(20.657436, -103.328877) ,
				new Coordinate(20.653378, -103.325830),
				new Coordinate(20.654867, -103.322256),
				new Coordinate(20.655545, -103.322310),
				new Coordinate(20.655786, -103.321774),
				new Coordinate(20.657066, -103.321854),
				new Coordinate(20.657571, -103.321080),
				new Coordinate(20.660767, -103.326141)
		};


		boolean validLocation = false;
		validLocation = GeoLocationUtils.LocationInPolygon(coord,cuceiPolygon);

		return validLocation;
		//return validLocation;
	}

	@Override
	public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, final ViewGroup parent) {

		final ArrayList<String> child =  (ArrayList<String>) childtems.get(groupPosition);
		//ASÍ FUNCIONA SIN PROBLEMA

		TextView textView = null;
		Button boton = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.group, null);
		}

		textView = (TextView) convertView.findViewById(R.id.textView1);
		textView.setText(child.get(childPosition));

		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				/* restricciones de gps
				   1) checar que este en el campus
				   2) despues de media hora de clase y hasta la hora

                  */
                  String CadenaTemp = (child.get(childPosition));
                  String largoCadena = String.valueOf(CadenaTemp.length());
                  final String grupo = String.valueOf(groupPosition);
                  Integer largoc = Integer.parseInt(largoCadena);
                  Integer ngrupo = Integer.parseInt(grupo);
				List <String> miListaDePalabras = new ArrayList <String> ();

                  if ((largoc > 70) && (ngrupo != 0)) {
                      //Toast.makeText(context, child.get(childPosition), Toast.LENGTH_LONG).show();
					 // Log.d("cadena1",CadenaTemp);
					 // Log.d("cadena2",largoCadena);
					 // Log.d("cadena3",largoc.toString());
					  //Log.d("cadena4",ngrupo.toString());
					  ///String str = CadenaTemp;
					  //str = str.replaceAll("[^-?0-9]+", " ");
					  //String newName = CadenaTemp.replaceAll(":", " ");
					//  System.out.println(Arrays.asList(str.trim().split(" ")));
					  //System.out.println(Arrays.asList(newName.trim().split(" ")));
					 // miListaDePalabras=Arrays.asList(newName.trim().split(" "));
					  //Log.d("Cadena 5", miListaDePalabras.get(2));
					  String lines[]=CadenaTemp.split("\\r?\\n");
					  Log.d("Cadena 6 ",lines[0]);
					  final String linea0[]=lines[0].split(":",2);
					  Log.d("materia",linea0[1]);
					  final String linea1[]=lines[1].split(":",2);
					  Log.d("Horario",linea1[1]);
					  final String linea2[]=lines[2].split(":",2);
					  Log.d("Edificio",linea2[1]);
					  final String linea3[]=lines[3].split(":",2);
					  Log.d("Aula",linea3[1]);
					  final String linea4[]=lines[4].split(":",2);
					  Log.d("profesor",linea4[1]);

                      AlertDialog.Builder builder = new AlertDialog.Builder(context);
                      builder.setTitle("        PON DEDO!     ")
                              .setMessage(child.get(childPosition) + largoCadena + " "+grupo)
                              .setCancelable(false)
                              .setPositiveButton("Falto a Clase", new DialogInterface.OnClickListener() {
                                  public void onClick(DialogInterface dialog, int id) {
                                      //envio datos a server
									/*  HttpClient httpclient = new DefaultHttpClient();
									  HttpPost httppost = new HttpPost("http://dcc.netai.net/insertaDatosDedocucei.php");

									  try {
										  // Add your data
										  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
										  nameValuePairs.add(new BasicNameValuePair("hora", linea1[1]));
										  nameValuePairs.add(new BasicNameValuePair("modulo", linea2[1] + "," + linea3[1]));
										  nameValuePairs.add(new BasicNameValuePair("materia", linea0[1]));
										  nameValuePairs.add(new BasicNameValuePair("maestro", linea4[1]));
										  httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
										  httpclient.execute(httppost);
										  try {
											  HttpResponse response = httpclient.execute(httppost);
										  } catch (Exception e) {

										  }
									  }catch (IOException e) {
										  // TODO Auto-generated catch block
									  }

									  // Execute HTTP Post Request

										  //Log.d("Responde servidor",response.toString());*/
									  StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

									  StrictMode.setThreadPolicy(policy);
									  String url = "http://dcc.netai.net/insertaDatosDedocucei.php";
									  String aux = "";
									  if(isValidTime(linea1[1],grupo) && isValidLocation()) {
										  try {
											  DefaultHttpClient httpClient = new DefaultHttpClient();
											  HttpPost httpPost = new HttpPost(url);
											  // HttpPost httpPost = new
											  // HttpPost("http://localhost/siiau1/profesores.php");
											  List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
											  nameValuePairs.add(new BasicNameValuePair("hora", linea1[1]));
											  nameValuePairs.add(new BasicNameValuePair("modulo", linea2[1] + "," + linea3[1]));
											  nameValuePairs.add(new BasicNameValuePair("materia", linea0[1]));
											  nameValuePairs.add(new BasicNameValuePair("maestro", linea4[1]));
											  httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

											  HttpResponse httpResponse = httpClient.execute(httpPost);
											  HttpEntity httpEntity = httpResponse.getEntity();

											  httpResponse.getStatusLine();

											  if (httpEntity != null) {
												  aux = EntityUtils.toString(httpEntity);
											  } else {
												  aux = "3Error";
											  }
											  Log.d("Servidor", aux);
										  } catch (UnsupportedEncodingException e) {
											  e.printStackTrace();
										  } catch (ClientProtocolException e) {
											  e.printStackTrace();
										  } catch (IOException e) {
											  e.printStackTrace();
										  } finally {
										  }
									  }else{
										  AlertDialog alertDialog = new AlertDialog.Builder(context).create(); //Read Update
										  alertDialog.setTitle("Datos inválidos");
										  alertDialog.setMessage("Necesita ser horario correcto y encontrarse dentro de CUCEI");

										  alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
											  public void onClick(DialogInterface dialog, int which) {
												  // here you can add functions
												  dialog.cancel();
											  }
										  });

										  alertDialog.show();  //<-- See This!
										 // throw  new IllegalArgumentException("Hora o ubicación no válida");
									  }
                                      dialog.cancel();
                                  }
                              })
                              .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                                  @Override
                                  public void onClick(DialogInterface dialog, int which) {
                                      dialog.cancel();
                                  }
                              });

                      AlertDialog alert = builder.create();
                      alert.show();
                  }
            }
		});



		return convertView;
	}

	@Override
	public View getGroupView(final int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.row, null);
		}

		((CheckedTextView) convertView).setText(parentItems.get(groupPosition));
		((CheckedTextView) convertView).setChecked(isExpanded);
		convertView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Log.i("uni",String.valueOf(groupPosition));
			}
		});

		return convertView;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return ((ArrayList<String>) childtems.get(groupPosition)).size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return null;
	}

	@Override
	public int getGroupCount() {
		return parentItems.size();
	}

	@Override
	public void onGroupCollapsed(int groupPosition) {
		super.onGroupCollapsed(groupPosition);
	}

	@Override
	public void onGroupExpanded(int groupPosition) {
		super.onGroupExpanded(groupPosition);
	}

	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
