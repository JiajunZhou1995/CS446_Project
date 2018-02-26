package com.mtr.codetrip.codetrip.helper;

/**
 * Created by Catrina on 26/02/2018.
 */

public class ServerAPIResponse implements AsyncResponse {

    private String myOutPut;

    public ServerAPIResponse(String myInput){
        HttpPostAsyncTask request = new HttpPostAsyncTask(myInput);
        request.delegate = this;
        request.execute();
    }
    @Override
    public void processFinish(String output) {
        myOutPut = output;
    }

    public String getResult(){
        return myOutPut;
    }
}
