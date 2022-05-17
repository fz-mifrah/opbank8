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

describe('Operateur e2e test', () => {
  const operateurPageUrl = '/operateur';
  const operateurPageUrlPattern = new RegExp('/operateur(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const operateurSample = { nomOp: 'deposit' };

  let operateur: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/operateurs+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/operateurs').as('postEntityRequest');
    cy.intercept('DELETE', '/api/operateurs/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (operateur) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/operateurs/${operateur.id}`,
      }).then(() => {
        operateur = undefined;
      });
    }
  });

  it('Operateurs menu should load Operateurs page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('operateur');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Operateur').should('exist');
    cy.url().should('match', operateurPageUrlPattern);
  });

  describe('Operateur page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(operateurPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Operateur page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/operateur/new$'));
        cy.getEntityCreateUpdateHeading('Operateur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operateurPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/operateurs',
          body: operateurSample,
        }).then(({ body }) => {
          operateur = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/operateurs+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [operateur],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(operateurPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Operateur page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('operateur');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operateurPageUrlPattern);
      });

      it('edit button click should load edit Operateur page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Operateur');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operateurPageUrlPattern);
      });

      it('last delete button click should delete instance of Operateur', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('operateur').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', operateurPageUrlPattern);

        operateur = undefined;
      });
    });
  });

  describe('new Operateur page', () => {
    beforeEach(() => {
      cy.visit(`${operateurPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Operateur');
    });

    it('should create an instance of Operateur', () => {
      cy.get(`[data-cy="nomOp"]`).type('THX Pizza Ergonomic').should('have.value', 'THX Pizza Ergonomic');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        operateur = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', operateurPageUrlPattern);
    });
  });
});
