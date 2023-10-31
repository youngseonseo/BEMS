package energypa.bems.unitCost.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import energypa.bems.unitCost.domain.UnitCost;
import energypa.bems.unitCost.dto.UnitCostDto;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@Service
@RequestMapping
public class ApiService {

    @Value("${openApi}")
    private String API_KEY;

    private final String REQUEST_URL = "https://bigdata.kepco.co.kr/openapi/v1/powerUsage/contractType.do";

    private final SimpleDateFormat DATE_FMT = new SimpleDateFormat("yyyyMMdd");


    public String makeQueryString(Map<String, String> paramMap) {
        final StringBuilder sb = new StringBuilder();

        paramMap.entrySet().forEach(( entry )->{
            if( sb.length() > 0 ) {
                sb.append('&');
            }
            sb.append(entry.getKey()).append('=').append(entry.getValue());
        });

        return sb.toString();
    }


    private LinkedHashMap<String, String> paramMapSet(LinkedHashMap<String, String> paramMap,UnitCostDto unitCostDto) {
        paramMap.put("year"  , unitCostDto.getYear().toString());                          // 조회연도(YYYY)
        if(unitCostDto.getMonth()<10){
            paramMap.put("month" , "0" + unitCostDto.getMonth());                         // 조회월(MM)
        }
        else{
            paramMap.put("month", unitCostDto.getMonth().toString());
        }
        paramMap.put("metroCd"  , unitCostDto.getMetroCd().toString());                    // 시도코드
        paramMap.put("cityCd"  , unitCostDto.getCityCd().toString());                      // 시군구코드
        paramMap.put("cntrCd"  , "100");                      // 계약종별
        paramMap.put("apiKey"  , API_KEY);                     // 발급받은 인증키
        paramMap.put("returnType"  , "json");                  // 응답포맷
        return paramMap;
    }


    public Double getApi(UnitCostDto unitCostDto) throws IOException {

        LinkedHashMap<String, String> paramMap = new LinkedHashMap<String, String>();
        paramMap = paramMapSet(paramMap,unitCostDto);
        JSONArray result;

        try {
            // Request URL 연결 객체 생성
            URL requestURL = new URL(REQUEST_URL + "?" + makeQueryString(paramMap));   // 쿼리문 완성
            log.info("url={}", requestURL);
            HttpURLConnection conn = (HttpURLConnection) requestURL.openConnection();

            // GET 방식으로 요청
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            conn.setDoInput(true);

            // 응답(Response) 구조 작성
            // Stream -> JSONObject
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            String readline;
            StringBuilder response = new StringBuilder();
            while ((readline = br.readLine()) != null) {
                response.append(readline);
            }
            br.close();
            conn.disconnect();


            // JSON 객체로  변환
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> map = new HashMap<>();
            map= objectMapper.readValue(response.toString(),  new TypeReference<Map<String,Object>>(){});

            List<Map<String, Object>> unitCostList =  (List<Map<String, Object>>) map.get("data");

            for(Map<String, Object> item :unitCostList){

                return  Double.valueOf(item.get("unitCost").toString());
            }

        } catch (IOException e) {
            throw new IllegalArgumentException("open API 정보를 불러오는데 실패했습니다.");
        }
        return null;
    }
}
