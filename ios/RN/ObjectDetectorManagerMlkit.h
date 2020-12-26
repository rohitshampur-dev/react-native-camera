
#import <UIKit/UIKit.h>
#if __has_include(<MLKObjectDetector/MLKObjectDetector.h>)
  #import <MLKitVision/MLKitVision.h>
  #import <MLKObjectDetector/MLKObjectDetector.h>
#endif

@interface ObjectDetectorManagerMlkit : NSObject
typedef void(^postRecognitionBlock)(NSArray *objects);

- (instancetype)init;

-(BOOL)isRealDetector;
-(void)findObjectsInFrame:(UIImage *)image scaleX:(float)scaleX scaleY:(float)scaleY completed:(postRecognitionBlock)completed;

@end 
