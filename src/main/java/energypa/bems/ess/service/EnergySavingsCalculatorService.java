package energypa.bems.ess.service;


import energypa.bems.energy.repository.BuildingPerMinuteRepository;
import energypa.bems.ess.EssPredictResultRepository;
import energypa.bems.ess.dto.RateConsumptionDto;
import energypa.bems.monitoring.dto.ImplicitTotalConsumption;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnergySavingsCalculatorService {

    private final EssPredictResultRepository essPredictResultRepository;
    private final BuildingPerMinuteRepository buildingPerMinuteRepository;
    public List<Integer> CalculateSavingCosts(){
        List<Integer> priceList = new ArrayList<>();
        Timestamp start = Timestamp.valueOf("2022-08-27 12:00:00");
        Timestamp end = Timestamp.valueOf("2022-08-28 00:00:00");
        Double sumBatteryPower = essPredictResultRepository.findSumBatteryPower(start, end);
        Double touRateByTimestamp = essPredictResultRepository.findTouRateByTimestamp(start);
        RateConsumptionDto rateConsumption = buildingPerMinuteRepository.getRateConsumption(start, end);

        Double total = rateConsumption.getAConsumptionRate() + rateConsumption.getBConsumptionRate() + rateConsumption.getCConsumptionRate();
        Double ARate = rateConsumption.getAConsumptionRate() / total;
        Double BRate = rateConsumption.getBConsumptionRate() / total;
        Double CRate = rateConsumption.getCConsumptionRate() / total;
        priceList.add((int)Math.round(sumBatteryPower * touRateByTimestamp * touRateByTimestamp * ARate));
        priceList.add((int)Math.round(sumBatteryPower * touRateByTimestamp * touRateByTimestamp * BRate));
        priceList.add((int)Math.round(sumBatteryPower * touRateByTimestamp * touRateByTimestamp * CRate));
        return priceList;
    }
}
