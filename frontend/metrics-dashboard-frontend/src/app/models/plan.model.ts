export interface Plan {
  id: number;
  planName: string;
  medianExecutionTime: number;
  avgItems: number;
  dataId: number;
  createdDate: string;
  colorCode: string;
}

export interface SummaryResponse {
  totalPlans: number;
  avgMedianTime: number;
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
