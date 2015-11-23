//
//  Map.swift
//  TabrisMapsExample
//
//  Created by Patryk Mol on 06/10/15.
//
//

import Foundation
import MapKit

extension MKMapView {
    func visibleAnnotations() -> [ESMarker] {
        return self.annotationsInMapRect(self.visibleMapRect).map { obj -> ESMarker in return obj as! ESMarker }
    }
}

public class ESMap : BasicWidget, MKMapViewDelegate, UIGestureRecognizerDelegate {

// MARK: - Properties -

    private var map: MKMapView
    private var readyEventToken: dispatch_once_t
    private var tapRecognizer: UITapGestureRecognizer?
    public var tapListener: Bool {
        willSet {
            if (newValue) {
                if (tapRecognizer != nil) {
                    tapRecognizer!.enabled = true
                } else {
                    tapRecognizer = UITapGestureRecognizer.init(target: self, action: Selector("didTap:"))
                    tapRecognizer?.numberOfTapsRequired = 1
                    tapRecognizer?.numberOfTouchesRequired = 1
                    map.addGestureRecognizer(tapRecognizer!)
                }
            } else {
                tapRecognizer!.enabled = false
            }
        }
    }
    public var readyListener: Bool
    public var panListener: Bool
    public var mapType: String {
        didSet {
            switch (mapType) {
                case "normal":
                    map.mapType = MKMapType.Standard
                case "satellite":
                    map.mapType = MKMapType.Satellite
                case "hybrid":
                    map.mapType = MKMapType.Hybrid
                case "satelliteflyover":
                    if #available(iOS 9.0, *) {
                        map.mapType = MKMapType.SatelliteFlyover
                    }
                case "hybridflyover":
                    if #available(iOS 9.0, *) {
                        map.mapType = MKMapType.HybridFlyover
                    }
                default:
                    mapType = oldValue
            }
        }
    }
    public var center: Array<CLLocationDegrees> {
        set {
            if (newValue.count >= 2) {
                map.setCenterCoordinate(CLLocationCoordinate2DMake(newValue[0], newValue[1]), animated: false)
            }
        }
        get {
            return [map.centerCoordinate.latitude, map.centerCoordinate.longitude]
        }
    }
    public var region: Array<Double> {
        set {
            if (newValue.count >= 4) {
                let center: CLLocationCoordinate2D = CLLocationCoordinate2D.init(latitude: newValue[0], longitude: newValue[1])
                let span: MKCoordinateSpan = MKCoordinateSpan.init(latitudeDelta: newValue[2], longitudeDelta: newValue[3])
                let region: MKCoordinateRegion = map.regionThatFits(MKCoordinateRegion.init(center: center, span: span))
                map.setRegion(region, animated: false)
            }
        }
        get {
            let region: MKCoordinateRegion = map.region
            return [region.center.latitude, region.center.longitude, region.span.latitudeDelta, region.span.longitudeDelta]
        }
    }

// MARK: - Initializers -

    override init!(objectId: String!, andClient client: TabrisClient!) {
        map = MKMapView()
        mapType = "normal"
        tapListener = false
        readyListener = false
        panListener = false
        tapRecognizer = nil
        readyEventToken = 0
        super.init(objectId: objectId, andClient: client)
        map.delegate = self
    }

    required convenience public init!(objectId: String!, properties: [NSObject : AnyObject]!, andClient client: TabrisClient!) {
        self.init(objectId: objectId, andClient: client)
        for (key, value) in properties {
            switch (key) {
                case "mapType":
                    self.mapType = value as! String
                case "center":
                    self.center = value as! Array<CLLocationDegrees>
                case "region":
                    self.region = value as! Array<Double>
                default:
                    break
            }
        }
    }

//  MARK: - Required Tabris methods -

    override public class func remoteObjectType() -> String {
        return "tabris.Map"
    }

    public override class func remoteObjectProperties() -> NSMutableSet {
        let set = super.remoteObjectProperties()
        set.addObject("mapType")
        set.addObject("center")
        set.addObject("region")
        return set
    }

    public override func view() -> UIView! {
        return map
    }

//  MARK: - Public methods -

    public func addMarker(marker: ESMarker) {
        map.addAnnotation(marker)
    }

    public func removeMarker(marker: ESMarker) {
        map.removeAnnotation(marker)
    }

    public func didTap(sender: UITapGestureRecognizer) {
        for marker: ESMarker in map.visibleAnnotations() {
            let touchPoint: CGPoint = sender.locationInView(map)
            let annotationView: MKAnnotationView = map.viewForAnnotation(marker)!
            let annotationViewFrame: CGRect = annotationView.frame
            if (annotationViewFrame.origin.x <= touchPoint.x && annotationViewFrame.origin.x + annotationViewFrame.size.width >= touchPoint.x &&
                annotationViewFrame.origin.y <= touchPoint.y && annotationViewFrame.origin.y + annotationViewFrame.size.height >= touchPoint.y) {
                    return
            }
        }
        if (tapListener) {
            let message: Notification = notifications.forObject(self) as! Notification
            message.fireEvent("tap", withAttributes: ["latLng":[map.centerCoordinate.latitude, map.centerCoordinate.longitude]])
        }
    }

//  MARK: - MKMapViewDelegate methods -

    public func mapViewDidFinishRenderingMap(mapView: MKMapView, fullyRendered: Bool) {
        if (readyListener) {
            dispatch_once(&readyEventToken, {
                let message: Notification = self.notifications.forObject(self) as! Notification
                message.fireEvent("ready", withAttributes: nil)
            })
        }
    }

    public func mapView(mapView: MKMapView, regionWillChangeAnimated animated: Bool) {
        if (panListener) {
            let message: Notification = notifications.forObject(self) as! Notification
            message.fireEvent("pan", withAttributes: ["latLng":[map.centerCoordinate.latitude, map.centerCoordinate.longitude]])
        }
    }

    public func mapView(mapView: MKMapView, didSelectAnnotationView view: MKAnnotationView) {
        let marker: ESMarker = view.annotation as! ESMarker
        marker.tapped()
    }

}