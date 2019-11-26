package com.example.hatchtracksensor;

import com.amazonaws.util.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Calendar;

import android.util.Log;
import android.text.format.DateFormat;

public class RestApi {
    private static final String HEADER_ACCESS_TOKEN = "Access-Token";

    public static JSONArray json_array;

    public static JSONObject registerUser(String email, String password, String given_name, String family_name) {
        JSONObject userJSON = null;
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);
            json.put("given_name", given_name);
            json.put("family_name", family_name);

            String body = json.toString();
            Log.i("body","body: "+body);
            String requestURL = "https://db.hatchtrack.com:18888/register_user";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String data = IOUtils.toString(in);
            json = new JSONObject(data);
            userJSON = json;
            //userJSON = json.getString("new_user");
            Log.i("registerUser",userJSON.toString());
        } catch (Exception e) {
            Log.i("failedRegiser","failedRegiser");

            e.printStackTrace();
        }

        return userJSON;
    }

    public static JSONObject confirmUser(String email, String confirm_code) {
        JSONObject userJSON = null;
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("confirm_code", confirm_code);

            String body = json.toString();
            Log.i("body","body: "+body);
            String requestURL = "https://db.hatchtrack.com:18888/register_confirm";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String data = IOUtils.toString(in);
            json = new JSONObject(data);
            userJSON = json;
            //userJSON = json.getString("new_user");
            Log.i("register_confirm",userJSON.toString());
        } catch (Exception e) {
            Log.i("failedRegiser","failedRegiser");

            e.printStackTrace();
        }

        return userJSON;
    }

    public static String postUserAuth(String email, String password) {
        String accessToken = "";
        try {
            JSONObject json = new JSONObject();
            json.put("email", email);
            json.put("password", password);

            String body = json.toString();
            String requestURL = "https://db.hatchtrack.com:18888/auth";
            URL url = new URL(requestURL);

            Log.i("postUserAuth",body);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String data = IOUtils.toString(in);
            json = new JSONObject(data);
            accessToken = json.getString("accessToken");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return accessToken;
    }

    public static boolean postUserNewPeep(String accessToken, PeepUnit peepUnit) {
        String peepUUID = peepUnit.getUUID();
        boolean status = true;
        try {

            JSONObject json = new JSONObject();
            json.put("peepUUID", peepUUID.replace("\n", ""));
            String body = json.toString();
            String requestURL = "https://db.hatchtrack.com:18888/api/v1/user/peep";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);
            writer.flush();
            writer.close();
            out.close();

            int code = conn.getResponseCode();
            if (200 == code) {
                status = true;
            } else {
                status = false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static boolean deleteUserPeep(String accessToken, PeepUnit peepUnit) {
        String peepUUID = peepUnit.getUUID();
        boolean status = true;

        try {
            String requestURL = "https://db.hatchtrack.com:18888/api/v1/user/peep?peepUUID=" + peepUUID;
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);

            int code = conn.getResponseCode();
            if (200 == code) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static String getPeepName(String accessToken, PeepUnit peepUnit) {
        String peepUUID = peepUnit.getUUID();
        String peepName = "";

        try {
            String requestURL = "https://db.hatchtrack.com:18888/api/v1/peep/name?peepUUID=" + peepUUID;
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String data = IOUtils.toString(in);
            JSONObject json = new JSONObject(data);
            try {
                peepName = json.getString("peepName");
            } catch (Exception e) {
                peepName = "N/A";
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return peepName;
    }

    public static boolean postPeepName(String accessToken, PeepUnit peepUnit) {
        boolean status = true;

        try {
            JSONObject json = new JSONObject();
            json.put("peepUUID", peepUnit.getUUID().replace("\n", ""));
            json.put("peepName", peepUnit.getName().replace("\n", ""));
            String body = json.toString();
            String requestURL = "https://db.hatchtrack.com:18888/api/v1/peep/name";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            int code = conn.getResponseCode();
            if (200 == code) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static boolean postNotificationToken(String accessToken, String token, String platform, String email) {
        boolean status = true;

        try {
            JSONObject json = new JSONObject();
            json.put("push_notif_token", token.replace("\n", ""));
            json.put("platform", platform.replace("\n", ""));
            json.put("email", email.replace("\n", ""));
            String body = json.toString();

            Log.i("postNotificationToken", body);

            String requestURL = "https://db.hatchtrack.com:18888/api/v1/user/notification_token_platform";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            int code = conn.getResponseCode();
            if (200 == code) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return status;
    }

    public static ArrayList<String> getPeepUUIDs(String accessToken) {
        ArrayList<String> list = new ArrayList<String>();
        try {
            String reqURL = "https://db.hatchtrack.com:18888/api/v1/user/peeps";
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String  data= IOUtils.toString(in);
            JSONObject json = new JSONObject(data);
            JSONArray array = json.getJSONArray("peepUUIDs");
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return  list;
    }

    public static ArrayList<String> getHatchUUIDs(String accessToken, PeepUnit peepUnit) {
        ArrayList<String> list = new ArrayList<String>();
        String peepUUID = peepUnit.getUUID();

        try {
            String reqURL = "https://db.hatchtrack.com:18888/api/v1/peep/hatches?peepUUID=" + peepUUID;
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String  data= IOUtils.toString(in);
            JSONObject json = new JSONObject(data);
            JSONArray array = json.getJSONArray("hatchUUIDs");
            for (int i = 0; i < array.length(); i++) {
                list.add(array.getString(i));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static JSONObject PushNotificationSettings(String accessToken){
        JSONObject json = null;

        try {
            String reqURL = "https://db.hatchtrack.com:18888/api/v1/user/push_notification_settings";
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String data = IOUtils.toString(in);
            json = new JSONObject(data);
            Log.i("NotificationSettings",json.toString());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }



    public static JSONObject getAllHatchData(String accessToken, String peepUUID) {
        JSONObject json = new JSONObject();

        try {
            String reqURL = "https://db.hatchtrack.com:18888/api/v1/peep/hatches_all_data?peepUUID=" + peepUUID;
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);
            Log.i("getAllHatchData",accessToken + " ---- " + reqURL);
            InputStream in = new BufferedInputStream(conn.getInputStream());
            String data = IOUtils.toString(in);
            json = new JSONObject(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    public static JSONObject getAllHatchDataEmail(String accessToken, String email) {
        JSONObject json = new JSONObject();

        try {
            String reqURL = "https://db.hatchtrack.com:18888/api/v1/peep/hatches_all_data_email?email=" + email;
            URL url = new URL(reqURL);

            Log.i("getAllHatchDataEmail",reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String data = IOUtils.toString(in);
            json = new JSONObject(data);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;
    }

    public static PeepHatch getHatch(String accessToken, String hatchUUID) {
        PeepHatch peepHatch = new PeepHatch();

        try {
            String reqURL = "https://db.hatchtrack.com:18888/api/v1/hatch?hatchUUID=" + hatchUUID;
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String data = IOUtils.toString(in);
            JSONObject json = new JSONObject(data);
            peepHatch.setUUID(hatchUUID);
            peepHatch.setStartUnixTimestamp(json.getLong("startUnixTimestamp"));
            peepHatch.setEndUnixTimestamp(json.getLong("endUnixTimestamp"));
            peepHatch.setMeasureIntervalMin(json.getInt("measureIntervalMin"));
            peepHatch.setTemperatureOffsetCelsius((float) json.getDouble("temperatureOffsetCelsius"));
            peepHatch.setSpeciesUUID(json.getString("speciesUUID"));
            if( !json.isNull("eggCount"))peepHatch.setEggCount(json.getInt("eggCount"));
            peepHatch.setHatchName(json.getString("hatchName"));
            peepHatch.setHatchNotes(json.getString("notes"));
            if( !json.isNull("hatchedCount"))peepHatch.setHatchedCount(json.getInt("hatchedCount"));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return peepHatch;
    }

    public static boolean postHatchEnd(String accessToken, PeepHatch peepHatch) {
        boolean status = false;

        try {
            JSONObject json = new JSONObject();
            json.put("hatchUUID", peepHatch.getUUID().replace("\n", ""));
            json.put("hatchedCount", peepHatch.getHatchedCount());
            String body = json.toString();

            String requestURL = "https://db.hatchtrack.com:18888/api/v1/hatch/end";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            int code = conn.getResponseCode();
            if (200 == code) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }

        return status;
    }

    public static boolean postHatchNotes(String accessToken, PeepHatch peepHatch) {
        boolean status = false;
        Log.i("postHatchNotes","postHatchNotes");
        try {
            JSONObject json = new JSONObject();
            json.put("hatchUUID", peepHatch.getUUID().replace("\n", ""));
            json.put("hatchNotes", peepHatch.getHatchNotes());
            String body = json.toString();
            Log.i("postHatchNotes", body);
            String requestURL = "https://db.hatchtrack.com:18888/api/v1/hatch/notes";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            int code = conn.getResponseCode();
            if (200 == code) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            Log.i("e",e.toString());
            e.printStackTrace();
            status = false;
        }

        return status;
    }

    public static boolean resumeHatch(String accessToken, PeepHatch peepHatch) {
        boolean status = false;

        try {
            JSONObject json = new JSONObject();
            json.put("hatchUUID", peepHatch.getUUID().replace("\n", ""));
            String body = json.toString();

            String requestURL = "https://db.hatchtrack.com:18888/api/v1/hatch/resume";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            int code = conn.getResponseCode();
            if (200 == code) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }

        return status;
    }

    public static boolean postNewPeepHatch(String accessToken, PeepUnit peepUnit, PeepHatch peepHatch) {
        boolean status = true;

        try {
            String peepUUID = peepUnit.getUUID();
            long endUnixTimestamp = peepHatch.getEndUnixTimestamp();
            int measureIntervalMin = peepHatch.getMeasureIntervalMin();
            float temperatureOffsetCelsius = peepHatch.getTemperatureOffsetCelsius();
            String speciesUUID = peepHatch.getSpeciesUUID();
            int eggCount = peepHatch.getEggCount();
            String hatchName = peepHatch.getHatchName();

            JSONObject json = new JSONObject();
            json.put("peepUUID", peepUUID.replace("\n", ""));
            json.put("endUnixTimestamp", endUnixTimestamp);
            json.put("measureIntervalMin", measureIntervalMin);
            json.put("temperatureOffsetCelsius", temperatureOffsetCelsius);
            json.put("temperatureOffsetCelsius", temperatureOffsetCelsius);
            json.put("temperatureOffsetCelsius", temperatureOffsetCelsius);
            json.put("speciesUUID", speciesUUID);
            json.put("eggCount", eggCount);
            json.put("hatchName", hatchName);
            String body = json.toString();

            Log.i("postNewPeepHatch JSON: ", body);


            String requestURL = "https://db.hatchtrack.com:18888/api/v1/peep/hatch";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            int code = conn.getResponseCode();
            if (200 == code) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }

        return status;
    }

    public static boolean postReconfigHatch(String accessToken, PeepHatch peepHatch) {
        boolean status = true;

        try {
            String hatchUUID = peepHatch.getUUID();
            long endUnixTimestamp = peepHatch.getEndUnixTimestamp();
            int measureIntervalMin = peepHatch.getMeasureIntervalMin();
            float temperatureOffsetCelsius = peepHatch.getTemperatureOffsetCelsius();
            String speciesUUID = peepHatch.getSpeciesUUID();
            int eggCount = peepHatch.getEggCount();
            String hatchName = peepHatch.getHatchName();

            JSONObject json = new JSONObject();
            json.put("hatchUUID", hatchUUID);
            json.put("endUnixTimestamp", endUnixTimestamp);
            //json.put("measureIntervalMin", measureIntervalMin);
            json.put("temperatureOffsetCelsius", temperatureOffsetCelsius);
            json.put("speciesUUID", speciesUUID);
            json.put("eggCount", eggCount);
            json.put("hatchName", hatchName);
            String body = json.toString();

            Log.i("HATCH","body: "+body);

            String requestURL = "https://db.hatchtrack.com:18888/api/v1/hatch/reconfig";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            int code = conn.getResponseCode();
            if (200 == code) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }

        return status;
    }

    public static PeepMeasurement getPeepLastMeasure(String accessToken, PeepUnit peepUnit) {
        PeepMeasurement peepMeasurement;
        String peepUUID = peepUnit.getUUID();

        try {
            String reqURL = "https://db.hatchtrack.com:18888/api/v1/peep/measure/last?peepUUID=" + peepUUID;
            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String  data= IOUtils.toString(in);
            JSONObject json = new JSONObject(data);
            peepMeasurement = new PeepMeasurement(
                    json.getString("hatchUUID"),
                    json.getLong("unixTimestamp"),
                    json.getDouble("humidity"),
                    json.getDouble("temperature")
            );
        } catch (Exception e) {
            e.printStackTrace();
            peepMeasurement = new PeepMeasurement(
                    "n/a",
                    0,
                    0,
                    0);
        }

        return peepMeasurement;
    }

    public static JSONArray getPeepTimeline(String accessToken, PeepUnit peepUnit, int timeline_mode, String hatchUUID) {
        PeepMeasurement peepMeasurement;

        Log.i("TIMELINE 0:","gethatchUUIDTimeline: "+hatchUUID);
        int last = 0;
        if(hatchUUID.equals("") && peepUnit!=null) {
            ArrayList<String> hatchUUIDs = RestApi.getHatchUUIDs(accessToken, peepUnit);
            if (0 < hatchUUIDs.size()) {
                last = hatchUUIDs.size() - 1;
            }
            hatchUUIDs.get(last);
            hatchUUID = hatchUUIDs.get(last).toString();
        }
        Log.i("TIMELINE 1:","gethatchUUIDTimeline: "+hatchUUID);
        try {
            String startDate = "";
            String endDate = "";

            // User readable time representation.
            java.text.DateFormat userTime = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            userTime.setTimeZone(TimeZone.getDefault());

            Calendar calendar = Calendar.getInstance();
            calendar.roll(Calendar.DAY_OF_YEAR, timeline_mode*-1+1);
            Date today = calendar.getTime();
            startDate = userTime.format(today);

            calendar.add(Calendar.DAY_OF_YEAR, timeline_mode);
            Date tomorrow = calendar.getTime();
            endDate = userTime.format(tomorrow);

            String reqURL = "https://db.hatchtrack.com:18888/api/v1/peep/measure/timeline?hatchUUID=" + hatchUUID + "&startDate="+ startDate + "&endDate="+ endDate + "&timeline_mode="+ timeline_mode;
            Log.i("URL",reqURL);

            URL url = new URL(reqURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);

            InputStream in = new BufferedInputStream(conn.getInputStream());
            String data= IOUtils.toString(in);

            /*
            JSONObject jsonResponse = new JSONObject(data);
            json_array = jsonResponse.getJSONArray("timeline");

            Log.i("JSON",json_array.toString());
            */
            json_array = new JSONArray(data);
            Log.i("J", data);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return json_array;
    }

    //NOTIFICATIONS SETTINGS TOGGLE SWITCHES
    public static boolean postToggleSwitch(String accessToken, boolean SwitchTempTooHotState, boolean SwitchTempTooColdState, boolean SwitchHumidityOverState, boolean SwitchHumidityUnderState) {
        boolean status = true;

        try {

            JSONObject json = new JSONObject();
            json.put("SwitchTempTooHotState", Boolean.valueOf(SwitchTempTooHotState));
            json.put("SwitchTempTooColdState", Boolean.valueOf(SwitchTempTooColdState));
            json.put("SwitchHumidityOverState", Boolean.valueOf(SwitchHumidityOverState));
            json.put("SwitchHumidityUnderState", Boolean.valueOf(SwitchHumidityUnderState));
            String body = json.toString();

            Log.i("postToggleSwitch: ", body);

            String requestURL = "https://db.hatchtrack.com:18888/api/v1/user/notification_settings";
            URL url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty(HEADER_ACCESS_TOKEN, accessToken);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            OutputStream out = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.write(body);

            writer.flush();
            writer.close();
            out.close();

            int code = conn.getResponseCode();
            if (200 == code) {
                status = true;
            } else {
                status = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            status = false;
        }

        return status;
    }
}
