import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ServiceClassComponent } from './list/service-class.component';
import { ServiceClassDetailComponent } from './detail/service-class-detail.component';
import { ServiceClassUpdateComponent } from './update/service-class-update.component';
import { ServiceClassDeleteDialogComponent } from './delete/service-class-delete-dialog.component';
import { ServiceClassRoutingModule } from './route/service-class-routing.module';

@NgModule({
  imports: [SharedModule, ServiceClassRoutingModule],
  declarations: [ServiceClassComponent, ServiceClassDetailComponent, ServiceClassUpdateComponent, ServiceClassDeleteDialogComponent],
  entryComponents: [ServiceClassDeleteDialogComponent],
})
export class ServiceClassModule {}
