import { Routes } from '@angular/router';
import { DashboardComponent } from './components/dashboard/dashboard.component';
import { ConfigurationComponent } from './components/configuration/configuration.component';
import { TrendsComponent } from './components/trends/trends.component';

export const routes: Routes = [
  { path: '', redirectTo: '/dashboard', pathMatch: 'full' },
  { path: 'dashboard', component: DashboardComponent },
  { path: 'configuration', component: ConfigurationComponent },
  { path: 'trends', component: TrendsComponent },
  { path: '**', redirectTo: '/dashboard' }
];
