<!--
 * @Author: your name
 * @Date: 2022-01-10 18:09:52
 * @LastEditTime: 2024-11-13 16:58:23
 * @LastEditors: chensiwen cikl777@163.com
 * @Description: 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
 * @FilePath: /sz- team-frontend-9.6.x/y9vue-home/src/App.vue
-->
<script lang="ts" setup>
    import zhCn from 'element-plus/lib/locale/lang/zh-cn';
    import en from 'element-plus/lib/locale/lang/en';
</script>

<template>
    <el-config-provider :locale="zhCn">
        <router-view></router-view>
    </el-config-provider>
</template>
