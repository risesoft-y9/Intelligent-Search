<template>
    <div class="data-center-background" m="t-4">
        <el-carousel trigger="click" arrow="never" :height="height + 'vh'" :interval="interval">
            <el-carousel-item v-for="item in menuBgs" :key="item">
                <img class="bg-image" :src="item" />
            </el-carousel-item>
        </el-carousel>
    </div>
    <div class="login-name">
        <!-- <span>开发工程师</span>	
		<span></span>
		<span>张伟</span>  -->
    </div>
    <div class="search-container">
        <div class="title-row">
            <img src="@/assets/images/logo.png" alt="图标" />
        </div>
        <div class="search-comp">
            <y9Search :list="assortList" />
        </div>
        <!-- <div class="text">
			<span></span>
			<span>当前数据范围为2021年1月1日至2022年10月31日收文，2022年1月日至2022年10月31日发文、便函。</span>
		</div> -->
    </div>
</template>

<script lang="ts" setup>
    import { ref } from 'vue';
    // 轮播时间
    const interval = ref(100000);
    // header高度
    const height = ref(100);
    // 搜索主页背景
    let menuBgs = [
        // new URL('../../assets/images/search_bg1.jpg', import.meta.url).href,
        new URL('../../assets/images/search_bg2.jpg', import.meta.url).href,
        new URL('../../assets/images/search_bg3.jpg', import.meta.url).href,
        new URL('../../assets/images/search_bg4.jpg', import.meta.url).href,
        // new URL('../../assets/images/search_bg5.jpg', import.meta.url).href,
        // new URL('../../assets/images/search_bg6.jpg', import.meta.url).href,
    ];

    // 搜索组件
    //let assortList = ['精准版', '智能版'];
    let assortList = ['精准版'];
</script>

<style lang="scss" scoped>
    .demonstration {
        color: var(--el-text-color-secondary);
    }

    .el-carousel__item:nth-child(2n) {
        background-color: #99a9bf;
    }

    .el-carousel__item:nth-child(2n + 1) {
        background-color: #d3dce6;
    }

    .data-center-background {
        .bg-image {
            min-width: 1000px;
            width: 100%;
            height: 100%;
            filter: brightness(1.08);
            opacity: 0.958;
        }
    }

    :deep(.search-div) {
        width: 80% !important;
    }
    .login-name {
        position: absolute;
        right: 3%;
        top: 3%;
        color: #fff;
        display: flex;
        align-items: center;
        span:nth-child(2) {
            display: inline-block;
            width: 2px;
            height: 14px;
            margin: 0 8px;
            background-color: var(--el-color-white);
        }
    }
    .search-container {
        width: 50%;
        position: absolute;
        top: 18%;
        left: 20%;
        z-index: 1;
        display: flex;
        flex-direction: column;
        color: #fff;
        .title-row {
            display: flex;
            align-items: center;
            margin-bottom: 30px;
            min-width: 600px;
            img {
                width: 275px;
                height: 53px;
            }
            .water-text {
                display: flex;
                flex-direction: column;
                margin-left: 20px;
                span:nth-child(1) {
                    letter-spacing: 2px;
                    font-size: 22px;
                    margin-bottom: 10px;
                }
            }
        }
        .text {
            margin-top: 30px;
            display: flex;
            span:nth-child(1) {
                display: inline-table;
                width: 10px;
                height: 10px;
                border-radius: 50%;
                background-color: #fff;
                margin-left: 10px;
            }
            span:nth-child(2) {
                display: table-cell;
                margin: -6px 0px 0px 8px;
                line-height: 22px;
                font-size: 16px;
                width: 404px;
            }
        }
    }
</style>
