package com.htwz.configuration;

import com.htwz.constant.AquariusConstant;
import com.nepxion.banner.BannerConstant;
import com.nepxion.banner.Description;
import com.nepxion.banner.LogoBanner;
import com.nepxion.banner.NepxionBanner;
import com.taobao.text.Color;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static com.htwz.constant.AquariusConstant.GITHUB_ADDRESS;

/**
 * banner配置
 *
 * @author lxs
 */
@Configuration
@ComponentScan(basePackages = {"com.htwz.context"})
public class AquariusConfiguration {

    static {
        LogoBanner logoBanner = new LogoBanner(AquariusConfiguration.class, "/log/logo.txt", "Welcome to distributed lock", 8, 5,
                new Color[]{Color.red, Color.green, Color.cyan, Color.blue, Color.yellow, Color.magenta, Color.red, Color.green}, true);

        NepxionBanner.show(logoBanner, new Description(BannerConstant.VERSION + ":",
                        AquariusConstant.AQUARIUS_VERSION, 0, 1),
                new Description(BannerConstant.GITHUB + ":", GITHUB_ADDRESS + "/distributed-lock",
                        0, 1));
    }
}