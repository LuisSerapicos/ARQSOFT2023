package acme.persistance.neo4j;

import acme.model.Rating;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Objects;

@Node("RatingNeo4J")
public class RatingNeo4J {
    @Id
    @GeneratedValue
    private Long id;

    private Long idRating;

    private long version;

    private Double rate;

    public RatingNeo4J() {

    }

    public RatingNeo4J(Long idRating, long version, Double rate) {
        this.idRating = Objects.requireNonNull(idRating);
        this.version = Objects.requireNonNull(version);
        setRate(rate);
    }

    public RatingNeo4J(Double rate) {
        setRate(rate);
    }

    public Long getIdRating() {
        return idRating;
    }

    public void setIdRating(Long idRating) {
        this.idRating = idRating;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public Rating toRating() {
        return new Rating(this.idRating, this.version, this.rate);
    }
}
