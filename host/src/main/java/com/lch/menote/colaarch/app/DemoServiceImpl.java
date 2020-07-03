package com.lch.menote.colaarch.app;

import com.lch.menote.colaarch.client.DemoService;
import com.lch.menote.colaarch.client.LoginDto;
import com.lch.menote.colaarch.client.LoginResponse;
import com.lch.menote.colaarch.domain.DemoGateway;
import com.lch.menote.colaarch.infrastructure.DemoConverter;

public class DemoServiceImpl implements DemoService {

    private DemoGateway  demoGateway;//inject.

    @Override
    public LoginResponse login(LoginDto v) {
        demoGateway.getUser(DemoConverter.convert(v));
        return null;
    }
}
