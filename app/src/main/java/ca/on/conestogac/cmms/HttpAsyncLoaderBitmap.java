package ca.on.conestogac.cmms;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by user on 2016-03-16.
 */
public class HttpAsyncLoaderBitmap extends AsyncTask<Uri.Builder, Void, Bitmap> {
    private Context context;
    private ImageView imageView;
    private AlertDialog mDialog;

    public HttpAsyncLoaderBitmap(Context context, ImageView imageView){
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage("Connecting server").setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });
        mDialog = builder.create();
        mDialog.show();
    }

    @Override
    protected Bitmap doInBackground(Uri.Builder... builder){
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        Bitmap bitmap = null;

        try{

            URL url = new URL(builder[0].toString());
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(ValueConstants.SERVER_TIMEOUT);
            connection.connect();
            inputStream = connection.getInputStream();

            bitmap = BitmapFactory.decodeStream(inputStream);
        }catch (MalformedURLException exception){

        }catch (IOException exception){

        }finally {
            if (connection != null){
                connection.disconnect();
            }
            try{
                if (inputStream != null){
                    inputStream.close();
                }
            }catch (IOException exception){
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap result){
        this.imageView.setImageBitmap(result);
        mDialog.dismiss();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        mDialog.dismiss();
    }
}
