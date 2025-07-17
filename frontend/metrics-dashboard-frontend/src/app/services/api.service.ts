import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../environments/environment';
import { Plan, SummaryResponse, ThresholdConfig, TrendDataPoint } from '../models/plan.model';

export interface PageResponse<T> {
  content: T[];
  totalElements: number;
  totalPages: number;
  size: number;
  number: number;
}

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private apiUrl = environment.apiUrl;
  private exportApiUrl = environment.exportApiUrl;

  constructor(private http: HttpClient) {}

  getDashboardData(dataId?: number, page: number = 0, size: number = 10, sortBy: string = 'planId', sortDir: string = 'asc'): Observable<PageResponse<Plan>> {
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortBy', sortBy)
      .set('sortDir', sortDir);
    
    if (dataId) {
      params = params.set('dataId', dataId.toString());
    }

    return this.http.get<PageResponse<Plan>>(`${this.apiUrl}/dashboard/data`, { params });
  }

  getDashboardSummary(dataId?: number): Observable<SummaryResponse> {
    let params = new HttpParams();
    if (dataId) {
      params = params.set('dataId', dataId.toString());
    }
    return this.http.get<SummaryResponse>(`${this.apiUrl}/dashboard/summary`, { params });
  }

  getThresholds(): Observable<ThresholdConfig> {
    return this.http.get<ThresholdConfig>(`${this.apiUrl}/config/thresholds`);
  }

  updateThresholds(thresholds: ThresholdConfig): Observable<ThresholdConfig> {
    return this.http.put<ThresholdConfig>(`${this.apiUrl}/config/thresholds`, thresholds);
  }

  getQuarterlyTrends(): Observable<TrendDataPoint[]> {
    return this.http.get<TrendDataPoint[]>(`${this.apiUrl}/graph/quarterly`);
  }

  getDailyTrends(dataId?: number, days: number = 30): Observable<TrendDataPoint[]> {
    let params = new HttpParams().set('days', days.toString());
    if (dataId) {
      params = params.set('dataId', dataId.toString());
    }
    return this.http.get<TrendDataPoint[]>(`${this.apiUrl}/graph/daily`, { params });
  }

  exportToCsv(dataId?: number): Observable<Blob> {
    let params = new HttpParams();
    if (dataId) {
      params = params.set('dataId', dataId.toString());
    }
    return this.http.get(`${this.exportApiUrl}/download/csv`, { 
      params, 
      responseType: 'blob' 
    });
  }

  deletePlan(planId: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/master/${planId}`);
  }

  bulkUpload(file: File): Observable<Blob> {
    const formData = new FormData();
    formData.append('file', file);
    
    return this.http.post(`${this.exportApiUrl}/upload/bulk`, formData, {
      responseType: 'blob'
    });
  }
}
