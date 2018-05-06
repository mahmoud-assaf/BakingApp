package com.mahmoud.bakingapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.TextView;

import com.mahmoud.bakingapp.fragment.BlankFragment;
import com.mahmoud.bakingapp.fragment.PlayerFragment;
import com.mahmoud.bakingapp.model.Ingredient;

import java.util.List;

/**
 * Created by mahmoud on 18/02/2018.
 */

public class Utils {
static Context context;
    public  Utils(Context context){
        this.context=context;
    }

    public  void showMessage(String msg, TextView textView,boolean isError){
        textView.setVisibility(View.VISIBLE);
        if ((isError)){
            textView.setBackgroundColor(context.getResources().getColor(R.color.bgStatusError));
        }else {
            textView.setBackgroundColor(context.getResources().getColor(R.color.bgStatusNormal));
        }
        textView.setText(msg);
    }



    //using @gar accepted answer at https://stackoverflow.com/questions/1560788/how-to-check-internet-access-on-android-inetaddress-never-times-out
    public static boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }

//calculating num of columns for grid
// https://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
    public static int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 250);
        return noOfColumns;
    }

public static String  formatIngredients(List<Ingredient> ingredients){

        String ingredientsTtext="";
    for (int i = 0; i <ingredients.size() ; i++) {
        ingredientsTtext+="- ("+String.valueOf(ingredients.get(i).quantity) +") "+ ingredients.get(i).measure+" of "+ingredients.get(i).ingredient +". \n";
    }
    return  ingredientsTtext;

}
    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

}
