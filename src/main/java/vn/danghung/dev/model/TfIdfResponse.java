package vn.danghung.dev.model;

public class TfIdfResponse {
    private String term;
    private int df;
    private long total_if;
    private double idf;
    private double pwc;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public int getDf() {
        return df;
    }

    public void setDf(int df) {
        this.df = df;
    }

    public long getTotal_if() {
        return total_if;
    }

    public void setTotal_if(long total_if) {
        this.total_if = total_if;
    }

    public double getIdf() {
        return idf;
    }

    public void setIdf(double idf) {
        this.idf = idf;
    }

    public double getPwc() {
        return pwc;
    }

    public void setPwc(double pwc) {
        this.pwc = pwc;
    }
}
