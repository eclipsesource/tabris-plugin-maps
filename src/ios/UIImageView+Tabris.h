@class TabrisClient;

@interface UIImageView (Tabris)

- (void)setImageWithProperties:(NSArray *)imageProperties
                        client:(TabrisClient *)client
                       success:(void (^)(NSURLRequest *request, NSHTTPURLResponse *response, UIImage *image))success
                       failure:(void (^)(NSURLRequest *request, NSHTTPURLResponse *response, NSError *error))failure;

@end
