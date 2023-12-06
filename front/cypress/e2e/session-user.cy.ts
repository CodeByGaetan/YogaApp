/// <reference types="cypress" />

describe('Session spec', () => {

  const session1a = {
    id: 1,
    name: "Pilat",
    description: 'Cours de pilat dynamique',
    date: '2023-12-27',
    teacher_id: 1,
    users: [1, 2],
    createdAt: '2023-12-06',
    updatedAt: '2023-12-06',
  }

  const session1b = {
    id: 1,
    name: "Pilat",
    description: 'Cours de pilat dynamique',
    date: '2023-12-27',
    teacher_id: 1,
    users: [2],
    createdAt: '2023-12-06',
    updatedAt: '2023-12-06',
  }

  const session2 = {
    id: 2,
    name: "Méditation",
    description: 'Cours de méditation de pleine conscience',
    date: '2023-12-30',
    teacher_id: 2,
    users: [2, 3, 5],
    createdAt: '2023-12-06',
    updatedAt: '2023-12-06',
  }

  const teacher1 = {
    id: 1,
    lastName: 'Suffy',
    firstName: 'Sam',
    createdAt: '2023-01-01',
    updatedAt: '2023-02-10',
  }

  it('Log simple user', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'claire.voyance@gmail.com',
        firstName: 'Claire',
        lastName: 'Voyance',
        admin: false
      },
    })
      .as('SessionInformation')

    cy.intercept('GET', '/api/session', [session1a, session2])
      .as('Session List')

    cy.get('input[formControlName=email]').type("claire.voyance@gmail.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('Not participate', () => {
    cy.intercept('GET', '/api/session/1', session1a)
      .as('Session')

    cy.intercept('GET', '/api/teacher/1', teacher1)
      .as('Teacher')

    cy.intercept('DELETE', '/api/session/1/participate/1', {})
      .as('Void')

    cy.contains('Detail').first().click()

    cy.url().should('include', '/sessions/detail/1')
    cy.should('not.contain', 'Delete')

    cy.intercept('GET', '/api/session/1', session1b)
      .as('Session')

    cy.contains('Do not participate').click()

    cy.contains('Participate')
  })

  it('Participate', () => {
    cy.intercept('POST', '/api/session/1/participate/1', {})
      .as('Void')

    cy.intercept('GET', '/api/session/1', session1a)
      .as('Session')

    cy.intercept('GET', '/api/teacher/1', teacher1)
      .as('Teacher')

    cy.contains('Participate').click()

    cy.contains('Do not participate')
  })

});