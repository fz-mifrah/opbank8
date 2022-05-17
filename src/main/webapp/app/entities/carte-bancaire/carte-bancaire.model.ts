import { ICompte } from 'app/entities/compte/compte.model';

export interface ICarteBancaire {
  id?: number;
  numCarte?: string;
  compte?: ICompte | null;
}

export class CarteBancaire implements ICarteBancaire {
  constructor(public id?: number, public numCarte?: string, public compte?: ICompte | null) {}
}

export function getCarteBancaireIdentifier(carteBancaire: ICarteBancaire): number | undefined {
  return carteBancaire.id;
}
