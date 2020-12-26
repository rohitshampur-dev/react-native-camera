
#import <UIKit/UIKit.h>
#if __has_include(<MLKitPoseDetection/MLKitPoseDetection.h>)
  #import <MLKitVision/MLKitVision.h>
  #import <MLKitPoseDetection/MLKitPoseDetection.h>
#endif

@interface PoseDetectorManagerMlkit : NSObject
typedef void(^postRecognitionBlock)(NSDictionary *pose);

- (instancetype)init;

-(BOOL)isRealDetector;
-(void)findPoseInFrame:(UIImage *)image scaleX:(float)scaleX scaleY:(float)scaleY completed:(postRecognitionBlock)completed;

@end 
