package www.kidsplace.at.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.util.Patterns;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by admin on 3/20/2018.
 */

public class UtilsMethods {

    /**
     * Check Validation Methods
     */
    public static boolean checkEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean checkURLValid(String url) {
        return Patterns.WEB_URL.matcher(url.toLowerCase()).matches();
    }

    public static boolean checkDateValid(String stDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = format.parse(stDate);
            return true;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Graphic Methods
     */
    public static Bitmap getBitmapFromIntentWithGallery(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bm;
    }

    public static Bitmap getBitmapFromIntentWithCameray(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        return thumbnail;
    }

    public static String saveImage(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File imagePath = new File(Environment.getExternalStorageDirectory() + "/" + "kidsplace", "images");

        if (!imagePath.exists())
            imagePath.mkdirs();

        File destination = new File(imagePath.getAbsolutePath(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return destination.getPath();
    }

    public static byte[] convertBitmapToByteArray(Bitmap bm) {
        if (bm == null)
            return new byte[]{};

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    /**
     * Location Methods
     */
    public static String getAddressFromLocation(Context context, Location location) {
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            Address returnedAddress = addresses.get(0);
            StringBuilder strReturnedAddress = new StringBuilder("");
            for (int i = 0; i < returnedAddress.getMaxAddressLineIndex() + 1; i ++) {
                strReturnedAddress.append(returnedAddress.getAddressLine(i));
                if (i < returnedAddress.getMaxAddressLineIndex() - 1) {
                    strReturnedAddress.append(" ,");
                }
            }

            return strReturnedAddress.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static Location getLocationFromString(String location) {
        double latitude;
        double longitude;
        if (location.isEmpty()) {
            latitude = 0;
            longitude = 0;
        } else {
            String[] locations = location.split(",");
            latitude = Double.parseDouble(locations[0]);
            longitude = Double.parseDouble(locations[1]);
        }

        Location loc = new Location("dummyprovider");
        loc.setLatitude(latitude);
        loc.setLongitude(longitude);
        return loc;
    }

    public static String getStringFromLocation(Location location) {
        return  location.getLatitude() + "," + location.getLongitude();
    }

    /**
     * Date Methods
     */
    public static Date convertStringToDate(String stDate) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date date = format.parse(stDate);
            return date;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new Date();
    }

    public static String convertDateToString(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        String dateTime = dateFormat.format(date);
        return dateTime;
    }

    public static boolean compareDate(int year, int month, int day) {
        Calendar c = Calendar.getInstance();
        long currentMillion = c.getTimeInMillis();

        c.set(year, month, day);
        long expireMillion = c.getTimeInMillis();

        return currentMillion > expireMillion;
    }

    /**
     * String Methods
     */
    public static String convertArrayToString(ArrayList<String> list) {
        String str = "";
        for (String category: list) {
            str += category;
            if (list.indexOf(category) < list.size() - 1) {
                str += ",";
            }
        }

        return str;
    }

    /**
     * Output Methods
     */
    public static int readIntFromPreference(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("kidsplace", Activity.MODE_PRIVATE);
        return preferences.getInt(key, 5);
    }

    public static void writeIntToPreferenece(Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences("kidsplace", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static boolean readBooleanFromPreference(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("kidsplace", Activity.MODE_PRIVATE);
        return preferences.getBoolean(key, false);
    }

    public static void writeBooleanFromPreference(Context context, String key, boolean value) {
        SharedPreferences preferences = context.getSharedPreferences("kidsplace", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static String readStringFromPreference(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences("kidsplace", Activity.MODE_PRIVATE);
        return preferences.getString(key, "");
    }

    public static void writeStringFromPreference(Context context, String key, String value) {
        SharedPreferences preferences = context.getSharedPreferences("kidsplace", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    /**
     * Json Methods
     */
    public static String getStringFromJSON(JSONObject object, String key) {
        try {
            String result = object.getString(key);
            return result;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static JSONObject getJSONFromString(String info) {
        if (!info.isEmpty()) {
            try {
                JSONObject result = new JSONObject(info);
                return result;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return new JSONObject();
    }

    public static void addStringToJSON(JSONObject object, String key, String value) {
        try {
            object.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * File Methods
     */
    public static void writeToFile(Context context, String data) {

        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput("user.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }


    public static String readFromFile(Context context) {

        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("user.txt");

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        }
        catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    /**
     * Utility Methods
     */
    public static String cutDouble(double value) {
        DecimalFormat df = new DecimalFormat("###.##");
        return df.format(value);
    }
}
