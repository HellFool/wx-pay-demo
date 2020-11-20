package com.zyx.controller;


import com.github.wxpay.sdk.WXPayUtil;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.zyx.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;


/**
 * @author zyx
 */
@RestController
@RequestMapping(value = "/api/pay")
@Slf4j
@Api(tags = {"支付"})
public class PayController {
    @Autowired
    private HttpServletRequest request;

    /**
     * 微信支付
     *
     * @return
     */
    @PostMapping(path = "/wxpay")
    @ApiOperation(value = "微信支付")
    public void wxPay(@RequestParam(value = "activePrice", required = false) Double activePrice, HttpServletResponse response) throws Exception {

        String ordersId = WXPayUtil.generateNonceStr();

        Map<String, String> paraMap = new HashMap<String, String>(16);
        String ip = request.getHeader("x-forwarded-for");
        String unknown = "unknown";
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || unknown.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String dh = ",";
        if (ip.indexOf(dh) != -1) {
            String[] ips = ip.split(",");
            ip = ips[0].trim();
        }

        String nonceStr = WXPayUtil.generateNonceStr();

        //支付金额为分
        Double a=activePrice * 100;
        Integer price=Integer.valueOf(a.intValue());

        paraMap.put("appid", AuthUtil.APPID);
        paraMap.put("body", "订单");
        paraMap.put("mch_id", AuthUtil.MCHID);
        paraMap.put("nonce_str", nonceStr);
        paraMap.put("out_trade_no", ordersId);
        paraMap.put("spbill_create_ip", ip);
        paraMap.put("total_fee", price+"");
        paraMap.put("trade_type", "NATIVE");
        //回调地址
        paraMap.put("notify_url", "http");
        String sign = WXPayUtil.generateSignature(paraMap, AuthUtil.PATERNERKEY);
        paraMap.put("sign", sign);
        // 将所有参数(map)转xml格式
        String xml = WXPayUtil.mapToXml(paraMap);

        String unifiedorderUrl = "https://api.mch.weixin.qq.com/pay/unifiedorder";
        String xmlStr = HttpRequest.httpsRequest(unifiedorderUrl, "POST", xml);

        String success = "SUCCESS";
        if (xmlStr.indexOf(success) != -1) {
            Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
            String codeUrl=map.get("code_url");
            // 设置响应流信息
            response.setContentType("image/jpg");
            response.setHeader("Pragma", "no-cache");
            response.setHeader("Cache-Control", "no-cache");
            response.setDateHeader("Expires", 0);

            OutputStream stream = response.getOutputStream();
            //获取一个二维码图片
            BitMatrix bitMatrix = QRCodeUtils.createCode(codeUrl);
            //以流的形式输出到前端
            MatrixToImageWriter.writeToStream(bitMatrix , "jpg" , stream);
        }

    }


}
