package com.example.mvplayer.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.example.mvplayer.BuildConfig
import com.example.mvplayer.R
//import com.facebook.appevents.AppEventsConstants
//import com.facebook.appevents.AppEventsLogger
//import com.google.android.gms.common.ConnectionResult
//import com.google.android.gms.common.GoogleApiAvailability
//import com.google.android.gms.maps.model.LatLng
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import com.google.android.material.snackbar.Snackbar
//import com.google.android.play.core.appupdate.AppUpdateManagerFactory
//import com.google.android.play.core.install.model.AppUpdateType
//import com.google.android.play.core.install.model.UpdateAvailability
//import com.google.gson.GsonBuilder
import com.google.android.material.snackbar.Snackbar
//import okhttp3.Request
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.text.SimpleDateFormat


object Utility {
    fun isConnectedToInternet(context: Context?):Boolean{
        val connectivityManage=context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo?=connectivityManage.activeNetworkInfo
        var isConnected: Boolean = activeNetwork?.isConnectedOrConnecting == true
        Log.i("isConnectedToInternet","Status: "+ isConnected)
        return isConnected
    }

    /*fun isGooglePlayServicesAvailable(activity: Activity): Boolean{
        val googleApiAvailability: GoogleApiAvailability = GoogleApiAvailability.getInstance()
        val status = googleApiAvailability.isGooglePlayServicesAvailable(activity.applicationContext)
        if (status != ConnectionResult.SUCCESS){
            if (googleApiAvailability.isUserResolvableError(status)){
                googleApiAvailability.getErrorDialog(activity, status, 2404).show()
            }
            return false
        }
        return true
    }*/

 /*   fun getAppVersionName(): String {
        return BuildConfig.VERSION_NAME
    }*/

    fun isGPSEnabled(context: Context?): Boolean{
        val locationManager: LocationManager= context!!.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }

    /*fun logAddToCartEvent(
        context: Context?, contentData: String?, contentId: String?, contentType: String?,
        currency: String?,
        price: Double){
        val logger = AppEventsLogger.newLogger(context)
        val params = Bundle()
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, contentData)
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_ID, contentId)
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT_TYPE, contentType)
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency)
        logger.logEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART, price, params)
    }*/

    /**
     * @param rootView: pass layout parent view id.
     * @param message: message needs to be displayed.
     * */
    fun showShackBarWithoutAction(rootView: View, message:String){
        Snackbar.make(rootView,message, Snackbar.LENGTH_LONG).show()
    }

    /**
     * @param rootView: pass layout parent view id.
     * @param message: message needs to be displayed.
     * */
    fun showShackBarWithAction(rootView: View, message:String?){
        Snackbar.make(rootView,message!!, Snackbar.LENGTH_INDEFINITE)
            .setAction("OK", View.OnClickListener {

            }).show()
    }

    /**
     * @param context: pass context.
     * @param key: key to be used.
     * @param value: value to be stored.
     * */
    fun setPreference(context: Context?, key: String, value: String?){
        val editor: SharedPreferences.Editor= context!!.getSharedPreferences(context.getString(R.string.app_name),
            Context.MODE_PRIVATE).edit()
        editor.putString(key,value)
        editor.apply()
        editor.commit()
    }

    /**
     * @param context: pass context.
     * @param key: key to be used.
     * @param value: value to be stored.
     * */
    fun setPreferenceInt(context: Context?, key: String, value: Int){
        var editor: SharedPreferences.Editor= context!!.getSharedPreferences(context.getString(R.string.app_name),
            Context.MODE_PRIVATE).edit()
        editor.putInt(key,value)
        editor.apply()
        editor.commit()
    }

    /**
     * @param context: pass context.
     * @param key: key to be used.
     * @param value: value to be stored.
     * */
    fun setBooleanPreference(context: Context?, key: String, value: Boolean){
        var editor: SharedPreferences.Editor= context!!.getSharedPreferences(context.getString(R.string.app_name),
            Context.MODE_PRIVATE).edit()
        editor.putBoolean(key,value)
        editor.apply()
        editor.commit()
    }

    /**
     * @param context: pass context.
     * @param key: key to get the key value.
     * */
    fun getPreference(context: Context?, key: String): String? {
        var sharedPref: SharedPreferences =context!!.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return sharedPref.getString(key, "")
    }

    /**
     * @param context: pass context.
     * @param key: key to get the key value.
     * */
    fun getBoolenPreference(context: Context?, key: String): Boolean? {
        var sharedPref: SharedPreferences =context!!.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        return sharedPref.getBoolean(key, false)
    }

    /**
     * @param context: pass context.
     * @param key: key to get the key value.
     * */
    fun removePreference(context: Context?, key: String){
        var sharedPref: SharedPreferences =context!!.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor= sharedPref.edit()
        editor.remove(key)
        editor.apply()
        editor.commit()
    }

    /**
     * @param context: pass context.
     * */
    fun clearPreference(context: Context?){
        var sharedPref: SharedPreferences =context!!.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        var editor: SharedPreferences.Editor= sharedPref.edit()
        editor.clear()
        editor.apply()
        editor.commit()
    }

    /**
     * @param TAG: pass class name.
     * @param message: message to be shown.
     * */
    fun LogI(TAG: String, message: String){
        if (BuildConfig.DEBUG){
            Log.i(TAG,message)
        }
    }

    /**
     * @param context: pass context.
     * @param message: message to be shown.
     * */
    fun ToastDebug(context: Context?, message: String){
        if (BuildConfig.DEBUG){
            Toast.makeText(context,message,Toast.LENGTH_LONG).show()
        }
    }

    /**
     * @param context: pass context.
     * @param message: message to be shown.
     * */
    fun showToast(context: Context?,msg: String){
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show()
    }

    /**
     * @param apiName: pass name of called Api.
     * @param request: pass request.call to show called Api an its parameter.
     * */
   /* fun showCalledApi(apiName: String, request: Request) {
        if (BuildConfig.DEBUG) {
            LogI(apiName, request.toString())
        }
    }*/

    /**
     * reduces the size of the image
     * @param image
     * @param maxSize
     * @return
     */
    fun getResizedBitmap(image: Bitmap, maxSize: Int): Bitmap {
        var width = image.width
        var height = image.height

        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 1) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    fun hideKeyboardFromActivity(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun hideKeyboardFromView(context: Context, view: View) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun generateUniqueID(): Int {
        val c = Calendar.getInstance()
        val sdf = SimpleDateFormat("mmssSS", Locale.US)
        return Integer.parseInt(sdf.format(c.time))
    }

    /*fun showAppUpdateDialog(context: Context) {
        val updateDialog = MaterialAlertDialogBuilder(context).apply {
            setMessage(context.getString(R.string.update_app_message))
           setCancelable(false)
            setPositiveButton("Update"){dialog,which ->
                dialog.dismiss()
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse("https://play.google.com/store/apps/details?id=com.homesfresh")
                context.startActivity(intent)
            }
        }.create()
        updateDialog.show()
    }*/
}