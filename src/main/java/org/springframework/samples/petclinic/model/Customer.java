/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Digits;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.support.MutableSortDefinition;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.core.style.ToStringCreator;

@Entity
@Table(name = "customers")
public class Customer extends Person {
	final private String role = "customer";
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "customer")
    private Set<Event> events;
    
	public String getRole() {
		return role;
	}
	
	protected Set<Event> getEventsInternal() {
        if (this.events == null) {
            this.events = new HashSet<>();
        }
        return events;
    }

    protected void setEventsInternal(Set<Event> events) {
        this.events = events;
    }

    public List<Event> getEvents() {
        List<Event> sortedEvents = new ArrayList<>(getEventsInternal());
        PropertyComparator.sort(sortedEvents, new MutableSortDefinition("name", true, true));
        return Collections.unmodifiableList(sortedEvents);
    }

    public void addEvent(Event event) {
    		getEventsInternal().add(event);
        event.setCustomer(this);
    }

    public Event getEvent(String name) {
        return getEvent(name, false);
    }

  
    public Event getEvent(String name, boolean ignoreNew) {
        name = name.toLowerCase();
        for (Event event : getEventsInternal()) {
            if (!ignoreNew || !event.isNew()) {
                String compName = event.getName();
                compName = compName.toLowerCase();
                if (compName.equals(name)) {
                    return event;
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)

            .append("id", this.getId())
            .append("new", this.isNew())
            .append("lastName", this.getLastName())
            .append("firstName", this.getFirstName())
            .append("address", this.address)
            .append("city", this.city)
            .append("telephone", this.telephone)
            .toString();
    }
}
