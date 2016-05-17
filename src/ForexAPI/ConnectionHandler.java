package ForexAPI;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import Server_TCP_Analysis.ServerRun;
import org.json.*;
import java.net.URL;
import Database.DatabaseConnection;

/**
 * Created by dimas on 11.03.16.
 */

public class ConnectionHandler implements Runnable {

    private boolean isRun = false;
    private HttpsURLConnection httpsURLConnection = null;
    private BufferedReader br = null;
    private String token = null;
    private String instrument = null;
    private JSONArray jsonArray = null;
    private DatabaseConnection databaseConnection = null;
    private boolean let_me_forecast = false;

    public ConnectionHandler(String instrument, DatabaseConnection dbConnection){

        this.instrument = instrument;
        this.token = "68fce9a1b2cfc45b00b4b82906d5b926-8f670b9a2ef669ab44b4a129874cbcb4";
        this.isRun = true;
        this.databaseConnection = dbConnection;

    }

    public boolean Status_Forecast(){

        return this.let_me_forecast;

    }

    public void setRunFlag(){

        this.isRun = false;

    }

    private void connectionHandle(){

        try{

            URL url = new URL("https://api-fxpractice.oanda.com/v1/prices?instruments=" + this.instrument);
            this.httpsURLConnection = (HttpsURLConnection)url.openConnection();
            this.httpsURLConnection.setRequestMethod("GET");
            this.httpsURLConnection.setRequestProperty("Authorization", "Bearer " + this.token);
            this.br = new BufferedReader(
                    new InputStreamReader(
                            this.httpsURLConnection.getInputStream()
                    )
            );

        }catch(Exception ex){
            setRunFlag();
            this.httpsURLConnection.disconnect();
            System.out.println(ex.toString());
        }

    }

    private JSONArray jsonParser(String jsonToParse){
        try {
            JSONObject object = new JSONObject(jsonToParse);
            JSONArray arrayList = object.getJSONArray("prices");
            return arrayList;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {

        while(this.isRun){

            try{

                connectionHandle();
                int resposeCode = this.httpsURLConnection.getResponseCode();
                StringBuilder stringBuilder = new StringBuilder();
                String input = "";

                while((input = this.br.readLine()) != null){

                    stringBuilder.append(input);

                }

                if(stringBuilder.length() != 0){

                    JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                    this.jsonArray = jsonParser(stringBuilder.toString());

                    for(int i = 0; i < this.jsonArray.length(); i++){

                        String status = "";


                        if(jsonObject.has("status")){

                            status = this.jsonArray.getJSONObject(i).getString("status");

                        }

                        String date = this.jsonArray.getJSONObject(i).getString("time");
                        double bid = this.jsonArray.getJSONObject(i).getDouble("bid");
                        double ask = this.jsonArray.getJSONObject(i).getDouble("ask");
                        String instr = this.jsonArray.getJSONObject(i).getString("instrument");
                        this.databaseConnection.insertToTable(bid, ask,
                                status, instrument);
                    }
                    System.out.println(jsonObject.toString());
                    Thread.sleep(10000);

                }else{

                    System.out.println(resposeCode);
                    Thread.sleep(3000);

                }

            }catch(Exception ex){
                System.out.println(ex.toString());
            }

        }

    }
}
