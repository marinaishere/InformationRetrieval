package riws.google.geolocate;

import java.io.IOException;

import com.google.code.geocoder.Geocoder;
import com.google.code.geocoder.GeocoderRequestBuilder;
import com.google.code.geocoder.model.GeocodeResponse;
import com.google.code.geocoder.model.GeocoderRequest;
import com.google.code.geocoder.model.LatLng;

public class LocateUtils {

	public static Coord getLocationCoord(String address) throws IOException {
		if (address == null)
			return null;
		final Geocoder geocoder = new Geocoder();
		GeocoderRequest geocoderRequest = new GeocoderRequestBuilder().setAddress(address).setLanguage("en").getGeocoderRequest();
		GeocodeResponse geocoderResponse = geocoder.geocode(geocoderRequest);
		if (geocoderResponse.getResults().size() > 0) {
			LatLng coord = geocoderResponse.getResults().get(0).getGeometry().getLocation();
			return new Coord(coord.getLat(), coord.getLng());
		}
		return null;
	}

}
