package models;

import java.util.List;
import java.util.logging.LogManager;

public class LMessage {
    private String sourceName;
    private int seq;
    private int age;
    private List<LinkCount> listLink;

    public LMessage(){}

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public List<LinkCount> getListLink() {
        return listLink;
    }

    public void setListLink(List<LinkCount> listLink) {
        this.listLink = listLink;
    }

    public LMessage(String sourceName, int seq, int age, List<LinkCount> listLink) {
        this.sourceName = sourceName;
        this.seq = seq;
        this.age = age;
        this.listLink = listLink;
    }

    @Override
    public String toString() {
        return "LMessage{" +
                "sourceName='" + sourceName + '\'' +
                ", seq=" + seq +
                ", age=" + age +
                ", listLink=" + listLink +
                '}';
    }
}
