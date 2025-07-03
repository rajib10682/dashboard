import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { CardModule } from 'primeng/card';
import { InputNumberModule } from 'primeng/inputnumber';
import { ButtonModule } from 'primeng/button';
import { MessageModule } from 'primeng/message';
import { MessagesModule } from 'primeng/messages';
import { ApiService } from '../../services/api.service';
import { ThresholdConfig } from '../../models/plan.model';

@Component({
  selector: 'app-configuration',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule,
    CardModule,
    InputNumberModule,
    ButtonModule,
    MessageModule,
    MessagesModule
  ],
  templateUrl: './configuration.component.html',
  styleUrls: ['./configuration.component.css']
})
export class ConfigurationComponent implements OnInit {
  thresholds: ThresholdConfig = {
    redThreshold: 100,
    amberThreshold: 50,
    greenThreshold: 25
  };
  
  loading = false;
  saveSuccess = false;
  errorMessage = '';

  constructor(private apiService: ApiService) {}

  ngOnInit() {
    this.loadThresholds();
  }

  loadThresholds() {
    this.apiService.getThresholds().subscribe({
      next: (thresholds) => {
        this.thresholds = thresholds;
      },
      error: (error) => {
        console.error('Error loading thresholds:', error);
        this.errorMessage = 'Failed to load threshold configuration';
      }
    });
  }

  saveThresholds() {
    if (!this.validateThresholds()) {
      this.errorMessage = 'Thresholds must be in descending order: Red > Amber > Green';
      return;
    }

    this.loading = true;
    this.errorMessage = '';
    this.saveSuccess = false;

    this.apiService.updateThresholds(this.thresholds).subscribe({
      next: (updatedThresholds) => {
        this.thresholds = updatedThresholds;
        this.loading = false;
        this.saveSuccess = true;
        setTimeout(() => this.saveSuccess = false, 3000);
      },
      error: (error) => {
        console.error('Error saving thresholds:', error);
        this.loading = false;
        this.errorMessage = 'Failed to save threshold configuration';
      }
    });
  }

  validateThresholds(): boolean {
    return this.thresholds.redThreshold > this.thresholds.amberThreshold &&
           this.thresholds.amberThreshold > this.thresholds.greenThreshold &&
           this.thresholds.greenThreshold > 0;
  }

  resetToDefaults() {
    this.thresholds = {
      redThreshold: 100,
      amberThreshold: 50,
      greenThreshold: 25
    };
    this.errorMessage = '';
    this.saveSuccess = false;
  }
}
