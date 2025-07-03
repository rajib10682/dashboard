import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { TabViewModule } from 'primeng/tabview';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { ChartModule } from 'primeng/chart';
import { ApiService } from '../../services/api.service';
import { TrendDataPoint } from '../../models/plan.model';

@Component({
  selector: 'app-trends',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardModule,
    TabViewModule,
    DropdownModule,
    ButtonModule,
    ChartModule
  ],
  templateUrl: './trends.component.html',
  styleUrls: ['./trends.component.css']
})
export class TrendsComponent implements OnInit {
  quarterlyData: any = {};
  dailyData: any = {};
  
  selectedDataId: number | null = null;
  selectedDays: number = 30;
  
  dataIdOptions = [
    { label: 'All Data', value: null },
    { label: 'Data ID 1', value: 1 },
    { label: 'Data ID 2', value: 2 },
    { label: 'Data ID 3', value: 3 }
  ];
  
  daysOptions = [
    { label: '7 Days', value: 7 },
    { label: '30 Days', value: 30 },
    { label: '60 Days', value: 60 },
    { label: '90 Days', value: 90 }
  ];

  chartOptions = {
    responsive: true,
    maintainAspectRatio: false,
    plugins: {
      legend: {
        position: 'top'
      },
      title: {
        display: true,
        text: 'Execution Time Trends'
      }
    },
    scales: {
      y: {
        beginAtZero: true,
        title: {
          display: true,
          text: 'Median Execution Time (ms)'
        }
      },
      x: {
        title: {
          display: true,
          text: 'Time Period'
        }
      }
    }
  };

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loadQuarterlyTrends();
    this.loadDailyTrends();
  }

  loadQuarterlyTrends() {
    this.apiService.getQuarterlyTrends().subscribe({
      next: (trends: TrendDataPoint[]) => {
        this.quarterlyData = {
          labels: trends.map(t => t.period),
          datasets: [
            {
              label: 'Median Execution Time',
              data: trends.map(t => t.medianValue),
              borderColor: '#42A5F5',
              backgroundColor: 'rgba(66, 165, 245, 0.2)',
              fill: true,
              tension: 0.4
            }
          ]
        };
      },
      error: (error) => {
        console.error('Error loading quarterly trends:', error);
      }
    });
  }

  loadDailyTrends() {
    this.apiService.getDailyTrends(this.selectedDataId || undefined, this.selectedDays).subscribe({
      next: (trends: TrendDataPoint[]) => {
        this.dailyData = {
          labels: trends.map(t => t.period),
          datasets: [
            {
              label: 'Median Execution Time',
              data: trends.map(t => t.medianValue),
              borderColor: '#66BB6A',
              backgroundColor: 'rgba(102, 187, 106, 0.2)',
              fill: true,
              tension: 0.4
            }
          ]
        };
      },
      error: (error) => {
        console.error('Error loading daily trends:', error);
      }
    });
  }

  onDailyFiltersChange() {
    this.loadDailyTrends();
  }

  refreshData() {
    this.loadQuarterlyTrends();
    this.loadDailyTrends();
  }
}
