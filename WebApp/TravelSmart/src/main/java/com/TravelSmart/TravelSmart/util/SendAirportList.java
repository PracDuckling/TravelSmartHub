package com.TravelSmart.TravelSmart.util;

import java.util.List;

public class SendAirportList {
    List<String> source;
    List<String> destination;

    public SendAirportList(List<String> source, List<String> destination) {
        this.source = source;
        this.destination = destination;
    }

    public List<String> getSource() {
        return source;
    }

    public void setSource(List<String> source) {
        this.source = source;
    }

    public List<String> getDestination() {
        return destination;
    }

    public void setDestination(List<String> destination) {
        this.destination = destination;
    }
}
