//
//  ESMap.h
//  TabrisMapsExample
//
//  Created by Patryk Mol on 30/06/16.
//
//

#import <Tabris/BasicWidget.h>

@class ESMarker;

@interface ESMap : BasicWidget
@property (assign) BOOL tapListener;
@property (assign) BOOL longpressListener;
@property (assign) BOOL readyListener;
@property (assign) BOOL cameraMovedListener;
@property (assign) BOOL changecameraListener;
@property (assign) BOOL showMyLocation;
@property (strong) NSString *mapType;
@property (strong) NSArray *position;
@property (strong) NSDictionary *camera;
@property (readonly, strong) NSArray *myLocation;
@property (strong) NSDictionary *region;
- (void)addMarker:(NSDictionary *)properties;
- (void)removeMarker:(NSDictionary *)properties;
- (void)moveToRegion: (NSDictionary *)properties;
- (void)refreshMarker:(ESMarker *)marker;
@end
