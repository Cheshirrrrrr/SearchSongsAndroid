package project.project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void onSingerClicked(View view){
        Log.i("^-^", "onCitySelected: " + view);
        // Запускаем экран CityCamActivity, который покажет веб-камеру из выбранного города
//        Intent cityCam = new Intent(this, CityCamActivity.class);
//        cityCam.putExtra(CityCamActivity.EXTRA_CITY, city);
//        startActivity(cityCam);
    }
}
