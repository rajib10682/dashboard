package com.metrics.dashboard.config;

import com.metrics.dashboard.entity.Config;
import com.metrics.dashboard.entity.Item;
import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.repository.ConfigRepository;
import com.metrics.dashboard.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private PlanRepository planRepository;
    
    @Autowired
    private ConfigRepository configRepository;
    
    private final Random random = new Random();
    
    @Override
    public void run(String... args) throws Exception {
        if (configRepository.count() == 0) {
            Config config = new Config(100.0, 50.0, 25.0);
            configRepository.save(config);
        }
        
        if (planRepository.count() == 0) {
            createSampleData();
        }
    }
    
    private void createSampleData() {
        String[] planNames = {
            "Customer Onboarding Process",
            "Payment Processing Workflow",
            "Order Fulfillment Pipeline",
            "User Authentication Service",
            "Data Backup Procedure",
            "Report Generation Task",
            "Email Notification System",
            "Inventory Management Process",
            "Quality Assurance Checks",
            "System Health Monitoring"
        };
        
        LocalDate[] quarterEndDates = {
            LocalDate.of(2024, 3, 31),
            LocalDate.of(2024, 6, 30),
            LocalDate.of(2024, 9, 30),
            LocalDate.of(2024, 12, 31)
        };
        
        for (int i = 0; i < planNames.length; i++) {
            Plan plan = new Plan();
            plan.setPlanName(planNames[i]);
            plan.setForDate(quarterEndDates[i % quarterEndDates.length]);
            plan.setDataId(1 + (i % 3));
            
            Double totalExecTime = generateRandomExecutionTime();
            Double onHoldTime = totalExecTime * 0.2;
            Double coreExecTime = totalExecTime - onHoldTime;
            
            com.metrics.dashboard.entity.Override override1 = new com.metrics.dashboard.entity.Override(
                plan, 
                planNames[i] + " - High Priority Override",
                totalExecTime * 1.2,
                onHoldTime * 1.2,
                coreExecTime * 1.2,
                "HIGH_PRIORITY"
            );
            
            com.metrics.dashboard.entity.Override override2 = new com.metrics.dashboard.entity.Override(
                plan,
                planNames[i] + " - Standard Override", 
                totalExecTime,
                onHoldTime,
                coreExecTime,
                "STANDARD"
            );
            
            Item item1 = new Item("ITEM_" + i + "_1", override1, "USD", "system", "system");
            item1.setItemValueMonth1(100.0 + random.nextDouble() * 50);
            item1.setItemValueMonth2(110.0 + random.nextDouble() * 50);
            item1.setItemValueMonth3(120.0 + random.nextDouble() * 50);
            
            Item item2 = new Item("ITEM_" + i + "_2", override2, "EUR", "system", "system");
            item2.setItemValueMonth1(80.0 + random.nextDouble() * 40);
            item2.setItemValueMonth2(85.0 + random.nextDouble() * 40);
            item2.setItemValueMonth3(90.0 + random.nextDouble() * 40);
            
            override1.setItems(Arrays.asList(item1));
            override2.setItems(Arrays.asList(item2));
            plan.setOverrides(Arrays.asList(override1, override2));
            
            planRepository.save(plan);
        }
    }
    
    private Double generateRandomExecutionTime() {
        double[] ranges = {15.0, 35.0, 65.0, 120.0};
        return ranges[random.nextInt(ranges.length)] + random.nextDouble() * 20;
    }
}
