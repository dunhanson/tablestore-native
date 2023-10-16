package com.bidizhaobiao.tablestore.nat.utils;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.PageUtil;
import cn.hutool.json.JSONUtil;
import com.alicloud.openservices.tablestore.ClientConfiguration;
import com.alicloud.openservices.tablestore.SyncClient;
import com.alicloud.openservices.tablestore.model.*;
import com.alicloud.openservices.tablestore.model.search.SearchQuery;
import com.alicloud.openservices.tablestore.model.search.SearchRequest;
import com.alicloud.openservices.tablestore.model.search.SearchResponse;
import com.alicloud.openservices.tablestore.model.search.query.Query;
import com.alicloud.openservices.tablestore.model.search.query.QueryBuilders;
import com.alicloud.openservices.tablestore.model.search.sort.FieldSort;
import com.alicloud.openservices.tablestore.model.search.sort.SortOrder;
import com.bidizhaobiao.tablestore.nat.entity.*;
import com.bidizhaobiao.tablestore.nat.entity.Condition;
import com.bidizhaobiao.tablestore.nat.exception.TablestoreException;
import com.bidizhaobiao.tablestore.nat.entity.Config;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * tablestore工具类
 * @author dunhanson
 * @since 2023-06-13
 * @version 1.0.0
 */
@Slf4j
public class TablestoreUtils {
    /**
     * 实例名称&SyncClient Map集合
     */
    private static final Map<String, SyncClient> clientMap = new LinkedHashMap<>();
    /**
     * 实例名称和Config Map集合
     */
    private static final Map<String, Config> configMap = new LinkedHashMap<>();
    /**
     * 文件名称
     */
    private static final String FILE_NAME = "tablestore.yml";
    /**
     * YAML前缀
     */
    private static final String YAML_PREFIX = "tablestore";

    private TablestoreUtils() {

    }

    static {
        init();
    }


    /**
     * 获取Config集合配置
     * @param inputStream InputStream
     * @return List<Config>
     */
    public static List<Config> getConfigs(InputStream inputStream) {
        try {
            if(Objects.isNull(inputStream)) {
                return new ArrayList<>();
            }
            Yaml yaml = new Yaml();
            Object object = yaml.load(inputStream);
            String jsonStr = JSONUtil.toJsonStr(object);
            jsonStr = JSONUtil.parseObj(jsonStr).getJSONArray(YAML_PREFIX).toString();
            return JSONUtil.toList(jsonStr, Config.class);
        } finally {
            IoUtil.close(inputStream);
        }
    }

    /**
     * 获取Config集合配置
     * @param path 路径
     * @return List<Config>
     */
    public static List<Config> getConfigs(String path) {
        try {
            FileInputStream inputStream = new FileInputStream(path);
            return getConfigs(inputStream);
        } catch (FileNotFoundException e) {
            throw new TablestoreException(e);
        }
    }

    /**
     * 进行初始化操作
     * @param configs 配置集合
     */
    public static void init(List<Config> configs) {
        if(CollectionUtils.isEmpty(configs)) {
            return;
        }
        for (Config config : configs) {
            SyncClient client = getSyncClient(config);
            clientMap.put(config.getInstanceName(), client);
            configMap.put(config.getInstanceName(), config);
        }
    }

    /**
     * 初始化通过yml文件
     * @param path 路径
     */
    public static void init(String path) {
        clientMap.clear();
        configMap.clear();
        List<Config> configs = getConfigs(path);
        init(configs);
    }

    /**
     * 初始化通过InputStream
     * @param inputStream InputStream
     */
    public static void init(InputStream inputStream) {
        clientMap.clear();
        configMap.clear();
        List<Config> configs = getConfigs(inputStream);
        init(configs);
    }

    /**
     * 默认初始化
     */
    public static void init() {
        InputStream inputStream = Config.class.getClassLoader().getResourceAsStream(FILE_NAME);
        init(getConfigs(inputStream));
    }

    /**
     * 获取OtsConfig
     * @param instanceName 实例名称
     * @return OtsConfig
     */
    public static Config getOtsConfig(String instanceName) {
        return configMap.get(instanceName);
    }

    /**
     * 获取Client
     * @param config OtsConfig
     * @return SyncClient
     */
    private static SyncClient getSyncClient(Config config) {
        // setting
        String endPoint = config.getEndPoint();
        String accessKeyId = config.getAccessKeyId();
        String accessKeySecret = config.getAccessKeySecret();
        String instanceName = config.getInstanceName();
        Integer connectionTimeoutInMillisecond = config.getConnectionTimeoutInMillisecond();
        ClientConfiguration configuration = getClientConfiguration(config, connectionTimeoutInMillisecond);
        return new SyncClient(endPoint, accessKeyId, accessKeySecret, instanceName, configuration);
    }

    /**
     * 获取 ClientConfiguration
     * @param config OtsConfig
     * @param connectionTimeoutInMillisecond 连接超时
     * @return ClientConfiguration
     */
    private static ClientConfiguration getClientConfiguration(Config config, Integer connectionTimeoutInMillisecond) {
        Integer socketTimeoutInMillisecond = config.getSocketTimeoutInMillisecond();
        Long syncClientWaitFutureTimeoutInMillis = config.getSyncClientWaitFutureTimeoutInMillis();
        // configuration
        ClientConfiguration configuration = new ClientConfiguration();
        configuration.setConnectionTimeoutInMillisecond(connectionTimeoutInMillisecond);
        configuration.setSocketTimeoutInMillisecond(socketTimeoutInMillisecond);
        configuration.setSyncClientWaitFutureTimeoutInMillis(syncClientWaitFutureTimeoutInMillis);
        configuration.setRetryStrategy(new AlwaysRetryStrategy());
        return configuration;
    }

    /**
     * 分页查询
     * @param condition 查询条件
     * @return 分页结果
     */
    public static Page<Map<String,Object>> page(com.bidizhaobiao.tablestore.nat.entity.Condition condition) {
        // 1、获取参数
        String instanceName = condition.getInstanceName();
        Query query = condition.getQuery();
        String indexName = condition.getIndexName();
        SearchRequest.ColumnsToGet columnsToGet = TablestoreUtils.covertColumns(condition.getColumns());
        com.alicloud.openservices.tablestore.model.search.sort.Sort sort = TablestoreUtils.covertSorts(condition.getSorts());
        String tableName = condition.getTableName();
        Boolean getAll = condition.getAllRecords();
        int pageNo = condition.getPageNo() == null ? 1 : condition.getPageNo();
        int pageSize = condition.getPageSize() == null ? 30 : condition.getPageSize();
        int offset = PageUtil.getStart(pageNo - 1, pageSize);
        // 实例判断
        if(StringUtils.isBlank(indexName)) {
            instanceName = clientMap.keySet().stream().findFirst().orElse(null);
        }
        //
        Config config = getOtsConfig(instanceName);
        // 2、执行搜索
        SearchQuery searchQuery = new SearchQuery();
        if(getAll == null || Boolean.FALSE.equals(getAll)) {
            searchQuery.setLimit(pageSize);
            searchQuery.setOffset(offset);
        } else {
            searchQuery.setLimit(100);
        }
        searchQuery.setSort(sort);
        searchQuery.setQuery(query);
        searchQuery.setGetTotalCount(true);
        // SearchRequest
        SearchRequest searchRequest = new SearchRequest(tableName, indexName, searchQuery);
        searchRequest.setColumnsToGet(columnsToGet);
        searchRequest.setTimeoutInMillisecond(config.getTimeoutInMillisecond());
        log.debug("timeoutInMillisecond:{}", searchRequest.getTimeoutInMillisecond());
        // SyncClient
        SyncClient client = clientMap.get(instanceName);
        SearchResponse response = client.search(searchRequest);
        log.debug("getAll:{}, totalCount:{}", getAll, response.getTotalCount());
        // 3、查询结果
        int totalCount = (int) response.getTotalCount();
        List<Row> rows = response.getRows();
        // 4、TOKEN翻页
        while (Boolean.TRUE.equals(getAll) && response.getNextToken() != null) {
            byte[] nextToken = response.getNextToken();
            searchRequest.getSearchQuery().setToken(nextToken);
            response = client.search(searchRequest);
            rows.addAll(response.getRows());
        }
        List<Map<String, Object>> data = CollectionUtils.isEmpty(rows) ? new ArrayList<>() :
                rows.stream().map(TablestoreUtils::toMap).collect(Collectors.toList());
        return new Page<>(pageNo, pageSize, totalCount, data);
    }

    /**
     * 列表查询
     * @param condition 查询条件
     * @return 结果集合
     */
    public static List<Map<String, Object>> selectlist(com.bidizhaobiao.tablestore.nat.entity.Condition condition) {
        Page<Map<String, Object>> page = page(condition);
        List<Map<String, Object>> data = page.getData();
        return CollectionUtils.isNotEmpty(data) ? data : new ArrayList<>();
    }

    /**
     * 获取主键集合
     * @param condition 查询条件
     * @return 主键集合
     */
    public static List<Object> selectPrimaryKeyList(Condition condition) {
        log.debug("condition:{}", JSONUtil.toJsonStr(condition));
        condition.setColumns(Collections.singletonList(condition.getPrimaryKey()));
        List<Map<String, Object>> mapList = selectlist(condition);
        if(CollectionUtils.isEmpty(mapList)) {
            return new ArrayList<>();
        }
        log.debug("mapList size:{}", mapList.size());
        return mapList.stream().map(map -> map.get(condition.getPrimaryKey())).collect(Collectors.toList());
    }

    /**
     * 单个结果查询
     * @param condition 查询条件
     * @return 单个结果
     */
    public static Map<String, Object> selectOne(Condition condition) {
        Page<Map<String, Object>> page = page(condition);
        List<Map<String, Object>> data = page.getData();
        return CollectionUtils.isNotEmpty(data) ? data.get(0) : new HashMap<>();
    }

    /**
     * Row转换成Map
     * @param row Row
     * @return Map
     */
    public static Map<String, Object> toMap(Row row) {
        Map<String, Object> map = new HashMap<>();
        // primaryKey
        PrimaryKey primaryKey = row.getPrimaryKey();
        for(PrimaryKeyColumn primaryKeyColumn : primaryKey.getPrimaryKeyColumns()) {
            try {
                map.put(primaryKeyColumn.getName(), primaryKeyColumn.getValue().toColumnValue().getValue());
            } catch (IOException e) {
                throw new TablestoreException("toMap fail: " + e.getMessage());
            }
        }
        // columns
        for(Column column : row.getColumns()) {
            String name = column.getName();
            Object value = null;
            if(Objects.nonNull(column.getValue())) {
                value = column.getValue().getValue();
            }
            map.put(name, value);
        }
        return map;
    }

    /**
     * rows转换成maps
     * @param rows row集合
     * @return map集合
     */
    public static List<Map<String, Object>> toMaps(List<Row> rows) {
        if(CollectionUtils.isEmpty(rows)) {
            return new ArrayList<>();
        }
        return rows.stream().map(TablestoreUtils::toMap).collect(Collectors.toList());
    }

    /**
     * 字段集合转换成
     * @param columns SearchRequest.ColumnsToGet
     * @return SearchRequest.ColumnsToGet
     */
    public static SearchRequest.ColumnsToGet covertColumns(List<String> columns) {
        SearchRequest.ColumnsToGet columnsToGet = new SearchRequest.ColumnsToGet();
        columnsToGet.setColumns(columns);
        return columnsToGet;
    }

    /**
     * 排序类型转换
     * @param sorts 排序集合
     * @return Sort
     */
    public static com.alicloud.openservices.tablestore.model.search.sort.Sort covertSorts(List<Sort> sorts) {
        if(CollectionUtils.isEmpty(sorts)) {
            return null;
        }
        List<com.alicloud.openservices.tablestore.model.search.sort.Sort.Sorter> sortList = sorts.stream().map(sort -> {
            SortOrder sortOrder;
            if (sort.getOrder() == Order.DESC) {
                sortOrder = SortOrder.DESC;
            } else {
                sortOrder = SortOrder.ASC;
            }
            return new FieldSort(sort.getField(), sortOrder);
        }).collect(Collectors.toList());
        return new com.alicloud.openservices.tablestore.model.search.sort.Sort(sortList);
    }

    /**
     * 创建主键查询
     * @param keyName 主键名称
     * @param keyValue 主键值
     * @return 主键查询
     */
    public static Query createPrimaryKeyQuery(String keyName, Object keyValue) {
        return QueryBuilders.term(keyName, keyValue).build();
    }
}
