package jp.vmi.html;

import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class ConfidentialMarkerFactory {
    private static final String CK_CONFIDENTIAL = "CONFIDENTIAL";

    public static Marker getMarker() {
        return MarkerFactory.getMarker(CK_CONFIDENTIAL);
    }
}
