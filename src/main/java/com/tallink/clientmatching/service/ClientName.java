package com.tallink.clientmatching.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ClientName {
    private String firstName;
    private String lastName;

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ClientName other = (ClientName) obj;

        if  ( equalsFirstName(other) &&  equalsLastName(other) )
            return true;
        else if ( equalsFirstName(other) &&  lastNameBothNull(other) )
            return true;
        else if ( firstNameBothNull(other) && equalsLastName(other) )
            return true;
        else
            return false;
    }

    private boolean equalsFirstName(ClientName other) {
        return this.firstName != null && other.firstName != null && this.firstName.equals(other.firstName);
    }

    private boolean lastNameBothNull(ClientName other) {
        return this.lastName == null && other.lastName == null;
    }

    private boolean firstNameBothNull(ClientName other) {
        return this.firstName == null && other.firstName == null;
    }

    private boolean equalsLastName(ClientName other) {
        return this.lastName != null && other.lastName != null && this.lastName.equals(other.lastName);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (this.firstName != null)
            result = prime * result + this.firstName.hashCode();
        if (this.lastName != null)
            result = prime * result + this.lastName.hashCode();
        return result;
    }
}
