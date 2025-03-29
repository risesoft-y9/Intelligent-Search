import Request from '@/api/lib/request';
import qs from 'qs';
const dataRequest = Request();

/**
 * 列表1
 * @param {*} params
 * @returns
 */
export const getDataList = async (params) => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: '/rest/dataExtractLog/searchDataExtractLogInfo',
        method: 'GET',
        cType: false,
        params,
    });
};

/***
 * 是否公开切换1
 * @param {*} params
 * @returns
 */
export const openChange = async (params) => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: '/rest/dataExtractLog/publicOrNot',
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
        url: '/rest/dataExtractLog/getAttachmentList',
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
        url: '/rest/dataExtractLog/getfile4zhengwen',
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
        url: '/rest/dataExtractLog/getfile4fujian',
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
        url: '/rest/dataExtractLog/synchronization',
        method: 'GET',
        cType: false,
    });
};

/***
 * 类型显示开关1
 * @returns
 */
export const typeNameSwitch = async (params) => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: '/rest/dataExtractLog/typeSwitch',
        method: 'POST',
        cType: false,
        params,
    });
};

/***
 * 获取数据类型1
 * @returns
 */
export const getDataNameList = async () => {
    // const data = qs.stringify(params);
    return await dataRequest({
        url: '/rest/dataExtractLog/getDataType',
        method: 'GET',
        cType: false,
    });
};

/**
 * 用户操作日志列表
 */
export const searchUserLogList = async (params) => {
    return await dataRequest({
        url: '/rest/userUsageLog/searchUserUsageLog',
        method: 'GET',
        cType: false,
        params,
    });
};

/**
 * 检索词管理1
 */

// 获取检索词列表
export const relatedWordList = async (params) => {
    return await dataRequest({
        url: '/rest/backstage/searchFrequency',
        method: 'GET',
        cType: false,
        params,
    });
};

// 删除检索词1
export const deleterelatedWordByIds = async (params) => {
    return await dataRequest({
        url: '/rest/backstage/delFrequency',
        method: 'POST',
        cType: false,
        params,
    });
};

// 启用/禁用1
export const enOrDisabledlatedWord = async (params) => {
    return await dataRequest({
        url: '/rest/backstage/disabledFrequency',
        method: 'POST',
        cType: false,
        params,
    });
};

// 导出
// export const exportRelatedWord = async () => {
//     return await dataRequest({
//         url: '/rest/backstage/exportFrequency',
//         method: 'GET',
//         cType: false,
//     });
// };

/***
 * 结束
 */
