import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TableModule } from 'primeng/table';
import { DropdownModule } from 'primeng/dropdown';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { TagModule } from 'primeng/tag';
import { ApiService, PageResponse } from '../../services/api.service';
import { Plan, SummaryResponse } from '../../models/plan.model';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    TableModule,
    DropdownModule,
    ButtonModule,
    CardModule,
    TagModule
  ],
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {
  plans: Plan[] = [];
  summary: SummaryResponse | null = null;
  loading = false;
  totalRecords = 0;
  
  selectedDataId: number | null = null;
  dataIdOptions = [
    { label: 'All Data', value: null },
    { label: 'Data ID 1', value: 1 },
    { label: 'Data ID 2', value: 2 },
    { label: 'Data ID 3', value: 3 }
  ];

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loadDashboardData();
    this.loadSummary();
  }

  loadDashboardData(event?: any) {
    this.loading = true;
    
    const page = event ? event.first / event.rows : 0;
    const size = event ? event.rows : 10;
    const sortBy = event?.sortField || 'planId';
    const sortDir = event?.sortOrder === 1 ? 'asc' : 'desc';

    this.apiService.getDashboardData(this.selectedDataId || undefined, page, size, sortBy, sortDir)
      .subscribe({
        next: (response: PageResponse<Plan>) => {
          this.plans = response.content;
          this.totalRecords = response.totalElements;
          this.loading = false;
        },
        error: (error) => {
          console.error('Error loading dashboard data:', error);
          this.loading = false;
        }
      });
  }

  loadSummary() {
    this.apiService.getDashboardSummary(this.selectedDataId || undefined)
      .subscribe({
        next: (summary) => {
          this.summary = summary;
        },
        error: (error) => {
          console.error('Error loading summary:', error);
        }
      });
  }

  onDataIdChange() {
    this.loadDashboardData();
    this.loadSummary();
  }

  getColorSeverity(colorCode: string): "success" | "secondary" | "info" | "warning" | "danger" | "contrast" | undefined {
    switch (colorCode) {
      case 'red': return 'danger';
      case 'amber': return 'warning';
      case 'green': return 'success';
      default: return 'info';
    }
  }

  openTrends() {
    window.open('/trends', '_blank');
  }

  exportData() {
    this.apiService.exportToCsv(this.selectedDataId || undefined)
      .subscribe({
        next: (blob) => {
          const url = window.URL.createObjectURL(blob);
          const a = document.createElement('a');
          a.href = url;
          a.download = `metrics_export_${new Date().toISOString().split('T')[0]}.csv`;
          a.click();
          window.URL.revokeObjectURL(url);
        },
        error: (error) => {
          console.error('Error exporting data:', error);
        }
      });
  }

  deletePlan(planId: number) {
    if (confirm('Are you sure you want to delete this plan? This will also delete all related overrides and items.')) {
      this.apiService.deletePlan(planId).subscribe({
        next: () => {
          this.loadDashboardData();
          this.loadSummary();
        },
        error: (error) => {
          console.error('Error deleting plan:', error);
        }
      });
    }
  }
}
