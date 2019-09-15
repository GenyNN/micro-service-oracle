package com.tallink.clientmatching.service

import spock.lang.Specification
import spock.lang.Subject

import static java.util.Optional.empty

class ClientMatcherTest extends Specification {

    def "It shall select best match by MAIN ClubOne account"() {

        given: 'We ask to search by email'
        def email = 'email@server.com'
        def query = Mock(ClientSearch)
        def factory = Mock(ClientSearchFactory)
        factory.searchByEmail(email) >> query

        and: 'this search produces two results, with MAIN and AFFILIATE CO accounts'
        query.queryName >> 'Mock email search query'
        query.run() >> [
                ['CLIENT_ID':1, 'FULL_NAME': 'John Smith', 'CLUB_ACCOUNT': '12345', 'CLUB_ACCOUNT_TYPE':'AFFILIATE'],
                ['CLIENT_ID':2, 'FULL_NAME': 'Ivan Sidorov', 'CLUB_ACCOUNT': '6789', 'CLUB_ACCOUNT_TYPE':'MAIN']
        ]

        when: 'we request matching'
        @Subject
        def matcher = new ClientMatcher(factory, email, phoneNumber(null), countryCode(null), clubOneNumber(null), firstName(null), lastName(null))
        def results = matcher.match()

        then: 'Client 2 with MAIN CO account type is selected'
        results[ClientMatcher.MAP_BEST_MATCH][0]['CLIENT_ID'] == 2
    }

    String phoneNumber(pn) {
        return pn
    }

    String countryCode(cc) {
        return cc
    }

    String clubOneNumber(co) {
        return co
    }

    String firstName(fn) {
        return fn
    }

    String lastName(ln) {
        return ln
    }
}
