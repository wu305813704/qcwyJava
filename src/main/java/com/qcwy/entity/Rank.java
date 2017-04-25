package com.qcwy.entity;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

/**
 * Created by KouKi on 2017/3/22.
 */
public class Rank  implements Serializable {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String job_no;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int score;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private int rank;

    public String getJob_no() {
        return job_no;
    }

    public void setJob_no(String job_no) {
        this.job_no = job_no;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
