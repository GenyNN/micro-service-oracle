package com.tallink.clientmatching.service

import spock.lang.Specification

class ClientNameTest extends Specification {

    def "It shall compare different client names"() {

        given: "Two clients with different first and last names"
        def first = new ClientName("ivan", "ivanov")
        def second = new ClientName("sidr", "sidorov")

        when: "We compare the ClientName's"
        def resEq = first == second

        then:  "They are not equal"
        resEq == false
    }

    def "It shall compare same client names"() {

        given: "Two clients with same first and last names"
        def first = new ClientName("ivan", "ivanov")
        def second = new ClientName("ivan", "ivanov")

        when: "We compare the ClientName's"
        def resEq = first == second

        then: "They are equal"
        resEq == true
    }

    def "It shall compare same client names but vice versa"() {

        given: "Two clients with same names but vice versa"
        def first = new ClientName("ivan", "ivanov")
        def second = new ClientName("ivanov", "ivan")

        when: "We compare the ClientName's"
        def resEq = first == second

        then: "They are not equal"
        resEq == false
    }

    def "It shall compare only client first names"() {

        given: "Two clients with first name only"
        def first = new ClientName("ivan", null)
        def second = new ClientName("ivan", null)

        when: "We compare the ClientName's"
        def resEq = first == second

        then: "They are equal"
        resEq == true
    }

    def "It shall compare only client last names"() {

        given: "Two clients with last name only"
        def first = new ClientName(null, "ivanov")
        def second = new ClientName(null, "ivanov")

        when: "We compare the ClientName's"
        def resEq = first == second

        then: "They are equal"
        resEq == true
    }

    def "It shall compare hash codes of clients with same names"() {

        given: "Two clients with same first and last names"
        def first = new ClientName("ivan", "ivanov")
        def second = new ClientName("ivan", "ivanov")

        when: "We compare the ClientName's"
        def firstHC = first.hashCode()
        def secondHC = second.hashCode()

        then: "HCs are equal"
        firstHC == secondHC
    }

    def "It shall compare hash codes of clients with different names"() {

        given: "Two clients with different first and last names"
        def first = new ClientName("ivan", "ivanov")
        def second = new ClientName("sidr", "sidorov")

        when: "We compare the ClientName's"
        def firstHC = first.hashCode()
        def secondHC = second.hashCode()

        then: "HCs are not equal"
        firstHC != secondHC
    }
}
