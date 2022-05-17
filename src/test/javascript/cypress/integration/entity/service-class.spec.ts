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

describe('ServiceClass e2e test', () => {
  const serviceClassPageUrl = '/service-class';
  const serviceClassPageUrlPattern = new RegExp('/service-class(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const serviceClassSample = { nomService: 'mobile enhance' };

  let serviceClass: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/service-classes+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/service-classes').as('postEntityRequest');
    cy.intercept('DELETE', '/api/service-classes/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (serviceClass) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/service-classes/${serviceClass.id}`,
      }).then(() => {
        serviceClass = undefined;
      });
    }
  });

  it('ServiceClasses menu should load ServiceClasses page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('service-class');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('ServiceClass').should('exist');
    cy.url().should('match', serviceClassPageUrlPattern);
  });

  describe('ServiceClass page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(serviceClassPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create ServiceClass page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/service-class/new$'));
        cy.getEntityCreateUpdateHeading('ServiceClass');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceClassPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/service-classes',
          body: serviceClassSample,
        }).then(({ body }) => {
          serviceClass = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/service-classes+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [serviceClass],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(serviceClassPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details ServiceClass page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('serviceClass');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceClassPageUrlPattern);
      });

      it('edit button click should load edit ServiceClass page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('ServiceClass');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceClassPageUrlPattern);
      });

      it('last delete button click should delete instance of ServiceClass', () => {
        cy.get(entityDeleteButtonSelector).last().click();
        cy.getEntityDeleteDialogHeading('serviceClass').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', serviceClassPageUrlPattern);

        serviceClass = undefined;
      });
    });
  });

  describe('new ServiceClass page', () => {
    beforeEach(() => {
      cy.visit(`${serviceClassPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('ServiceClass');
    });

    it('should create an instance of ServiceClass', () => {
      cy.get(`[data-cy="nomService"]`).type('alarm communities Clothing').should('have.value', 'alarm communities Clothing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        serviceClass = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', serviceClassPageUrlPattern);
    });
  });
});
