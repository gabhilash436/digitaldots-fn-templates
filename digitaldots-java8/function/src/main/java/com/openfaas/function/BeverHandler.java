package com.openfaas.function;

import com.openfaas.model.IRequest;
import com.openfaas.model.IResponse;

public abstract class BeverHandler  extends com.openfaas.model.AbstractHandler{
    public abstract IResponse Handle(IRequest var1);
}
