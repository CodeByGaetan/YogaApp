/// <reference types="cypress" />

describe('Login spec', () => {

  it('Login successfull', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'userName',
        firstName: 'firstName',
        lastName: 'lastName',
        admin: true
      },
    })

    cy.intercept(
      {
        method: 'GET',
        url: '/api/session',
      },
      []).as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('Logout successfull', () => {
    cy.contains('Logout').click()
    cy.url().should('not.include', '/sessions')
  })

  it('Show error if wrong credential', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', { statusCode: 401 })

    cy.get('input[formControlName=email]').type("bernard@studio.com")
    cy.get('input[formControlName=password]').type(`${"azerty!1234"}{enter}{enter}`)
    cy.url().should('not.include', '/sessions')
    cy.contains('An error occurred')
  })

  it('Show error and submit button disabled if password field is not set', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', { statusCode: 401 })

    cy.get('input[formControlName=email]').type(`${"yoga@studio.com"}{enter}{enter}`)
    cy.get('input[formControlName=password]').should('have.class', 'ng-invalid')
    cy.contains('Submit').should('be.disabled')
    cy.contains('An error occurred')
  })

  it('Show error and submit button disabled if email field is not set', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', { statusCode: 401 })

    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    cy.get('input[formControlName=email]').should('have.class', 'ng-invalid')
    cy.contains('Submit').should('be.disabled')
    cy.contains('An error occurred')
  })

});