package ca.on.conestogac.cmms;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by user on 2016-02-23.
 */
public class HttpAsyncLoader  extends AsyncTaskLoader<String> {
    private String url = null;
    private String method = null;
    private String param = null;


    public HttpAsyncLoader(Context context, String url, String method, String param) {
        super(context);
        this.url = url;
        this.method = method;
        this.param = param;
    }

    @Override
    public String loadInBackground() {
        HttpURLConnection conn = null;
        // todo: text "GET"
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);

            Writer writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
            writer.write(param);
            writer.close();

            conn.connect();

            if( conn.getResponseCode() == HttpURLConnection.HTTP_OK ){
                StringBuffer response = new StringBuffer();
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    response.append(inputLine);
                }
                return response.toString();
            }
        } catch (IOException e) {
            DebugUtility.logError(e.getMessage());
        } finally {
            if( conn != null ){
                conn.disconnect();
            }
        }
        return null;
    }
}
