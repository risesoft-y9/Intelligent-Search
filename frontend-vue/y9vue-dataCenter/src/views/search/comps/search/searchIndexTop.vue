<!--
 * 搜索首页的头部
-->

<template>
    <div class="search-index-top">
        <el-row style="width: calc(100% - 60px); margin: 0 30px; position: initial">
            <el-col :span="2"></el-col>
            <el-col :span="20">
                <div class="search-left">
                    <!-- 图标 -->
                    <div class="title-row">
                        <img src="@/assets/images/logo.png" />
                    </div>
                    <div class="search-left-content">
                        <!-- 搜索组件 -->
                        <y9Search :list="assortList" :deleteBtn="true" />
                        <!-- 搜索结果 -->
                        <div class="search-num">
                            <span></span>
                            <span
                                >当前总数据量为{{ searchStore.getTotalNumber }}条，为您找到相关结果{{
                                    searchStore.getRelationNumber
                                }}条。</span
                            >
                        </div>
                    </div>
                </div>
            </el-col>
            <el-col :span="2"></el-col>
        </el-row>

        <!-- 用户名 -->
        <!-- <div class="search-right">
            <span>开发工程师</span>	
            <span></span>
            <span>张伟</span> 
        </div> -->
    </div>
</template>
<script lang="ts" setup>
    import { onMounted, ref } from 'vue';
    import { useSearchStore } from '@/store/modules/searchStore';

    const searchStore = useSearchStore();

    // 搜索组件
    // let assortList = ['精准版', '智能版'];
    let assortList = ['精准版'];
</script>
<style scoped lang="scss">
    .search-index-top {
        width: 100%;
        height: 180px;
        background-image: url(@/assets/images/searchIndexTopBg.jpg);
        background-size: cover;
        display: flex;
        align-items: center;
        justify-content: space-between;
        position: relative;

        .search-left {
            display: flex;
            align-items: center;
            width: 100%;
            .title-row {
                margin-right: 15px;
                display: flex;
                align-items: center;
                width: 400px;
                justify-content: center;
                img {
                    width: 283px;
                    height: 51px;
                }
                .water-text {
                    display: flex;
                    flex-direction: column;
                    margin-left: 20px;
                    color: var(--el-color-primary);
                    font-size: 14px;
                    span:nth-child(1) {
                        letter-spacing: 1px;
                        font-size: 18px;
                        margin-bottom: 10px;
                    }
                }
            }
            .search-left-content {
                width: calc(100% - 400px - 15px);
                margin-bottom: 15px;
                :deep(.el-input) {
                    width: 100%;
                }
                .search-num {
                    display: flex;
                    align-items: center;
                    font-size: 14px;
                    position: absolute;
                    bottom: 12px;
                    span:nth-child(1) {
                        width: 5px;
                        height: 5px;
                        background-color: #777;
                        margin: 0 5px 3px 0;
                    }
                    span:nth-child(2) {
                        height: 20px;
                        color: #777;
                    }
                }
            }
        }
        .search-right {
            // margin-right: 3%;
            margin-right: 45px;
            font-size: 14px;
            color: #666;
            font-weight: 600;
            span:nth-child(2) {
                display: inline-block;
                width: 2px;
                height: 11px;
                margin: 0 8px;
                background-color: #666;
            }
        }
    }
</style>
