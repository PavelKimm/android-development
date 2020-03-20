package ru.tpu.courses.lab2;

import java.util.Comparator;

class Record {
    private Integer id;
    private String title;
    private Float rate;

    Record(Integer id, String title, Float rate) {
        this.id = id;
        this.title = title;
        this.rate = rate;
    }

    Integer getId() {
        return id;
    }

    String getTitle() {
        return title;
    }

    Float getRate() {
        return rate;
    }

    public static Comparator<Record> rateComparator = new Comparator<Record>() {
        @Override
        public int compare(Record r1, Record r2) {
            return (r2.getRate().compareTo(r1.getRate()));
        }
    };
}
