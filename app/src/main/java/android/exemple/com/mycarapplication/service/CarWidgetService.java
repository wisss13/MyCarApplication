package android.exemple.com.mycarapplication.service;

import android.content.Intent;
import android.exemple.com.mycarapplication.adapters.CarWidgetFactoryAdapter;
import android.widget.RemoteViewsService;

/**
 * Created by lenovo on 16/01/2016.
 */
public class CarWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CarWidgetFactoryAdapter(this, intent);
    }
}
