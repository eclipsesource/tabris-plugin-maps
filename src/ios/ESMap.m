//
//  ESMap.m
//  TabrisMapsExample
//
//  Created by Patryk Mol on 30/06/16.
//
//

#import "ESMap.h"
#import "ESMarker.h"
#import <MapKit/MapKit.h>

@interface MKMapView (Tabris)
- (NSSet *)visibleAnnotations;
@end

@implementation MKMapView (Tabris)
- (NSSet *)visibleAnnotations {
    return [self annotationsInMapRect:self.visibleMapRect];
}
@end

@interface ESMap () <MKMapViewDelegate, UIGestureRecognizerDelegate>
@property (strong) MKMapView *map;
@property (assign) dispatch_once_t readyEventToken;
@property (strong) UITapGestureRecognizer *tapRecognizer;
@end

@implementation ESMap

@synthesize map = _map;
@synthesize readyEventToken = _readyEventToken;
@synthesize tapRecognizer = _tapRecognizer;
@synthesize mapType = _mapType;
@synthesize center = _center;
@synthesize region = _region;
@synthesize myLocation = _myLocation;
@synthesize tapListener = _tapListener;
@synthesize readyListener = _readyListener;
@synthesize panListener = _panListener;
@synthesize showMyLocation = _showMyLocation;

- (instancetype)initWithObjectId:(NSString *)objectId properties:(NSDictionary *)properties andClient:(TabrisClient *)client {
    self = [super initWithObjectId:objectId properties:properties andClient:client];
    if (self) {
        NSString *mapType = [properties objectForKey:@"mapType"];
        if (mapType) {
            self.mapType = mapType;
        }
        NSArray *center = [properties objectForKey:@"center"];
        if (center) {
            self.center = center;
        }
        NSArray *region = [properties objectForKey:@"region"];
        if (region) {
            self.region = region;
        }
        BOOL showMyLocation = [[properties objectForKey:@"showMyLocation"] boolValue];
        if (showMyLocation) {
            self.showMyLocation = showMyLocation;
        }
    }
    return self;
}

- (instancetype)initWithObjectId:(NSString *)objectId andClient:(TabrisClient *)client {
    self = [super initWithObjectId:objectId andClient:client];
    if (self) {
        self.map = [[MKMapView alloc] init];
        self.map.delegate = self;
        self.mapType = @"normal";
        self.tapRecognizer = nil;
        self.readyEventToken = 0;
    }
    return self;
}

+ (NSString *)remoteObjectType {
    return @"tabris.Map";
}

+ (NSMutableSet *)remoteObjectProperties {
    NSMutableSet *set = [super remoteObjectProperties];
    [set addObject:@"mapType"];
    [set addObject:@"center"];
    [set addObject:@"myLocation"];
    [set addObject:@"region"];
    [set addObject:@"showMyLocation"];
    [set addObject:@"tapListener"];
    [set addObject:@"readyListener"];
    [set addObject:@"panListener"];
    return set;
}

- (UIView *)view {
    return self.map;
}

- (void)setMapType:(NSString *)mapType {
    if ([mapType isEqualToString:@"normal"]) {
        self.map.mapType = MKMapTypeStandard;
        _mapType = mapType;
    } else if ([mapType isEqualToString:@"satellite"]) {
        self.map.mapType = MKMapTypeSatellite;
        _mapType = mapType;
    } else if ([mapType isEqualToString:@"hybrid"]) {
        self.map.mapType = MKMapTypeHybrid;
        _mapType = mapType;
    } else if ([mapType isEqualToString:@"satelliteflyover"] && [[[UIDevice currentDevice] systemVersion] floatValue] >= 9.0f) {
        self.map.mapType = MKMapTypeSatelliteFlyover;
        _mapType = mapType;
    } else if ([mapType isEqualToString:@"hybridflyover"] && [[[UIDevice currentDevice] systemVersion] floatValue] >= 9.0f) {
        self.map.mapType = MKMapTypeHybridFlyover;
        _mapType = mapType;
    }
}

- (NSString *)mapType {
    return _mapType;
}

- (void)setTapListener:(BOOL)tapListener {
    _tapListener = tapListener;
    if (tapListener) {
        if (self.tapRecognizer != nil) {
            self.tapRecognizer.enabled = YES;
        } else {
            self.tapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action:@selector(didTap:)];
            self.tapRecognizer.numberOfTapsRequired = 1;
            self.tapRecognizer.numberOfTouchesRequired = 1;
            [self.map addGestureRecognizer:self.tapRecognizer];
        }
    } else {
        self.tapRecognizer.enabled = NO;
    }
}

- (BOOL)tapListener {
    return _tapListener;
}

- (void)setShowMyLocation:(BOOL)showMyLocation {
    self.map.showsUserLocation = showMyLocation;
}

- (BOOL)showMyLocation {
    return self.map.showsUserLocation;
}

- (NSArray *)myLocation {
    CLLocationDegrees latitude = self.map.userLocation.location.coordinate.latitude;
    CLLocationDegrees longitude = self.map.userLocation.location.coordinate.longitude;
    if (latitude == 0 && longitude == 0) {
        return nil;
    }
    return @[@(latitude), @(longitude)];
}

- (void)setCenter:(NSArray *)center {
    if ([center count] < 2) {
        return;
    }
    self.map.centerCoordinate = CLLocationCoordinate2DMake([[center objectAtIndex:0] floatValue], [[center objectAtIndex:1] floatValue]);
}

- (NSArray *)center {
    return @[@(self.map.centerCoordinate.latitude), @(self.map.centerCoordinate.longitude)];
}

- (void)setRegion:(NSArray *)region {
    if ([region count] < 4) {
        return;
    }
    CLLocationCoordinate2D center = CLLocationCoordinate2DMake([[region objectAtIndex:0] floatValue], [[region objectAtIndex:1] floatValue]);
    MKCoordinateSpan span = MKCoordinateSpanMake([[region objectAtIndex:2] floatValue], [[region objectAtIndex:3] floatValue]);
    self.map.region = [self.map regionThatFits:MKCoordinateRegionMake(center, span)];
}

- (NSArray *)region {
    MKCoordinateRegion region = self.map.region;
    return @[@(region.center.latitude), @(region.center.longitude), @(region.span.latitudeDelta), @(region.span.longitudeDelta)];
}

- (void)addMarker:(ESMarker *)marker {
    [self.map addAnnotation:marker];
}

- (void)removeMarker:(ESMarker *)marker {
    [self.map removeAnnotation:marker];
}

- (void)didTap:(UITapGestureRecognizer *)sender {
    CGPoint touchPoint = [sender locationInView:self.map];
    for (ESMarker *marker in self.map.visibleAnnotations) {

        MKAnnotationView *annotationView = [self.map viewForAnnotation:marker];
        CGRect annotationViewFrame = annotationView.frame;
        if (annotationViewFrame.origin.x <= touchPoint.x && annotationViewFrame.origin.x + annotationViewFrame.size.width >= touchPoint.x &&
            annotationViewFrame.origin.y <= touchPoint.y && annotationViewFrame.origin.y + annotationViewFrame.size.height >= touchPoint.y) {
            return;
        }
    }
    if (self.tapListener) {
        CLLocationCoordinate2D coordinate = [self.map convertPoint:touchPoint toCoordinateFromView:self.map];
        Message<Notification> *message = [[self notifications] forObject:self];
        [message fireEvent:@"tap" withAttributes:@{@"latLng":@[@(coordinate.latitude), @(coordinate.longitude)]}];
    }
}

- (void)mapViewDidFinishRenderingMap:(MKMapView *)mapView fullyRendered:(BOOL)fullyRendered {
    if (self.readyListener) {
        dispatch_once(&_readyEventToken, ^{
            Message<Notification> *message = [[self notifications] forObject:self];
            [message fireEvent:@"ready" withAttributes:@{}];
        });
    }
}

- (void)mapView:(MKMapView *)mapView regionWillChangeAnimated:(BOOL)animated {
    if (self.panListener) {
        Message<Notification> *message = [[self notifications] forObject:self];
        [message fireEvent:@"ready" withAttributes:@{@"latLng":@[@(self.map.centerCoordinate.latitude), @(self.map.centerCoordinate.longitude)]}];
    }
}

- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view {
    if ([NSStringFromClass([view class]) isEqualToString:@"MKModernUserLocationView"]) {
        return;
    }
    ESMarker *marker = (ESMarker *)view.annotation;
    [marker tapped];
}

@end
