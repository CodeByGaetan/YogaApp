/// <reference types="cypress" />

import { should } from "chai";

describe('Register spec', () => {

  it('Register user', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', {})

    cy.get('input[formControlName=firstName]').type("Test")
    cy.get('input[formControlName=lastName]').type("EndToEnd")
    cy.get('input[formControlName=email]').type("gaetan@studio.com")
    cy.get('input[formControlName=password]').type(`${"azerty!1234"}{enter}{enter}`)

    cy.url().should('include', '/login')
  })

  it('Submit button disabled if password, or other, field is not set', () => {
    cy.visit('/register')
    cy.get('input[formControlName=firstName]').type("Test")
    cy.get('input[formControlName=lastName]').type("EndToEnd")
    cy.get('input[formControlName=email]').type("gaetan@studio.com")
    cy.get('input[formControlName=password]').should('have.class', 'ng-invalid')
    cy.contains('Submit').should('be.disabled')
  })

  it('Show error if email already exists', () => {
    cy.visit('/register')

    cy.intercept('POST', '/api/auth/register', { statusCode: 400 })

    cy.get('input[formControlName=firstName]').type("Test")
    cy.get('input[formControlName=lastName]').type("EndToEnd")
    cy.get('input[formControlName=email]').type("gaetan@studio.com")
    cy.get('input[formControlName=password]').type("azerty!1234")
    cy.contains('Submit').click()
    cy.contains('An error occurred')
  })

});