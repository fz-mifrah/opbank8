import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Client e2e test', () => {
  const clientPageUrl = '/client';
  const clientPageUrlPattern = new RegExp('/client(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const clientSample = {
    cin: 'cohesive Plastic',
    nom: 'port',
    prenom: 'productize convergence panel',
    adress: 'Generic',
    postalCode: 77286,
    dateNaissence: '2022-05-17',
    email: 'Alexis.Nicolas@yahoo.com',
  };

  let client: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/clients+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/clients').as('postEntityRequest');
    cy.intercept('DELETE', '/api/clients/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (client) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/clients/${client.id}`,
      }).then(() => {
        client = undefined;
      });
    }
  });

  it('Clients menu should load Clients page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('client');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Client').should('exist');
    cy.url().should('match', clientPageUrlPattern);
  });

  describe('Client page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(clientPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Client page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/client/new$'));
        cy.getEntityCreateUpdateHeading('Client');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', clientPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/clients',
          body: clientSample,
        }).then(({ body }) => {
          client = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/clients+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/clients?page=0&size=20>; rel="last",<http://localhost/api/clients?page=0&size=20>; rel="first"',
              },
              body: [client],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(clientPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Client page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('client');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', clientPageUrlPattern);
      });

      it('edit button click should load edit Client page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Client');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', clientPageUrlPattern);
      });

      it('last delete button click should delete instance of Client', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('client').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', clientPageUrlPattern);

        client = undefined;
      });
    });
  });

  describe('new Client page', () => {
    beforeEach(() => {
      cy.visit(`${clientPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Client');
    });

    it('should create an instance of Client', () => {
      cy.get(`[data-cy="cin"]`).type('Pants').should('have.value', 'Pants');

      cy.get(`[data-cy="nom"]`).type('real-time Sterling').should('have.value', 'real-time Sterling');

      cy.get(`[data-cy="prenom"]`).type('e-business').should('have.value', 'e-business');

      cy.get(`[data-cy="adress"]`).type('hierarchy').should('have.value', 'hierarchy');

      cy.get(`[data-cy="postalCode"]`).type('2114').should('have.value', '2114');

      cy.get(`[data-cy="dateNaissence"]`).type('2022-05-17').should('have.value', '2022-05-17');

      cy.get(`[data-cy="email"]`).type('Lauren.Nader73@yahoo.com').should('have.value', 'Lauren.Nader73@yahoo.com');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        client = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', clientPageUrlPattern);
    });
  });
});
