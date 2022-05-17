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

describe('Transfer e2e test', () => {
  const transferPageUrl = '/transfer';
  const transferPageUrlPattern = new RegExp('/transfer(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const transferSample = { cin: 'intranet Metal', nomPrenom: 'visualize Internal' };

  let transfer: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/transfers+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/transfers').as('postEntityRequest');
    cy.intercept('DELETE', '/api/transfers/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (transfer) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/transfers/${transfer.id}`,
      }).then(() => {
        transfer = undefined;
      });
    }
  });

  it('Transfers menu should load Transfers page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('transfer');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Transfer').should('exist');
    cy.url().should('match', transferPageUrlPattern);
  });

  describe('Transfer page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(transferPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Transfer page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/transfer/new$'));
        cy.getEntityCreateUpdateHeading('Transfer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transferPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/transfers',
          body: transferSample,
        }).then(({ body }) => {
          transfer = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/transfers+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/transfers?page=0&size=20>; rel="last",<http://localhost/api/transfers?page=0&size=20>; rel="first"',
              },
              body: [transfer],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(transferPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Transfer page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('transfer');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transferPageUrlPattern);
      });

      it('edit button click should load edit Transfer page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Transfer');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transferPageUrlPattern);
      });

      it('last delete button click should delete instance of Transfer', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('transfer').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', transferPageUrlPattern);

        transfer = undefined;
      });
    });
  });

  describe('new Transfer page', () => {
    beforeEach(() => {
      cy.visit(`${transferPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Transfer');
    });

    it('should create an instance of Transfer', () => {
      cy.get(`[data-cy="cin"]`).type('Squares content-based Forward').should('have.value', 'Squares content-based Forward');

      cy.get(`[data-cy="nomPrenom"]`).type('backing').should('have.value', 'backing');

      cy.get(`[data-cy="tel"]`).type('73301').should('have.value', '73301');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        transfer = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', transferPageUrlPattern);
    });
  });
});
