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

describe('Operation e2e test', () => {
  const operationPageUrl = '/operation';
  const operationPageUrlPattern = new RegExp('/operation(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const operationSample = { numOperation: 'IB Peso drive', montant: 61378 };

  let operation: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/operations+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/operations').as('postEntityRequest');
    cy.intercept('DELETE', '/api/operations/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (operation) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/operations/${operation.id}`,
      }).then(() => {
        operation = undefined;
      });
    }
  });

  it('Operations menu should load Operations page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('operation');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Operation').should('exist');
    cy.url().should('match', operationPageUrlPattern);
  });

  describe('Operation page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(operationPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Operation page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/operation/new$'));
        cy.getEntityCreateUpdateHeading('Operation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operationPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/operations',
          body: operationSample,
        }).then(({ body }) => {
          operation = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/operations+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/operations?page=0&size=20>; rel="last",<http://localhost/api/operations?page=0&size=20>; rel="first"',
              },
              body: [operation],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(operationPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Operation page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('operation');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operationPageUrlPattern);
      });

      it('edit button click should load edit Operation page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Operation');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operationPageUrlPattern);
      });

      it('last delete button click should delete instance of Operation', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('operation').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operationPageUrlPattern);

        operation = undefined;
      });
    });
  });

  describe('new Operation page', () => {
    beforeEach(() => {
      cy.visit(`${operationPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Operation');
    });

    it('should create an instance of Operation', () => {
      cy.get(`[data-cy="numOperation"]`).type('withdrawal compressing').should('have.value', 'withdrawal compressing');

      cy.get(`[data-cy="date"]`).type('2022-05-17').should('have.value', '2022-05-17');

      cy.get(`[data-cy="typeOperatin"]`).select('TypeRecharge');

      cy.get(`[data-cy="etatOperation"]`).select('Rejetee');

      cy.get(`[data-cy="montant"]`).type('5480').should('have.value', '5480');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        operation = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', operationPageUrlPattern);
    });
  });
});
