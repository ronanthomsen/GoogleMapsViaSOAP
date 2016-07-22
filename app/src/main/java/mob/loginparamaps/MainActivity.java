package mob.loginparamaps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.concurrent.ExecutionException;


public class MainActivity extends AppCompatActivity {

    private EditText txtUsuario;
    private EditText txtSenha;
    private String resultado = "";
    private String usuario;
    private String senha;
    private SharedPreferences sp;
    private Exception exception;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtUsuario = (EditText) findViewById(R.id.txtLogin);
        txtSenha = (EditText)  findViewById(R.id.txtSenha);
        usuario = txtUsuario.getText().toString();
        senha = txtSenha.getText().toString();

        sp = getPreferences(MODE_PRIVATE);

        usuario = sp.getString("sp_usuario", null);
        senha = sp.getString("sp_senha", null);

        if(usuario != null){
            txtUsuario.setText(usuario);
            txtSenha.setText(senha);
        }

    }






    private class LoginTask extends AsyncTask<String, Void, String> {

        private Exception exception;

        @Override
        protected String doInBackground(String... urls) {

            //String NAMESPACE = "http://10.0.2.2:8080/"; LocalHost
            String NAMESPACE = "http://10.0.2.2:8080/";
            String METHOD_NAME_BUSCAR_CLIENTES = "validaLogin";
            String SOAP_ACTION = "Servico";
            //String URL = "http://192.168.0.2:8080/ValidaLoginProject/services/Servico?wsd";
            String URL = "http://10.0.2.2:8080/ValidaLoginProject/services/Servico?wsd";
            Log.i("RESULTADO", "ENTRADA");

            try {
                Log.i("RESULTADO", "ENTRADA2");
                SoapObject request = new SoapObject(NAMESPACE, METHOD_NAME_BUSCAR_CLIENTES);

                PropertyInfo p1 = new PropertyInfo();
                p1.setName("login");
                p1.setValue(urls[0]);
                p1.setType(String.class);

                PropertyInfo p2 = new PropertyInfo();
                p2.setName("senha");
                p2.setValue(urls[1]);
                p2.setType(String.class);
                Log.i("RESULTADO", "ENTRADA3");
                request.addProperty(p1);
                request.addProperty(p2);

                SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
                envelope.setOutputSoapObject(request);
                Log.i("RESULTADO", "ENTRADA4");
                HttpTransportSE androidHttpTransport = new HttpTransportSE(URL);
                Log.i("RESULTADO", "ENTRADA5");

                // Realizamos a chamada do webservice
                androidHttpTransport.call(SOAP_ACTION, envelope);
                Log.i("RESULTADO", "ENTRADA6");

                Object response = envelope.getResponse();
                resultado = response.toString();
                Log.i("RESULTADO", resultado+"");

            } catch (Exception e) {
                this.exception = e;
                Log.e("ERRO", e.toString());
            }
            return resultado;
        }




    }


    public void validaLogin(View v){

        CheckBox cbxLembrar = (CheckBox)  findViewById(R.id.cbxLembrar);
        String _usuario = txtUsuario.getText().toString();
        String _senha = txtSenha.getText().toString();
        SharedPreferences.Editor e = sp.edit();
        AsyncTask<String, Void, String> validaResultado;
        String _resultado = new String();
        Intent chamaMapa;


        if( !_usuario.equals("") && !_senha.equals("") ){

            if(cbxLembrar.isChecked()){

                e.putString("sp_usuario",_usuario);
                e.putString("sp_senha",_senha);
                e.commit();
            }

            validaResultado = new LoginTask().execute(_usuario, _senha);
            try {
                _resultado = validaResultado.get().toString();
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            } catch (ExecutionException e1) {
                e1.printStackTrace();
            }

            if(_resultado.equals("true")){
                chamaMapa = new Intent(this, MapsActivity.class);
                startActivity(chamaMapa);
            }
            else{
                Toast.makeText(this, "Login ou Senha errados", Toast.LENGTH_SHORT).show();
            }


        }


    }








}





