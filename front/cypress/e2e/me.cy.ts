/// <reference types="cypress" />

describe('Me spec', () => {

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
      .as('SessionInformation')

    cy.intercept('GET', '/api/session', [])
      .as('session')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('Display admin user information', () => {

    const userDetail = {
      id: 1,
      email: 'yoga@studio.com',
      lastName: 'Carena',
      firstName: 'Emma',
      admin: true,
      createdAt: '2023-01-06',
      updatedAt: '2023-01-06',
    }

    cy.intercept('GET', '/api/user/1', {
      body: userDetail
    })
    .as('User')

    cy.contains('Account').click()
    cy.url().should('include', '/me')
    cy.contains('User information')
    cy.contains(userDetail.email)
    cy.contains(userDetail.lastName, { matchCase: false})
    cy.contains(userDetail.firstName, { matchCase: false})
    cy.should('not.contain', 'Delete')
    cy.contains('You are admin')
  })

  it('Go to sessions when click on left arrow', () => {

    cy.intercept('GET', '/api/session', [])
      .as('session')

    cy.contains('arrow_back').click()
    cy.url().should('include', '/sessions')
  })

  it('Display not admin user information', () => {
    const userDetail = {
      id: 1,
      email: 'yoga@studio.com',
      lastName: 'Bonboeur',
      firstName: 'Jean',
      admin: false,
      createdAt: '2023-01-06',
      updatedAt: '2023-01-06',
    }

    cy.intercept('GET', '/api/user/1', {
      body: userDetail
    })
    .as('User')

    cy.contains('Account').click()
    cy.contains('Delete my account')
    
  })

  it('Delete user successfull', () => {
    cy.intercept('DELETE', '/api/user/1', [])
    .as('Any')

    cy.get('button').contains('Delete').click()
    cy.contains('Your account has been deleted !')
    cy.url().should('eq', Cypress.config().baseUrl)
    cy.contains('Login')
    cy.contains('Register')
  })




});