import dayjs from 'dayjs/esm';
import { ICompte } from 'app/entities/compte/compte.model';

export interface IClient {
  id?: number;
  cin?: string;
  nom?: string;
  prenom?: string;
  adress?: string;
  postalCode?: number;
  dateNaissence?: dayjs.Dayjs;
  email?: string;
  compte?: ICompte | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public cin?: string,
    public nom?: string,
    public prenom?: string,
    public adress?: string,
    public postalCode?: number,
    public dateNaissence?: dayjs.Dayjs,
    public email?: string,
    public compte?: ICompte | null
  ) {}
}

export function getClientIdentifier(client: IClient): number | undefined {
  return client.id;
}
