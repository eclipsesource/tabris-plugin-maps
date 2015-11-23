//
//  ESMarker.swift
//  TabrisMapsExample
//
//  Created by Patryk Mol on 19/11/15.
//
//

import Foundation
import MapKit

public class ESMarker : BasicWidget, MKAnnotation {
    public var latLng: Array<Double> {
        set {
            if (newValue.count >= 2) {
                coordinate = CLLocationCoordinate2D.init(latitude: newValue[0], longitude: newValue[1])
            }
        }
        get {
            return [coordinate.latitude, coordinate.longitude]
        }
    }
    public var tapListener: Bool
    public var coordinate: CLLocationCoordinate2D
    public var title: String?
    public var subtitle: String?
    weak private var map: ESMap?

    override init!(objectId: String!, andClient client: TabrisClient!) {
        coordinate = CLLocationCoordinate2D.init(latitude: 0, longitude: 0);
        tapListener = false
        super.init(objectId: objectId, andClient: client)
    }

    required convenience public init!(objectId: String!, properties: [NSObject : AnyObject]!, andClient client: TabrisClient!) {
        self.init(objectId: objectId, andClient: client)
        let propertiesDictionary: NSDictionary = properties as NSDictionary
        let coordinates: Array<Double>? = propertiesDictionary.objectForKey("latLng") as? Array<Double>
        if (coordinates != nil) {
            coordinate = CLLocationCoordinate2D.init(latitude: coordinates![0], longitude: coordinates![1])
        }
        let parentId: String? = propertiesDictionary.objectForKey("parent") as? String
        if (parentId != nil) {
            map = client.objectById(parentId) as? ESMap
            map!.addMarker(self)
        }
    }

    override public func view() -> UIView! {
        return nil;
    }

    public override class func remoteObjectProperties() -> NSMutableSet {
        let set = super.remoteObjectProperties()
        set.addObject("latLng")
        return set
    }

    override public class func remoteObjectType() -> String {
        return "tabris.maps.marker"
    }

    public func tapped() {
        if (tapListener) {
            let message: Notification = notifications.forObject(self) as! Notification
            message.fireEvent("tap", withAttributes: nil)
        }
    }

    override public func destroy() {
        super.destroy()
        map?.removeMarker(self)
    }
}