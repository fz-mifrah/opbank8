import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'client',
        data: { pageTitle: 'opbankApp.client.home.title' },
        loadChildren: () => import('./client/client.module').then(m => m.ClientModule),
      },
      {
        path: 'compte',
        data: { pageTitle: 'opbankApp.compte.home.title' },
        loadChildren: () => import('./compte/compte.module').then(m => m.CompteModule),
      },
      {
        path: 'banque',
        data: { pageTitle: 'opbankApp.banque.home.title' },
        loadChildren: () => import('./banque/banque.module').then(m => m.BanqueModule),
      },
      {
        path: 'carte-bancaire',
        data: { pageTitle: 'opbankApp.carteBancaire.home.title' },
        loadChildren: () => import('./carte-bancaire/carte-bancaire.module').then(m => m.CarteBancaireModule),
      },
      {
        path: 'operation',
        data: { pageTitle: 'opbankApp.operation.home.title' },
        loadChildren: () => import('./operation/operation.module').then(m => m.OperationModule),
      },
      {
        path: 'virement',
        data: { pageTitle: 'opbankApp.virement.home.title' },
        loadChildren: () => import('./virement/virement.module').then(m => m.VirementModule),
      },
      {
        path: 'transfer',
        data: { pageTitle: 'opbankApp.transfer.home.title' },
        loadChildren: () => import('./transfer/transfer.module').then(m => m.TransferModule),
      },
      {
        path: 'recharge',
        data: { pageTitle: 'opbankApp.recharge.home.title' },
        loadChildren: () => import('./recharge/recharge.module').then(m => m.RechargeModule),
      },
      {
        path: 'operateur',
        data: { pageTitle: 'opbankApp.operateur.home.title' },
        loadChildren: () => import('./operateur/operateur.module').then(m => m.OperateurModule),
      },
      {
        path: 'paiment-facture',
        data: { pageTitle: 'opbankApp.paimentFacture.home.title' },
        loadChildren: () => import('./paiment-facture/paiment-facture.module').then(m => m.PaimentFactureModule),
      },
      {
        path: 'service-class',
        data: { pageTitle: 'opbankApp.serviceClass.home.title' },
        loadChildren: () => import('./service-class/service-class.module').then(m => m.ServiceClassModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
