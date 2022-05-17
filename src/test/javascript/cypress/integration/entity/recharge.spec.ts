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

describe('Recharge e2e test', () => {
  const rechargePageUrl = '/recharge';
  const rechargePageUrlPattern = new RegExp('/recharge(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const rechargeSample = { numTel: 91952 };

  let recharge: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/recharges+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/recharges').as('postEntityRequest');
    cy.intercept('DELETE', '/api/recharges/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (recharge) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/recharges/${recharge.id}`,
      }).then(() => {
        recharge = undefined;
      });
    }
  });

  it('Recharges menu should load Recharges page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('recharge');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Recharge').should('exist');
    cy.url().should('match', rechargePageUrlPattern);
  });

  describe('Recharge page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(rechargePageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Recharge page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/recharge/new$'));
        cy.getEntityCreateUpdateHeading('Recharge');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rechargePageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/recharges',
          body: rechargeSample,
        }).then(({ body }) => {
          recharge = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/recharges+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/recharges?page=0&size=20>; rel="last",<http://localhost/api/recharges?page=0&size=20>; rel="first"',
              },
              body: [recharge],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(rechargePageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Recharge page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('recharge');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rechargePageUrlPattern);
      });

      it('edit button click should load edit Recharge page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Recharge');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rechargePageUrlPattern);
      });

      it('last delete button click should delete instance of Recharge', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('recharge').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', rechargePageUrlPattern);

        recharge = undefined;
      });
    });
  });

  describe('new Recharge page', () => {
    beforeEach(() => {
      cy.visit(`${rechargePageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Recharge');
    });

    it('should create an instance of Recharge', () => {
      cy.get(`[data-cy="numTel"]`).type('44840').should('have.value', '44840');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        recharge = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', rechargePageUrlPattern);
    });
  });
});
