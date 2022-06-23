package com.otto.ramarket.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "shop_units")
public class ShopUnit {
    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private ShopUnitType type;

    @Column(name = "date", nullable = false)
    private ZonedDateTime date;

    @ManyToOne(cascade = {CascadeType.REMOVE})
    @JoinColumn(name = "parent_id")
    private ShopUnit parent;

    @Column(name = "price")
    private Long price;

    @OneToMany(mappedBy = "parent")
    private List<ShopUnit> children;

    public ShopUnit() {

    }

    public ShopUnit(UUID id, String name, ShopUnitType type, ZonedDateTime date, ShopUnit parent, Long price) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.date = date;
        this.parent = parent;
        this.price = price;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ShopUnitType getType() {
        return type;
    }

    public void setType(ShopUnitType type) {
        this.type = type;
    }

    public ZonedDateTime getDate() {
        return date;
    }

    public void setDate(ZonedDateTime date) {
        this.date = date;
    }

    public ShopUnit getParent() {
        return parent;
    }

    public void setParent(ShopUnit parent) {
        this.parent = parent;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public List<ShopUnit> getChildren() {
        return children;
    }

    public void setChildren(List<ShopUnit> children) {
        this.children = children;
    }

    @Override
    public String toString() {
        return "ShopUnit{" +
                "id=" + id +
                ", name='" + name +
                ", type=" + type +
                ", date=" + date +
                ", parent=" + parent +
                ", price=" + price +
                '}';
    }
}
