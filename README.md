 * <h1>GuideView</h1>
 * 本系统能够快速的为一个Activity里的任何一个View控件创建一个遮罩式的导航页。</p>
 * <h3>工作原理</h3>
 * 首先它需要一个目标View或者它的id,我们通过findViewById来得到这个View，计算它在屏幕上的区域targetRect,通过这个区域，开始绘制一个覆盖整个Activity的遮罩，可以定义遮罩的颜色和透明度，然而目标View被绘制成透明从而实现高亮的效果。接下来是在相对于这个targetRect的区域绘制一些图片或者文字。我们把这样一张图片或者文字抽象成一个Component接口，设置文字或者图片，所有的图片文字都是相对于targetRect来定义的。可以设定额外的x，y偏移量,可以对遮罩系统设置可见状态的发生变化时的监听回调，可以对遮罩系统设置开始和结束时的动画效，另外，我们可以不对整个Activity覆盖遮罩，而是对某一个View覆盖遮罩。</p>
 * <h3>注意：具体用法参见demo，内附详细注释</h3>
 * ![image]( https://github.com/binIoter/GuideView/blob/master/app/src/main/assets/img1.png )
 * ![image]( https://github.com/binIoter/GuideView/blob/master/app/src/main/assets/img2.png )</p>
 * ![image]( https://github.com/binIoter/GuideView/blob/master/app/src/main/assets/img3.png )
 * ![image]( https://github.com/binIoter/GuideView/blob/master/app/src/main/assets/img4.png )</p>
 * ![image]( https://github.com/binIoter/GuideView/blob/master/app/src/main/assets/img5.png )

 * <h3>usage</h3>
        public class SimpleComponent implements Component {

           @Override
           public View getView(LayoutInflater inflater) {
		        LinearLayout ll = new LinearLayout(inflater.getContext());
		        LinearLayout.LayoutParams param =
		                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		        ll.setOrientation(LinearLayout.VERTICAL);
		        ll.setLayoutParams(param);
		        TextView textView = new TextView(inflater.getContext());
		        textView.setText(R.string.welcome);
		        textView.setTextColor(inflater.getContext().getResources().getColor(R.color.color_white));
		        textView.setTextSize(20);
		        ImageView imageView = new ImageView(inflater.getContext());
		        imageView.setImageResource(R.mipmap.arrow);
		        ll.removeAllViews();
		        ll.addView(textView);
		        ll.addView(imageView);
		        ll.setOnClickListener(new View.OnClickListener() {
		            @Override
		            public void onClick(View view) {
		                Toast.makeText(view.getContext(), "引导层被点击了", Toast.LENGTH_SHORT).show();
		
		            }
		        });
		        return ll;
           }
	
	           @Override
	           public int getAnchor() {
	        		return Component.ANCHOR_BOTTOM;
	           }
	
	           @Override
	           public int getFitPosition() {
	        		return Component.FIT_START;
	           }
	
	           @Override
	           public int getXOffset() {
	       		    return 30;
	           }
	
	           @Override
	           public int getYOffset() {
	        		return 0;
	           }
           }

        public void showGuideView() {   
	        final GuideBuilder builder1 = new GuideBuilder();
	        builder1.setTargetView(button1)
	                .setAlpha(150)
	                .setOverlayTarget(true)//设置目标区域是否高亮显示
	                .setOutsideTouchable(false);
	        builder1.setOnVisibilityChangedListener(new GuideBuilder.OnVisibilityChangedListener() {
	            @Override
	            public void onShown() {
	                //  Toast.makeText(MutiGuideViewActivity.this, "show", Toast.LENGTH_SHORT).show();
	                }
	            @Override
	            public void onDismiss() {
	                button2.post(new Runnable() {
	                    @Override
	                    public void run() {
	                        showGuideView2();
	                    }
	                });
	                // Toast.makeText(MutiGuideViewActivity.this, "dismiss", Toast.LENGTH_SHORT).show();
	            }
	        });
	
	        builder1.addComponent(new SimpleComponent());
	        Guide guide = builder1.createGuide();
	        guide.setShouldCheckLocInWindow(false);
	        guide.show(MutiGuideViewActivity.this);
        }
        

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
