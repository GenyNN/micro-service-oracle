package com.tallink.clientmatching.service;

import lombok.AllArgsConstructor;

import java.util.*;
import java.util.stream.Collectors;

import static com.tallink.clientmatching.service.ClubAccountType.MAIN;
import static com.tallink.clientmatching.service.SearchResultField.*;
import static com.tallink.clientmatching.util.InstrumentationUtils.executeAndLogTime;
import static com.tallink.clientmatching.util.StreamUtils.*;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
public class ClientMatcher {

    public static final String MAP_BEST_MATCH = "Best match";
    public static final String MAP_OTHERS = "Others";

    private ClientSearchFactory queryFactory;

    private String email;

    private String phoneNumber;

    private String countryCode;

    private String clubOneNumber;

    private String firstName;

    private String lastName;

    public Map<String, Object> match() {

        return executeAndLogTime("Total match time", () -> {
            List<Map<String, Object>> searchResults =
                    searchForEachField(Optional.ofNullable(email),
                            Optional.ofNullable(phoneNumber),
                            Optional.ofNullable(countryCode),
                            Optional.ofNullable(clubOneNumber),
                            firstName,
                            lastName);
            Collection<ClientName> names = getNames(searchResults);
            List<Map<String, Object>> bestMatch = pickBestMatch(searchResults);
            if (!bestMatch.isEmpty()) return mapResults(bestMatch, searchResults);

            if (searchResults.isEmpty()) return mapResults(bestMatch, searchResults);


            if (phoneNumber != null) searchResults = searchByEmails(searchResults, names);
            else if (email != null) searchResults = searchByPhoneNumbers(searchResults, names);

            bestMatch = pickBestMatch(searchResults);
            return mapResults(bestMatch, searchResults);
        });
    }

    private Collection<ClientName> getNames(List<Map<String, Object>> searchResults) {
        return searchResults
                .stream()
                .map(r -> new ClientName(getFirstName(r), getLastName(r)))
                .distinct()
                .collect(toList());
    }

    private String getFirstName(Map<String, Object> client) {
        return client.get(FIRST_NAME.name()) != null ? client.get(FIRST_NAME.name()).toString() : null;
    }

    private String getLastName(Map<String, Object> client) {
        return client.get(LAST_NAME.name()) != null ? client.get(LAST_NAME.name()).toString() : null;
    }

    private List<Map<String, Object>> pickBestMatch(List<Map<String, Object>> searchResults) {
        if (this.clubOneNumber != null && this.firstName != null && this.lastName != null)
            return tryPickByClubOneFirstNameLastName(searchResults);
        else
            return tryPickFromMainClubAccountHolders(searchResults);
    }

    private List<Map<String, Object>> tryPickByClubOneFirstNameLastName(List<Map<String, Object>> searchResults) {
        return searchResults.stream()
                .filter(item ->
                        item.get(CLUB_ACCOUNT.name()) != null &&
                                item.get(CLUB_ACCOUNT.name()).equals(this.clubOneNumber) &&
                                item.get(FIRST_NAME.name()) != null &&
                                item.get(FIRST_NAME.name()).equals(this.firstName) &&
                                item.get(FIRST_NAME.name()).equals(this.firstName) &&
                                item.get(LAST_NAME.name()) != null &&
                                item.get(LAST_NAME.name()).equals(this.lastName)
                )
                .collect(toList());
    }

    private List<Map<String, Object>> searchByEmails(List<Map<String, Object>> searchResults, Collection<ClientName> neededClients) {

        Collection<String> emailsFiltered = getEmails(searchResults);

        if (emailsFiltered.isEmpty()) return new ArrayList<>();

        List<ClientSearch> queries = neededClients.stream()
                .map(client -> queryFactory.searchByEmails(emailsFiltered, client.getFirstName(), client.getLastName()))
                .collect(Collectors.toList());

        return parallelSearch(queries);
    }

    private Collection<String> getEmails(List<Map<String, Object>> searchResults) {
        return searchResults
                .stream()
                .filter(r -> r.containsKey(EMAIL.name()) == true)
                .map(r -> r.get(EMAIL.name()).toString())
                .distinct()
                .collect(toList());
    }

    private List<Map<String, Object>> searchByPhoneNumbers(List<Map<String, Object>> searchResults, Collection<ClientName> neededClients) {

        Collection<String> phoneNumbers = getPhoneNumbers(searchResults);

        if (phoneNumbers.isEmpty()) return new ArrayList<>();

        List<ClientSearch> queries = neededClients.stream()
                .map(client -> queryFactory.searchByPhones(phoneNumbers, client.getFirstName(), client.getLastName()))
                .collect(Collectors.toList());

        return parallelSearch(queries);
    }

    private Collection<String> getPhoneNumbers (List<Map<String, Object>> searchResults) {
        Collection<String> phones = new ArrayList<>();
        for (Map<String, Object> result : searchResults) {
            if (!result.containsKey(PHONE_NUMBER.name())) continue;
            int codeSeparator = result.get(PHONE_NUMBER.name()).toString().indexOf('-');
            if (codeSeparator > 0) {
                String code = result.get(PHONE_NUMBER.name()).toString().substring(0, codeSeparator);
                String phone = result.get(PHONE_NUMBER.name()).toString().substring(codeSeparator + 1);
                phones.add(code.concat(phone));
                phones.add(phone);
            } else {
                String phone = result.get(PHONE_NUMBER.name()).toString();
                phones.add(phone);
            }
        }
        return phones.stream().distinct().collect(Collectors.toList());
    }

    private List<Map<String, Object>> tryPickFromMainClubAccountHolders(List<Map<String, Object>> searchResults) {
        return tryPickFromClubAccountHolders(searchResults, MAIN);
    }

    private Object getField(SearchResultField field, Map<String, Object> item) {
        return item.get(field.name());
    }

    private List<Map<String, Object>> tryPickFromClubAccountHolders(List<Map<String, Object>> searchResults,
                                                                    ClubAccountType clientType) {
        List<Map<String, Object>> clubOneAccountHolders = getClubOneAccountHolders(searchResults, clientType);
        return clubOneAccountHolders.stream().collect(toList());
    }

    private List<Map<String, Object>> getClubOneAccountHolders(List<Map<String, Object>> searchResults, ClubAccountType clientType) {
        return searchResults.stream()
                .filter(item ->
                        item.get(CLUB_ACCOUNT.name()) != null &&
                                item.get(CLUB_ACCOUNT_TYPE.name()) != null &&
                                item.get(CLUB_ACCOUNT_TYPE.name()).equals(clientType.name()))
                .collect(toList());
    }

    private List<Map<String, Object>> searchForEachField(Optional<String> email, Optional<String> phoneNumber,
                                                         Optional<String> countryCode, Optional<String> clubOneNumber,
                                                         String firstName, String lastName) {
        List<ClientSearch> queries = new ArrayList<>();
        email.ifPresent(e -> queries.add(queryFactory.searchByEmail(e)));
        phoneNumber.ifPresent(pn -> queries.add(queryFactory.searchByPhone(pn, countryCode)));
        clubOneNumber.ifPresent(co -> queries.add(queryFactory.searchByClubOne(co, firstName, lastName)));

        return parallelSearch(queries);
    }

    private List<Map<String, Object>> parallelSearch(List<ClientSearch> queries) {
        return queries.parallelStream()
                .map(q -> executeAndLogTime(q.getQueryName(), q::run))
                .flatMap(Collection::stream)
                .map(ClientMatcher::selectRequiredFields)
                .collect(toList())
                .stream()
                .filter(distinctByKey(item -> item.get(CLIENT_ID.name())))
                .collect(toList());
    }

    private List<Map<String, Object>> search(ClientSearch query) {

        return executeAndLogTime(query.getQueryName(), query::run)
                .stream()
                .map(ClientMatcher::selectRequiredFields)
                .filter(distinctByKey(item -> item.get(CLIENT_ID.name())))
                .collect(toList());
    }

    private Map<String, Object> mapResults(List<Map<String, Object>> bestMatch, List<Map<String, Object>> allResults) {
        return newTreeMap(
                entry(MAP_BEST_MATCH, bestMatch),
                entry(MAP_OTHERS, retainOnlyOthers(bestMatch, allResults))
        );
    }

    private List<Map<String, Object>> retainOnlyOthers(List<Map<String, Object>> bestMatch, List<Map<String, Object>> allResults) {
        return allResults.stream().filter(r -> !bestMatch.contains(r)).collect(toList());
    }

    private static Map<String, Object> selectRequiredFields(Map<String, Object> result) {
        return result
                .entrySet()
                .stream()
                .filter(entry -> allSearchResultFieldNames().contains(entry.getKey()) && entry.getValue() != null)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}