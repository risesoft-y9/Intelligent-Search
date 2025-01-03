<template>
    <div class="search-index">
        <!-- 头部 -->
        <searchIndexTop />
        <!-- 内容区域 -->
        <div class="search-content">
            <div class="content-data">
                <el-row>
                    <el-col :span="2"></el-col>
                    <el-col :span="20">
                        <div class="content-row">
                            <div class="content-filter">
                                <!-- 筛选条件 -->
                                <FileFiltering />
                            </div>
                            <div class="content-right">
                                <div class="search-data" id="top-data">
                                    <!-- 搜索结果 -->
                                    <!-- <div class="search-num">
                                        <span></span>
                                        <span
                                            >当前总数据量为{{ totalNumber }}条，为您找到相关结果{{
                                                relationNumber
                                            }}条。</span
                                        >
                                    </div> -->
                                </div>
                                <div v-loading="listLoading" id="content-data-div">
                                    <searchListData
                                        :dataList="dataList"
                                        @on-currpage="onCurrentPageChange"
                                        @on-pagesize="onPageSizeChange"
                                        :paginationInfo="y9PaginationCircularBgConfig"
                                    ></searchListData>
                                </div>
                            </div>
                        </div>
                    </el-col>
                    <el-col :span="2"></el-col>
                </el-row>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
    import { ref, watch, onMounted } from 'vue';
    import y9_storage from '@/utils/storage';
    import { replaceGoodWords } from '@/utils/index';
    import { useSearchStore } from '@/store/modules/searchStore';
    import { $deeploneObject, $objEqual } from '@/utils/object';
    import { getSearchIndexList, getSearchArticleType } from '@/api/search/index'; //index-APIfox
    import { useRouter } from 'vue-router';
    // 搜索首页头部
    import searchIndexTop from './comps/search/searchIndexTop.vue';
    // 列表组件
    import searchListData from './comps/search/searchListData.vue';
    // 筛选条件
    // 文件筛选
    import FileFiltering from './comps/search/FileFiltering.vue';

    // 实例化路由
    const router = useRouter();
    const searchStore = useSearchStore();

    // 总数据量
    let totalNumber = ref(0);
    // 相关结果
    let relationNumber = ref(0);

    /***end结束 */

    /***
     * 搜索列表
     */
    let listLoading = ref(false);
    let dataList = ref([]);

    // 分页
    //有背景色的圆形分页
    let y9PaginationCircularBgConfig = ref({
        customStyle: 'circular',
        background: true, //是否显示背景色
        hideOnSinglePage: false, //只有一页时是否隐藏
        layout: 'prev, pager, next,sizes', //布局
        currentPage: 1, //当前页数，支持 v-model 双向绑定
        pageSize: 10, //每页显示条目个数，支持 v-model 双向绑定
        total: 0, //总条目数
        pagerCount: 9,
        pageSizeOpts: [5, 10, 20, 30], //每页显示个数选择器的选项设置
    });

    // 搜索条件的监听
    watch(
        () => searchStore.getSearchFilterInfo,
        (newVal) => {
            // 如果有类型数据就不用请求 没有就请求
            if (newVal.dataType.length == 0) {
                getInitTypes();
                return;
            }
            // newVal 过滤对象
            let value = newVal?.searchContent?.split(' ');
            y9PaginationCircularBgConfig.value.currentPage = 1;
            y9PaginationCircularBgConfig.value.pageSize = 10;
            initSearchList(value, newVal);
        },
        {
            immediate: true,
            deep: true,
        }
    );

    // 请求 可选择类型的接口
    async function getInitTypes() {
        let result = await getSearchArticleType();
        let nameList = await replaceGoodWords(result.data);
        // 根据nonAllSelectedList来判断传递的类型数据
        let dataTypeList: Array<String> = [];
        dataTypeList = nameList?.map((item) => item.dataName);
        searchStore.$patch({
            searchFilterInfo: {
                dataType: dataTypeList,
            },
        });
    }

    // 搜索列表 init
    async function initSearchList(newVal?, params?) {
        listLoading.value = true;
        let info = $deeploneObject(params);
        // 参数
        let paramsInfo = Object.assign(info, {
            // 年度范围
            timeType: params.timeType === '时间不限' ? '' : params.timeType?.replace('至', ' - '),
            // 类型
            dataType: params.dataType?.join(','),
            // 页数
            page: y9PaginationCircularBgConfig.value.currentPage,
            rows: y9PaginationCircularBgConfig.value.pageSize,
        });

        // 请求接口返回数据
        await getSearchIndexList(paramsInfo)
            .then(async (res) => {
                if (res.code == 0) {
                    dataList.value = await replaceGoodWords(res.data.dataList);
                    totalNumber.value = res.data.countAll;
                    searchStore.setTotalNumber(res.data.countAll);
                    searchStore.setRelationNumber(res.data.total);
                    relationNumber.value = res.data.total;
                    // 分页赋值
                    y9PaginationCircularBgConfig.value.total = res.data.total;
                }
                ElNotification({
                    title: res.code == 0 ? '成功' : '失败',
                    message: res.msg,
                    type: res.code == 0 ? 'success' : 'error',
                    duration: 2000,
                    offset: 80,
                });
            })
            .catch((err) => console.log(err, '接口返回错误'));
        //console.log("res==2222")
        listLoading.value = false;
        //  深拷贝数组 $deeploneObject
        let deepDataList = $deeploneObject(dataList.value) || [];

        // 搜索结果符合搜索词变颜色
        if (newVal?.length) {
            await deepDataList?.map(async (item) => {
                await newVal?.map((value) => {
                    if (value) {
                        const reg = new RegExp(`${value}`, 'g');
                        // 标题
                        if (item?.title?.includes(value)) {
                            item.title = item.title.replace(reg, `<span style="color: red;">${value}</span>`);
                        }
                        // content
                        if (item?.content?.includes(value)) {
                            item.content = item.content.replace(reg, `<span style="color: red;">${value}</span>`);
                        }
                    }
                });
            });
        }

        dataList.value = deepDataList;

        const contentDataDivDom = document.getElementById('content-data-div');
        contentDataDivDom.scrollTop = 0;
    }

    // 分页 操作 换页数 或每页个数
    function onCurrentPageChange(currPage) {
        y9PaginationCircularBgConfig.value.currentPage = currPage;
        // 请求接口
        initSearchList(searchStore.searchFilterInfo.searchContent?.split(' '), searchStore.searchFilterInfo);
    }

    function onPageSizeChange(pageSize) {
        y9PaginationCircularBgConfig.value.pageSize = pageSize;
        // 请求接口
        initSearchList(searchStore.searchFilterInfo.searchContent?.split(' '), searchStore.searchFilterInfo);
    }

    /***end结束 */

    /***
     * 机器人
     */
    function handleGoRobot() {
        router.push('/robot');
    }
</script>
<style scoped lang="scss">
    @import '@/theme/global.scss';
    .search-index {
        min-width: 1264px;
        .search-content {
            height: calc(100vh - 180px);
            // width: 100%;
            width: 100vw;
            background-color: var(--el-color-primary-light-9);
            // overflow: auto;
            .content-data {
                height: 100%;
                margin: 0 30px;
            }
            .content-row {
                display: flex;
                width: 100%;
                height: 100%;
                .content-filter {
                    width: 400px;
                    height: calc(100% - 35px - 15px);
                    margin-right: 15px;
                    margin-top: 30px;
                }
                .content-right {
                    width: calc(100% - 400px - 15px);
                    height: 100%;
                }
            }
            .search-list {
                margin-bottom: 30px;
            }
            :deep(.y9-pagination) {
                margin-bottom: 15px;
                .custom-circular {
                    button,
                    ul,
                    li {
                        background-color: #ddd;
                    }
                }
            }
            .search-data {
                color: #999;
                font-size: 14px;
                height: 30px;
                // display: flex;
                // align-items: center;
                .search-num {
                    display: flex;
                    align-items: center;
                    span:nth-child(1) {
                        width: 5px;
                        height: 5px;
                        background-color: #999;
                        margin-right: 5px;
                    }
                    span:nth-child(2) {
                        height: 20px;
                    }
                }
                .search-word {
                    display: flex;
                    width: 100%;
                    // height: 45px;
                    align-items: center;
                    margin-top: 5px;
                    .word-title {
                        display: flex;
                        align-items: center;
                        color: var(--el-color-primary);
                        font-weight: 700;
                        width: 112px;
                        > i {
                            margin: 2px 5px 0 0;
                            cursor: pointer;
                        }
                        > span {
                            letter-spacing: 1px;
                        }
                    }
                    .word-content {
                        display: flex;
                        flex-wrap: wrap;
                        width: calc(100% - 112px);
                        .content-item {
                            // width: 65px;
                            // height: 25px;
                            border-radius: 15px;
                            background-color: var(--el-bg-color);
                            margin: 0 7px;
                            text-align: center;
                            line-height: 23px;
                            color: #777;
                            cursor: pointer;
                            padding: 2px 12px;
                            margin: 5px 5px 5px 0;
                            color: var(--el-color-primary-light-3);
                        }
                    }
                    .word-none {
                        color: var(--el-color-primary-light-5);
                        margin: 10px 0;
                    }
                }
            }
            .search-key-word {
                background-color: #fff;
                padding: 15px 20px;
                box-shadow: $boxShadow;
                color: #555;
                height: 270px;
                box-sizing: border-box;
                border-radius: 5px;
                margin-bottom: 15px;
                > span {
                    color: #666;
                    font-weight: 600;
                    font-size: 16px;
                }
                .word-content {
                    margin-top: 15px;
                    span {
                        display: inline-block;
                        border: 1px solid var(--el-color-primary);
                        padding: 2px 18px;
                        color: var(--el-color-primary);
                        margin: 0 15px 15px 0;
                        border-radius: 5px;
                        font-size: 14px;
                    }
                }
            }
            .search-hot-file {
                background-color: #fff;
                padding: 15px 20px;
                box-shadow: $boxShadow;
                color: #555;
                // height: calc(100% - 318px);
                height: calc(100% - 35px);
                overflow: hidden;
                margin-bottom: 15px;
                border-radius: 5px;
                > span {
                    color: #666;
                    font-weight: 600;
                    font-size: 16px;
                }
                .file-list {
                    margin-top: 18px;
                    height: 100%;
                    overflow: auto;
                    .file-list-item {
                        display: flex;
                        align-items: center;
                        margin-bottom: 20px;
                        .list-icon {
                            display: inline-block;
                            font-size: 14px;
                            width: 14px;
                            text-align: center;
                            margin-right: 10px;
                            color: #999;
                            i {
                                display: inline-block;
                                transform: rotate(180deg);
                            }
                        }
                        > span {
                            color: var(--el-color-primary);
                            font-size: 14px;
                            width: 90%;
                            cursor: pointer;
                            @include textEllipsis;
                        }
                    }
                    .file-list-item:nth-child(1),
                    .file-list-item:nth-child(2) {
                        .list-icon {
                            color: #f57f17;
                        }
                    }
                    .file-list-item:nth-child(3) {
                        .list-icon {
                            color: #f9a825;
                        }
                    }
                    .file-list-item:nth-child(4) {
                        .list-icon {
                            color: #fbc02d;
                        }
                    }
                }
            }
            :deep(.el-row) {
                height: calc(100vh - 180px);
                min-width: 700px;
                .el-col {
                    height: 100%;
                }
            }
        }
    }

    #content-data-div {
        height: calc(100% - 35px);
        overflow: auto;
        -ms-overflow-style: none;
        overflow: -moz-scrollbars-none;
        scrollbar-width: none;
    }
    #content-data-div::-webkit-scrollbar {
        width: 0 !important;
    }
    .service-init {
        position: fixed;
        right: 1%;
        top: 50%;
        width: 90px;
        height: 100px;
        img {
            width: 90px;
            height: 100px;
            cursor: pointer;
        }
    }

    .stage {
        display: inline-block;
        font-size: 0; /* 消除默认行高和间距 */
        .dot {
            display: inline-block;
            width: 5px; /* 点的宽度 */
            height: 5px; /* 点的高度 */
            margin: 0 3px; /* 点之间的间距 */
            // background-color: var(--el-color-primary-light-5); /* 点的颜色 */
            background-color: #777;
            border-radius: 50%; /* 设置为圆形 */
            animation: blink 1s infinite; /* 应用动画 */
        }
        .dot:nth-child(1) {
            animation-delay: 0s; /* 第一个点没有延迟 */
        }

        .dot:nth-child(2) {
            animation-delay: 0.33s; /* 第二个点延迟1/3秒 */
        }

        .dot:nth-child(3) {
            animation-delay: 0.66s; /* 第三个点延迟2/3秒 */
        }
    }
    @keyframes blink {
        0%,
        100% {
            opacity: 0.5; /* 初始和结束时的不透明度 */
        }
        50% {
            opacity: 1; /* 中间时的不透明度 */
        }
    }
</style>
