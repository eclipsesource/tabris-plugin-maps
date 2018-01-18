//
//  ESMarker.h
//  TabrisMapsExample
//
//  Created by Patryk Mol on 30/06/16.
//
//

#import "ESMap.h"
#import <Tabris/BasicWidget.h>
#import <MapKit/MapKit.h>

@interface ESMarker : BasicWidget <MKAnnotation>
@property (weak) ESMap *map;
@property (strong) NSArray *position;
@property (strong) NSArray *image;
@property (assign) BOOL tapListener;
- (void)tapped;
@end
