package energypa.bems.predictElec.service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import energypa.bems.energy.domain.FloorOneHour;
import energypa.bems.predictElec.dto.RequestElecDto;
import energypa.bems.predictElec.dto.ReturnElecDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
@RequestMapping
public class PythonServerConnection {

    public List<ReturnElecDto> PredictElec(RequestElecDto requestElecDto) throws IOException {


        try {
            // 파이썬 AI 서버의 URL
            String pythonServerUrl = "http://127.0.0.1:10000/predict/elec_24/";
            URL requestURL = new URL(pythonServerUrl);   // 쿼리문 완성

            HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();

            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-type", "application/json");
            connection.setDoOutput(true);
            connection.setConnectTimeout(5000); //연결제한 시간 설정. 5초 간 연결시도
            connection.setDoInput(true);

            try (DataOutputStream dataOutputStream = new DataOutputStream(connection.getOutputStream());) {


                // RequestBody 동적으로 구성
                ObjectMapper objectMapper = new ObjectMapper();
                String requestBody = objectMapper.writeValueAsString(requestElecDto);

                // RequestBody 전송
                try (OutputStream os = connection.getOutputStream();
                     OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8")) {
                    osw.write(requestBody);
                    osw.flush();
                }

                // 응답(Response) 구조 작성
                // Stream -> JSONObject
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
                String readline;
                Double predictValue;
                StringBuilder response = new StringBuilder();
                while ((readline = br.readLine()) != null) {
                    response.append(readline);
                }


                br.close();
                connection.disconnect();

                // JSON 객체로  변환
                Map<String, Object> map = objectMapper.readValue(response.toString(),  new TypeReference<Map<String,Object>>(){});

                List<Timestamp> timestampList =  (List<Timestamp>) map.get("TIMESTAMP");
                List<Double> prediction =  (List<Double>) map.get("PREDICTION");
                List<ReturnElecDto> returnElecDtoList = new ArrayList<>();

                for (int i=0; i<24; i++){
                    ReturnElecDto returnElecDto = new ReturnElecDto(Timestamp.valueOf(String.valueOf(timestampList.get(i))), Double.parseDouble(String.valueOf(prediction.get(i))), requestElecDto.getBuilding(), requestElecDto.getFloor());
                    returnElecDtoList.add(returnElecDto);
                }

                connection.disconnect();
                return returnElecDtoList;

            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}


