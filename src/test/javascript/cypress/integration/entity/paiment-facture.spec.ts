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

describe('PaimentFacture e2e test', () => {
  const paimentFacturePageUrl = '/paiment-facture';
  const paimentFacturePageUrlPattern = new RegExp('/paiment-facture(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const paimentFactureSample = { referance: 63136 };

  let paimentFacture: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/paiment-factures+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/paiment-factures').as('postEntityRequest');
    cy.intercept('DELETE', '/api/paiment-factures/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (paimentFacture) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/paiment-factures/${paimentFacture.id}`,
      }).then(() => {
        paimentFacture = undefined;
      });
    }
  });

  it('PaimentFactures menu should load PaimentFactures page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('paiment-facture');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('PaimentFacture').should('exist');
    cy.url().should('match', paimentFacturePageUrlPattern);
  });

  describe('PaimentFacture page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(paimentFacturePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create PaimentFacture page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/paiment-facture/new$'));
        cy.getEntityCreateUpdateHeading('PaimentFacture');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paimentFacturePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/paiment-factures',
          body: paimentFactureSample,
        }).then(({ body }) => {
          paimentFacture = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/paiment-factures+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/paiment-factures?page=0&size=20>; rel="last",<http://localhost/api/paiment-factures?page=0&size=20>; rel="first"',
              },
              body: [paimentFacture],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(paimentFacturePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details PaimentFacture page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('paimentFacture');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paimentFacturePageUrlPattern);
      });

      it('edit button click should load edit PaimentFacture page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('PaimentFacture');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paimentFacturePageUrlPattern);
      });

      it('last delete button click should delete instance of PaimentFacture', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('paimentFacture').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', paimentFacturePageUrlPattern);

        paimentFacture = undefined;
      });
    });
  });

  describe('new PaimentFacture page', () => {
    beforeEach(() => {
      cy.visit(`${paimentFacturePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('PaimentFacture');
    });

    it('should create an instance of PaimentFacture', () => {
      cy.get(`[data-cy="referance"]`).type('32738').should('have.value', '32738');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        paimentFacture = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', paimentFacturePageUrlPattern);
    });
  });
});
