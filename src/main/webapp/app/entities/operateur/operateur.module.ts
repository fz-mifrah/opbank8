import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { OperateurComponent } from './list/operateur.component';
import { OperateurDetailComponent } from './detail/operateur-detail.component';
import { OperateurUpdateComponent } from './update/operateur-update.component';
import { OperateurDeleteDialogComponent } from './delete/operateur-delete-dialog.component';
import { OperateurRoutingModule } from './route/operateur-routing.module';

@NgModule({
  imports: [SharedModule, OperateurRoutingModule],
  declarations: [OperateurComponent, OperateurDetailComponent, OperateurUpdateComponent, OperateurDeleteDialogComponent],
  entryComponents: [OperateurDeleteDialogComponent],
})
export class OperateurModule {}
