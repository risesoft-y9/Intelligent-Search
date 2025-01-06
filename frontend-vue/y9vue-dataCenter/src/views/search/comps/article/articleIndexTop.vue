<!--
 * @Author: chensiwen cikl777@163.com
 * @Date: 2024-12-17 17:07:03
 * @LastEditors: chensiwen cikl777@163.com
 * @LastEditTime: 2025-01-06 11:54:25
 * @FilePath: \y9vue-dataCenter\src\views\search\comps\article\articleIndexTop.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<!--
 *  具体文本页 头部
-->

<template>
    <div class="article-top">
        <div class="top-left">
            <img src="@/assets/images/logo.png" />
        </div>
        <div class="top-right">
            <el-avatar :size="33" :src="userInfo.avator" />
            <span>{{ userInfo.userName }}</span>
        </div>
    </div>
</template>
<script lang="ts" setup>
    import { onMounted, ref } from 'vue';
    import y9_storage from '@/utils/storage';

    interface userType {
        userName?: string;
        avator?: string;
    }

    const userInfo = ref<userType>({
        avator: 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
    });

    onMounted(() => {
        getUserInfo();
    });

    function getUserInfo() {
        const y9UserInfo = y9_storage.getObjectItem('ssoUserInfo');
        userInfo.value = {
            userName: y9UserInfo.name,
            avator: y9UserInfo.avator || 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png',
        };
    }
</script>
<style scoped lang="scss">
    @import '@/theme/global.scss';
    .article-top {
        display: flex;
        padding: 10px 3% 10px 2%;
        justify-content: space-between;
        align-items: center;
        height: 40px;
        background-color: #fff;
        box-shadow: $boxShadow;
        .top-left {
            display: flex;
            align-items: center;
            img {
                width: 150px;
                height: 34px;
                margin-right: 10px;
            }
            .left-text {
                display: flex;
                flex-direction: column;
                justify-content: flex-start;
                color: var(--el-color-primary);
                font-size: 14px;
                span:nth-child(1) {
                    font-weight: 600;
                    margin-bottom: 5px;
                }
            }
        }
        .top-right {
            font-size: 14px;
            display: flex;
            align-items: center;
            span {
                margin-left: 10px;
            }
        }
    }
</style>
