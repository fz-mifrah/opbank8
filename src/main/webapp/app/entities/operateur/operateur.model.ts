import { IRecharge } from 'app/entities/recharge/recharge.model';

export interface IOperateur {
  id?: number;
  nomOp?: string;
  recharges?: IRecharge[] | null;
}

export class Operateur implements IOperateur {
  constructor(public id?: number, public nomOp?: string, public recharges?: IRecharge[] | null) {}
}

export function getOperateurIdentifier(operateur: IOperateur): number | undefined {
  return operateur.id;
}
