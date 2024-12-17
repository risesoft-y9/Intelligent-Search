/*
 * @Author: chensiwen cikl777@163.com
 * @Date: 2024-10-12 15:59:48
 * @LastEditors: chensiwen cikl777@163.com
 * @LastEditTime: 2024-12-16 16:33:33
 * @FilePath: \y9vue-dataCenter\src\store\modules\searchStore.ts
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 */
import { defineStore } from 'pinia';
export const useSearchStore = defineStore('searchStore', {
    state: () => {
        return {
            // 搜索过滤对象
            searchFilterInfo: {
                // 搜索值
                searchContent: window.location.href?.split('wd')[1]?.split('=')[1]
                    ? decodeURIComponent(window.location.href?.split('wd')[1]?.split('=')[1])
                    : null,
                // searchContent: null,
                // 全部类型/* '国务院部门文件', '国务院公报', '中央有关文件', '国防部', '国务院文件', '要闻', '政策解读' */
                dataType: [],
                // 时间过滤
                timeType: '时间不限',
                // 综合排序
                sortStr: '综合排序',
                // 搜索类型
                type: 1,
            },
            showHome: true,
            // 总数据量
            totalNumber: 0,
            // 相关结果
            relationNumber: 0,
        };
    },
    getters: {
        getSearchFilterInfo: (state) => {
            return state.searchFilterInfo;
        },
        getShowHome: (state) => {
            return window.location.href?.split('wd')[1] ? false : state.showHome;
            // return state.showHome;
        },
        getTotalNumber: (state) => state.totalNumber,
        getRelationNumber: (state) => state.relationNumber,
    },
    actions: {
        setTotalNumber(data) {
            this.totalNumber = data;
        },
        setRelationNumber(data) {
            this.relationNumber = data;
        },
    },
});
