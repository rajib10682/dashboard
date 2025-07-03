package com.metrics.dashboard.config;

import com.metrics.dashboard.entity.Config;
import com.metrics.dashboard.entity.Detail;
import com.metrics.dashboard.entity.Plan;
import com.metrics.dashboard.repository.ConfigRepository;
import com.metrics.dashboard.repository.PlanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
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
        
        for (int i = 0; i < planNames.length; i++) {
            Plan plan = new Plan();
            plan.setPlanName(planNames[i]);
            plan.setMedianExecutionTime(generateRandomExecutionTime());
            plan.setAvgItems(50 + random.nextInt(200));
            plan.setDataId(1 + (i % 3));
            plan.setCreatedDate(LocalDateTime.now().minusDays(random.nextInt(90)));
            
            com.metrics.dashboard.entity.Override override1 = new com.metrics.dashboard.entity.Override(plan, plan.getMedianExecutionTime() * 1.2);
            com.metrics.dashboard.entity.Override override2 = new com.metrics.dashboard.entity.Override(plan, plan.getMedianExecutionTime() * 0.8);
            
            Detail detail1 = new Detail(override1, "Override detail for " + planNames[i] + " - High priority");
            Detail detail2 = new Detail(override2, "Override detail for " + planNames[i] + " - Low priority");
            
            override1.setDetails(Arrays.asList(detail1));
            override2.setDetails(Arrays.asList(detail2));
            plan.setOverrides(Arrays.asList(override1, override2));
            
            planRepository.save(plan);
        }
    }
    
    private Double generateRandomExecutionTime() {
        double[] ranges = {15.0, 35.0, 65.0, 120.0};
        return ranges[random.nextInt(ranges.length)] + random.nextDouble() * 20;
    }
}
