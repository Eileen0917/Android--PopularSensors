package com.example.eileen.hw7_1;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity implements SensorEventListener{
    TextView txtProxi, txtLight, txtMagnet, txtPressure, txtAcc, txtGyro;
    Toolbar tbToolbar;
    Sensor proxiSensor, lightSensor, pressSensor, magnetSensor, accSensor, gyroSensor;
    SensorManager sensorMgr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtProxi = (TextView) findViewById(R.id.txtProxi);
        txtLight = (TextView) findViewById(R.id.txtLight);
        txtMagnet = (TextView) findViewById(R.id.txtMagnet);
        txtPressure = (TextView) findViewById(R.id.txtPressure);
        txtAcc = (TextView) findViewById(R.id.txtAcc);
        txtGyro = (TextView) findViewById(R.id.txtGyro);
        tbToolbar = (Toolbar) findViewById(R.id.tbToolbar);
        tbToolbar.setLogo(R.mipmap.sensors);
        tbToolbar.setTitleTextAppearance(this, (int) getResources().getDimension(R.dimen.action_menu_item_text_size));

        sensorMgr = (SensorManager) getSystemService(SENSOR_SERVICE);
        proxiSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_PROXIMITY);
        if(proxiSensor==null){
            txtProxi.setText("趨近感測器：無\n");
        }

        lightSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_LIGHT);
        if(lightSensor==null){
            txtLight.setText("光線感測器：無\n");
        }

        pressSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_PRESSURE);
        if(pressSensor==null){
            txtPressure.setText("大氣壓力感測器：無\n");
        }

        magnetSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if(magnetSensor==null){
            txtMagnet.setText("磁場感測器：無\n");
        }

        accSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accSensor==null){
            txtAcc.setText("加速度感測器：無\n");
        }

        gyroSensor = sensorMgr.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        if(gyroSensor==null){
            txtGyro.setText("陀螺儀感測器：無\n");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorMgr.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorMgr.registerListener(this, proxiSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorMgr.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorMgr.registerListener(this, pressSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorMgr.registerListener(this, magnetSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorMgr.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorMgr.registerListener(this, gyroSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()){
            case Sensor.TYPE_PROXIMITY:
                txtProxi.setText("趨近感測器:\n" + "物體趨近距離" + String.format("%.2f", event.values[0]) + "公分(cm)...\n");
                break;

            case Sensor.TYPE_LIGHT:
                txtLight.setText("光線感測器:\n" + "光線強度:" + event.values[0] + "流明(lux)...\n");
                break;

            case Sensor.TYPE_PRESSURE:
                txtLight.setText("大氣壓力感測器:\n" + "大氣壓力:" + String.format("%.2f", event.values[0]/1000) + "巴(Bar)...\n");
                break;

            case Sensor.TYPE_MAGNETIC_FIELD:
                getMagnetMeter(event);
                break;

            case Sensor.TYPE_ACCELEROMETER:
                getAccMeter(event);
                break;

            case Sensor.TYPE_GYROSCOPE:
                getGyroScope(event);
                break;

            default:
                Toast.makeText(getApplicationContext(), "No sensor responds!!", Toast.LENGTH_SHORT).show();
                break;


        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    private void getMagnetMeter(SensorEvent event){
        float[] value = event.values;

        String magnetFlds = "磁場感應器:\n" + "各軸磁場強度:\n" + "\tx=" + String.format("%.2f", value[0]*10) + "毫高斯(0.1 uTesla)\n"
                + "\ty=" + String.format("%.2f", value[1]*10) + "毫高斯(0.1 uTesla)\n"
                + "\tz=" + String.format("%.2f", value[2]*10) + "毫高斯(0.1 uTesla)\n";
        txtMagnet.setText(magnetFlds);
    }

    private void getAccMeter(SensorEvent event){
        float[] value = event.values;
        float tAcc = (float)Math.sqrt((double)(value[0]*value[0] + value[1]*value[1] + value[2]*value[2]));
        String acc = "加速度感應器:\n" + "總加速度:" + String.format("%.2f", tAcc) + "米/秒平方(m/s2)\n各軸加速度: \n\tx=" + String.format("%.2f", value[0])
                + "米/秒平方(m/s2)\n\ty=" + String.format("%.2f", value[1]) + "米/秒平方(m/s2)\n\tz=" + String.format("%.2f", value[2]) + "米/秒平方(m/s2)\n";
        txtAcc.setText(acc);

    }

    private void getGyroScope(SensorEvent event){
        float[] value = event.values;
        String angularVelocity = "陀螺儀感測器:\n" + "各軸角速度\n\tx=" + String.format("%.2f", value[0]) + "度/秒(deg/s)\n\ty=" +
                String.format("%.2f", value[1]) + "度/秒(deg/s)\n\tz=" + String.format("%.2f", value[2]) + "度/秒(deg/s)\n";
        txtGyro.setText(angularVelocity);
    }
}
