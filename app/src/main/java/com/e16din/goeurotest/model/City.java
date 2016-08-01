package com.e16din.goeurotest.model;

import java.io.Serializable;


public class City implements Serializable {

    private String name = "test";

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /// origin response json:

//    {
//        "_id": 376583,
//            "key": null,
//            "name": "Hamburg",
//            "fullName": "Hamburg, Deutschland",
//            "iata_airport_code": null,
//            "type": "location",
//            "country": "Deutschland",
//            "geo_position": {
//        "latitude": 53.57532,
//                "longitude": 10.01534
//    },
//        "locationId": 8752,
//            "inEurope": true,
//            "countryId": 56,
//            "countryCode": "DE",
//            "coreCountry": true,
//            "distance": null,
//            "names": {
//        "pt": "Hamburgo",
//                "fr": "Hambourg",
//                "ru": "Гамбург",
//                "it": "Amburgo",
//                "is": "Hamborg",
//                "fi": "Hampuri",
//                "es": "Hamburgo",
//                "zh": "汉堡",
//                "cs": "Hamburk"
//    },
//        "alternativeNames": {
//
//    }
//    },
}
