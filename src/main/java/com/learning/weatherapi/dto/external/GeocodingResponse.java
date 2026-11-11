
package com.learning.weatherapi.dto.external;

import java.util.List;

public record GeocodingResponse(
        List<Location> results
) {
    public record Location(
            String name,
            double latitude,
            double longitude,
            String country
    ) {
    }
}
