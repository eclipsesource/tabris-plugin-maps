//
//  TabrisImage.h
//  Tabris
//
//  Created by Karol Szafrański on 26.01.18.
//  Copyright © 2018 EclipseSource. All rights reserved.
//

#import <Foundation/Foundation.h>

@class TabrisImage;
@protocol TabrisImageDelegate
@optional
- (CGSize)targetImageSize:(TabrisImage*)tabrisImage;
@end

@class TabrisClient;
@interface TabrisImage : NSObject

@property (weak, nonatomic) NSObject<TabrisImageDelegate>* delegate;
@property (strong, nonatomic) UIImage* loadedImage;

- (id)initWithTabrisClient:(TabrisClient*)tabrisClient propertiesArray:(NSArray*)propertiesArray;
- (void)getImage:(void (^)(UIImage* image, NSError* error))completition;
- (void)cancelGetImageRequest;

@end
