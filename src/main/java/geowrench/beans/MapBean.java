package geowrench.beans;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author Tero Rantanen
 */
@ManagedBean(name = "mapBean")
@SessionScoped
public class MapBean {

    private final String centerCoordinates = "61.4900, 23.8900";
    private final MapModel mapModel;
    private Marker selectedMarker;
    private String markerTitle = "Start";

    /**
     * Creates a new instance of MapBean
     */
    public MapBean() {
        mapModel = new DefaultMapModel();
        LatLng coord1 = new LatLng(61.4950, 23.8950);
        
        Marker m = new Marker(coord1, "Marker1");
        m.setTitle("TITTELI");
        m.setClickable(true);
        mapModel.addOverlay(m);
    }

    public String getCenterCoordinates() {
        return centerCoordinates;
    }

    public MapModel getMapModel() {
        return mapModel;
    }

    public Marker getSelectedMarker() {
        return selectedMarker;
    }

    public void setSelectedMarker(Marker val) {
        selectedMarker = val;
    }

    public String getMarkerTitle() {
        return markerTitle;
    }

    public void setMarkerTitle(String s) {
        markerTitle = s;
    }

    public void onMarkerSelect(OverlaySelectEvent event) {
        selectedMarker = (Marker) event.getOverlay();
        System.out.println(selectedMarker.getTitle());
    }


}
