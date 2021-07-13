package com.openfaas.function;

import com.digitaldots.function.BeverFunctionHandler;
import com.openfaas.model.IRequest;
import com.openfaas.model.IResponse;

public class Handler extends com.openfaas.model.AbstractHandler{
    
    public IResponse Handle(IRequest request) {
        BeverFunctionHandler beverHandler = new BeverFunctionHandler();
        return beverHandler.Handle(request);
    }
}

