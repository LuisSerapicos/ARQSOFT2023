package com.isep.acme.persistance.mongodb;

import com.isep.acme.model.Rating;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.Objects;

import static com.isep.acme.utils.Generator.generateLongID;
import static com.isep.acme.utils.Generator.generateVersion;

@Document(collection = "rating")
@Data
@NoArgsConstructor
public class RatingMongo {
    @Field(value = "idRating")
    private Long idRating;

    @Field(value = "version")
    private long version;

    @Field(value = "rate")
    private Double rate;


    public RatingMongo(long version, Double rate) {
        this.idRating = generateLongID();
        this.version = Objects.requireNonNull(version);
        setRate(rate);
    }

    public RatingMongo(Double rate) {
        this.idRating = generateLongID();
        this.version = generateVersion();
        setRate(rate);
    }

    public RatingMongo(Long idRating, long versionDouble, double rate) {
        this.idRating = idRating;
        this.version = versionDouble;
        setRate(rate);
    }

    public Long getIdRating() {
        return idRating;
    }

    public long getVersion() {
        return version;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Rating toRating(){
        return new Rating(this.idRating,this.version,this.getRate());
    }
}
