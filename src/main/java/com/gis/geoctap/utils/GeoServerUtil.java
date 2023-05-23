package com.gis.geoctap.utils;


import it.geosolutions.geoserver.rest.GeoServerRESTManager;
import it.geosolutions.geoserver.rest.GeoServerRESTPublisher;
import it.geosolutions.geoserver.rest.GeoServerRESTReader;
import it.geosolutions.geoserver.rest.decoder.RESTDataStore;
import it.geosolutions.geoserver.rest.decoder.RESTLayer;
import it.geosolutions.geoserver.rest.manager.GeoServerRESTStoreManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class GeoServerUtil {
    /**
     * geoserver的用户名
     */
    @Value("${geoserver.username}")
    private String username;
    /**
     * geoserver的密码
     */
    @Value("${geoserver.password}")
    private String password;
    /**
     * geoserver的地址
     */
    @Value("${geoserver.url}")
    private String url;
    /**
     * geoserver的工作空间
     */
    @Value("${geoserver.workspace}")
    private String workspace;

    public GeoServerRESTManager initGeoserverClient(){
        try {
            GeoServerRESTManager manager = new GeoServerRESTManager(new URL(url), username, password);
            log.info("连接geoserver服务器成功");
            return manager;
        } catch (Exception e) {
            e.printStackTrace();
            log.info("连接geoserver服务器失败");
            return null;
        }
    }

    public Map<String, String> createGeoShpLayer(String layerName, File shpFile) {
        GeoServerRESTManager manager = initGeoserverClient();
        GeoServerRESTPublisher publisher = manager.getPublisher();
        try {
            GeoServerRESTReader reader = manager.getReader();
            //  2、判断是否有工作区，没有则创建
            boolean b2 = reader.existsWorkspace(workspace);
            if (!b2) {
                boolean b = publisher.createWorkspace(workspace);
                if (!b) {
                    System.out.println("工作区创建失败");
                    return null;
                }
            }

            //  3、判断是否有数据源，没有则创建
            //  4、发布图层，如果存在就不发布
            //  创建数据源 和 发布图层服务可以一步进行
            RESTLayer layer = reader.getLayer(workspace, layerName);
            if (layer == null) {
                // 进行发布；参数依次为：工作区名称、数据源名称、图层名称、shp文件压缩文件对象、坐标系
                boolean b = publisher.publishShp(workspace , layerName , layerName , shpFile , "EPSG:4326");
                if (!b) {
                    System.out.println("shp图层发布失败");
                    return null;
                }
            }

        } catch (FileNotFoundException e) {
            System.out.println(e);
            return null;
        }
        Map<String, String> result = new HashMap<>();
        result.put("url", url + "/" + workspace + "/wms");
        result.put("workspace", workspace);
        result.put("layer", layerName);
        return result;
    }
}
