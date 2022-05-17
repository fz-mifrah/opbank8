import { IOperation } from 'app/entities/operation/operation.model';
import { ICompte } from 'app/entities/compte/compte.model';

export interface IVirement {
  id?: number;
  description?: string | null;
  operation?: IOperation | null;
  compte?: ICompte | null;
}

export class Virement implements IVirement {
  constructor(
    public id?: number,
    public description?: string | null,
    public operation?: IOperation | null,
    public compte?: ICompte | null
  ) {}
}

export function getVirementIdentifier(virement: IVirement): number | undefined {
  return virement.id;
}
