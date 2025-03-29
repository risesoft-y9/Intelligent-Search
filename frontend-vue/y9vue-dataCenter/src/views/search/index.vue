<!--
 * @Author: chensiwen cikl777@163.com
 * @Date: 2024-10-12 15:59:48
 * @LastEditors: chensiwen cikl777@163.com
 * @LastEditTime: 2024-12-17 10:46:31
 * @FilePath: \y9vue-dataCenter\src\views\search\index.vue
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
<template>
    <searchHome v-if="showHome"></searchHome>
    <searchIndex v-else></searchIndex>
</template>

<script lang="ts" setup>
    import { useRoute } from 'vue-router';
    import searchHome from '@/views/search/searchHome.vue';
    import searchIndex from '@/views/search/searchIndex.vue';
    import { useSearchStore } from '@/store/modules/searchStore';
    import { onMounted, ref, watch, onUnmounted } from 'vue';
    import y9_storage from '@/utils/storage';
    // 实例化路由
    const route = useRoute();
    const searchStore = useSearchStore();

    // 定义变量
    let showHome = ref(searchStore.getShowHome);

    watch(
        () => searchStore.getShowHome,
        (newVal) => {
            showHome.value = newVal;
        }
    );

    onMounted(() => {
        window.addEventListener('popstate', onPopState);
    });

    onUnmounted(() => {
        window.removeEventListener('popstate', onPopState);
    });

    function onPopState({ state }) {
        // 刷新
        location.reload();
    }
</script>
