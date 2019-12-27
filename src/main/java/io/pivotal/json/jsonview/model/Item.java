package io.pivotal.json.jsonview.model;

import com.fasterxml.jackson.annotation.JsonView;

public class Item {
    @JsonView(Versions.V1.class)
    public int id;

    @JsonView(Versions.V1.class)
    public String itemName;

    @JsonView(Versions.V2.class)
    public String ownerName;

    @JsonView(Versions.V3.class)
    public String ownerCreditCard;

    public Item() {
        super();
    }

    public Item(final int id, final String itemName, final String ownerName,
                final String creditCard) {
        this.id = id;
        this.itemName = itemName;
        this.ownerName = ownerName;
        this.ownerCreditCard = creditCard;
    }

    public int getId() {
        return id;
    }

    public String getItemName() {
        return itemName;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getOwnerCreditCard() {
        return ownerCreditCard;
    }
}
