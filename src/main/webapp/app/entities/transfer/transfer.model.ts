import { IOperation } from 'app/entities/operation/operation.model';

export interface ITransfer {
  id?: number;
  cin?: string;
  nomPrenom?: string;
  tel?: number | null;
  operation?: IOperation | null;
}

export class Transfer implements ITransfer {
  constructor(
    public id?: number,
    public cin?: string,
    public nomPrenom?: string,
    public tel?: number | null,
    public operation?: IOperation | null
  ) {}
}

export function getTransferIdentifier(transfer: ITransfer): number | undefined {
  return transfer.id;
}
