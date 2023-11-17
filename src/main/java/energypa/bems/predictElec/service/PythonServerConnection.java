package energypa.bems.predictElec.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.shaded.gson.JsonObject;
import energypa.bems.predictElec.dto.RequestElecDto;
import energypa.bems.predictElec.dto.ReturnElecDto;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Service
@RequestMapping
public class PythonServerConnection {

    public String createJSONstring( String timestamp, Integer Building, Integer Floor, Integer Consumption){
        JsonObject obj = new JsonObject();

        Map<String, String> map = new HashMap<>();
        map.put("TIMESTAMP", timestamp);
        map.put("BUILDING", String.valueOf(Building));
        map.put("FLOOR", String.valueOf(Floor));
        map.put("CONSUMPTION(W)", String.valueOf(Consumption));
        JSONObject jo = new JSONObject(map);
        return obj.toString();
    }


    public ReturnElecDto PredictElec(RequestElecDto requestElecDto) {

        try {
            // 파이썬 AI 서버의 URL
            String pythonServerUrl = "http://127.0.0.1:10000/predict/elec/";
            Map<String,Object> params= new LinkedHashMap<>();
            params.put("TIMESTAMP", requestElecDto.getTimestamp());
            params.put("BUILDING", requestElecDto.getBuilding());
            params.put("FLOOR", requestElecDto.getFloor());
            params.put("CONSUMPTION(W)", requestElecDto.getConsumption());

            StringBuilder postData = new StringBuilder();
            for(Map.Entry<String, Object> param: params.entrySet()){
                if(postData.length()!=0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()),"UTF-8"));
            }
            System.out.println("postData = " + postData);
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");


            // HTTP 연결 설정
            URL url = new URL(pythonServerUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.getOutputStream().write(postDataBytes);


            // 응답 코드 확인
            int responseCode = connection.getResponseCode();
            System.out.println("Response Code: " + responseCode);

            // 응답 데이터 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            StringBuilder response = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();
            connection.disconnect();

            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = new HashMap<>();
            map= objectMapper.readValue(response.toString(),  new TypeReference<Map<String,Object>>(){});

            return new ReturnElecDto(Timestamp.valueOf(map.get("TIMESTAMP").toString()), Double.valueOf(map.get("PREDICTION").toString()));


        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
