package com.sourgou.librairies.ussdrequest;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

public class USSDRequest {

    protected String syntax;
    protected Context context;


    public USSDRequest(String syntax, Context context) {
        this.syntax = syntax;
        this.context = context;
    }


    public void makeRequest(){

        if (Build.VERSION.SDK_INT < 23) {
            phoneCall();
        }else {

            if (ActivityCompat.checkSelfPermission(this.context,
                    Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {

                phoneCall();
            }else {
                final String[] PERMISSIONS_STORAGE = {Manifest.permission.CALL_PHONE};
                //Asking request Permissions
                ActivityCompat.requestPermissions((Activity) this.context, PERMISSIONS_STORAGE, 9);
            }
        }
    }


    protected void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean permissionGranted = false;
        switch(requestCode){
            case 9:
                permissionGranted = grantResults[0]== PackageManager.PERMISSION_GRANTED;
                break;
        }
        if(permissionGranted){
            phoneCall();
        }else {
            Toast.makeText(this.context, "Veuillez autoriser, l\'application à passer des appels téléphoniques à partir des paramètres afin de pouvoir continuer", Toast.LENGTH_SHORT).show();
        }
    }

    protected void phoneCall(){
        if (ActivityCompat.checkSelfPermission(this.context,
                Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //String ussd = "*555*1" + Uri.encode("#");
            callIntent.setData(Uri.parse("tel:" + this.syntax));
            this.context.startActivity(callIntent);
        }else{
            Toast.makeText(this.context, "Veuillez autoriser, l\'application à passer des appels téléphoniques à partir des paramètres afin de pouvoir continuer", Toast.LENGTH_SHORT).show();
        }
    }
}
