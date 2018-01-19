package riws.map;

import java.util.ArrayList;
import java.util.List;

import de.fhpotsdam.unfolding.UnfoldingMap;
import de.fhpotsdam.unfolding.data.Feature;
import de.fhpotsdam.unfolding.data.GeoRSSReader;
import de.fhpotsdam.unfolding.data.PointFeature;
import de.fhpotsdam.unfolding.marker.Marker;
import de.fhpotsdam.unfolding.providers.Microsoft;
import de.fhpotsdam.unfolding.utils.MapUtils;
import processing.core.PApplet;
import processing.core.PFont;

public class SimpleMapApp extends PApplet {
	private static final long serialVersionUID = -7859126349673340658L;
	// map = new UnfoldingMap(this, new Microsoft.AerialProvider());

	UnfoldingMap map;

	public void setup() {
		size(800, 600, OPENGL);

		map = new UnfoldingMap(this, new Microsoft.AerialProvider());
		map.zoomToLevel(2);
		MapUtils.createDefaultEventDispatcher(this, map);

		List<Feature> features = GeoRSSReader.loadData(this, "markers.xml");
		List<Marker> markers = createLabeledMarkers(features);
		map.addMarkers(markers);
	}

	public void draw() {
		background(240);
		map.draw();
	}

	public void mouseMoved() {
		// Deselect all marker
		for (Marker marker : map.getMarkers()) {
			marker.setSelected(false);
		}

		// Select hit marker
		Marker marker = map.getFirstHitMarker(mouseX, mouseY);
		// NB: Use mm.getHitMarkers(x, y) for multi-selection.
		if (marker != null) {
			marker.setSelected(true);
		}

		// List<Marker> markers = map.getHitMarkers(mouseX, mouseY);
		// for (Marker marker2 : markers) {
		// if (marker2 != null) {
		// marker2.setSelected(true);
		// }
		// }

	}

	public List<Marker> createLabeledMarkers(List<Feature> features) {
		PFont font = loadFont("ui/OpenSans-12.vlw");
		List<Marker> markers = new ArrayList<Marker>();
		for (Feature feature : features) {
			String label = feature.getStringProperty("title");
			PointFeature pointFeature = (PointFeature) feature;
			Marker marker = new LabeledMarker(pointFeature.getLocation(), label, font, 15);
			markers.add(marker);
		}
		return markers;
	}
}
