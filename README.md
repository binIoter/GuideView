 * <h1>GuideView</h1>
 * 本系统能够快速的为一个Activity里的任何一个View控件创建一个遮罩式的导航页，并且可以再高亮区域绘制任何你想要的布局或者lottie动画等炫酷效果</p>
 * <h3>工作原理</h3>
 * 首先它需要一个目标View或者它的id,我们通过findViewById来得到这个View，计算它在屏幕上的区域targetRect,通过这个区域，开始绘制一个覆盖整个Activity的遮罩，可以定义遮罩的颜色和透明度，然而目标View被绘制成透明从而实现高亮的效果。接下来是在相对于这个targetRect的区域绘制一些图片或者文字。我们把这样一张图片或者文字抽象成一个Component接口，设置文字或者图片，所有的图片文字都是相对于targetRect来定义的。可以设定额外的x，y偏移量,可以对遮罩系统设置可见状态的发生变化时的监听回调，可以对遮罩系统设置开始和结束时的动画效。</p>
 * <h3>注意：具体用法参见demo，内附详细注释</h3>
 * <img src = "https://github.com/binIoter/GuideView/blob/master/app/src/main/assets/guide.gif"></img>

 * <h3>使用方法</h3>
 *  <h4>1.添加gradle依赖</h4>
        implementation 'com.binioter:guideview:1.0.0'
        
 *  <h4>1.编写用于在高亮区域周围展示的component</h4>
 *  
        public class SimpleComponent implements Component {
            
              @Override public View getView(LayoutInflater inflater) {
            
                LinearLayout ll = (LinearLayout) inflater.inflate(R.layout.layer_frends, null);
                ll.setOnClickListener(new View.OnClickListener() {
                  @Override public void onClick(View view) {
                    Toast.makeText(view.getContext(), "引导层被点击了", Toast.LENGTH_SHORT).show();
                  }
                });
                return ll;
              }
            
              @Override public int getAnchor() {
                return Component.ANCHOR_BOTTOM;
              }
            
              @Override public int getFitPosition() {
                return Component.FIT_END;
              }
            
              @Override public int getXOffset() {
                return 0;
              }
            
              @Override public int getYOffset() {
                return 10;
          }
        }
        
*  <h4>2.展示引导蒙层，并监听蒙层展示、隐藏事件</h4>
*  
         public void showGuideView() {
            GuideBuilder builder = new GuideBuilder();
            builder.setTargetView(header_imgbtn)
                    .setAlpha(150)
                    .setHighTargetCorner(20)
                    .setHighTargetPadding(10);
            builder.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
              @Override
              public void onShown() {
              }
        
              @Override
              public void onDismiss() {
                showGuideView2();
              }
            });
        
            builder.addComponent(new SimpleComponent());
            guide = builder.createGuide();
            guide.show(SimpleGuideViewActivity.this);


## License

        Copyright 2016 binIoter
    
        Licensed under the Apache License, Version 2.0 (the "License");
        you may not use this file except in compliance with the License.
        You may obtain a copy of the License at
    
           http://www.apache.org/licenses/LICENSE-2.0
    
        Unless required by applicable law or agreed to in writing, software
        distributed under the License is distributed on an "AS IS" BASIS,
        WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
        See the License for the specific language governing permissions and
        limitations under the License.
