
#import <UIKit/UIKit.h>
@import MLKit;

@interface LabelDetectorManagerMlkit : NSObject
typedef void(^postRecognitionBlock)(NSArray *labels);

- (instancetype)init;

-(BOOL)isRealDetector;
-(void)findLabelsInFrame:(UIImage *)image scaleX:(float)scaleX scaleY:(float)scaleY completed:(postRecognitionBlock)completed;

@end 
