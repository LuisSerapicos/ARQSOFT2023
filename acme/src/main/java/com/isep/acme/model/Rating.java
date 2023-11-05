package com.isep.acme.model;


import javax.persistence.*;

@Entity
public class Rating {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long idRating;

    @Version
    private long version;

    @Column(nullable = false)
    private Double rate;

    protected Rating(){}

    public Rating(Long idRating, long version, Double rate) {
        this.idRating = idRating;
        this.version = version;
        setRate(rate);
    }

    public Long getIdRating() {
        return idRating;
    }

    public long getVersion() {
        return version;
    }

    public Rating(Double rate) {
        setRate(rate);
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
