//
//  ESMap.m
//  TabrisMapsExample
//
//  Created by Patryk Mol on 30/06/16.
//
//

#import "ESMap.h"
#import "ESMarker.h"
#import <Tabris/TabrisImage.h>
#import <Tabris/BasicObject+Shorthands.h>
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
@property (strong) UILongPressGestureRecognizer *longpressRecognizer;
@property (strong) UITapGestureRecognizer *doubleTapRecognizer;
@property (strong) UIPanGestureRecognizer *panRecognizer;
@property (strong) UIPinchGestureRecognizer *pinchRecognizer;
@property (assign) BOOL gestureWasRecognized;
@end

@implementation ESMap

@synthesize map = _map;
@synthesize readyEventToken = _readyEventToken;
@synthesize tapRecognizer = _tapRecognizer;
@synthesize longpressRecognizer = _longpressRecognizer;
@synthesize doubleTapRecognizer = _doubleTapRecognizer;
@synthesize panRecognizer = _panRecognizer;
@synthesize pinchRecognizer = _pinchRecognizer;
@synthesize mapType = _mapType;
@synthesize position = _position;
@synthesize region = _region;
@synthesize myLocation = _myLocation;
@synthesize tapListener = _tapListener;
@synthesize longpressListener = _longpressListener;
@synthesize readyListener = _readyListener;
@synthesize cameraMovedListener = _cameraMovedListener;
@synthesize changeCameraListener = _changeCameraListener;
@synthesize showMyLocation = _showMyLocation;

- (instancetype)initWithObjectId:(NSString *)objectId properties:(NSDictionary *)properties inContext:(id<TabrisContext>)context {
    self = [super initWithObjectId:objectId properties:properties inContext:context];
    if (self) {
        self.map = [[MKMapView alloc] init];
        self.map.delegate = self;
        self.map.showsPointsOfInterest = NO;
        self.mapType = @"normal";
        self.tapRecognizer = nil;
        self.longpressRecognizer = nil;
        self.doubleTapRecognizer = nil;
        self.panRecognizer = nil;
        self.pinchRecognizer = nil;
        self.gestureWasRecognized = NO;
        self.readyEventToken = 0;
        self.interactiveWhenEnabled = YES;
        [self adjustUserInteraction];
        [self defineWidgetView:self.map];
        [self registerSelector:@selector(moveToRegion:) forCall:@"moveToRegion"];
        [self registerSelector:@selector(addMarker:) forCall:@"addMarker"];
        [self registerSelector:@selector(removeMarker:) forCall:@"removeMarker"];
        NSString *mapType = [properties objectForKey:@"mapType"];
        if (mapType) {
            self.mapType = mapType;
        }
        NSArray *position = [properties objectForKey:@"position"];
        if (position) {
            self.position = position;
        }
        NSDictionary *region = [properties objectForKey:@"region"];
        if (region) {
            self.region = region;
        }
        NSDictionary *camera = [properties objectForKey:@"camera"];
        if (camera) {
            self.camera = camera;
        }
        NSNumber *showMyLocation = [properties objectForKey:@"showMyLocation"];
        if (showMyLocation) {
            self.showMyLocation = [showMyLocation boolValue];
        }
    }
    return self;
}

+ (NSString *)remoteObjectType {
    return @"com.eclipsesource.maps.Map";
}

+ (NSMutableSet *)remoteObjectProperties {
    NSMutableSet *set = [super remoteObjectProperties];
    [set addObject:@"mapType"];
    [set addObject:@"position"];
    [set addObject:@"camera"];
    [set addObject:@"myLocation"];
    [set addObject:@"region"];
    [set addObject:@"showMyLocation"];
    [set addObject:@"tapListener"];
    [set addObject:@"longpressListener"];
    [set addObject:@"readyListener"];
    [set addObject:@"cameraMovedListener"];
    [set addObject:@"changecameraListener"];
    return set;
}

- (void)regionWillChangeByUserInteraction:(UIGestureRecognizer*)gestureRecognizer {
    if (gestureRecognizer.state == UIGestureRecognizerStateEnded) {
        self.gestureWasRecognized = YES;
    }
}

- (BOOL)gestureRecognizer:(UIGestureRecognizer *)gestureRecognizer shouldRecognizeSimultaneouslyWithGestureRecognizer:(UIGestureRecognizer *)otherGestureRecognizer {
    return YES;
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

- (MKMapRect)getRectangleFromCoordinates:(NSArray *)northEastCoordinate
                     southWestCoordinate:(NSArray *)southWestCoordinate {
    MKMapPoint northEastPoint = MKMapPointForCoordinate(CLLocationCoordinate2DMake([[northEastCoordinate objectAtIndex:0] floatValue],
                                                                                   [[northEastCoordinate objectAtIndex:1] floatValue]));
    MKMapPoint southWestPoint = MKMapPointForCoordinate(CLLocationCoordinate2DMake([[southWestCoordinate objectAtIndex:0] floatValue],
                                                                                   [[southWestCoordinate objectAtIndex:1] floatValue]));
    MKMapRect southWestRectangle = MKMapRectMake(southWestPoint.x, southWestPoint.y, 0, 0);
    MKMapRect northEastRectangle = MKMapRectMake(northEastPoint.x, northEastPoint.y, 0, 0);
    return MKMapRectUnion(northEastRectangle, southWestRectangle);
}

- (void)moveToRegion: (NSDictionary *)parameters {
    NSDictionary *region = [parameters objectForKey:@"region"];
    NSArray *northEastCoordinate = [region objectForKey:@"northEast"];
    NSArray *southWestCoordinate = [region objectForKey:@"southWest"];
    NSDictionary *options = [parameters objectForKey:@"options"];
    CGFloat padding = [[options objectForKey:@"padding"] floatValue];
    BOOL animate = [[options objectForKey:@"animate"] boolValue];
    if ([northEastCoordinate count] < 2 || [southWestCoordinate count] < 2) {
        return;
    }
    MKMapRect rectangle = [self getRectangleFromCoordinates:northEastCoordinate southWestCoordinate:southWestCoordinate];
    [self.map setVisibleMapRect:rectangle edgePadding:UIEdgeInsetsMake(padding, padding, padding, padding) animated:animate];
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

- (void)setLongpressListener:(BOOL)longpressListener {
    _longpressListener = longpressListener;
    if (longpressListener) {
        if (self.longpressRecognizer != nil) {
            self.longpressRecognizer.enabled = YES;
        } else {
            self.longpressRecognizer = [[UILongPressGestureRecognizer alloc] initWithTarget:self action:@selector(didLongpress:)];
            [self.map addGestureRecognizer:self.longpressRecognizer];
        }
    } else {
        self.longpressRecognizer.enabled = NO;
    }
}

- (BOOL)longpressListener {
    return _longpressListener;
}

- (void)setCameraMovedListener:(BOOL)cameraMovedListener {
    _cameraMovedListener = cameraMovedListener;
    if (cameraMovedListener) {
        [self enableDoubleTapGestureRecognizer];
        [self enablePanGestureRecognizer];
        [self enablePinchGestureRecognizer];
    } else {
        self.doubleTapRecognizer.enabled = NO;
        self.panRecognizer.enabled = NO;
        self.pinchRecognizer.enabled = NO;
    }
}

- (BOOL)cameraMovedListener {
    return _cameraMovedListener;
}

- (void)enableDoubleTapGestureRecognizer {
    if (self.doubleTapRecognizer != nil) {
        self.doubleTapRecognizer.enabled = YES;
    } else {
        self.doubleTapRecognizer = [[UITapGestureRecognizer alloc] initWithTarget:self action: @selector(regionWillChangeByUserInteraction:)];
        self.doubleTapRecognizer.numberOfTapsRequired = 2;
        self.doubleTapRecognizer.numberOfTouchesRequired = 1;
        [self.doubleTapRecognizer setDelegate: self];
        [self.map addGestureRecognizer:self.doubleTapRecognizer];
    }
}

- (void)enablePanGestureRecognizer {
    if (self.panRecognizer != nil) {
        self.panRecognizer.enabled = YES;
    } else {
        self.panRecognizer = [[UIPanGestureRecognizer alloc] initWithTarget:self action: @selector(regionWillChangeByUserInteraction:)];
        [self.panRecognizer setDelegate:self];
        [self.map addGestureRecognizer:self.panRecognizer];
    }
}

- (void)enablePinchGestureRecognizer {
    if (self.pinchRecognizer != nil) {
        self.pinchRecognizer.enabled = YES;
    } else {
        self.pinchRecognizer = [[UIPinchGestureRecognizer alloc] initWithTarget:self action: @selector(regionWillChangeByUserInteraction:)];
        [self.pinchRecognizer setDelegate:self];
        [self.map addGestureRecognizer:self.pinchRecognizer];
    }
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

- (void)setPosition:(NSArray *)position {
    if ([position count] < 2) {
        return;
    }
    self.map.centerCoordinate = CLLocationCoordinate2DMake([[position objectAtIndex:0] floatValue], [[position objectAtIndex:1] floatValue]);
    [self handleProgrammaticCameraChange];
}

- (NSArray *)position {
    return @[@(self.map.centerCoordinate.latitude), @(self.map.centerCoordinate.longitude)];
}

- (void)setCamera:(NSDictionary *)camera {
    self.position = [camera objectForKey:@"position"];
}

- (NSDictionary *)camera {
    return @{@"position": @[@(self.map.centerCoordinate.latitude), @(self.map.centerCoordinate.longitude)]};
}

- (void)setRegion:(NSDictionary *)region {
    NSArray *northEastCoordinate = [region objectForKey:@"northEast"];
    NSArray *southWestCoordinate = [region objectForKey:@"southWest"];
    if ([northEastCoordinate count] < 2 || [southWestCoordinate count] < 2) {
        return;
    }
    [self.map setVisibleMapRect:[self getRectangleFromCoordinates:northEastCoordinate southWestCoordinate:southWestCoordinate]];
    [self handleProgrammaticCameraChange];
}

- (void)handleProgrammaticCameraChange {
    // Prevent canceled gestures from being handled.
    if (self.gestureWasRecognized) {
        self.gestureWasRecognized = NO;
    }
}

- (NSDictionary *)region {
    MKMapRect rectangle = self.map.visibleMapRect;
    MKMapPoint northEastPoint = MKMapPointMake(MKMapRectGetMaxX(rectangle), rectangle.origin.y);
    MKMapPoint southWestPoint = MKMapPointMake(rectangle.origin.x, MKMapRectGetMaxY(rectangle));
    CLLocationCoordinate2D northEastCordinate = MKCoordinateForMapPoint(northEastPoint);
    CLLocationCoordinate2D southWestCoordinate = MKCoordinateForMapPoint(southWestPoint);
    return @{
             @"northEast": @[@(northEastCordinate.latitude), @(northEastCordinate.longitude)],
             @"southWest": @[@(southWestCoordinate.latitude), @(southWestCoordinate.longitude)]
             };
}

- (void)addMarker:(NSDictionary *)properties {
    ESMarker *marker = [self getMarkerFrom:properties];
    marker.map = self;
    if (marker) {
        [self.map addAnnotation:(ESMarker *) marker];
    }
}

- (void)removeMarker:(NSDictionary *)properties {
    ESMarker *marker = [self getMarkerFrom:properties];
    if (marker) {
        [self.map removeAnnotation:(ESMarker *) marker];
    }
}

- (ESMarker *)getMarkerFrom:(NSDictionary *)properties {
    NSString *markerId = [properties objectForKey:@"marker"];
    if (!markerId) {
        return nil;
    }
    id<RemoteObject> marker = [self objectById:markerId];
    if(![marker isKindOfClass:[ESMarker class]]) {
        return nil;
    }
    return (ESMarker *) marker;
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
        [self fireEventNamed:@"tap" withAttributes:@{@"position":@[@(coordinate.latitude), @(coordinate.longitude)]}];
    }
}

- (void)didLongpress:(UILongPressGestureRecognizer *)sender {
    CGPoint touchPoint = [sender locationInView:self.map];
    if (self.longpressListener && sender.state == UIGestureRecognizerStateBegan) {
        CLLocationCoordinate2D coordinate = [self.map convertPoint:touchPoint toCoordinateFromView:self.map];
        [self fireEventNamed:@"longpress" withAttributes:@{@"position":@[@(coordinate.latitude), @(coordinate.longitude)]}];
    }
}

- (void)mapViewDidFinishRenderingMap:(MKMapView *)mapView fullyRendered:(BOOL)fullyRendered {
    if (self.readyListener) {
        dispatch_once(&_readyEventToken, ^{
            [self fireEventNamed:@"ready" withAttributes:@{}];
        });
    }
}

- (void)mapView:(MKMapView *)mapView regionDidChangeAnimated:(BOOL)animated {
    if (self.changeCameraListener) {
        [self fireEventNamed:@"changeCamera" withAttributes:self.camera];
    }
    if (self.cameraMovedListener && self.gestureWasRecognized) {
        self.gestureWasRecognized = NO;
        [self fireEventNamed:@"cameraMoved" withAttributes:@{@"camera":self.camera}];
    }
}

- (void)mapView:(MKMapView *)mapView didSelectAnnotationView:(MKAnnotationView *)view {
    if ([NSStringFromClass([view class]) isEqualToString:@"MKModernUserLocationView"]) {
        return;
    }
    ESMarker *marker = (ESMarker *)view.annotation;
    [marker tapped];
}

- (MKAnnotationView *)mapView:(MKMapView *)mapView viewForAnnotation:(id<MKAnnotation>)annotation {
    if (![annotation isKindOfClass:[ESMarker class]]) {
        return nil;
    }

    NSObject* image = ((ESMarker *) annotation).image;
    NSString* reuseIdentifier = [self reuseIdentifierForImage:image] ?: @"pin";
    MKAnnotationView *view = [mapView dequeueReusableAnnotationViewWithIdentifier:reuseIdentifier];
    if (view) {
        view.annotation = annotation;
        return view;
    }
    return [self createAnnotationView:annotation reuseIdentifier:reuseIdentifier image:image];
}

- (NSString*)reuseIdentifierForImage:(NSObject*)imageObject {
    if ([imageObject isKindOfClass:[NSArray class]]) {
        NSArray* imageArray = ((NSArray*)imageObject);
        return imageArray.count ? [imageArray objectAtIndex:0] : nil;
    } else if ([imageObject isKindOfClass:[NSDictionary class]]) {
        NSDictionary* imageDictionary = ((NSDictionary*)imageObject);
        return [imageDictionary objectForKey:@"src"];
    }
    return nil;
}

- (MKAnnotationView*)createAnnotationView:(id<MKAnnotation>)annotation reuseIdentifier:(NSString*)reuseIdentifier image:(NSObject *)image {
    if (image) {
        MKAnnotationView* view = [[MKAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:reuseIdentifier];
        [self setAnnotationViewImage:view image:image];
        return view;
    } else {
        return [[MKPinAnnotationView alloc] initWithAnnotation:annotation reuseIdentifier:reuseIdentifier];
    }
}

- (void)setAnnotationViewImage:(MKAnnotationView *)view image: (NSObject *) image {
    __block TabrisImage *tabrisImage = [[TabrisImage alloc] initWithTabrisContext:self.context propertiesObject:image];
    [tabrisImage getImage:^(UIImage *uiImage, NSError *error) {
        view.image = error ? nil : uiImage;
        tabrisImage = nil;
    }];
}

- (void)refreshMarker:(ESMarker *)marker {
    [self.map removeAnnotation:marker];
    [self.map addAnnotation:marker];
}

- (void)destroy {
    _map.showsUserLocation = NO;
    for (ESMarker *marker in self.map.annotations) {
        [marker destroy];
    }
    [super destroy];
}

@end
