/// <reference types="cypress" />

describe('Routing spec', () => {

    it('Show not found page', () => {
      cy.visit('/session-inexistante')
      cy.contains('Page not found !')
    })

    it('Show login page trying to access a guarded page', () => {
      cy.visit('/sessions')
      cy.url().should('include', '/login')
      cy.get('.login').contains('Login')
    })

    it('Show register page trying to access an unguarded page', () => {
      cy.visit('/register')
      cy.url().should('include', '/register')
      cy.get('.register').contains('Register')
    })
    
})