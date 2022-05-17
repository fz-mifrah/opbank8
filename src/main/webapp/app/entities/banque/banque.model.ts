import { ICompte } from 'app/entities/compte/compte.model';

export interface IBanque {
  id?: number;
  nom?: string;
  adresse?: string | null;
  email?: string;
  comptes?: ICompte[] | null;
}

export class Banque implements IBanque {
  constructor(
    public id?: number,
    public nom?: string,
    public adresse?: string | null,
    public email?: string,
    public comptes?: ICompte[] | null
  ) {}
}

export function getBanqueIdentifier(banque: IBanque): number | undefined {
  return banque.id;
}
