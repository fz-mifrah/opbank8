import { IPaimentFacture } from 'app/entities/paiment-facture/paiment-facture.model';

export interface IServiceClass {
  id?: number;
  nomService?: string;
  paimentFactures?: IPaimentFacture[] | null;
}

export class ServiceClass implements IServiceClass {
  constructor(public id?: number, public nomService?: string, public paimentFactures?: IPaimentFacture[] | null) {}
}

export function getServiceClassIdentifier(serviceClass: IServiceClass): number | undefined {
  return serviceClass.id;
}
