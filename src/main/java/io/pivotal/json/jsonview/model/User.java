package io.pivotal.json.jsonview.model;

import com.fasterxml.jackson.annotation.JsonView;

public class User {
    // No JsonView so not exposed
    public int id;

    @JsonView(Versions.V1.class)
    public String name;

    public User() {
        super();
    }

    public User(final int id, final String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
