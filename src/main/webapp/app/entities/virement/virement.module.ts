import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { VirementComponent } from './list/virement.component';
import { VirementDetailComponent } from './detail/virement-detail.component';
import { VirementUpdateComponent } from './update/virement-update.component';
import { VirementDeleteDialogComponent } from './delete/virement-delete-dialog.component';
import { VirementRoutingModule } from './route/virement-routing.module';

@NgModule({
  imports: [SharedModule, VirementRoutingModule],
  declarations: [VirementComponent, VirementDetailComponent, VirementUpdateComponent, VirementDeleteDialogComponent],
  entryComponents: [VirementDeleteDialogComponent],
})
export class VirementModule {}
