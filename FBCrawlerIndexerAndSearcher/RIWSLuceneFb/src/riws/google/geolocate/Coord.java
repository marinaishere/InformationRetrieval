package riws.google.geolocate;

import java.math.BigDecimal;

public class Coord {

	private BigDecimal lat;
	private BigDecimal lon;

	public Coord(BigDecimal lat, BigDecimal lon) {
		this.lat = lat;
		this.lon = lon;
	}

	public BigDecimal getLat() {
		return lat;
	}

	public BigDecimal getLon() {
		return lon;
	}

}
