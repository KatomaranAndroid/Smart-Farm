package com.github.bkhezry.demomapdrawingtools.buy;


import com.github.bkhezry.demomapdrawingtools.utils.FarmNew;

import java.util.List;

public class Snap {

    private int mGravity;
    private String mText;
    private List<FarmNew> mApps;

    public Snap(int gravity, String text, List<FarmNew> apps) {
        mGravity = gravity;
        mText = text;
        mApps = apps;
    }

    public String getText(){
        return mText;
    }

    public int getGravity(){
        return mGravity;
    }

    public List<FarmNew> getApps(){
        return mApps;
    }

}
