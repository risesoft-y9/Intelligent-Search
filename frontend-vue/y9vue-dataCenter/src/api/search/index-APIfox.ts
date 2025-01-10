import Request from '@/api/lib/request';
import qs from 'qs';
const searchRequest = Request();

/**
 * 智能检索的可选择文章类型的数据
 * @returns
 */
export const getSearchArticleType = async () => {
    return await searchRequest({
        url: 'https://apifoxmock.com/m2/2209901-946370-default/241805294?apifoxApiId=241805294',
        method: 'GET',
        cType: false,
    });
};

/**
 * 智能检索的搜索列表
 * @param {*} params
 * @returns
 */
export const getSearchIndexList = async (params) => {
    return await searchRequest({
        url: 'https://apifoxmock.com/m2/2209901-946370-default/241808566?apifoxApiId=241808566',
        method: 'get',
        cType: false,
        params,
    });
};

/**
 *  内页的文档报告
 * @param {*} params
 * @returns
 */
export const openFile = async (params) => {
    return await searchRequest({
        url: 'https://apifoxmock.com/m2/2209901-946370-default/243100425?apifoxApiId=243100425',
        method: 'GET',
        cType: false,
        params,
    });
};

/***
 *
 * 搜索相关的热搜 推荐列表
 * @param {*} keyword
 * @returns
 */
export const hotSearchDataList = async (id, keyword, type) => {
    return await searchRequest({
        url: 'https://apifoxmock.com/m2/2209901-946370-default/243261974?apifoxApiId=243261974',
        method: 'GET',
        cType: false,
        params: { id: id, keyword: keyword?.replaceAll('[', '')?.replaceAll(']', ''), dataType: type },
    });
};

/***
 *
 * 搜索标题(搜索框下弹出来的,类似于百度)
 * @param {*} keyword
 * @returns
 */
export const searchHistoryList = async (params) => {
    return await searchRequest({
        url: 'https://apifoxmock.com/m2/2209901-946370-default/243258077?apifoxApiId=243258077',
        method: 'GET',
        cType: false,
        params: params,
    });
};

/***
 *
 * 推荐文件
 * @param {*} keyword
 * @returns
 */
export const getHotFileList = async () => {
    return await searchRequest({
        url: 'https://mock.apifox.cn/m1/2209901-0-default/datacenter/rest/officeInfo/getHot',
        method: 'GET',
        cType: false,
    });
};

/***
 * 下载记录
 */
export const downloadLog = async (id, url, fileName) => {
    return await searchRequest({
        url: 'https://apifoxmock.com/m1/2209901-946370-default/datacenter/rest/officeInfo/downloadLog?apifoxApiId=243262949',
        method: 'post',
        cType: false,
        params: { id: id, url: url, fileName: fileName },
    });
};
