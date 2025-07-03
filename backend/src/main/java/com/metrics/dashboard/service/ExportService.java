package com.metrics.dashboard.service;

import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.repository.PlanRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.StringWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExportService {
    
    @Autowired
    private PlanRepository planRepository;
    
    public Map<String, String> exportToCsv(Integer dataId) {
        List<Plan> plans;
        if (dataId != null) {
            plans = planRepository.findByDataId(dataId);
        } else {
            plans = planRepository.findAll();
        }
        
        try {
            StringWriter writer = new StringWriter();
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("Plan ID", "Plan Name", "Median Execution Time", "Average Items", "Data ID", "Created Date")
                .build();
            
            try (CSVPrinter csvPrinter = new CSVPrinter(writer, csvFormat)) {
                for (Plan plan : plans) {
                    csvPrinter.printRecord(
                        plan.getId(),
                        plan.getPlanName(),
                        plan.getMedianExecutionTime(),
                        plan.getAvgItems(),
                        plan.getDataId(),
                        plan.getCreatedDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                    );
                }
            }
            
            String filename = "metrics_export_" + 
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss")) + ".csv";
            
            Map<String, String> result = new HashMap<>();
            result.put("csvData", writer.toString());
            result.put("filename", filename);
            
            return result;
            
        } catch (Exception e) {
            throw new RuntimeException("Error generating CSV export", e);
        }
    }
}
