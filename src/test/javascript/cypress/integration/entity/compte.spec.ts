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

describe('Compte e2e test', () => {
  const comptePageUrl = '/compte';
  const comptePageUrlPattern = new RegExp('/compte(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const compteSample = { rib: 64766, code: 53711, solde: 12825 };

  let compte: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/comptes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/comptes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/comptes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (compte) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/comptes/${compte.id}`,
      }).then(() => {
        compte = undefined;
      });
    }
  });

  it('Comptes menu should load Comptes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('compte');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Compte').should('exist');
    cy.url().should('match', comptePageUrlPattern);
  });

  describe('Compte page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(comptePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Compte page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/compte/new$'));
        cy.getEntityCreateUpdateHeading('Compte');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', comptePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/comptes',
          body: compteSample,
        }).then(({ body }) => {
          compte = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/comptes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/comptes?page=0&size=20>; rel="last",<http://localhost/api/comptes?page=0&size=20>; rel="first"',
              },
              body: [compte],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(comptePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Compte page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('compte');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', comptePageUrlPattern);
      });

      it('edit button click should load edit Compte page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Compte');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', comptePageUrlPattern);
      });

      it('last delete button click should delete instance of Compte', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('compte').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', comptePageUrlPattern);

        compte = undefined;
      });
    });
  });

  describe('new Compte page', () => {
    beforeEach(() => {
      cy.visit(`${comptePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Compte');
    });

    it('should create an instance of Compte', () => {
      cy.get(`[data-cy="rib"]`).type('92185').should('have.value', '92185');

      cy.get(`[data-cy="dateOuverture"]`).type('2022-05-17').should('have.value', '2022-05-17');

      cy.get(`[data-cy="code"]`).type('44815').should('have.value', '44815');

      cy.get(`[data-cy="solde"]`).type('74098').should('have.value', '74098');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        compte = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', comptePageUrlPattern);
    });
  });
});
