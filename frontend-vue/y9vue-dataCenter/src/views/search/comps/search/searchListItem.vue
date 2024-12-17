<!--
 * 搜索列表 结果的每个item组件
-->

<template>
    <div class="list-item">
        <div class="base-info" @click="handlerClickItem(encodeURI(listItem?.id), encodeURI(listItem?.dataType))">
            <template v-if="listItem?.dataType != null">
                <div class="item-title">
                    <span v-html="listItem?.title == '' || listItem?.title == null ? '无标题' : listItem?.title"></span>
                    <div class="item-span">
                        <span class="triangle"></span>
                        <span>{{ listItem?.dataType }}</span>
                    </div>
                </div>
            </template>
            <template v-if="listItem?.dataType != null">
                <div class="item-contents" v-if="listItem?.content" v-loading="loading">
                    <span v-html="newContent"></span>
                    <span v-if="newContentFlag">...</span>
                </div>
                <div class="item-tips">
                    <span>登记时间:{{ listItem?.dataTime }}</span>
                    <span>浏览次数:{{ listItem?.clickNum }}</span>
                </div>
            </template>
        </div>
    </div>
</template>
<script lang="ts" setup>
    import { watch, ref, onMounted } from 'vue';
    import { useRouter } from 'vue-router';
    import { useSearchStore } from '@/store/modules/searchStore';

    // 实例化路由
    const router = useRouter();

    // let itemInfo = ref({});
    const searchStore = useSearchStore();

    // 截取字段
    let newContent = ref('');
    let newContentFlag = ref(false);

    // 文件能预览的类型总合
    let allFileType = ['doc', 'docx', 'xlsx', 'xls', 'ppt', 'pptx', 'txt', 'wps', 'ofd', 'jpg', 'png', 'pdf'];

    const props = defineProps({
        // 传过来的数据对象
        listItem: {
            type: Object,
            default: () => {},
        },
    });

    let loading = ref(false);

    watch(
        () => props.listItem,
        (newVal) => {
            // 使用定时器 执行content内容的处理函数
            if (newVal?.content) {
                loading.value = true;
                setTimeout(async () => {
                    await handlerClick();
                    loading.value = false;
                }, 500);
            }
        },
        {
            immediate: true,
            deep: true,
        }
    );

    // 点击item 进入收文，发文详细页面 /article
    function handlerClickItem(id, dataType) {
        //跳转路由并打开新窗口
        const routeUrl = router.resolve({
            path: '/article',
            query: { id, dataType, keyword: searchStore.searchFilterInfo.searchContent },
        });
        window.open(routeUrl.href);
    }

    // 截取数据 确保显示一行多 并非两行
    async function handlerClick() {
        let contentElement = document.getElementsByClassName('item-contents');
        if (contentElement) {
            let widthValue = contentElement[0]?.clientWidth;
            let fontInfo = [];
            let contentInfo;
            let value = searchStore.searchFilterInfo.searchContent?.split(' ');
            if (props.listItem?.content?.includes('<span')) {
                // 匹配到的span 标签个数
                contentInfo = props.listItem?.content
                    ?.replace(/<\/?.+?\/?>/g, '')
                    ?.slice(0, Math.ceil(Math.ceil(widthValue / 14) * 1.7));
                await value?.map((value) => {
                    if (value) {
                        const reg = new RegExp(`${value}`, 'g');
                        contentInfo = contentInfo.replace(reg, `<span style="color: red;">${value}</span>`);
                        fontInfo = contentInfo?.match(/<\/span>/g);
                    }
                });
            }
            if (props.listItem?.content?.length > Math.ceil(Math.ceil(widthValue / 14) * 1.7 + 33 * fontInfo?.length)) {
                // 截取数据
                newContentFlag.value = true;
                newContent.value = props.listItem.content?.slice(
                    0,
                    Math.ceil(Math.ceil(widthValue / 14) * 1.7 + 33 * fontInfo?.length)
                );
            } else {
                newContent.value = props.listItem.content;
                if (props.listItem?.content?.length) {
                    newContentFlag.value = false;
                }
            }
        }
    }

    window.addEventListener('resize', function () {
        if (props.listItem?.content) {
            handlerClick();
        }
    });
</script>
<style scoped lang="scss">
    @import '@/theme/global.scss';
    .list-item {
        // background-color: var(--el-bg-color);
        font-size: 15px;
        font-family: 'Microsoft YaHei';
        // box-shadow: $boxShadow;
        // border-radius: 5px;
        margin-bottom: 15px;

        .base-info {
            padding: 15px 10px;
            cursor: pointer;
            position: relative;
            display: flex;
            flex-direction: column;
            justify-content: center;
            .item-title {
                display: flex;
                justify-content: space-between;
                margin-bottom: 15px;
                > span {
                    color: var(--el-color-primary);
                    font-weight: 600;
                    font-size: 16px;
                }
                .item-span {
                    padding: 0 5px;
                    display: flex;
                    margin-right: 2%;
                    flex-shrink: 0;
                    .triangle {
                        display: inline-block;
                        width: 0;
                        height: 0;
                        border: 10px solid transparent;
                        border-right: 13px solid var(--el-color-primary);
                    }
                    span:nth-child(2) {
                        padding: 0px 10px;
                        color: #fff;
                        background-color: var(--el-color-primary);
                        font-size: 12px;
                        height: 20px;
                        line-height: 20px;
                        display: flex;
                        justify-content: center;
                    }
                }
            }
            .item-city {
                margin-bottom: 12px;
                color: #666;
                font-family: initial;
                > span {
                    margin-right: 10px;
                }
            }
            .item-contents {
                font-size: 14px;
                line-height: 20px;
                margin-bottom: 15px;
                color: #606266;
                width: 98%;
            }
            .item-tips {
                color: #909399;
                display: flex;
                align-items: center;
                flex-wrap: wrap;
                font-size: 14px;
                span {
                    margin-right: 5px;
                }
                span:nth-child(1) {
                    display: inline-block;
                    max-width: 180px;
                    @include textEllipsis;
                }
                span:nth-child(3) {
                    display: inline-block;
                    max-width: 200px;
                    @include textEllipsis;
                }
            }
        }
        .file-list {
            padding: 5px 20px;
            border-top: solid 1px #eee;
            font-family: 'Microsoft YaHei';
            .file-list-item {
                margin-bottom: 4px;
                display: flex;
                align-items: flex-start;
                color: var(--el-color-primary-light-3);
                .file-icon {
                    margin-right: 4px;
                    flex-shrink: 0;
                    line-height: 22px;
                }
                .file-content {
                    // margin-bottom: 5px;
                    cursor: pointer;
                    font-size: 14px;
                    line-height: 22px;
                    .search-key-list {
                        display: flex;
                        color: #999;
                        font-size: 12px;
                        .search-key-item {
                            margin-right: 5px;
                            // cursor: pointer;
                        }
                    }
                }
            }
        }
    }
</style>
