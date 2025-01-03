<!--
 *  具体文本页
-->

<template>
    <div class="article-index">
        <!-- 头部 -->
        <articleIndexTop />
        <el-row class="article-content">
            <el-col :span="2"> </el-col>
            <!-- 中 -->
            <el-col :span="15">
                <articleIndexContent @getTitle="handlerGetTitle" />
            </el-col>
            <!-- 右 -->
            <el-col :span="5">
                <div class="content-right">
                    <span class="card-title">智能推荐</span>
                    <div v-loading="intelligentLoading" class="content-box">
                        <div v-if="intelligentList?.length > 0">
                            <div v-for="(item, index) in intelligentList" :key="index" class="title-item">
                                <div v-if="getId() != item.id">
                                    <span @click="handlerClickItem(encodeURI(item.id), encodeURI(item.dataType))">
                                        <i class="ri-stop-mini-fill"></i>{{ item.title }}
                                    </span>
                                </div>
                                <div v-if="getId() == item.id && intelligentList?.length == 1">
                                    <img src="@/assets/images/IntelligentRelation.png" alt="智能关联缺省" />
                                </div>
                            </div>
                        </div>
                        <img v-else src="@/assets/images/IntelligentRelation.png" alt="智能关联缺省" />
                    </div>
                </div>
            </el-col>
            <el-col :span="2"> </el-col>
        </el-row>
    </div>
</template>
<script lang="ts" setup>
    import { ref } from 'vue';
    import { useRouter } from 'vue-router';
    import { hotSearchDataList } from '@/api/search/index'; // index-APIfox
    import { replaceGoodWords } from '@/utils/index';
    // 头部
    import articleIndexTop from './comps/article/articleIndexTop.vue';
    // 中心内容
    import articleIndexContent from './comps/article/articleIndexContent.vue';
    // 实例化路由
    const router = useRouter();

    /***
     * 智能推荐
     */
    // 总数组
    let intelligentList = ref([]);
    let intelligentLoading = ref(false);
    // 标题
    let searchTitle = ref('');
    // 类型
    let appNameValue = ref('');

    // 内容
    let searchContent = ref();

    // 当前文档id
    let id = ref('');

    let appNameType = {
        收文: 1,
        局发文: 2,
        便函: 3,
        内部签报: 4,
        接诉即办: 5,
        处发文: 6,
        其他涉水诉求: 7,
        行政许可: 8,
        双随机: 9,
        资讯信息: 10,
    };

    function getId() {
        return id.value;
    }

    function handlerGetTitle(objInfo, keyword) {
        // title是标题 comments是历程列表 dataType是类型
        intelligentLoading.value = true;
        // 赋值
        searchTitle.value = objInfo?.title;

        // 类型
        appNameValue.value = objInfo?.dataType;

        // keyword
        searchContent.value = keyword;

        id.value = objInfo?.id;

        // 请求智能推荐的 接口
        getHotFileListInit();
    }

    // 智能推荐接口
    async function getHotFileListInit() {
        await hotSearchDataList(getId(), searchTitle.value, appNameValue.value)
            .then(async (res) => {
                if (res.code == 0) {
                    intelligentList.value = await replaceGoodWords(res.data.data);
                }
            })
            .catch((err) => console.log(err));

        intelligentLoading.value = false;
    }

    /**结束 */

    /***点击进入详细页面 */
    function handlerClickItem(id, dataType) {
        //跳转路由并打开新窗口
        const routeUrl = router.resolve({
            path: '/article',
            query: { id, dataType },
        });
        window.open(routeUrl.href);
    }
    /**end结束 */
</script>
<style scoped lang="scss">
    @import '@/theme/global.scss';
    .article-index {
        height: 100vh;
        background-color: var(--el-color-primary-light-9);
        min-width: 1264px;
        .article-content {
            margin: 30px;
            .card-title {
                display: inline-block;
                height: 40px;
                width: 100%;
                line-height: 40px;
                text-align: center;
                font-size: 16px;
                color: #666;
                margin-bottom: 10px;
            }
            .content-right {
                background-color: #fff;
                height: calc(100vh - 60px - 60px);
                // overflow: auto;
                box-shadow: $boxShadow;
                padding: 30px;
                box-sizing: border-box;
                border-radius: 5px;
                .content-box {
                    height: calc(100% - 50px);
                    overflow: auto;
                    > img {
                        width: 100%;
                        opacity: 0.7;
                        margin-top: 40px;
                    }
                }
                .title-item {
                    color: #777;
                    font-size: 14px;
                    display: flex;
                    margin-bottom: 10px;
                    cursor: pointer;
                    span {
                        line-height: 22px;
                    }
                    i {
                        font-size: 12px;
                        margin-right: 2px;
                    }
                }
                .title-item:hover {
                    color: var(--el-color-primary);
                }
            }
        }
    }
</style>
