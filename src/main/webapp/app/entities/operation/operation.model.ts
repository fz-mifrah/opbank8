import dayjs from 'dayjs/esm';
import { IVirement } from 'app/entities/virement/virement.model';
import { ITransfer } from 'app/entities/transfer/transfer.model';
import { IRecharge } from 'app/entities/recharge/recharge.model';
import { IPaimentFacture } from 'app/entities/paiment-facture/paiment-facture.model';
import { ICompte } from 'app/entities/compte/compte.model';
import { TypeOperation } from 'app/entities/enumerations/type-operation.model';
import { EtatOperation } from 'app/entities/enumerations/etat-operation.model';

export interface IOperation {
  id?: number;
  numOperation?: string;
  date?: dayjs.Dayjs | null;
  typeOperatin?: TypeOperation | null;
  etatOperation?: EtatOperation | null;
  montant?: number;
  virement?: IVirement | null;
  transfer?: ITransfer | null;
  recharge?: IRecharge | null;
  paimentFacture?: IPaimentFacture | null;
  compte?: ICompte | null;
}

export class Operation implements IOperation {
  constructor(
    public id?: number,
    public numOperation?: string,
    public date?: dayjs.Dayjs | null,
    public typeOperatin?: TypeOperation | null,
    public etatOperation?: EtatOperation | null,
    public montant?: number,
    public virement?: IVirement | null,
    public transfer?: ITransfer | null,
    public recharge?: IRecharge | null,
    public paimentFacture?: IPaimentFacture | null,
    public compte?: ICompte | null
  ) {}
}

export function getOperationIdentifier(operation: IOperation): number | undefined {
  return operation.id;
}
