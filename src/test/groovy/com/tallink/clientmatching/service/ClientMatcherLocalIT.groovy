package com.tallink.clientmatching.service

import com.tallink.clientmatching.ClientMatchingApplication
import org.springframework.beans.factory.annotation.Autowired

import org.springframework.boot.test.SpringApplicationContextLoader
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ContextConfiguration
import spock.lang.Ignore
import spock.lang.Specification
import spock.lang.Stepwise

import static java.util.Optional.ofNullable

@ContextConfiguration(loader = SpringApplicationContextLoader.class, classes = [ClientMatchingApplication.class])
@WebIntegrationTest
@Stepwise
@Ignore
class ClientMatcherLocalIT extends Specification {

    @Autowired
    ClientMatcher clientMatcher;

    def "shall test getting data from seaware only by email"() {
        when:
        def res = clientMatcher.loadClientList(ofNullable("robert.kaev@gmail.com"), ofNullable(null), ofNullable(null), ofNullable(null), ofNullable(null), ofNullable(null))
        then:
        res["Others"].collect().size() > 0
    }

    def "shall test getting data from seaware only by email and phone"() {
        when:
        def res = clientMatcher.loadClientList(ofNullable("robert.kaev@gmail.com"), ofNullable("53493550"), ofNullable(null), ofNullable(null), ofNullable(null), ofNullable(null))
        then:
        res["Best match"].getAt("CLIENT_ID") == 2332922
        res["Best match"].getAt("CLUB_ACCOUNT_TYPE") == "MAIN"
        res["Others"].collect().size() > 0
    }

    def "shall test getting data from seaware only by all params"() {
        when:
        def res = clientMatcher.loadClientList(ofNullable("robert.kaev@gmail.com"), ofNullable("53493550"), ofNullable(null), ofNullable("15429375"), ofNullable(null), ofNullable(null))
        then:
        res["Best match"].getAt("CLIENT_ID") == 2332922
        res["Best match"].getAt("CLUB_ACCOUNT_TYPE") == "MAIN"
        res["Others"].collect().size() > 0
    }
}
