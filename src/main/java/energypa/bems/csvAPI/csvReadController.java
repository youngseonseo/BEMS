package energypa.bems.csvAPI;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
@RequestMapping("/api")
@CrossOrigin
@Tag(name="csv Read API", description = "csv 파일 읽기 API")
public class csvReadController {

    @GetMapping(value = "/readCsv", produces = "application/json")
    public ResponseEntity<?> readCsv(){
        // 추후 return 할 데이터 목록
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();

        // return data key 목록
        List<String> headerList = new ArrayList<String>();

        try{
            BufferedReader br = Files.newBufferedReader(Paths.get("preprocessed_data/아파트_동별_소비전력_2022-07-18~2023-08-30.csv"));
            String line = "";

            while((line = br.readLine()) != null){
                List<String> stringList = new ArrayList<String>();
                String stringArray[] = line.split(",");
                stringList = Arrays.asList(stringArray);

                // csv 1열 데이터를 header로 인식
                if(headerList.size() == 0){
                    headerList = stringList;
                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    // header 컬럼 개수를 기준으로 데이터 set
                    for(int i = 0; i < headerList.size(); i++){
                        map.put(headerList.get(i), stringList.get(i));
                    }
                    mapList.add(map);
                }
            }
            System.out.println(mapList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(mapList, HttpStatus.OK);
    }
}