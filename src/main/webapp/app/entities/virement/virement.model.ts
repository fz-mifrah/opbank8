import { IOperation } from 'app/entities/operation/operation.model';
import { ICompte } from 'app/entities/compte/compte.model';

export interface IVirement {
  id?: number;
  description?: string | null;
  operation?: IOperation | null;
  compte?: ICompte | null;
}
export interface IOrderVirement {
  monCompte?:number | null;
  destCompte?:number | null;
  montant?:number | null;
  label?:string | null;
}

export class Virement implements IVirement {
  constructor(
    public id?: number,
    public description?: string | null,
    public operation?: IOperation | null,
    public compte?: ICompte | null
  ) {}
}

export class OrderVirement implements IOrderVirement {
  constructor(
    public monCompte?:number | null,
    public  destCompte?:number | null,
    public montant?:number | null,
    public label?:string | null
  ) {}
}

export function getVirementIdentifier(virement: IVirement): number | undefined {
  return virement.id;
}
