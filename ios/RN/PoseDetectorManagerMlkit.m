#import "PoseDetectorManagerMlkit.h"
#import <React/RCTConvert.h>
#if __has_include(<MLKitPoseDetection/MLKitPoseDetection.h>)

@interface PoseDetectorManagerMlkit ()
@property(nonatomic, strong) MLKPoseDetector *poseDetector;
@property(nonatomic, strong) MLKPoseDetectorOptions *options;
@property(nonatomic, assign) float scaleX;
@property(nonatomic, assign) float scaleY;
@end

@implementation PoseDetectorManagerMlkit

- (instancetype)init 
{
  if (self = [super init]) {
    self.options = [[MLKPoseDetectorOptions alloc] init];

    self.poseDetector = [MLKPoseDetector poseDetectorWithOptions:_options];
  }
  return self;
}

- (BOOL)isRealDetector 
{
  return true;
}

- (void)findLabelsInFrame:(UIImage *)uiImage
                  scaleX:(float)scaleX
                  scaleY:(float)scaleY
               completed:(void (^)(NSArray *result))completed 
{
    self.scaleX = scaleX;
    self.scaleY = scaleY;
    MLKVisionImage *image = [[MLKVisionImage alloc] initWithImage:uiImage];
    NSMutableArray *emptyResult = [[NSMutableArray alloc] init];
    [_poseDetector
     processImage:image
     completion:^(NSArray<MLKPose *> *poses, NSError *error) {
         if (error != nil || poses == nil) {
             completed(emptyResult);
         } else {
             completed([self processPoses:poses]);
         }
     }];
}

- (NSDictionary *)processPoses:(NSArray *)poses 
{
    NSMutableArray *result = [[NSMutableDictionary alloc] init];
    for (MLKPose *pose in poses) {
        // TODO - do this for all landmarks
        MLKPoseLandmark *landmark =
            [pose landmarkOfType:MLKPoseLandmarkTypeLeftShoulder];

        MLKVisionPoint *position = leftAnkleLandmark.position;

        NSMutableDictionary *resultMap =
            [[NSMutableDictionary alloc] initWithCapacity:3];
        [resultMap setObject:landmark.inFrameLikelihood forKey:@"inFrameLikelihood"];
        [resultMap setObject:landmark.y forKey:@"x"];
        [resultMap setObject:landmark.y forKey:@"y"];
        [result setObject:resultMap forKey:@"leftShoulder"];
    }
    return result;
}

@end
#else

@interface PoseDetectorManagerMlkit ()
@end

@implementation PoseDetectorManagerMlkit

- (instancetype)init {
 self = [super init];
 return self;
}

- (BOOL)isRealDetector {
 return false;
}

- (NSArray *)findPoseInFrame:(UIImage *)image
                    scaleX:(float)scaleX
                    scaleY:(float)scaleY
                    completed:(void (^)(NSArray *result))completed;
{
 NSLog(@"PoseDetector not installed, stub used!");
 NSArray *features = @[ @"Error, Pose Detector not installed" ];
 return features;
}

@end
#endif
