package com.szantog.brew_e;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class WebsocketWorker extends Worker {

    private Context context;

    public WebsocketWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @NonNull
    @Override
    public Result doWork() {
        if (!Socketing.isConnected) {
            Socketing.start(context);
        }

        return Result.success();
    }
}
