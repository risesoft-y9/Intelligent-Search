<!---
    列表左侧的搜索条件展示
-->
<template>
    <div class="file-content">
        <div class="content-top">
            <div class="content-title">列表状态</div>
            <div class="form-item">
                <span class="item-title">排序方式</span>
                <span class="item-content">
                    <el-radio-group v-model="sortTextName" size="small" @change="handlerSortChange">
                        <el-radio-button label="默认排序" />
                        <el-radio-button label="热度排序" />
                        <el-radio-button label="时间正序" />
                        <el-radio-button label="时间倒序" />
                    </el-radio-group>
                </span>
            </div>
        </div>
        <div class="content-bottom">
            <div class="button-group">
                <div class="content-title" style="border-bottom: none">条件筛选</div>
                <buttonCom icon-class="ri-search-line" btn-name="搜索" @click="handlerSearch"></buttonCom>
                <buttonCom
                    icon-class="ri-restart-line"
                    btn-name="重置"
                    :primary="false"
                    @click="handlerReset"
                ></buttonCom>
            </div>
            <div style="height: calc(100% - 62px); overflow: scroll">
                <!-- 类型 -->
                <div class="form-item" style="align-items: flex-start">
                    <span class="item-title">类型</span>
                    <div class="item-content">
                        <div class="type-select">
                            <el-checkbox
                                v-model="checkAll"
                                @change="handleCheckAllChange"
                                :indeterminate="isIndeterminate"
                                >全部类型</el-checkbox
                            >
                            <el-checkbox-group
                                v-model="checkedTypes"
                                class="checkbox-group"
                                @change="handleCheckedTypesChange"
                            >
                                <el-checkbox
                                    v-for="item in typesList?.slice(0, sliceNumber)"
                                    :key="item.dataName"
                                    :label="item.dataName"
                                    >{{ item.dataName }}</el-checkbox
                                >
                            </el-checkbox-group>
                            <div class="select-btn" v-if="typesList.length > 6">
                                <div @click="handlerClickReduce('more')" class="btn-style" v-if="sliceNumber == 7"
                                    ><i class="ri-add-box-line"></i>更多</div
                                >
                                <div v-else @click="handlerClickReduce('reduce')" class="btn-style"
                                    ><i class="ri-checkbox-indeterminate-line"></i>收回</div
                                >
                            </div>
                        </div>
                    </div>
                </div>
                <!-- 日期 -->
                <div class="form-item" style="align-items: flex-start">
                    <span class="item-title">日期</span>
                    <div class="item-content">
                        <div style="margin-top: -6px">
                            <div class="groups">
                                <el-radio-group v-model="timeShowText" @change="handlerTimeText">
                                    <el-radio label="时间不限">时间不限</el-radio>
                                    <el-radio label="一年内">一年内</el-radio>
                                    <el-radio label="一月内" style="margin-right: 0px">一月内</el-radio>
                                    <el-radio label="time" style="margin-top: 8px">
                                        <div class="date-select" style="margin-top: 8px">
                                            <div class="select-item" style="width: 112px">
                                                <el-date-picker
                                                    v-model="beginDate"
                                                    type="date"
                                                    placeholder="开始日期"
                                                    value-format="YYYY-MM-DD"
                                                    @change="handlerTimeChange"
                                                />
                                            </div>
                                            <div class="select-item">
                                                <span style="margin-left: 10px">至</span>
                                                <el-date-picker
                                                    v-model="endDate"
                                                    type="date"
                                                    placeholder="结束日期"
                                                    value-format="YYYY-MM-DD"
                                                    @change="handlerTimeChange"
                                                />
                                            </div>
                                        </div>
                                    </el-radio>
                                </el-radio-group>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
    import { ref, onMounted } from 'vue';
    import { useSearchStore } from '@/store/modules/searchStore';
    import { getSearchArticleType } from '@/api/search/index';
    import { replaceGoodWords } from '@/utils/index';
    import { getDay } from '@/utils/index';
    import { searchValue } from '@/components/data';
    const searchStore = useSearchStore();
    import buttonCom from './buttonCom.vue';
    import { urlHandleMethod } from '@/utils/routes';
    import { useRouter } from 'vue-router';
    // 实例化路由
    const router = useRouter();

    const emits = defineEmits(['updateFlag']);

    let beginDate = ref('');
    let endDate = ref('');

    let timeShowText = ref('时间不限');

    let timeShow = ref(false);

    let initData = {
        timeType: '时间不限',
    };

    // 整个 搜索条件 对象
    let filterInfo = ref(initData);

    // 类型
    let typeTextList = ref<Array<String>>([]);

    // 全部类型复选框
    let checkAll = ref(true);
    // 全部类型复选框的状态
    let isIndeterminate = ref(false);

    // 确认的按钮状态
    let buttonStatus = ref(false);

    // 类型组选中的数据
    let checkedTypes = ref<Array<String>>([]);

    // 控制其他条件搜索的出现的数组对照
    let lastCheckedTypes = ref<Array<String>>([]);

    onMounted(() => {
        // 类型获取
        getInitTypes();
    });

    let typesList = ref([]);
    let sliceNumber = ref<number>(0);

    // 请求 可选择类型的接口
    async function getInitTypes() {
        let result = await getSearchArticleType();
        typesList.value = await replaceGoodWords(result.data);
        typeTextList.value = typesList.value?.map((item) => item.dataName);
        checkedTypes.value = typeTextList.value;
        lastCheckedTypes.value = typeTextList.value;
        sliceNumber.value = 7;
    }

    // 点击收回
    function handlerClickReduce(type) {
        if (type == 'more') {
            sliceNumber.value = typesList.value.length;
        } else {
            sliceNumber.value = 7;
        }
    }

    // 点击全部类型
    async function handleCheckAllChange(val?: boolean) {
        // let newName: Array<String> = [];
        let newTypeName: Array<String> = [];
        await typesList.value.map((item) => {
            // newName.push(item.dataIndexName);
            newTypeName.push(item.dataName);
        });
        checkedTypes.value = val ? newTypeName : [];
        isIndeterminate.value = false;
        buttonStatus.value = val ? false : true;
        handlerResetValue();

        lastCheckedTypes.value = checkedTypes.value;
        // typeShow.value = val? true : false;
    }

    // 类型组选择数据 的操作
    async function handleCheckedTypesChange(value?: string[]) {
        const checkedCount: number = value?.length || 0;
        checkAll.value = checkedCount === typesList.value.length;
        isIndeterminate.value = checkedCount > 0 && checkedCount < typesList.value.length;
        buttonStatus.value = checkedCount > 0 ? false : true;
        // typeShow.value = checkedCount == 0 ? false : true;
        // 选择的数据 将字段值改为中文显示
        // let nameList: Array<String> = [];
        // await value?.map(async (item) => {
        //     await typesList.value?.map((item1) => {
        //         if (item === item1.dataIndexName) {
        //             nameList.push(item1.dataName);
        //         }
        //     });
        // });

        // checkedTypes.value = nameList;
        handlerResetValue();

        lastCheckedTypes.value = checkedTypes.value;
    }

    let sortTextName = ref('默认排序');
    let sortName = ref('综合排序');
    // 排序方式按钮点击
    function handlerSortChange(type) {
        switch (type) {
            case '默认排序':
                sortName.value = '综合排序';
                break;
            case '热度排序':
                sortName.value = '热度排序';
                break;
            case '时间正序':
                sortName.value = '时间正序';
                break;
            case '时间倒序':
                sortName.value = '时间倒序';
                break;
            default:
                break;
        }
        sortTextName.value = type;
        searchStore.$patch({
            searchFilterInfo: {
                sortStr: sortName.value,
            },
        });
    }

    // 点击时间的 几种字段
    function handlerTimeText(type) {
        timeShow.value = false;
        var date = new Date();
        var year = date.getFullYear();
        var month = date.getMonth() + 1;
        var day = date.getDate();
        var month2 = '';
        var day2 = '';
        if (month >= 1 && month <= 9) {
            month2 = '0' + month;
        } else {
            month2 = '' + month;
        }
        if (day >= 1 && day <= 9) {
            day2 = '0' + day;
        } else {
            day2 = '' + day;
        }
        var d2 = year + '-' + month2 + '-' + day2;
        switch (type) {
            case '时间不限':
                filterInfo.value.timeType = '';
                timeShowText.value = '时间不限';
                break;
            case '一年内':
                var d1 = getDay(-365) + '-01';
                filterInfo.value.timeType = d1 + ' - ' + d2;
                timeShowText.value = '一年内';
                break;
            case '一月内':
                var d1 = getDay(-30) + '-01';
                filterInfo.value.timeType = d1 + ' - ' + d2;
                timeShowText.value = '一月内';
                break;
            case '一周内':
                var d1 = getDay(-7) + '-01';
                filterInfo.value.timeType = d1 + ' - ' + d2;
                timeShowText.value = '一周内';
                break;
            case 'time':
                timeShowText.value = 'time';
            default:
                break;
        }

        if (type !== 'time') {
            endDate.value = '';
            beginDate.value = '';
        }
    }

    // 时间组件改变时间数值
    function handlerTimeChange() {
        timeShowText.value = 'time';
        if (beginDate.value || endDate.value) {
            // 时间大小的比较
            if (beginDate.value > endDate.value) {
                filterInfo.value.timeType = `${endDate.value}至${beginDate.value}`;
            } else {
                filterInfo.value.timeType = `${beginDate.value}至${endDate.value}`;
            }
        } else {
            filterInfo.value.timeType = '';
        }
    }

    // 搜索
    async function handlerSearch() {
        if (timeShowText.value == 'time' && !beginDate.value) {
            ElMessage({
                message: '请输入开始时间',
                type: 'error',
                duration: 3 * 1000,
            });
            return;
        } else if (timeShowText.value == 'time' && !endDate.value) {
            ElMessage({
                message: '请输入结束时间',
                type: 'error',
                duration: 3 * 1000,
            });
            return;
        } else if (!checkedTypes.value.length) {
            ElMessage({
                message: '请至少选择一个类型搜索',
                type: 'error',
                duration: 3 * 1000,
            });
        } else {
            // 得到filterInfo对象有值的字段
            // fieldsList

            // const filteredInfo = filterObject(filterInfo.value);
            // const uniqueArr = uniqueArray(fieldsList.value);

            // let fields: any = [];
            // for (let key in filteredInfo) {
            //     await uniqueArr?.map((item) => {
            //         if (item.name == key) {
            //             fields.push({ content: filteredInfo[key], searchType: item.searchType, name: item.name });
            //         }
            //     });
            // }

            urlHandleMethod(searchValue.value, router);

            searchStore.$patch({
                searchFilterInfo: Object.assign({}, searchStore.$state.searchFilterInfo, {
                    dataType: checkedTypes.value,
                    timeType: filterInfo.value.timeType,
                    // fields,
                    searchContent: searchValue.value || null,
                }),
            });
            emits('updateFlag', false);
        }
    }

    // 过滤对象中为null的值
    // const filterObject = (obj) => {
    //     return Object.keys(obj).reduce((acc, key) => {
    //         if (obj[key] !== null && key !== 'timeType') {
    //             acc[key] = obj[key];
    //         }
    //         return acc;
    //     }, {});
    // };

    // 数组去重
    // const uniqueArray = (arr) => {
    //     return arr.reduce((acc, current) => {
    //         const x = acc.find((item) => item.name === current.name);
    //         if (!x) {
    //             return acc.concat([current]);
    //         } else {
    //             return acc;
    //         }
    //     }, []);
    // };

    // 重置
    function handlerReset() {
        lastCheckedTypes.value = typeTextList.value;
        handleCheckAllChange(true);
        checkAll.value = true;
        handlerResetValue();
    }

    function handlerResetValue() {
        timeShowText.value = '时间不限';
        beginDate.value = '';
        endDate.value = '';
        filterInfo.value = {
            timeType: '时间不限',
        };
    }
</script>

<style lang="scss" scoped>
    @import '@/theme/global.scss';
    .file-content {
        height: 100%;
        .content-top {
            width: 100%;
            height: 110px;
            box-shadow: $boxShadow;
            border-radius: 4px;
            background-color: var(--el-bg-color);
            margin-bottom: 15px;
        }
        .content-bottom {
            width: 100%;
            height: calc(100% - 110px - 15px);
            box-shadow: $boxShadow;
            border-radius: 4px;
            background-color: var(--el-bg-color);
        }
        .content-title {
            height: 50px;
            line-height: 50px;
            color: #606266;
            font-size: 16px;
            padding-left: 18px;
            box-sizing: border-box;
            border-bottom: 1px solid #e4e7ed;
        }
        .button-group {
            display: flex;
            height: 62px;
            align-items: center;
            border-bottom: 1px solid #e4e7ed;
            box-sizing: border-box;
        }
        .form-item {
            min-height: 35px;
            display: flex;
            align-items: center;
            margin: 12px 0;
            line-height: 18px;
            .item-title {
                width: 60px;
                // height: 35px;
                // line-height: 35px;
                text-align: center;
                margin-left: 18px;
                margin-right: 12px;
                color: #333;
                font-size: 14px;
            }
            .item-border {
                border: 1px solid #c0c4cc;
                border-radius: 8px;
            }
            .item-content {
                width: calc(100% - 60px - 18px - 12px - 25px);
                cursor: pointer;
                display: flex;
                margin-right: 25px;
                :deep(.el-input__wrapper) {
                    border-radius: 8px;
                    box-shadow: none;
                }
                .type-select {
                    margin-top: -6px;
                    :deep(.el-checkbox) {
                        margin-right: 10px;
                        .el-checkbox__label {
                            padding-left: 3px;
                        }
                    }
                    .select-btn {
                        font-size: 14px;
                        .btn-style {
                            display: flex;
                            align-items: center;
                            margin-top: 5px;
                            color: var(--el-color-primary);
                            i {
                                margin-right: 2px;
                                font-size: 16px;
                            }
                        }
                    }
                }
            }
        }
    }

    :global(.el-drawer__body) {
        padding: 20px 2px;
    }

    :deep(.el-input) {
        --el-select-input-focus-border-color: var(--el-bg-color);
    }

    .date-select {
        padding: 0 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-top: 11px;
        .select-item {
            display: flex;
            align-items: center;
            font-size: 14px;
            margin-bottom: 10px;
            width: 145px;
            > span {
                margin-right: 10px;
                width: 20px;
            }
            :deep(.el-input__wrapper) {
                border: 1px solid #c0c4cc;
                border-radius: 4px !important;
                .el-input__prefix {
                    display: none;
                }
                .el-input__suffix-inner {
                    width: 10px;
                    height: 28px;
                }
            }
        }
    }
</style>
