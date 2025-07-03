import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FileUploadModule } from 'primeng/fileupload';
import { ButtonModule } from 'primeng/button';
import { CardModule } from 'primeng/card';
import { MessageModule } from 'primeng/message';
import { MessagesModule } from 'primeng/messages';
import { ApiService } from '../../services/api.service';

@Component({
  selector: 'app-upload',
  standalone: true,
  imports: [CommonModule, FileUploadModule, ButtonModule, CardModule, MessageModule, MessagesModule],
  templateUrl: './upload.component.html',
  styleUrls: ['./upload.component.css']
})
export class UploadComponent {
  maxFileSize = 10485760; // 10MB
  uploadedFiles: any[] = [];
  messages: any[] = [];
  
  constructor(private apiService: ApiService) {}
  
  onUpload(event: any) {
    const file = event.files[0];
    
    this.messages = [
      { severity: 'info', summary: 'Processing', detail: 'Uploading and processing file...' }
    ];
    
    this.apiService.bulkUpload(file).subscribe({
      next: (blob: Blob) => {
        const url = window.URL.createObjectURL(blob);
        const a = document.createElement('a');
        a.href = url;
        a.download = `bulk_upload_response_${new Date().toISOString().split('T')[0]}.xlsx`;
        a.click();
        window.URL.revokeObjectURL(url);
        
        this.messages = [
          { severity: 'success', summary: 'Success', detail: 'File processed successfully. Response file downloaded.' }
        ];
        
        this.uploadedFiles = event.files;
      },
      error: (error: any) => {
        console.error('Upload error:', error);
        this.messages = [
          { severity: 'error', summary: 'Error', detail: 'Failed to process file. Please check file format and size.' }
        ];
      }
    });
  }
  
  onError(event: any) {
    this.messages = [
      { severity: 'error', summary: 'Upload Error', detail: 'File upload failed. Please check file size and format.' }
    ];
  }
  
  onSelect(event: any) {
    this.messages = [];
    this.uploadedFiles = [];
  }
  
  onClear() {
    this.messages = [];
  }
}
