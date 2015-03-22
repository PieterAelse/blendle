package com.piotapps.blendle.interfaces;

import com.piotapps.blendle.pojo.PopularItems;

public interface AsynCallback {

    void started();
    void progress();
    void ended(PopularItems pi);
}
