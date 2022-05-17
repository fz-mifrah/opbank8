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

describe('Virement e2e test', () => {
  const virementPageUrl = '/virement';
  const virementPageUrlPattern = new RegExp('/virement(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const virementSample = {};

  let virement: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/virements+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/virements').as('postEntityRequest');
    cy.intercept('DELETE', '/api/virements/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (virement) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/virements/${virement.id}`,
      }).then(() => {
        virement = undefined;
      });
    }
  });

  it('Virements menu should load Virements page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('virement');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Virement').should('exist');
    cy.url().should('match', virementPageUrlPattern);
  });

  describe('Virement page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(virementPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Virement page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/virement/new$'));
        cy.getEntityCreateUpdateHeading('Virement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', virementPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/virements',
          body: virementSample,
        }).then(({ body }) => {
          virement = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/virements+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/virements?page=0&size=20>; rel="last",<http://localhost/api/virements?page=0&size=20>; rel="first"',
              },
              body: [virement],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(virementPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Virement page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('virement');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', virementPageUrlPattern);
      });

      it('edit button click should load edit Virement page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Virement');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', virementPageUrlPattern);
      });

      it('last delete button click should delete instance of Virement', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('virement').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', virementPageUrlPattern);

        virement = undefined;
      });
    });
  });

  describe('new Virement page', () => {
    beforeEach(() => {
      cy.visit(`${virementPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Virement');
    });

    it('should create an instance of Virement', () => {
      cy.get(`[data-cy="description"]`).type('payment empower').should('have.value', 'payment empower');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        virement = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', virementPageUrlPattern);
    });
  });
});
