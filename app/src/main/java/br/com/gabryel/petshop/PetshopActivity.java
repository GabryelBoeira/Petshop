package br.com.gabryel.petshop;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;


public class PetshopActivity extends AppCompatActivity {

    Petshop petshop;
    //requisição para o uso da camera
    int CAMERA_REQUEST = 1888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_petshop);

        EditText txtNome = (EditText) findViewById(R.id.txtNome);
        EditText txtNomeAnimal = (EditText) findViewById(R.id.txtNomeAnimal);
        Spinner spTipo = (Spinner) findViewById(R.id.spTipo);
        EditText txtTelefone = (EditText) findViewById(R.id.txtTelefone);
        EditText txtProcedimento = (EditText) findViewById(R.id.txtProcedimento);
        CheckBox checkAtivo = (CheckBox) findViewById(R.id.checkAtivo);
        ImageView image = (ImageView) findViewById(R.id.image);

        Intent it = getIntent();
        if (it != null && it.hasExtra("petshop")) {

            petshop = (Petshop) it.getSerializableExtra("petshop");

            txtNome.setText(petshop.getNomeDono());
            txtNomeAnimal.setText(petshop.getNomeAnimal());
            txtProcedimento.setText(petshop.getProcedimento());
            spTipo.setSelection(((ArrayAdapter) spTipo.getAdapter()).getPosition(petshop.getTipo()));
            txtTelefone.setText(petshop.getNumero());
            checkAtivo.setChecked(petshop.isAtivo());

            ByteArrayInputStream imageStream = new ByteArrayInputStream(petshop.getImagem());
            Bitmap bitmap = BitmapFactory.decodeStream(imageStream);
            image.setImageBitmap(bitmap);
        }

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, CAMERA_REQUEST);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_petshop, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //notificação para salvar e aterar junto ao dos dados
        final EditText txtDescrição = (EditText) findViewById(R.id.txtProcedimento);
        DatePicker data = (DatePicker) findViewById(R.id.data);
        TimePicker hora = (TimePicker) findViewById(R.id.hora);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, data.getDayOfMonth());
        calendar.set(Calendar.MONTH, data.getMonth());
        calendar.set(Calendar.YEAR, data.getYear());
        calendar.set(Calendar.HOUR_OF_DAY, hora.getCurrentHour());
        calendar.set(Calendar.MINUTE, hora.getCurrentMinute());

        Timer t = new Timer();
        t.schedule(new TimerTask() {
            public void run() {


                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(PetshopActivity.this)
                                .setSmallIcon(R.drawable.index)
                                .setContentTitle("Hora de busca do pet")
                                .setContentText(txtDescrição.getText().toString())
                                .setVibrate(new long[]{100, 250});
// Cria o intent que irá chamar a atividade a ser aberta quando clicar na notifição
                Intent resultIntent = new Intent(PetshopActivity.this, PetshopActivity.class);

//PendingIntent é "vinculada" a uma notification para abrir a intent
                PendingIntent resultPendingIntent = PendingIntent.
                        getActivity(PetshopActivity.this, 0, resultIntent, 0);

//associa o intent na notificação
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//gera a notificação
                mNotificationManager.notify(99, mBuilder.build());
            }
        }, calendar.getTime());

        if (id == R.id.save) {

            EditText txtNome = (EditText) findViewById(R.id.txtNome);
            EditText txtNomeAnimal = (EditText) findViewById(R.id.txtNomeAnimal);
            Spinner spTipo = (Spinner) findViewById(R.id.spTipo);
            EditText txtTelefone = (EditText) findViewById(R.id.txtTelefone);
            EditText txtProcedimento = (EditText) findViewById(R.id.txtProcedimento);
            CheckBox checkAtivo = (CheckBox) findViewById(R.id.checkAtivo);
            ImageView image = (ImageView) findViewById(R.id.image);

            if (petshop == null) {
                petshop = new Petshop();
            }

            petshop.setNomeDono(txtNome.getText().toString());
            petshop.setNomeAnimal(txtNomeAnimal.getText().toString());
            petshop.setTipo(spTipo.getSelectedItem().toString());
            petshop.setNumero(txtTelefone.getText().toString());
            petshop.setProcedimento(txtProcedimento.getText().toString());
            petshop.setAtivo(checkAtivo.isChecked());

            Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageInByte = baos.toByteArray();
            petshop.setImagem(imageInByte);

            new PetshopDao().salvar(petshop);
            petshop = null;
            
            Toast.makeText(getApplicationContext(),
                    "Salvo com sucesso!",
                    Toast.LENGTH_LONG).show();

            Intent it = new Intent(PetshopActivity.this, MainActivity.class);
            startActivity(it);

            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void ligar(View view) {

        EditText txtTelefone = (EditText) findViewById(R.id.txtTelefone);

        Intent callIntent = new Intent(Intent.ACTION_CALL);
        callIntent.setData(Uri.parse("tel:" + txtTelefone.getText()));

        ActivityCompat.requestPermissions(PetshopActivity.this, new String[]{Manifest.permission.CALL_PHONE}, 1);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        startActivity(callIntent);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK){
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            ImageView image = (ImageView) findViewById(R.id.image);
            image.setImageBitmap(photo);
        }
    }
}
