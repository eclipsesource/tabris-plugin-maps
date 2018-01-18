//
//  ESMarker.m
//  TabrisMapsExample
//
//  Created by Patryk Mol on 30/06/16.
//
//

#import "ESMarker.h"
#import "ESMap.h"

@implementation ESMarker

@synthesize position = _position;
@synthesize tapListener = _tapListener;
@synthesize coordinate = _coordinate;
@synthesize title = _title;
@synthesize subtitle = _subtitle;
@synthesize image = _image;
@synthesize map = _map;

- (instancetype)initWithObjectId:(NSString *)objectId properties:(NSDictionary *)properties andClient:(TabrisClient *)client {
    self = [super initWithObjectId:objectId properties:properties andClient:client];
    if (self) {
        NSArray *coordinates = [properties objectForKey:@"position"];
        if (coordinates) {
            self.coordinate = CLLocationCoordinate2DMake([[coordinates objectAtIndex:0] floatValue], [[coordinates objectAtIndex:1] floatValue]);
        }
    }
    return self;
}

- (instancetype)initWithObjectId:(NSString *)objectId andClient:(TabrisClient *)client {
    self = [super initWithObjectId:objectId andClient:client];
    if (self) {
        [self setCoordinate:CLLocationCoordinate2DMake(0, 0)];
    }
    return self;
}

-(void)setImage:(NSArray *)image {
    _image = image;
    if (self.map) {
        [self.map refreshMarker: self];
    }
}

-(NSArray *)image {
    return _image;
}

- (UIView *)view {
    return nil;
}

+ (NSMutableSet *)remoteObjectProperties {
    NSMutableSet *set = [super remoteObjectProperties];
    [set addObject:@"position"];
    return set;
}

+ (NSString *)remoteObjectType {
    return @"com.eclipsesource.maps.Marker";
}

- (void)tapped {
    if (self.tapListener) {
        Message<Notification> *message = [[self notifications] forObject:self];
        [message fireEvent:@"tap" withAttributes:@{}];
    }
}

- (void)setCoordinate:(CLLocationCoordinate2D)newCoordinate {
    _coordinate = newCoordinate;
}


@end
