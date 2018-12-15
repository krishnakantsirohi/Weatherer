package com.example.krish.weatherer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Weather {
    private String city, condition, temp, max_temp, min_temp, pressure, humidity, sunrise, sunset, wind_speed, wind_direction, rain, hour, icon;
    Bitmap bmp;

    public Weather(JSONObject jsonObject) throws JSONException {
        this.setCity(jsonObject);
        this.setCondition(jsonObject);
        this.setHumidity(jsonObject);
        this.setMax_temp(jsonObject);
        this.setMin_temp(jsonObject);
        this.setPressure(jsonObject);
        this.setRain(jsonObject);
        this.setSunrise(jsonObject);
        this.setSunset(jsonObject);
        this.setTemp(jsonObject);
        this.setWind_direction(jsonObject);
        this.setWind_speed(jsonObject);
    }

    public Weather(JSONObject jsonObject, int flag) throws JSONException, IOException
    {
        System.out.println(jsonObject.toString()    );
        this.setTemp(jsonObject);
        this.setMin_temp(jsonObject);
        this.setMax_temp(jsonObject);
        this.setHour(jsonObject);
        this.setIcon(jsonObject);
    }

    public Bitmap getIcon() {
        return bmp;
    }

    public void setIcon(JSONObject jsonObject) throws JSONException, IOException {
        this.icon = jsonObject.getJSONArray("weather").getJSONObject(0).get("icon").toString();
        this.bmp = BitmapFactory.decodeStream(new URL("https://openweathermap.org/img/w/"+this.icon+".png").openConnection().getInputStream());
    }

    public String getCity() {
        return city;
    }

    public String getCondition() {
        return condition;
    }

    public String getTemp() {
        return temp;
    }

    public String getMax_temp() {
        return max_temp;
    }

    public String getMin_temp() {
        return min_temp;
    }

    public String getPressure() {
        return pressure;
    }

    public String getHumidity() {
        return humidity;
    }

    public String getSunrise() {
        return sunrise+"AM";
    }

    public String getSunset() {
        return sunset+"PM";
    }

    public String getWind_speed() {
        return wind_speed;
    }

    public String getWind_direction() {
        return wind_direction;
    }

    public String getRain() {
        return rain;
    }

    public String getHour(){ return hour;}

    public void setCity(JSONObject jsonObject) throws JSONException {
        this.city = jsonObject.get("name").toString();
    }

    public void setCondition(JSONObject jsonObject) throws JSONException {
        JSONArray jsonArray = jsonObject.getJSONArray("weather");
        JSONObject jsonObject1 = jsonArray.getJSONObject(0);
        this.condition = jsonObject1.get("main").toString();
    }

    public void setTemp(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObject2 = jsonObject.getJSONObject("main");
        this.temp = jsonObject2.get("temp").toString()+"°";
        if (this.temp.contains("."))
            this.temp = this.temp.substring(0,this.temp.lastIndexOf('.'))+"°";
    }

    public void setMax_temp(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObject2 = jsonObject.getJSONObject("main");
        this.max_temp = jsonObject2.get("temp_max").toString()+"°";
        if (this.max_temp.contains("."))
            this.max_temp = this.max_temp.substring(0,this.max_temp.lastIndexOf('.'))+"°";
    }

    public void setMin_temp(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObject2 = jsonObject.getJSONObject("main");
        this.min_temp = jsonObject2.get("temp_min").toString()+"°";
        if (this.min_temp.contains("."))
            this.min_temp = this.min_temp.substring(0,this.min_temp.lastIndexOf('.'))+"°";
    }

    public void setPressure(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObject2 = jsonObject.getJSONObject("main");
        this.pressure = jsonObject2.get("pressure").toString();
    }

    public void setHumidity(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObject2 = jsonObject.getJSONObject("main");
        this.humidity = jsonObject2.get("humidity").toString();
    }

    public void setSunrise(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObject3 = jsonObject.getJSONObject("sys");
        this.sunrise = getTime(Long.parseLong(jsonObject3.get("sunrise").toString()));
    }

    public void setSunset(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObject3 = jsonObject.getJSONObject("sys");
        this.sunset = getTime(Long.parseLong(jsonObject3.get("sunset").toString()));
    }

    public void setWind_speed(JSONObject jsonObject) throws JSONException {
        this.wind_speed = jsonObject.getJSONObject("wind").get("speed").toString();
    }

    public void setWind_direction(JSONObject jsonObject) throws JSONException {
        this.wind_direction = jsonObject.getJSONObject("wind").get("deg").toString();
    }

    public void setRain(JSONObject jsonObject) throws JSONException {
        JSONObject jsonObject4 = jsonObject.getJSONObject("wind");
        this.rain = jsonObject4.get("speed").toString();
    }

    public void setHour(JSONObject jsonObject) throws JSONException{
        this.hour = new SimpleDateFormat("hh aa").format(new Date(Long.parseLong(jsonObject.get("dt").toString()) * 1000));
        if (this.hour.contains("0"))
            this.hour = this.hour.replace("0","");
        this.hour = this.hour.replaceAll(" ","");
    }

    public String getTime(Long l) {
        Date date = new Date(l);
        return new SimpleDateFormat("h:mm").format(new Date(l * 1000));
    }
}
