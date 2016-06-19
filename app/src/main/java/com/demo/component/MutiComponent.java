package com.demo.component;

import android.view.LayoutInflater;
import android.view.View;
import com.blog.www.guideview.Component;

/**
 * Created by binIoter on 16/6/17.
 */
public class MutiComponent extends SimpleComponent {

    @Override
    public View getView(LayoutInflater inflater) {
        return super.getView(inflater);
    }

    @Override
    public int getAnchor() {
        return Component.ANCHOR_TOP;
    }

    @Override
    public int getFitPosition() {
        return Component.FIT_START;
    }

    @Override
    public int getXOffset() {
        return -60;
    }

    @Override
    public int getYOffset() {
        return 0;
    }
}
