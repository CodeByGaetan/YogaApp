/// <reference types="cypress" />

describe('Session spec', () => {

  const session1 = {
    id: 1,
    name: "Pilat",
    description: 'Cours de pilat dynamique',
    date: '2023-12-27',
    teacher_id: 1,
    users: [1, 2],
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
  const session3 = {
    id: 3,
    name: 'Stretching',
    description: 'Etirements suivant les méridiens',
    date: '2024-01-04',
    teacher_id: 1,
    users: [],
    createdAt: '2023-12-06',
    updatedAt: '2023-12-06',
  }
  const session2Mod = {
    id: 2,
    name: 'Souplesse',
    description: 'Etirements suivant les phases de la lune',
    date: '2024-01-04',
    teacher_id: 1,
    users: [],
    createdAt: '2023-12-06',
    updatedAt: '2023-12-07',
  }

  const teacher1 = {
    id: 1,
    lastName: 'Suffy',
    firstName: 'Sam',
    createdAt: '2023-01-01',
    updatedAt: '2023-02-10',
  }
  const teacher2 = {
    id: 2,
    lastName: 'Cover',
    firstName: 'Harry',
    createdAt: '2023-01-05',
    updatedAt: '2023-06-11',
  }

  it('Log admin user', () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
      body: {
        id: 1,
        username: 'yoga@studio.com',
        firstName: 'Justine',
        lastName: 'Fois',
        admin: true
      },
    })
      .as('SessionInformation')

    cy.intercept('GET', '/api/session', [session1, session2])
      .as('Session List')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

    cy.url().should('include', '/sessions')
  })

  it('Display session list', () => {
    cy.contains('Sessions available')
    cy.contains(session1.name)
    cy.contains(session1.description)
    cy.contains(session2.name)
    cy.contains(session1.description)
  })

  it('Show session details', () => {
    cy.intercept('GET', '/api/session/1', session1)
      .as('Session')

    cy.intercept('GET', '/api/teacher/1', teacher1)
      .as('Teacher')

    cy.contains('Detail').first().click()

    cy.url().should('include', '/sessions/detail/1')
    cy.contains(session1.name)
    cy.contains(session1.description)
    cy.contains(`${session1.users.length} attendees`)
    cy.contains(teacher1.firstName, { matchCase: false })
    cy.contains(teacher1.lastName, { matchCase: false })
  })

  it('Delete session', () => {
    cy.intercept('DELETE', '/api/session/1', {})
      .as('Any')

    cy.intercept('GET', '/api/session', [session2])
      .as('Session List')

    cy.contains('Delete').click()
    cy.contains('Session deleted !')
    cy.contains('Close').click()

    cy.url().should('include', '/sessions')
  })

  it('Create session', () => {

    cy.intercept('GET', '/api/teacher', [teacher1, teacher2])
      .as('Teacher List')

    cy.contains('Create').click()

    cy.contains('Create session')

    cy.get('input[formControlName=name]').type(session3.name)
    cy.get('input[formControlName=date]').type(session3.date)
    cy.get('mat-select[formControlName=teacher_id]').click()
    cy.contains('Sam Suffy').click()
    cy.contains('Save').should('be.disabled')
    cy.get('textarea[formControlName=description]').type(session3.description)

    cy.intercept('POST', '/api/session', session3)
      .as('Session')

    cy.intercept('GET', '/api/session', [session2, session3])
      .as('Session List')

    cy.contains('Save').click()

    cy.contains('Session created !')
    cy.contains('Close').click()
    cy.url().should('include', '/sessions')

  })

  it('Update Session', () => {

    cy.intercept('GET', '/api/session/2', session2)
      .as('Session')

    cy.intercept('GET', '/api/teacher', [teacher1, teacher2])
      .as('Teacher List')

    cy.contains('Edit').first().click()

    cy.url().should('include', '/sessions/update/2')
    cy.contains('Update session')

    cy.get('input[formControlName=name]').should('have.value', session2.name).clear().type(session2Mod.name)
    cy.get('textarea[formControlName=description]').should('have.value', session2.description).clear().type(session2Mod.description)

    cy.intercept('PUT', '/api/session/2', session2Mod)
      .as('Session')

    cy.intercept('GET', '/api/session', [session2Mod, session3])
      .as('Session List')

    cy.contains('Save').click()

    cy.contains('Session updated !')
    cy.contains('Close').click()
    cy.url().should('include', '/sessions')
  })




});