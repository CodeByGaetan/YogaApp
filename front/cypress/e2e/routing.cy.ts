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

    // it('Show session list page if user already Log', () => {

    //   cy.session(() => {
    //     cy.visit('/login')
  
    //     cy.intercept('POST', '/api/auth/login', {
    //       body: {
    //         id: 1,
    //         username: 'claire.voyance@gmail.com',
    //         firstName: 'Claire',
    //         lastName: 'Voyance',
    //         admin: false
    //       },
    //     })
    //       .as('SessionInformation')
    
    //     cy.intercept('GET', '/api/session', [])
    //       .as('Session List')
    
    //     cy.get('input[formControlName=email]').type("claire.voyance@gmail.com")
    //     cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
    
    //     cy.url().should('include', '/sessions')
  
    //     cy.visit('/login')
  
    //   })
      
    // })

})