import { IServiceClass } from 'app/entities/service-class/service-class.model';
import { IOperation } from 'app/entities/operation/operation.model';

export interface IPaimentFacture {
  id?: number;
  referance?: number;
  serviceClass?: IServiceClass | null;
  operation?: IOperation | null;
}

export class PaimentFacture implements IPaimentFacture {
  constructor(
    public id?: number,
    public referance?: number,
    public serviceClass?: IServiceClass | null,
    public operation?: IOperation | null
  ) {}
}

export function getPaimentFactureIdentifier(paimentFacture: IPaimentFacture): number | undefined {
  return paimentFacture.id;
}
