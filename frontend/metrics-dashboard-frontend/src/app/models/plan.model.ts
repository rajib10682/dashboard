export interface Plan {
  planId: number;
  planName: string;
  forDate: string;
  dataId: number;
  avgCoreExecutionTime: number;
  colorCode: string;
}

export interface SummaryResponse {
  totalPlans: number;
  avgExecutionTime: number;
  totalItems: number;
}

export interface ThresholdConfig {
  redThreshold: number;
  amberThreshold: number;
  greenThreshold: number;
}

export interface TrendDataPoint {
  period: string;
  medianValue: number;
  date: string;
}
