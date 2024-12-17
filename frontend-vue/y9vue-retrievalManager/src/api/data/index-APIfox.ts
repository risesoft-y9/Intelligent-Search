import Request from '@/api/lib/request';
import qs from 'qs';
const dataRequest = Request();

/**
 * 列表
 * @param {*} params
 * @returns
 */
export const getDataList = async (params) => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: 'https://mock.apifox.cn/m2/2209901-0-default/81034306',
        method: 'GET',
        cType: false,
        params,
    });
};

/***
 * 是否公开切换
 * @param {*} params
 * @returns
 */
export const openChange = async (params) => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: 'https://apifoxmock.com/m2/2209901-946370-default/244151475',
        method: 'POST',
        cType: false,
        params,
    });
};

/***
 * 获取附件列表
 * @param {*} params
 * @returns
 */
export const getAnnexList = async (params) => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: 'https://mock.apifox.cn/m2/2209901-0-default/81276472',
        method: 'GET',
        cType: false,
        params,
    });
};

/***
 * 解析正文
 * @param {*} params
 * @returns
 */
export const parseMainText = async (params) => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: 'https://mock.apifox.cn/m2/2209901-0-default/81340419',
        method: 'GET',
        cType: false,
        params,
    });
};

/***
 * 对单个或多个附件解析
 * @param {*} params
 * @returns
 */
export const parseAnnex = async (params) => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: 'https://mock.apifox.cn/m2/2209901-0-default/81371367',
        method: 'GET',
        cType: false,
        params,
    });
};

/***
 * 同步
 * @returns
 */
export const synchronous = async () => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: 'https://mock.apifox.cn/m2/2209901-0-default/81399654',
        method: 'GET',
        cType: false,
    });
};

/***
 * 类型显示开关
 * @returns
 */
export const typeNameSwitch = async (params) => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: 'https://apifoxmock.com/m2/2209901-946370-default/244159501',
        method: 'POST',
        cType: false,
        params,
    });
};

/***
 * 获取数据类型
 * @returns
 */
export const getDataNameList = async (params) => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: 'https://mock.apifox.cn/m2/2209901-0-default/81724279',
        method: 'GET',
        cType: false,
        params,
    });
};

/**
 * 用户操作日志列表1
 */
export const searchUserLogList = async (params) => {
    return await dataRequest({
        url: 'https://apifoxmock.com/m2/2209901-0-default/183942840',
        method: 'GET',
        cType: false,
        params,
    });
};

/**
 * 检索词管理
 */

// 获取检索词列表
export const relatedWordList = async (params) => {
    return await dataRequest({
        url: 'https://mock.apipark.cn/m1/2209901-0-default/rest/backstage/searchFrequency',
        method: 'GET',
        cType: false,
        params,
    });
};

// 删除检索词
export const deleterelatedWordByIds = async (params) => {
    return await dataRequest({
        url: 'https://apifoxmock.com/m2/2209901-946370-default/243652467',
        method: 'post',
        cType: false,
        params,
    });
};

// 启用/禁用
export const enOrDisabledlatedWord = async (params) => {
    return await dataRequest({
        url: 'https://apifoxmock.com/m2/2209901-946370-default/243653240',
        method: 'POST',
        cType: false,
        params,
    });
};

// 导出
// export const exportRelatedWord = async () => {
//     return await dataRequest({
//         url: 'https://mock.apipark.cn/m1/2209901-0-default/datacenter/rest/backstage/exportFrequency',
//         method: 'GET',
//         cType: false,
//     });
// };

/***
 * 结束
 */
