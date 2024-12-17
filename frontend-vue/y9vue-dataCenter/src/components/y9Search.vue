<!--
 * 搜索组件  防抖和请求接口
-->

<template>
    <div class="y9-search">
        <div class="y9-search-tabs">
            <div
                v-for="(item, index) in list"
                :key="index"
                class="y9-search-tabs-item"
                :class="{ 'y9-search-tabs-item-active': index === activeIndex }"
                @click="handlerClickTab(index)"
            >
                {{ item }}
            </div>
        </div>

        <div class="search-div">
            <el-input v-model="searchValue" id="searchInput" @input="handlerInput" autofocus>
                <template #suffix>
                    <i v-if="showDeleteBtn" class="ri-close-line" @click="handlerDeleteValue"></i>
                    <i class="ri-search-2-line" @click="handlerClick"></i>
                </template>
            </el-input>
            <div id="search-history" class="search-history" v-show="historyList.length > 0 && associationalWord">
                <div
                    class="history-item"
                    v-for="(item, index) in historyList"
                    :key="index"
                    @click="handlerClickHistoryKey(item)"
                >
                    {{ item }}
                </div>
            </div>
        </div>
    </div>
</template>
<script lang="ts" setup>
    import { ref, watch, onMounted } from 'vue';
    import { searchValue } from './data';
    import { useSearchStore } from '@/store/modules/searchStore';
    import { searchHistoryList } from '@/api/search/index';
    import { replaceGoodWords } from '@/utils/index';
    import { urlHandleMethod } from '@/utils/routes';
    import { useRouter } from 'vue-router';
    // 实例化路由
    const router = useRouter();
    const searchStore = useSearchStore();

    // active值，默认第一个
    let activeIndex = ref(0);
    // 删除按钮 出现的flag
    let showDeleteBtn = ref(false);

    // 控制联想词出现的变量
    let associationalWord = ref(false);

    let props = defineProps({
        // 搜索分类的数组
        list: {
            type: Array,
            default: () => [],
        },
        // 是否显示input框的删除按钮
        deleteBtn: {
            type: Boolean,
            default: false,
        },
    });

    if (searchStore.searchFilterInfo.searchContent && props.deleteBtn) {
        showDeleteBtn.value = true;
    }

    //历史搜索列表数据
    const historyList = ref([]);
    //是否请求历史搜索列表
    let isPostHistoryList = true;

    onMounted(() => {
        // 初次进入搜索页面 请求接口
        if (searchValue.value) {
            getHistoryList();
            associationalWord.value = false;
        }
    });

    // 监听输入框事件
    let timer = null;
    watch(
        () => searchValue.value,
        (newSearchVal) => {
            clearTimeout(timer);
            timer = setTimeout(async () => {
                if (newSearchVal) {
                    handlerInput();
                    if (isPostHistoryList) {
                        getHistoryList();
                        associationalWord.value = true;
                    } else {
                        isPostHistoryList = true;
                    }
                } else {
                    associationalWord.value = false;
                }
            }, 500);
        }
    );

    // 监听store的搜索值
    watch(
        () => searchStore.searchFilterInfo.searchContent,
        (newVal) => {
            searchValue.value = newVal;
        },
        {
            immediate: true,
        }
    );

    // 点击搜索tab分类
    function handlerClickTab(index) {
        activeIndex.value = index;
        searchStore.$patch({
            searchFilterInfo: {
                type: activeIndex.value + 1,
            },
        });
    }

    document.onkeydown = function (event) {
        if (event.keyCode == 13) {
            // 看是否是键盘enter还是防抖搜索
            // 键盘enter
            handlerClick();
        }
    };

    // 点击搜索按钮
    function handlerClick() {
        urlHandleMethod(searchValue.value, router);

        searchStore.$patch({
            showHome: false,
        });

        searchStore.$patch({
            searchFilterInfo: {
                searchContent: searchValue.value || null,
            },
        });

        associationalWord.value = false;
    }

    // input focus
    function handlerInput() {
        if (props.deleteBtn && searchValue.value) {
            showDeleteBtn.value = true;
        } else {
            showDeleteBtn.value = false;
        }
    }

    // 点击删除按钮
    function handlerDeleteValue() {
        searchValue.value = '';
    }

    //获取搜索历史列表
    async function getHistoryList() {
        const sendData = {
            keyword: searchValue.value,
        };
        const res = await searchHistoryList(sendData);

        historyList.value = (await replaceGoodWords(res.data)) || [];
    }

    //点击搜索历史列表项
    function handlerClickHistoryKey(item) {
        isPostHistoryList = false;
        searchValue.value = item;
        // 如果是首页就不请求接口
        if (!searchStore.showHome) getHistoryList();
        handlerClick();
    }

    //点击历史列表以外的地方，隐藏搜索历史列表
    const hiddenSearchHistory = (e) => {
        let div = document.getElementById('search-history');
        let searchDiv = document.getElementById('searchInput');
        // 点击除历史列表或搜索框以外的地方，历史列表隐藏
        if (div && searchDiv && !div.contains(e.target) && !searchDiv.contains(e.target)) {
            associationalWord.value = false;
        }
        // 点击搜索，如果有搜索值出现历史列表
        if (searchDiv && searchDiv.contains(e.target)) {
            if (searchValue.value) {
                associationalWord.value = true;
            }
        }
    };
    onMounted(() => {
        document.addEventListener('click', hiddenSearchHistory);
    });
    onBeforeUnmount(() => {
        document.removeEventListener('click', hiddenSearchHistory);
    });
</script>
<style scoped lang="scss">
    @import '@/theme/global.scss';
    .y9-search {
        position: relative;
        .y9-search-tabs {
            display: flex;
            .y9-search-tabs-item {
                width: 90px;
                height: 42px;
                line-height: 42px;
                background-color: #efefef;
                border-radius: 5px 5px 0px 0px;
                text-align: center;
                border-bottom: none;
                cursor: pointer;
                user-select: none;
                color: #999;
                box-shadow: $boxShadow;
            }
            .y9-search-tabs-item-active {
                background-color: var(--el-color-white);
                width: 95px;
                box-shadow: 2px -2px 2px 1px rgba(0, 0, 0, 0.06);
                z-index: 1;
                color: #666;
            }
        }

        .search-div {
            width: 100%;
            min-width: 585px;
            border-radius: v-bind("historyList.length > 0 && associationalWord ? '0px 10px 0px 0px' : '10px'");
            background-color: var(--el-color-white);
            box-shadow: 0 0 0 1px var(--el-input-bg-color) inset;
            position: relative;
            :deep(.el-input) {
                height: 50px;

                .el-input__inner {
                    font-size: 16px;
                }
                .el-input__wrapper {
                    border-radius: 0px 10px 10px;
                    box-shadow: 0 0 0 1px var(--el-input-bg-color) inset;
                    padding-left: 20px;
                }
                .ri-search-2-line,
                .ri-close-line {
                    font-size: 30px;
                    margin-right: 10px;
                    cursor: pointer;
                }
                .ri-close-line {
                    font-size: 36px;
                }
                .el-input__wrapper.is-focus {
                    box-shadow: 0 0 0 1px var(--el-input-bg-color) inset;
                }
            }
            .search-history {
                position: absolute;
                left: 0px;
                right: 0px;
                top: 100%;
                z-index: 100;
                border-radius: 0px 0px 10px 10px;
                box-shadow: $boxShadow;
                background-color: var(--el-color-white);
                .history-item {
                    padding: 0 20px;
                    color: #333;
                    font-size: 14px;
                    line-height: 36px;
                    &:first-child {
                        border-top: solid 1px #eee;
                    }
                    &:hover {
                        background-color: #eee;
                        color: var(--el-color-primary);
                        cursor: pointer;
                    }
                    &:last-child {
                        border-radius: 0px 10px 10px;
                    }
                }
            }
        }
    }
</style>
