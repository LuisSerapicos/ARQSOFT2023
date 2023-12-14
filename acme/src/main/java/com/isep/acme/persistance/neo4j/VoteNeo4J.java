package com.isep.acme.persistance.neo4j;

import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;

import java.util.Objects;

@Node("VoteNeo4J")
public class VoteNeo4J {
    @Id
    @GeneratedValue
    private Long id;
    private String vote;
    private Long userID;


    protected VoteNeo4J() {

    }

    public VoteNeo4J(String vote, Long userID) {
        this.vote = vote;
        this.userID = userID;
    }

    public String getVote() {
        return vote;
    }

    public void setVote(String vote) {
        this.vote = vote;
    }

    public Long getUserID() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        VoteNeo4J vote1 = (VoteNeo4J) o;
        return vote.equals(vote1.getVote()) && userID.equals(vote1.getUserID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(vote, userID);
    }
}
