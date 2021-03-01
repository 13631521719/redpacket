package com.dy.game.rpservice.service;

import com.dy.game.rpcommon.param.GrabRedPagketParam;
import com.dy.game.rpcommon.param.SendRedPagketParam;
import com.dy.game.rpcommon.response.WebResponse;

public interface RedPagketService {
    WebResponse sendRedPagket(SendRedPagketParam sendRedPagketParam)throws Exception;

    WebResponse gradRedPagket(GrabRedPagketParam grabRedPagketParam)throws Exception;
}
