#import "ObjectDetectorManagerMlkit.h"
#import <React/RCTConvert.h>
#if __has_include(<MLKObjectDetector/MLKObjectDetector.h>)

@interface ObjectDetectorManagerMlkit ()
@property(nonatomic, strong) MLKObjectDetector *objectDetector;
@property(nonatomic, strong) MLKObjectDetectorOptions *options;
@property(nonatomic, assign) float scaleX;
@property(nonatomic, assign) float scaleY;
@end

@implementation ObjectDetectorManagerMlkit

- (instancetype)init 
{
  if (self = [super init]) {
    self.options = [[MLKObjectDetectorOptions alloc] init];
    self.objectDetector = [MLKObjectDetector objectDetectorWithOptions:_options];
  }
  return self;
}

- (BOOL)isRealDetector 
{
  return true;
}

- (void)findObjectsInFrame:(UIImage *)uiImage
                  scaleX:(float)scaleX
                  scaleY:(float)scaleY
               completed:(void (^)(NSArray *result))completed 
{
    self.scaleX = scaleX;
    self.scaleY = scaleY;
    MLKVisionImage *image = [[MLKVisionImage alloc] initWithImage:uiImage];
    NSMutableArray *emptyResult = [[NSMutableArray alloc] init];
    [_objectDetector
     processImage:image
     completion:^(NSArray<MLKObject *> *objects, NSError *error) {
         if (error != nil || objects == nil) {
             completed(emptyResult);
         } else {
             completed([self processObjects:objects]);
         }
     }];
}

- (NSArray *)processObjects:(NSArray *)objects 
{
    NSMutableArray *result = [[NSMutableArray alloc] init];
    for (MLKObject *obj in objects) {
        NSMutableDictionary *resultDict =
        [[NSMutableDictionary alloc] initWithCapacity:2];
        [resultDict setObject:obj.frame forKey:@"frame"];
        [resultDict setObject:obj.trackingID forKey:@"trackingID"];
        [resultDict setArray:obj.labels forKey:@"labels"];
        [result addObject:resultDict];
    }
    return result;
}

@end
#else

@interface ObjectDetectorManagerMlkit ()
@end

@implementation ObjectDetectorManagerMlkit

- (instancetype)init {
 self = [super init];
 return self;
}

- (BOOL)isRealDetector {
 return false;
}

- (NSArray *)findObjectsInFrame:(UIImage *)image
                    scaleX:(float)scaleX
                    scaleY:(float)scaleY
                    completed:(void (^)(NSArray *result))completed;
{
 NSLog(@"ObjectDetector not installed, stub used!");
 NSArray *features = @[ @"Error, Object Detector not installed" ];
 return features;
}

@end
#endif
